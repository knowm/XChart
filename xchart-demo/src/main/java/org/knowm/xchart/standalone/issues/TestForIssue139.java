package org.knowm.xchart.standalone.issues;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.CategorySeries.CategorySeriesRenderStyle;
import org.knowm.xchart.SwingWrapper;

/** @author timmolter */
public class TestForIssue139 {

  public static void main(String[] args) {

    int[] x = new int[] {0, 1, 2, 3, 4};
    int[] a = new int[] {1, 3, 1, 2, 1};
    int[] b = new int[] {2, 1, 1, 2, 2};
    int[] c = new int[] {1, 1, 2, 3, 3};

    CategoryChart chart = new CategoryChartBuilder().width(640).height(480).build();

    chart.getStyler().setLegendVisible(false);
    chart.getStyler().setDefaultSeriesRenderStyle(CategorySeriesRenderStyle.Stick);
    // chart.getStyler().setBarsOverlapped(true);
    chart.getStyler().setAvailableSpaceFill(.25);

    chart.addSeries("A", x, a);
    chart.addSeries("B", x, b);
    chart.addSeries("C", x, c);

    new SwingWrapper(chart).displayChart();
  }
}
