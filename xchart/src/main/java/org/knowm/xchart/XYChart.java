package org.knowm.xchart;

import java.awt.*;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.knowm.xchart.internal.Utils;
import org.knowm.xchart.internal.chartpart.AxisPair;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.chartpart.Legend_Marker;
import org.knowm.xchart.internal.chartpart.Plot_XY;
import org.knowm.xchart.internal.series.Series.DataType;
import org.knowm.xchart.internal.style.SeriesColorMarkerLineStyle;
import org.knowm.xchart.internal.style.SeriesColorMarkerLineStyleCycler;
import org.knowm.xchart.style.Styler.ChartTheme;
import org.knowm.xchart.style.Theme;
import org.knowm.xchart.style.XYStyler;

public class XYChart extends Chart<XYStyler, XYSeries> {

  /**
   * Constructor - the default Chart Theme will be used (XChartTheme)
   *
   * @param width
   * @param height
   */
  public XYChart(int width, int height) {

    super(width, height, new XYStyler());

    axisPair = new AxisPair<XYStyler, XYSeries>(this);
    plot = new Plot_XY<XYStyler, XYSeries>(this);
    legend = new Legend_Marker<XYStyler, XYSeries>(this);
  }

  /**
   * Constructor
   *
   * @param width
   * @param height
   * @param theme - pass in a instance of Theme class, probably a custom Theme.
   */
  public XYChart(int width, int height, Theme theme) {

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
  public XYChart(int width, int height, ChartTheme chartTheme) {

    this(width, height, chartTheme.newInstance(chartTheme));
  }

  /**
   * Constructor
   *
   * @param chartBuilder
   */
  public XYChart(XYChartBuilder chartBuilder) {

    this(chartBuilder.width, chartBuilder.height, chartBuilder.chartTheme);
    setTitle(chartBuilder.title);
    setXAxisTitle(chartBuilder.xAxisTitle);
    setYAxisTitle(chartBuilder.yAxisTitle);
  }

  /**
   * Add a series for a X-Y type chart using using double arrays
   *
   * @param seriesName
   * @param yData the Y-Axis data
   * @return A Series object that you can set properties on
   */
  public XYSeries addSeries(String seriesName, double[] yData) {

    return addSeries(seriesName, null, yData);
  }

  /**
   * Add a series for a X-Y type chart using using double arrays
   *
   * @param seriesName
   * @param xData the X-Axis data
   * @param xData the Y-Axis data
   * @return A Series object that you can set properties on
   */
  public XYSeries addSeries(String seriesName, double[] xData, double[] yData) {

    return addSeries(seriesName, xData, yData, null, DataType.Number);
  }

  /**
   * Add a series for a X-Y type chart using using float arrays
   *
   * @param seriesName
   * @param yData the Y-Axis data
   * @return A Series object that you can set properties on
   */
  public XYSeries addSeries(String seriesName, float[] yData) {

    return addSeries(seriesName, null, yData, null);
  }

  /**
   * Add a series for a X-Y type chart using using float arrays
   *
   * @param seriesName
   * @param xData the X-Axis data
   * @param xData the Y-Axis data
   * @return A Series object that you can set properties on
   */
  public XYSeries addSeries(String seriesName, float[] xData, float[] yData) {

    return addSeries(seriesName, xData, yData, null);
  }

  /**
   * Add a series for a X-Y type chart using using float arrays with error bars
   *
   * @param seriesName
   * @param xData the X-Axis data
   * @param xData the Y-Axis data
   * @param errorBars the error bar data
   * @return A Series object that you can set properties on
   */
  public XYSeries addSeries(String seriesName, float[] xData, float[] yData, float[] errorBars) {

    return addSeries(
        seriesName,
        Utils.getDoubleArrayFromFloatArray(xData),
        Utils.getDoubleArrayFromFloatArray(yData),
        Utils.getDoubleArrayFromFloatArray(errorBars),
        DataType.Number);
  }

  /**
   * Add a series for a X-Y type chart using using int arrays
   *
   * @param seriesName
   * @param yData the Y-Axis data
   * @return A Series object that you can set properties on
   */
  public XYSeries addSeries(String seriesName, int[] yData) {

    return addSeries(seriesName, null, yData, null);
  }

  /**
   * Add a series for a X-Y type chart using using int arrays
   *
   * @param seriesName
   * @param xData the X-Axis data
   * @param yData the Y-Axis data
   * @return A Series object that you can set properties on
   */
  public XYSeries addSeries(String seriesName, int[] xData, int[] yData) {

    return addSeries(seriesName, xData, yData, null);
  }

  /**
   * Add a series for a X-Y type chart using using int arrays with error bars
   *
   * @param seriesName
   * @param xData the X-Axis data
   * @param xData the Y-Axis data
   * @param errorBars the error bar data
   * @return A Series object that you can set properties on
   */
  public XYSeries addSeries(String seriesName, int[] xData, int[] yData, int[] errorBars) {

    return addSeries(
        seriesName,
        Utils.getDoubleArrayFromIntArray(xData),
        Utils.getDoubleArrayFromIntArray(yData),
        Utils.getDoubleArrayFromIntArray(errorBars),
        DataType.Number);
  }

  /**
   * Add a series for a X-Y type chart using Lists
   *
   * @param seriesName
   * @param xData the X-Axis data
   * @param yData the Y-Axis data
   * @return A Series object that you can set properties on
   */
  public XYSeries addSeries(String seriesName, List<?> xData, List<? extends Number> yData) {

    return addSeries(seriesName, xData, yData, null);
  }

  /**
   * Add a series for a X-Y type chart using Lists
   *
   * @param seriesName
   * @param yData the Y-Axis data
   * @return A Series object that you can set properties on
   */
  public XYSeries addSeries(String seriesName, List<? extends Number> yData) {

    return addSeries(seriesName, null, yData, null);
  }

  /**
   * Add a series for a X-Y type chart using Lists
   *
   * @param seriesName
   * @param xData the X-Axis data
   * @param yData the Y-Axis data
   * @param errorBars the error bar data
   * @return A Series object that you can set properties on
   */
  public XYSeries addSeries(
      String seriesName,
      List<?> xData,
      List<? extends Number> yData,
      List<? extends Number> errorBars) {

    DataType dataType = getDataType(xData);
    switch (dataType) {
      case Date:
        return addSeries(
            seriesName,
            Utils.getDoubleArrayFromDateList(xData),
            Utils.getDoubleArrayFromNumberList(yData),
            Utils.getDoubleArrayFromNumberList(errorBars),
            DataType.Date);

      default:
        return addSeries(
            seriesName,
            Utils.getDoubleArrayFromNumberList(xData),
            Utils.getDoubleArrayFromNumberList(yData),
            Utils.getDoubleArrayFromNumberList(errorBars),
            DataType.Number);
    }
  }

  private DataType getDataType(List<?> data) {

    if (data == null) {
      return DataType.Number; // It will be autogenerated
    }

    DataType axisType;

    Iterator<?> itr = data.iterator();
    Object dataPoint = itr.next();
    if (dataPoint instanceof Number) {
      axisType = DataType.Number;
    } else if (dataPoint instanceof Date) {
      axisType = DataType.Date;
    } else {
      throw new IllegalArgumentException("Series data must be either Number or Date type!!!");
    }
    return axisType;
  }

  public XYSeries addSeries(String seriesName, double[] xData, double[] yData, double[] errorBars) {

    return addSeries(seriesName, xData, yData, errorBars, DataType.Number);
  }

  /**
   * Add a series for a X-Y type chart using Lists with error bars
   *
   * @param seriesName
   * @param xData the X-Axis data
   * @param yData the Y-Axis data
   * @param errorBars the error bar data
   * @return A Series object that you can set properties on
   */
  private XYSeries addSeries(
      String seriesName, double[] xData, double[] yData, double[] errorBars, DataType dataType) {

    // Sanity checks
    sanityCheck(seriesName, xData, yData, errorBars);

    XYSeries series;
    if (xData != null) {

      // Sanity check
      if (xData.length != yData.length) {
        throw new IllegalArgumentException("X and Y-Axis sizes are not the same!!!");
      }

      series = new XYSeries(seriesName, xData, yData, errorBars, dataType);
    } else { // generate xData
      series =
          new XYSeries(
              seriesName, Utils.getGeneratedDataAsArray(yData.length), yData, errorBars, dataType);
    }

    seriesMap.put(seriesName, series);

    return series;
  }

  /**
   * Update a series by updating the X-Axis, Y-Axis and error bar data
   *
   * @param seriesName
   * @param newXData - set null to be automatically generated as a list of increasing Integers
   *     starting from 1 and ending at the size of the new Y-Axis data list.
   * @param newYData
   * @param newErrorBarData - set null if there are no error bars
   * @return
   */
  public XYSeries updateXYSeries(
      String seriesName,
      List<?> newXData,
      List<? extends Number> newYData,
      List<? extends Number> newErrorBarData) {

    DataType dataType = getDataType(newXData);
    switch (dataType) {
      case Date:
        return updateXYSeries(
            seriesName,
            Utils.getDoubleArrayFromDateList(newXData),
            Utils.getDoubleArrayFromNumberList(newYData),
            Utils.getDoubleArrayFromNumberList(newErrorBarData));

      default:
        return updateXYSeries(
            seriesName,
            Utils.getDoubleArrayFromNumberList(newXData),
            Utils.getDoubleArrayFromNumberList(newYData),
            Utils.getDoubleArrayFromNumberList(newErrorBarData));
    }
  }

  /**
   * Update a series by updating the X-Axis, Y-Axis and error bar data
   *
   * @param seriesName
   * @param newXData - set null to be automatically generated as a list of increasing Integers
   *     starting from 1 and ending at the size of the new Y-Axis data list.
   * @param newYData
   * @param newErrorBarData - set null if there are no error bars
   * @return
   */
  public XYSeries updateXYSeries(
      String seriesName, double[] newXData, double[] newYData, double[] newErrorBarData) {

    Map<String, XYSeries> seriesMap = getSeriesMap();
    XYSeries series = seriesMap.get(seriesName);
    if (series == null) {
      throw new IllegalArgumentException("Series name >" + seriesName + "< not found!!!");
    }
    if (newXData == null) {
      double[] generatedXData = Utils.getGeneratedDataAsArray(newYData.length);
      series.replaceData(generatedXData, newYData, newErrorBarData);
    } else {
      series.replaceData(newXData, newYData, newErrorBarData);
    }

    return series;
  }

  ///////////////////////////////////////////////////
  // Internal Members and Methods ///////////////////
  ///////////////////////////////////////////////////

  private void sanityCheck(String seriesName, double[] xData, double[] yData, double[] errorBars) {

    if (seriesMap.keySet().contains(seriesName)) {
      throw new IllegalArgumentException(
          "Series name >"
              + seriesName
              + "< has already been used. Use unique names for each series!!!");
    }
    if (yData == null) {
      throw new IllegalArgumentException("Y-Axis data cannot be null!!! >" + seriesName);
    }
    if (yData.length == 0) {
      throw new IllegalArgumentException("Y-Axis data cannot be empty!!! >" + seriesName);
    }
    if (xData != null && xData.length == 0) {
      throw new IllegalArgumentException("X-Axis data cannot be empty!!! >" + seriesName);
    }
    if (errorBars != null && errorBars.length != yData.length) {
      throw new IllegalArgumentException(
          "Error bars and Y-Axis sizes are not the same!!! >" + seriesName);
    }
  }

  @Override
  public void paint(Graphics2D g, int width, int height) {

    setWidth(width);
    setHeight(height);

    // set the series render styles if they are not set. Legend and Plot need it.
    for (XYSeries xySeries : getSeriesMap().values()) {
      XYSeries.XYSeriesRenderStyle chartXYSeriesRenderStyle =
          xySeries.getXYSeriesRenderStyle(); // would be directly set
      if (chartXYSeriesRenderStyle == null) { // wasn't overridden, use default from Style Manager
        xySeries.setXYSeriesRenderStyle(getStyler().getDefaultSeriesRenderStyle());
      }
    }
    setSeriesStyles();

    paintBackground(g);

    axisPair.paint(g);
    plot.paint(g);
    chartTitle.paint(g);
    legend.paint(g);
  }

  /** set the series color, marker and line style based on theme */
  private void setSeriesStyles() {

    SeriesColorMarkerLineStyleCycler seriesColorMarkerLineStyleCycler =
        new SeriesColorMarkerLineStyleCycler(
            getStyler().getSeriesColors(),
            getStyler().getSeriesMarkers(),
            getStyler().getSeriesLines());
    for (XYSeries series : getSeriesMap().values()) {

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
