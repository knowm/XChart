package org.knowm.xchart.standalone.issues;

import java.util.ArrayList;
import java.util.List;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.style.Styler.LegendPosition;

public class TestForIssue315 {

  static final int WIDTH = 465;
  static final int HEIGHT = 320;

  static XYChart getChart(boolean group0Enabled, boolean group1Enabled) {

    double[] xData = new double[] { 0.0, 1.0, 2.0 };
    double[] yData = new double[] { 2.0, 1.0, 0.0 };
    double[] yData2 = new double[] { 10.0, 20.0, 15.0 };
    XYChart chart = new XYChart(500, 200);
    XYSeries xySeries = chart.addSeries("Series 0", xData, yData);
    xySeries.setYAxisGroup(0);
    xySeries.setEnabled(group0Enabled);

    XYSeries xySeries2 = chart.addSeries("Series 1", xData, yData2);
    xySeries2.setYAxisGroup(1);
    xySeries2.setEnabled(group1Enabled);
    chart.getStyler().setLegendPosition(LegendPosition.OutsideS);
    return chart;
  }

  public static void main(String[] args) {

    List<Chart> charts = new ArrayList<Chart>();
    boolean[] options = { true, false };
    for (boolean g0 : options) {
      for (boolean g1 : options) {
        XYChart chart = getChart(g0, g1);
        chart.setTitle("Series 0 " + (g0 ? "enabled" : "disabled") + " - Series 1 " + (g1 ? "enabled" : "disabled"));
        charts.add(chart);
      }
    }

    new SwingWrapper(charts).displayChartMatrix();
  }

}
