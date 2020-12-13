package org.knowm.xchart.standalone.issues;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.style.Styler;

public class TestForIssue539 {

  public static void main(String[] args) {
    XYChart myChart = new XYChart(800, 600);
    myChart.setTitle("Multiple Y axes scale bug");
    myChart.getStyler().setYAxisGroupPosition(0, Styler.YAxisPosition.Left);
    myChart.getStyler().setYAxisGroupPosition(1, Styler.YAxisPosition.Right);
    myChart.setXAxisTitle("x");
    myChart.setYAxisGroupTitle(0, "group zero");
    myChart.setYAxisGroupTitle(1, "group one");

    myChart
        .addSeries("series on group zero", new double[] {1, 2, 3}, new double[] {4, 5, 6})
        .setYAxisGroup(0);

    myChart
        .addSeries("series on group one", new double[] {1, 2, 3}, new double[] {-100, -200, -300})
        .setYAxisGroup(1);

    new SwingWrapper<>(myChart).displayChart();
  }
}
