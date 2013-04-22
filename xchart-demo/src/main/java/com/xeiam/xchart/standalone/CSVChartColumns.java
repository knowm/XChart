/**
 * Copyright (c) 2013 Knowmtech <http://knowmtech.com>
 * 
 * All rights reserved. No warranty, explicit or implicit, provided. In no event shall the author be liable for any claim or damages.
 * 
 * IMPORTANT: THIS CODE IS PROPRIETARY!!! ABSOLUTELY NO DUPLICATION OR DISTRIBUTION IS PERMITTED WITHOUT EXPRESS WRITTEN PERMISSION FROM:
 * M. ALEXANDER NUGENT CONSULTING 22B STACY RD, SANTA FE NM 87585 (505)-988-7016 i@alexnugent.name
 */
package com.xeiam.xchart.standalone;

import com.xeiam.xchart.CSVExporter;
import com.xeiam.xchart.CSVImporter;
import com.xeiam.xchart.CSVImporter.DataOrientation;
import com.xeiam.xchart.Chart;
import com.xeiam.xchart.SwingWrapper;

/**
 * @author timmolter
 */
public class CSVChartColumns {

  public static void main(String[] args) throws Exception {

    // import chart from a folder containing CSV files
    Chart chart = CSVImporter.getChartFromCSVDir("./CSV/CSVChartColumns/", DataOrientation.Columns, 600, 600);

    CSVExporter.writeCSVColumns(chart.getSeriesMap().get(0), "./CSV/CSVChartColumnsExport/");

    // Show it
    new SwingWrapper(chart).displayChart();

  }
}
