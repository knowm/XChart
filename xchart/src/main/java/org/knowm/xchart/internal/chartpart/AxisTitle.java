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

import org.knowm.xchart.internal.Series;
import org.knowm.xchart.internal.Series_AxesChart;
import org.knowm.xchart.internal.chartpart.Axis.Direction;
import org.knowm.xchart.style.AxesChartStyler;

/**
 * AxisTitle
 */
public class AxisTitle<ST extends AxesChartStyler, S extends Series> implements ChartPart {

  private final Chart<AxesChartStyler, Series_AxesChart> chart;
  private Rectangle2D bounds;
  private final Direction direction;

  /**
   * Constructor
   *
   * @param chart the Chart
   * @param direction the Direction
   */
  protected AxisTitle(Chart<AxesChartStyler, Series_AxesChart> chart, Direction direction) {

    this.chart = chart;
    this.direction = direction;
  }

  @Override
  public void paint(Graphics2D g) {

    bounds = new Rectangle2D.Double();

    g.setColor(chart.getStyler().getChartFontColor());
    g.setFont(chart.getStyler().getAxisTitleFont());

    if (direction == Axis.Direction.Y) {

      if (chart.getyYAxisTitle() != null && !chart.getyYAxisTitle().trim().equalsIgnoreCase("") && chart.getStyler().isYAxisTitleVisible()) {

        FontRenderContext frc = g.getFontRenderContext();
        TextLayout nonRotatedTextLayout = new TextLayout(chart.getyYAxisTitle(), chart.getStyler().getAxisTitleFont(), frc);
        Rectangle2D nonRotatedRectangle = nonRotatedTextLayout.getBounds();

        // ///////////////////////////////////////////////

        int xOffset = (int) (chart.getYAxis().getBounds().getX() + nonRotatedRectangle.getHeight());
        int yOffset = (int) ((chart.getYAxis().getBounds().getHeight() + nonRotatedRectangle.getWidth()) / 2.0 + chart.getYAxis().getBounds().getY());

        AffineTransform rot = AffineTransform.getRotateInstance(-1 * Math.PI / 2, 0, 0);
        Shape shape = nonRotatedTextLayout.getOutline(rot);

        AffineTransform orig = g.getTransform();
        AffineTransform at = new AffineTransform();

        at.translate(xOffset, yOffset);
        g.transform(at);
        g.fill(shape);
        g.setTransform(orig);

        // ///////////////////////////////////////////////
        // System.out.println(nonRotatedRectangle.getHeight());

        // bounds
        bounds = new Rectangle2D.Double(xOffset - nonRotatedRectangle.getHeight(), yOffset - nonRotatedRectangle.getWidth(), nonRotatedRectangle.getHeight() + chart.getStyler().getAxisTitlePadding(),
            nonRotatedRectangle.getWidth());
        // g.setColor(Color.blue);
        // g.draw(bounds);
      }
      else {
        bounds = new Rectangle2D.Double(chart.getYAxis().getBounds().getX(), chart.getYAxis().getBounds().getY(), 0, chart.getYAxis().getBounds().getHeight());
      }

    }
    else {

      if (chart.getXAxisTitle() != null && !chart.getXAxisTitle().trim().equalsIgnoreCase("") && chart.getStyler().isXAxisTitleVisible()) {

        FontRenderContext frc = g.getFontRenderContext();
        TextLayout textLayout = new TextLayout(chart.getXAxisTitle(), chart.getStyler().getAxisTitleFont(), frc);
        Rectangle2D rectangle = textLayout.getBounds();
        // System.out.println(rectangle);

        double xOffset = chart.getXAxis().getBounds().getX() + (chart.getXAxis().getBounds().getWidth() - rectangle.getWidth()) / 2.0;
        double yOffset = chart.getXAxis().getBounds().getY() + chart.getXAxis().getBounds().getHeight() - rectangle.getHeight();

        // textLayout.draw(g, (float) xOffset, (float) (yOffset - rectangle.getY()));
        Shape shape = textLayout.getOutline(null);
        AffineTransform orig = g.getTransform();
        AffineTransform at = new AffineTransform();
        at.translate((float) xOffset, (float) (yOffset - rectangle.getY()));
        g.transform(at);
        g.fill(shape);
        g.setTransform(orig);

        bounds = new Rectangle2D.Double(xOffset, yOffset - chart.getStyler().getAxisTitlePadding(), rectangle.getWidth(), rectangle.getHeight() + chart.getStyler().getAxisTitlePadding());
        // g.setColor(Color.blue);
        // g.draw(bounds);

      }
      else {
        bounds = new Rectangle2D.Double(chart.getXAxis().getBounds().getX(), chart.getXAxis().getBounds().getY() + chart.getXAxis().getBounds().getHeight(), chart.getXAxis().getBounds().getWidth(),
            0);
        // g.setColor(Color.blue);
        // g.draw(bounds);

      }
    }
  }

  @Override
  public Rectangle2D getBounds() {

    return bounds;
  }
}
