package org.knowm.xchart;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.knowm.xchart.OHLCSeries.OHLCSeriesRenderStyle;
import org.knowm.xchart.internal.Utils;
import org.knowm.xchart.internal.chartpart.AxisPair;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.chartpart.Legend_OHLC;
import org.knowm.xchart.internal.chartpart.Plot_OHLC;
import org.knowm.xchart.internal.series.Series.DataType;
import org.knowm.xchart.internal.style.SeriesColorMarkerLineStyle;
import org.knowm.xchart.internal.style.SeriesColorMarkerLineStyleCycler;
import org.knowm.xchart.style.OHLCStyler;
import org.knowm.xchart.style.Styler.ChartTheme;
import org.knowm.xchart.style.theme.Theme;

/** @author arthurmcgibbon */
public class OHLCChart extends Chart<OHLCStyler, OHLCSeries> {

  /**
   * Constructor - the default Chart Theme will be used (XChartTheme)
   *
   * @param width
   * @param height
   */
  public OHLCChart(int width, int height) {

    super(width, height, new OHLCStyler());

    axisPair = new AxisPair<OHLCStyler, OHLCSeries>(this);
    plot = new Plot_OHLC<OHLCStyler, OHLCSeries>(this);
    legend = new Legend_OHLC<OHLCStyler, OHLCSeries>(this);
  }

  /**
   * Constructor
   *
   * @param width
   * @param height
   * @param theme - pass in a instance of Theme class, probably a custom Theme.
   */
  public OHLCChart(int width, int height, Theme theme) {

    this(width, height);
    styler.setTheme(theme);
    styler.setToolTipBackgroundColor(new Color(210, 210, 210));
    styler.setToolTipFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
  }

  /**
   * Constructor
   *
   * @param width
   * @param height
   * @param chartTheme - pass in the desired ChartTheme enum
   */
  public OHLCChart(int width, int height, ChartTheme chartTheme) {

    this(width, height, chartTheme.newInstance(chartTheme));
  }

  /**
   * Constructor
   *
   * @param chartBuilder
   */
  public OHLCChart(OHLCChartBuilder chartBuilder) {

    this(chartBuilder.getWidth(), chartBuilder.getHeight(), chartBuilder.getChartTheme());
    setTitle(chartBuilder.getTitle());
    setXAxisTitle(chartBuilder.xAxisTitle);
    setYAxisTitle(chartBuilder.yAxisTitle);
  }

  /**
   * Add a series for a OHLC type chart using using float arrays
   *
   * @param seriesName
   * @param openData the open data
   * @param highData the high data
   * @param lowData the low data
   * @param closeData the close data
   * @return A Series object that you can set properties on
   */
  public OHLCSeries addSeries(
      String seriesName, float[] openData, float[] highData, float[] lowData, float[] closeData) {

    return addSeries(seriesName, null, openData, highData, lowData, closeData);
  }

  /**
   * Add a series for a OHLC type chart using using float arrays
   *
   * @param seriesName
   * @param xData the x-axis data
   * @param openData the open data
   * @param highData the high data
   * @param lowData the low data
   * @param closeData the close data
   * @return A Series object that you can set properties on
   */
  public OHLCSeries addSeries(
      String seriesName,
      float[] xData,
      float[] openData,
      float[] highData,
      float[] lowData,
      float[] closeData) {

    return addSeries(
        seriesName,
        Utils.getDoubleArrayFromFloatArray(xData),
        Utils.getDoubleArrayFromFloatArray(openData),
        Utils.getDoubleArrayFromFloatArray(highData),
        Utils.getDoubleArrayFromFloatArray(lowData),
        Utils.getDoubleArrayFromFloatArray(closeData),
        null,
        DataType.Number);
  }

  /**
   * Add a series for a OHLC type chart using using float arrays
   *
   * @param seriesName
   * @param xData the x-axis data
   * @param openData the open data
   * @param highData the high data
   * @param lowData the low data
   * @param closeData the close data
   * @param volumeData the volume data
   * @return A Series object that you can set properties on
   */
  public OHLCSeries addSeries(
      String seriesName,
      float[] xData,
      float[] openData,
      float[] highData,
      float[] lowData,
      float[] closeData,
      float[] volumeData) {

    return addSeries(
        seriesName,
        Utils.getDoubleArrayFromFloatArray(xData),
        Utils.getDoubleArrayFromFloatArray(openData),
        Utils.getDoubleArrayFromFloatArray(highData),
        Utils.getDoubleArrayFromFloatArray(lowData),
        Utils.getDoubleArrayFromFloatArray(closeData),
        Utils.getLongArrayFromFloatArray(volumeData),
        DataType.Number);
  }

