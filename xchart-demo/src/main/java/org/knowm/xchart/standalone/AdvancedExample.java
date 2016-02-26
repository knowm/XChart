/**
 * Copyright 2013 Xeiam LLC.
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
package org.knowm.xchart.standalone;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.knowm.xchart.ChartBuilder_XY;
import org.knowm.xchart.Chart_XY;
import org.knowm.xchart.Series_XY.ChartXYSeriesRenderStyle;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.internal.style.Styler.LegendPosition;

/**
 * @author timmolter
 */
public class AdvancedExample {

  public static void main(String[] args) {

    // Create Chart
    final Chart_XY chart = new ChartBuilder_XY().width(600).height(400).title("Area Chart").xAxisTitle("X").yAxisTitle("Y").build();

    // Customize Chart
    chart.getStyler().setLegendPosition(LegendPosition.InsideNE);
    chart.getStyler().setDefaultSeriesRenderStyle(ChartXYSeriesRenderStyle.Area);

    // Series
    chart.addSeries("a", new double[] { 0, 3, 5, 7, 9 }, new double[] { -3, 5, 9, 6, 5 });
    chart.addSeries("b", new double[] { 0, 2, 4, 6, 9 }, new double[] { -1, 6, 4, 0, 4 });
    chart.addSeries("c", new double[] { 0, 1, 3, 8, 9 }, new double[] { -2, -1, 1, 0, 1 });

    // Create and set up the window.
    final JFrame frame = new JFrame("Advanced Example");

    // Schedule a job for the event-dispatching thread:
    // creating and showing this application's GUI.
    javax.swing.SwingUtilities.invokeLater(new Runnable() {

      @Override
      public void run() {

        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS)); // <-- you need this for now

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel chartPanel = new XChartPanel(chart);
        frame.add(chartPanel);

        JLabel label = new JLabel("Blah blah blah.", SwingConstants.CENTER);

        frame.add(label);

        // Display the window.
        frame.pack();
        frame.setVisible(true);
      }
    });
  }

}
