package org.knowm.xchart.demo.charts.boxPlot;

import java.util.Arrays;
import org.knowm.xchart.BoxChart;
import org.knowm.xchart.BoxChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler.ChartTheme;

/*
 * Box Plot with 3 series
 * plot data points
 */
public class BoxChart02 implements ExampleChart<BoxChart> {

  public static void main(String[] args) {
    ExampleChart<BoxChart> exampleChart = new BoxChart02();
    BoxChart chart = exampleChart.getChart();
    new SwingWrapper<BoxChart>(chart).displayChart();
  }

  @Override
  public BoxChart getChart() {

    // Create Chart
    BoxChart chart =
        new BoxChartBuilder()
            .title("box plot show all point")
            .xAxisTitle("Color")
            .yAxisTitle("temperature")
            .theme(ChartTheme.Matlab)
            .build();
    // Series

    chart.addSeries(
        "aaa",
        Arrays.asList(
            9634.37, 23886.43, 13828.96, 7773.08, 14959.32, 8046.95, 6547.51, 9528.85, 9241.53,
            9353.79, 8224.60, 10436.48, 10399.62, 15067.39, 8505.73, 9398.87, 11611.29, 12280.94,
            9631.96));
    chart.addSeries("bbb", Arrays.asList(7000, 8000, 9000));
    chart.addSeries("ccc", Arrays.asList(7000, 8000, null, 9000));
    chart.getStyler().setShowWithinAreaPoint(true);
    chart.getStyler().setToolTipsEnabled(true);
    return chart;
  }
}
