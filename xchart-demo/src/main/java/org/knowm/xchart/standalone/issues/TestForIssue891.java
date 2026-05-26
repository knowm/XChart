package org.knowm.xchart.standalone.issues;

import java.util.Arrays;
import java.util.Collections;
import org.knowm.xchart.BoxChart;
import org.knowm.xchart.BoxChartBuilder;
import org.knowm.xchart.BoxSeries;
import org.knowm.xchart.SwingWrapper;

/**
 * Reproducer for https://github.com/knowm/XChart/issues/891
 *
 * <p>Updating a BoxChart by setting the BoxSeries data to an empty list causes an
 * IndexOutOfBoundsException.
 */
public class TestForIssue891 {

  public static void main(String[] args) {

    demonstrateValidation();
    new SwingWrapper<>(getChart()).displayChart();
  }

  /** Constructs and returns the chart without launching a window (headless-safe). */
  public static BoxChart getChart() {

    BoxChart chart =
        new BoxChartBuilder()
            .title("Box plot – replace with valid data (issue 891)")
            .xAxisTitle("X")
            .yAxisTitle("Y")
            .build();

    BoxSeries boxSeries = chart.addSeries("test", Arrays.asList(1, 2, 3));
    // Replacing with valid data should work fine
    boxSeries.replaceData(Arrays.asList(4, 5, 6));

    return chart;
  }

  /**
   * Demonstrates that replacing with empty data throws an IllegalArgumentException rather than an
   * IndexOutOfBoundsException.
   */
  public static void demonstrateValidation() {

    BoxChart chart =
        new BoxChartBuilder().title("validation demo").xAxisTitle("X").yAxisTitle("Y").build();

    BoxSeries boxSeries = chart.addSeries("test", Arrays.asList(1, 2));
    try {
      boxSeries.replaceData(Collections.emptyList());
      System.out.println("ERROR: expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      System.out.println("OK: caught expected exception: " + e.getMessage());
    }
  }
}
