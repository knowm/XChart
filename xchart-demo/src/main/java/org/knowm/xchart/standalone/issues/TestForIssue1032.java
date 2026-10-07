package org.knowm.xchart.standalone.issues;

import java.util.List;
import org.knowm.xchart.BoxChart;
import org.knowm.xchart.BoxChartBuilder;
import org.knowm.xchart.BoxSeries;
import org.knowm.xchart.SwingWrapper;

public class TestForIssue1032 {

  public static void main(String[] args) {
    new SwingWrapper<>(getChart()).displayChart();
  }

  /** Constructs a box chart using null-free immutable lists without launching a window. */
  public static BoxChart getChart() {
    BoxChart chart =
        new BoxChartBuilder().width(640).height(480).title("Immutable box data").build();
    chart.addSeries("Added", List.of(1, 2, 3));
    BoxSeries updated = chart.addSeries("Updated", List.of(2, 3));
    updated.replaceData(List.of(3, 4));
    chart.updateBoxSeries("Updated", List.of(4, 5, 6));
    return chart;
  }
}
