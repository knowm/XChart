/**
 * Copyright (C) 2013 Xeiam LLC http://xeiam.com
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.xeiam.xchart.demo.charts.bar;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.ChartBuilder;
import com.xeiam.xchart.SwingWrapper;
import com.xeiam.xchart.demo.charts.ExampleChart;
import com.xeiam.xchart.style.StyleManager.ChartType;
import com.xeiam.xchart.style.theme.GGPlot2Theme;

/**
 * <p>
 * Demonstrates the following:
 * <ul>
 * <li>Number categories
 * <li>Positive and negative values
 * <li>Multiple series
 */
public class BarChart05 implements ExampleChart {

  public static void main(String[] args) {

    ExampleChart exampleChart = new BarChart05();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  @Override
  public Chart getChart() {

    // Create Chart
    Chart chart = new ChartBuilder().chartType(ChartType.Bar).width(800).height(600).title("Temperature vs. Length").xAxisTitle("Length").yAxisTitle("Temperature").theme(new GGPlot2Theme()).build();
    chart.addSeries("fish", new double[] { 10, 20, 30, 40 }, new double[] { -40, 30, 20, 60 });
    chart.addSeries("worms", new double[] { 10, 20, 30, 40 }, new double[] { 50, 10, -20, 40 });

    // Customize Chart
    chart.getStyleManager().setChartTitleVisible(false);

    return chart;
  }
}
