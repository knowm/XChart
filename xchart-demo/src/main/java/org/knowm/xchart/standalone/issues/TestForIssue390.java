package org.knowm.xchart.standalone.issues;

import java.io.IOException;
import java.util.Random;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.style.Styler.CardinalPosition;

public class TestForIssue390 {

  public static void main(String[] args) throws IOException {

    Random rand = new Random();

    double min = 0;
    double max = 20;
    int nbServices = 20;
    // int nbExpress = 1;
    int nbInstances = 50;

    long s = 24;
    rand.setSeed(s);
    /*double[][] coordinates = new double[nbServices][2];

    for (int i = 0 ; i < coordinates.length; i++) {
    }*/

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
    chart.getStyler().setLegendPosition(CardinalPosition.InsideSW);
    chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Line);
    // chart.getStyler().setYAxisMax(2.0);

    /*XYDataset dataset = XYDataset.createDataset();
    ChartFactory.createXYLineChart("Test", "X", "Y", dataset);*/

    double[] xData = new double[nbServices];
    for (int i = 0; i < xData.length; i++) {
      xData[i] = (double) (i + 1) / nbServices;
    }

    double[][] results = new double[3][nbServices];

    for (int t = 0; t < 3; t++) {
      System.out.print("Simulations pour t=" + (t + 1) * 0.25 + "\n");

      for (int n = 1; n < nbServices + 1; n++) {
        results[t][n - 1] = 1.2;
        // System.out.print(results[n-1] + "\n");
      }

      chart.addSeries(Double.toString(t), xData, results[t]);
      /*System.out.print(xData.length + "\n");
      System.out.print(results.length + "\n");*/
      System.out.print(results[t][0] + " " + results[t][nbServices - 1] + "\n");
    }

    // Show it
    new SwingWrapper(chart).displayChart();

    BitmapEncoder.saveBitmap(chart, "./Sample_Chart", BitmapFormat.PNG);
  }
}
