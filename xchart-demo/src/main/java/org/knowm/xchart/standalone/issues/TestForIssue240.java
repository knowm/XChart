package org.knowm.xchart.standalone.issues;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;

public class TestForIssue240 {

  private static final int WIDTH = 600;
  private static final int HEIGHT = 500;

  private static double[] xData = new double[] { 1, 2, 3, 6, 7, 9, 12, 15, 17, 20 };
  private static double[] yData = new double[] { -5.6, 15, 36, 27, 89, 74, 25, 16, 14, 46 };

  public static void main(String[] args) throws IOException {

    List<XYChart> charts = new ArrayList<>();
    charts.add(getLineChart());
    charts.add(getSmoothedLineChart());
    charts.add(getSmoothedAreaChart());
    charts.add(getLineAndSmoothedAreaChart());
    // show
    new SwingWrapper<>(charts).displayChartMatrix();
  }

  private static XYChart getLineChart() {
    // Create Chart
    XYChart chart =
        new XYChartBuilder()
            .width(WIDTH)
            .height(HEIGHT)
            .title("Line Chart")
            .xAxisTitle("X")
            .yAxisTitle("Y")
            .build();    

    chart.getStyler().setLegendVisible(false);

    chart.addSeries("line", xData,yData);
    return chart;
  }

  private static XYChart getSmoothedLineChart() {
    // Create Chart
    XYChart chart =
        new XYChartBuilder()
            .width(WIDTH)
            .height(HEIGHT)
            .title("Smoothed Line Chart")
            .xAxisTitle("X")
            .yAxisTitle("Y")
            .build();

    chart.getStyler().setLegendVisible(false);

    XYSeries series = chart.addSeries("smoothed line", xData, yData);
    series.setSmooth(true);
    return chart;
  }

  private static XYChart getSmoothedAreaChart() {
    // Create Chart
    XYChart chart =
        new XYChartBuilder()
            .width(WIDTH)
            .height(HEIGHT)
            .title("Smoothed Area Chart")
            .xAxisTitle("X")
            .yAxisTitle("Y")
            .build();

    chart.getStyler().setLegendVisible(false);

    XYSeries series = chart.addSeries("smoothed area", xData, yData);
    series.setSmooth(true);
    series.setXYSeriesRenderStyle(XYSeriesRenderStyle.Area);
    return chart;
  }

  private static XYChart getLineAndSmoothedAreaChart() {
    // Create Chart
    XYChart chart =
        new XYChartBuilder()
            .width(WIDTH)
            .height(HEIGHT)
            .title("Line And Smoothed Area Chart")
            .xAxisTitle("X")
            .yAxisTitle("Y")
            .build();

    chart.getStyler().setLegendVisible(true);

    XYSeries series1 = chart.addSeries("smoothed area", xData,
        new double[] { 10,2.5,5.6,7.8,Double.NaN ,-17,58,39,Double.NaN ,20});
    series1.setSmooth(true);
    series1.setXYSeriesRenderStyle(XYSeriesRenderStyle.Area);

    XYSeries series2 = chart.addSeries("line", xData,
        yData);
    series2.setSmooth(false);
    return chart;
  }
}
