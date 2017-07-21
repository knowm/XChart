/**
 * Copyright 2015-2017 Knowm Inc. (http://knowm.org) and contributors.
 * Copyright 2011-2015 Xeiam LLC (http://xeiam.com) and contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.knowm.xchart.standalone.issues;

import java.util.ArrayList;
import java.util.List;

import org.knowm.xchart.RadarChart;
import org.knowm.xchart.RadarChart.RadarRenderStyle;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.demo.charts.radar.RadarChart01;
import org.knowm.xchart.style.lines.SeriesLines;

/**
 * Create a Chart matrix
 *
 * @author timmolter
 */
public class TestForIssue189_1 {

  public static void main(String[] args) {

    ExampleChart<RadarChart> alc = new RadarChart01();
    List<RadarChart> charts = new ArrayList<RadarChart>();
    {
      RadarChart chart = alc.getChart();
      chart.setTitle("Default radar chart");
      charts.add(chart);
    }
    {
      RadarChart chart = alc.getChart();
      chart.setTitle("Radar chart with circle rendering");
      chart.getStyler().setPlotGridLinesStroke(SeriesLines.NONE);
      chart.setRadarRenderStyle(RadarRenderStyle.Circle);
      charts.add(chart);
    }
    {
      //current default
      RadarChart chart = alc.getChart();
      chart.setTitle("Radar chart with 3 variables and start angle");
      chart.getStyler().setToolTipsEnabled(true);
      chart.getStyler().setStartAngleInDegrees(45);
      chart.getStyler().setPlotGridLinesStroke(SeriesLines.NONE);
      chart.setVariableLabels(new String[] {"Sales", "Marketting", "Development"});
      charts.add(chart);
    }
    {
      RadarChart chart = alc.getChart();
      chart.setTitle("Radar chart with non circular rendering");
      chart.getStyler().setPlotGridLinesStroke(SeriesLines.NONE);
      chart.getStyler().setCircular(false);      
      charts.add(chart);
    }

    new SwingWrapper<RadarChart>(charts).displayChartMatrix();
  }

}
