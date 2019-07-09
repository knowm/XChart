package org.knowm.xchart.standalone.issues;

import java.util.ArrayList;
import java.util.List;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.internal.chartpart.Chart;

public class TestForIssue325 {

  static final int WIDTH = 465;
  static final int HEIGHT = 320;

  static XYChart getChart(int multiple, int yAxisLeftWidth) {

    int size = 30;
    List<Integer> xData = new ArrayList<Integer>(size);
    List<Double> yData = new ArrayList<Double>();
    for (int i = 0; i <= size; i++) {
      double radians = (Math.PI / (size / 2) * i) ;
      xData.add(i * multiple);
      yData.add(Math.sin(radians) * multiple);
    }

    XYChart chart = new XYChart(WIDTH, HEIGHT);
    chart.addSeries("Series 0", xData, yData);

    chart.getStyler().setLegendVisible(false);
    chart.getStyler().setYAxisLeftWidthHint(yAxisLeftWidth);
    return chart;
  }

  public static void main(String[] args) {

    List<Chart> charts = new ArrayList<Chart>();
    int[] multiples = { 1, 1000 };
    int[] widths = { 0, 15, 55 };
    for (int m : multiples) {
      for (int width : widths) {
        XYChart chart = getChart(m, width);
        chart.setTitle("Multiple: " + m + " width: " + width);
        charts.add(chart);
      }
    }

    new SwingWrapper(charts, charts.size() / widths.length, widths.length).displayChartMatrix();
    
    try {
      // wait frame to appear
      Thread.sleep(500);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    for (int i = 0; i < charts.size(); i++) {
      System.out.printf("%-30s on screen width: %5.2f%n", charts.get(i).getTitle(),  charts.get(i).getYAxisLeftWidth());
    }
  }

}
