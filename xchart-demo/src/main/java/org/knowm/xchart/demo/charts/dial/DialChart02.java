package org.knowm.xchart.demo.charts.dial;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import org.knowm.xchart.DialChart;
import org.knowm.xchart.DialChartBuilder;
import org.knowm.xchart.DialSeries;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler;

/**
 * Dial Chart
 *
 * <p>Demonstrates the following:
 *
 * <ul>
 *   <li>Dial Chart
 *   <li>DialChartBuilder
 *   <li>Highly customized
 *   <li>GGPlot Theme
 */
public class DialChart02 implements ExampleChart<DialChart> {

  public static void main(String[] args) {

    ExampleChart<DialChart> exampleChart = new DialChart02();
    DialChart chart = exampleChart.getChart();
    new SwingWrapper<>(chart).displayChart();
  }

  @Override
  public DialChart getChart() {

    // Create Chart
    DialChart chart =
        new DialChartBuilder()
            .width(800)
            .height(600)
            .title(getClass().getSimpleName())
            .theme(Styler.ChartTheme.XChart)
            .build();

    // Series
    DialSeries series = chart.addSeries("Rate", 0.55, "55 %");

    chart.getStyler().setToolTipsEnabled(true);
    chart.getStyler().setLegendVisible(true);
    chart.getStyler().setArcAngle(330);

    chart.getStyler().setDonutThickness(.33);
    chart.getStyler().setCircular(true);

    // arrow
    chart.getStyler().setArrowArcAngle(40);
    chart.getStyler().setArrowArcPercentage(.05);
    chart.getStyler().setArrowLengthPercentage(.5);
    chart.getStyler().setArrowColor(Color.RED);

    chart.getStyler().setAxisTickLabelsVisible(true);
    chart.getStyler().setAxisTicksMarksVisible(true);
    chart.getStyler().setAxisTickMarksColor(Color.BLACK);
    chart.getStyler().setAxisTickMarksStroke(new BasicStroke(3.0f));
    chart.getStyler().setAxisTitleVisible(true);
    chart.getStyler().setAxisTitleFont(new Font(Font.MONOSPACED, Font.BOLD, 13));
    chart.getStyler().setAxisTitlePadding(30);
    chart.getStyler().setAxisTickValues(new double[] {0, 0.2, 0.4, 0.6, 0.8, 1});
    chart.getStyler().setAxisTickLabels(new String[] {"0", "20", "40", "60", "80", "100"});

    chart.getStyler().setLowerFrom(0);
    chart.getStyler().setLowerTo(.1);
    chart.getStyler().setLowerColor(Color.LIGHT_GRAY);
    chart.getStyler().setMiddleFrom(.1);
    chart.getStyler().setMiddleTo(.8);
    chart.getStyler().setMiddleColor(Color.GRAY);
    chart.getStyler().setUpperFrom(.8);
    chart.getStyler().setUpperTo(1);
    chart.getStyler().setUpperColor(Color.DARK_GRAY);

    chart.getStyler().setLabelVisible(true);
    chart.getStyler().setLabelFont(new Font(Font.MONOSPACED, Font.BOLD, 8));

    return chart;
  }

  @Override
  public String getExampleChartName() {

    return getClass().getSimpleName() + " - Highly Customized Dial Chart";
  }
}
