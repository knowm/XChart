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
package org.knowm.xchart.demo.charts.ohlc;

import org.knowm.xchart.OHLCChart;
import org.knowm.xchart.OHLCChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Demonstrates the following:
 * <ul>
 * <li>Candle render style green up, red down
 * <li>LegendPosition.OutsideS
 * <li>Two YAxis Groups - both on left
 */
public class OHLCChart02 implements ExampleChart<OHLCChart> {

  public static void main(String[] args) {

    ExampleChart<OHLCChart> exampleChart = new OHLCChart02();
    OHLCChart chart = exampleChart.getChart();
    new SwingWrapper<OHLCChart>(chart).displayChart();
  }

  @Override
  public OHLCChart getChart() {

    // Create Chart
    OHLCChart chart = new OHLCChartBuilder().width(800).height(600).title("Prices").build();

    // Customize Chart
    chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideS);
    chart.getStyler().setLegendLayout(Styler.LegendLayout.Horizontal);

    List<Date> xData = new ArrayList<Date>();
    List<Double> openData = new ArrayList<Double>();
    List<Double> highData = new ArrayList<Double>();
    List<Double> lowData = new ArrayList<Double>();
    List<Double> closeData = new ArrayList<Double>();

    OHLCChart01.populateData(xData, openData, highData, lowData, closeData);
    xData = null;
    chart.addSeries("Series", xData, openData, highData, lowData, closeData);
    chart.getStyler().setToolTipsEnabled(true);
    return chart;
  }

}
