package org.knowm.xchart.demo.charts.bar;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.CategorySeries;
import org.knowm.xchart.CategorySeries.CategorySeriesRenderStyle;
import org.knowm.xchart.Histogram;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler.LegendPosition;

/**
 * Stepped Chart Histogram
 *
 * <p>Demonstrates the following:
 *
 * <ul>
 *   <li>Histogram
 *   <li>Bar Chart styles - overlapped
 *   <li>Custom Line Style
 *   <li>Render style - Stepped Bars
 */
public class BarChart10 implements ExampleChart<CategoryChart> {

  public static void main(String[] args) {

    ExampleChart<CategoryChart> exampleChart = new BarChart10();
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

    // While supported, SteppedBars in anything but overlapped mode are fairly useless.
    chart.getStyler().setOverlapped(true);

    // Series
    Histogram histogram1 = new Histogram(getGaussianData(10000), 20, -20, 20);
    Histogram histogram2 = new Histogram(getGaussianData(5000), 20, -20, 20);
    CategorySeries series1 =
        chart.addSeries("histogram 2", histogram2.getxAxisData(), histogram2.getyAxisData());
    CategorySeries series2 =
        chart.addSeries("histogram 1", histogram1.getxAxisData(), histogram1.getyAxisData());

    // Set both series to SteppedBar
    series2.setChartCategorySeriesRenderStyle(CategorySeriesRenderStyle.SteppedBar);
    series1.setChartCategorySeriesRenderStyle(CategorySeriesRenderStyle.SteppedBar);

    // Remove the outline from the first series
    series1.setLineColor(new Color(0, 0, 0, 0));

    // Make the fill of the second series transparent, leaving us with only the outline
    series2.setFillColor(new Color(0, 0, 0, 0));

    // Also give it a nice dotted-line apperance
    BasicStroke baseLineStyle = new BasicStroke();
    BasicStroke newLineStyle =
        new BasicStroke(
            2f,
            baseLineStyle.getEndCap(),
            baseLineStyle.getLineJoin(),
            baseLineStyle.getMiterLimit(),
            new float[] {5, 5},
            baseLineStyle.getDashPhase());

    series2.setLineStyle(newLineStyle);

    return chart;
  }

  private List<Double> getGaussianData(int count) {

    List<Double> data = new ArrayList<Double>(count);
    Random r = new Random();
    for (int i = 0; i < count; i++) {
      data.add(r.nextGaussian() * 10);
    }
    return data;
  }
}
