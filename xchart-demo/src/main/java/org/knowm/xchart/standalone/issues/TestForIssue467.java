package org.knowm.xchart.standalone.issues;

import java.util.Arrays;
import java.util.List;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.CategorySeries;
import org.knowm.xchart.CategorySeries.CategorySeriesRenderStyle;
import org.knowm.xchart.SwingWrapper;

/**
 * Demonstrates smooth curved lines in a CategoryChart (string X-axis labels).
 *
 * @see <a href="https://github.com/knowm/XChart/issues/467">Issue #467</a>
 */
public class TestForIssue467 {

  public static void main(String[] args) {
    new SwingWrapper<>(getChart()).displayChart();
  }

  public static CategoryChart getChart() {
    CategoryChart chart =
        new CategoryChartBuilder()
            .width(800)
            .height(600)
            .title("Issue 467 - Smooth Lines in CategoryChart")
            .xAxisTitle("Month")
            .yAxisTitle("Value")
            .build();

    chart.getStyler().setOverlapped(true);
    chart.getStyler().setAvailableSpaceFill(0.4);

    List<String> xData = Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "Jun");
    List<Integer> yData1 = Arrays.asList(4, 7, 3, 8, 5, 9);
    List<Integer> yData2 = Arrays.asList(2, 5, 6, 3, 7, 4);
    List<Integer> yData3 = Arrays.asList(1, 3, 5, 2, 4, 6);

    CategorySeries series1 = chart.addSeries("Smooth", xData, yData1);
    series1.setChartCategorySeriesRenderStyle(CategorySeriesRenderStyle.Line);
    series1.setSmooth(true);

    CategorySeries series2 = chart.addSeries("Normal", xData, yData2);
    series2.setChartCategorySeriesRenderStyle(CategorySeriesRenderStyle.Line);
    series2.setSmooth(false);

    CategorySeries series3 = chart.addSeries("Bar", xData, yData3);
    series3.setChartCategorySeriesRenderStyle(CategorySeriesRenderStyle.Bar);

    return chart;
  }
}
