package org.knowm.xchart.standalone.issues;

import java.util.Arrays;
import java.util.List;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

/**
 * Demonstrates that a very long series name is truncated in the legend rather than breaking the
 * chart layout.
 *
 * @see <a href="https://github.com/knowm/XChart/issues/686">Issue #686</a>
 */
public class TestForIssue686 {

  public static void main(String[] args) {
    new SwingWrapper<>(getChart()).displayChart();
  }

  public static XYChart getChart() {
    XYChart chart =
        new XYChartBuilder()
            .width(600)
            .height(400)
            .title("Issue 686 - Long Series Name")
            .build();

    List<Double> xData = Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0);
    List<Double> yData = Arrays.asList(2.0, 4.0, 3.0, 5.0, 4.0);

    String veryLongName =
        "This is an extremely long series name that used to break the chart layout by pushing the plot area to zero width";
    chart.addSeries(veryLongName, xData, yData);

    return chart;
  }
}
