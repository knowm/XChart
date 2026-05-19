package org.knowm.xchart.standalone.prs;

import java.util.Arrays;
import java.util.List;
import org.knowm.xchart.HorizontalBarChart;
import org.knowm.xchart.HorizontalBarChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.Styler.ChartTheme;
import org.knowm.xchart.style.Styler.LegendPosition;

/**
 * Demonstrates PR #866 — new HorizontalBarChart chart type.
 *
 * <p>PR: https://github.com/knowm/XChart/pull/866
 *
 * <p>This demo shows four variants:
 *
 * <ol>
 *   <li>Basic — single series, integer categories, positive values.
 *   <li>Negative values — single series, all negative values.
 *   <li>Span — multiple series with positive and negative values (zero-line shown).
 *   <li>Labels — multiple series with bar value labels enabled.
 * </ol>
 */
public class TestForPR866 {

  public static void main(String[] args) {

    new SwingWrapper<>(getChartBasic()).displayChart();
    new SwingWrapper<>(getChartNegative()).displayChart();
    new SwingWrapper<>(getChartSpan()).displayChart();
    new SwingWrapper<>(getChartLabels()).displayChart();
  }

  /** Chart 1: single series, integer categories, all positive values. */
  public static HorizontalBarChart getChartBasic() {

    HorizontalBarChart chart =
        new HorizontalBarChartBuilder()
            .width(700)
            .height(400)
            .title("PR #866 – Basic Horizontal Bar")
            .xAxisTitle("Value")
            .yAxisTitle("Category")
            .build();

    chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
    chart.getStyler().setPlotGridLinesVisible(false);

    chart.addSeries("series 1", Arrays.asList(4, 5, 9, 6, 5), Arrays.asList(0, 1, 2, 3, 4));

    return chart;
  }

  /** Chart 2: single series, all negative values. */
  public static HorizontalBarChart getChartNegative() {

    HorizontalBarChart chart =
        new HorizontalBarChartBuilder()
            .width(700)
            .height(400)
            .title("PR #866 – Negative Values")
            .xAxisTitle("Value")
            .yAxisTitle("Category")
            .build();

    chart.addSeries(
        "loss",
        Arrays.asList(-3, -7, -2, -9, -4),
        Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri"));

    return chart;
  }

  /** Chart 3: multiple series spanning positive and negative values. */
  public static HorizontalBarChart getChartSpan() {

    HorizontalBarChart chart =
        new HorizontalBarChartBuilder()
            .theme(ChartTheme.GGPlot2)
            .width(700)
            .height(400)
            .title("PR #866 – Span (positive + negative), multiple series")
            .xAxisTitle("Temperature")
            .yAxisTitle("Color")
            .build();

    List<String> categories = Arrays.asList("Blue", "Red", "Green", "Yellow", "Orange");

    chart.addSeries("fish", Arrays.asList(-40, 30, 20, 60, 60), categories);
    chart.addSeries("birds", Arrays.asList(13, 22, -23, -34, 37), categories);
    chart.addSeries("ants", Arrays.asList(50, 57, -14, -20, 31), categories);

    return chart;
  }

  /** Chart 4: labels enabled, multiple series, positive values. */
  public static HorizontalBarChart getChartLabels() {

    HorizontalBarChart chart =
        new HorizontalBarChartBuilder()
            .width(700)
            .height(400)
            .title("PR #866 – Labels enabled")
            .xAxisTitle("Score")
            .yAxisTitle("Team")
            .build();

    chart.getStyler().setLabelsVisible(true);
    chart.getStyler().setLegendPosition(LegendPosition.OutsideS);

    List<String> categories = Arrays.asList("Alpha", "Beta", "Gamma", "Delta");

    chart.addSeries("Q1", Arrays.asList(12, 9, 15, 7), categories);
    chart.addSeries("Q2", Arrays.asList(8, 14, 11, 13), categories);

    return chart;
  }
}
