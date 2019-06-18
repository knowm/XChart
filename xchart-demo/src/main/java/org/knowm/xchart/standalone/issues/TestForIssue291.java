package org.knowm.xchart.standalone.issues;

import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.knowm.xchart.style.Styler.YAxisPosition;

/**
 *
 * @author timmolter
 */
public class TestForIssue291 {

  static final int WIDTH = 465;
  static final int HEIGHT = 320;

  public static void main(String[] args) {

    final XYChart chart = getLineChart();
    chart.setTitle("sin(x) on second axis with title");
    String seriesName = "y=sin(x)";
    XYSeries series = (XYSeries) chart.getSeriesMap().get(seriesName);
    series.setYAxisGroup(1);
    chart.getStyler().setYAxisGroupPosition(1, YAxisPosition.Left);
    chart.getStyler().setYAxisGroupPosition(0, YAxisPosition.Right);
    chart.setYAxisGroupTitle(1, "sin(x)");

    final SwingWrapper<Chart> sw = new SwingWrapper<Chart>(chart);

    sw.displayChart();

    boolean seriesShown = true;
    while (true) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      if (seriesShown) {
        chart.removeSeries(seriesName);
      } else {
        double[] xData = series.getXData();
        double[] yData = series.getYData();
        chart.addSeries(seriesName, xData, yData);
      }
      seriesShown = !seriesShown;
      try {
        SwingUtilities.invokeAndWait(
            new Runnable() {
              public void run() {
                sw.repaintChart();
              }
            });
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  static XYChart getLineChart() {

    XYChart chart =
        new XYChartBuilder().width(WIDTH).height(HEIGHT).xAxisTitle("X").yAxisTitle("Y").build();

    // Customize Chart
    chart.getStyler().setToolTipsEnabled(true);
    chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
    // generates sine data
    int size = 30;
    List<Integer> xData = new ArrayList<Integer>();
    List<Double> yData = new ArrayList<Double>();
    List<Integer> xData2 = new ArrayList<Integer>();
    List<Double> yData2 = new ArrayList<Double>();
    for (int i = 0; i <= size; i++) {
      double radians = (Math.PI / (size / 2) * i);
      int x = i - size / 2;
      xData.add(x);
      yData.add(-1 * Math.sin(radians));
      xData2.add(x);
      yData2.add(-10 * Math.cos(radians));
    }

    // Series
    chart.addSeries("y=sin(x)", xData, yData);
    chart.addSeries("y=cos(x)", xData2, yData2);
    return chart;
  }
}
