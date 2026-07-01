package org.knowm.xchart.standalone.issues;

import java.util.Locale;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.Styler.LegendPosition;

/**
 * Reproducer / how-to for https://github.com/knowm/XChart/issues/699
 *
 * <p>"Time plot tooltip, float number display with comma."
 *
 * <p>When a Y-axis decimal pattern was set, the tooltip formatted the Y value with the JVM default
 * locale, so on a comma-decimal-separator JVM (e.g. French/German) the tooltip showed "3,9" while
 * the axis showed "3.9". The tooltip now uses the styler locale, matching the axis. Set a
 * period-decimal locale (like {@link Locale#US}) via {@code setLocale(...)} to force periods.
 *
 * <p>Hover over a data point to see the tooltip.
 */
public class TestForIssue699 {

  public static void main(String[] args) {

    // Try another locale by passing its language tag as the first argument, e.g. "de-DE"
    // (comma separator) or "en-US" (period separator). Defaults to US.
    Locale locale = args.length > 0 ? Locale.forLanguageTag(args[0]) : Locale.GERMAN;
    new SwingWrapper<>(getChart(locale)).displayChart();
  }

  /** Constructs and returns the chart without launching a window (headless-safe). */
  public static XYChart getChart() {

    return getChart(Locale.US);
  }

  /**
   * Constructs the chart with the given styler locale. The tooltip's decimal separator follows this
   * locale, just like the axis labels — {@link Locale#US} yields "3.9", {@link Locale#GERMANY}
   * yields "3,9".
   */
  public static XYChart getChart(Locale locale) {

    XYChart chart =
        new XYChartBuilder()
            .width(800)
            .height(600)
            .title("Tooltip decimal separator follows locale (issue 699): " + locale.toLanguageTag())
            .xAxisTitle("X")
            .yAxisTitle("Y")
            .build();

    chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
    chart.getStyler().setToolTipsAlwaysVisible(true);
    chart.getStyler().setYAxisDecimalPattern("#0.#########");
    chart.getStyler().setLocale(locale);

    double[] xData = {1.18, 2.5, 3.7, 4.2, 5.9};
    double[] yData = {3.9, 5.1, 4.4, 7.25, 6.5};

    chart.addSeries("readings", xData, yData);

    return chart;
  }
}
