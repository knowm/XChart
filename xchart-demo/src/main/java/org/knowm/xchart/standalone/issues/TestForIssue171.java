package org.knowm.xchart.standalone.issues;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.CategorySeries;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.Styler;

/**
 * Demonstrates that x-axis labels no longer overlap when there are many categories.
 *
 * <p>Reproduces the 161-category histogram from issue #171. The default xAxisTickMarkSpacingHint
 * now automatically skips labels on category charts so they don't overlap.
 */
public class TestForIssue171 {

  public static void main(String[] args) {
    new SwingWrapper<>(getChart()).displayChart();
  }

  /** Constructs and returns the chart without launching a window (headless-safe). */
  public static CategoryChart getChart() {
    int n = 80;
    int nbRandomWalks = 1_000_000;
    Random generator = new Random(42);

    int[] histogramY = new int[2 * n + 1];
    for (int j = 0; j < nbRandomWalks; j++) {
      int delta = 0;
      for (int i = 0; i < n; i++) {
        delta += generator.nextInt(2) < 1 ? -1 : 1;
      }
      histogramY[delta + n] += 1;
    }

    List<Integer> xData = new ArrayList<>();
    List<Integer> yData = new ArrayList<>();
    for (int i = 0; i < 2 * n + 1; i++) {
      xData.add(i - n);
      yData.add(histogramY[i]);
    }

    CategoryChart chart =
        new CategoryChartBuilder()
            .width(800)
            .height(600)
            .title("Random Walk Distribution (Issue #171)")
            .xAxisTitle("delta")
            .yAxisTitle("occurrences")
            .build();

    chart.getStyler().setChartTitleVisible(true);
    chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
    chart.getStyler().setLegendVisible(false);
    chart.getStyler().setDefaultSeriesRenderStyle(CategorySeries.CategorySeriesRenderStyle.Stick);

    chart.addSeries("data", xData, yData);

    return chart;
  }
}
