package org.knowm.xchart.standalone.prs;

import java.util.function.Function;

import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.PieSeries;
import org.knowm.xchart.SwingWrapper;

/**
 * Demonstrates PR #847 — custom label generator for pie charts.
 *
 * <p>PR: https://github.com/knowm/XChart/pull/847
 *
 * <p>Before this PR the only way to customise slice labels was the built-in {@code LabelType} enum.
 * This PR adds {@code PieStyler.setCustomSeriesLabelFunction(Function<PieSeries, String>)} so
 * callers can produce any label text they like.
 *
 * <p>This demo shows three common use-cases side-by-side via three separate charts:
 *
 * <ol>
 *   <li>Default behaviour — no custom function (LabelType.NameAndValue).
 *   <li>Custom function — percentage of total formatted to two decimal places.
 *   <li>Custom function — name and value combined with a slash separator, e.g. "Gold / 24".
 * </ol>
 */
public class TestForPR847 {

  public static void main(String[] args) {

    new SwingWrapper<>(getChartDefault()).displayChart();
    new SwingWrapper<>(getChartCustomPercentage()).displayChart();
    new SwingWrapper<>(getChartCustomNameAndValue()).displayChart();
  }

  /** Chart 1: default labels — no custom function. */
  public static PieChart getChartDefault() {

    PieChart chart =
        new PieChartBuilder()
            .width(500)
            .height(400)
            .title("PR #847 – Default (LabelType.NameAndValue)")
            .build();

    addSlices(chart);
    return chart;
  }

  /**
   * Chart 2: custom function that shows each slice's percentage of the total, e.g. "42.00%".
   *
   * <p>The function receives the {@link PieSeries} so it has access to {@code getValue()} and
   * {@code getName()}. The caller is responsible for computing any aggregate (total) before passing
   * the function in.
   */
  public static PieChart getChartCustomPercentage() {

    PieChart chart =
        new PieChartBuilder()
            .width(500)
            .height(400)
            .title("PR #847 – Custom: percentage of total")
            .build();

    addSlices(chart);

    double total =
        chart.getSeriesMap().values().stream()
            .filter(s -> s.getValue() != null)
            .mapToDouble(s -> s.getValue().doubleValue())
            .sum();

    Function<PieSeries, String> percentageLabel =
        series -> String.format("%.2f%%", series.getValue().doubleValue() / total * 100);

    chart.getStyler().setCustomSeriesLabelFunction(percentageLabel);
    return chart;
  }

  /** Chart 3: custom function showing name and value separated by a slash, e.g. "Gold / 24". */
  public static PieChart getChartCustomNameAndValue() {

    PieChart chart =
        new PieChartBuilder()
            .width(500)
            .height(400)
            .title("PR #847 – Custom: name + value")
            .build();

    addSlices(chart);

    Function<PieSeries, String> multiLineLabel =
        series -> series.getName() + " / " + series.getValue();

    chart.getStyler().setCustomSeriesLabelFunction(multiLineLabel);
    return chart;
  }

  // ── shared data ──────────────────────────────────────────────────────────────

  private static void addSlices(PieChart chart) {

    chart.addSeries("Gold", 24);
    chart.addSeries("Silver", 21);
    chart.addSeries("Platinum", 39);
    chart.addSeries("Copper", 17);
    chart.addSeries("Zinc", 40);
  }
}
