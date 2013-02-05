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

import com.xeiam.xchart.internal.interfaces.IChartPart;

/**
 * Axis
 * 
 * @author timmolter
 */
public class Axis implements IChartPart {

  public enum AxisType {

    NUMBER, DATE;
  }

  /** parent */
  protected AxisPair axisPair;

  /** the axisType */
  protected AxisType axisType;

  /** the axis title */
  public AxisTitle axisTitle;

  /** the axis tick */
  public AxisTick axisTick;

  /** the axis direction */
  protected Direction direction;

  protected BigDecimal min = null;

  protected BigDecimal max = null;

  /** the bounds */
  private Rectangle bounds;

  /** the paint zone */
  private Rectangle paintZone;

  /** An axis direction */
  protected enum Direction {

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
    axisTitle = new AxisTitle(this, direction == Direction.X ? axisPair.chart.getStyleManager().isxAxisTitleVisible() : axisPair.chart.getStyleManager().isyAxisTitleVisible());
    axisTick = new AxisTick(this, direction == Direction.X ? axisPair.chart.getStyleManager().isxAxisTicksVisible() : axisPair.chart.getStyleManager().isyAxisTicksVisible());
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

  protected Rectangle getPaintZone() {

    return paintZone;
  }

  public AxisTitle getAxisTitle() {

    return axisTitle;
  }

  protected void setAxisTitle(AxisTitle axisTitle) {

    this.axisTitle = axisTitle;
  }

  /**
   * @return
   */
  protected int getSizeHint() {

    if (direction == Direction.X) { // X-Axis

      // Axis title
      double titleHeight = 0.0;
      if (axisPair.chart.getStyleManager().isxAxisTitleVisible()) {
        TextLayout textLayout = new TextLayout(axisTitle.getText(), axisTick.axis.axisPair.chart.getStyleManager().getAxisTitleFont(), new FontRenderContext(null, true, false));
        Rectangle rectangle = textLayout.getPixelBounds(null, 0, 0);
        titleHeight = rectangle.getHeight() + axisTick.axis.axisPair.chart.getStyleManager().getAxisTitlePadding();
      }

      // Axis tick labels
      double axisTickLabelsHeight = 0.0;
      if (axisPair.chart.getStyleManager().isxAxisTicksVisible()) {
        TextLayout textLayout = new TextLayout("0", axisTick.axis.axisPair.chart.getStyleManager().getAxisTicksFont(), new FontRenderContext(null, true, false));
        Rectangle rectangle = textLayout.getPixelBounds(null, 0, 0);
        axisTickLabelsHeight = rectangle.getHeight() + axisPair.chart.getStyleManager().getAxisTickPadding() + axisPair.chart.getStyleManager().getAxisTickMarkLength()
            + axisPair.chart.getStyleManager().getPlotPadding();
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
      int xOffset = axisPair.chart.getStyleManager().getChartPadding();
      int yOffset = (int) (axisPair.getChartTitleBounds().getY() + axisPair.getChartTitleBounds().getHeight() + axisPair.chart.getStyleManager().getChartPadding());
      int width = 80; // arbitrary, final width depends on Axis tick labels
      int height = axisPair.chart.height - yOffset - axisPair.xAxis.getSizeHint() - axisPair.chart.getStyleManager().getChartPadding();
      Rectangle yAxisRectangle = new Rectangle(xOffset, yOffset, width, height);
      this.paintZone = yAxisRectangle;
      // g.setColor(Color.green);
      // g.draw(yAxisRectangle);

      // fill in Axis with sub-components
      axisTitle.paint(g);
      axisTick.paint(g);

      xOffset = (int) paintZone.getX();
      yOffset = (int) paintZone.getY();
      width = (int) (axisPair.chart.getStyleManager().isyAxisTitleVisible() ? axisTitle.getBounds().getWidth() : 0) + (int) axisTick.getBounds().getWidth();
      height = (int) paintZone.getHeight();
      bounds = new Rectangle(xOffset, yOffset, width, height);
      // g.setColor(Color.yellow);
      // g.draw(bounds);

    } else { // X-Axis

      // calculate paint zone
      // |____________________|

      int xOffset = (int) (axisPair.yAxis.getBounds().getWidth() + (axisPair.chart.getStyleManager().isyAxisTicksVisible() ? axisPair.chart.getStyleManager().getPlotPadding() : 0) + axisPair.chart
          .getStyleManager().getChartPadding());
      int yOffset = (int) (axisPair.yAxis.getBounds().getY() + axisPair.yAxis.getBounds().getHeight());
      int width = (int) (axisPair.chart.width - axisPair.yAxis.getBounds().getWidth() - axisPair.getChartLegendBounds().getWidth() - (axisPair.chart.getStyleManager().isLegendVisible() ? 3 : 2)
          * axisPair.chart.getStyleManager().getChartPadding());
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
      height = (int) ((axisPair.chart.getStyleManager().isxAxisTitleVisible() ? axisTitle.getBounds().getHeight() : 0) + (int) axisTick.getBounds().getHeight());
      bounds = new Rectangle(xOffset, yOffset, width, height);
      bounds = new Rectangle(xOffset, yOffset, width, height);
      // g.setColor(Color.yellow);
      // g.draw(bounds);
    }

  }
}
