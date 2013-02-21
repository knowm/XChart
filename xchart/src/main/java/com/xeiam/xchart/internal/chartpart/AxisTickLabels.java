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
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;

import com.xeiam.xchart.Chart;

/**
 * Axis tick labels
 */
public class AxisTickLabels implements ChartPart {

  /** parent */
  private final AxisTick axisTick;

  /** the bounds */
  private Rectangle bounds;

  /**
   * Constructor
   * 
   * @param axisTick
   */
  protected AxisTickLabels(AxisTick axisTick) {

    this.axisTick = axisTick;
  }

  @Override
  public Rectangle getBounds() {

    return bounds;
  }

  @Override
  public void paint(Graphics2D g) {

    bounds = new Rectangle();
    g.setFont(getChart().getStyleManager().getAxisTickLabelsFont());

    g.setColor(getChart().getStyleManager().getAxisTickLabelsColor());

    if (axisTick.getAxis().getDirection() == Axis.Direction.Y) { // Y-Axis

      int xOffset = (int) (axisTick.getAxis().getAxisTitle().getBounds().getX() + axisTick.getAxis().getAxisTitle().getBounds().getWidth());
      int yOffset = (int) (axisTick.getAxis().getPaintZone().getY());
      int maxTickLabelWidth = 0;
      for (int i = 0; i < axisTick.getTickLabels().size(); i++) {

        String tickLabel = axisTick.getTickLabels().get(i);
        // System.out.println(tickLabel);
        int tickLocation = axisTick.getTickLocations().get(i);

        if (tickLabel != null) { // some are null for logarithmic axes
          FontRenderContext frc = g.getFontRenderContext();
          // TextLayout layout = new TextLayout(tickLabel, font, new FontRenderContext(null, true, false));
          TextLayout layout = new TextLayout(tickLabel, getChart().getStyleManager().getAxisTickLabelsFont(), frc);
          Rectangle tickLabelBounds = layout.getPixelBounds(null, 0, 0);
          layout.draw(g, xOffset, (int) (yOffset + axisTick.getAxis().getPaintZone().getHeight() - tickLocation + tickLabelBounds.getHeight() / 2.0));

          if (tickLabelBounds.getWidth() > maxTickLabelWidth) {
            maxTickLabelWidth = (int) tickLabelBounds.getWidth();
          }
        }
      }

      // bounds
      bounds = new Rectangle(xOffset, yOffset, maxTickLabelWidth, (int) axisTick.getAxis().getPaintZone().getHeight());
      // g.setColor(Color.blue);
      // g.draw(bounds);

    } else { // X-Axis

      int xOffset = (int) (axisTick.getAxis().getPaintZone().getX());
      int yOffset = (int) (axisTick.getAxis().getAxisTitle().getBounds().getY());
      int maxTickLabelHeight = 0;
      for (int i = 0; i < axisTick.getTickLabels().size(); i++) {

        String tickLabel = axisTick.getTickLabels().get(i);
        int tickLocation = axisTick.getTickLocations().get(i);

        if (tickLabel != null) { // some are null for logarithmic axes
          FontRenderContext frc = g.getFontRenderContext();
          TextLayout layout = new TextLayout(tickLabel, getChart().getStyleManager().getAxisTickLabelsFont(), frc);
          Rectangle tickLabelBounds = layout.getPixelBounds(null, 0, 0);
          layout.draw(g, (int) (xOffset + tickLocation - tickLabelBounds.getWidth() / 2.0), yOffset);

          if (tickLabelBounds.getHeight() > maxTickLabelHeight) {
            maxTickLabelHeight = (int) tickLabelBounds.getHeight();
          }
        }
      }

      // bounds
      bounds = new Rectangle(xOffset, yOffset - maxTickLabelHeight, (int) axisTick.getAxis().getPaintZone().getWidth(), maxTickLabelHeight);
      // g.setColor(Color.blue);
      // g.draw(bounds);

    }

  }

  @Override
  public Chart getChart() {

    return axisTick.getChart();
  }
}
