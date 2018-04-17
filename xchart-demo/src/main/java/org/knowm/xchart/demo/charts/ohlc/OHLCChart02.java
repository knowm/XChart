package org.knowm.xchart.demo.charts.ohlc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.knowm.xchart.OHLCChart;
import org.knowm.xchart.OHLCChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler;

/**
 * Demonstrates the following:
 *
 * <ul>
 *   <li>Candle render style green up, red down
 *   <li>LegendPosition.OutsideS
 *   <li>Two YAxis Groups - both on left
 */
public class OHLCChart02 implements ExampleChart<OHLCChart> {

  public static void main(String[] args) {

    ExampleChart<OHLCChart> exampleChart = new OHLCChart02();
    OHLCChart chart = exampleChart.getChart();
    new SwingWrapper<OHLCChart>(chart).displayChart();
  }

  @Override
  public OHLCChart getChart() {

    // Create Chart
    OHLCChart chart = new OHLCChartBuilder().width(800).height(600).title("Prices").build();

    // Customize Chart
    chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideS);
    chart.getStyler().setLegendLayout(Styler.LegendLayout.Horizontal);

    List<Date> xData = new ArrayList<Date>();
    List<Double> openData = new ArrayList<Double>();
    List<Double> highData = new ArrayList<Double>();
    List<Double> lowData = new ArrayList<Double>();
    List<Double> closeData = new ArrayList<Double>();

    OHLCChart01.populateData(xData, openData, highData, lowData, closeData);
    xData = null;
    chart.addSeries("Series", xData, openData, highData, lowData, closeData);
    chart.getStyler().setToolTipsEnabled(true);
    return chart;
  }
}
