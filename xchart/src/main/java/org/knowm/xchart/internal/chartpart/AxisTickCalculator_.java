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

  /**
   * Constructor
   *
   * @param axisDirection
   * @param workingSpace
   * @param minValue
   * @param maxValue
   * @param styler
   */

  public AxisTickCalculator_(
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

  protected abstract void calculate();

  protected boolean areValuesEquallySpaced(List<Double> values) {
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
  protected void calculateForEquallySpacedAxisValues(double tickSpace, double margin) {
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

  boolean areAllTickLabelsUnique(List<?> tickLabels) {
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
