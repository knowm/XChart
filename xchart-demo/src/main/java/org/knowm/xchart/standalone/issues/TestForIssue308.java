package org.knowm.xchart.standalone.issues;

import java.util.ArrayList;
import java.util.Arrays;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.CategorySeries;
import org.knowm.xchart.CategorySeries.CategorySeriesRenderStyle;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler.ChartTheme;
import org.knowm.xchart.style.Styler.LegendPosition;

public class TestForIssue308 implements ExampleChart<CategoryChart> {

  public static void main(String[] args) {

    ExampleChart<CategoryChart> exampleChart = new TestForIssue308();
    CategoryChart chart = exampleChart.getChart();
    new SwingWrapper<CategoryChart>(chart).displayChart();
  }

  @Override
  public CategoryChart getChart() {

    // Create Chart
    CategoryChart chart =
        new CategoryChartBuilder()
            .width(800)
            .height(600)
            .title("Value vs. Letter")
            .xAxisTitle("Letter")
            .yAxisTitle("Value")
            .theme(ChartTheme.GGPlot2)
            .build();

    // Customize Chart
    chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
    chart.getStyler().setAvailableSpaceFill(.55);
    chart.getStyler().setOverlapped(true);

    // Series
    chart.addSeries(
        "China",
        new ArrayList<String>(Arrays.asList(new String[] {"A", "B", "C", "D", "E"})),
        new ArrayList<Number>(Arrays.asList(new Number[] {-11, -23, 20, 36, 20})),
        new ArrayList<Number>(Arrays.asList(new Number[] {3, 3, 2, 1, 2})));
    CategorySeries series2 =
        chart.addSeries(
            "Korea",
            new ArrayList<String>(Arrays.asList(new String[] {"A", "B", "C", "D", "E"})),
            new ArrayList<Number>(Arrays.asList(new Number[] {13, 15, -22, -28, 7})),
            new ArrayList<Number>(Arrays.asList(new Number[] {3, 3, 2, 1, 2})));
    series2.setChartCategorySeriesRenderStyle(CategorySeriesRenderStyle.Line);
    CategorySeries series3 =
        chart.addSeries(
            "World Ave.",
            new ArrayList<String>(Arrays.asList(new String[] {"A", "B", "C", "D", "E"})),
            new ArrayList<Number>(Arrays.asList(new Number[] {30, 22, 18, -36, -32})),
            new ArrayList<Number>(Arrays.asList(new Number[] {3, 3, 2, 1, 2})));
    series3.setChartCategorySeriesRenderStyle(CategorySeriesRenderStyle.Scatter);

    return chart;
  }

  @Override
  public String getExampleChartName() {

    return getClass().getSimpleName();
  }
}
