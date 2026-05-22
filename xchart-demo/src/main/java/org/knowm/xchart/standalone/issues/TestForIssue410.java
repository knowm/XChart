package org.knowm.xchart.standalone.issues;

import java.util.Arrays;
import org.knowm.xchart.BoxChart;
import org.knowm.xchart.BoxChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.Styler.ChartTheme;

public class TestForIssue410 {

  public static BoxChart getChart() {


    // Create Chart
    BoxChart chart =
        new BoxChartBuilder().title("TestForIssue410").theme(ChartTheme.GGPlot2).build();

    chart.addSeries("boxOne", Arrays.asList(1000, 5000, 60000));
    return chart;
  }

  public static void main(String[] args) {
    BoxChart chart = getChart();
    SwingWrapper<BoxChart> sw = new SwingWrapper<>(chart);
    sw.displayChart();
    sw.getXChartPanel().setToolTipsEnabled(true);
  }
}
