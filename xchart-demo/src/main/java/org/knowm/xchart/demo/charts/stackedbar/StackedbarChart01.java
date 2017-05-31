/**
 * Copyright 2015-2016 Knowm Inc. (http://knowm.org) and contributors. Copyright
 * 2011-2015 Xeiam LLC (http://xeiam.com) and contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.knowm.xchart.demo.charts.stackedbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.knowm.xchart.ChartBuilder_Category;
import org.knowm.xchart.Chart_Category;
import org.knowm.xchart.StackedBarChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.style.Styler;

public class StackedbarChart01 implements ExampleChart {

    public static void main(String[] args) {

        ExampleChart exampleChart = new StackedbarChart01();
        Chart chart = exampleChart.getChart();
        new SwingWrapper(chart).displayChart();
    }

    @Override
    public Chart getChart() {

        // Create Chart
         Chart_Category chart = new ChartBuilder_Category().width(800).height(600).title("Temperature vs. Color").xAxisTitle("Color").yAxisTitle("Temperature").theme(Styler.ChartTheme.GGPlot2).build();
 
         List<String> xData = new ArrayList<String>(Arrays.asList(new String[]{"Blue", "Red", "Green", "Yellow", "Orange"}));
         List<String> labels = new ArrayList<String>(Arrays.asList(new String[]{"fish", "worms", "birds", "ants", "slugs"}));
         List<Double> fish = new ArrayList<Double>(Arrays.asList(new Double[]{2.0, 4.0, 3.0, 1.0, 1.0}));
         List<Double> worms = new ArrayList<Double>(Arrays.asList(new Double[]{1.0, 4.0, 6.0, 4.0, 1.0}));
         List<Double> birds = new ArrayList<Double>(Arrays.asList(new Double[]{3.0, 5.0, 6.0, 1.0, 1.0}));
         List<Double> ants = new ArrayList<Double>(Arrays.asList(new Double[]{3.0, 5.0, 1.0, 2.0, 1.0}));
         List<Double> slugs = new ArrayList<Double>(Arrays.asList(new Double[]{6.0, 1.0, 3.0, 2.0, 1.0}));
         
         StackedBarChart stackedBarChart = new StackedBarChart(labels, xData, fish, worms, birds, ants, slugs);
         chart = stackedBarChart.addChartElements(chart, true);
         return chart;

    }

}
