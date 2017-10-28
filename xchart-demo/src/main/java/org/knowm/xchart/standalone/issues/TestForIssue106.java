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

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.demo.charts.area.AreaChart03;
import org.knowm.xchart.style.Styler;

/**
 * Create a Chart matrix
 *
 * @author timmolter
 */
public class TestForIssue106 {

  public static void main(String[] args) {

    ExampleChart<XYChart> alc = new AreaChart03();
    List<XYChart> charts = new ArrayList<XYChart>();
    {
      XYChart chart = alc.getChart();
      chart.setTitle("Default data labels");
      chart.getStyler().setToolTipsEnabled(true);
      charts.add(chart);
    }
    {
      XYChart chart = alc.getChart();
      chart.setTitle("No data label");
      charts.add(chart);
    }
    {
      //current default
      XYChart chart = alc.getChart();
      chart.getStyler().setToolTipsEnabled(true);
      chart.getStyler().setToolTipBackgroundColor(Color.RED);
      chart.getStyler().setToolTipType(Styler.ToolTipType.yLabels);
      chart.setTitle("Red background");
      charts.add(chart);
    }
    {
      XYChart chart = alc.getChart();
      chart.getStyler().setToolTipsEnabled(true);
      chart.getStyler().setToolTipBorderColor(Color.BLUE);
      chart.getStyler().setToolTipFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));
      chart.setTitle("Blue and custom Font");
      charts.add(chart);
    }

    new SwingWrapper<XYChart>(charts).displayChartMatrix();
  }
}
