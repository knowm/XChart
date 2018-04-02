package org.knowm.xchart.internal.chartpart;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.text.Format;
import java.util.LinkedList;
import java.util.List;
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
}
