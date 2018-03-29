package org.knowm.xchart.standalone.issues;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.SwingWrapper;

/** @author timmolter */
public class TestForIssue111 {

  public static void main(String[] args) {

    int[] x = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
    int[] y = new int[] {1, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1};
    // int[] x = new int[] { 0, 1, 2, 3, 4, 5, 6, 7 };
    // int[] y = new int[] { 1, 0, 1, 0, 1, 0, 0, 0 };

    CategoryChart chart = new CategoryChartBuilder().width(640).height(480).build();
    chart.addSeries("test", x, y);
    chart.getStyler().setLegendVisible(false);
    new SwingWrapper(chart).displayChart();
  }
}
