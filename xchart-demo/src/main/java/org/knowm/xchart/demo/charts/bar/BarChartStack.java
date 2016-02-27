package org.knowm.xchart.demo.charts.bar;
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

import org.knowm.xchart.ChartBuilder_Category;
import org.knowm.xchart.Chart_Category;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.style.Styler;

import java.util.ArrayList;
import java.util.Arrays;



import java.util.ArrayList;
import java.util.Arrays;

import org.knowm.xchart.ChartBuilder_Category;
import org.knowm.xchart.Chart_Category;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.style.Styler.ChartTheme;

/**
 * GGPlot2 Theme
 * <p>
 * Demonstrates the following:
 * <ul>
 * <li>String categories
 * <li>Positive and negative values
 * <li>Multiple series
 */

public class BarChartStack implements ExampleChart {

    public static void main(String[] args) {

        ExampleChart exampleChart = new BarChart05();
        Chart chart = exampleChart.getChart();
        new SwingWrapper(chart).displayChart();
    }




    @Override
    public Chart getChart() {


        // Create Chart
        Chart_Category chart = new ChartBuilder_Category().width(800).height(600).title("Temperature vs. Color").xAxisTitle("Color").yAxisTitle("Temperature").theme(Styler.ChartTheme.GGPlot2).build();

        // Customize Chart
        chart.getStyler().setBarsOverlapped(true);// keep it true for stackbar , due to overlaping

        double a11,a12,a13,a14,a15;
        double a21,a22,a23,a24,a25;
        double a31,a32,a33,a34,a35;
        double a41,a42,a43,a44,a45;
        double a51,a52,a53,a54,a55;

        a11=2 ; a12=4 ; a13=3; a14=1 ; a15=1; //input for fish, just change it
        a21= 1; a22= 4; a23= 6; a24= 4; a25=1; //input for worms
        a31= 3; a32=5 ; a33=6 ; a34=1 ; a35=1; // input for birds
        a41= 3; a42=5 ; a43=1 ; a44=2 ; a45=1; // input for ants
        a51= 6; a52=1 ; a53=3 ; a54=2 ; a55=1; // input for slugs


        // Series
        chart.addSeries("fish", new ArrayList<String>(Arrays.asList(new String[]{"Blue", "Red", "Green", "Yellow", "Orange"})), new ArrayList<Number>(Arrays.asList(new Number[] { a11+a21+a31+a41+a51, a12+a22+a32+a42+a52, a13+a23+a33+a43+a53, a14+a24+a34+a44+a54, a15+a25+a35+a45+a55 })));
        chart.addSeries("worms", new ArrayList<String>(Arrays.asList(new String[] { "Blue", "Red", "Green", "Yellow", "Orange" })), new ArrayList<Number>(Arrays.asList(new Number[] { a21+a31+a41+a51, a22+a32+a42+a52, a23+a33+a43+a53, a24+a34+a44+a54, a25+a35+a45+a55 })));
        chart.addSeries("birds", new ArrayList<String>(Arrays.asList(new String[] { "Blue", "Red", "Green", "Yellow", "Orange" })), new ArrayList<Number>(Arrays.asList(new Number[] { a31+a41+a51, a32+a42+a52, a33+a43+a53, a34+a44+a54, a35+a45+a55 })));
        chart.addSeries("ants", new ArrayList<String>(Arrays.asList(new String[] { "Blue", "Red", "Green", "Yellow", "Orange" })), new ArrayList<Number>(Arrays.asList(new Number[] { a41+a51, a42+a52, a43+a53,a44+a54, a45+a55  })));
        chart.addSeries("slugs", new ArrayList<String>(Arrays.asList(new String[] { "Blue", "Red", "Green", "Yellow", "Orange" })), new ArrayList<Number>(Arrays.asList(new Number[] { a51, a52, a53, a54, a55  })));

        return chart;
    }
}
