package org.knowm.xchart.internal.series;

import java.awt.*;

/**
 * A Series containing X and Y data to be plotted on a Chart with X and Y Axes.
 *
 * @author timmolter
 */
public abstract class AxesChartSeries extends Series {

  final DataType xAxisDataType;
  final DataType yAxisType;
  /** the minimum value of axis range */
  protected double xMin;
  /** the maximum value of axis range */
  protected double xMax;
  /** the minimum value of axis range */
  protected double yMin;
  /** the maximum value of axis range */
  protected double yMax;
  /** Line Style */
  private BasicStroke stroke;
  /** Line Color */
  private Color lineColor;
  /** Line Width */
  private float lineWidth = -1.0f;

  protected String[] toolTips;

  /**
   * Constructor
   *
   * @param name
   * @param xAxisDataType
   */
  protected AxesChartSeries(String name, DataType xAxisDataType) {

    super(name);
    this.xAxisDataType = xAxisDataType;
    yAxisType = DataType.Number;
  }

  protected abstract void calculateMinMax();

  public double getXMin() {

    return xMin;
  }

  public double getXMax() {

    return xMax;
  }

  public double getYMin() {

    return yMin;
  }

  public double getYMax() {

    return yMax;
  }

  public BasicStroke getLineStyle() {

    return stroke;
  }

  /**
   * Set the line style of the series
   *
   * @param basicStroke
   */
  public AxesChartSeries setLineStyle(BasicStroke basicStroke) {

    stroke = basicStroke;
    if (this.lineWidth > 0.0f) {
      stroke =
          new BasicStroke(
              lineWidth,
              this.stroke.getEndCap(),
              this.stroke.getLineJoin(),
              this.stroke.getMiterLimit(),
              this.stroke.getDashArray(),
              this.stroke.getDashPhase());
    }
    return this;
  }

  public Color getLineColor() {

    return lineColor;
  }

  /**
   * Set the line color of the series
   *
   * @param color
   */
  public AxesChartSeries setLineColor(java.awt.Color color) {

    this.lineColor = color;
    return this;
  }

  public float getLineWidth() {

    return lineWidth;
  }

  /**
   * Set the line width of the series
   *
   * @param lineWidth
   */
  public AxesChartSeries setLineWidth(float lineWidth) {

    this.lineWidth = lineWidth;
    return this;
  }

  public DataType getxAxisDataType() {

    return xAxisDataType;
  }

  public DataType getyAxisDataType() {

    return yAxisType;
  }

  public String[] getToolTips() {

    return toolTips;
  }

  public void setToolTips(String[] toolTips) {

    this.toolTips = toolTips;
  }
}
