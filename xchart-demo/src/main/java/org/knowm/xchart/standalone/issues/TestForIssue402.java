package org.knowm.xchart.standalone.issues;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler.LegendPosition;

public class TestForIssue402 implements ExampleChart<CategoryChart> {

  public static void main(String[] args) {
    ExampleChart<CategoryChart> exampleChart = new TestForIssue402();
    CategoryChart chart = exampleChart.getChart();
    new SwingWrapper<CategoryChart>(chart).displayChart();
  }

  @Override
  public CategoryChart getChart() {

    // Create Chart
    CategoryChart chart =
        new CategoryChartBuilder()
            .width(800)
            .height(600)
            .title("TestForIssue402")
            .xAxisTitle("x")
            .yAxisTitle("y")
            .build();

    // Customize Chart
    chart.getStyler().setLegendPosition(LegendPosition.OutsideE);
    chart.getStyler().setStacked(true);
    chart.getStyler().setLabelsVisible(true);
    chart.getStyler().setShowStackSum(true);

    // Series
    chart.addSeries("a", new double[] {0, 1, 2, 3, 4}, new double[] {40, 35, -45, -60, -60});
    chart.addSeries("b", new double[] {0, 1, 2, 3, 4}, new double[] {50, 65, 60, -70, 30});
    chart.addSeries("c", new double[] {0, 1, 2, 3, 4}, new double[] {70, 45, -55, -80, 40});
    chart.addSeries("d", new double[] {0, 1, 2, 3, 4}, new double[] {90, 80, 75, 75, 50});

    return chart;
  }

  @Override
  public String getExampleChartName() {

    return getClass().getSimpleName();
  }
}
