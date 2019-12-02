package org.knowm.xchart;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Iterator;

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

public class BoxPlotChart extends Chart<BoxPlotStyler, BoxPlotSeries> {

  protected BoxPlotChart(int width, int height) {

    super(width, height, new BoxPlotStyler());
    axisPair = new AxisPair<BoxPlotStyler, BoxPlotSeries>(this);
    plot = new Plot_BoxPlot<BoxPlotStyler, BoxPlotSeries>(this);
    legend = new Legend_Marker<BoxPlotStyler, BoxPlotSeries>(this);
    infoPanel = new InfoPanel<BoxPlotStyler, BoxPlotSeries>(this);
  }

  public BoxPlotChart(int width, int height, Theme theme) {

    this(width, height);
    styler.setTheme(theme);
  }

  public BoxPlotChart(int width, int height, ChartTheme chartTheme) {
    this(width, height, chartTheme.newInstance(chartTheme));
  }

  public BoxPlotChart(BoxPlotChartBuilder chartBuilder) {
    this(chartBuilder.width, chartBuilder.height, chartBuilder.chartTheme);
    setTitle(chartBuilder.title);
    setXAxisTitle(chartBuilder.xAxisTitle);
    setYAxisTitle(chartBuilder.yAxisTitle);
  }

  public BoxPlotSeries addSeries(String seriesName, List<?> xData, List<? extends Number> yData) {

    return addSeries(seriesName, xData, yData, null);
  }

  public BoxPlotSeries addSeries(String seriesName, int[] xData, int[] yData) {

    return addSeries(seriesName, xData, yData, null);
  }

  public BoxPlotSeries addSeries(String seriesName, int[] xData, int[] yData, int[] errorBars) {

    return addSeries(seriesName, Utils.getNumberListFromIntArray(xData), Utils.getNumberListFromIntArray(yData),
        Utils.getNumberListFromIntArray(errorBars));
  }

  public BoxPlotSeries addSeries(String seriesName, double[] xData, double[] yData, double[] errorBars) {

    return addSeries(seriesName, Utils.getNumberListFromDoubleArray(xData), Utils.getNumberListFromDoubleArray(yData),
        Utils.getNumberListFromDoubleArray(errorBars));
  }

  public BoxPlotSeries addSeries(String seriesName, List<?> xData, List<? extends Number> yData,
      List<? extends Number> errorBars) {

    // Sanity checks
    sanityCheck(seriesName, xData, yData, errorBars);
    BoxPlotSeries series = null;

    if (xData != null) {

      if (xData.size() != yData.size()) {
        throw new IllegalArgumentException("X and Y-Axis sizes are not the same !!!");
      }
    } else {
      xData = Utils.getGeneratedDataAsList(yData.size());
    }
    series = new BoxPlotSeries(seriesName, xData, yData, errorBars, getDataType(xData));
    seriesMap.put(seriesName, series);
    return series;
  }

  private DataType getDataType(List<?> data) {

    DataType axisType;
    Iterator<?> itr = data.iterator();
    Object dataPoint = itr.next();
    if (dataPoint instanceof Number) {
      axisType = DataType.Number;
    } else if (dataPoint instanceof Date) {
      axisType = DataType.Date;
    } else if (dataPoint instanceof String) {
      axisType = DataType.String;
    } else {
      throw new IllegalArgumentException("Series data must be either Number, Data or String type!!!");
    }
    return axisType;
  }

  private void sanityCheck(String seriesName, List<?> xData, List<? extends Number> yData,
      List<? extends Number> errorBars) {

    if (seriesMap.keySet().contains(seriesName)) {
      throw new IllegalArgumentException(
          "Series name > " + seriesName + " < has already been used. Use unique names for each series!!!");
    }
    if (yData == null) {
      throw new IllegalArgumentException("Y-Axis data connot be null !!!");
    }
    if (yData.size() == 0) {
      throw new IllegalArgumentException("Y-Axis data connot be empyt !!!");
    }
    if (xData != null && xData.size() == 0) {
      throw new IllegalArgumentException("X-Axis data connot be empty !!!");
    }
    if (errorBars != null && errorBars.size() != yData.size()) {
      throw new IllegalArgumentException("Error bars and Y-Axis sizes are not the same !!!");
    }
  }

  public BoxPlotSeries updateBoxSeries(String seriesName, double[] newXData, double[] newYData,
      double[] newErrorBarData) {

    return updateBoxSeries(seriesName, Utils.getNumberListFromDoubleArray(newXData),
        Utils.getNumberListFromDoubleArray(newYData), Utils.getNumberListFromDoubleArray(newErrorBarData));
  }

  public BoxPlotSeries updateBoxSeries(String seriesName, List<?> newXData, List<? extends Number> newYData,
      List<? extends Number> newErrorBarData) {

    Map<String, BoxPlotSeries> seriesMap = getSeriesMap();
    BoxPlotSeries series = seriesMap.get(seriesName);

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

    SeriesColorMarkerLineStyleCycler seriesColorMarkerLineStyleCycler = new SeriesColorMarkerLineStyleCycler(
        getStyler().getSeriesColors(), getStyler().getSeriesMarkers(), getStyler().getSeriesLines());

    for (BoxPlotSeries series : getSeriesMap().values()) {
      SeriesColorMarkerLineStyle seriesColorMarkerLineStyle = seriesColorMarkerLineStyleCycler
          .getNextSeriesColorMarkerLineStyle();
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
    legend.paint(g);
    infoPanel.paint(g);
  }
}
