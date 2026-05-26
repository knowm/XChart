package org.knowm.xchart.standalone.issues;

import java.util.Arrays;
import org.knowm.xchart.BoxChart;
import org.knowm.xchart.BoxChartBuilder;
import org.knowm.xchart.SwingWrapper;

/**
 * Reproducer for https://github.com/knowm/XChart/issues/890
 *
 * <p>Box chart gets stuck when only one yData value is provided.
 */
public class TestForIssue890 {

  public static void main(String[] args) {

    new SwingWrapper<>(getChart()).displayChart();
  }

  /** Constructs and returns the chart without launching a window (headless-safe). */
  public static BoxChart getChart() {

    BoxChart chart =
        new BoxChartBuilder()
            .title("Box plot – single data point (issue 890)")
            .xAxisTitle("X")
            .yAxisTitle("Y")
            .build();

    chart.addSeries("test", Arrays.asList(1));

    return chart;
  }
}
