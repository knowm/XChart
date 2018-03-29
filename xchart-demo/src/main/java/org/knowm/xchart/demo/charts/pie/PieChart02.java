package org.knowm.xchart.demo.charts.pie;

import java.awt.Color;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.PieStyler.AnnotationType;

/**
 * Pie Chart Custom Color Palette
 *
 * <p>Demonstrates the following:
 *
 * <ul>
 *   <li>Pie Chart
 *   <li>PieChartBuilder
 *   <li>Custom series palette
 *   <li>Value Annotations
 */
public class PieChart02 implements ExampleChart<PieChart> {

  public static void main(String[] args) {

    ExampleChart<PieChart> exampleChart = new PieChart02();
    PieChart chart = exampleChart.getChart();
    new SwingWrapper<PieChart>(chart).displayChart();
  }

  @Override
  public PieChart getChart() {

    // Create Chart
    PieChart chart =
        new PieChartBuilder()
            .width(800)
            .height(600)
            .title("Pie Chart Custom Color Palette")
            .build();

    // Customize Chart
    Color[] sliceColors =
        new Color[] {
          new Color(224, 68, 14),
          new Color(230, 105, 62),
          new Color(236, 143, 110),
          new Color(243, 180, 159),
          new Color(246, 199, 182)
        };
    chart.getStyler().setSeriesColors(sliceColors);
    chart.getStyler().setAnnotationType(AnnotationType.Value);
    // chart.getStyler().setDecimalPattern("#0.000");

    // Series
    chart.addSeries("Gold", 24);
    chart.addSeries("Silver", 21);
    chart.addSeries("Platinum", 39);
    chart.addSeries("Copper", 17);
    chart.addSeries("Zinc", 40);

    return chart;
  }
}
