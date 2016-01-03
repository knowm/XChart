/**
 * Copyright 2015 Knowm Inc. (http://knowm.org) and contributors.
 * Copyright 2011-2015 Xeiam LLC (http://xeiam.com) and contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.knowm.xchart.internal.chartpart;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.List;

import org.knowm.xchart.StyleManager.ChartType;
import org.knowm.xchart.StyleManager.LegendPosition;
import org.knowm.xchart.internal.chartpart.ChartInternal.ChartInternalType;

/**
 * Axis
 *
 * @author timmolter
 */
public class Axis implements ChartPart {

  public enum AxisType {

    Number, Date, String;
  }

  /** parent */
  private AxisPair axisPair;

  /** the axisType */
  private AxisType axisType;

  /** the axis title */
  private AxisTitle axisTitle;

  /** the axis tick */
  private AxisTick axisTick;

  /** the axis tick calculator */
  private AxisTickCalculator axisTickCalculator;

  /** the axis direction */
  private Direction direction;

  private double min;

  private double max;

  /** the bounds */
  private Rectangle2D bounds;

  /** the paint zone */
  private Rectangle2D paintZone;

  /** An axis direction */
  public enum Direction {

    /** the constant to represent X axis */
    X,

    /** the constant to represent Y axis */
    Y
  }

  /**
   * Constructor
   *
   * @param direction the axis direction (X or Y)
   * @param chart the chart
   */
  protected Axis(AxisPair axisPair, Direction direction) {

    this.axisPair = axisPair;
    this.direction = direction;
    axisTitle = new AxisTitle(this);
    axisTick = new AxisTick(this);
  }

  /**
   * Reset the default min and max values in preparation for calculating the actual min and max
   */
  protected void resetMinMax() {

    min = Double.MAX_VALUE;
    max = -Double.MAX_VALUE;
  }

  /**
   * @param min
   * @param max
   */
  protected void addMinMax(double min, double max) {

    // System.out.println(min);
    // System.out.println(max);
    // NaN indicates String axis data, so min and max play no role
    if (this.min == Double.NaN || min < this.min) {
      this.min = min;
    }
    if (this.max == Double.NaN || max > this.max) {
      this.max = max;
    }

    // System.out.println(this.min);
    // System.out.println(this.max);
  }

  protected void setAxisType(AxisType axisType) {

    if (this.axisType != null && this.axisType != axisType) {
      throw new IllegalArgumentException("Different Axes (Date, Number, String) cannot be mixed on the same chart!!");
    }
    this.axisType = axisType;
  }

  @Override
  public Rectangle2D getBounds() {

    return bounds;
  }

