package org.knowm.xchart.standalone.issues;

import java.util.ArrayList;
import java.util.List;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.SwingWrapper;

/**
 * Demonstrates that clearing all series from a chart no longer throws NoSuchElementException when
 * the chart is repainted (issue #739).
 */
public class TestForIssue739 {

  public static void main(String[] args) throws InterruptedException {

    CategoryChart chart = getChart();
    SwingWrapper<CategoryChart> sw = new SwingWrapper<>(chart);
    sw.displayChart();

    Thread.sleep(2000);

    // Clear all series — chart should render blank, not throw NoSuchElementException.
    // Copy keys first to avoid ConcurrentModificationException while removing.
    List<String> names = new ArrayList<>(chart.getSeriesMap().keySet());
    names.forEach(chart::removeSeries);
    sw.repaintChart();
  }

  /** Constructs and returns the chart without launching a window (headless-safe). */
  public static CategoryChart getChart() {

    CategoryChart chart =
        new CategoryChartBuilder().width(600).height(400).title("Issue 739").build();
    chart.addSeries("series1", new double[] {1, 2, 3}, new double[] {4, 5, 6});
    chart.addSeries("series2", new double[] {1, 2, 3}, new double[] {6, 5, 4});
    return chart;
  }
}
