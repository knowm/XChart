package org.knowm.xchart.standalone;

import org.knowm.xchart.ChartEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.markers.SeriesMarkers;

/**
 * Generates a WebP sample chart ({@code ./Sample_Chart.webp}) in the project root.
 *
 * <p>WebP is not built into the JDK. This example works because the demo module declares an ImageIO
 * WebP writer plugin ({@code com.github.usefulness:webp-imageio}, which bundles native binaries for
 * Windows/Linux/macOS including Apple Silicon). With any such plugin on the classpath, {@link ChartEncoder} needs no
 * special-casing: it simply hands the {@code "webp"} format name to {@code javax.imageio.ImageIO}.
 *
 * <p>To enable WebP export in your own project, add a WebP ImageIO plugin to your build; no XChart
 * change is required.
 */
public class ExampleWebP {

  public static void main(String[] args) throws Exception {

    XYChart chart = buildSampleChart();
    ChartEncoder.saveChart(chart, "./Sample_Chart", "webp");
    System.out.println("Wrote ./Sample_Chart.webp");
  }

  static XYChart buildSampleChart() {

    XYChart chart = new XYChart(500, 400);
    chart.setTitle("Sample Chart");
    chart.setXAxisTitle("X");
    chart.setYAxisTitle("Y");
    XYSeries series = chart.addSeries("y(x)", null, new double[] {2.0, 1.0, 0.0});
    series.setMarker(SeriesMarkers.CIRCLE);
    return chart;
  }
}