  /**
   * Add a series for a OHLC type chart using using int arrays
   *
   * @param seriesName
   * @param openData the open data
   * @param highData the high data
   * @param lowData the low data
   * @param closeData the close data
   * @return A Series object that you can set properties on
   */
  public OHLCSeries addSeries(
      String seriesName, int[] openData, int[] highData, int[] lowData, int[] closeData) {

    return addSeries(seriesName, null, openData, highData, lowData, closeData);
  }

  /**
   * Add a series for a OHLC type chart using using int arrays
   *
   * @param seriesName
   * @param xData the x-axis data
   * @param openData the open data
   * @param highData the high data
   * @param lowData the low data
   * @param closeData the close data
   * @return A Series object that you can set properties on
   */
  public OHLCSeries addSeries(
      String seriesName,
      int[] xData,
      int[] openData,
      int[] highData,
      int[] lowData,
      int[] closeData) {

    return addSeries(
        seriesName,
        Utils.getDoubleArrayFromIntArray(xData),
        Utils.getDoubleArrayFromIntArray(openData),
        Utils.getDoubleArrayFromIntArray(highData),
        Utils.getDoubleArrayFromIntArray(lowData),
        Utils.getDoubleArrayFromIntArray(closeData),
        null,
        DataType.Number);
  }

  /**
   * Add a series for a OHLC type chart using using int arrays
   *
   * @param seriesName
   * @param xData the x-axis data
   * @param openData the open data
   * @param highData the high data
   * @param lowData the low data
   * @param closeData the close data
   * @param volumeData the volume data
   * @return A Series object that you can set properties on
   */
  public OHLCSeries addSeries(
      String seriesName,
      int[] xData,
      int[] openData,
      int[] highData,
      int[] lowData,
      int[] closeData,
      int[] volumeData) {

    return addSeries(
        seriesName,
        Utils.getDoubleArrayFromIntArray(xData),
        Utils.getDoubleArrayFromIntArray(openData),
        Utils.getDoubleArrayFromIntArray(highData),
        Utils.getDoubleArrayFromIntArray(lowData),
        Utils.getDoubleArrayFromIntArray(closeData),
        Utils.getLongArrayFromIntArray(volumeData),
        DataType.Number);
  }

  /**
   * Add a series for a OHLC type chart using Lists
   *
   * @param seriesName
   * @param xData the x-axis data
   * @param openData the open data
   * @param highData the high data
   * @param lowData the low data
   * @param closeData the close data
   * @return A Series object that you can set properties on
   */
  public OHLCSeries addSeries(
      String seriesName,
      List<?> xData,
      List<? extends Number> openData,
      List<? extends Number> highData,
      List<? extends Number> lowData,
      List<? extends Number> closeData) {

    DataType dataType = getDataType(xData);
    switch (dataType) {
      case Date:
        return addSeries(
            seriesName,
            Utils.getDoubleArrayFromDateList(xData),
            Utils.getDoubleArrayFromNumberList(openData),
            Utils.getDoubleArrayFromNumberList(highData),
            Utils.getDoubleArrayFromNumberList(lowData),
            Utils.getDoubleArrayFromNumberList(closeData),
            null,
            DataType.Date);

      default:
        return addSeries(
            seriesName,
            Utils.getDoubleArrayFromNumberList(xData),
            Utils.getDoubleArrayFromNumberList(openData),
            Utils.getDoubleArrayFromNumberList(highData),
            Utils.getDoubleArrayFromNumberList(lowData),
            Utils.getDoubleArrayFromNumberList(closeData),
            null,
            DataType.Number);
    }
  }

