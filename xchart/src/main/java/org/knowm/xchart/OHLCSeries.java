/**
 * Copyright 2015-2017 Knowm Inc. (http://knowm.org) and contributors.
 * Copyright 2011-2015 Xeiam LLC (http://xeiam.com) and contributors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.knowm.xchart;

import org.knowm.xchart.internal.chartpart.RenderableSeries;
import org.knowm.xchart.internal.chartpart.RenderableSeries.LegendRenderType;
import org.knowm.xchart.internal.series.AxesChartSeries;

import java.awt.*;

/**
 * @author arthurmcgibbon
 */
public class OHLCSeries extends AxesChartSeries {

  public enum OHLCSeriesRenderStyle implements RenderableSeries {

    Candle(LegendRenderType.Line),

    HiLo(LegendRenderType.Line);

    private final LegendRenderType legendRenderType;

    OHLCSeriesRenderStyle(LegendRenderType legendRenderType) {

      this.legendRenderType = legendRenderType;
    }

    @Override public LegendRenderType getLegendRenderType() {

      return legendRenderType;
    }
  }

  private double[] xData; // can be Number or Date(epochtime)

  private double[] openData;
  private double[] highData;
  private double[] lowData;
  private double[] closeData;

  private OHLCSeriesRenderStyle ohlcSeriesRenderStyle;

  /**
   * Up Color
   */
  private Color upColor;

  /**
   * Down Color
   */
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
  public OHLCSeries(String name, double[] xData, double[] openData, double[] highData, double[] lowData, double[] closeData, DataType xAxisDataType) {

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

  /**
   * Set the up color of the series
   *
   * @param color
   */
  public OHLCSeries setUpColor(java.awt.Color color) {

    this.upColor = color;
    return this;
  }

  public Color getUpColor() {

    return upColor;
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

  public Color getDownColor() {

    return downColor;
  }

  @Override public LegendRenderType getLegendRenderType() {
    return ohlcSeriesRenderStyle.getLegendRenderType();
  }

  /**
   * This is an internal method which shouldn't be called from client code. Use {@link org.knowm.xchart.OHLCChart#updateOHLCSeries} instead!
   *
   * @param newXData
   * @param newOpenData
   * @param newHighData
   * @param newLowData
   * @param newCloseData
   */
  void replaceData(double[] newXData, double[] newOpenData, double[] newHighData, double[] newLowData, double[] newCloseData) {

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

      if (highs[i] != Double.NaN && highs[i] > max) {
        max = highs[i];
      }
      if (lows[i] != Double.NaN && lows[i] < min) {
        min = lows[i];
      }
    }

    return new double[]{min, max};
  }

  @Override protected void calculateMinMax() {

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
}