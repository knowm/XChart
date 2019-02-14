package org.knowm.xchart.demo.charts.pie;

import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.PieSeries.PieSeriesRenderStyle;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.PieStyler.AnnotationType;
import org.knowm.xchart.style.SumFormatter;
import org.knowm.xchart.style.colors.BaseSeriesColors;

/**
 * Pie Chart with Donut Style and custom sum formatter.
 *
 * <p>Demonstrates the following:
 *
 * <ul>
 *   <li>Donut Chart
 *   <li>PieChartBuilder
 *   <li>XChart Theme
 */
public class PieChart07 implements ExampleChart<PieChart> {

  public static void main(String[] args) {

    ExampleChart<PieChart> exampleChart = new PieChart07();
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
            .title("PieChart07 - Pie Chart with Donut Style and custom sum formatter")
            .build();

    // Customize Chart
    chart.getStyler().setLegendVisible(false);
    chart.getStyler().setAnnotationType(AnnotationType.LabelAndValue);
    chart.getStyler().setAnnotationDistance(.82);
    chart.getStyler().setPlotContentSize(.9);
    chart.getStyler().setDefaultSeriesRenderStyle(PieSeriesRenderStyle.Donut);

    chart.getStyler().setSumFormatter(new SumFormatter() {
      @Override
      public String format(double sum) {
        return String.format("Total: %.0f", sum);
      }
    });

    chart.getStyler().setSeriesColors(new BaseSeriesColors().getSeriesColors());

    chart.getStyler().setSumVisible(true);
    chart.getStyler().setSumFontSize(20f);

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
