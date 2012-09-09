/**
 * Copyright 2012 Xeiam LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xeiam.xchart.swing;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import com.xeiam.xchart.Chart;

/**
 * @author timmolter
 * @create Sep 9, 2012
 */
public class XChartJPanel extends JPanel {

  private Chart chart;

  public XChartJPanel(Chart chart) {

    this.chart = chart;
  }

  @Override
  protected void paintComponent(Graphics g) {

    super.paintComponent(g);

    super.removeAll();
    System.out.println(getSize().toString());

    chart.paint((Graphics2D) g, getSize().width, getSize().height);
  }

  @Override
  public Dimension getPreferredSize() {

    return new Dimension(chart.getWidth(), chart.getHeight());
  }
}
