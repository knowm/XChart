package org.knowm.xchart.standalone.readme;

import java.io.IOException;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

/** @author timmolter */
public class SimplestExample {

  public static void main(String[] args) throws IOException {

    double[] xData = new double[] {0.0, 1.0, 2.0};
    double[] yData = new double[] {2.0, 1.0, 0.0};

    // Create Chart
    XYChart chart = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", xData, yData);

    // Show it
    new SwingWrapper<XYChart>(chart).displayChart();

    // Save it
    BitmapEncoder.saveBitmap(chart, "./Sample_Chart", BitmapFormat.PNG);

    // or save it in high-res
    BitmapEncoder.saveBitmapWithDPI(chart, "./Sample_Chart_300_DPI", BitmapFormat.PNG, 300);
  }
}
