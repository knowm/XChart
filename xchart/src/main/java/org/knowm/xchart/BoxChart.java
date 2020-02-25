package org.knowm.xchart;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.knowm.xchart.internal.Utils;
import org.knowm.xchart.internal.chartpart.AxisPair;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.chartpart.InfoPanel;
import org.knowm.xchart.internal.chartpart.Legend_Marker;
import org.knowm.xchart.internal.chartpart.Plot_BoxPlot;
import org.knowm.xchart.internal.series.Series.DataType;
import org.knowm.xchart.internal.style.SeriesColorMarkerLineStyle;
import org.knowm.xchart.internal.style.SeriesColorMarkerLineStyleCycler;
import org.knowm.xchart.style.BoxPlotStyler;
import org.knowm.xchart.style.Styler.ChartTheme;
import org.knowm.xchart.style.Theme;

public class BoxChart extends Chart<BoxPlotStyler, BoxSeries> {

  private ArrayList xData = new ArrayList();
  private ArrayList newXData = new ArrayList();

  protected BoxChart(int width, int height) {

    super(width, height, new BoxPlotStyler());
    axisPair = new AxisPair<BoxPlotStyler, BoxSeries>(this);
    plot = new Plot_BoxPlot<BoxPlotStyler, BoxSeries>(this);
    legend = new Legend_Marker<BoxPlotStyler, BoxSeries>(this);
    infoPanel = new InfoPanel<BoxPlotStyler, BoxSeries>(this);
  }

  public BoxChart(int width, int height, Theme theme) {

    this(width, height);
    styler.setTheme(theme);
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
    BoxSeries series = null;
    xData.add(seriesName);
    series = new BoxSeries(seriesName, xData, yData, null, DataType.String);
    seriesMap.put(seriesName, series);
    return series;
  }

  private void sanityCheck(String seriesName, List<? extends Number> yData) {

    if (seriesMap.keySet().contains(seriesName)) {
      throw new IllegalArgumentException(
          "Series name > "
              + seriesName
              + " < has already been used. Use unique names for each series!!!");
    }
    if (yData == null) {
      throw new IllegalArgumentException("Y-Axis data connot be null !!!");
    }
    if (yData.size() == 0) {
      throw new IllegalArgumentException("Y-Axis data connot be empyt !!!");
    }
  }

  public BoxSeries updateBoxSeries(String seriesName, double[] newYData) {

    newXData.add(seriesName);
    return updateBoxSeries(
        seriesName,
        newXData,
        Utils.getNumberListFromDoubleArray(newYData),
        Utils.getNumberListFromDoubleArray(null));
  }

  public BoxSeries updateBoxSeries(
      String seriesName,
      List<?> newXData,
      List<? extends Number> newYData,
      List<? extends Number> newErrorBarData) {

    Map<String, BoxSeries> seriesMap = getSeriesMap();
    BoxSeries series = seriesMap.get(seriesName);

    if (series == null) {
      throw new IllegalArgumentException("Series name > " + seriesName + " < not found !!!");
    }

    if (newXData == null) {
      // generate X-Data
      List<Integer> generatedXData = new ArrayList<Integer>();

      for (int i = 1; i <= newYData.size(); i++) {
        generatedXData.add(i);
      }
      series.replaceData(generatedXData, newYData, newErrorBarData);
    } else {
      series.replaceData(newXData, newYData, newErrorBarData);
    }
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
    infoPanel.paint(g);
  }
}
