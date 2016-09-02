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
package org.knowm.xchart.standalone.issues;

import java.awt.Color;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.colors.XChartSeriesColors;
import org.knowm.xchart.style.lines.SeriesLines;
import org.knowm.xchart.style.markers.SeriesMarkers;

/**
 * @author timmolter
 */
public class TestForIssue27_1 {

  public static void main(String[] args) throws Exception {

    double[] xData = new double[] { 0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0 };
    double[] yData1 = new double[] { 100, 100, 100, 50, 50, 50, 50 };
    double[] errdata = new double[] { 50, 20, 10, 50, 40, 20, 10 };

    double[] yData2 = new double[] { 50, 80, 90, 0, 10, 30, 40 };
    double[] yData3 = new double[] { 150, 120, 110, 100, 90, 70, 60 };

    XYChart mychart = new XYChart(900, 700);
    mychart.getStyler().setYAxisMin(0.0);
    mychart.getStyler().setYAxisMax(150.0);
    mychart.getStyler().setErrorBarsColor(Color.black);
    XYSeries series1 = mychart.addSeries("Error bar test data", xData, yData1, errdata);
    XYSeries series2 = mychart.addSeries("Y+error", xData, yData2);
    XYSeries series3 = mychart.addSeries("Y-error", xData, yData3);
    series1.setLineStyle(SeriesLines.SOLID);
    series1.setMarker(SeriesMarkers.DIAMOND);
    series1.setMarkerColor(Color.MAGENTA);
    series2.setLineStyle(SeriesLines.DASH_DASH);
    series2.setMarker(SeriesMarkers.NONE);
    series2.setLineColor(XChartSeriesColors.RED);
    series3.setLineStyle(SeriesLines.DASH_DASH);
    series3.setMarker(SeriesMarkers.NONE);
    series3.setLineColor(XChartSeriesColors.RED);

    new SwingWrapper(mychart).displayChart();
  }

}
