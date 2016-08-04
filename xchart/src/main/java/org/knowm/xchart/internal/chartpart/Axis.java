/**
 * Copyright 2015-2016 Knowm Inc. (http://knowm.org) and contributors.
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

import org.knowm.xchart.internal.Series;
import org.knowm.xchart.internal.Series_AxesChart;
import org.knowm.xchart.style.AxesChartStyler;
import org.knowm.xchart.style.CategoryStyler;
import org.knowm.xchart.style.Styler.LegendPosition;

/**
 * Axis
 *
 * @author timmolter
 */
public class Axis<ST extends AxesChartStyler, S extends Series> implements ChartPart {

  public enum AxisDataType {

    Number, Date, String;
  }

  private final Chart<AxesChartStyler, Series_AxesChart> chart;
  private Rectangle2D bounds;

  private final AxesChartStyler stylerAxesChart;

  /** the axisDataType */
  private AxisDataType axisDataType;

  /** the axis title */
  private AxisTitle<AxesChartStyler, Series_AxesChart> axisTitle;

  /** the axis tick */
  private AxisTick<AxesChartStyler, Series_AxesChart> axisTick;

  /** the axis tick calculator */
  private AxisTickCalculator_ axisTickCalculator;

  /** the axis direction */
  private Direction direction;

  private double min;

