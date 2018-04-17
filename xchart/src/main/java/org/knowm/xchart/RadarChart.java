package org.knowm.xchart;

import java.awt.*;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.chartpart.Legend_Pie;
import org.knowm.xchart.internal.chartpart.Plot_Radar;
import org.knowm.xchart.internal.style.SeriesColorMarkerLineStyle;
import org.knowm.xchart.internal.style.SeriesColorMarkerLineStyleCycler;
import org.knowm.xchart.style.RadarStyler;
import org.knowm.xchart.style.Styler.ChartTheme;
import org.knowm.xchart.style.Theme;

public class RadarChart extends Chart<RadarStyler, RadarSeries> {

  protected String[] variableLabels;
  private RadarRenderStyle radarRenderStyle = RadarRenderStyle.Polygon;

  /**
   * Constructor - the default Chart Theme will be used (XChartTheme)
   *
   * @param width
   * @param height
   */
  public RadarChart(int width, int height) {

    super(width, height, new RadarStyler());
    plot = new Plot_Radar<RadarStyler, RadarSeries>(this);
    legend = new Legend_Pie<RadarStyler, RadarSeries>(this);
  }

  /**
   * Constructor
   *
   * @param width
   * @param height
   * @param theme - pass in a instance of Theme class, probably a custom Theme.
   */
  public RadarChart(int width, int height, Theme theme) {

    this(width, height);
    styler.setTheme(theme);
  }

  /**
   * Constructor
   *
   * @param width
   * @param height
   * @param chartTheme - pass in the desired ChartTheme enum
   */
  public RadarChart(int width, int height, ChartTheme chartTheme) {

    this(width, height, chartTheme.newInstance(chartTheme));
  }

  /**
   * Constructor
   *
   * @param chartBuilder
   */
  public RadarChart(RadarChartBuilder chartBuilder) {

    this(chartBuilder.width, chartBuilder.height, chartBuilder.chartTheme);
    setTitle(chartBuilder.title);
  }

  /**
   * Add a series for a Radar type chart
   *
   * @param seriesName
   * @param values
   * @return
   */
  public RadarSeries addSeries(String seriesName, double[] values) {

    return addSeries(seriesName, values, null);
  }

  /**
   * Add a series for a Radar type chart
   *
   * @param seriesName
   * @param values
   * @param tooltipOverrides
   * @return
   */
  public RadarSeries addSeries(String seriesName, double[] values, String[] tooltipOverrides) {

    // Sanity checks
    sanityCheck(seriesName, values, tooltipOverrides);

    RadarSeries series = new RadarSeries(seriesName, values, tooltipOverrides);

    seriesMap.put(seriesName, series);

    return series;
  }

  private void sanityCheck(String seriesName, double[] values, String[] annotations) {

    if (variableLabels == null) {
      throw new IllegalArgumentException("Variable labels cannot be null!!!");
    }

    if (seriesMap.keySet().contains(seriesName)) {
      throw new IllegalArgumentException(
          "Series name >"
              + seriesName
              + "< has already been used. Use unique names for each series!!!");
    }
    if (values == null) {
      throw new IllegalArgumentException("Values data cannot be null!!!");
    }
    if (values.length < variableLabels.length) {
      throw new IllegalArgumentException("Too few values!!!");
    }
    for (double d : values) {
      if (d < 0 || d > 1) {
        throw new IllegalArgumentException("Values must be in [0, 1] range!!!");
      }
    }

    if (annotations != null && annotations.length < variableLabels.length) {
      throw new IllegalArgumentException("Too few tool tips!!!");
    }
  }

  @Override
  public void paint(Graphics2D g, int width, int height) {

    setWidth(width);
    setHeight(height);

    setSeriesStyles();

    paintBackground(g);

    plot.paint(g);
    chartTitle.paint(g);
    legend.paint(g);
  }

  /** set the series color based on theme */
  private void setSeriesStyles() {

    SeriesColorMarkerLineStyleCycler seriesColorMarkerLineStyleCycler =
        new SeriesColorMarkerLineStyleCycler(
            getStyler().getSeriesColors(),
            getStyler().getSeriesMarkers(),
            getStyler().getSeriesLines());
    for (RadarSeries series : getSeriesMap().values()) {

      SeriesColorMarkerLineStyle seriesColorMarkerLineStyle =
          seriesColorMarkerLineStyleCycler.getNextSeriesColorMarkerLineStyle();

      if (series.getLineStyle() == null) { // wasn't set manually
        series.setLineStyle(seriesColorMarkerLineStyle.getStroke());
      }
      if (series.getLineColor() == null) { // wasn't set manually
        series.setLineColor(seriesColorMarkerLineStyle.getColor());
      }
      if (series.getFillColor() == null) { // wasn't set manually
        series.setFillColor(seriesColorMarkerLineStyle.getColor());
      }
      if (series.getMarker() == null) { // wasn't set manually
        series.setMarker(seriesColorMarkerLineStyle.getMarker());
      }
      if (series.getMarkerColor() == null) { // wasn't set manually
        series.setMarkerColor(seriesColorMarkerLineStyle.getColor());
      }
    }
  }

  public String[] getVariableLabels() {

    return variableLabels;
  }

  public void setVariableLabels(String[] variableLabels) {

    this.variableLabels = variableLabels;
  }

  public RadarRenderStyle getRadarRenderStyle() {

    return radarRenderStyle;
  }

  public void setRadarRenderStyle(RadarRenderStyle radarRenderStyle) {

    this.radarRenderStyle = radarRenderStyle;
  }

  public enum RadarRenderStyle {
    Polygon,
    Circle;
  }
}
