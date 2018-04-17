package org.knowm.xchart.demo.charts.theme;

import java.util.ArrayList;
import java.util.List;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.demo.charts.ExampleChart;

/**
 * My Custom Theme
 *
 * <p>Demonstrates the following:
 *
 * <ul>
 *   <li>Using a custom class that implements Theme
 */
public class ThemeChart04 implements ExampleChart<XYChart> {

  public static void main(String[] args) {

    ExampleChart<XYChart> exampleChart = new ThemeChart04();
    XYChart chart = exampleChart.getChart();
    new SwingWrapper<XYChart>(chart).displayChart();
  }

  @Override
  public XYChart getChart() {

    // Create Chart
    XYChart chart =
        new XYChartBuilder()
            .width(800)
            .height(600)
            .title("My Custom Theme")
            .xAxisTitle("X")
            .yAxisTitle("Y")
            .build();
    chart.getStyler().setTheme(new MyCustomTheme());

    // Customize Chart
    chart.getStyler().setMarkerSize(11);

    // Series
    for (int i = 1; i <= 3; i++) {

      // generates circle data
      double x, y;
      List<Number> xData = new ArrayList<Number>();
      List<Number> yData = new ArrayList<Number>();

      for (int j = 0; j < 360; j = j + 5) {

        x = (double) i * Math.cos(Math.toRadians(j));
        y = (double) i * Math.sin(Math.toRadians(j));
        xData.add(x);
        yData.add(y);
      }

      String seriesName = "r=" + i;
      chart.addSeries(seriesName, xData, yData);
    }
    return chart;
  }
}
