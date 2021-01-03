package org.knowm.xchart.demo.charts.pie;

import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.PieSeries.PieSeriesRenderStyle;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.PieStyler.LabelType;

/**
 * Pie Chart with Donut Style
 *
 * <p>Demonstrates the following:
 *
 * <ul>
 *   <li>Donut Chart
 *   <li>PieChartBuilder
 *   <li>XChart Theme
 *   <li>NameAndValue data labels
 *   <li>Sum in center of pie
 */
public class PieChart04 implements ExampleChart<PieChart> {

  public static void main(String[] args) {

    ExampleChart<PieChart> exampleChart = new PieChart04();
    PieChart chart = exampleChart.getChart();
    new SwingWrapper<>(chart).displayChart();
  }

  @Override
  public PieChart getChart() {

    // Create Chart
    PieChart chart =
        new PieChartBuilder().width(800).height(600).title(getClass().getSimpleName()).build();

    // Customize Chart
    chart.getStyler().setLegendVisible(false);
    chart.getStyler().setDefaultSeriesRenderStyle(PieSeriesRenderStyle.Donut);
    chart.getStyler().setLabelType(LabelType.NameAndValue);
    // TODO make this relative to the inner and outer edge of the doughnut slice, not the center of
    // the pie
    chart.getStyler().setLabelsDistance(.82);
    chart.getStyler().setPlotContentSize(.9);
    chart.getStyler().setSumVisible(true);

    // Series
    chart.addSeries("A", 22);
    chart.addSeries("B", 10);
    chart.addSeries("C", 34);
    chart.addSeries("D", 22);
    chart.addSeries("E", 29);
    chart.addSeries("F", 40);

    return chart;
  }

  @Override
  public String getExampleChartName() {

    return getClass().getSimpleName() + " - Pie Chart with Donut Style";
  }
}
