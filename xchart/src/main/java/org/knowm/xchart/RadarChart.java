/**
 * Copyright 2015-2017 Knowm Inc. (http://knowm.org) and contributors.
 * Copyright 2011-2015 Xeiam LLC (http://xeiam.com) and contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.knowm.xchart;

import java.awt.Graphics2D;

import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.chartpart.Legend_Pie;
import org.knowm.xchart.internal.chartpart.Plot_Radar;
import org.knowm.xchart.internal.style.SeriesColorMarkerLineStyle;
import org.knowm.xchart.internal.style.SeriesColorMarkerLineStyleCycler;
import org.knowm.xchart.style.RadarStyler;
import org.knowm.xchart.style.Styler.ChartTheme;
import org.knowm.xchart.style.Theme;

/**
 * @author timmolter
 */
public class RadarChart extends Chart<RadarStyler, RadarSeries> {

  public enum RadarRenderStyle {
    Polygon, Circle;
  }

  private RadarRenderStyle radarRenderStyle = RadarRenderStyle.Polygon;

  protected String[] variableLabels;

  /**
   * Constructor - the default Chart Theme will be used (XChartTheme)
   *
   * @param width
   * @param height
   */
  public RadarChart(int width, int height) {

    super(width, height, new RadarStyler());
    plot = new Plot_Radar(this);
    legend = new Legend_Pie(this);
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
   * @param value
   * @return
   */
  public RadarSeries addSeries(String seriesName, double[] values) {

    return addSeries(seriesName, values, null);
  }

  public RadarSeries addSeries(String seriesName, double[] values, String[] toolTips) {

    // Sanity checks
    sanityCheck(seriesName, values, toolTips);

    RadarSeries series = new RadarSeries(seriesName, values, toolTips);

    seriesMap.put(seriesName, series);

    return series;
  }

  private void sanityCheck(String seriesName, double[] values, String[] toolTips) {

    if (variableLabels == null) {
      throw new IllegalArgumentException("Variable labels cannot be null!!!");
    }

    if (seriesMap.keySet().contains(seriesName)) {
      throw new IllegalArgumentException("Series name >" + seriesName + "< has already been used. Use unique names for each series!!!");
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

    if (toolTips != null && toolTips.length < variableLabels.length) {
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

  /**
   * set the series color based on theme
   */
  private void setSeriesStyles() {

    SeriesColorMarkerLineStyleCycler seriesColorMarkerLineStyleCycler = new SeriesColorMarkerLineStyleCycler(getStyler().getSeriesColors(), getStyler().getSeriesMarkers(), getStyler()
        .getSeriesLines());
    for (RadarSeries series : getSeriesMap().values()) {

      SeriesColorMarkerLineStyle seriesColorMarkerLineStyle = seriesColorMarkerLineStyleCycler.getNextSeriesColorMarkerLineStyle();

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

}
