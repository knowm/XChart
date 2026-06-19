package org.knowm.xchart.standalone.issues;

import java.util.Map;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;

/**
 * Demonstrates how to map specific X-axis tick positions to custom label strings using {@link
 * org.knowm.xchart.style.AxesChartStyler#setXAxisTickLabelsFormattingFunction(java.util.function.Function)}.
 *
 * <p>Issue #658 asked whether a "tick location label map" API was available. The equivalent
 * functionality is achieved via the formatting function: build a {@code Map<Double, String>} and
 * pass a lookup lambda as the formatter. Tick values that are not in the map fall back to the
 * default numeric format.
 *
 * <p>Expected result: X-axis shows "Mon"–"Fri" labels on the first five integer tick positions
 * instead of raw numbers.
 */
public class TestForIssue658 {

  public static void main(String[] args) {

    new SwingWrapper<>(getChart()).displayChart();
  }

  public static XYChart getChart() {

    double[] xData = new double[] {1, 2, 3, 4, 5};
    double[] yData = new double[] {4, 5, 3, 8, 6};

    XYChart chart =
        new XYChartBuilder()
            .width(800)
            .height(600)
            .title("Issue #658 — custom X-axis tick labels via map")
            .xAxisTitle("Day")
            .yAxisTitle("Value")
            .build();

    chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideSW);
    chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);

    chart.addSeries("Sales", xData, yData);

    Map<Double, String> dayLabels =
        Map.of(1.0, "Mon", 2.0, "Tue", 3.0, "Wed", 4.0, "Thu", 5.0, "Fri");

    chart
        .getStyler()
        .setXAxisTickLabelsFormattingFunction(
            x -> dayLabels.getOrDefault(x, String.valueOf(x.intValue())));

    return chart;
  }
}
