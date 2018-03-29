package org.knowm.xchart.standalone.csv;

import org.knowm.xchart.CSVExporter;
import org.knowm.xchart.CSVImporter;
import org.knowm.xchart.CSVImporter.DataOrientation;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

/** @author timmolter */
public class Export2Columns {

  public static void main(String[] args) throws Exception {

    // import chart from a folder containing CSV files
    XYChart chart =
        CSVImporter.getChartFromCSVDir("./CSV/CSVChartColumns/", DataOrientation.Columns, 600, 600);

    // export a single series
    CSVExporter.writeCSVColumns(
        chart.getSeriesMap().get("series1"), "./CSV/CSVChartColumnsExport/");

    // export all series
    CSVExporter.writeCSVColumns(chart, "./CSV/CSVChartColumnsExport/");

    // Show it
    new SwingWrapper<XYChart>(chart).displayChart();
  }
}
