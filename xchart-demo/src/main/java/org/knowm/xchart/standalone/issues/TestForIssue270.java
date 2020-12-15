package org.knowm.xchart.standalone.issues;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler.CardinalPosition;

public class TestForIssue270 {

  public static void main(String[] args) {

    // Create Chart
    XYChart chart =
        new XYChartBuilder()
            .width(800)
            .height(600)
            .title("TestForIssue270")
            .xAxisTitle("X")
            .yAxisTitle("y")
            .build();

    // Customize Chart
    chart.getStyler().setLegendPosition(CardinalPosition.OutsideE);
    chart.getStyler().setToolTipsEnabled(true);
    chart.getStyler().setToolTipsAlwaysVisible(true);

    // Series
    XYSeries a = chart.addSeries("a", new double[] {1, 2, 3, 4, 5}, new double[] {-1, 6, 9, 6, 5});
    a.setCustomToolTips(true);

    chart.addSeries("b", new double[] {1, 2, 3, 4, 5}, new double[] {9, 7, 3, -3, 8});

    new SwingWrapper<>(chart).displayChart();
  }
}
