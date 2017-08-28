/**
 * Copyright 2015-2017 Knowm Inc. (http://knowm.org) and contributors.
 * Copyright 2011-2015 Xeiam LLC (http://xeiam.com) and contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.knowm.xchart.internal.chartpart;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.Map;

import org.knowm.xchart.BubbleSeries;
import org.knowm.xchart.internal.Utils;
import org.knowm.xchart.style.BubbleStyler;

/**
 * @author timmolter
 */
public class PlotContent_Bubble<ST extends BubbleStyler, S extends BubbleSeries> extends PlotContent_<ST, S> {

  private final ST stylerBubble;

  /**
   * Constructor
   *
   * @param chart
   */
  PlotContent_Bubble(Chart<ST, S> chart) {

    super(chart);
    stylerBubble = chart.getStyler();
  }

  @Override
  public void doPaint(Graphics2D g) {

    // X-Axis
    double xTickSpace = stylerBubble.getPlotContentSize() * getBounds().getWidth();
    double xLeftMargin = Utils.getTickStartOffset((int) getBounds().getWidth(), xTickSpace);

    // Y-Axis
    double yTickSpace = stylerBubble.getPlotContentSize() * getBounds().getHeight();
    double yTopMargin = Utils.getTickStartOffset((int) getBounds().getHeight(), yTickSpace);

    double xMin = chart.getXAxis().getMin();
    double xMax = chart.getXAxis().getMax();

    // logarithmic
    if (stylerBubble.isXAxisLogarithmic()) {
      xMin = Math.log10(xMin);
      xMax = Math.log10(xMax);
    }

    Map<String, S> map = chart.getSeriesMap();
    for (S series : map.values()) {

      if (!series.isEnabled()) {
        continue;
      }

      double yMin = chart.getYAxis(series.getYAxisGroup()).getMin();
      double yMax = chart.getYAxis(series.getYAxisGroup()).getMax();
      if (stylerBubble.isYAxisLogarithmic()) {
        yMin = Math.log10(yMin);
        yMax = Math.log10(yMax);
      }

      // data points

      for (int i = 0; i <series.getXData().length ; i++) {


        double x = series.getXData()[i];
        // System.out.println(x);
        if (stylerBubble.isXAxisLogarithmic()) {
          x = Math.log10(x);
        }
        // System.out.println(x);

        if (series.getYData()[i] == Double.NaN) {

//          previousX = -Double.MAX_VALUE;
//          previousY = -Double.MAX_VALUE;
          continue;
        }

        double yOrig = series.getYData()[i];

        double y;

        // System.out.println(y);
        if (stylerBubble.isYAxisLogarithmic()) {
          y = Math.log10(yOrig);
        } else {
          y = yOrig;
        }
        // System.out.println(y);

        double xTransform = xLeftMargin + ((x - xMin) / (xMax - xMin) * xTickSpace);
        double yTransform = getBounds().getHeight() - (yTopMargin + (y - yMin) / (yMax - yMin) * yTickSpace);

        // a check if all x data are the exact same values
        if (Math.abs(xMax - xMin) / 5 == 0.0) {
          xTransform = getBounds().getWidth() / 2.0;
        }

        // a check if all y data are the exact same values
        if (Math.abs(yMax - yMin) / 5 == 0.0) {
          yTransform = getBounds().getHeight() / 2.0;
        }

        double xOffset = getBounds().getX() + xTransform;
        double yOffset = getBounds().getY() + yTransform;
        // System.out.println(xTransform);
        // System.out.println(xOffset);
        // System.out.println(yTransform);
        // System.out.println(yOffset);
        // System.out.println("---");

//        previousX = xOffset;
//        previousY = yOffset;

        // paint bubbles
        if (series.getExtraValues() != null) {

          double bubbleSize = series.getExtraValues()[i];

          // Draw it
          Shape bubble;
          // if (BubbleSeriesRenderStyle.Round == series.getBubbleSeriesRenderStyle()) {
          bubble = new Ellipse2D.Double(xOffset - bubbleSize / 2, yOffset - bubbleSize / 2, bubbleSize, bubbleSize);
          // }
          // else {
          // bubble = new Ellipse2D.Double(xOffset, yOffset, xOffset + 10, yOffset + 10);
          // }
          // set bubble color
          g.setColor(series.getFillColor());
          g.fill(bubble);
          // set bubble color
          g.setColor(series.getLineColor());
          g.setStroke(series.getLineStyle());
          g.draw(bubble);
          // add data labels
          chart.toolTips.addData(bubble, xOffset, yOffset, 0, chart.getXAxisFormat().format(x), chart.getYAxisFormat().format(yOrig));
        }
      }
    }
  }
}
