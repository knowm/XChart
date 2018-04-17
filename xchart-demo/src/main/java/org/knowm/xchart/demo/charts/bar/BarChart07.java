package org.knowm.xchart.demo.charts.bar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.Histogram;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.Styler.LegendPosition;

/**
 * Histogram Not Overlapped
 *
 * <p>Demonstrates the following:
 *
 * <ul>
 *   <li>Histogram
 *   <li>Bar Chart styles - not overlapped, bar width
 *   <li>Integer data values
 *   <li>Tool Tips
 */
public class BarChart07 implements ExampleChart<CategoryChart> {

  public static void main(String[] args) {

    ExampleChart<CategoryChart> exampleChart = new BarChart07();
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
            .title("Score Histogram")
            .xAxisTitle("Mean")
            .yAxisTitle("Count")
            .build();

    // Customize Chart
    chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
    chart.getStyler().setAvailableSpaceFill(.96);
    chart.getStyler().setPlotGridVerticalLinesVisible(false);
    chart.getStyler().setToolTipsEnabled(true);
    chart.getStyler().setToolTipType(Styler.ToolTipType.yLabels);

    // Series
    Histogram histogram1 = new Histogram(getGaussianData(1000), 10, -30, 30);
    chart.addSeries("histogram 1", histogram1.getxAxisData(), histogram1.getyAxisData());
    Histogram histogram2 = new Histogram(getGaussianData(1000), 10, -30, 30);
    chart.addSeries("histogram 2", histogram2.getxAxisData(), histogram2.getyAxisData());

    return chart;
  }

  private List<Integer> getGaussianData(int count) {

    List<Integer> data = new ArrayList<Integer>(count);
    Random r = new Random();
    for (int i = 0; i < count; i++) {
      data.add((int) (r.nextGaussian() * 10));
    }
    return data;
  }
}
