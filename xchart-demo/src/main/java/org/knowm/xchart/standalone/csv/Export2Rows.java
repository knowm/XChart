package org.knowm.xchart.standalone.csv;

import org.knowm.xchart.CSVExporter;
import org.knowm.xchart.CSVImporter;
import org.knowm.xchart.CSVImporter.DataOrientation;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

/** @author timmolter */
public class Export2Rows {

  public static void main(String[] args) throws Exception {

    // import chart from a folder containing CSV files
    XYChart chart =
        CSVImporter.getChartFromCSVDir("./CSV/CSVChartRows/", DataOrientation.Rows, 600, 400);

    // export a single series
    CSVExporter.writeCSVRows(chart.getSeriesMap().get("series1"), "./CSV/CSVChartRowsExport/");

    // export all series
    CSVExporter.writeCSVRows(chart, "./CSV/CSVChartRowsExport/");

    // Show it
    new SwingWrapper<XYChart>(chart).displayChart();
  }
}
