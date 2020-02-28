package org.knowm.xchart.demo.charts.box;

import java.util.Arrays;
import org.knowm.xchart.BoxChart;
import org.knowm.xchart.BoxChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler.ChartTheme;

/*
 * Box Chart with 3 series
 */
public class BoxChart01 implements ExampleChart<BoxChart> {

  public static void main(String[] args) {
    ExampleChart<BoxChart> exampleChart = new BoxChart01();
    BoxChart chart = exampleChart.getChart();
    new SwingWrapper<BoxChart>(chart).displayChart();
  }

  @Override
  public BoxChart getChart() {

    // Create Chart
    BoxChart chart =
        new BoxChartBuilder()
            .title("box plot demo")
            .xAxisTitle("X")
            .yAxisTitle("Y")
            .theme(ChartTheme.GGPlot2)
            .build();

    // Series
    chart.addSeries("aaa", Arrays.asList(40, 30, 20, 60, 50));
    chart.addSeries("bbb", Arrays.asList(-20, -10, -30, -15, -25));
    chart.addSeries("ccc", Arrays.asList(50, -20));
    return chart;
  }
}
