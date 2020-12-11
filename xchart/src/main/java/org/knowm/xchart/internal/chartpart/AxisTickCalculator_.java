package org.knowm.xchart.internal.chartpart;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.Format;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.knowm.xchart.internal.Utils;
import org.knowm.xchart.internal.chartpart.Axis.Direction;
import org.knowm.xchart.style.AxesChartStyler;
import org.knowm.xchart.style.CategoryStyler;
import org.knowm.xchart.style.Styler;

/** @author timmolter */
public abstract class AxisTickCalculator_ {

  /** the List of tick label position in pixels */
  final List<Double> tickLocations = new LinkedList<Double>();

  /** the List of tick label values */
  final List<String> tickLabels = new LinkedList<String>();

  final Direction axisDirection;

  final double workingSpace;

  final double minValue;

  final double maxValue;

  List<Double> axisValues;

  final AxesChartStyler styler;

  Format axisFormat;

  /**
   * Constructor
   *
   * @param axisDirection
   * @param workingSpace
   * @param minValue
   * @param maxValue
   * @param styler
   */
  AxisTickCalculator_(
      Direction axisDirection,
      double workingSpace,
      double minValue,
      double maxValue,
      AxesChartStyler styler) {

    this.axisDirection = axisDirection;
    this.workingSpace = workingSpace;
    this.minValue = getAxisMinValue(styler, axisDirection, minValue);
    this.maxValue = getAxisMaxValue(styler, axisDirection, maxValue);
    this.styler = styler;
  }

  AxisTickCalculator_(
      Direction axisDirection,
      double workingSpace,
      double minValue,
      double maxValue,
      List<Double> axisValues,
      AxesChartStyler styler) {
    this.axisDirection = axisDirection;
    this.workingSpace = workingSpace;
    Set<Double> axisValuesWithMinMax = new LinkedHashSet<>();
    axisValuesWithMinMax.add(minValue);
    axisValuesWithMinMax.addAll(axisValues);
    axisValuesWithMinMax.add(maxValue);
    this.axisValues = new ArrayList<>(axisValuesWithMinMax);
    this.minValue = getAxisMinValue(styler, axisDirection, minValue);
    this.maxValue = getAxisMaxValue(styler, axisDirection, maxValue);
    this.styler = styler;
  }

  /**
   * Gets the first position
   *
   * @param gridStep
   * @return
   */
  double getFirstPosition(double gridStep) {

    // System.out.println("******");

    return minValue - (minValue % gridStep) - gridStep;
  }

  public List<Double> getTickLocations() {

    return tickLocations;
  }

  public List<String> getTickLabels() {

    return tickLabels;
  }

  /**
   * Given the generated tickLabels, will they fit side-by-side without overlapping each other and
   * looking bad? Sometimes the given tickSpacingHint is simply too small.
   *
   * @param tickLabels
   * @param tickSpacingHint
   * @return
   */
  boolean willLabelsFitInTickSpaceHint(List<String> tickLabels, int tickSpacingHint) {

    String sampleLabel = "Y";
    if (Direction.X.equals(this.axisDirection)) {
      // find the longest String in all the labels
      for (String tickLabel : tickLabels) {
        if (tickLabel != null && tickLabel.length() > sampleLabel.length()) {
          sampleLabel = tickLabel;
        }
      }
    }
    // System.out.println("longestLabel: " + sampleLabel);

    TextLayout textLayout =
        new TextLayout(
            sampleLabel, styler.getAxisTickLabelsFont(), new FontRenderContext(null, true, false));
    AffineTransform rot =
        styler.getXAxisLabelRotation() == 0
            ? null
            : AffineTransform.getRotateInstance(
                -1 * Math.toRadians(styler.getXAxisLabelRotation()));
    Shape shape = textLayout.getOutline(rot);
    Rectangle2D rectangle = shape.getBounds();
    double largestLabelWidth =
        Direction.X.equals(this.axisDirection) ? rectangle.getWidth() : rectangle.getHeight();
    // System.out.println("largestLabelWidth: " + largestLabelWidth);
    // System.out.println("tickSpacingHint: " + tickSpacingHint);

    // if (largestLabelWidth * 1.1 >= tickSpacingHint) {
    // System.out.println("WILL NOT FIT!!!");
    // }

    return (largestLabelWidth * 1.1 < tickSpacingHint);
  }