  @Override
  public void paint(Graphics2D g) {

    paintZone = new Rectangle2D.Double();
    bounds = new Rectangle2D.Double();

    // determine Axis bounds
    if (direction == Direction.Y) { // Y-Axis - gets called first

      if (getChartInternal().getChartInternalType() == ChartInternalType.Pie) {
        bounds = new Rectangle2D.Double(getChartInternal().getStyleManager().getChartPadding(), getChartInternal().getChartTitle().getSizeHint(), 0, getChartInternal().getHeight() - getChartInternal()
            .getChartTitle().getSizeHint() - getChartInternal().getStyleManager().getPlotPadding() - getChartInternal().getStyleManager().getChartPadding());
        return;
      }

      // first determine the height of

      // calculate paint zone
      // ----
      // |
      // |
      // |
      // |
      // ----
      double xOffset = getChartInternal().getStyleManager().getChartPadding();
      double yOffset = getChartInternal().getChartTitle().getSizeHint();

      /////////////////////////
      int i = 1; // just twice through is all it takes
      double width = 60; // arbitrary, final width depends on Axis tick labels
      double height = 0;
      do {
        // System.out.println("width before: " + width);

        double approximateXAxisWidth =

            getChartInternal().getWidth()

                - width // y-axis approx. width

                - (getChartInternal().getStyleManager().getLegendPosition() == LegendPosition.OutsideE ? getChartInternal().getChartLegend().getLegendBoxWidth() : 0)

                - 2 * getChartInternal().getStyleManager().getChartPadding()

                - (getChartInternal().getStyleManager().isYAxisTicksVisible() ? (getChartInternal().getStyleManager().getPlotPadding()) : 0)

                - (getChartInternal().getStyleManager().getLegendPosition() == LegendPosition.OutsideE && getChartInternal().getStyleManager().isLegendVisible() ? getChartInternal().getStyleManager()
                    .getChartPadding() : 0)

        ;

        height = getChartInternal().getHeight() - yOffset - axisPair.getXAxis().getXAxisHeightHint(approximateXAxisWidth) - getChartInternal().getStyleManager().getPlotPadding() - getChartInternal()
            .getStyleManager().getChartPadding();

        width = getYAxisWidthHint(height);
        // System.out.println("width after: " + width);

        // System.out.println("height: " + height);

      } while (i-- > 0);

      /////////////////////////

      Rectangle2D yAxisRectangle = new Rectangle2D.Double(xOffset, yOffset, width, height);
      this.paintZone = yAxisRectangle;
      // g.setColor(Color.green);
      // g.draw(yAxisRectangle);

      // fill in Axis with sub-components
      axisTitle.paint(g);
      axisTick.paint(g);

      xOffset = paintZone.getX();
      yOffset = paintZone.getY();
      width = (getChartInternal().getStyleManager().isYAxisTitleVisible() ? axisTitle.getBounds().getWidth() : 0) + axisTick.getBounds().getWidth();
      height = paintZone.getHeight();
      bounds = new Rectangle2D.Double(xOffset, yOffset, width, height);

      // g.setColor(Color.yellow);
      // g.draw(bounds);

    }
    else { // X-Axis

      if (getChartInternal().getChartInternalType() == ChartInternalType.Pie) {
        bounds = new Rectangle2D.Double(getChartInternal().getStyleManager().getChartPadding(), getChartInternal().getHeight() - getChartInternal().getStyleManager().getChartPadding(),
            getChartInternal().getWidth() - getChartInternal().getChartTitle().getSizeHint() - getChartInternal().getStyleManager().getPlotPadding() - (getChartInternal().getStyleManager()
                .getLegendPosition() == LegendPosition.OutsideE ? getChartInternal().getChartLegend().getLegendBoxWidth() : 0), 0);
        return;
      }

      // calculate paint zone
      // |____________________|

      double xOffset = axisPair.getYAxis().getBounds().getWidth() + (getChartInternal().getStyleManager().isYAxisTicksVisible() ? getChartInternal().getStyleManager().getPlotPadding() : 0)
          + getChartInternal().getStyleManager().getChartPadding();
      double yOffset = axisPair.getYAxis().getBounds().getY() + axisPair.getYAxis().getBounds().getHeight() + getChartInternal().getStyleManager().getPlotPadding();

      double width =

          getChartInternal().getWidth()

              - axisPair.getYAxis().getBounds().getWidth() // y-axis was already painted

              - (getChartInternal().getStyleManager().getLegendPosition() == LegendPosition.OutsideE ? getChartInternal().getChartLegend().getLegendBoxWidth() : 0)

              - 2 * getChartInternal().getStyleManager().getChartPadding()

              - (getChartInternal().getStyleManager().isYAxisTicksVisible() ? (getChartInternal().getStyleManager().getPlotPadding()) : 0)

              - (getChartInternal().getStyleManager().getLegendPosition() == LegendPosition.OutsideE && getChartInternal().getStyleManager().isLegendVisible() ? getChartInternal().getStyleManager()
                  .getChartPadding() : 0)

      ;

      // double height = this.getXAxisHeightHint(width);
      // System.out.println("height: " + height);
      // the Y-Axis was already draw at this point so we know how much vertical room is left for the X-Axis
      double height = getChartInternal().getHeight() - axisPair.getYAxis().getBounds().getY() - axisPair.getYAxis().getBounds().getHeight() - getChartInternal().getStyleManager().getChartPadding()
          - getChartInternal().getStyleManager().getPlotPadding();
      // System.out.println("height2: " + height2);

      Rectangle2D xAxisRectangle = new Rectangle2D.Double(xOffset, yOffset, width, height);

      // the paint zone
      this.paintZone = xAxisRectangle;
      // g.setColor(Color.green);
      // g.draw(xAxisRectangle);

      // now paint the X-Axis given the above paint zone
      axisTitle.paint(g);
      axisTick.paint(g);

      bounds = paintZone;

      // g.setColor(Color.yellow);
      // g.draw(bounds);
    }

  }

