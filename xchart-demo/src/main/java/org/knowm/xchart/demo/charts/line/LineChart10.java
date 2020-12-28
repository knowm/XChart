package org.knowm.xchart.demo.charts.line;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.internal.chartpart.AnnotationLine;
import org.knowm.xchart.style.markers.None;

public class LineChart10 implements ExampleChart<XYChart> {

  public static void main(String[] args) {

    ExampleChart<XYChart> exampleChart = new LineChart10();
    XYChart chart = exampleChart.getChart();
    new SwingWrapper<>(chart).displayChart();
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
    chart.getStyler().setAxisTitlesVisible(false);
    chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Line);

    double start = 0;
    double end = 1;
    double increment = 0.01;

    List<Double> xData = new ArrayList<>();
    List<Double> yData = new ArrayList<>();

    double x = start;

    while (x <= end) {

      double y = Math.exp(2 * x - (7 * x * x * x));
      xData.add(x);
      yData.add(y);
      x += increment;
    }

    // Series
    XYSeries series = chart.addSeries("series1", xData, yData);
    series.setMarker(new None());

    // draw a horizontal line at series max point
    AnnotationLine maxY = new AnnotationLine(series.getYMax(), false, false);
    maxY.setColor(Color.GREEN);
    maxY.setStroke(new BasicStroke(3.0f));
    //    maxY.init(chartPanel);
    chart.addAnnotation(maxY);

    return chart;
  }

  @Override
  public String getExampleChartName() {

    return getClass().getSimpleName() + " - Annotations";
  }
}
