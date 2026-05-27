package org.knowm.xchart.standalone.issues;

import java.util.Arrays;
import java.util.List;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.markers.SeriesMarkers;

/**
 * Demonstrates TeX/LaTeX formula support in chart elements (issue #125).
 *
 * <p>Strings wrapped in {@code $...$} are rendered as LaTeX math using the optional
 * <a href="https://github.com/opencollab/jlatexmath">JLatexMath</a> library. Without JLatexMath on
 * the classpath, these strings are displayed as plain text — no crash, just a graceful fallback.
 *
 * <h2>Licensing</h2>
 *
 * JLatexMath is GPL v2 <strong>with the Classpath Exception</strong>. This exception (added in
 * commit {@code f1733370} on 2016-01-19, exactly as requested in this XChart issue) permits
 * linking JLatexMath with software distributed under any license, including Apache 2.0. See:
 * <a href="https://github.com/opencollab/jlatexmath/commit/f173337015d89e29b8b2fa05de2c1f0e03ff8e3f">
 * opencollab/jlatexmath@f1733370</a>
 *
 * <p>To activate TeX rendering, add this optional Maven dependency to your project:
 *
 * <pre>{@code
 * <dependency>
 *   <groupId>org.scilab.forge</groupId>
 *   <artifactId>jlatexmath</artifactId>
 *   <version>1.0.7</version>
 * </dependency>
 * }</pre>
 */
public class TestForIssue125 {

  public static void main(String[] args) {

    new SwingWrapper<>(getXYChart()).displayChart();
    new SwingWrapper<>(getCategoryChart()).displayChart();
  }

  /**
   * XY chart with TeX in the chart title, both axis titles, and legend entries.
   *
   * <p>Demonstrates: {@code $...$} strings in {@link org.knowm.xchart.XYChartBuilder#title},
   * {@link org.knowm.xchart.XYChartBuilder#xAxisTitle},
   * {@link org.knowm.xchart.XYChartBuilder#yAxisTitle}, and series labels.
   */
  public static XYChart getXYChart() {

    XYChart chart =
        new XYChartBuilder()
            .width(800)
            .height(500)
            .title("$\\hat{y} = \\beta_0 + \\beta_1 x + \\epsilon$")
            .xAxisTitle("$x \\; [\\mathrm{rad}]$")
            .yAxisTitle("$f(x)$")
            .build();

    chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);

    double[] x = new double[100];
    double[] sinY = new double[100];
    double[] cosY = new double[100];
    for (int i = 0; i < 100; i++) {
      x[i] = i * 2 * Math.PI / 99;
      sinY[i] = Math.sin(x[i]);
      cosY[i] = Math.cos(x[i]);
    }

    XYSeries sinSeries = chart.addSeries("$\\sin(x)$", x, sinY);
    sinSeries.setMarker(SeriesMarkers.NONE);

    XYSeries cosSeries = chart.addSeries("$\\cos(x)$", x, cosY);
    cosSeries.setMarker(SeriesMarkers.NONE);

    return chart;
  }

  /**
   * Category chart using TeX strings as category labels on the X axis.
   *
   * <p>Demonstrates {@code $...$} in category axis tick labels (string data type).
   */
  public static CategoryChart getCategoryChart() {

    CategoryChart chart =
        new CategoryChartBuilder()
            .width(800)
            .height(500)
            .title("Greek-letter categories")
            .xAxisTitle("Category")
            .yAxisTitle("$\\sigma \\; [\\mathrm{MPa}]$")
            .build();

    chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);

    List<String> categories =
        Arrays.asList("$\\alpha$", "$\\beta$", "$\\gamma$", "$\\delta$", "$\\epsilon$");
    List<Double> values = Arrays.asList(10.0, 25.0, 18.0, 32.0, 14.0);

    chart.addSeries("Stress", categories, values);

    return chart;
  }
}
