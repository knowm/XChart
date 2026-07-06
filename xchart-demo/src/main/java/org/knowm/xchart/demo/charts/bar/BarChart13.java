package org.knowm.xchart.demo.charts.bar;

import java.util.Arrays;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.CategorySeries;
import org.knowm.xchart.CategorySeries.CategorySeriesRenderStyle;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.knowm.xchart.style.Styler.YAxisPosition;
import org.knowm.xchart.style.markers.SeriesMarkers;

/**
 * Stacked Bars with a Line on a secondary Y-Axis
 *
 * <p>Demonstrates the following:
 *
 * <ul>
 *   <li>Stacked bar series on the primary (left) Y-Axis group
 *   <li>A Line series assigned to a secondary (right) Y-Axis group
 *   <li>Line/Scatter series are excluded from stacking, and each axis group is ranged only from its
 *       own series (see issue #906)
 * </ul>
 */
public class BarChart13 implements ExampleChart<CategoryChart> {

  public static void main(String[] args) {

    ExampleChart<CategoryChart> exampleChart = new BarChart13();
    CategoryChart chart = exampleChart.getChart();
    new SwingWrapper<>(chart).displayChart();
  }

  @Override
  public CategoryChart getChart() {

    // Create Chart
    CategoryChart chart =
        new CategoryChartBuilder()
            .width(800)
            .height(600)
            .title(getClass().getSimpleName())
            .xAxisTitle("Category")
            .yAxisTitle("Primary (Bars)")
            .build();

    // Customize Chart
    chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
    chart.getStyler().setStacked(true); // affects stackable (bar/area/stick) series only
    chart.getStyler().setYAxisGroupPosition(1, YAxisPosition.Right); // secondary axis on the right

    // Stacked bar series on the primary (left) Y-Axis group
    chart.addSeries("Series A", Arrays.asList("A", "B", "C", "D"), Arrays.asList(10, 20, 15, 25));
    chart.addSeries("Series B", Arrays.asList("A", "B", "C", "D"), Arrays.asList(5, 15, 10, 20));
    chart.addSeries("Series C", Arrays.asList("A", "B", "C", "D"), Arrays.asList(8, 12, 18, 10));

    // Line series on the secondary (right) Y-Axis group. It is drawn at its own values, not stacked
    // on top of the bars, and the right axis is ranged only from this series' data.
    CategorySeries lineSeries =
        chart.addSeries(
            "Trend Line", Arrays.asList("A", "B", "C", "D"), Arrays.asList(100, 150, 130, 180));
    lineSeries.setChartCategorySeriesRenderStyle(CategorySeriesRenderStyle.Line);
    lineSeries.setYAxisGroup(1);
    lineSeries.setMarker(SeriesMarkers.CIRCLE);

    chart.setYAxisGroupTitle(1, "Secondary (Line)");

    return chart;
  }

  @Override
  public String getExampleChartName() {

    return getClass().getSimpleName() + " - Stacked Bars with a Line on a secondary Y-Axis";
  }
}
