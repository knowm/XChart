package org.knowm.xchart.standalone;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.ChartEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.markers.SeriesMarkers;

/**
 * Creates a simple Chart and saves it in every supported export format via the unified {@link
 * ChartEncoder}. The raster format tiff ships with the JDK; svg/eps/pdf use the optional vector
 * dependencies. Additional raster formats (e.g. webp, avif) work automatically if a matching ImageIO
 * plugin is on the classpath.
 */
public class Example1 {

  public static void main(String[] args) throws Exception {

    double[] yData = new double[] {2.0, 1.0, 0.0};

    // Create Chart
    XYChart chart = new XYChart(500, 400);
    chart.setTitle("Sample Chart");
    chart.setXAxisTitle("X");
    chart.setYAxisTitle("Y");
    XYSeries series = chart.addSeries("y(x)", null, yData);
    series.setMarker(SeriesMarkers.CIRCLE);

    // Unified export: one method, any format name ImageIO (or a vector encoder) can write.
    ChartEncoder.saveChart(chart, "./Sample_Chart", "png");
    ChartEncoder.saveChart(chart, "./Sample_Chart", "jpg");
    ChartEncoder.saveChart(chart, "./Sample_Chart", "bmp");
    ChartEncoder.saveChart(chart, "./Sample_Chart", "gif");
    ChartEncoder.saveChart(chart, "./Sample_Chart", "tiff");

    // Vector formats (optional VectorGraphics2D / pdfbox-graphics2d dependencies).
    ChartEncoder.saveChart(chart, "./Sample_Chart", "eps");
    ChartEncoder.saveChart(chart, "./Sample_Chart", "pdf");
    ChartEncoder.saveChart(chart, "./Sample_Chart", "svg");

    // Features without a ChartEncoder equivalent still live on BitmapEncoder:
    // JPEG quality control and custom-DPI raster export.
    BitmapEncoder.saveJPGWithQuality(chart, "./Sample_Chart_With_Quality.jpg", 0.95f);
    BitmapEncoder.saveBitmapWithDPI(chart, "./Sample_Chart_300_DPI", BitmapFormat.PNG, 300);
    BitmapEncoder.saveBitmapWithDPI(chart, "./Sample_Chart_300_DPI", BitmapFormat.JPG, 300);
    BitmapEncoder.saveBitmapWithDPI(chart, "./Sample_Chart_300_DPI", BitmapFormat.GIF, 300);
  }
}
