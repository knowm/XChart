/**
 * Copyright 2011-2012 Xeiam LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xeiam.xchart;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import com.xeiam.xchart.Axis.AxisType;
import com.xeiam.xchart.interfaces.IChartPart;
import com.xeiam.xchart.series.Series;
import com.xeiam.xchart.series.SeriesLineStyle;

/**
 * @author timmolter
 */
public class PlotContent implements IChartPart {

  /** parent */
  private Plot plot;

  /**
   * Constructor
   * 
   * @param plot
   */
  protected PlotContent(Plot plot) {

    this.plot = plot;
  }

  @Override
  public Rectangle getBounds() {

    return plot.getBounds();
  }

  @Override
  public void paint(Graphics2D g) {

    Rectangle bounds = plot.getBounds();

    Map<Integer, Series> seriesMap = plot.chart.axisPair.seriesMap;
    for (Integer seriesId : seriesMap.keySet()) {

      Series series = seriesMap.get(seriesId);

      // X-Axis
      int xTickSpace = AxisPair.getTickSpace((int) bounds.getWidth());
      int xLeftMargin = AxisPair.getMargin((int) bounds.getWidth(), xTickSpace);

      // Y-Axis
      int yTickSpace = AxisPair.getTickSpace((int) bounds.getHeight());
      int yTopMargin = AxisPair.getMargin((int) bounds.getHeight(), yTickSpace);

      // data points
      Collection<?> xData = series.getxData();
      BigDecimal xMin = plot.chart.axisPair.xAxis.min;
      BigDecimal xMax = plot.chart.axisPair.xAxis.max;
      Collection<Number> yData = series.getyData();
      BigDecimal yMin = plot.chart.axisPair.yAxis.min;
      BigDecimal yMax = plot.chart.axisPair.yAxis.max;
      Collection<Number> errorBars = series.getErrorBars();

      int previousX = Integer.MIN_VALUE;
      int previousY = Integer.MIN_VALUE;

      Iterator<?> xItr = xData.iterator();
      Iterator<Number> yItr = yData.iterator();
      Iterator<Number> ebItr = null;
      if (errorBars != null) {
        ebItr = errorBars.iterator();
      }
      while (xItr.hasNext()) {

        BigDecimal x = null;
        if (plot.chart.axisPair.xAxis.axisType == AxisType.NUMBER) {
          x = new BigDecimal(((Number) xItr.next()).doubleValue());
        }
        if (plot.chart.axisPair.xAxis.axisType == AxisType.DATE) {
          x = new BigDecimal(((Date) xItr.next()).getTime());
          // System.out.println(x);
        }

        BigDecimal y = new BigDecimal(yItr.next().doubleValue());
        // System.out.println(y);
        double eb = 0.0;
        if (errorBars != null) {
          eb = ebItr.next().doubleValue();
        }

        // int xTransform = (int) (xLeftMargin + ((x - xMin) / (xMax - xMin) * xTickSpace));
        int xTransform = (int) (xLeftMargin + (x.subtract(xMin).doubleValue() / xMax.subtract(xMin).doubleValue() * xTickSpace));
        // int yTransform = (int) (bounds.getHeight() - (yTopMargin + (y - yMin) / (yMax - yMin) * yTickSpace));
        int yTransform = (int) (bounds.getHeight() - (yTopMargin + y.subtract(yMin).doubleValue() / yMax.subtract(yMin).doubleValue() * yTickSpace));

        // a check if all y data are the exact same values
        if (Math.abs(xMax.subtract(xMin).doubleValue()) / 5 == 0.0) {
          xTransform = (int) (bounds.getWidth() / 2.0);
        }

        // a check if all y data are the exact same values
        if (Math.abs(yMax.subtract(yMin).doubleValue()) / 5 == 0.0) {
          yTransform = (int) (bounds.getHeight() / 2.0);
        }

        int xOffset = (int) (bounds.getX() + xTransform - 1);
        int yOffset = (int) (bounds.getY() + yTransform);
        // System.out.println(yOffset);
        // System.out.println(yTransform);

        // paint line
        if (series.getLineStyle() != null) {
          if (previousX != Integer.MIN_VALUE && previousY != Integer.MIN_VALUE) {
            g.setColor(series.getLineColor());
            g.setStroke(series.getLineStyle());
            g.drawLine(previousX, previousY, xOffset, yOffset);
          }
          previousX = xOffset;
          previousY = yOffset;
        }

        // paint marker
        if (series.getMarker() != null) {
          g.setColor(series.getMarkerColor());
          series.getMarker().paint(g, xOffset, yOffset);
        }

        // paint errorbar
        if (errorBars != null) {
          g.setColor(plot.chart.bordersColor);
          g.setStroke(SeriesLineStyle.getBasicStroke(SeriesLineStyle.SOLID));
          int bottom = (int) (-1 * bounds.getHeight() * eb / (yMax.subtract(yMin).doubleValue()));
          int top = (int) (bounds.getHeight() * eb / (yMax.subtract(yMin).doubleValue()));
          g.drawLine(xOffset, yOffset + bottom, xOffset, yOffset + top);
          g.drawLine(xOffset - 3, yOffset + bottom, xOffset + 3, yOffset + bottom);
          g.drawLine(xOffset - 3, yOffset + top, xOffset + 3, yOffset + top);
        }
      }

    }

  }

}
