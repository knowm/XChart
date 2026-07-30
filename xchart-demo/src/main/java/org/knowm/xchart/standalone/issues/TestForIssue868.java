package org.knowm.xchart.standalone.issues;

import java.util.Locale;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.PieStyler.LabelType;

/**
 * Demonstrates issue #868 — the locale set on the {@code PieStyler} now takes effect on slice
 * labels and tooltips.
 *
 * <p>Previously, {@code PlotContent_Pie} captured the styler's locale in its constructor, which
 * runs inside the {@code PieChart} constructor. Since {@code setLocale(...)} can only ever be
 * called after the chart is constructed, it was effectively a no-op for pie charts and the decimal
 * separator always followed the JVM default locale. The {@code DecimalFormat} is now built at paint
 * time from the styler's current locale and decimal pattern, so both {@code setLocale(...)} and a
 * custom localized pattern work.
 *
 * <p>This demo sets {@link Locale#GERMANY} after construction: the percentage labels should render
 * with a comma decimal separator (e.g. "33,3%") regardless of the system locale.
 */
public class TestForIssue868 {

  public static void main(String[] args) {

    new SwingWrapper<>(getChart()).displayChart();
  }

  /** Constructs and returns the chart without launching a window (headless-safe). */
  public static PieChart getChart() {

    PieChart chart =
        new PieChartBuilder()
            .width(700)
            .height(400)
            .title("Issue #868 – Pie chart locale set after construction (de_DE)")
            .build();

    // Set after construction — the point of issue #868.
    chart.getStyler().setLocale(Locale.GERMANY);
    chart.getStyler().setLabelType(LabelType.Percentage);
    chart.getStyler().setDecimalPattern("#0.0");

    chart.addSeries("Gold", 24);
    chart.addSeries("Silver", 21);
    chart.addSeries("Platinum", 39);
    return chart;
  }
}
