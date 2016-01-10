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
package org.knowm.xchart;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import org.knowm.xchart.internal.Series;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.chartpart.LegendPie;
import org.knowm.xchart.internal.chartpart.Plot_Pie;
import org.knowm.xchart.internal.style.SeriesColorMarkerLineStyle;
import org.knowm.xchart.internal.style.SeriesColorMarkerLineStyleCycler;
import org.knowm.xchart.internal.style.StyleManager.ChartTheme;
import org.knowm.xchart.internal.style.Theme;

/**
 * @author timmolter
 */
public class Chart_Pie extends Chart<StyleManagerPie, Series_Pie> {

  /**
   * Constructor - the default Chart Theme will be used (XChartTheme)
   *
   * @param width
   * @param height
   */
  public Chart_Pie(int width, int height) {

    super(width, height, new StyleManagerPie());
    plot = new Plot_Pie(this);
    chartLegend = new LegendPie(this);
  }

  /**
   * Constructor
   *
   * @param width
   * @param height
   * @param theme - pass in a instance of Theme class, probably a custom Theme.
   */
  public Chart_Pie(int width, int height, Theme theme) {

    this(width, height);
    styleManager.setTheme(theme);
  }

  /**
   * Constructor
   *
   * @param width
   * @param height
   * @param chartTheme - pass in the desired ChartTheme enum
   */
  public Chart_Pie(int width, int height, ChartTheme chartTheme) {

    this(width, height, chartTheme.newInstance(chartTheme));
  }

  /**
   * Constructor
   *
   * @param chartBuilder
   */
  public Chart_Pie(ChartBuilderPie chartBuilder) {

    this(chartBuilder.width, chartBuilder.height, chartBuilder.chartTheme);
    setTitle(chartBuilder.title);
  }

  /**
   * Add a series for a Pie type chart
   *
   * @param seriesName
   * @param value
   * @return
   */
  public Series_Pie addSeries(String seriesName, Number value) {

    Series_Pie series = new Series_Pie(seriesName, value);

    if (seriesMap.keySet().contains(seriesName)) {
      throw new IllegalArgumentException("Series name >" + seriesName + "< has already been used. Use unique names for each series!!!");
    }
    seriesMap.put(seriesName, series);

    return series;
  }

  @Override
  public void paint(Graphics2D g, int width, int height) {

    setWidth(width);
    setHeight(height);
    paint(g);
  }

  @Override
  public void paint(Graphics2D g) {

    // Sanity checks
    if (getSeriesMap().isEmpty()) {
      throw new RuntimeException("No series defined for Chart!!!");
    }

    // set the series types if they are not set. Legend and Plot need it.
    for (Series_Pie seriesPie : getSeriesMap().values()) {
      Series_Pie.ChartPieSeriesRenderStyle seriesType = seriesPie.getChartPieSeriesRenderStyle(); // would be directly set
      if (seriesType == null) { // wasn't overridden, use default from Style Manager
        seriesPie.setChartPieSeriesRenderStyle(getStyleManager().getChartPieSeriesRenderStyle());
      }
    }

    setSeriesStyles();

    // paint chart main background
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // global rendering hint
    g.setColor(styleManager.getChartBackgroundColor());
    Shape rect = new Rectangle2D.Double(0, 0, getWidth(), getHeight());
    g.fill(rect);

    plot.paint(g);
    chartTitle.paint(g);
    chartLegend.paint(g);

    g.dispose();
  }

  /**
   * set the series color based on theme
   */
  public void setSeriesStyles() {

    SeriesColorMarkerLineStyleCycler seriesColorMarkerLineStyleCycler = new SeriesColorMarkerLineStyleCycler(getStyleManager().getSeriesColors(), getStyleManager().getSeriesMarkers(),
        getStyleManager().getSeriesLines());
    for (Series series : getSeriesMap().values()) {

      SeriesColorMarkerLineStyle seriesColorMarkerLineStyle = seriesColorMarkerLineStyleCycler.getNextSeriesColorMarkerLineStyle();

      if (series.getFillColor() == null) { // wasn't set manually
        series.setFillColor(seriesColorMarkerLineStyle.getColor());
      }
    }
  }

}
