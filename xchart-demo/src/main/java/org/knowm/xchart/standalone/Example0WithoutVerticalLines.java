/**
 * Copyright 2015 Knowm Inc. (http://knowm.org) and contributors.
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
package org.knowm.xchart.standalone;

import java.util.ArrayList;
import java.util.List;

import org.knowm.xchart.Chart;
import org.knowm.xchart.ChartBuilder;
import org.knowm.xchart.Series;
import org.knowm.xchart.SeriesMarker;
import org.knowm.xchart.SwingWrapper;

/**
 * Creates a simple Chart using QuickChart
 */
public class Example0WithoutVerticalLines {

	  public static void main(String[] args) {

		    int numCharts = 4;

		    List<Chart> charts = new ArrayList<Chart>();

		    for (int i = 0; i < numCharts; i++) {
		      Chart chart = new ChartBuilder().xAxisTitle("X").yAxisTitle("Y").width(600).height(400).build();
		      chart.getStyleManager().setYAxisMin(-10);
		      chart.getStyleManager().setYAxisMax(10);
		      chart.getStyleManager().setPlotGridVerticalLinesVisible(false);
		      Series series = chart.addSeries("" + i, null, getRandomWalk(200));
		      series.setMarker(SeriesMarker.NONE);
		      charts.add(chart);
		    }
		    new SwingWrapper(charts).displayChartMatrix();
		  }

		  /**
		   * Generates a set of random walk data
		   * 
		   * @param numPoints
		   * @return
		   */
		  private static double[] getRandomWalk(int numPoints) {

		    double[] y = new double[numPoints];
		    y[0] = 0;
		    for (int i = 1; i < y.length; i++) {
		      y[i] = y[i - 1] + Math.random() - .5;
		    }
		    return y;
		  }

}
