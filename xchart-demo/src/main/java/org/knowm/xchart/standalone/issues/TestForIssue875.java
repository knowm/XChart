package org.knowm.xchart.standalone.issues;

import java.util.Arrays;
import java.util.List;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.SwingWrapper;

/** Category lists and primitive numeric values, without boxing at the call site. */
public class TestForIssue875 {

  public static void main(String[] args) {
    new SwingWrapper<>(getChart()).displayChart();
  }

  /** Constructs and returns the chart without launching a window (headless-safe). */
  public static CategoryChart getChart() {
    CategoryChart chart =
        new CategoryChartBuilder()
            .width(640)
            .height(480)
            .title("Category lists with primitive values")
            .xAxisTitle("Category")
            .yAxisTitle("Value")
            .build();
    List<String> categories = Arrays.asList("First", "Second", "Third");
    chart.addSeriesWithCategories("Integers", categories, new int[] {3, 1, 4});
    chart.addSeriesWithCategories(
        "Doubles", categories, new double[] {2.5, 3.5, 4.5}, new double[] {0.5, 0.25, 0.75});
    return chart;
  }
}
