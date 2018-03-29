package org.knowm.xchart.demo.charts.line;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.colors.XChartSeriesColors;
import org.knowm.xchart.style.lines.SeriesLines;
import org.knowm.xchart.style.markers.SeriesMarkers;

/** Hundreds of Series on One Plot */
public class LineChart04 implements ExampleChart<XYChart> {

  public static void main(String[] args) {

    ExampleChart<XYChart> exampleChart = new LineChart04();
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
            .title("LineChart04")
            .xAxisTitle("X")
            .yAxisTitle("Y")
            .build();

    // Customize Chart
    chart.getStyler().setLegendVisible(false);

    // Series
    for (int i = 0; i < 200; i++) {
      XYSeries series =
          chart.addSeries(
              "A" + i,
              new double[] {Math.random() / 1000, Math.random() / 1000},
              new double[] {Math.random() / -1000, Math.random() / -1000});
      series.setLineColor(XChartSeriesColors.BLUE);
      series.setLineStyle(SeriesLines.SOLID);
      series.setMarker(SeriesMarkers.CIRCLE);
      series.setMarkerColor(XChartSeriesColors.BLUE);
    }

    return chart;
  }
}