  public Format getAxisFormat() {

    return axisFormat;
  }

  protected void calculate() {

    // a check if all axis data are the exact same values
    if (minValue == maxValue) {
      tickLabels.add(getAxisFormat().format(BigDecimal.valueOf(maxValue).doubleValue()));
      tickLocations.add(workingSpace / 2.0);
      return;
    }

    // a check for no data
    if (minValue > maxValue && minValue == Double.MAX_VALUE) {
      tickLabels.add(getAxisFormat().format(0.0));
      tickLocations.add(workingSpace / 2.0);
      return;
    }

    // tick space - a percentage of the working space available for ticks
    double tickSpace = styler.getPlotContentSize() * workingSpace; // in plot space

    // this prevents an infinite loop when the plot gets sized really small.
    if (axisDirection == Direction.X && tickSpace < styler.getXAxisTickMarkSpacingHint()) {
      return;
    }
    if (axisDirection == Direction.Y && tickSpace < styler.getYAxisTickMarkSpacingHint()) {
      return;
    }

    // where the tick should begin in the working space in pixels
    double margin =
        Utils.getTickStartOffset(
            workingSpace,
            tickSpace); // in plot space double gridStep = getGridStepForDecimal(tickSpace);
    // the span of the data
    double span = Math.abs(Math.min((maxValue - minValue), Double.MAX_VALUE - 1)); // in data space

    if (axisValues != null && areValuesEquallySpaced(axisValues)) {
      calculateForEquallySpacedAxisValues(tickSpace, margin);
      return;
    }

    //////////////////////////

    int tickSpacingHint =
        (axisDirection == Direction.X
                ? styler.getXAxisTickMarkSpacingHint()
                : styler.getYAxisTickMarkSpacingHint())
            - 5;

    // for very short plots, squeeze some more ticks in than normal into the Y-Axis
    if (axisDirection == Direction.Y && tickSpace < 160) {
      tickSpacingHint = 25 - 5;
    }

    int gridStepInChartSpace;

    do {

      // System.out.println("calculating ticks...");
      tickLabels.clear();
      tickLocations.clear();

      tickSpacingHint += 5;

      // System.out.println("tickSpacingHint: " + tickSpacingHint);

      // gridStepHint --> significand * 10 ** exponent
      // e.g. 724.1 --> 7.241 * 10 ** 2
      double significand = span / tickSpace * tickSpacingHint;
      int exponent = 0;
      if (significand == 0) {
        exponent = 1;
      } else if (significand < 1) {
        while (significand < 1) {
          significand *= 10.0;
          exponent--;
        }
      } else {
        while (significand >= 10 || significand == Double.NEGATIVE_INFINITY) {
          significand /= 10.0;
          exponent++;
        }
      }

      // calculate the grid step width hint.
      double gridStep;
      if (significand > 7.5) {
        // gridStep = 10.0 * 10 ** exponent
        gridStep = 10.0 * Utils.pow(10, exponent);
      } else if (significand > 3.5) {
        // gridStep = 5.0 * 10 ** exponent
        gridStep = 5.0 * Utils.pow(10, exponent);
      } else if (significand > 1.5) {
        // gridStep = 2.0 * 10 ** exponent
        gridStep = 2.0 * Utils.pow(10, exponent);
      } else {
        // gridStep = 1.0 * 10 ** exponent
        gridStep = Utils.pow(10, exponent);
      }

      //////////////////////////
      // System.out.println("******************");
      // System.out.println("gridStep: " + gridStep);
      // System.out.println("***gridStepInChartSpace: " + gridStep / span * tickSpace);
      gridStepInChartSpace = (int) (gridStep / span * tickSpace);
      // System.out.println("gridStepInChartSpace: " + gridStepInChartSpace);
      BigDecimal gridStepBigDecimal = new BigDecimal(gridStep, MathContext.DECIMAL64);
      // BigDecimal gridStepBigDecimal = BigDecimal.valueOf(gridStep);
      int scale = Math.min(10, gridStepBigDecimal.scale());
      // int scale = gridStepBigDecimal.scale();
      // System.out.println("scale: " + scale);
      // int scale = gridStepBigDecimal.scale();
      BigDecimal cleanedGridStep0 =
          gridStepBigDecimal
              .setScale(scale, RoundingMode.HALF_UP)
              .stripTrailingZeros(); // chop off any double imprecision
      BigDecimal cleanedGridStep =
          cleanedGridStep0
              .setScale(scale, RoundingMode.HALF_DOWN)
              .stripTrailingZeros(); // chop off any double imprecision
      // System.out.println("cleanedGridStep: " + cleanedGridStep);

      BigDecimal firstPosition = null;
      double firstPositionAsDouble = getFirstPosition(cleanedGridStep.doubleValue());
      if (Double.isNaN(firstPositionAsDouble)) {
        // This happens when the data values are almost the same but differ by a very tiny amount.
        // The solution for now is to create a single axis label and tick at the average value
        double averageValue = (maxValue + minValue) / 2.0;
        String formattedTickLabel = Double.isNaN(averageValue)
                ? "NaN"
                : getAxisFormat().format(BigDecimal.valueOf(averageValue));
        tickLabels.add(formattedTickLabel);
        return;
      } else if (firstPositionAsDouble == Double.NEGATIVE_INFINITY) {
        firstPosition = BigDecimal.valueOf(-1 * Double.MAX_VALUE);
      } else {
        try {
          firstPosition = BigDecimal.valueOf(firstPositionAsDouble);
        } catch (java.lang.NumberFormatException e) {

          System.out.println(
              "Some debug stuff. This happens once in a blue moon, and I don't know why.");
          System.out.println("scale: " + scale);
          System.out.println("exponent: " + exponent);
          System.out.println("gridStep: " + gridStep);
          System.out.println("cleanedGridStep: " + cleanedGridStep);
          System.out.println("cleanedGridStep.doubleValue(): " + cleanedGridStep.doubleValue());
          System.out.println(
              "NumberFormatException caused by this number: "
                  + getFirstPosition(cleanedGridStep.doubleValue()));
        }
      }

      // System.out.println("firstPosition: " + firstPosition); // chop off any double imprecision
      BigDecimal cleanedFirstPosition =
          firstPosition
              .setScale(10, RoundingMode.HALF_UP)
              .stripTrailingZeros(); // chop off any double imprecision
      //      System.out.println("cleanedFirstPosition: " + cleanedFirstPosition);

      // generate all tickLabels and tickLocations from the first to last position
      for (BigDecimal value = cleanedFirstPosition;
          value.compareTo(
                  BigDecimal.valueOf(
                      (maxValue + 2 * cleanedGridStep.doubleValue()) == Double.POSITIVE_INFINITY
                          ? Double.MAX_VALUE
                          : maxValue + 2 * cleanedGridStep.doubleValue()))
              < 0;
          value = value.add(cleanedGridStep)) {

        // if (value.compareTo(BigDecimal.valueOf(maxValue)) <= 0 &&
        // value.compareTo(BigDecimal.valueOf(minValue)) >= 0) {
        // System.out.println(value);
        String tickLabel = getAxisFormat().format(value.doubleValue());
        // System.out.println(tickLabel);
        tickLabels.add(tickLabel);

        // here we convert tickPosition finally to plot space, i.e. pixels
        double tickLabelPosition =
            margin + ((value.doubleValue() - minValue) / (maxValue - minValue) * tickSpace);
        tickLocations.add(tickLabelPosition);
        // }

      }
    } while (!areAllTickLabelsUnique(tickLabels)
        || !willLabelsFitInTickSpaceHint(tickLabels, gridStepInChartSpace));
  }

