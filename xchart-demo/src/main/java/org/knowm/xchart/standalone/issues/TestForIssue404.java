package org.knowm.xchart.standalone.issues;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.demo.charts.date.DateChart03;
import org.knowm.xchart.demo.charts.line.LineChart03;

public class TestForIssue404 {

  public static void main(String[] args) {

    List<XYChart> charts = new ArrayList<>();

    XYChart chart1 = new LineChart03().getChart();
    chart1.setTitle("Set XY axis tickLabels and tickMarks color");
    chart1.getStyler().setXAxisTitleColor(Color.RED);
    chart1.getStyler().setYAxisTitleColor(Color.GREEN);
    chart1.getStyler().setAxisTickLabelsColor(Color.PINK);
    chart1.getStyler().setXAxisTickLabelsColor(Color.BLUE);
    chart1.getStyler().setYAxisTickLabelsColor(Color.CYAN);
    chart1.getStyler().setAxisTickMarksColor(Color.ORANGE);
    chart1.getStyler().setXAxisTickMarksColor(Color.RED);
    chart1.getStyler().setYAxisTickMarksColor(Color.YELLOW);
    charts.add(chart1);

    XYChart chart2 = new DateChart03().getChart();
    chart2.setTitle("Set multiple Y-axis tickLabels and tickMarks colors");
    chart2.setYAxisGroupTitle(1, "Y1");
    chart2.setYAxisGroupTitle(0, "Y2");
    chart2.getStyler().setYAxisGroupTitleColor(1, chart2.getStyler().getSeriesColors()[0]);
    chart2.getStyler().setYAxisGroupTitleColor(0, chart2.getStyler().getSeriesColors()[1]);

    chart2.getStyler().setXAxisTickLabelsColor(Color.YELLOW);
    chart2.getStyler().setXAxisTickMarksColor(Color.BLUE);

    chart2.getStyler().setYAxisGroupTickLabelsColorMap(1, Color.CYAN);
    chart2.getStyler().setYAxisGroupTickLabelsColorMap(0, Color.RED);
    chart2.getStyler().setYAxisGroupTickMarksColorMap(1, Color.ORANGE);
    chart2.getStyler().setYAxisGroupTickMarksColorMap(0, Color.PINK);
    charts.add(chart2);
    new SwingWrapper<XYChart>(charts).displayChartMatrix();
  }
}
