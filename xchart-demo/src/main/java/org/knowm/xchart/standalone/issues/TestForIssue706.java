package org.knowm.xchart.standalone.issues;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.Styler;

/**
 * Demonstrates independent chart title font color control (issue #706).
 *
 * <p>Three charts are shown side by side:
 *
 * <ol>
 *   <li>Default — title color follows chartFontColor (no override).
 *   <li>Red title — setChartTitleFontColor(Color.RED) while body text stays dark.
 *   <li>Custom color + larger font — orange title, bold 20pt, dark body text.
 * </ol>
 */
public class TestForIssue706 {

  public static XYChart getChartDefault() {

    XYChart chart =
        new XYChartBuilder()
            .title("Default Title Color")
            .xAxisTitle("X")
            .yAxisTitle("Y")
            .width(400)
            .height(350)
            .build();

    chart.addSeries("sin", xData(), sinData());
    chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideSW);
    return chart;
  }

  public static XYChart getChartRedTitle() {

    XYChart chart =
        new XYChartBuilder()
            .title("Red Title (Issue #706)")
            .xAxisTitle("X")
            .yAxisTitle("Y")
            .width(400)
            .height(350)
            .build();

    chart.addSeries("sin", xData(), sinData());
    chart.getStyler().setChartTitleFontColor(Color.RED);
    chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideSW);
    return chart;
  }

  public static XYChart getChartOrangeTitle() {

    XYChart chart =
        new XYChartBuilder()
            .title("Orange Bold Title")
            .xAxisTitle("X")
            .yAxisTitle("Y")
            .width(400)
            .height(350)
            .build();

    chart.addSeries("sin", xData(), sinData());
    chart
        .getStyler()
        .setChartTitleFontColor(new Color(220, 100, 0))
        .setChartTitleFont(new Font("Arial", Font.BOLD, 20));
    chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideSW);
    return chart;
  }

  public static void main(String[] args) {

    List<XYChart> charts = new ArrayList<>();
    charts.add(getChartDefault());
    charts.add(getChartRedTitle());
    charts.add(getChartOrangeTitle());

    new SwingWrapper<>(charts).displayChartMatrix();
  }

  private static double[] xData() {

    double[] x = new double[100];
    for (int i = 0; i < 100; i++) {
      x[i] = i * 2 * Math.PI / 99;
    }
    return x;
  }

  private static double[] sinData() {

    double[] y = new double[100];
    for (int i = 0; i < 100; i++) {
      y[i] = Math.sin(i * 2 * Math.PI / 99);
    }
    return y;
  }
}
