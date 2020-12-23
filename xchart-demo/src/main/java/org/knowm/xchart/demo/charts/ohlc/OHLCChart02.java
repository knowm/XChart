package org.knowm.xchart.demo.charts.ohlc;

import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.knowm.xchart.OHLCChart;
import org.knowm.xchart.OHLCChartBuilder;
import org.knowm.xchart.OHLCSeries;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler;

/**
 * Demonstrates the following:
 *
 * <ul>
 *   <li>Tooltips
 *   <li>Candle render style green down, red up
 *   <li>LegendPosition.OutsideS
 *   <li>OHLCSeriesRenderStyle.HiLo
 */
public class OHLCChart02 implements ExampleChart<OHLCChart> {

  public static void main(String[] args) {

    ExampleChart<OHLCChart> exampleChart = new OHLCChart02();
    OHLCChart chart = exampleChart.getChart();
    new SwingWrapper<>(chart).displayChart();
  }

  @Override
  public OHLCChart getChart() {

    // Create Chart
    OHLCChart chart = new OHLCChartBuilder().width(800).height(600).title("OHLCChart02").build();

    // Customize Chart
    chart.getStyler().setLegendPosition(Styler.CardinalPosition.OutsideS);
    chart.getStyler().setLegendLayout(Styler.LegendLayout.Horizontal);
    chart.getStyler().setDefaultSeriesRenderStyle(OHLCSeries.OHLCSeriesRenderStyle.HiLo);
    chart.getStyler().setToolTipsEnabled(true);

    List<Date> xData = new ArrayList<>();
    List<Double> openData = new ArrayList<>();
    List<Double> highData = new ArrayList<>();
    List<Double> lowData = new ArrayList<>();
    List<Double> closeData = new ArrayList<>();

    OHLCChart01.populateData(xData, openData, highData, lowData, closeData);

    chart
        .addSeries("Series", xData, openData, highData, lowData, closeData)
        .setUpColor(Color.RED)
        .setDownColor(Color.GREEN);

    return chart;
  }

  @Override
  public String getExampleChartName() {

    return getClass().getSimpleName() + " - Candle with custom colors";
  }
}
