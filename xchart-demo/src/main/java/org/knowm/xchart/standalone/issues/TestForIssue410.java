package org.knowm.xchart.standalone.issues;

import java.util.Arrays;
import org.knowm.xchart.BoxChart;
import org.knowm.xchart.BoxChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.Styler.ChartTheme;

public class TestForIssue410 {

  public static void main(String[] args) {

    // Create Chart
    BoxChart chart =
        new BoxChartBuilder().title("TestForIssue410").theme(ChartTheme.GGPlot2).build();

    chart.getStyler().setToolTipsEnabled(true);

    // Series
    chart.addSeries("boxOne", Arrays.asList(1000, 5000, 60000));
    new SwingWrapper<BoxChart>(chart).displayChart();
  }
}
