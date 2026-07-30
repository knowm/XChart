package org.knowm.xchart.regressiontests;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.image.BufferedImage;
import java.util.Locale;
import org.junit.jupiter.api.Test;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.style.PieStyler.LabelType;

/**
 * Regression test for <a href="https://github.com/knowm/XChart/issues/868">issue 868</a>: the pie
 * chart froze its {@code DecimalFormat} symbols at chart construction time, so {@code
 * setLocale(...)} on the styler — which can only ever be called after construction — had no effect
 * on slice labels. The format is now built at paint time from the styler's current locale and
 * decimal pattern.
 */
public class RegressionTestIssue868 {

  @Test
  public void localeSetAfterConstructionAffectsPieLabels() {

    // Percentage labels always go through the DecimalFormat: "30,9%" vs "30.9%"
    BufferedImage german = renderPie(Locale.GERMANY, null);
    BufferedImage us = renderPie(Locale.US, null);

    assertThat(imagesEqual(german, us)).isFalse();
  }

  @Test
  public void valueLabelsWithoutPatternUseStylerLocale() {

    // Value labels used to bypass the DecimalFormat via y.toString() when no decimal pattern was
    // set; they now always go through the format: "1234,5" vs "1234.5"
    BufferedImage german = renderPie(Locale.GERMANY, null, LabelType.Value);
    BufferedImage us = renderPie(Locale.US, null, LabelType.Value);

    assertThat(imagesEqual(german, us)).isFalse();
  }

  @Test
  public void customDecimalPatternUsesStylerLocaleSymbols() {

    // "30,86%" vs "30.86%" in the slice labels, formatted with the custom pattern
    BufferedImage german = renderPie(Locale.GERMANY, "#0.00");
    BufferedImage us = renderPie(Locale.US, "#0.00");

    assertThat(imagesEqual(german, us)).isFalse();
  }

  @Test
  public void sameLocaleRendersIdentically() {

    // Guards the two tests above against false positives from non-deterministic rendering.
    BufferedImage a = renderPie(Locale.GERMANY, null);
    BufferedImage b = renderPie(Locale.GERMANY, null);

    assertThat(imagesEqual(a, b)).isTrue();
  }

  private static BufferedImage renderPie(Locale locale, String decimalPattern) {
    return renderPie(locale, decimalPattern, LabelType.Percentage);
  }

  private static BufferedImage renderPie(
      Locale locale, String decimalPattern, LabelType labelType) {

    PieChart chart = new PieChartBuilder().width(400).height(300).build();
    // The point of issue #868: the locale can only be set after the chart is constructed.
    chart.getStyler().setLocale(locale);
    chart.getStyler().setLabelType(labelType);
    chart.getStyler().setDecimalPattern(decimalPattern);
    chart.getStyler().setLegendVisible(false);
    chart.addSeries("a", 1234.5);
    chart.addSeries("b", 2765.5);
    return BitmapEncoder.getBufferedImage(chart);
  }

  private static boolean imagesEqual(BufferedImage a, BufferedImage b) {

    if (a.getWidth() != b.getWidth() || a.getHeight() != b.getHeight()) {
      return false;
    }
    for (int y = 0; y < a.getHeight(); y++) {
      for (int x = 0; x < a.getWidth(); x++) {
        if (a.getRGB(x, y) != b.getRGB(x, y)) {
          return false;
        }
      }
    }
    return true;
  }
}
