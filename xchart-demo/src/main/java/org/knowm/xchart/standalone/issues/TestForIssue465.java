package org.knowm.xchart.standalone.issues;

import java.util.Arrays;
import java.util.List;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.CategorySeries.CategorySeriesRenderStyle;
import org.knowm.xchart.SwingWrapper;

/**
 * Issue #465 — Stacked Area chart.
 *
 * <p>The CategoryChart stacking engine now supports the {@link CategorySeriesRenderStyle#Area}
 * render style, so a stacked area chart works whenever the X-axis is categorical. This demo shows
 * the same dataset rendered as a stacked bar chart and a stacked area chart side by side, so the
 * area tops line up with the bar tops.
 *
 * <p>The data mirrors the AnyChart "stacked area" example the reporter linked. AnyChart's rows are
 * {@code [category, s1, s2, s3]}; here they are transposed into one series per column.
 */
public class TestForIssue465 {

  // AnyChart data set:
  //   ["Winter", 20000, 40000, 20000]
  //   ["Spring", 20000, 40000, 40000]
  //   ["Summer", 40000, 30000, 30000]
  //   ["Autumn", 20000, 20000, 40000]
  private static final List<String> SEASONS = Arrays.asList("Winter", "Spring", "Summer", "Autumn");
  private static final List<Integer> SERIES_1 = Arrays.asList(20000, 20000, 40000, 20000);
  private static final List<Integer> SERIES_2 = Arrays.asList(40000, 40000, 30000, 20000);
  private static final List<Integer> SERIES_3 = Arrays.asList(20000, 40000, 30000, 40000);

  public static void main(String[] args) {

    List<CategoryChart> charts =
        Arrays.asList(
            buildChart("Stacked Bar", CategorySeriesRenderStyle.Bar),
            buildChart("Stacked Area", CategorySeriesRenderStyle.Area));

    new SwingWrapper<>(charts, 1, 2).displayChartMatrix();
  }

  private static CategoryChart buildChart(String title, CategorySeriesRenderStyle renderStyle) {

    CategoryChart chart =
        new CategoryChartBuilder()
            .width(600)
            .height(500)
            .title(title)
            .xAxisTitle("Season")
            .yAxisTitle("Value")
            .build();

    chart.getStyler().setStacked(true);
    chart.getStyler().setDefaultSeriesRenderStyle(renderStyle);
    chart.getStyler().setLegendVisible(false);

    chart.addSeries("Series 1", SEASONS, SERIES_1);
    chart.addSeries("Series 2", SEASONS, SERIES_2);
    chart.addSeries("Series 3", SEASONS, SERIES_3);

    return chart;
  }
}
