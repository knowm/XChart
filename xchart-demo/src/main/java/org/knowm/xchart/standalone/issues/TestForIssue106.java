package org.knowm.xchart.standalone.issues;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.demo.charts.area.AreaChart03;
import org.knowm.xchart.ToolTipType;

public class TestForIssue106 {

  public static void main(String[] args) {

    ExampleChart<XYChart> alc = new AreaChart03();
    List<XYChart> charts = new ArrayList<XYChart>();
    {
      XYChart chart = alc.getChart();
      chart.setTitle("Default data labels");
      charts.add(chart);
    }
    {
      XYChart chart = alc.getChart();
      chart.setTitle("No data label");
      charts.add(chart);
    }
    {
      // current default
      XYChart chart = alc.getChart();
      chart.getStyler().setToolTipBackgroundColor(Color.RED);
      chart.getStyler().setToolTipType(ToolTipType.yLabels);
      chart.setTitle("Red background");
      charts.add(chart);
    }
    {
      XYChart chart = alc.getChart();
      chart.getStyler().setToolTipBorderColor(Color.BLUE);
      chart.getStyler().setToolTipFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));
      chart.setTitle("Blue and custom Font");
      charts.add(chart);
    }

    SwingWrapper<XYChart> wrapper = new SwingWrapper<XYChart>(charts);
    wrapper.displayChartMatrix();
    wrapper.getXChartPanel(0).setToolTipsEnabled(true);
    wrapper.getXChartPanel(2).setToolTipsEnabled(true);
    wrapper.getXChartPanel(3).setToolTipsEnabled(true);
  }
}
