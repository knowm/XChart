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

import org.knowm.xchart.StyleManager.LegendPosition;

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

      // first determine the height of

      // calculate paint zone
      // ----
      // |
      // |
      // |
      // |
      // ----
      double xOffset = getChartPainter().getStyleManager().getChartPadding();
      double yOffset = getChartPainter().getChartTitle().getSizeHint();

      /////////////////////////
      int i = 1; // just twice through is all it takes
      double width = 60; // arbitrary, final width depends on Axis tick labels
      double height = 0;
      do {
        // System.out.println("width before: " + width);

        double approximateXAxisWidth =

            getChartPainter().getWidth()

                - width // y-axis approx. width

                - (getChartPainter().getStyleManager().getLegendPosition() == LegendPosition.OutsideE ? getChartPainter().getChartLegend().getLegendBoxWidth() : 0)

                - 2 * getChartPainter().getStyleManager().getChartPadding()

                - (getChartPainter().getStyleManager().isYAxisTicksVisible() ? (getChartPainter().getStyleManager().getPlotPadding()) : 0)

                - (getChartPainter().getStyleManager().getLegendPosition() == LegendPosition.OutsideE && getChartPainter().getStyleManager().isLegendVisible() ? getChartPainter().getStyleManager()
                    .getChartPadding() : 0)

        ;

        height = getChartPainter().getHeight() - yOffset - axisPair.getXAxis().getXAxisHeightHint(approximateXAxisWidth) - getChartPainter().getStyleManager().getPlotPadding() - getChartPainter()
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
      width = (getChartPainter().getStyleManager().isYAxisTitleVisible() ? axisTitle.getBounds().getWidth() : 0) + axisTick.getBounds().getWidth();
      height = paintZone.getHeight();
      bounds = new Rectangle2D.Double(xOffset, yOffset, width, height);

      // g.setColor(Color.yellow);
      // g.draw(bounds);

    }
    else { // X-Axis

      // calculate paint zone
      // |____________________|

      double xOffset = axisPair.getYAxis().getBounds().getWidth() + (getChartPainter().getStyleManager().isYAxisTicksVisible() ? getChartPainter().getStyleManager().getPlotPadding() : 0)
          + getChartPainter().getStyleManager().getChartPadding();
      double yOffset = axisPair.getYAxis().getBounds().getY() + axisPair.getYAxis().getBounds().getHeight() + getChartPainter().getStyleManager().getPlotPadding();

      double width =

          getChartPainter().getWidth()

              - axisPair.getYAxis().getBounds().getWidth() // y-axis was already painted

              - (getChartPainter().getStyleManager().getLegendPosition() == LegendPosition.OutsideE ? getChartPainter().getChartLegend().getLegendBoxWidth() : 0)

              - 2 * getChartPainter().getStyleManager().getChartPadding()

              - (getChartPainter().getStyleManager().isYAxisTicksVisible() ? (getChartPainter().getStyleManager().getPlotPadding()) : 0)

              - (getChartPainter().getStyleManager().getLegendPosition() == LegendPosition.OutsideE && getChartPainter().getStyleManager().isLegendVisible() ? getChartPainter().getStyleManager()
                  .getChartPadding() : 0)

      ;

      // double height = this.getXAxisHeightHint(width);
      // System.out.println("height: " + height);
      // the Y-Axis was already draw at this point so we know how much vertical room is left for the X-Axis
      double height = getChartPainter().getHeight() - axisPair.getYAxis().getBounds().getY() - axisPair.getYAxis().getBounds().getHeight() - getChartPainter().getStyleManager().getChartPadding()
          - getChartPainter().getStyleManager().getPlotPadding();
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

    // Axis title
    double titleHeight = 0.0;
    if (axisTitle.getText() != null && !axisTitle.getText().trim().equalsIgnoreCase("") && getChartPainter().getStyleManager().isXAxisTitleVisible()) {
      TextLayout textLayout = new TextLayout(axisTitle.getText(), getChartPainter().getStyleManager().getAxisTitleFont(), new FontRenderContext(null, true, false));
      Rectangle2D rectangle = textLayout.getBounds();
      titleHeight = rectangle.getHeight() + getChartPainter().getStyleManager().getAxisTitlePadding();
    }

    // Axis tick labels
    double axisTickLabelsHeight = 0.0;
    if (getChartPainter().getStyleManager().isXAxisTicksVisible()) {

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
      TextLayout textLayout = new TextLayout(sampleLabel.length() == 0 ? " " : sampleLabel, getChartPainter().getStyleManager().getAxisTickLabelsFont(), new FontRenderContext(null, true, false));
      AffineTransform rot = getChartPainter().getStyleManager().getXAxisLabelRotation() == 0 ? null : AffineTransform.getRotateInstance(-1 * Math.toRadians(getChartPainter().getStyleManager()
          .getXAxisLabelRotation()));
      Shape shape = textLayout.getOutline(rot);
      Rectangle2D rectangle = shape.getBounds();

      axisTickLabelsHeight = rectangle.getHeight() + getChartPainter().getStyleManager().getAxisTickPadding() + getChartPainter().getStyleManager().getAxisTickMarkLength();
    }
    return titleHeight + axisTickLabelsHeight;
  }

  private double getYAxisWidthHint(double workingSpace) {

    // Axis title
    double titleHeight = 0.0;
    if (axisTitle.getText() != null && !axisTitle.getText().trim().equalsIgnoreCase("") && getChartPainter().getStyleManager().isYAxisTitleVisible()) {
      TextLayout textLayout = new TextLayout(axisTitle.getText(), getChartPainter().getStyleManager().getAxisTitleFont(), new FontRenderContext(null, true, false));
      Rectangle2D rectangle = textLayout.getBounds();
      titleHeight = rectangle.getHeight() + getChartPainter().getStyleManager().getAxisTitlePadding();
    }

    // Axis tick labels
    double axisTickLabelsHeight = 0.0;
    if (getChartPainter().getStyleManager().isYAxisTicksVisible()) {

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
      TextLayout textLayout = new TextLayout(sampleLabel.length() == 0 ? " " : sampleLabel, getChartPainter().getStyleManager().getAxisTickLabelsFont(), new FontRenderContext(null, true, false));
      Rectangle2D rectangle = textLayout.getBounds();

      axisTickLabelsHeight = rectangle.getWidth() + getChartPainter().getStyleManager().getAxisTickPadding() + getChartPainter().getStyleManager().getAxisTickMarkLength();
    }
    return titleHeight + axisTickLabelsHeight;
  }

  private AxisTickCalculator getAxisTickCalculator(double workingSpace) {

    if (getDirection() == Direction.X && getAxisType() == AxisType.String) {

      return new AxisTickCategoryChartCalculator(getDirection(), workingSpace, getMin(), getMax(), getChartPainter());

    }
    else if (getDirection() == Direction.X && getChartPainter().getStyleManager().isXAxisLogarithmic() && getAxisType() != AxisType.Date) {

      return new AxisTickLogarithmicCalculator(getDirection(), workingSpace, getMin(), getMax(), getChartPainter().getStyleManager());

    }
    else if (getDirection() == Direction.Y && getChartPainter().getStyleManager().isYAxisLogarithmic() && getAxisType() != AxisType.Date) {

      return new AxisTickLogarithmicCalculator(getDirection(), workingSpace, getMin(), getMax(), getChartPainter().getStyleManager());

    }
    else if (getAxisType() == AxisType.Date) {

      return new AxisTickDateCalculator(getDirection(), workingSpace, getMin(), getMax(), getChartPainter().getStyleManager());

    }
    else { // number

      return new AxisTickNumberCalculator(getDirection(), workingSpace, getMin(), getMax(), getChartPainter().getStyleManager());
    }
  }

  @Override
  public ChartPainter getChartPainter() {

    return axisPair.getChartPainter();
  }

  // Getters /////////////////////////////////////////////////

  public AxisType getAxisType() {

    return axisType;
  }

  public double getMin() {

    return min;
  }

  public double getMax() {

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

}
