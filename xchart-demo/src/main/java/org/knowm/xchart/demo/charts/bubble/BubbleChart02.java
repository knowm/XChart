package org.knowm.xchart.demo.charts.bubble;

import java.text.DecimalFormat;
import org.knowm.xchart.BubbleChart;
import org.knowm.xchart.BubbleChartBuilder;
import org.knowm.xchart.BubbleSeries;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler;

/**
 * Bubble Chart with Custom Tooltips
 *
 * <p>Demonstrates the following:
 *
 * <ul>
 *   <li>Custom, per-data-point tooltip strings via {@link BubbleSeries#setToolTips(String[])}
 *   <li>Tooltips always visible
 */
public class BubbleChart02 implements ExampleChart<BubbleChart> {

  public static void main(String[] args) {

    ExampleChart<BubbleChart> exampleChart = new BubbleChart02();
    BubbleChart chart = exampleChart.getChart();
    SwingWrapper<BubbleChart> wrapper = new SwingWrapper<>(chart);
    wrapper.displayChart();
    wrapper.getXChartPanel().setToolTipsEnabled(true);
  }

  @Override
  public BubbleChart getChart() {

    // Create Chart
    BubbleChart chart =
        new BubbleChartBuilder()
            .width(800)
            .height(600)
            .title(getClass().getSimpleName())
            .xAxisTitle("Volume")
            .yAxisTitle("Rate")
            .build();
    chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideSW);
    chart.getStyler().setLegendLayout(Styler.LegendLayout.Horizontal);
    chart.getStyler().setYAxisDecimalPattern("#%");
    chart.getStyler().setToolTipsAlwaysVisible(true);

    // Series: volume (x), rate (y), number of hits (bubble size)
    double[] volume = new double[] {1298, 843, 2755, 1520};
    double[] rate = new double[] {0.0215, 0.041, 0.017, 0.033};
    double[] hits = new double[] {279, 346, 468, 502};

    BubbleSeries series = chart.addSeries("conversion", volume, rate, hits);

    // Build a custom tooltip string per data point combining all three values, e.g. "2% (279/1298)"
    DecimalFormat percentFormat = new DecimalFormat("#%");
    String[] toolTips = new String[volume.length];
    for (int i = 0; i < volume.length; i++) {
      toolTips[i] =
          percentFormat.format(rate[i]) + " (" + (int) hits[i] + "/" + (int) volume[i] + ")";
    }
    series.setCustomToolTips(true);
    series.setToolTips(toolTips);

    return chart;
  }

  @Override
  public void customizePanel(XChartPanel<BubbleChart> panel) {

    panel.setToolTipsEnabled(true);
  }

  @Override
  public String getExampleChartName() {

    return getClass().getSimpleName() + " - Bubble Chart with Custom Tooltips";
  }
}
