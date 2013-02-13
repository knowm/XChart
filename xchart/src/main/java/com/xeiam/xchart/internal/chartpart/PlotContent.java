/**
 * Copyright 2011-2013 Xeiam LLC.
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
package com.xeiam.xchart.internal.chartpart;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import com.xeiam.xchart.AreaChart;
import com.xeiam.xchart.Chart;
import com.xeiam.xchart.ScatterChart;
import com.xeiam.xchart.internal.chartpart.Axis.AxisType;
import com.xeiam.xchart.style.Series;

/**
 * @author timmolter
 */
public class PlotContent implements ChartPart {

  /** parent */
  private Plot plot;

  private final Stroke errorBarStroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);

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

    boolean isScatterChart = getChart() instanceof ScatterChart;
    boolean isAreaChart = getChart() instanceof AreaChart;

    Rectangle bounds = plot.getBounds();

    Map<Integer, Series> seriesMap = getChart().getAxisPair().getSeriesMap();
    for (Integer seriesId : seriesMap.keySet()) {

      Series series = seriesMap.get(seriesId);

      // X-Axis
      int xTickSpace = AxisPair.getTickSpace((int) bounds.getWidth());
      int xLeftMargin = AxisPair.getTickStartOffset((int) bounds.getWidth(), xTickSpace);

      // Y-Axis
      int yTickSpace = AxisPair.getTickSpace((int) bounds.getHeight());
      int yTopMargin = AxisPair.getTickStartOffset((int) bounds.getHeight(), yTickSpace);

      // data points
      Collection<?> xData = series.getxData();
      BigDecimal xMin = getChart().getAxisPair().getxAxis().getMin();
      BigDecimal xMax = getChart().getAxisPair().getxAxis().getMax();
      Collection<Number> yData = series.getyData();
      BigDecimal yMin = getChart().getAxisPair().getyAxis().getMin();
      BigDecimal yMax = getChart().getAxisPair().getyAxis().getMax();
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
        if (getChart().getAxisPair().getxAxis().getAxisType() == AxisType.NUMBER) {
          x = new BigDecimal(((Number) xItr.next()).doubleValue());
        }
        if (getChart().getAxisPair().getxAxis().getAxisType() == AxisType.DATE) {
          x = new BigDecimal(((Date) xItr.next()).getTime());
          // System.out.println(x);
        }

        BigDecimal y = new BigDecimal(yItr.next().doubleValue());
        // System.out.println(y);
        double eb = 0.0;
        if (errorBars != null) {
          eb = ebItr.next().doubleValue();
        }

        int xTransform = (int) (xLeftMargin + (x.subtract(xMin).doubleValue() / xMax.subtract(xMin).doubleValue() * xTickSpace));
        int yBottomOfArea = (int) (bounds.getHeight() - (yTopMargin + y.subtract(yMin).doubleValue() / yMax.subtract(yMin).doubleValue() * yTickSpace));

        // a check if all x data are the exact same values
        if (Math.abs(xMax.subtract(xMin).doubleValue()) / 5 == 0.0) {
          xTransform = (int) (bounds.getWidth() / 2.0);
        }

        // a check if all y data are the exact same values
        if (Math.abs(yMax.subtract(yMin).doubleValue()) / 5 == 0.0) {
          yBottomOfArea = (int) (bounds.getHeight() / 2.0);
        }

        int xOffset = (int) (bounds.getX() + xTransform - 1);
        int yOffset = (int) (bounds.getY() + yBottomOfArea);
        // System.out.println(yOffset);
        // System.out.println(yTransform);

        // paint line
        if (series.getStroke() != null && !isScatterChart) {
          if (previousX != Integer.MIN_VALUE && previousY != Integer.MIN_VALUE) {
            g.setColor(series.getStrokeColor());
            g.setStroke(series.getStroke());
            g.drawLine(previousX, previousY, xOffset, yOffset);
          }

        }

        // paint area
        if (isAreaChart) {
          if (previousX != Integer.MIN_VALUE && previousY != Integer.MIN_VALUE) {
            g.setColor(series.getStrokeColor());
            yBottomOfArea = (int) (bounds.getY() + bounds.getHeight() - yTopMargin + 1);
            g.fillPolygon(new int[] { previousX, xOffset, xOffset, previousX }, new int[] { previousY, yOffset, yBottomOfArea, yBottomOfArea }, 4);
          }
        }

        previousX = xOffset;
        previousY = yOffset;

        // paint marker
        if (series.getMarker() != null) {
          g.setColor(series.getMarkerColor());
          series.getMarker().paint(g, xOffset, yOffset);
        }

        // paint errorbar
        if (errorBars != null) {
          g.setColor(getChart().getStyleManager().getErrorBarsColor());
          g.setStroke(errorBarStroke);
          int bottom = (int) (-1 * bounds.getHeight() * eb / (yMax.subtract(yMin).doubleValue()));
          int top = (int) (bounds.getHeight() * eb / (yMax.subtract(yMin).doubleValue()));
          g.drawLine(xOffset, yOffset + bottom, xOffset, yOffset + top);
          g.drawLine(xOffset - 3, yOffset + bottom, xOffset + 3, yOffset + bottom);
          g.drawLine(xOffset - 3, yOffset + top, xOffset + 3, yOffset + top);
        }
      }

    }

  }

  @Override
  public Chart getChart() {

    return plot.getChart();
  }

}
