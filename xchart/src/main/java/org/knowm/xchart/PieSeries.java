package org.knowm.xchart;

import org.knowm.xchart.internal.chartpart.RenderableSeries.LegendRenderType;
import org.knowm.xchart.internal.series.Series;

/**
 * A Series containing Pie data to be plotted on a Chart
 *
 * @author timmolter
 */
public class PieSeries extends Series {

  private PieSeriesRenderStyle chartPieSeriesRenderStyle = null;
  private Number value;
  private String toolTip;

  /**
   * Constructor
   *
   * @param name
   * @param value
   */
  public PieSeries(String name, Number value) {

    super(name);
    this.value = value;
  }

  /**
   * *This is an internal method which shouldn't be called from client code. Use
   * PieChart.updatePieSeries instead!
   *
   * @param value
   */
  public void replaceData(Number value) {

    this.value = value;
  }

  public PieSeriesRenderStyle getChartPieSeriesRenderStyle() {

    return chartPieSeriesRenderStyle;
  }

  public PieSeries setChartPieSeriesRenderStyle(PieSeriesRenderStyle chartPieSeriesRenderStyle) {

    this.chartPieSeriesRenderStyle = chartPieSeriesRenderStyle;
    return this;
  }

  public Number getValue() {

    return value;
  }

  public void setValue(Number value) {

    this.value = value;
  }

  @Override
  public LegendRenderType getLegendRenderType() {

    // Pie charts are always rendered as a Box in the legend
    return null;
  }

  public enum PieSeriesRenderStyle {
    Pie(),

    Donut();
  }

  public String getToolTip() {

    return toolTip;
  }

  public void setToolTip(String toolTip) {

    this.toolTip = toolTip;
  }
}
