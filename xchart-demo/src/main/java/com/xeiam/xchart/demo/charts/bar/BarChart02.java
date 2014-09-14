/**
 * Copyright 2011 - 2014 Xeiam LLC.
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
package com.xeiam.xchart.demo.charts.bar;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.ChartBuilder;
import com.xeiam.xchart.Series;
import com.xeiam.xchart.SeriesColor;
import com.xeiam.xchart.StyleManager.ChartTheme;
import com.xeiam.xchart.StyleManager.ChartType;
import com.xeiam.xchart.SwingWrapper;
import com.xeiam.xchart.demo.charts.ExampleChart;

/**
 * Date Categories
 * <p>
 * Demonstrates the following:
 * <ul>
 * <li>Date categories
 * <li>All negative values
 * <li>Single series
 * <li>No horizontal plot gridlines
 * <li>Change series color
 * <li>MATLAB Theme
 */
public class BarChart02 implements ExampleChart {

  public static void main(String[] args) {

    ExampleChart exampleChart = new BarChart02();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  @Override
  public Chart getChart() {

    // Create Chart
    Chart chart = new ChartBuilder().theme(ChartTheme.Matlab).chartType(ChartType.Bar).width(800).height(600).title("Units Sold Per Year").xAxisTitle("Year").yAxisTitle("Units Sold").build();

    List<Date> xData = new ArrayList<Date>();
    Collection<Number> yData = new ArrayList<Number>();

    Random random = new Random();
    DateFormat sdf = new SimpleDateFormat("yyyy");
    Date date = null;
    for (int i = 1; i <= 8; i++) {
      try {
        date = sdf.parse("" + (2000 + i));
      } catch (ParseException e) {
        e.printStackTrace();
      }
      xData.add(date);
      yData.add(-1 * 0.00000001 * ((random.nextInt(i) + 1)));
    }
    Series series = chart.addSeries("Model 77", xData, yData);
    series.setLineColor(SeriesColor.RED);
    chart.getStyleManager().setPlotGridLinesVisible(false);
    chart.getStyleManager().setBarFilled(false);

    return chart;
  }
}
