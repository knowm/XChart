/**
 * Copyright 2015-2016 Knowm Inc. (http://knowm.org) and contributors.
 * Copyright 2011-2015 Xeiam LLC (http://xeiam.com) and contributors.
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
package org.knowm.xchart.internal.chartpart;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;

import org.knowm.xchart.internal.Series;
import org.knowm.xchart.internal.Series_AxesChart;
import org.knowm.xchart.internal.chartpart.Axis.Direction;
import org.knowm.xchart.style.AxesChartStyler;

/**
 * Axis tick labels
 */
public class AxisTickLabels<ST extends AxesChartStyler, S extends Series> implements ChartPart {

  private final Chart<AxesChartStyler, Series_AxesChart> chart;
  private Rectangle2D bounds;
  private final Direction direction;

  /**
   * Constructor
   *
   * @param chart
   * @param direction
   */
  protected AxisTickLabels(Chart<AxesChartStyler, Series_AxesChart> chart, Direction direction) {

    this.chart = chart;
    this.direction = direction;
  }

  @Override
  public void paint(Graphics2D g) {

    g.setFont(chart.getStyler().getAxisTickLabelsFont());

    g.setColor(chart.getStyler().getAxisTickLabelsColor());

    if (direction == Axis.Direction.Y && chart.getStyler().isYAxisTicksVisible()) { // Y-Axis

      double xWidth = chart.getYAxis().getAxisTitle().getBounds().getWidth();
      double xOffset = chart.getYAxis().getAxisTitle().getBounds().getX() + xWidth;
      double yOffset = chart.getYAxis().getBounds().getY();
      double height = chart.getYAxis().getBounds().getHeight();
      double maxTickLabelWidth = 0;
      Map<Double, TextLayout> axisLabelTextLayouts = new HashMap<Double, TextLayout>();

      for (int i = 0; i < chart.getYAxis().getAxisTickCalculator().getTickLabels().size(); i++) {

        String tickLabel = chart.getYAxis().getAxisTickCalculator().getTickLabels().get(i);
        // System.out.println("** " + tickLabel);
        double tickLocation = chart.getYAxis().getAxisTickCalculator().getTickLocations().get(i);
        double flippedTickLocation = yOffset + height - tickLocation;

        if (tickLabel != null && flippedTickLocation > yOffset && flippedTickLocation < yOffset + height) { // some are null for logarithmic axes
          FontRenderContext frc = g.getFontRenderContext();
          TextLayout axisLabelTextLayout = new TextLayout(tickLabel, chart.getStyler().getAxisTickLabelsFont(), frc);
          Rectangle2D tickLabelBounds = axisLabelTextLayout.getBounds();
          double boundWidth = tickLabelBounds.getWidth();
          if (boundWidth > maxTickLabelWidth) {
            maxTickLabelWidth = boundWidth;
          }
          axisLabelTextLayouts.put(tickLocation, axisLabelTextLayout);
        }
      }

      for (Double tickLocation : axisLabelTextLayouts.keySet()) {

        TextLayout axisLabelTextLayout = axisLabelTextLayouts.get(tickLocation);
        Shape shape = axisLabelTextLayout.getOutline(null);
        Rectangle2D tickLabelBounds = shape.getBounds();

        double flippedTickLocation = yOffset + height - tickLocation;

        AffineTransform orig = g.getTransform();
        AffineTransform at = new AffineTransform();
        double boundWidth = tickLabelBounds.getWidth();
        double xPos;
        switch (chart.getStyler().getYAxisLabelAlignment()) {
        case Right:
          xPos = xOffset + maxTickLabelWidth - boundWidth;
          break;
        case Centre:
          xPos = xOffset + (maxTickLabelWidth - boundWidth) / 2;
          break;
        case Left:
        default:
          xPos = xOffset;
        }
        at.translate(xPos, flippedTickLocation + tickLabelBounds.getHeight() / 2.0);
        g.transform(at);
        g.fill(shape);
        g.setTransform(orig);

      }

      // bounds
      bounds = new Rectangle2D.Double(xOffset, yOffset, maxTickLabelWidth, height);
      // g.setColor(Color.blue);
      // g.draw(bounds);

    }
    // X-Axis
    else if (direction == Axis.Direction.X && chart.getStyler().isXAxisTicksVisible()) {

      double xOffset = chart.getXAxis().getBounds().getX();
      double yOffset = chart.getXAxis().getAxisTitle().getBounds().getY();
      double width = chart.getXAxis().getBounds().getWidth();
      double maxTickLabelHeight = 0;

      // System.out.println("axisTick.getTickLabels().size(): " + axisTick.getTickLabels().size());
      for (int i = 0; i < chart.getXAxis().getAxisTickCalculator().getTickLabels().size(); i++) {

        String tickLabel = chart.getXAxis().getAxisTickCalculator().getTickLabels().get(i);
        // System.out.println("tickLabel: " + tickLabel);
        double tickLocation = chart.getXAxis().getAxisTickCalculator().getTickLocations().get(i);
        double shiftedTickLocation = xOffset + tickLocation;

        // discard null and out of bounds labels
        if (tickLabel != null && shiftedTickLocation > xOffset && shiftedTickLocation < xOffset + width) { // some are null for logarithmic axes

          FontRenderContext frc = g.getFontRenderContext();
          TextLayout textLayout = new TextLayout(tickLabel, chart.getStyler().getAxisTickLabelsFont(), frc);
          // System.out.println(textLayout.getOutline(null).getBounds().toString());

          // Shape shape = v.getOutline();
          AffineTransform rot = AffineTransform.getRotateInstance(-1 * Math.toRadians(chart.getStyler().getXAxisLabelRotation()), 0, 0);
          Shape shape = textLayout.getOutline(rot);
          Rectangle2D tickLabelBounds = shape.getBounds2D();

          AffineTransform orig = g.getTransform();
          AffineTransform at = new AffineTransform();
          double xPos;
          switch (chart.getStyler().getXAxisLabelAlignment()) {
          case Left:
            xPos = shiftedTickLocation;
            break;
          case Right:
            xPos = shiftedTickLocation - tickLabelBounds.getWidth();
            break;
          case Centre:
          default:
            xPos = shiftedTickLocation - tickLabelBounds.getWidth() / 2.0;
          }
          // System.out.println("tickLabelBounds: " + tickLabelBounds.toString());
          double shiftX = -1 * tickLabelBounds.getX() * Math.sin(Math.toRadians(chart.getStyler().getXAxisLabelRotation()));
          double shiftY = -1 * (tickLabelBounds.getY() + tickLabelBounds.getHeight());
          // System.out.println(shiftX);
          // System.out.println("shiftY: " + shiftY);
          at.translate(xPos + shiftX, yOffset + shiftY);

          g.transform(at);
          g.fill(shape);
          g.setTransform(orig);

          // // debug box
          // g.setColor(Color.MAGENTA);
          // g.draw(new Rectangle2D.Double(xPos, yOffset - tickLabelBounds.getHeight(), tickLabelBounds.getWidth(), tickLabelBounds.getHeight()));
          // g.setColor(getChartPainter().getstyler().getAxisTickLabelsColor());

          if (tickLabelBounds.getHeight() > maxTickLabelHeight) {
            maxTickLabelHeight = tickLabelBounds.getHeight();
          }
        }
        // else {
        // System.out.println("discarding: " + tickLabel);
        // }
      }

      // bounds
      bounds = new Rectangle2D.Double(xOffset, yOffset - maxTickLabelHeight, width, maxTickLabelHeight);
      // g.setColor(Color.blue);
      // g.draw(bounds);

    }
    else {
      bounds = new Rectangle2D.Double();
    }

  }

  @Override
  public Rectangle2D getBounds() {

    return bounds;
  }
}
