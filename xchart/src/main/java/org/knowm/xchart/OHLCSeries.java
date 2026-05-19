package org.knowm.xchart;

import java.awt.Color;
import java.util.Arrays;

import org.knowm.xchart.internal.chartpart.RenderableSeries;
import org.knowm.xchart.internal.chartpart.RenderableSeries.LegendRenderType;
import org.knowm.xchart.internal.series.MarkerSeries;

public class OHLCSeries extends MarkerSeries {

  // full unfiltered data — retained so zoom can be reset to the original range
  private double[] xDataAll;
  private double[] openDataAll;
  private double[] highDataAll;
  private double[] lowDataAll;
  private double[] closeDataAll;
  private long[] volumeDataAll;
  private double[] yDataAll;

  // active (possibly zoom-filtered) data — what the chart actually renders
  private double[] xData; // can be Number or Date(epochtime)
  private double[] openData;
  private double[] highData;
  private double[] lowData;
  private double[] closeData;
  private long[] volumeData;
  private double[] yData;
  private OHLCSeriesRenderStyle ohlcSeriesRenderStyle;

  /** Up Color */
  private Color upColor;

  /** Down Color */
  private Color downColor;

  /**
   * Constructor
   *
   * @param name
   * @param xData
   * @param openData
   * @param highData
   * @param lowData
   * @param closeData
   */
  public OHLCSeries(
      String name,
      double[] xData,
      double[] openData,
      double[] highData,
      double[] lowData,
      double[] closeData,
      DataType xAxisDataType) {

    this(name, xData, openData, highData, lowData, closeData, null, xAxisDataType);
  }

  /**
   * Constructor
   *
   * @param name
   * @param xData
   * @param openData
   * @param highData
   * @param lowData
   * @param closeData
   * @param volumeData
   */
  public OHLCSeries(
      String name,
      double[] xData,
      double[] openData,
      double[] highData,
      double[] lowData,
      double[] closeData,
      long[] volumeData,
      DataType xAxisDataType) {

    super(name, xAxisDataType);
    this.xDataAll = xData;
    this.openDataAll = openData;
    this.highDataAll = highData;
    this.lowDataAll = lowData;
    this.closeDataAll = closeData;
    this.volumeDataAll = volumeData;
    this.xData = xData;
    this.openData = openData;
    this.highData = highData;
    this.lowData = lowData;
    this.closeData = closeData;
    this.volumeData = volumeData;
    calculateMinMax();
  }

  /**
   * Constructor
   *
   * @param name
   * @param xData
   * @param yData
   * @param xAxisDataType
   */
  public OHLCSeries(String name, double[] xData, double[] yData, DataType xAxisDataType) {

    super(name, xAxisDataType);
    this.xDataAll = xData;
    this.yDataAll = yData;
    this.xData = xData;
    this.yData = yData;
    this.ohlcSeriesRenderStyle = OHLCSeriesRenderStyle.Line;
    calculateMinMax();
  }

  public OHLCSeriesRenderStyle getOhlcSeriesRenderStyle() {

    return ohlcSeriesRenderStyle;
  }

  public OHLCSeries setOhlcSeriesRenderStyle(OHLCSeriesRenderStyle ohlcSeriesRenderStyle) {

    if (yData == null && ohlcSeriesRenderStyle == OHLCSeriesRenderStyle.Line) {
      throw new IllegalArgumentException(
          "Series name >"
              + this.getName()
              + "<, yData is equal to null and cannot be set to OHLCSeriesRenderStyle.Line");
    }
    if (yData != null && ohlcSeriesRenderStyle != OHLCSeriesRenderStyle.Line) {
      throw new IllegalArgumentException(
          "Series name >"
              + this.getName()
              + "<, yData is not equal to null and can only be set to OHLCSeriesRenderStyle.Line");
    }
    this.ohlcSeriesRenderStyle = ohlcSeriesRenderStyle;
    return this;
  }

  public Color getUpColor() {

    return upColor;
  }

  /**
   * Set the up color of the series
   *
   * @param color
   */
  public OHLCSeries setUpColor(java.awt.Color color) {

    this.upColor = color;
    return this;
  }

  public Color getDownColor() {

    return downColor;
  }

  /**
   * Set the down color of the series
   *
   * @param color
   */
  public OHLCSeries setDownColor(java.awt.Color color) {

    this.downColor = color;
    return this;
  }

  @Override
  public LegendRenderType getLegendRenderType() {

    return ohlcSeriesRenderStyle.getLegendRenderType();
  }

  /**
   * This is an internal method which shouldn't be called from client code. Use {@link
   * org.knowm.xchart.OHLCChart#updateOHLCSeries} instead!
   *
   * @param newXData
   * @param newOpenData
   * @param newHighData
   * @param newLowData
   * @param newCloseData
   */
  void replaceData(
      double[] newXData,
      double[] newOpenData,
      double[] newHighData,
      double[] newLowData,
      double[] newCloseData) {

    replaceData(newXData, newOpenData, newHighData, newLowData, newCloseData, null);
  }

  /**
   * This is an internal method which shouldn't be called from client code. Use {@link
   * org.knowm.xchart.OHLCChart#updateOHLCSeries} instead!
   *
   * @param newXData
   * @param newOpenData
   * @param newHighData
   * @param newLowData
   * @param newCloseData
   * @param newVolumeData
   */
  void replaceData(
      double[] newXData,
      double[] newOpenData,
      double[] newHighData,
      double[] newLowData,
      double[] newCloseData,
      long[] newVolumeData) {

    // Sanity check should already by done
    this.xData = newXData;
    this.openData = newOpenData;
    this.highData = newHighData;
    this.lowData = newLowData;
    this.closeData = newCloseData;
    this.volumeData = newVolumeData;
    calculateMinMax();
  }

