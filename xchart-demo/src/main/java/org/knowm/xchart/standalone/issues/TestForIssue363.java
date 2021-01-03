package org.knowm.xchart.standalone.issues;

import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.PieSeries.PieSeriesRenderStyle;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.PieStyler.ClockwiseDirectionType;
import org.knowm.xchart.style.PieStyler.LabelType;
import org.knowm.xchart.style.colors.BaseSeriesColors;

public class TestForIssue363 implements ExampleChart<PieChart> {

  public static void main(String[] args) {

    ExampleChart<PieChart> exampleChart = new TestForIssue363();
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
            .title("PieChart - Pie Chart with Pie Style with DirectionType CLOCKWISE")
            .build();

    // Customize Chart
    chart.getStyler().setLegendVisible(false);
    chart.getStyler().setLabelType(LabelType.NameAndValue);
    chart.getStyler().setLabelsDistance(.82);
    chart.getStyler().setPlotContentSize(.9);
    chart.getStyler().setDefaultSeriesRenderStyle(PieSeriesRenderStyle.Pie);
    chart.getStyler().setClockwiseDirectionType(ClockwiseDirectionType.CLOCKWISE);
    chart.getStyler().setDecimalPattern("#");

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

  @Override
  public String getExampleChartName() {

    return getClass().getSimpleName();
  }
}
