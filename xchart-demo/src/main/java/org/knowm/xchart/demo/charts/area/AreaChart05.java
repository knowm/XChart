package org.knowm.xchart.demo.charts.area;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler.LegendPosition;

/**
 * Area Chart with 1 series
 *
 * <p>Demonstrates the following:
 *
 * <ul>
 *   <li>Polygon Area Chart
 *   <li>Place legend at Inside-NE position
 *   <li>ChartBuilder
 */
public class AreaChart05 implements ExampleChart<XYChart> {

  public static void main(String[] args) {

    ExampleChart<XYChart> exampleChart = new AreaChart05();
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
    chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.PolygonArea);

    // Series
    chart.addSeries(
        "a",
        new double[] {
          0, 3, 5, 7, 9, // x coordinates ascending
          9, 7, 5, 3, 0
        }, // x coordinates descending
        new double[] {
          -1, 6, 9, 6, 5, // upper y
          4, 0, 4, 5, -3
        }); // lower y

    return chart;
  }
}

