package org.knowm.xchart;

import java.awt.*;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.chartpart.Legend_Pie;
import org.knowm.xchart.internal.chartpart.Plot_Dial;
import org.knowm.xchart.internal.style.SeriesColorMarkerLineStyle;
import org.knowm.xchart.internal.style.SeriesColorMarkerLineStyleCycler;
import org.knowm.xchart.style.DialStyler;
import org.knowm.xchart.style.Styler.ChartTheme;
import org.knowm.xchart.style.Theme;

public class DialChart extends Chart<DialStyler, DialSeries> {

  /**
   * Constructor - the default Chart Theme will be used (XChartTheme)
   *
   * @param width
   * @param height
   */
  public DialChart(int width, int height) {

    super(width, height, new DialStyler());
    plot = new Plot_Dial<DialStyler, DialSeries>(this);
    legend = new Legend_Pie<DialStyler, DialSeries>(this);
  }

  /**
   * Constructor
   *
   * @param width
   * @param height
   * @param theme - pass in a instance of Theme class, probably a custom Theme.
   */
  public DialChart(int width, int height, Theme theme) {

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
  public DialChart(int width, int height, ChartTheme chartTheme) {

    this(width, height, chartTheme.newInstance(chartTheme));
  }

  /**
   * Constructor
   *
   * @param chartBuilder
   */
  public DialChart(DialChartBuilder chartBuilder) {

    this(chartBuilder.width, chartBuilder.height, chartBuilder.chartTheme);
    setTitle(chartBuilder.title);
  }

  /**
   * Add a series for a Dial type chart
   *
   * @param seriesName
   * @param value
   * @return
   */
  public DialSeries addSeries(String seriesName, double value) {

    return addSeries(seriesName, value, null);
  }

  /**
   * Add a series for a Dial type chart
   *
   * @param seriesName
   * @param value
   * @param annotation
   * @return
   */
  public DialSeries addSeries(String seriesName, double value, String annotation) {

    // Sanity checks
    sanityCheck(seriesName, value);

    DialSeries series = new DialSeries(seriesName, value, annotation);

    seriesMap.put(seriesName, series);

    return series;
  }

  private void sanityCheck(String seriesName, double value) {

    if (seriesMap.keySet().contains(seriesName)) {
      throw new IllegalArgumentException(
          "Series name >"
              + seriesName
              + "< has already been used. Use unique names for each series!!!");
    }
    if (value < 0 || value > 1) {
      throw new IllegalArgumentException("Value must be in [0, 1] range!!!");
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
    for (DialSeries series : getSeriesMap().values()) {

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
}
