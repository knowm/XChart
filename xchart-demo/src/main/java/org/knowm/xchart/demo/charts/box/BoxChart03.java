package org.knowm.xchart.demo.charts.box;

import java.util.Arrays;
import org.knowm.xchart.BoxChart;
import org.knowm.xchart.BoxChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler.ChartTheme;

/*
 * Box Plot with 1 series
 * and show ToolTips
 * and Y-Axis is logarithmic
 */
public class BoxChart03 implements ExampleChart<BoxChart> {

  public static void main(String[] args) {
    ExampleChart<BoxChart> exampleChart = new BoxChart03();
    BoxChart chart = exampleChart.getChart();
    new SwingWrapper<BoxChart>(chart).displayChart();
  }

  @Override
  public BoxChart getChart() {

    // Create Chart
    BoxChart chart =
        new BoxChartBuilder()
            .width(600)
            .height(450)
            .title("Y Axis Logarithmic-box plot demo")
            .xAxisTitle("X")
            .yAxisTitle("Y")
            .theme(ChartTheme.XChart)
            .build();

    // Customize Chart
    chart.getStyler().setToolTipsEnabled(true);
    chart.getStyler().setYAxisLogarithmic(true);

    // Series
    chart.addSeries("aaa", Arrays.asList(10, 40, 80, 120, 350));
    return chart;
  }

  @Override
  public String getExampleChartName() {

    return getClass().getSimpleName() + " - Logarithmic Y-Axis";
  }
}