  /**
   * Add a series for a OHLC type chart using Lists
   *
   * @param seriesName
   * @param xData the x-axis data
   * @param openData the open data
   * @param highData the high data
   * @param lowData the low data
   * @param closeData the close data
   * @param volumeData the volume data
   * @return A Series object that you can set properties on
   */
  public OHLCSeries addSeries(
      String seriesName,
      List<?> xData,
      List<? extends Number> openData,
      List<? extends Number> highData,
      List<? extends Number> lowData,
      List<? extends Number> closeData,
      List<? extends Number> volumeData) {

    DataType dataType = getDataType(xData);
    switch (dataType) {
      case Date:
        return addSeries(
            seriesName,
            Utils.getDoubleArrayFromDateList(xData),
            Utils.getDoubleArrayFromNumberList(openData),
            Utils.getDoubleArrayFromNumberList(highData),
            Utils.getDoubleArrayFromNumberList(lowData),
            Utils.getDoubleArrayFromNumberList(closeData),
            Utils.getLongArrayFromNumberList(volumeData),
            DataType.Date);

      default:
        return addSeries(
            seriesName,
            Utils.getDoubleArrayFromNumberList(xData),
            Utils.getDoubleArrayFromNumberList(openData),
            Utils.getDoubleArrayFromNumberList(highData),
            Utils.getDoubleArrayFromNumberList(lowData),
            Utils.getDoubleArrayFromNumberList(closeData),
            Utils.getLongArrayFromNumberList(volumeData),
            DataType.Number);
    }
  }

  /**
   * Add a series for a OHLC type chart using Lists
   *
   * @param seriesName
   * @param openData the open data
   * @param highData the high data
   * @param lowData the low data
   * @param closeData the close data
   * @return A Series object that you can set properties on
   */
  public OHLCSeries addSeries(
      String seriesName,
      List<? extends Number> openData,
      List<? extends Number> highData,
      List<? extends Number> lowData,
      List<? extends Number> closeData) {

    return addSeries(seriesName, null, openData, highData, lowData, closeData);
  }

  /**
   * Add a series for a Line type chart using int arrays
   *
   * @param seriesName
   * @param xData the X-Axis data
   * @param xData the Y-Axis data
   * @return A Series object that you can set properties on
   */
  public OHLCSeries addSeries(String seriesName, int[] xData, int[] yData) {

    return addSeries(
        seriesName,
        Utils.getDoubleArrayFromIntArray(xData),
        Utils.getDoubleArrayFromIntArray(yData),
        DataType.Number);
  }

  /**
   * Add a series for a Line type chart using float arrays
   *
   * @param seriesName
   * @param xData the X-Axis data
   * @param xData the Y-Axis data
   * @return A Series object that you can set properties on
   */
  public OHLCSeries addSeries(String seriesName, float[] xData, float[] yData) {

    return addSeries(
        seriesName,
        Utils.getDoubleArrayFromFloatArray(xData),
        Utils.getDoubleArrayFromFloatArray(yData),
        DataType.Number);
  }

  /**
   * Add a series for a Line type chart using double arrays
   *
   * @param seriesName
   * @param xData the X-Axis data
   * @param xData the Y-Axis data
   * @return A Series object that you can set properties on
   */
  public OHLCSeries addSeries(String seriesName, double[] xData, double[] yData) {

    return addSeries(seriesName, xData, yData, DataType.Number);
  }

  /**
   * Add a series for a Line type chart using Lists
   *
   * @param seriesName
   * @param xData the X-Axis data
   * @param xData the Y-Axis data
   * @return A Series object that you can set properties on
   */
  public OHLCSeries addSeries(String seriesName, List<?> xData, List<? extends Number> yData) {

    DataType dataType = getDataType(xData);
    switch (dataType) {
      case Date:
        return addSeries(
            seriesName,
            Utils.getDoubleArrayFromDateList(xData),
            Utils.getDoubleArrayFromNumberList(yData),
            getDataType(xData));

      default:
        return addSeries(
            seriesName,
            Utils.getDoubleArrayFromNumberList(xData),
            Utils.getDoubleArrayFromNumberList(yData),
            getDataType(xData));
    }
  }

  private DataType getDataType(List<?> data) {

    if (data == null || data.isEmpty()) {
      return DataType.Number; // It will be autogenerated
    }

    Iterator<?> itr = data.iterator();
    if (!itr.hasNext()) {
        throw new IllegalArgumentException(
                "List of data should contain next Iteration to get data type!!!");
    }
    return getAxisType(itr.next());
  }

private DataType getAxisType(Object dataPoint) {
	DataType axisType;
	if (dataPoint instanceof Number) {
      axisType = DataType.Number;
    } else if (dataPoint instanceof Date) {
      axisType = DataType.Date;
    } else {
      throw new IllegalArgumentException("Series data must be either Number or Date type!!!");
    }
	return axisType;
}

