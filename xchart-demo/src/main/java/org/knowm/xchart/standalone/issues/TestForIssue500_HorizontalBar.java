package org.knowm.xchart.standalone.issues;

import java.util.Arrays;
import org.knowm.xchart.HorizontalBarChart;
import org.knowm.xchart.HorizontalBarChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler.LegendPosition;

/**
 * Issue #500: data labels can be placed outside the bars for horizontal bar charts too.
 *
 * <p>A labels position greater than 1 draws the label beyond the end of the bar. The automatic label
 * font color is computed against the plot background, so the labels stay legible without setting a
 * color manually.
 */
public class TestForIssue500_HorizontalBar implements ExampleChart<HorizontalBarChart> {

  public static void main(String[] args) {
    ExampleChart<HorizontalBarChart> exampleChart = new TestForIssue500_HorizontalBar();
    HorizontalBarChart chart = exampleChart.getChart();
    new SwingWrapper<>(chart).displayChart();
  }

  @Override
  public HorizontalBarChart getChart() {

    // Create Chart
    HorizontalBarChart chart =
        new HorizontalBarChartBuilder()
            .width(800)
            .height(600)
            .title("TestForIssue500_HorizontalBar")
            .yAxisTitle("Score")
            .xAxisTitle("Number")
            .build();

    // Customize Chart
    chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
    chart.getStyler().setPlotGridLinesVisible(false);
    chart.getStyler().setLabelsVisible(true);
    // A value greater than 1 places the labels outside (beyond the end of) the bars
    chart.getStyler().setLabelsPosition(1.1);

    // Series
    chart.addSeries("test 1", Arrays.asList(4, 5, -7, 6, -5), Arrays.asList(0, 1, 2, 3, 4));

    return chart;
  }

  @Override
  public String getExampleChartName() {

    return getClass().getSimpleName();
  }
}
