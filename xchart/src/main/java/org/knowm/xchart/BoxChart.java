package org.knowm.xchart;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.knowm.xchart.internal.Utils;
import org.knowm.xchart.internal.chartpart.AxisPair;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.chartpart.Legend_Marker;
import org.knowm.xchart.internal.chartpart.Plot_Box;
import org.knowm.xchart.internal.series.Series.DataType;
import org.knowm.xchart.internal.style.SeriesColorMarkerLineStyle;
import org.knowm.xchart.internal.style.SeriesColorMarkerLineStyleCycler;
import org.knowm.xchart.style.BoxStyler;
import org.knowm.xchart.style.Styler.ChartTheme;
import org.knowm.xchart.style.theme.Theme;

public class BoxChart extends Chart<BoxStyler, BoxSeries> {

  private List<String> xData = new ArrayList<>();

  protected BoxChart(int width, int height) {

    super(width, height, new BoxStyler());
    axisPair = new AxisPair<BoxStyler, BoxSeries>(this);
    plot = new Plot_Box<BoxStyler, BoxSeries>(this);
    legend = new Legend_Marker<BoxStyler, BoxSeries>(this);
  }

  public BoxChart(int width, int height, Theme theme) {

    this(width, height);
    styler.setTheme(theme);
    // Box chart Legend does not show
    styler.setLegendVisible(false);
  }

  public BoxChart(int width, int height, ChartTheme chartTheme) {
    this(width, height, chartTheme.newInstance(chartTheme));
  }

  public BoxChart(BoxChartBuilder chartBuilder) {
    this(chartBuilder.width, chartBuilder.height, chartBuilder.chartTheme);
    setTitle(chartBuilder.title);
    setXAxisTitle(chartBuilder.xAxisTitle);
    setYAxisTitle(chartBuilder.yAxisTitle);
  }

  public BoxSeries addSeries(String seriesName, int[] yData) {

    return addSeries(seriesName, Utils.getNumberListFromIntArray(yData));
  }

  public BoxSeries addSeries(String seriesName, double[] yData) {

    return addSeries(seriesName, Utils.getNumberListFromDoubleArray(yData));
  }

  public BoxSeries addSeries(String seriesName, List<? extends Number> yData) {

    // Sanity checks
    sanityCheck(seriesName, yData);
    xData.add(seriesName);
    BoxSeries series = new BoxSeries(seriesName, xData, yData, null, DataType.String);
    seriesMap.put(seriesName, series);
    return series;
  }

  private void sanityCheck(String seriesName, List<? extends Number> yData) {

    if (seriesMap.containsKey(seriesName)) {
      throw new IllegalArgumentException(
          "Series name > "
              + seriesName
              + " < has already been used. Use unique names for each series!!!");
    }

    sanityCheckYData(yData);
  }

  private void sanityCheckYData(List<? extends Number> yData) {

    if (yData == null) {
      throw new IllegalArgumentException("Y-Axis data connot be null !!!");
    }
    if (yData.size() == 0) {
      throw new IllegalArgumentException("Y-Axis data connot be empyt !!!");
    }
    if (yData.contains(null)) {
      throw new IllegalArgumentException("Y-Axis data cannot contain null !!!");
    }
  }

  public BoxSeries updateBoxSeries(String seriesName, int[] newYData) {

    return updateBoxSeries(seriesName, Utils.getNumberListFromIntArray(newYData));
  }

  public BoxSeries updateBoxSeries(String seriesName, double[] newYData) {

    return updateBoxSeries(seriesName, Utils.getNumberListFromDoubleArray(newYData));
  }

  public BoxSeries updateBoxSeries(String seriesName, List<? extends Number> newYData) {

    Map<String, BoxSeries> seriesMap = getSeriesMap();
    BoxSeries series = seriesMap.get(seriesName);

    if (series == null) {
      throw new IllegalArgumentException("Series name > " + seriesName + " < not found !!!");
    }
    sanityCheckYData(newYData);
    series.replaceData(newYData);
    return series;
  }

  private void setSeriesStyles() {

    SeriesColorMarkerLineStyleCycler seriesColorMarkerLineStyleCycler =
        new SeriesColorMarkerLineStyleCycler(
            getStyler().getSeriesColors(),
            getStyler().getSeriesMarkers(),
            getStyler().getSeriesLines());
    SeriesColorMarkerLineStyle seriesColorMarkerLineStyle =
        seriesColorMarkerLineStyleCycler.getNextSeriesColorMarkerLineStyle();
    for (BoxSeries series : getSeriesMap().values()) {

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

  @Override
  public void paint(Graphics2D g, int width, int height) {

    setWidth(width);
    setHeight(height);
    setSeriesStyles();
    paintBackground(g);

    axisPair.paint(g);
    plot.paint(g);
    chartTitle.paint(g);
    annotations.forEach(x -> x.paint(g));
  }
}
