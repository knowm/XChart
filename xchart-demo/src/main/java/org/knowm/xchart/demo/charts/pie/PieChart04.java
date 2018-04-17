package org.knowm.xchart.demo.charts.pie;

import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.PieSeries.PieSeriesRenderStyle;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.PieStyler.AnnotationType;
import org.knowm.xchart.style.Styler;

/**
 * Pie Chart with Donut Style
 *
 * <p>Demonstrates the following:
 *
 * <ul>
 *   <li>Donut Chart
 *   <li>PieChartBuilder
 *   <li>XChart Theme
 *   <li>Horizontal Legend OutsideS
 */
public class PieChart04 implements ExampleChart<PieChart> {

  public static void main(String[] args) {

    ExampleChart<PieChart> exampleChart = new PieChart04();
    PieChart chart = exampleChart.getChart();
    new SwingWrapper<PieChart>(chart).displayChart();
  }

  @Override
  public PieChart getChart() {

    // Create Chart
    PieChart chart =
        new PieChartBuilder().width(800).height(600).title("Pie Chart with Donut Style").build();
    chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideS);
    chart.getStyler().setLegendLayout(Styler.LegendLayout.Horizontal);

    // Customize Chart
    //    chart.getStyler().setLegendVisible(false);
    chart.getStyler().setAnnotationType(AnnotationType.Label);
    chart.getStyler().setAnnotationDistance(.82);
    chart.getStyler().setPlotContentSize(.9);
    chart.getStyler().setDefaultSeriesRenderStyle(PieSeriesRenderStyle.Donut);
    // chart.getStyler().setCircular(false);

    // Series
    chart.addSeries("A", 22);
    chart.addSeries("B", 10);
    chart.addSeries("C", 34);
    chart.addSeries("D", 22);
    chart.addSeries("E", 29);
    chart.addSeries("F", 40);

    return chart;
  }
}
