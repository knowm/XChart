package org.knowm.xchart.standalone.issues;

import java.awt.Color;
import java.util.Arrays;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.CategorySeries;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.markers.SeriesMarkers;

/**
 * Reproducer for https://github.com/knowm/XChart/issues/892
 *
 * <p>"When you have series using different render styles, like bar and line, the legend markers and
 * text is displayed wonky." (reported with a horizontal, OutsideS legend)
 *
 * <p>Expected: in a horizontal legend, the markers/boxes and their text should line up uniformly
 * across mixed render styles. Currently a Bar entry centers its text within the 20px legend box
 * while a Line entry centers within the (smaller) marker size, so the two entries sit at different
 * vertical baselines.
 *
 * <p>This is the reporter's original example, verbatim except for being wrapped in a headless-safe
 * {@link #getChart()} so it can also be rendered/asserted in tests.
 */
public class TestForIssue892 {

  public static void main(String[] args) {

    new SwingWrapper<>(getChart()).displayChart();
  }

  /** Constructs and returns the chart without launching a window (headless-safe). */
  public static CategoryChart getChart() {

    // Create chart
    CategoryChart chart =
        new CategoryChartBuilder()
            .width(800)
            .height(600)
            .title("Bar + Line Series Legend Marker Demo")
            .xAxisTitle("Category")
            .yAxisTitle("Value")
            .build();

    // General Styler settings
    chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideS);
    chart.getStyler().setLegendLayout(Styler.LegendLayout.Horizontal);
    chart.getStyler().setLegendVisible(true);

    // Bar Series
    CategorySeries barSeries =
        chart.addSeries("Bar Series", Arrays.asList("A", "B", "C"), Arrays.asList(20, 40, 55));
    barSeries.setChartCategorySeriesRenderStyle(CategorySeries.CategorySeriesRenderStyle.Bar);
    barSeries.setFillColor(new Color(35, 127, 211));

    // Line Series
    CategorySeries lineSeries =
        chart.addSeries("Line Series", Arrays.asList("A", "B", "C"), Arrays.asList(15, 30, 45));
    lineSeries.setChartCategorySeriesRenderStyle(CategorySeries.CategorySeriesRenderStyle.Line);
    lineSeries.setLineColor(new Color(237, 133, 53));
    lineSeries.setLineWidth(2.5f);
    lineSeries.setMarker(SeriesMarkers.CIRCLE);

    return chart;
  }
}
