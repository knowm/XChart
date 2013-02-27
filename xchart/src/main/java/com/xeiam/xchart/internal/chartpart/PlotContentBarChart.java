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

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.Series;

/**
 * @author timmolter
 */
public class PlotContentBarChart extends PlotContent {

  /**
   * Constructor
   * 
   * @param plot
   */
  protected PlotContentBarChart(Plot plot) {

    super(plot);
  }

  @Override
  public void paint(Graphics2D g) {

    Rectangle bounds = plot.getBounds();

    // X-Axis
    int xTickSpace = AxisPair.getTickSpace((int) bounds.getWidth());
    int xLeftMargin = AxisPair.getTickStartOffset((int) bounds.getWidth(), xTickSpace);

    // Y-Axis
    int yTickSpace = AxisPair.getTickSpace((int) bounds.getHeight());
    int yTopMargin = AxisPair.getTickStartOffset((int) bounds.getHeight(), yTickSpace);

    // get all categories
    Set<Object> categories = new TreeSet<Object>();
    Map<Integer, Series> seriesMap = getChart().getAxisPair().getSeriesMap();
    for (Integer seriesId : seriesMap.keySet()) {

      Series series = seriesMap.get(seriesId);
      Iterator<?> xItr = series.getxData().iterator();
      while (xItr.hasNext()) {
        categories.add(xItr.next());
      }
    }
    int numBars = categories.size();
    int gridStep = (int) (xTickSpace / (double) numBars);

    // plot series
    int seriesCounter = 0;
    for (Integer seriesId : seriesMap.keySet()) {

      Series series = seriesMap.get(seriesId);

      // data points
      Collection<?> xData = series.getxData();

      Collection<Number> yData = series.getyData();
      BigDecimal yMin = getChart().getAxisPair().getyAxis().getMin();
      BigDecimal yMax = getChart().getAxisPair().getyAxis().getMax();
      if (yMin.compareTo(BigDecimal.ZERO) > 0 && yMax.compareTo(BigDecimal.ZERO) > 0) {
        yMin = BigDecimal.ZERO;
      } else if (yMin.compareTo(BigDecimal.ZERO) < 0 && yMax.compareTo(BigDecimal.ZERO) < 0) {
        yMax = BigDecimal.ZERO;
      }
      // override min and maxValue if specified
      if (getChart().getStyleManager().getyAxisMin() != null) {
        yMin = new BigDecimal(getChart().getStyleManager().getyAxisMin());
      }
      if (getChart().getStyleManager().getyAxisMax() != null) {
        yMax = new BigDecimal(getChart().getStyleManager().getyAxisMax());
      }
      Iterator<?> categoryItr = categories.iterator();
      Iterator<Number> yItr = yData.iterator();

      int barCounter = 0;
      while (categoryItr.hasNext()) {

        if (xData.contains(categoryItr.next())) {

          BigDecimal y = new BigDecimal(yItr.next().doubleValue());
          int yTransform = (int) (bounds.getHeight() - (yTopMargin + y.subtract(yMin).doubleValue() / yMax.subtract(yMin).doubleValue() * yTickSpace));
          int yOffset = (int) (bounds.getY() + yTransform);

          int zeroTransform = (int) (bounds.getHeight() - (yTopMargin + (yMin.subtract(yMin).doubleValue()) / (yMax.subtract(yMin).doubleValue()) * yTickSpace));
          int zeroOffset = (int) (bounds.getY() + zeroTransform);

          // paint bar
          int barWidth = (int) (gridStep / seriesMap.size() / 1.1);
          int barMargin = (int) (gridStep * .05);
          int xOffset = (int) (bounds.getX() + xLeftMargin + gridStep * barCounter++ + seriesCounter * barWidth + barMargin);
          g.setColor(series.getStrokeColor());
          g.fillPolygon(new int[] { xOffset, xOffset + barWidth, xOffset + barWidth, xOffset }, new int[] { yOffset, yOffset, zeroOffset, zeroOffset }, 4);
        } else {
          barCounter++;
        }
      }
      seriesCounter++;
    }

  }

  @Override
  public Chart getChart() {

    return plot.getChart();
  }

}
