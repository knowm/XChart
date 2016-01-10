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
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;
import java.util.Map;

import org.knowm.xchart.Series_Pie;
import org.knowm.xchart.StyleManagerPie;
import org.knowm.xchart.internal.Series;
import org.knowm.xchart.internal.style.StyleManager;

/**
 * @author timmolter
 */
public class PlotContent_Pie<SM extends StyleManager, S extends Series> extends PlotContent_ {

  StyleManagerPie styleManagerPie;

  /**
   * Constructor
   *
   * @param plot
   */
  protected PlotContent_Pie(Chart<StyleManagerPie, Series_Pie> chart) {

    super(chart);
    styleManagerPie = chart.getStyleManager();
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
    double percentage = styleManagerPie.getPieFillPercentage();

    // if (styleManagerPie.isCircular()) {
    //
    // double pieDiameter = Math.min(bounds.getWidth(), bounds.getHeight());
    // }

    double halfBorderPercentage = (1 - percentage) / 2.0;
    double width = styleManagerPie.isCircular() ? Math.min(bounds.getWidth(), bounds.getHeight()) : bounds.getWidth();
    double height = styleManagerPie.isCircular() ? Math.min(bounds.getWidth(), bounds.getHeight()) : bounds.getHeight();

    Rectangle2D pieBounds = new Rectangle2D.Double(

        bounds.getX() + bounds.getWidth() / 2 - width / 2 + halfBorderPercentage * width,

        bounds.getY() + bounds.getHeight() / 2 - height / 2 + halfBorderPercentage * height,

        width * percentage,

        height * percentage);

    // g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
    // g.setColor(Color.black);
    // g.draw(pieBounds);

    // get total
    double total = 0.0;

    // TODO 3.0.0 figure out this warning.
    Map<String, Series_Pie> map = chart.getSeriesMap();
    for (Series_Pie series : map.values()) {

      total += series.getValue().doubleValue();
    }

    // draw pie slices
    double curValue = 0.0;
    double startAngle = 0;
    // TODO 3.0.0 figure out this warning.
    map = chart.getSeriesMap();
    for (Series_Pie series : map.values()) {

      Number y = series.getValue();

      startAngle = (curValue * 360 / total);
      double arcAngle = (y.doubleValue() * 360 / total);
      g.setColor(series.getFillColor());
      g.fill(new Arc2D.Double(pieBounds.getX(), pieBounds.getY(), pieBounds.getWidth(), pieBounds.getHeight(), startAngle, arcAngle, Arc2D.PIE));
      g.setColor(styleManagerPie.getPlotBackgroundColor());
      g.draw(new Arc2D.Double(pieBounds.getX(), pieBounds.getY(), pieBounds.getWidth(), pieBounds.getHeight(), startAngle, arcAngle, Arc2D.PIE));
      curValue += y.doubleValue();
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
