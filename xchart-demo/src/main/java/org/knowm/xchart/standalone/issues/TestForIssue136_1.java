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
import java.util.Arrays;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.Styler.LegendPosition;

/**
 * Create a Chart matrix
 *
 * @author timmolter
 */
public class TestForIssue136_1 {

  public static void main(String[] args) {
    int[][] sizes = { {800, 600}, {800, 800}, {800,900}}; 
    
    ArrayList<CategoryChart> charts = new ArrayList<CategoryChart>();
    for (int[] is : sizes) {
      CategoryChart chart = getChart(is[0], is[1]);
      chart.getStyler().setXAxisLabelRotation(90);
      //chart.getStyler().setAxisTitlesVisible(false);
      chart.getStyler().setYAxisTicksVisible(false);
      chart.getStyler().setHasAnnotations(false);
      chart.getStyler().setLegendVisible(false);

      chart.setVertical(true);
      //chart.getStyler().setYAxisTitleVisible(false);
      charts.add(chart);
      
      new SwingWrapper<CategoryChart>(chart).displayChart(is[0] + "x" + is[1]);
    }

    new SwingWrapper<CategoryChart>(charts).displayChartMatrix();
  }

  
  public static CategoryChart getChart(int width, int height) {

    // Create Chart
    CategoryChart chart = new CategoryChartBuilder().width(width).height(height).title("Score Histogram").xAxisTitle("Score").yAxisTitle("Number").build();

    // Customize Chart
    chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
    chart.getStyler().setHasAnnotations(true);
    chart.getStyler().setPlotGridLinesVisible(false);

    // Series
    chart.addSeries("test 1", Arrays.asList(0, 1, 2, 3, 4), Arrays.asList(4, 5, 9, 6, 5));

    return chart;
  }

  
}
