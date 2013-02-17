/**
 * Copyright 2011-2013 Xeiam LLC.
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
package com.xeiam.xchart.internal.chartpart;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.math.BigDecimal;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.style.StyleManager.LegendPosition;

/**
 * Axis
 * 
 * @author timmolter
 */
public class Axis implements ChartPart {

  public enum AxisType {

    Number, Date;
  }

  /** parent */
  private AxisPair axisPair;

  /** the axisType */
  private AxisType axisType;

  /** the axis title */
  private AxisTitle axisTitle;

  /** the axis tick */
  private AxisTick axisTick;

  /** the axis direction */
  private Direction direction;

  private BigDecimal min = null;

  private BigDecimal max = null;

  private BigDecimal minOverride = null;

  private BigDecimal maxOverride = null;

  /** the bounds */
  private Rectangle bounds;

  /** the paint zone */
  private Rectangle paintZone;

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
    axisTitle = new AxisTitle(this, direction == Direction.X ? getChart().getStyleManager().isxAxisTitleVisible() : getChart().getStyleManager().isyAxisTitleVisible());
    axisTick = new AxisTick(this, direction == Direction.X ? getChart().getStyleManager().isxAxisTicksVisible() : getChart().getStyleManager().isyAxisTicksVisible());
  }

  /**
   * @param min
   * @param max
   */
  protected void addMinMax(BigDecimal min, BigDecimal max) {

    // System.out.println(min);
    // System.out.println(max);
    if (this.min == null || min.compareTo(this.min) < 0) {
      this.min = min;
    }
    if (this.max == null || max.compareTo(this.max) > 0) {
      this.max = max;
    }

    // System.out.println(this.min);
    // System.out.println(this.max);
  }

  protected void setAxisType(AxisType axisType) {

    if (this.axisType != null && this.axisType != axisType) {
      throw new IllegalArgumentException("Date and Number Axes cannot be mixed on the same chart!! ");
    }
    this.axisType = axisType;
  }

  @Override
  public Rectangle getBounds() {

    return bounds;
  }

  /**
   * @return
   */
  protected int getSizeHint() {

    if (direction == Direction.X) { // X-Axis

      // Axis title
      double titleHeight = 0.0;
      if (getChart().getStyleManager().isxAxisTitleVisible()) {
        TextLayout textLayout = new TextLayout(axisTitle.getText(), getChart().getStyleManager().getAxisTitleFont(), new FontRenderContext(null, true, false));
        Rectangle rectangle = textLayout.getPixelBounds(null, 0, 0);
        titleHeight = rectangle.getHeight() + getChart().getStyleManager().getAxisTitlePadding();
      }

      // Axis tick labels
      double axisTickLabelsHeight = 0.0;
      if (getChart().getStyleManager().isxAxisTicksVisible()) {
        TextLayout textLayout = new TextLayout("0", getChart().getStyleManager().getAxisTickLabelsFont(), new FontRenderContext(null, true, false));
        Rectangle rectangle = textLayout.getPixelBounds(null, 0, 0);
        axisTickLabelsHeight = rectangle.getHeight() + getChart().getStyleManager().getAxisTickPadding() + getChart().getStyleManager().getAxisTickMarkLength()
            + getChart().getStyleManager().getPlotPadding();
      }
      return (int) (titleHeight + axisTickLabelsHeight);
    } else { // Y-Axis
      return 0; // We layout the yAxis first depending in the xAxis height hint. We don't care about the yAxis height hint
    }
  }

  @Override
  public void paint(Graphics2D g) {

    paintZone = new Rectangle();
    bounds = new Rectangle();

    // determine Axis bounds
    if (direction == Direction.Y) { // Y-Axis

      // calculate paint zone
      // ----
      // |
      // |
      // |
      // |
      // ----
      int xOffset = getChart().getStyleManager().getChartPadding();
      int yOffset = getChart().getChartTitle().getSizeHint();
      int width = 80; // arbitrary, final width depends on Axis tick labels
      int height = getChart().getHeight() - yOffset - axisPair.getxAxis().getSizeHint() - getChart().getStyleManager().getChartPadding();
      Rectangle yAxisRectangle = new Rectangle(xOffset, yOffset, width, height);
      this.paintZone = yAxisRectangle;
      // g.setColor(Color.green);
      // g.draw(yAxisRectangle);

      // fill in Axis with sub-components
      axisTitle.paint(g);
      axisTick.paint(g);

      xOffset = (int) paintZone.getX();
      yOffset = (int) paintZone.getY();
      width = (int) (getChart().getStyleManager().isyAxisTitleVisible() ? axisTitle.getBounds().getWidth() : 0) + (int) axisTick.getBounds().getWidth();
      height = (int) paintZone.getHeight();
      bounds = new Rectangle(xOffset, yOffset, width, height);
      // g.setColor(Color.yellow);
      // g.draw(bounds);

    } else { // X-Axis

      // calculate paint zone
      // |____________________|

      int xOffset = (int) (axisPair.getyAxis().getBounds().getWidth() + (getChart().getStyleManager().isyAxisTicksVisible() ? getChart().getStyleManager().getPlotPadding() : 0) + getChart()
          .getStyleManager().getChartPadding());
      int yOffset = (int) (axisPair.getyAxis().getBounds().getY() + axisPair.getyAxis().getBounds().getHeight());

      int chartLegendWidth = 0;
      if (getChart().getStyleManager().getLegendPosition() == LegendPosition.OutsideW) {
        chartLegendWidth = getChart().getChartLegend().getSizeHint()[0];
      }

      int width = (int) (getChart().getWidth() - axisPair.getyAxis().getBounds().getWidth() - chartLegendWidth - (getChart().getStyleManager().isLegendVisible() ? 3 : 2)
          * getChart().getStyleManager().getChartPadding());
      int height = this.getSizeHint();
      Rectangle xAxisRectangle = new Rectangle(xOffset, yOffset, width, height);
      this.paintZone = xAxisRectangle;
      // g.setColor(Color.green);
      // g.draw(xAxisRectangle);

      axisTitle.paint(g);
      axisTick.paint(g);

      xOffset = (int) paintZone.getX();
      yOffset = (int) paintZone.getY();
      width = (int) paintZone.getWidth();
      height = (int) ((getChart().getStyleManager().isxAxisTitleVisible() ? axisTitle.getBounds().getHeight() : 0) + (int) axisTick.getBounds().getHeight());
      bounds = new Rectangle(xOffset, yOffset, width, height);
      bounds = new Rectangle(xOffset, yOffset, width, height);
      // g.setColor(Color.yellow);
      // g.draw(bounds);
    }

  }

  @Override
  public Chart getChart() {

    return axisPair.getChart();
  }

  // Getters /////////////////////////////////////////////////

  public AxisType getAxisType() {

    return axisType;
  }

  public BigDecimal getMin() {

    if (minOverride == null) {
      return min;
    } else {
      return minOverride;
    }

  }

  public BigDecimal getMax() {

    if (maxOverride == null) {
      return max;
    } else {
      return maxOverride;
    }
  }

  public BigDecimal getMinOverride() {

    return minOverride;
  }

  public BigDecimal getMaxOverride() {

    return maxOverride;
  }

  public AxisTick getAxisTick() {

    return axisTick;
  }

  public Direction getDirection() {

    return direction;
  }

  protected Rectangle getPaintZone() {

    return paintZone;
  }

  public AxisTitle getAxisTitle() {

    return axisTitle;
  }

  protected void setAxisTitle(AxisTitle axisTitle) {

    this.axisTitle = axisTitle;
  }

  public void setMinOverride(double minOverride) {

    this.minOverride = new BigDecimal(minOverride);
  }

  public void setMaxOverride(double maxOverride) {

    this.maxOverride = new BigDecimal(maxOverride);
  }
}
