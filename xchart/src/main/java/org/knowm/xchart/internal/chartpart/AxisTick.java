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

import org.knowm.xchart.graphics.Graphics;
import org.knowm.xchart.graphics.RenderContext;
import org.knowm.xchart.internal.Series;
import org.knowm.xchart.internal.Series_AxesChart;
import org.knowm.xchart.internal.chartpart.Axis.Direction;
import org.knowm.xchart.style.AxesChartStyler;

import java.awt.geom.Rectangle2D;

/**
 * An axis tick
 */
public class AxisTick<ST extends AxesChartStyler, S extends Series> implements ChartPart {

  private final Chart<AxesChartStyler, Series_AxesChart> chart;
  private Rectangle2D bounds;
  private final Direction direction;

  /** the axisticklabels */
  private AxisTickLabels<AxesChartStyler, Series_AxesChart> axisTickLabels;

  /** the axistickmarks */
  private AxisTickMarks<AxesChartStyler, Series_AxesChart> axisTickMarks;

  /**
   * Constructor
   *
   * @param chart
   * @param direction
   */
  protected AxisTick(Chart<AxesChartStyler, Series_AxesChart> chart, Direction direction) {

    this.chart = chart;
    this.direction = direction;
    axisTickLabels = new AxisTickLabels<AxesChartStyler, Series_AxesChart>(chart, direction);
    axisTickMarks = new AxisTickMarks<AxesChartStyler, Series_AxesChart>(chart, direction);
  }

  @Override
  public Rectangle2D getBounds(RenderContext rc) {

    return bounds;
  }

  @Override
  public void paint(Graphics g) {

    if (direction == Axis.Direction.Y && chart.getStyler().isYAxisTicksVisible()) {

      axisTickLabels.paint(g);
      axisTickMarks.paint(g);

      bounds = new Rectangle2D.Double(

          axisTickLabels.getBounds(g.getRenderContext()).getX(),

          axisTickLabels.getBounds(g.getRenderContext()).getY(),

          axisTickLabels.getBounds(g.getRenderContext()).getWidth() + chart.getStyler().getAxisTickPadding() + axisTickMarks.getBounds(g.getRenderContext()).getWidth(),

          axisTickMarks.getBounds(g.getRenderContext()).getHeight()

      );

      // g.setColor(Color.red);
      // g.draw(bounds);

    }
    else if (direction == Axis.Direction.X && chart.getStyler().isXAxisTicksVisible()) {

      axisTickLabels.paint(g);
      axisTickMarks.paint(g);

      bounds = new Rectangle2D.Double(

          axisTickMarks.getBounds(g.getRenderContext()).getX(),

          axisTickMarks.getBounds(g.getRenderContext()).getY(),

          axisTickLabels.getBounds(g.getRenderContext()).getWidth(),

          axisTickMarks.getBounds(g.getRenderContext()).getHeight() + chart.getStyler().getAxisTickPadding() + axisTickLabels.getBounds(g.getRenderContext()).getHeight()

      );

      // g.setColor(Color.red);
      // g.draw(bounds);

    }
    else {
      bounds = new Rectangle2D.Double();
    }

  }

  // Getters /////////////////////////////////////////////////

  protected AxisTickLabels<AxesChartStyler, Series_AxesChart> getAxisTickLabels() {

    return axisTickLabels;
  }

}
