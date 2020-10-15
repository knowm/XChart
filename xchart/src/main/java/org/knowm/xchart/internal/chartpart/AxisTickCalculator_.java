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
import java.util.LinkedList;
import java.util.List;

import org.knowm.xchart.internal.Utils;
import org.knowm.xchart.internal.chartpart.Axis.Direction;
import org.knowm.xchart.style.AxesChartStyler;

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

    // Assume that for Y-Axis the ticks will all fit based on their tickSpace hint because the text
    // is usually horizontal and "short". This more applies to the X-Axis.
    if (this.axisDirection == Direction.Y) {
      return true;
    }

    String sampleLabel = " ";
    // find the longest String in all the labels
    for (String tickLabel : tickLabels) {
      if (tickLabel != null && tickLabel.length() > sampleLabel.length()) {
        sampleLabel = tickLabel;
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
    double largestLabelWidth = rectangle.getWidth();
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
                      .setScale(scale, BigDecimal.ROUND_DOWN)
                      .stripTrailingZeros(); // chop off any double imprecision
      // System.out.println("cleanedGridStep: " + cleanedGridStep);

      BigDecimal firstPosition = null;
      double firstPositionAsDouble = getFirstPosition(cleanedGridStep.doubleValue());
      if (Double.isNaN(firstPositionAsDouble)) {
        // This happens when the data values are almost the same but differ by a very tiny amount.
        // The solution for now is to create a single axis label and tick at the average value
        tickLabels.add(getAxisFormat().format(BigDecimal.valueOf((maxValue + minValue) / 2.0)));
        tickLocations.add(workingSpace / 2.0);
        return;
      } else if (firstPositionAsDouble == Double.NEGATIVE_INFINITY) {
        firstPosition = BigDecimal.valueOf(-1 * Double.MAX_VALUE);
      } else {
        try {
          firstPosition = BigDecimal.valueOf(firstPositionAsDouble);
        } catch (NumberFormatException e) {

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
      // System.out.println("cleanedFirstPosition: " + cleanedFirstPosition);

      // generate all tickLabels and tickLocations from the first to last position
      for (BigDecimal value = cleanedFirstPosition;
           value.compareTo(BigDecimal.valueOf(maxValue + 2 * cleanedGridStep.doubleValue())) < 0;
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
    } while (!willLabelsFitInTickSpaceHint(tickLabels, gridStepInChartSpace));
  }
}
