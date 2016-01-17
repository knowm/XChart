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
import org.knowm.xchart.internal.style.StyleManagerAxesChart;

/**
 * AxisTitle
 */
public class AxisTitle<SM extends StyleManagerAxesChart, S extends Series> implements ChartPart {

  private final Chart<StyleManagerAxesChart, Series_AxesChart> chart;
  private Rectangle2D bounds;
  private final Direction direction;

  /**
   * Constructor
   *
   * @param chart the Chart
   * @param direction the Direction
   */
  protected AxisTitle(Chart<StyleManagerAxesChart, Series_AxesChart> chart, Direction direction) {

    this.chart = chart;
    this.direction = direction;
  }

  @Override
  public void paint(Graphics2D g) {

    bounds = new Rectangle2D.Double();

    g.setColor(chart.getStyleManager().getChartFontColor());
    g.setFont(chart.getStyleManager().getAxisTitleFont());

    if (direction == Axis.Direction.Y) {

      if (chart.getyYAxisTitle() != null && !chart.getyYAxisTitle().trim().equalsIgnoreCase("") && chart.getStyleManager().isYAxisTitleVisible()) {

        FontRenderContext frc = g.getFontRenderContext();
        TextLayout nonRotatedTextLayout = new TextLayout(chart.getyYAxisTitle(), chart.getStyleManager().getAxisTitleFont(), frc);
        Rectangle2D nonRotatedRectangle = nonRotatedTextLayout.getBounds();

        // ///////////////////////////////////////////////

        int xOffset = (int) (chart.getYAxis().getPaintZone().getX() + nonRotatedRectangle.getHeight());
        int yOffset = (int) ((chart.getYAxis().getPaintZone().getHeight() + nonRotatedRectangle.getWidth()) / 2.0 + chart.getYAxis().getPaintZone().getY());

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
        bounds = new Rectangle2D.Double(xOffset - nonRotatedRectangle.getHeight(), yOffset - nonRotatedRectangle.getWidth(), nonRotatedRectangle.getHeight() + chart.getStyleManager()
            .getAxisTitlePadding(), nonRotatedRectangle.getWidth());
        // g.setColor(Color.blue);
        // g.draw(bounds);
      }
      else {
        bounds = new Rectangle2D.Double(chart.getYAxis().getPaintZone().getX(), chart.getYAxis().getPaintZone().getY(), 0, chart.getYAxis().getPaintZone().getHeight());
      }

    }
    else {

      if (chart.getXAxisTitle() != null && !chart.getXAxisTitle().trim().equalsIgnoreCase("") && chart.getStyleManager().isXAxisTitleVisible()) {

        FontRenderContext frc = g.getFontRenderContext();
        TextLayout textLayout = new TextLayout(chart.getXAxisTitle(), chart.getStyleManager().getAxisTitleFont(), frc);
        Rectangle2D rectangle = textLayout.getBounds();
        // System.out.println(rectangle);

        double xOffset = chart.getXAxis().getPaintZone().getX() + (chart.getXAxis().getPaintZone().getWidth() - rectangle.getWidth()) / 2.0;
        double yOffset = chart.getXAxis().getPaintZone().getY() + chart.getXAxis().getPaintZone().getHeight() - rectangle.getHeight();

        // textLayout.draw(g, (float) xOffset, (float) (yOffset - rectangle.getY()));
        Shape shape = textLayout.getOutline(null);
        AffineTransform orig = g.getTransform();
        AffineTransform at = new AffineTransform();
        at.translate((float) xOffset, (float) (yOffset - rectangle.getY()));
        g.transform(at);
        g.fill(shape);
        g.setTransform(orig);

        bounds = new Rectangle2D.Double(xOffset, yOffset - chart.getStyleManager().getAxisTitlePadding(), rectangle.getWidth(), rectangle.getHeight() + chart.getStyleManager().getAxisTitlePadding());
        // g.setColor(Color.blue);
        // g.draw(bounds);

      }
      else {
        bounds = new Rectangle2D.Double(chart.getXAxis().getPaintZone().getX(), chart.getXAxis().getPaintZone().getY() + chart.getXAxis().getPaintZone().getHeight(), chart.getXAxis().getPaintZone()
            .getWidth(), 0);
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