  private boolean areValuesEquallySpaced(List<Double> values) {
    if (values.size() < 2) {
      return false;
    }
    double space = values.get(1) - values.get(0);
    double threshold = .0001;
    if (threshold > Math.abs(maxValue - minValue)) {
      return false;
    }
    return IntStream.range(1, values.size())
        .mapToDouble(i -> values.get(i) - values.get(i - 1))
        .allMatch(x -> Math.abs(x - space) < threshold);
  }

  /**
   * Calculates the ticks so that they only appear at positions where data is available.
   *
   * @param tickSpace a percentage of the working space available for ticks
   * @param margin where the tick should begin in the working space in pixels
   */
  private void calculateForEquallySpacedAxisValues(double tickSpace, double margin) {
    if (axisValues == null) {
      throw new IllegalStateException("No axis values.");
    }
    int gridStepInChartSpace;
    int tickValuesHint = 0;
    List<Double> tickLabelValues;
    double tickLabelMaxValue;
    double tickLabelMinValue;
    do {
      tickValuesHint++;
      tickLabels.clear();
      int finalTickValuesHint = tickValuesHint;
      tickLabelValues =
          IntStream.range(0, axisValues.size())
              .filter(it -> it % finalTickValuesHint == 0)
              .mapToDouble(axisValues::get)
              .boxed()
              .collect(Collectors.toList());
      tickLabelMaxValue = tickLabelValues.stream().mapToDouble(x -> x).max().orElse(maxValue);
      tickLabelMinValue = tickLabelValues.stream().mapToDouble(x -> x).min().orElse(minValue);
      tickLabels.addAll(
          tickLabelValues.stream()
              .map(x -> getAxisFormat().format(x))
              .collect(Collectors.toList()));
      // the span of the data
      double span =
          Math.abs(
              Math.min(
                  (tickLabelMaxValue - tickLabelMinValue), Double.MAX_VALUE - 1)); // in data space
      double gridStep = span / (tickLabelValues.size() - 1);

      gridStepInChartSpace = (int) (gridStep / span * tickSpace);
    } while (!areAllTickLabelsUnique(tickLabels)
        || !willLabelsFitInTickSpaceHint(tickLabels, gridStepInChartSpace));

    tickLocations.clear();
    tickLocations.addAll(
        tickLabelValues.stream()
            .map(value -> margin + ((value - minValue) / (maxValue - minValue) * tickSpace))
            .collect(Collectors.toList()));
  }