  /**
   * The vertical Y-Axis is drawn first, but to know the lower bounds of it, we need to know how high the X-Axis paint zone is going to be. Since the tick labels could be rotated, we need to actually
   * determine the tick labels first to get an idea of how tall the X-Axis tick labels will be.
   *
   * @return
   */
  private double getXAxisHeightHint(double workingSpace) {

    if (getChartInternal().getChartInternalType() == ChartInternalType.Pie) {
      return 0.0;
    }

    // Axis title
    double titleHeight = 0.0;
    if (axisTitle.getText() != null && !axisTitle.getText().trim().equalsIgnoreCase("") && getChartInternal().getStyleManager().isXAxisTitleVisible()) {
      TextLayout textLayout = new TextLayout(axisTitle.getText(), getChartInternal().getStyleManager().getAxisTitleFont(), new FontRenderContext(null, true, false));
      Rectangle2D rectangle = textLayout.getBounds();
      titleHeight = rectangle.getHeight() + getChartInternal().getStyleManager().getAxisTitlePadding();
    }

    // Axis tick labels
    double axisTickLabelsHeight = 0.0;
    if (getChartInternal().getStyleManager().isXAxisTicksVisible()) {

      // get some real tick labels
      // System.out.println("XAxisHeightHint");
      // System.out.println("workingSpace: " + workingSpace);
      this.axisTickCalculator = getAxisTickCalculator(workingSpace);

      String sampleLabel = "";
      // find the longest String in all the labels
      for (int i = 0; i < axisTickCalculator.getTickLabels().size(); i++) {
        // System.out.println("label: " + axisTickCalculator.getTickLabels().get(i));
        if (axisTickCalculator.getTickLabels().get(i) != null && axisTickCalculator.getTickLabels().get(i).length() > sampleLabel.length()) {
          sampleLabel = axisTickCalculator.getTickLabels().get(i);
        }
      }
      // System.out.println("sampleLabel: " + sampleLabel);

      // get the height of the label including rotation
      TextLayout textLayout = new TextLayout(sampleLabel.length() == 0 ? " " : sampleLabel, getChartInternal().getStyleManager().getAxisTickLabelsFont(), new FontRenderContext(null, true, false));
      AffineTransform rot = getChartInternal().getStyleManager().getXAxisLabelRotation() == 0 ? null : AffineTransform.getRotateInstance(-1 * Math.toRadians(getChartInternal().getStyleManager()
          .getXAxisLabelRotation()));
      Shape shape = textLayout.getOutline(rot);
      Rectangle2D rectangle = shape.getBounds();

      axisTickLabelsHeight = rectangle.getHeight() + getChartInternal().getStyleManager().getAxisTickPadding() + getChartInternal().getStyleManager().getAxisTickMarkLength();
    }
    return titleHeight + axisTickLabelsHeight;
  }

  private double getYAxisWidthHint(double workingSpace) {

    if (getChartInternal().getChartInternalType() == ChartInternalType.Pie) {
      return 0.0;
    }

    // Axis title
    double titleHeight = 0.0;
    if (axisTitle.getText() != null && !axisTitle.getText().trim().equalsIgnoreCase("") && getChartInternal().getStyleManager().isYAxisTitleVisible()) {
      TextLayout textLayout = new TextLayout(axisTitle.getText(), getChartInternal().getStyleManager().getAxisTitleFont(), new FontRenderContext(null, true, false));
      Rectangle2D rectangle = textLayout.getBounds();
      titleHeight = rectangle.getHeight() + getChartInternal().getStyleManager().getAxisTitlePadding();
    }

    // Axis tick labels
    double axisTickLabelsHeight = 0.0;
    if (getChartInternal().getStyleManager().isYAxisTicksVisible()) {

      // get some real tick labels
      // System.out.println("XAxisHeightHint");
      // System.out.println("workingSpace: " + workingSpace);
      this.axisTickCalculator = getAxisTickCalculator(workingSpace);

      String sampleLabel = "";
      // find the longest String in all the labels
      for (int i = 0; i < axisTickCalculator.getTickLabels().size(); i++) {
        if (axisTickCalculator.getTickLabels().get(i) != null && axisTickCalculator.getTickLabels().get(i).length() > sampleLabel.length()) {
          sampleLabel = axisTickCalculator.getTickLabels().get(i);
        }
      }

      // get the height of the label including rotation
      TextLayout textLayout = new TextLayout(sampleLabel.length() == 0 ? " " : sampleLabel, getChartInternal().getStyleManager().getAxisTickLabelsFont(), new FontRenderContext(null, true, false));
      Rectangle2D rectangle = textLayout.getBounds();

      axisTickLabelsHeight = rectangle.getWidth() + getChartInternal().getStyleManager().getAxisTickPadding() + getChartInternal().getStyleManager().getAxisTickMarkLength();
    }
    return titleHeight + axisTickLabelsHeight;
  }

