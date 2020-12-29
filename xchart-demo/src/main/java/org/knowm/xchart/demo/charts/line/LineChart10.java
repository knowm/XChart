package org.knowm.xchart.demo.charts.line;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.internal.chartpart.AnnotationImage;
import org.knowm.xchart.internal.chartpart.AnnotationLine;
import org.knowm.xchart.internal.chartpart.AnnotationText;
import org.knowm.xchart.style.markers.None;
/**
 * Annotations
 *
 * <p>Demonstrates the following:
 *
 * <ul>
 *   <li>Line Annotation
 *   <li>Text Annotation
 *   <li>Image Annotation
 */
public class LineChart10 implements ExampleChart<XYChart> {

  public static void main(String[] args)  {

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

    BasicStroke stroke =
        new BasicStroke(
            2,
            BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_BEVEL,
            10.0f,
            new float[] {3.0f, 0.0f},
            0.0f);


    // draw a horizontal line at series max point
    AnnotationLine maxY = new AnnotationLine(series.getYMax(), false, false);
    maxY.setColor(Color.GREEN);
    maxY.setStroke(new BasicStroke(3.0f));
    chart.addAnnotation(maxY);


    // draw a horizontal line at series min point
    AnnotationLine minY = new AnnotationLine(series.getYMin(), false, false);
    minY.setColor(Color.RED);
    chart.addAnnotation(minY);

    // draw a vertical line at 0.45
    AnnotationLine xLine = new AnnotationLine(0.45, true, false);
    xLine.setColor(Color.MAGENTA);
    xLine.setStroke(stroke);
    chart.addAnnotation(xLine);


    // draw a vertical line at 100 pixels
    AnnotationLine xLinePixel = new AnnotationLine(100, true, true);
    xLinePixel.setColor(Color.BLACK);
    xLinePixel.setStroke(stroke);
    chart.addAnnotation(xLinePixel);

    // add text near to max line
    AnnotationText maxText = new AnnotationText("Max", 0.0, series.getYMax(), false);
    chart.addAnnotation(maxText);

    BufferedImage image  = null;
    try {
      image = ImageIO.read(getClass().getResource("/XChart_64_64.png"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    AnnotationImage annotationImage = new AnnotationImage(image, 0, 1, false);
    chart.addAnnotation(annotationImage);


    return chart;
  }

  @Override
  public String getExampleChartName() {

    return getClass().getSimpleName() + " - Annotations";
  }
}
