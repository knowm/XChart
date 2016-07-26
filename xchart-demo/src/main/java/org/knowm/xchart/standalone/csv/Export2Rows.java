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
package org.knowm.xchart.standalone.csv;

import org.knowm.xchart.CSVExporter;
import org.knowm.xchart.CSVImporter;
import org.knowm.xchart.CSVImporter.DataOrientation;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

/**
 * @author timmolter
 */
public class Export2Rows {

  public static void main(String[] args) throws Exception {

    // import chart from a folder containing CSV files
    XYChart chart = CSVImporter.getChartFromCSVDir("./CSV/CSVChartRows/", DataOrientation.Rows, 600, 400);

    // export a single series
    CSVExporter.writeCSVRows(chart.getSeriesMap().get("series1"), "./CSV/CSVChartRowsExport/");

    // export all series
    CSVExporter.writeCSVRows(chart, "./CSV/CSVChartRowsExport/");

    // Show it
    new SwingWrapper<XYChart>(chart).displayChart();

  }
}