  private AxisTickCalculator getAxisTickCalculator(double workingSpace) {

    // X-Axis
    if (getDirection() == Direction.X) {

      if (getChartInternal().getChartInternalType() == ChartInternalType.Category) {

        List<?> categories = (List<?>) getChartInternal().getSeriesMap().values().iterator().next().getXData();
        AxisType axisType = getChartInternal().getAxisPair().getXAxis().getAxisType();
        return new AxisTickCalculator_Category(getDirection(), workingSpace, categories, axisType, getChartInternal().getStyleManager());
      }
      else if (getChartInternal().getChartInternalType() == ChartInternalType.Pie) {
        return null;
      }
      else if (getChartInternal().getChartInternalType() == ChartInternalType.XY && getAxisType() == AxisType.Date) {

        return new AxisTickCalculator_Date(getDirection(), workingSpace, min, max, getChartInternal().getStyleManager());
      }
      else if (getChartInternal().getStyleManager().isXAxisLogarithmic()) {

        return new AxisTickCalculator_Logarithmic(getDirection(), workingSpace, min, max, getChartInternal().getStyleManager());
      }
      else {
        return new AxisTickCalculator_Number(getDirection(), workingSpace, min, max, getChartInternal().getStyleManager());

      }
    }

    // Y-Axis
    else {

      if (getChartInternal().getChartInternalType() == ChartInternalType.Pie) {
        return null;
      }
      else if (getChartInternal().getStyleManager().isYAxisLogarithmic() && getAxisType() != AxisType.Date) {

        return new AxisTickCalculator_Logarithmic(getDirection(), workingSpace, min, max, getChartInternal().getStyleManager());
      }
      else {
        return new AxisTickCalculator_Number(getDirection(), workingSpace, min, max, getChartInternal().getStyleManager());

      }
    }

  }

  @Override
  public ChartInternal getChartInternal() {

    return axisPair.getChartInternal();
  }

  // Getters /////////////////////////////////////////////////

  public AxisType getAxisType() {

    return axisType;
  }

  protected double getMin() {

    return min;
  }

  protected double getMax() {

    return max;
  }

  public AxisTick getAxisTick() {

    return axisTick;
  }

  public Direction getDirection() {

    return direction;
  }

  protected Rectangle2D getPaintZone() {

    return paintZone;
  }

  public AxisTitle getAxisTitle() {

    return axisTitle;
  }

  protected void setAxisTitle(AxisTitle axisTitle) {

    this.axisTitle = axisTitle;
  }

  public AxisTickCalculator getAxisTickCalculator() {

    return this.axisTickCalculator;
  }

  public void overrideMinMax() {

    if (direction == Direction.X) { // X-Axis

      double overrideXAxisMinValue = min;
      double overrideXAxisMaxValue = max;

      // override min and maxValue if specified
      if (getChartInternal().getStyleManager().getXAxisMin() != null && getChartInternal().getStyleManager().getChartType() != ChartType.Bar) { // bar chart cannot have a max or min TODO is this true?
        overrideXAxisMinValue = getChartInternal().getStyleManager().getXAxisMin();
      }
      if (getChartInternal().getStyleManager().getXAxisMax() != null && getChartInternal().getStyleManager().getChartType() != ChartType.Bar) { // bar chart cannot have a max or min
        overrideXAxisMaxValue = getChartInternal().getStyleManager().getXAxisMax();
      }
      min = overrideXAxisMinValue;
      max = overrideXAxisMaxValue;
    }
    else {

      double overrideYAxisMinValue = min;
      double overrideYAxisMaxValue = max;
      // override min/max value for bar charts' Y-Axis
      if (getChartInternal().getStyleManager().getChartType() == ChartType.Bar) { // this is the Y-Axis for a bar chart
        if (min > 0.0 && max > 0.0) {
          overrideYAxisMinValue = 0.0;
        }
        if (min < 0.0 && max < 0.0) {
          overrideYAxisMaxValue = 0.0;
        }
      }

      // override min and maxValue if specified
      if (getChartInternal().getStyleManager().getYAxisMin() != null) {
        overrideYAxisMinValue = getChartInternal().getStyleManager().getYAxisMin();
      }
      if (getChartInternal().getStyleManager().getYAxisMax() != null) {
        overrideYAxisMaxValue = getChartInternal().getStyleManager().getYAxisMax();
      }
      min = overrideYAxisMinValue;
      max = overrideYAxisMaxValue;
    }

  }

}
