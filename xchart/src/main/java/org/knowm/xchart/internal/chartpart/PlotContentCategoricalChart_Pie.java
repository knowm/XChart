/**
 * Copyright 2015 Knowm Inc. (http://knowm.org) and contributors.
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

import org.knowm.xchart.Series;

/**
 * @author timmolter
 */
public class PlotContentCategoricalChart_Pie extends PlotContent {

  /**
   * Constructor
   *
   * @param plot
   */
  protected PlotContentCategoricalChart_Pie(Plot plot) {

    super(plot);
  }

  @Override
  public void paint(Graphics2D g) {

    // plot area bounds
    Rectangle2D bounds = plot.getBounds();
    // g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
    // g.setColor(Color.red);
    // g.draw(bounds);

    // if the area to draw a chart on is so small, don't even bother
    if (bounds.getWidth() < 30) {
      return;
    }

    // clip bounds TODO Do we need this?
    Rectangle2D rectangle = new Rectangle2D.Double(0, 0, getChartInternal().getWidth(), getChartInternal().getHeight());
    // g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
    // g.setColor(Color.green);
    // g.draw(rectangle);
    g.setClip(bounds.createIntersection(rectangle));

    // pie bounds
    double percentage = .70;
    double halfBorderPercentage = (1 - percentage) / 2.0;
    Rectangle2D pieBounds = new Rectangle2D.Double(bounds.getX() + bounds.getWidth() * halfBorderPercentage, bounds.getY() + bounds.getHeight() * halfBorderPercentage, bounds.getWidth() * percentage,
        bounds.getHeight() * percentage);
        // g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
        // g.setColor(Color.black);
        // g.draw(pieBounds);

    // get total
    double total = 0.0;
    for (Series series : getChartInternal().getSeriesMap().values()) {

      String x = (String) series.getXData().iterator().next();
      Number y = series.getYData().iterator().next();

      total += y.doubleValue();
    }

    // draw pie slices
    double curValue = 0.0;
    double startAngle = 0;
    for (Series series : getChartInternal().getSeriesMap().values()) {

      // String x = (String) series.getXData().iterator().next();
      Number y = series.getYData().iterator().next();

      startAngle = (curValue * 360 / total);
      double arcAngle = (y.doubleValue() * 360 / total);
      g.setColor(series.getFillColor());
      g.fill(new Arc2D.Double(pieBounds.getX(), pieBounds.getY(), pieBounds.getWidth(), pieBounds.getHeight(), startAngle, arcAngle, Arc2D.PIE));
      g.setColor(getChartInternal().getStyleManager().getPlotBackgroundColor());
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
