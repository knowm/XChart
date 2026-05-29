package org.knowm.xchart.demo.charts.stick;

import java.util.Arrays;
import java.util.List;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.CategorySeries.CategorySeriesRenderStyle;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler.LegendPosition;

/**
 * Hydrogen Spectral Emission Lines — TeX Labels Demo
 *
 * <p>Demonstrates the following:
 *
 * <ul>
 *   <li>Stick category series render type
 *   <li>Multiple series on the same chart (Lyman, Balmer, Paschen spectral series)
 *   <li>TeX/LaTeX rendering in chart title, axis titles, and legend labels
 * </ul>
 */
public class StickChart02 implements ExampleChart<CategoryChart> {

  public static void main(String[] args) {

    ExampleChart<CategoryChart> exampleChart = new StickChart02();
    CategoryChart chart = exampleChart.getChart();
    new SwingWrapper<CategoryChart>(chart).displayChart();
  }

  @Override
  public CategoryChart getChart() {

    // Hydrogen emission wavelengths (nm) — Rydberg formula:
    //   1/λ = R_H * (1/n1² - 1/n2²)
    // Lyman series (n1=1): UV
    List<String> lymanLabels =
        Arrays.asList(
            "$Ly\\alpha$", "$Ly\\beta$", "$Ly\\gamma$", "$Ly\\delta$", "$Ly\\epsilon$");
    List<Double> lymanWavelengths = Arrays.asList(121.6, 102.6, 97.2, 95.0, 93.8);

    // Balmer series (n1=2): visible
    List<String> balmerLabels =
        Arrays.asList("$H\\alpha$", "$H\\beta$", "$H\\gamma$", "$H\\delta$", "$H\\epsilon$");
    List<Double> balmerWavelengths = Arrays.asList(656.3, 486.1, 434.0, 410.2, 397.0);

    // Paschen series (n1=3): near-IR
    List<String> paschenLabels =
        Arrays.asList(
            "$Pa\\alpha$", "$Pa\\beta$", "$Pa\\gamma$", "$Pa\\delta$", "$Pa\\epsilon$");
    List<Double> paschenWavelengths = Arrays.asList(1875.1, 1281.8, 1093.8, 1004.9, 954.6);

    CategoryChart chart =
        new CategoryChartBuilder()
            .width(900)
            .height(600)
            .title("$\\frac{1}{\\lambda} = R_H \\left( \\frac{1}{n_1^2} - \\frac{1}{n_2^2} \\right)$")
            .xAxisTitle("Transition")
            .yAxisTitle("$\\lambda\\;(\\text{nm})$")
            .build();

    chart.getStyler().setDefaultSeriesRenderStyle(CategorySeriesRenderStyle.Stick);
    chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
    chart.getStyler().setChartTitleVisible(true);

    chart.addSeries("$\\text{Lyman}\\;(n_1=1,\\;UV)$", lymanLabels, lymanWavelengths);
    chart.addSeries("$\\text{Balmer}\\;(n_1=2,\\;\\text{visible})$", balmerLabels, balmerWavelengths);
    chart.addSeries(
        "$\\text{Paschen}\\;(n_1=3,\\;\\text{near-IR})$", paschenLabels, paschenWavelengths);

    return chart;
  }

  @Override
  public String getExampleChartName() {

    return getClass().getSimpleName() + " - Hydrogen Spectral Lines with TeX Labels";
  }
}
