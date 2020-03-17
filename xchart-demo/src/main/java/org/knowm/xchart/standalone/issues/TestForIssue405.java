package org.knowm.xchart.standalone.issues;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.demo.charts.date.DateChart03;
import org.knowm.xchart.demo.charts.line.LineChart03;

public class TestForIssue405 {

  public static void main(String[] args) {

    List<XYChart> charts = new ArrayList<>();

    XYChart chart1 = new LineChart03().getChart();
    chart1.setTitle("Set XY axis title font color");
    chart1.getStyler().setXAxisTitleColor(Color.RED);
    chart1.getStyler().setYAxisTitleColor(Color.GREEN);
    charts.add(chart1);

    XYChart chart2 = new DateChart03().getChart();
    chart2.setTitle("Set multiple Y-axis title font colors");
    chart2.setYAxisGroupTitle(1, "Y1");
    chart2.setYAxisGroupTitle(0, "Y2");
    chart2.getStyler().setYAxisGroupTitleColor(1, chart2.getStyler().getSeriesColors()[0]);
    chart2.getStyler().setYAxisGroupTitleColor(0, chart2.getStyler().getSeriesColors()[1]);
    charts.add(chart2);
    new SwingWrapper<XYChart>(charts).displayChartMatrix();
  }
}
