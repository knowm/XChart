package org.knowm.xchart.standalone.issues;

import java.awt.BasicStroke;
import java.text.ParseException;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

public class TestForIssue628 {

  public static void main(String[] args) throws ParseException {

    XYChart chart = getCategoryChart();
    new SwingWrapper(chart).displayChart();
  }

  public static XYChart getCategoryChart() {

    double[] xData = new double[] {0.0, 1.0, 2.0, 3.0, 4.0};
    double[] yData = new double[] {1.0, 2, 3.0, 4, 2.5};
    double[] yData2 = new double[] {2.2, 2, 1.2, 3.1, 2.7};

    // Create Chart
    XYChart chart = new XYChartBuilder().width(400).height(600).build();
    chart
        .getStyler()
        .setPlotGridLinesStroke(
            new BasicStroke(
                (float) 3.125,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_BEVEL,
                10,
                new float[] {6.25f, 6.25f},
                0));
    chart.addSeries("y(x)", xData, yData);
    chart.addSeries("y2(x)", xData, yData2);

    return chart;
  }
}