  public OHLCSeries addSeries(
      String seriesName,
      double[] xData,
      double[] openData,
      double[] highData,
      double[] lowData,
      double[] closeData) {

    return addSeries(
        seriesName, xData, openData, highData, lowData, closeData, null, DataType.Number);
  }

  /**
   * Add a series for a OHLC type chart using using double arrays
   *
   * @param seriesName
   * @param openData the open data
   * @param highData the high data
   * @param lowData the low data
   * @param closeData the close data
   * @return A Series object that you can set properties on
   */
  public OHLCSeries addSeries(
      String seriesName,
      double[] openData,
      double[] highData,
      double[] lowData,
      double[] closeData) {

    return addSeries(seriesName, null, openData, highData, lowData, closeData);
  }

  /**
   * Add a series for a OHLC type chart using using double arrays
   *
   * @param seriesName
   * @param xData the x-axis data
   * @param openData the open data
   * @param highData the high data
   * @param lowData the low data
   * @param closeData the close data
   * @param volumeData the volume data
   * @return A Series object that you can set properties on
   */
  public OHLCSeries addSeries(
      String seriesName,
      double[] xData,
      double[] openData,
      double[] highData,
      double[] lowData,
      double[] closeData,
      long[] volumeData) {

    return addSeries(
        seriesName, xData, openData, highData, lowData, closeData, volumeData, DataType.Number);
  }

  private OHLCSeries addSeries(
      String seriesName,
      double[] xData,
      double[] openData,
      double[] highData,
      double[] lowData,
      double[] closeData,
      long[] volumeData,
      DataType dataType) {

    if (seriesMap.containsKey(seriesName)) {
      throw new IllegalArgumentException(
          "Series name >"
              + seriesName
              + "< has already been used. Use unique names for each series!!!");
    }

    // Sanity checks
    sanityCheck(seriesName, openData, highData, lowData, closeData, volumeData);

    final double[] xDataToUse;
    if (xData != null) {
      // Sanity check
      checkDataLengths(seriesName, "X-Axis", "Close", xData, closeData);

      xDataToUse = xData;
    } else { // generate xData
      xDataToUse = Utils.getGeneratedDataAsArray(closeData.length);
    }
    OHLCSeries series =
        new OHLCSeries(
            seriesName, xDataToUse, openData, highData, lowData, closeData, volumeData, dataType);
    seriesMap.put(seriesName, series);

    return series;
  }

  private OHLCSeries addSeries(
      String seriesName, double[] xData, double[] yData, DataType dataType) {

    if (seriesMap.containsKey(seriesName)) {
      throw new IllegalArgumentException(
          "Series name >"
              + seriesName
              + "< has already been used. Use unique names for each series!!!");
    }

    final double[] xDataToUse;
    if (xData != null) {
      // Sanity check
      checkDataLengths(seriesName, "X-Axis", "Y-Axis", xData, yData);

      xDataToUse = xData;
    } else { // generate xData
      xDataToUse = Utils.getGeneratedDataAsArray(yData.length);
    }
    OHLCSeries series = new OHLCSeries(seriesName, xDataToUse, yData, dataType);
    seriesMap.put(seriesName, series);
    return series;
  }

  /**
   * Update a series by updating the xData, openData, highData, lowData and closeData
   *
   * @param seriesName
   * @param newXData - set null to be automatically generated as a list of increasing Integers
   *     starting from 1 and ending at the size of the new Y-Axis data list.
   * @param newOpenData
   * @param newHighData
   * @param newLowData
   * @param newCloseData
   * @return
   */
  public OHLCSeries updateOHLCSeries(
      String seriesName,
      List<?> newXData,
      List<? extends Number> newOpenData,
      List<? extends Number> newHighData,
      List<? extends Number> newLowData,
      List<? extends Number> newCloseData) {

    DataType dataType = getDataType(newXData);
    switch (dataType) {
      case Date:
        return updateOHLCSeries(
            seriesName,
            Utils.getDoubleArrayFromDateList(newXData),
            Utils.getDoubleArrayFromNumberList(newOpenData),
            Utils.getDoubleArrayFromNumberList(newHighData),
            Utils.getDoubleArrayFromNumberList(newLowData),
            Utils.getDoubleArrayFromNumberList(newCloseData));

      default:
        return updateOHLCSeries(
            seriesName,
            Utils.getDoubleArrayFromNumberList(newXData),
            Utils.getDoubleArrayFromNumberList(newOpenData),
            Utils.getDoubleArrayFromNumberList(newHighData),
            Utils.getDoubleArrayFromNumberList(newLowData),
            Utils.getDoubleArrayFromNumberList(newCloseData));
    }
  }

