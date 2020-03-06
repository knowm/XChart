package org.knowm.xchart.demo.charts.box;

import java.util.Arrays;
import org.knowm.xchart.BoxChart;
import org.knowm.xchart.BoxChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.BoxPlotStyler.BoxplotCalCulationMethod;
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
            .xAxisTitle("X")
            .yAxisTitle("Y")
            .theme(ChartTheme.Matlab)
            .build();

    // Customize Chart
    chart.getStyler().setBoxplotCalCulationMethod(BoxplotCalCulationMethod.N_LESS_1_PLUS_1);

    // Series
    chart.addSeries("aaa", Arrays.asList(1, 2, 3, 4, 5, 6));
    chart.addSeries("bbb", Arrays.asList(1, 2, 3, 4, 5, 6, 17));
    chart.addSeries("ccc", Arrays.asList(-10, -8, 1, 2, 3, 4, 5, 6, 7, 8, 9, 20, 21));
    chart.getStyler().setShowWithinAreaPoint(true);
    chart.getStyler().setToolTipsEnabled(true);
    return chart;
  }
}