  /**
   * This is an internal method which shouldn't be called from client code. Use {@link
   * org.knowm.xchart.OHLCChart#updateOHLCSeries} instead!
   *
   * @param newXData
   * @param newYData
   */
  void replaceData(double[] newXData, double[] newYData) {

    this.xData = newXData;
    this.yData = newYData;
    calculateMinMax();
  }

  /**
   * Finds the min and max of a dataset
   *
   * @param lows
   * @param highs
   * @return
   */
  private double[] findMinMax(double[] lows, double[] highs) {

    double min = Double.MAX_VALUE;
    double max = -Double.MAX_VALUE;

    for (int i = 0; i < highs.length; i++) {

      if (!Double.isNaN(highs[i]) && highs[i] > max) {
        max = highs[i];
      }
      if (!Double.isNaN(lows[i]) && lows[i] < min) {
        min = lows[i];
      }
    }

    return new double[] {min, max};
  }

  @Override
  protected void calculateMinMax() {

    double[] xMinMax = findMinMax(xData, xData);
    xMin = xMinMax[0];
    xMax = xMinMax[1];
    final double[] yMinMax;
    if (yData == null) {
      yMinMax = findMinMax(lowData, highData);
    } else {
      yMinMax = findMinMax(yData, yData);
    }
    yMin = yMinMax[0];
    yMax = yMinMax[1];
  }

  public double[] getXData() {

    return xData;
  }

  public boolean isAllXData() {

    return xData.length == xDataAll.length;
  }

  public void filterXByIndex(int startIndex, int endIndex) {

    startIndex = Math.max(0, startIndex);
    int len = xDataAll.length;
    endIndex = Math.min(len, endIndex);
    xData = Arrays.copyOfRange(xDataAll, startIndex, endIndex);
    if (openDataAll != null) openData = Arrays.copyOfRange(openDataAll, startIndex, endIndex);
    if (highDataAll != null) highData = Arrays.copyOfRange(highDataAll, startIndex, endIndex);
    if (lowDataAll != null) lowData = Arrays.copyOfRange(lowDataAll, startIndex, endIndex);
    if (closeDataAll != null) closeData = Arrays.copyOfRange(closeDataAll, startIndex, endIndex);
    if (volumeDataAll != null) volumeData = Arrays.copyOfRange(volumeDataAll, startIndex, endIndex);
    if (yDataAll != null) yData = Arrays.copyOfRange(yDataAll, startIndex, endIndex);
    calculateMinMax();
  }

  public boolean filterXByValue(double minValue, double maxValue) {

    int length = xDataAll.length;
    boolean[] keep = new boolean[length];
    int count = 0;
    for (int i = 0; i < length; i++) {
      keep[i] = xDataAll[i] >= minValue && xDataAll[i] <= maxValue;
      if (keep[i]) count++;
    }
    if (count == length) {
      return false;
    }
    xData = new double[count];
    double[] newOpen = openDataAll != null ? new double[count] : null;
    double[] newHigh = highDataAll != null ? new double[count] : null;
    double[] newLow = lowDataAll != null ? new double[count] : null;
    double[] newClose = closeDataAll != null ? new double[count] : null;
    long[] newVolume = volumeDataAll != null ? new long[count] : null;
    double[] newY = yDataAll != null ? new double[count] : null;
    int idx = 0;
    for (int i = 0; i < length; i++) {
      if (!keep[i]) continue;
      xData[idx] = xDataAll[i];
      if (newOpen != null) newOpen[idx] = openDataAll[i];
      if (newHigh != null) newHigh[idx] = highDataAll[i];
      if (newLow != null) newLow[idx] = lowDataAll[i];
      if (newClose != null) newClose[idx] = closeDataAll[i];
      if (newVolume != null) newVolume[idx] = volumeDataAll[i];
      if (newY != null) newY[idx] = yDataAll[i];
      idx++;
    }
    openData = newOpen;
    highData = newHigh;
    lowData = newLow;
    closeData = newClose;
    volumeData = newVolume;
    yData = newY;
    calculateMinMax();
    return true;
  }

  public void resetFilter() {

    xData = xDataAll;
    openData = openDataAll;
    highData = highDataAll;
    lowData = lowDataAll;
    closeData = closeDataAll;
    volumeData = volumeDataAll;
    yData = yDataAll;
    calculateMinMax();
  }

  public double[] getOpenData() {

    return openData;
  }

  public double[] getHighData() {

    return highData;
  }

  public double[] getLowData() {

    return lowData;
  }

  public double[] getCloseData() {

    return closeData;
  }

  // TODO remove this??
  public long[] getVolumeData() {

    return volumeData;
  }

  public double[] getYData() {

    return yData;
  }

  public enum OHLCSeriesRenderStyle implements RenderableSeries {
    Candle(LegendRenderType.Line),
    HiLo(LegendRenderType.Line),
    Line(LegendRenderType.Line);

    private final LegendRenderType legendRenderType;

    OHLCSeriesRenderStyle(LegendRenderType legendRenderType) {

      this.legendRenderType = legendRenderType;
    }

    @Override
    public LegendRenderType getLegendRenderType() {

      return legendRenderType;
    }
  }
}