  private static boolean areAllTickLabelsUnique(List<?> tickLabels) {
    return new LinkedHashSet<>(tickLabels).size() == tickLabels.size();
  }

  /**
   * Determines the axis min value, which may differ from the min value of the respective data (e.g.
   * for bar charts).
   *
   * @param styler the chart {@link Styler}
   * @param axisDirection the axis {@link Direction}
   * @param dataMinValue the minimum value of the data corresponding with the axis.
   * @return the axis min value
   */
  private static double getAxisMinValue(
      Styler styler, Direction axisDirection, double dataMinValue) {
    if (Direction.Y.equals(axisDirection) && styler instanceof CategoryStyler && dataMinValue > 0)
      return 0;
    return dataMinValue;
  }

  /**
   * Determines the axis max value, which may differ from the max value of the respective data (e.g.
   * for bar charts).
   *
   * @param styler the chart {@link Styler}
   * @param axisDirection the axis {@link Direction}
   * @param dataMaxValue the maximum value of the data corresponding with the axis.
   * @return the axis max value
   */
  private static double getAxisMaxValue(
      Styler styler, Direction axisDirection, double dataMaxValue) {
    if (Direction.Y.equals(axisDirection) && styler instanceof CategoryStyler && dataMaxValue < 0)
      return 0;
    return dataMaxValue;
  }
}
