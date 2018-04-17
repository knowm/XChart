package org.knowm.xchart.standalone.issues;

import java.util.Arrays;
import java.util.List;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.internal.chartpart.Chart;

/** Creates a list of Charts and saves it as a PNG file. */
public class TestForIssue1 {

  public static void main(String[] args) throws Exception {

    List<Chart> charts =
        Arrays.asList(
            new Chart[] {
              createChart("chart1", new double[] {2.0, 1.0, 0.0}),
              createChart("chart2", new double[] {3.0, 4.0, 0.0}),
              createChart("chart3", new double[] {4.0, 1.5, 0.0}),
              createChart("chart4", new double[] {2.0, 3.0, 0.0}),
              createChart("chart5", new double[] {4.0, 1.0, 0.0}),
              createChart("chart6", new double[] {5.0, 2.0, 0.0})
            });

    BitmapEncoder.saveBitmap(charts, 2, 3, "./Sample_Charts", BitmapFormat.PNG);
  }

  private static XYChart createChart(String title, double[] yData) {
    XYChart chart = new XYChart(300, 200);
    chart.setTitle(title);
    chart.setXAxisTitle("X");
    chart.setXAxisTitle("Y");
    chart.addSeries("y(x)", null, yData);
    return chart;
  }
}
