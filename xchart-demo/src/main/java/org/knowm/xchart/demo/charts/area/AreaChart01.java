package org.knowm.xchart.demo.charts.area;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler.LegendPosition;

/**
 * Area Chart with 3 series
 *
 * <p>Demonstrates the following:
 *
 * <ul>
 *   <li>Area Chart
 *   <li>Place legend at Inside-NE position
 *   <li>ChartBuilder
 */
public class AreaChart01 implements ExampleChart<XYChart> {

  public static void main(String[] args) {

    ExampleChart<XYChart> exampleChart = new AreaChart01();
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
            .title(getClass().getSimpleName())
            .xAxisTitle("X")
            .yAxisTitle("Y")
            .build();

    // Customize Chart
    chart.getStyler().setLegendPosition(LegendPosition.InsideNE);
    chart.getStyler().setAxisTitlesVisible(false);
    chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Area);

    // Series
    chart.addSeries("a", new double[] {0, 3, 5, 7, 9}, new double[] {-3, 5, 9, 6, 5});
    chart.addSeries("b", new double[] {0, 2, 4, 6, 9}, new double[] {-1, 6, 4, 0, 4});
    chart.addSeries("c", new double[] {0, 1, 3, 8, 9}, new double[] {-2, -1, 1, 0, 1});

    return chart;
  }
}
