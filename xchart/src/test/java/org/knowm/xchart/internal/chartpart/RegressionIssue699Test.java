package org.knowm.xchart.internal.chartpart;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.Format;
import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.knowm.xchart.XYChart;

/** Regression test for <a href="https://github.com/knowm/XChart/issues/699">issue 699</a>. */
public class RegressionIssue699Test {

  /**
   * The tooltip Y value format (used with a Y-axis decimal pattern) must honor the styler locale,
   * matching how the axis tick labels are formatted. Before the fix, the tooltip built a {@link
   * java.text.DecimalFormat} with the JVM default locale, so a comma-locale JVM rendered "3,9" in
   * the tooltip while the axis showed "3.9".
   */
  @Test
  public void yAxisDecimalPatternTooltipFormatShouldHonorStylerLocale() {

    AxesChart<?, ?> chart = new XYChart(500, 400);
    chart.getStyler().setYAxisDecimalPattern("#0.#########");

    chart.getStyler().setLocale(Locale.US);
    Format usFormat = chart.getYAxisFormat("#0.#########");
    assertThat(usFormat.format(3.9)).isEqualTo("3.9");

    chart.getStyler().setLocale(Locale.GERMANY);
    Format germanyFormat = chart.getYAxisFormat("#0.#########");
    assertThat(germanyFormat.format(3.9)).isEqualTo("3,9");
  }
}
