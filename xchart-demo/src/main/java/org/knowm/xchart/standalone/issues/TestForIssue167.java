package org.knowm.xchart.standalone.issues;

import java.util.Arrays;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.CategorySeries;
import org.knowm.xchart.CategorySeries.CategorySeriesRenderStyle;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;

/** @author timmolter */
public class TestForIssue167 implements ExampleChart<CategoryChart> {

  public static void main(String[] args) {

    ExampleChart<CategoryChart> exampleChart = new TestForIssue167();
    CategoryChart chart = exampleChart.getChart();
    CategoryChart chart2 = ((TestForIssue167) exampleChart).getChart2();
    new SwingWrapper<CategoryChart>(chart).displayChart();
    new SwingWrapper<CategoryChart>(chart2).displayChart();
  }

  @Override
  public CategoryChart getChart() {

    // Create Chart
    CategoryChart chart =
        new CategoryChartBuilder()
            .width(800)
            .height(600)
            .title("CategorySeriesRenderStyle-bug")
            .xAxisTitle("Year")
            .yAxisTitle("Data")
            .build();

    // Customize Chart
    chart.getStyler().setLegendVisible(false);
    chart.getStyler().setHasAnnotations(true);
    chart.getStyler().setOverlapped(true);

    // This is the setting that causes troubles. When setting this to .Bar
    // it works just fine. Draws negative values but doesn't leave blank to
    // nulls.
    chart.getStyler().setDefaultSeriesRenderStyle(CategorySeriesRenderStyle.Line);

    // Series
    String[] years = new String[] {"2000", "2001", "2002", "2003", "2004"};
    chart.addSeries(
        "data 1", Arrays.asList(years), Arrays.asList(100.0, 110.0, 120.0, 130.0, 140.0));
    chart.addSeries("data 2", Arrays.asList(years), Arrays.asList(50.0, 60.0, 70.0, null, 90.0));
    chart.addSeries(
        "data 3", Arrays.asList(years), Arrays.asList(-50.0, -60.0, -70.0, null, -90.0));
    chart.addSeries(
        "data 4", Arrays.asList(years), Arrays.asList(-100.0, -110.0, -120.0, -130.0, -140.0));

    return chart;
  }

  public CategoryChart getChart2() {

    // Create Chart
    CategoryChart chart =
        new CategoryChartBuilder()
            .width(800)
            .height(600)
            .title("CategorySeriesRenderStyle-bug")
            .xAxisTitle("Year")
            .yAxisTitle("Data")
            .build();

    // Customize Chart
    chart.getStyler().setLegendVisible(false);
    chart.getStyler().setHasAnnotations(true);
    chart.getStyler().setOverlapped(true);

    // Series
    String[] years = new String[] {"2000", "2001", "2002", "2003", "2004"};
    CategorySeries data1 =
        chart.addSeries(
            "data 1", Arrays.asList(years), Arrays.asList(100.0, 110.0, 120.0, 130.0, 140.0));
    CategorySeries data2 =
        chart.addSeries(
            "data 2", Arrays.asList(years), Arrays.asList(50.0, 60.0, 70.0, null, 90.0));
    CategorySeries data3 =
        chart.addSeries(
            "data 3", Arrays.asList(years), Arrays.asList(-50.0, -60.0, -70.0, null, -90.0));
    CategorySeries data4 =
        chart.addSeries(
            "data 4", Arrays.asList(years), Arrays.asList(-100.0, -110.0, -120.0, -130.0, -140.0));

    // Now rendering is set individually to line per series. Draws nulls but
    // all negatives are drawn as zero.
    data1.setChartCategorySeriesRenderStyle(CategorySeriesRenderStyle.Line);
    data2.setChartCategorySeriesRenderStyle(CategorySeriesRenderStyle.Line);
    data3.setChartCategorySeriesRenderStyle(CategorySeriesRenderStyle.Line);
    data4.setChartCategorySeriesRenderStyle(CategorySeriesRenderStyle.Line);
    return chart;
  }
}
