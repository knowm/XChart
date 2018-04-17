package org.knowm.xchart;

import java.awt.*;
import java.util.Map;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.chartpart.Legend_Pie;
import org.knowm.xchart.internal.chartpart.Plot_Pie;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.internal.style.SeriesColorMarkerLineStyle;
import org.knowm.xchart.internal.style.SeriesColorMarkerLineStyleCycler;
import org.knowm.xchart.style.PieStyler;
import org.knowm.xchart.style.Styler.ChartTheme;
import org.knowm.xchart.style.Theme;

/** @author timmolter */
public class PieChart extends Chart<PieStyler, PieSeries> {

  /**
   * Constructor - the default Chart Theme will be used (XChartTheme)
   *
   * @param width
   * @param height
   */
  public PieChart(int width, int height) {

    super(width, height, new PieStyler());
    plot = new Plot_Pie<PieStyler, PieSeries>(this);
    legend = new Legend_Pie<PieStyler, PieSeries>(this);
  }

  /**
   * Constructor
   *
   * @param width
   * @param height
   * @param theme - pass in a instance of Theme class, probably a custom Theme.
   */
  public PieChart(int width, int height, Theme theme) {

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
  public PieChart(int width, int height, ChartTheme chartTheme) {

    this(width, height, chartTheme.newInstance(chartTheme));
  }

  /**
   * Constructor
   *
   * @param chartBuilder
   */
  public PieChart(PieChartBuilder chartBuilder) {

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
  public PieSeries addSeries(String seriesName, Number value) {

    PieSeries series = new PieSeries(seriesName, value);

    if (seriesMap.keySet().contains(seriesName)) {
      throw new IllegalArgumentException(
          "Series name >"
              + seriesName
              + "< has already been used. Use unique names for each series!!!");
    }
    seriesMap.put(seriesName, series);

    return series;
  }

  /**
   * Update a series by updating the pie slide value
   *
   * @param seriesName
   * @param value
   * @return
   */
  public PieSeries updatePieSeries(String seriesName, Number value) {

    Map<String, PieSeries> seriesMap = getSeriesMap();
    PieSeries series = seriesMap.get(seriesName);
    if (series == null) {
      throw new IllegalArgumentException("Series name >" + seriesName + "< not found!!!");
    }
    series.replaceData(value);

    return series;
  }

  @Override
  public void paint(Graphics2D g, int width, int height) {

    setWidth(width);
    setHeight(height);

    // set the series types if they are not set. Legend and Plot need it.
    for (PieSeries seriesPie : getSeriesMap().values()) {
      PieSeries.PieSeriesRenderStyle seriesType =
          seriesPie.getChartPieSeriesRenderStyle(); // would be directly set
      if (seriesType == null) { // wasn't overridden, use default from Style Manager
        seriesPie.setChartPieSeriesRenderStyle(getStyler().getDefaultSeriesRenderStyle());
      }
    }
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
    for (Series series : getSeriesMap().values()) {

      SeriesColorMarkerLineStyle seriesColorMarkerLineStyle =
          seriesColorMarkerLineStyleCycler.getNextSeriesColorMarkerLineStyle();

      if (series.getFillColor() == null) { // wasn't set manually
        series.setFillColor(seriesColorMarkerLineStyle.getColor());
      }
    }
  }
}
