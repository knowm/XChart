package org.knowm.xchart.demo.charts.horizontalbar;

import org.knowm.xchart.HorizontalBarChart;
import org.knowm.xchart.HorizontalBarChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler;

import java.util.Arrays;

/**
 * Multiple series, labels and tooltips
 *
 * <p>Demonstrates the following:
 *
 * <ul>
 *   <li>Number categories
 *   <li>Positive values
 *   <li>Multiple series
 *   <li>Missing point in series
 *   <li>Labels
 *   <li>Bar Chart Annotations
 *   <li>Horizontal Legend OutsideS
 */
public class HorizontalBarChart04 implements ExampleChart<HorizontalBarChart> {

  public static void main(String[] args) {

    ExampleChart<HorizontalBarChart> exampleChart = new HorizontalBarChart04();
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
            .title(getClass().getSimpleName())
            .yAxisTitle("Age")
            .xAxisTitle("XFactor")
            .build();

    // Customize Chart
    chart.getStyler().setLabelsVisible(true);
    chart.getStyler().setToolTipsEnabled(true);
    chart.getStyler().setPlotGridVerticalLinesVisible(false);
    chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideS);
    chart.getStyler().setLegendLayout(Styler.LegendLayout.Horizontal);

    // Series
    chart.addSeries("female", Arrays.asList(50, 10, 20, 40, 35), Arrays.asList(10, 20, 30, 40, 50));
    chart.addSeries("male", Arrays.asList(40, 30, 20, null, 60), Arrays.asList(10, 20, 30, 40, 50));

    return chart;
  }

  @Override
  public String getExampleChartName() {

    return getClass().getSimpleName() + " - Multiple series, labels and tooltips";
  }
}
