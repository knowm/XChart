package org.knowm.xchart.demo.charts.pie;

import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.PieStyler.LabelType;
import org.knowm.xchart.style.Styler;

/**
 * Pie Chart - circle with border
 *
 * <p>Demonstrates the following:
 *
 * <ul>
 *   <li>Pie Chart
 *   <li>PieChartBuilder
 *   <li>Custom series palette
 *   <li>Percentage Labels
 *   <li>Custom pie slice border width
 */
public class PieChart05 implements ExampleChart<PieChart> {

  public static void main(String[] args) {

    ExampleChart<PieChart> exampleChart = new PieChart05();
    PieChart chart = exampleChart.getChart();
    new SwingWrapper<>(chart).displayChart();
  }

  @Override
  public PieChart getChart() {

    // Create Chart
    PieChart chart =
        new PieChartBuilder()
            .width(800)
            .height(600)
            .title(getClass().getSimpleName())
            .theme(Styler.ChartTheme.Matlab)
            .build();

    // Customize Chart
    chart.getStyler().setLabelType(LabelType.Percentage);
    chart.getStyler().setSliceBorderWidth(10);
    // chart.getStyler().setDecimalPattern("#0.000");

    // Series
    chart.addSeries("Married", 2889);
    chart.addSeries("Single", 1932);
    chart.addSeries("Widowed", 390);
    chart.addSeries("Divorced", 789);

    return chart;
  }

  @Override
  public String getExampleChartName() {

    return getClass().getSimpleName() + " - Pie Chart with Matlab Theme";
  }
}
