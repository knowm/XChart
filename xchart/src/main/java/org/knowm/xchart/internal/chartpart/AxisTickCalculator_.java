package org.knowm.xchart.internal.chartpart;

import java.awt.Font;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.Format;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.knowm.xchart.internal.Utils;
import org.knowm.xchart.internal.chartpart.Axis_.Direction;
import org.knowm.xchart.style.AxesChartStyler;

public abstract class AxisTickCalculator_ implements AxisTickCalculator {

  /** the List of tick label position in pixels */
  final List<Double> tickLocations = new LinkedList<>();

  /** the List of tick label values */
  final List<String> tickLabels = new LinkedList<>();

  final Direction axisDirection;

  final double workingSpace;

  final double minValue;

  final double maxValue;

  List<Double> axisValues;

  final AxesChartStyler styler;

  Format axisFormat;

  int yIndex;

  private static final int MAX_LABEL_LENGTH = 20;

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
    this.minValue = minValue;
    this.maxValue = maxValue;
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
    this.minValue = minValue;
    this.maxValue = maxValue;
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

    //    System.out.println("minValue = " + minValue);
    //    System.out.println("(minValue % gridStep) = " + (minValue % gridStep));

    return minValue - (minValue % gridStep) - gridStep;
  }

  // TODO make these non-public??
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

    if (tickSpacingHint <= 0 || tickLabels.isEmpty()) {
      return false;
    }

    int MAX_CHECK = 3;
    for (int i = 0; i < Math.min(MAX_CHECK, tickLabels.size()); i++) {
      String tickLabel = tickLabels.get(i);
      if (tickLabel != null && tickLabel.length() > MAX_LABEL_LENGTH) {
        return false;
      }
    }

    String sampleLabel = "Y";
    if (Direction.X.equals(this.axisDirection)) {

      if (tickSpacingHint <= 0) {
        return false;
      }

      for (String tickLabel : tickLabels) {
        if (tickLabel != null) {
          if (tickLabel.length() > MAX_LABEL_LENGTH) {
            return false;
          }
          if (tickLabel.length() > sampleLabel.length()) {
            sampleLabel = tickLabel;
          }
        }
      }
    }

    Font font = styler.getAxisTickLabelsFont();
    FontRenderContext frc = new FontRenderContext(null, true, false);
    TextLayout textLayout = new TextLayout(sampleLabel, font, frc);

    AffineTransform rot = null;
    if (styler.getXAxisLabelRotation() != 0) {
      rot = AffineTransform.getRotateInstance(-1 * Math.toRadians(styler.getXAxisLabelRotation()));
    }

    Shape shape = textLayout.getOutline(rot);
    Rectangle2D rectangle = shape.getBounds();
    double largestLabelWidth =
        Direction.X.equals(this.axisDirection) ? rectangle.getWidth() : rectangle.getHeight();

    return (largestLabelWidth * 1.1 < tickSpacingHint);
  }

  public Format getAxisFormat() {

    return axisFormat;
  }

  private boolean hasYAxisGroupSpacingHint() {
    return axisDirection == Direction.Y && styler.getYAxisGroupTickMarkSpacingHint(yIndex) != null;
  }

  private int getTickMarkSpacingHint() {
    if (axisDirection == Direction.X) {
      return styler.getXAxisTickMarkSpacingHint();
    }
    Integer groupHint = styler.getYAxisGroupTickMarkSpacingHint(yIndex);
    return groupHint != null ? groupHint : styler.getYAxisTickMarkSpacingHint();
  }

  protected void calculate() {

    // System.out.println("calculate");

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
    if (axisDirection == Direction.Y && tickSpace < getTickMarkSpacingHint()) {
      // An explicit group hint requests sparse ticks, not an empty axis after resizing.
      if (hasYAxisGroupSpacingHint()
          && tickSpace > 0
          && !isNumberFormatChoppingDecimals(maxValue, minValue)) {
        double margin = Utils.getTickStartOffset(workingSpace, tickSpace);
        tickLabels.add(getAxisFormat().format(minValue));
        tickLocations.add(margin);
        tickLabels.add(getAxisFormat().format(maxValue));
        tickLocations.add(margin + tickSpace);
      }
      return;
    }

    // this prevents an infinite loop when the axis number formatter "rounds" all axis ticks to the
    // same value i.e. "#0.00" for 0.0, 0.0001, 0.0002
    // issue #582
    if (isNumberFormatChoppingDecimals(maxValue, minValue)) {
      //      System.out.println("returning");
      return;
    }

    // where the tick should begin in the working space in pixels
    double margin = Utils.getTickStartOffset(workingSpace, tickSpace); // in plot space
    // the span of the data
    double span = Math.abs(Math.min((maxValue - minValue), Double.MAX_VALUE - 1)); // in data space

    // Explicit group spacing takes precedence over data-driven, equally spaced ticks.
    if (axisValues != null && areValuesEquallySpaced(axisValues) && !hasYAxisGroupSpacingHint()) {
      calculateForEquallySpacedAxisValues(tickSpace, margin);
      return;
    }

    //////////////////////////

    int tickSpacingHint = getTickMarkSpacingHint() - 5;

    // Squeeze more ticks into short Y-axes unless the caller explicitly set group spacing.
    if (axisDirection == Direction.Y && tickSpace < 160 && !hasYAxisGroupSpacingHint()) {
      tickSpacingHint = 25 - 5;
    }

    BigDecimal lower = BigDecimal.valueOf(minValue);
    BigDecimal upper = BigDecimal.valueOf(maxValue);
    List<String> layoutLabels;
    int gridStepInChartSpace;

    do {

      //       System.out.println("calculating ticks...");
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

      // Decimal arithmetic keeps exact grid multiples at either boundary.
      BigDecimal first =
          cleanedGridStep.signum() == 0
              ? null
              : lower.divide(cleanedGridStep, 0, RoundingMode.CEILING).multiply(cleanedGridStep);
      if (first == null || first.compareTo(upper) > 0) {
        double middle = lower.add(upper).divide(BigDecimal.valueOf(2)).doubleValue();
        tickLabels.add(getAxisFormat().format(middle));
        tickLocations.add(workingSpace / 2.0);
        return;
      }
      BigDecimal value = first;
      while (value.compareTo(upper) <= 0) {
        double tick = value.doubleValue();
        tickLabels.add(getAxisFormat().format(tick));
        tickLocations.add(margin + (tick - minValue) / (maxValue - minValue) * tickSpace);
        value = value.add(cleanedGridStep);
      }
      if (tickLabels.size() == 1) {
        return;
      }
      layoutLabels = labelsForGridSpacing(lower, upper, first, value, cleanedGridStep);
    } while (!areAllTickLabelsUnique(layoutLabels)
        || !willLabelsFitInTickSpaceHint(layoutLabels, gridStepInChartSpace));
  }

  List<String> labelsForGridSpacing(
      BigDecimal lower, BigDecimal upper, BigDecimal first, BigDecimal next, BigDecimal step) {
    return tickLabels;
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
    List<Double> visibleValues =
        axisValues.stream()
            .filter(value -> value >= minValue && value <= maxValue)
            .collect(Collectors.toList());
    int tickValuesHint = 0;
    List<Double> tickLabelValues;
    double tickLabelMaxValue;
    double tickLabelMinValue;
    double range = maxValue - minValue;
    if (range < 1e-10) {
      return;
    }
    do {
      tickValuesHint++;
      tickLabels.clear();
      int finalTickValuesHint = tickValuesHint;
      tickLabelValues =
          IntStream.range(0, visibleValues.size())
              .filter(it -> it % finalTickValuesHint == 0)
              .mapToDouble(visibleValues::get)
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
    } while (tickLabels.size() > 1
        && (!areAllTickLabelsUnique(tickLabels)
            || !willLabelsFitInTickSpaceHint(tickLabels, gridStepInChartSpace)));

    tickLocations.clear();
    tickLocations.addAll(
        tickLabelValues.stream()
            .map(value -> margin + ((value - minValue) / (maxValue - minValue) * tickSpace))
            .collect(Collectors.toList()));
  }

  boolean areAllTickLabelsUnique(List<?> tickLabels) {
    return new LinkedHashSet<>(tickLabels).size() == tickLabels.size();
  }

  private boolean isNumberFormatChoppingDecimals(double axisMax, double axisMin) {

    //        System.out.println("axisMax = " + axisMax);
    //        System.out.println("axisMin = " + axisMin);
    String formattedMaxValue = getAxisFormat().format(axisMax);
    //        System.out.println("formattedMaxValue = " + formattedMaxValue);
    String formattedMinValue = getAxisFormat().format(axisMin);
    //        System.out.println("formattedMinValue = " + formattedMinValue);
    // if formatted number lost its decimals due to formatter
    if (formattedMaxValue.equals(formattedMinValue)) {
      return true;
    }
    return false;
  }
}