  public OHLCSeries updateOHLCSeries(
      String seriesName,
      List<?> newXData,
      List<? extends Number> newOpenData,
      List<? extends Number> newHighData,
      List<? extends Number> newLowData,
      List<? extends Number> newCloseData,
      List<? extends Number> volumeData) {

    DataType dataType = getDataType(newXData);
    switch (dataType) {
      case Date:
        return updateOHLCSeries(
            seriesName,
            Utils.getDoubleArrayFromDateList(newXData),
            Utils.getDoubleArrayFromNumberList(newOpenData),
            Utils.getDoubleArrayFromNumberList(newHighData),
            Utils.getDoubleArrayFromNumberList(newLowData),
            Utils.getDoubleArrayFromNumberList(newCloseData),
            Utils.getLongArrayFromNumberList(volumeData));

      default:
        return updateOHLCSeries(
            seriesName,
            Utils.getDoubleArrayFromNumberList(newXData),
            Utils.getDoubleArrayFromNumberList(newOpenData),
            Utils.getDoubleArrayFromNumberList(newHighData),
            Utils.getDoubleArrayFromNumberList(newLowData),
            Utils.getDoubleArrayFromNumberList(newCloseData),
            Utils.getLongArrayFromNumberList(volumeData));
    }
  }

  /**
   * Update a series by updating the xData, openData, highData, lowData and closeData
   *
   * @param seriesName
   * @param newXData - set null to be automatically generated as a list of increasing Integers
   *     starting from 1 and ending at the size of the new Y-Axis data list.
   * @param newOpenData
   * @param newHighData
   * @param newLowData
   * @param newCloseData
   * @return
   */
  public OHLCSeries updateOHLCSeries(
      String seriesName,
      double[] newXData,
      double[] newOpenData,
      double[] newHighData,
      double[] newLowData,
      double[] newCloseData) {

    return updateOHLCSeries(
        seriesName, newXData, newOpenData, newHighData, newLowData, newCloseData, null);
  }

  /**
   * Update a series by updating the xData, openData, highData, lowData,closeData and volumeData
   *
   * @param seriesName
   * @param newXData - set null to be automatically generated as a list of increasing Integers
   *     starting from 1 and ending at the size of the new Y-Axis data list.
   * @param newOpenData
   * @param newHighData
   * @param newLowData
   * @param newCloseData
   * @param newVolumeData
   * @return
   */
  public OHLCSeries updateOHLCSeries(
      String seriesName,
      double[] newXData,
      double[] newOpenData,
      double[] newHighData,
      double[] newLowData,
      double[] newCloseData,
      long[] newVolumeData) {

    sanityCheck(seriesName, newOpenData, newHighData, newLowData, newCloseData, newVolumeData);

    Map<String, OHLCSeries> seriesMap = getSeriesMap();
    OHLCSeries series = seriesMap.get(seriesName);
    if (series == null) {
      throw new IllegalArgumentException("Series name >" + seriesName + "< not found!!!");
    }
    final double[] xDataToUse;
    if (newXData != null) {
      // Sanity check
      checkDataLengths(seriesName, "X-Axis", "Close", newXData, newCloseData);
      xDataToUse = newXData;
    } else {
      xDataToUse = Utils.getGeneratedDataAsArray(newCloseData.length);
    }

    series.replaceData(
        xDataToUse, newOpenData, newHighData, newLowData, newCloseData, newVolumeData);

    return series;
  }

  /**
   * Update a series by updating the X-Axis and Y-Axis data
   *
   * @param seriesName
   * @param newXData
   * @param newYData
   * @return
   */
  public OHLCSeries updateOHLCSeries(
      String seriesName, List<?> newXData, List<? extends Number> newYData) {

    DataType dataType = getDataType(newXData);
    switch (dataType) {
      case Date:
        return updateOHLCSeries(
            seriesName,
            Utils.getDoubleArrayFromDateList(newXData),
            Utils.getDoubleArrayFromNumberList(newYData));

      default:
        return updateOHLCSeries(
            seriesName,
            Utils.getDoubleArrayFromNumberList(newXData),
            Utils.getDoubleArrayFromNumberList(newYData));
    }
  }

