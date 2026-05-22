package org.knowm.xchart.standalone.issues;

import java.io.IOException;
import java.util.Random;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.style.Styler.LegendPosition;

public class TestForIssue390 {

  public static XYChart getChart() {

    Random rand = new Random();

    double min = 0;
    double max = 20;
    int nbServices = 20;
    int nbInstances = 50;

    long s = 24;
    rand.setSeed(s);

    final XYChart chart =
        new XYChartBuilder()
            .width(600)
            .height(400)
            .title("Augmentation du cout induit par livraison express")
            .xAxisTitle("Proportion de colis express")
            .yAxisTitle("Rapport cout/cout_opt")
            .build();

    // Customize Chart
    chart.getStyler().setLegendVisible(true);
    chart.getStyler().setLegendPosition(LegendPosition.InsideSW);
    chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Line);

    double[] xData = new double[nbServices];
    for (int i = 0; i < xData.length; i++) {
      xData[i] = (double) (i + 1) / nbServices;
    }

    double[][] results = new double[3][nbServices];

    for (int t = 0; t < 3; t++) {
      for (int n = 1; n < nbServices + 1; n++) {
        results[t][n - 1] = 1.2;
      }
      chart.addSeries(Double.toString(t), xData, results[t]);
    }

    return chart;
  }

  public static void main(String[] args) throws IOException {

    XYChart chart = getChart();
    new SwingWrapper<>(chart).displayChart();
    BitmapEncoder.saveBitmap(chart, "./Sample_Chart", BitmapFormat.PNG);
  }
}