  private double max;

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
   * @param chart the Chart
   * @param direction the axis direction (X or Y)
   */
  public Axis(Chart<AxesChartStyler, Series_AxesChart> chart, Direction direction) {

    this.chart = chart;
    this.stylerAxesChart = chart.getStyler();

    this.direction = direction;
    axisTitle = new AxisTitle<AxesChartStyler, Series_AxesChart>(chart, direction);
    axisTick = new AxisTick<AxesChartStyler, Series_AxesChart>(chart, direction);
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

  @Override
  public void paint(Graphics2D g) {

    bounds = new Rectangle2D.Double();

    // determine Axis bounds
    if (direction == Direction.Y) { // Y-Axis - gets called first

      // calculate paint zone
      // ----
      // |
      // |
      // |
      // |
      // ----
      double xOffset = stylerAxesChart.getChartPadding();
      // double yOffset = chart.getChartTitle().getBounds().getHeight() < .1 ? stylerAxesChart.getChartPadding() : chart.getChartTitle().getBounds().getHeight()
      // + stylerAxesChart.getChartPadding();
      double yOffset = chart.getChartTitle().getBounds().getHeight() + stylerAxesChart.getChartPadding();

      /////////////////////////
      int i = 1; // just twice through is all it takes
      double width = 60; // arbitrary, final width depends on Axis tick labels
      double height = 0;
      do {
        // System.out.println("width before: " + width);

        double approximateXAxisWidth =

            chart.getWidth()

                - width // y-axis approx. width

                - (stylerAxesChart.getLegendPosition() == LegendPosition.OutsideE ? chart.getLegend().getBounds().getWidth() : 0)

                - 2 * stylerAxesChart.getChartPadding()

                - (stylerAxesChart.isYAxisTicksVisible() ? (stylerAxesChart.getPlotMargin()) : 0)

                - (stylerAxesChart.getLegendPosition() == LegendPosition.OutsideE && stylerAxesChart.isLegendVisible() ? stylerAxesChart.getChartPadding() : 0)

        ;

        height = chart.getHeight() - yOffset - chart.getXAxis().getXAxisHeightHint(approximateXAxisWidth) - stylerAxesChart.getPlotMargin() - stylerAxesChart.getChartPadding();

        width = getYAxisWidthHint(height);
        // System.out.println("width after: " + width);

        // System.out.println("height: " + height);

      } while (i-- > 0);

      /////////////////////////

      bounds = new Rectangle2D.Double(xOffset, yOffset, width, height);
      // g.setColor(Color.yellow);
      // g.draw(bounds);

      // fill in Axis with sub-components
      axisTitle.paint(g);
      axisTick.paint(g);

      // now we know the real bounds width after ticks and title are painted
      width = (stylerAxesChart.isYAxisTitleVisible() ? axisTitle.getBounds().getWidth() : 0) + axisTick.getBounds().getWidth();

      bounds = new Rectangle2D.Double(xOffset, yOffset, width, height);
      // g.setColor(Color.yellow);
      // g.draw(bounds);

    }
    else { // X-Axis

      // calculate paint zone
      // |____________________|

      double xOffset = chart.getYAxis().getBounds().getWidth() + (stylerAxesChart.isYAxisTicksVisible() ? stylerAxesChart.getPlotMargin() : 0) + stylerAxesChart.getChartPadding();
      double yOffset = chart.getYAxis().getBounds().getY() + chart.getYAxis().getBounds().getHeight() + stylerAxesChart.getPlotMargin();

      double width =

          chart.getWidth()

              - chart.getYAxis().getBounds().getWidth() // y-axis was already painted

              - (stylerAxesChart.getLegendPosition() == LegendPosition.OutsideE ? chart.getLegend().getBounds().getWidth() : 0)

              - 2 * stylerAxesChart.getChartPadding()

              - (stylerAxesChart.isYAxisTicksVisible() ? (stylerAxesChart.getPlotMargin()) : 0)

              - (stylerAxesChart.getLegendPosition() == LegendPosition.OutsideE && stylerAxesChart.isLegendVisible() ? stylerAxesChart.getChartPadding() : 0)

      ;

      // double height = this.getXAxisHeightHint(width);
      // System.out.println("height: " + height);
      // the Y-Axis was already draw at this point so we know how much vertical room is left for the X-Axis
      double height = chart.getHeight() - chart.getYAxis().getBounds().getY() - chart.getYAxis().getBounds().getHeight() - stylerAxesChart.getChartPadding() - stylerAxesChart.getPlotMargin();
      // System.out.println("height2: " + height2);

      bounds = new Rectangle2D.Double(xOffset, yOffset, width, height);
      // g.setColor(Color.yellow);
      // g.draw(bounds);

      // now paint the X-Axis given the above paint zone
      this.axisTickCalculator = getAxisTickCalculator(bounds.getWidth());
      axisTitle.paint(g);
      axisTick.paint(g);

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
    if (chart.getXAxisTitle() != null && !chart.getXAxisTitle().trim().equalsIgnoreCase("") && stylerAxesChart.isXAxisTitleVisible()) {
      TextLayout textLayout = new TextLayout(chart.getXAxisTitle(), stylerAxesChart.getAxisTitleFont(), new FontRenderContext(null, true, false));
      Rectangle2D rectangle = textLayout.getBounds();
      titleHeight = rectangle.getHeight() + stylerAxesChart.getAxisTitlePadding();
    }

    this.axisTickCalculator = getAxisTickCalculator(workingSpace);

    // Axis tick labels
    double axisTickLabelsHeight = 0.0;
    if (stylerAxesChart.isXAxisTicksVisible()) {

      // get some real tick labels
      // System.out.println("XAxisHeightHint");
      // System.out.println("workingSpace: " + workingSpace);

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
      TextLayout textLayout = new TextLayout(sampleLabel.length() == 0 ? " " : sampleLabel, stylerAxesChart.getAxisTickLabelsFont(), new FontRenderContext(null, true, false));
      AffineTransform rot = stylerAxesChart.getXAxisLabelRotation() == 0 ? null : AffineTransform.getRotateInstance(-1 * Math.toRadians(stylerAxesChart.getXAxisLabelRotation()));
      Shape shape = textLayout.getOutline(rot);
      Rectangle2D rectangle = shape.getBounds();

      axisTickLabelsHeight = rectangle.getHeight() + stylerAxesChart.getAxisTickPadding() + stylerAxesChart.getAxisTickMarkLength();
    }
    return titleHeight + axisTickLabelsHeight;
  }

  private double getYAxisWidthHint(double workingSpace) {

    // Axis title
    double titleHeight = 0.0;
    if (chart.getyYAxisTitle() != null && !chart.getyYAxisTitle().trim().equalsIgnoreCase("") && stylerAxesChart.isYAxisTitleVisible()) {
      TextLayout textLayout = new TextLayout(chart.getyYAxisTitle(), stylerAxesChart.getAxisTitleFont(), new FontRenderContext(null, true, false));
      Rectangle2D rectangle = textLayout.getBounds();
      titleHeight = rectangle.getHeight() + stylerAxesChart.getAxisTitlePadding();
    }

    this.axisTickCalculator = getAxisTickCalculator(workingSpace);

    // Axis tick labels
    double axisTickLabelsHeight = 0.0;
    if (stylerAxesChart.isYAxisTicksVisible()) {

      // get some real tick labels
      // System.out.println("XAxisHeightHint");
      // System.out.println("workingSpace: " + workingSpace);

      String sampleLabel = "";
      // find the longest String in all the labels
      for (int i = 0; i < axisTickCalculator.getTickLabels().size(); i++) {
        if (axisTickCalculator.getTickLabels().get(i) != null && axisTickCalculator.getTickLabels().get(i).length() > sampleLabel.length()) {
          sampleLabel = axisTickCalculator.getTickLabels().get(i);
        }
      }

      // get the height of the label including rotation
      TextLayout textLayout = new TextLayout(sampleLabel.length() == 0 ? " " : sampleLabel, stylerAxesChart.getAxisTickLabelsFont(), new FontRenderContext(null, true, false));
      Rectangle2D rectangle = textLayout.getBounds();

      axisTickLabelsHeight = rectangle.getWidth() + stylerAxesChart.getAxisTickPadding() + stylerAxesChart.getAxisTickMarkLength();
    }
    return titleHeight + axisTickLabelsHeight;
  }

  private AxisTickCalculator_ getAxisTickCalculator(double workingSpace) {

    // X-Axis
    if (getDirection() == Direction.X) {

      if (stylerAxesChart instanceof CategoryStyler) {

        List<?> categories = (List<?>) chart.getSeriesMap().values().iterator().next().getXData();
        AxisDataType axisType = chart.getAxisPair().getXAxis().getAxisDataType();

        return new AxisTickCalculator_Category(getDirection(), workingSpace, categories, axisType, stylerAxesChart);
      }
      else if (getAxisDataType() == AxisDataType.Date) {

        return new AxisTickCalculator_Date(getDirection(), workingSpace, min, max, stylerAxesChart);
      }
      else if (stylerAxesChart.isXAxisLogarithmic()) {

        return new AxisTickCalculator_Logarithmic(getDirection(), workingSpace, min, max, stylerAxesChart);
      }
      else {
        return new AxisTickCalculator_Number(getDirection(), workingSpace, min, max, stylerAxesChart);

      }
    }

    // Y-Axis
    else {

      if (stylerAxesChart.isYAxisLogarithmic() && getAxisDataType() != AxisDataType.Date) {

        return new AxisTickCalculator_Logarithmic(getDirection(), workingSpace, min, max, stylerAxesChart);
      }
      else {
        return new AxisTickCalculator_Number(getDirection(), workingSpace, min, max, stylerAxesChart);

      }
    }

  }

  // Getters /////////////////////////////////////////////////

  protected AxisDataType getAxisDataType() {

    return axisDataType;
  }

  public void setAxisDataType(AxisDataType axisDataType) {

    if (axisDataType != null && this.axisDataType != null && this.axisDataType != axisDataType) {
      throw new IllegalArgumentException("Different Axes (e.g. Date, Number, String) cannot be mixed on the same chart!!");
    }
    this.axisDataType = axisDataType;
  }

  protected double getMin() {

    return min;
  }

  protected void setMin(double min) {

    this.min = min;
  }

  protected double getMax() {

    return max;
  }

  protected void setMax(double max) {

    this.max = max;
  }

  protected AxisTick<AxesChartStyler, Series_AxesChart> getAxisTick() {

    return axisTick;
  }

  protected Direction getDirection() {

    return direction;
  }

  protected AxisTitle<AxesChartStyler, Series_AxesChart> getAxisTitle() {

    return axisTitle;
  }

  public AxisTickCalculator_ getAxisTickCalculator() {

    return this.axisTickCalculator;
  }

  @Override
  public Rectangle2D getBounds() {

    return bounds;
  }
}
