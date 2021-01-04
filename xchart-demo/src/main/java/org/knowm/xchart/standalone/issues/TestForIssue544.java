package org.knowm.xchart.standalone.issues;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.CategorySeries;
import org.knowm.xchart.SwingWrapper;

public class TestForIssue544 {

  public static void main(String[] args) {

    TestForIssue544 exampleChart = new TestForIssue544();
    CategoryChart chart = exampleChart.getChart();
    new SwingWrapper<CategoryChart>(chart).displayChart();
  }

  public CategoryChart getChart() {

    // Create Chart
    CategoryChart chart =
        new CategoryChartBuilder()
            .width(800)
            .height(600)
            .title("LineChart04")
            .xAxisTitle("X")
            .yAxisTitle("Y")
            .build();

    // Customize Chart
    chart.getStyler().setLegendVisible(false);
    CategorySeries series =
        chart.addSeries("A", new double[] {0.0, 1.0, 3.0}, new double[] {0.0, 10.0, 20.0});
    series.setEnabled(false);
    return chart;
  }

  public String getExampleChartName() {

    return getClass().getSimpleName() + " - Hundreds of Series on One Plot";
  }
}
