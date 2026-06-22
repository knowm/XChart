package org.knowm.xchart.standalone.issues;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler.LegendPosition;

/**
 * Issue #500: data labels can be placed outside (on top of) the bars again, as in 3.6.1.
 *
 * <p>A labels position greater than 1 draws the label outside the bar (above positive bars, below
 * negative bars). The automatic label font color is computed against the plot background, so the
 * labels stay legible without setting a color manually.
 */
public class TestForIssue500 implements ExampleChart<CategoryChart> {

  public static void main(String[] args) {
    ExampleChart<CategoryChart> exampleChart = new TestForIssue500();
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
            .title("TestForIssue500")
            .xAxisTitle("x")
            .yAxisTitle("y")
            .build();

    // Customize Chart
    chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
    chart.getStyler().setLabelsVisible(true);
    // A value greater than 1 places the labels outside (above/below) the bars
    chart.getStyler().setLabelsPosition(1.1);

    // Series
    chart.addSeries("test 1", new double[] {0, 1, 2, 3, 4}, new double[] {4, 5, -7, 6, -5});

    return chart;
  }

  @Override
  public String getExampleChartName() {

    return getClass().getSimpleName();
  }
}
