package org.knowm.xchart;

import java.awt.*;
import org.knowm.xchart.internal.chartpart.RenderableSeries;
import org.knowm.xchart.internal.chartpart.RenderableSeries.LegendRenderType;
import org.knowm.xchart.internal.series.AxesChartSeries;

/** @author arthurmcgibbon */
public class OHLCSeries extends AxesChartSeries {

  private double[] xData; // can be Number or Date(epochtime)
  private double[] openData;
  private double[] highData;
  private double[] lowData;
  private double[] closeData;
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

    super(name, xAxisDataType);
    this.xData = xData;
    this.openData = openData;
    this.highData = highData;
    this.lowData = lowData;
    this.closeData = closeData;
    calculateMinMax();
  }

  public OHLCSeriesRenderStyle getOhlcSeriesRenderStyle() {

    return ohlcSeriesRenderStyle;
  }

  public OHLCSeries setOhlcSeriesRenderStyle(OHLCSeriesRenderStyle ohlcSeriesRenderStyle) {

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

    // Sanity check should already by done
    this.xData = newXData;
    this.openData = newOpenData;
    this.highData = newHighData;
    this.lowData = newLowData;
    this.closeData = newCloseData;
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
    double[] yMinMax = findMinMax(lowData, highData);
    yMin = yMinMax[0];
    yMax = yMinMax[1];
  }

  public double[] getXData() {

    return xData;
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

  public enum OHLCSeriesRenderStyle implements RenderableSeries {
    Candle(LegendRenderType.Line),

    HiLo(LegendRenderType.Line);

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
