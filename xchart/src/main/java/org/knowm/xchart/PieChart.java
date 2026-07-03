package org.knowm.xchart;

import java.awt.Graphics2D;
import java.util.Map;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.chartpart.Legend_Pie;
import org.knowm.xchart.internal.chartpart.Plot_Pie;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.internal.style.SeriesColorMarkerLineStyle;
import org.knowm.xchart.internal.style.SeriesColorMarkerLineStyleCycler;
import org.knowm.xchart.style.PieStyler;
import org.knowm.xchart.style.Styler.ChartTheme;
import org.knowm.xchart.style.theme.Theme;

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

    this(
        chartBuilder.width,
        chartBuilder.height,
        chartBuilder.customTheme != null
            ? chartBuilder.customTheme
            : chartBuilder.chartTheme.newInstance(chartBuilder.chartTheme));
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

    if (seriesMap.containsKey(seriesName)) {
      throw new IllegalArgumentException(
          "Series name >"
              + seriesName
              + "< has already been used. Use unique names for each series!!!");
    }
    sanityCheck(seriesName, value);

    PieSeries series = new PieSeries(seriesName, value);
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

    Map<String, PieSeries> seriesMap = this.seriesMap;
    PieSeries series = seriesMap.get(seriesName);
    if (series == null) {
      throw new IllegalArgumentException("Series name >" + seriesName + "< not found!!!");
    }
    sanityCheck(seriesName, value);
    series.replaceData(value);

    return series;
  }

  /**
   * Pie slices represent a fraction of a whole, so only non-negative, finite values make sense. Fail
   * fast on invalid input rather than rendering a corrupted chart.
   *
   * @param seriesName
   * @param value
   */
  private void sanityCheck(String seriesName, Number value) {

    if (value == null) {
      throw new IllegalArgumentException("Value cannot be null!!! >" + seriesName);
    }
    double doubleValue = value.doubleValue();
    if (Double.isNaN(doubleValue) || Double.isInfinite(doubleValue)) {
      throw new IllegalArgumentException("Value cannot be NaN or infinite!!! >" + seriesName);
    }
    if (doubleValue < 0.0) {
      throw new IllegalArgumentException("Value cannot be negative!!! >" + seriesName);
    }
  }

  @Override
  public void paint(Graphics2D g, int width, int height) {

    setWidth(width);
    setHeight(height);

    // set the series types if they are not set. Legend and Plot need it.
    for (PieSeries seriesPie : seriesMap.values()) {
      PieSeries.PieSeriesRenderStyle seriesType =
          seriesPie.getChartPieSeriesRenderStyle(); // would be directly set
      if (seriesType == null) { // wasn't overridden, use default from Style Manager
        seriesPie.setChartPieSeriesRenderStyle(getStyler().getDefaultSeriesRenderStyle());
      }
    }
    setSeriesStyles();

    if (styler.isToolTipsAlwaysVisible()) enableInteractionData();
    paintBackground(g);

    plot.paint(g);
    chartTitle.paint(g);
    legend.paint(g);
    paintAlwaysVisibleToolTips(g);
  }

  /** set the series color based on theme */
  private void setSeriesStyles() {

    SeriesColorMarkerLineStyleCycler seriesColorMarkerLineStyleCycler =
        new SeriesColorMarkerLineStyleCycler(
            getStyler().getSeriesColors(),
            getStyler().getSeriesMarkers(),
            getStyler().getSeriesLines());
    for (Series series : seriesMap.values()) {

      SeriesColorMarkerLineStyle seriesColorMarkerLineStyle =
          seriesColorMarkerLineStyleCycler.getNextSeriesColorMarkerLineStyle();

      if (series.getFillColor() == null) { // wasn't set manually
        series.setFillColor(seriesColorMarkerLineStyle.getColor());
      }
    }
  }
}
