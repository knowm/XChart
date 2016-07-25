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
package org.knowm.xchart.demo.charts.realtime;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.PieSeries.PieSeriesRenderStyle;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.PieStyler.AnnotationType;
import org.knowm.xchart.style.Styler.ChartTheme;

/**
 * Real-time Pie Chart
 * <p>
 * Demonstrates the following:
 * <ul>
 * <li>real-time chart updates with SwingWrapper
 * <li>Matlab theme
 * <li>Pie Chart
 */
public class RealtimeChart02 implements ExampleChart<PieChart> {

  private PieChart pieChart;

  public static void main(String[] args) {

    // Setup the panel
    final RealtimeChart02 realtimeChart01 = new RealtimeChart02();
    realtimeChart01.go();
  }

  private void go() {

    final SwingWrapper<PieChart> swingWrapper = new SwingWrapper<PieChart>(getChart());
    swingWrapper.displayChart();

    // Simulate a data feed
    TimerTask chartUpdaterTask = new TimerTask() {

      @Override
      public void run() {

        updateData();

        javax.swing.SwingUtilities.invokeLater(new Runnable() {

          @Override
          public void run() {

            swingWrapper.repaintChart();
          }
        });
      }
    };

    Timer timer = new Timer();
    timer.scheduleAtFixedRate(chartUpdaterTask, 0, 500);
  }

  @Override
  public PieChart getChart() {

    // Create Chart
    pieChart = new PieChartBuilder().width(500).height(400).theme(ChartTheme.Matlab).title("Real-time Pie Chart").build();

    // Customize Chart
    pieChart.getStyler().setLegendVisible(false);
    pieChart.getStyler().setAnnotationType(AnnotationType.LabelAndPercentage);
    pieChart.getStyler().setAnnotationDistance(1.22);
    pieChart.getStyler().setPlotContentSize(.7);
    pieChart.getStyler().setDefaultSeriesRenderStyle(PieSeriesRenderStyle.Donut);

    Map<String, Number> pieData = getRandomData();
    for (Entry<String, Number> entry : pieData.entrySet()) {
      pieChart.addSeries(entry.getKey(), entry.getValue());
    }
    return pieChart;
  }

  public void updateData() {

    Map<String, Number> pieData = getRandomData();
    for (Entry<String, Number> entry : pieData.entrySet()) {
      pieChart.updatePieSeries(entry.getKey(), entry.getValue());
    }
  }

  private Map<String, Number> getRandomData() {

    Map<String, Number> pieData = new HashMap<String, Number>();

    pieData.put("A", Math.random() * 100);
    pieData.put("B", Math.random() * 100);
    pieData.put("C", Math.random() * 100);
    pieData.put("D", Math.random() * 100);
    pieData.put("E", Math.random() * 100);

    return pieData;

  }
}