  /**
   * Update a series by updating the X-Axis and Y-Axis data
   *
   * @param seriesName
   * @param newXData
   * @param newYData
   * @return
   */
  public OHLCSeries updateOHLCSeries(String seriesName, double[] newXData, double[] newYData) {

    Map<String, OHLCSeries> seriesMap = getSeriesMap();
    OHLCSeries series = seriesMap.get(seriesName);
    if (series == null) {
      throw new IllegalArgumentException("Series name >" + seriesName + "< not found!!!");
    }
    final double[] xDataToUse;
    if (newXData != null) {
      // Sanity check
      checkDataLengths(seriesName, "newXData", "newYData", newXData, newYData);
      xDataToUse = newXData;
    } else {
      xDataToUse = Utils.getGeneratedDataAsArray(newYData.length);
    }

    series.replaceData(xDataToUse, newYData);
    return series;
  }

  private void checkData(String seriesName, String dataName, double[] data) {

    if (data == null) {
      throw new IllegalArgumentException(dataName + " data cannot be null!!! >" + seriesName);
    }
    if (data.length == 0) {
      throw new IllegalArgumentException(dataName + " data cannot be empty!!! >" + seriesName);
    }
  }

  private void checkDataLengths(
      String seriesName, String data1Name, String data2Name, double[] data1, double[] data2) {

    if (data1.length != data2.length) {
      throw new IllegalArgumentException(
          data1Name + " and " + data2Name + " sizes are not the same!!! >" + seriesName);
    }
  }

  ///////////////////////////////////////////////////
  // Internal Members and Methods ///////////////////
  ///////////////////////////////////////////////////

  private void sanityCheck(
      String seriesName,
      double[] openData,
      double[] highData,
      double[] lowData,
      double[] closeData,
      long[] volumeData) {

    checkData(seriesName, "Open", openData);
    checkData(seriesName, "High", highData);
    checkData(seriesName, "Low", lowData);
    checkData(seriesName, "Close", closeData);

    checkDataLengths(seriesName, "Open", "Close", openData, closeData);
    checkDataLengths(seriesName, "High", "Close", highData, closeData);
    checkDataLengths(seriesName, "Low", "Close", lowData, closeData);
    if (volumeData != null) {
      if (volumeData.length != closeData.length) {
        throw new IllegalArgumentException(
            "Volume and Close sizes are not the same!!! >" + seriesName);
      }
    }
  }

  @Override
  public void paint(Graphics2D g, int width, int height) {

    setWidth(width);
    setHeight(height);

    // set the series render styles if they are not set. Legend and Plot need it.
    for (OHLCSeries series : getSeriesMap().values()) {
      OHLCSeries.OHLCSeriesRenderStyle renderStyle =
          series.getOhlcSeriesRenderStyle(); // would be directly set
      if (renderStyle == null) { // wasn't overridden, use default from Style Manager
        series.setOhlcSeriesRenderStyle(getStyler().getDefaultSeriesRenderStyle());
      }
    }
    setSeriesStyles();

    paintBackground(g);

    axisPair.paint(g);
    plot.paint(g);
    chartTitle.paint(g);
    legend.paint(g);
    annotations.paint(g);
  }

  /** set the series color, marker and line style based on theme */
  private void setSeriesStyles() {

    SeriesColorMarkerLineStyleCycler seriesColorMarkerLineStyleCycler =
        new SeriesColorMarkerLineStyleCycler(
            getStyler().getSeriesColors(),
            getStyler().getSeriesMarkers(),
            getStyler().getSeriesLines());
    for (OHLCSeries series : getSeriesMap().values()) {

      SeriesColorMarkerLineStyle seriesColorMarkerLineStyle =
          seriesColorMarkerLineStyleCycler.getNextSeriesColorMarkerLineStyle();

      if (series.getLineStyle() == null) { // wasn't set manually
        series.setLineStyle(seriesColorMarkerLineStyle.getStroke());
      }
      if (series.getOhlcSeriesRenderStyle() == OHLCSeriesRenderStyle.Line
          && series.getLineColor() == null) { // wasn't set manually
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
      if (series.getUpColor() == null) { // wasn't set manually
        series.setUpColor(new Color(19, 179, 70));
      }
      if (series.getDownColor() == null) { // wasn't set manually
        series.setDownColor(new Color(242, 39, 42));
      }
    }
  }
}
