package org.knowm.xchart.demo.charts.radar;

import org.knowm.xchart.RadarChart;
import org.knowm.xchart.RadarChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler.LegendPosition;

/**
 * Radar Chart with counter-clockwise layout
 *
 * <p>Demonstrates the following:
 *
 * <ul>
 *   <li>Radar Chart drawing direction (issue #580)
 *   <li>RadarStyler#setCounterClockwise
 * </ul>
 *
 * <p>The radii are laid out clockwise by default (matching the common radar/spider chart
 * convention). Here the labels 1..6 are laid out counter-clockwise instead.
 */
public class RadarChart03 implements ExampleChart<RadarChart> {

  public static void main(String[] args) {

    ExampleChart<RadarChart> exampleChart = new RadarChart03();
    RadarChart chart = exampleChart.getChart();
    SwingWrapper<RadarChart> wrapper = new SwingWrapper<>(chart);
    wrapper.displayChart();
    wrapper.getXChartPanel().setToolTipsEnabled(true);
  }

  @Override
  public RadarChart getChart() {

    RadarChart chart =
        new RadarChartBuilder().width(800).height(600).title(getClass().getSimpleName()).build();
    chart.getStyler().setLegendPosition(LegendPosition.InsideSW);
    // draw counter-clockwise instead of the default clockwise
    chart.getStyler().setCounterClockwise(true);

    chart.setRadiiLabels(new String[] {"1", "2", "3", "4", "5", "6"});
    chart.addSeries("Values", new double[] {0.9, 0.8, 0.7, 0.6, 0.5, 0.4});

    return chart;
  }

  @Override
  public void customizePanel(XChartPanel<RadarChart> panel) {

    panel.setToolTipsEnabled(true);
  }

  @Override
  public String getExampleChartName() {

    return getClass().getSimpleName() + " - Radar Chart Direction";
  }
}
