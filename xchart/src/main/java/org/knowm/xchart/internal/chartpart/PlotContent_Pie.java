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
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.util.Map;

import org.knowm.xchart.Series_Pie;
import org.knowm.xchart.Styler_Pie;
import org.knowm.xchart.internal.Series;
import org.knowm.xchart.internal.style.Styler;

/**
 * @author timmolter
 */
public class PlotContent_Pie<ST extends Styler, S extends Series> extends PlotContent_ {

  Styler_Pie stylerPie;
  DecimalFormat df = new DecimalFormat("#.0");

  /**
   * Constructor
   *
   * @param plot
   */
  protected PlotContent_Pie(Chart<Styler_Pie, Series_Pie> chart) {

    super(chart);
    stylerPie = chart.getStyler();
  }

  @Override
  public void paint(Graphics2D g) {

    // plot area bounds
    Rectangle2D bounds = getBounds();
    // g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
    // g.setColor(Color.red);
    // g.draw(bounds);

    // if the area to draw a chart on is so small, don't even bother
    if (bounds.getWidth() < 30) {
      return;
    }

    // clip bounds TODO Do we need this? Can we move it to the parent class for all subclasses?
    Rectangle2D rectangle = new Rectangle2D.Double(0, 0, chart.getWidth(), chart.getHeight());
    // g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
    // g.setColor(Color.green);
    // g.draw(rectangle);
    g.setClip(bounds.createIntersection(rectangle));

    // pie bounds
    double pieFillPercentage = stylerPie.getPieFillPercentage();

    // if (stylerPie.isCircular()) {
    //
    // double pieDiameter = Math.min(bounds.getWidth(), bounds.getHeight());
    // }

    double halfBorderPercentage = (1 - pieFillPercentage) / 2.0;
    double width = stylerPie.isCircular() ? Math.min(bounds.getWidth(), bounds.getHeight()) : bounds.getWidth();
    double height = stylerPie.isCircular() ? Math.min(bounds.getWidth(), bounds.getHeight()) : bounds.getHeight();

    Rectangle2D pieBounds = new Rectangle2D.Double(

        bounds.getX() + bounds.getWidth() / 2 - width / 2 + halfBorderPercentage * width,

        bounds.getY() + bounds.getHeight() / 2 - height / 2 + halfBorderPercentage * height,

        width * pieFillPercentage,

        height * pieFillPercentage);

    // g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
    // g.setColor(Color.black);
    // g.draw(pieBounds);

    // get total
    double total = 0.0;

    Map<String, Series_Pie> map = chart.getSeriesMap();
    for (Series_Pie series : map.values()) {

      total += series.getValue().doubleValue();
    }

    // draw pie slices
    // double curValue = 0.0;
    // double curValue = 0.0;
    double startAngle = stylerPie.getStartAngleInDegrees() + 90;

    map = chart.getSeriesMap();
    for (Series_Pie series : map.values()) {

      Number y = series.getValue();

      // draw slice
      double arcAngle = (y.doubleValue() * 360 / total);
      g.setColor(series.getFillColor());
      g.fill(new Arc2D.Double(pieBounds.getX(), pieBounds.getY(), pieBounds.getWidth(), pieBounds.getHeight(), startAngle, arcAngle, Arc2D.PIE));
      g.setColor(stylerPie.getPlotBackgroundColor());
      g.draw(new Arc2D.Double(pieBounds.getX(), pieBounds.getY(), pieBounds.getWidth(), pieBounds.getHeight(), startAngle, arcAngle, Arc2D.PIE));
      // curValue += y.doubleValue();

      // draw percentage on slice
      double percentage = y.doubleValue() / total * 100;

      TextLayout textLayout = new TextLayout(df.format(percentage) + "%", stylerPie.getPieFont(), new FontRenderContext(null, true, false));
      Rectangle2D percentageRectangle = textLayout.getBounds();

      double xCenter = pieBounds.getX() + pieBounds.getWidth() / 2 - percentageRectangle.getWidth() / 2;
      double yCenter = pieBounds.getY() + pieBounds.getHeight() / 2 + percentageRectangle.getHeight() / 2;
      double angle = (arcAngle + startAngle) - arcAngle / 2;
      double xOffset = xCenter + Math.cos(Math.toRadians(angle)) * (pieBounds.getWidth() / 2 * stylerPie.getAnnotationDistance());
      double yOffset = yCenter - Math.sin(Math.toRadians(angle)) * (pieBounds.getHeight() / 2 * stylerPie.getAnnotationDistance());

      g.setColor(stylerPie.getChartFontColor());
      g.setFont(stylerPie.getChartTitleFont());

      Shape shape = textLayout.getOutline(null);
      AffineTransform orig = g.getTransform();
      AffineTransform at = new AffineTransform();
      at.translate(xOffset, yOffset);
      g.transform(at);
      g.fill(shape);
      g.setTransform(orig);

      // // Tick Mark
      // xCenter = pieBounds.getX() + pieBounds.getWidth() / 2;
      // yCenter = pieBounds.getY() + pieBounds.getHeight() / 2;
      // double xOffsetStart = xCenter + Math.cos(Math.toRadians(angle)) * (pieBounds.getWidth() / 2.01);
      // double xOffsetEnd = xCenter + Math.cos(Math.toRadians(angle)) * (pieBounds.getWidth() / 1.9);
      // double yOffsetStart = yCenter - Math.sin(Math.toRadians(angle)) * (pieBounds.getHeight() / 2.01);
      // double yOffsetEnd = yCenter - Math.sin(Math.toRadians(angle)) * (pieBounds.getHeight() / 1.9);
      //
      // g.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
      // Shape line = new Line2D.Double(xOffsetStart, yOffsetStart, xOffsetEnd, yOffsetEnd);
      // g.draw(line);

      startAngle += arcAngle;
    }

    g.setClip(null);

  }

  // private double[] getPercentageVector(Collection<? extends Number> collection) {
  //
  // float total = 0.0f;
  //
  // double[] vectorCenters = new double[collection.size()];
  // Iterator<? extends Number> yItr = collection.iterator();
  //
  // int counter = 0;
  // while (yItr.hasNext()) {
  //
  // Number next = yItr.next();
  //
  // double y = next.doubleValue();
  // System.out.println(y);
  // vectorCenters[counter] = vectorCenters[counter] + y;
  //
  // total += vectorCenters[counter++];
  // }
  //
  // double[] vectorPercentages = new double[vectorCenters.length];
  //
  // for (int i = 0; i < vectorPercentages.length; i++) {
  // vectorPercentages[i] = vectorCenters[i] / total;
  // }
  //
  // return vectorPercentages;
  // }

}
