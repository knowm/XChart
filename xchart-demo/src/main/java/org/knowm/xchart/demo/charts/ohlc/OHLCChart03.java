package org.knowm.xchart.demo.charts.ohlc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.knowm.xchart.OHLCChart;
import org.knowm.xchart.OHLCChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler;

/**
 * Demonstrates the following:
 *
 * <ul>
 *   <li>Tooltips
 *   <li>LegendPosition.InsideS
 *   <li>default OHLCSeriesRenderStyle.Candle
 *   <li>3 series with Line render style
 */
public class OHLCChart03 implements ExampleChart<OHLCChart> {

  public static void main(String[] args) {

    ExampleChart<OHLCChart> exampleChart = new OHLCChart03();
    OHLCChart chart = exampleChart.getChart();
    new SwingWrapper<>(chart).displayChart();
  }

  @Override
  public OHLCChart getChart() {

    // Create Chart
    OHLCChart chart = new OHLCChartBuilder().width(800).height(600).title("OHLCChart03").build();

    // Customize Chart
    chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideS);
    chart.getStyler().setLegendLayout(Styler.LegendLayout.Horizontal);
    chart.getStyler().setToolTipsEnabled(true);
    chart.getStyler().setYAxisDecimalPattern("##.00");
    //    chart.getStyler().setDefaultSeriesRenderStyle(OHLCSeries.OHLCSeriesRenderStyle.Line);
    chart.getStyler().setToolTipsEnabled(true);

    List<Date> xData = new ArrayList<>();
    List<Double> openData = new ArrayList<>();
    List<Double> highData = new ArrayList<>();
    List<Double> lowData = new ArrayList<>();
    List<Double> closeData = new ArrayList<>();

    OHLCChart01.populateData(xData, openData, highData, lowData, closeData);
    List<Long> volumeData = new ArrayList<>();
    Random random = new Random();
    for (int i = 0; i < xData.size(); i++) {
      volumeData.add((long) random.nextInt(100000));
    }
    // TODO remove volume??
    chart.addSeries("DAY K", xData, openData, highData, lowData, closeData, volumeData);
    chart.addSeries("MA5", xData, calculateMA(5, closeData));
    chart.addSeries("MA10", xData, calculateMA(10, closeData));
    chart.addSeries("MA15", xData, calculateMA(15, closeData));

    return chart;
  }

  private List<Double> calculateMA(int dayCount, List<Double> closeData) {

    List<Double> result = new ArrayList<>();
    for (int i = 0; i < closeData.size(); i++) {
      if (i < dayCount) {
        result.add(null);
        continue;
      }
      double sum = 0.0;
      for (int j = 0; j < dayCount; j++) {
        sum += closeData.get(i - j);
      }
      result.add(sum / dayCount);
    }
    return result;
  }

  @Override
  public String getExampleChartName() {

    return getClass().getSimpleName() + " - Candle and lines";
  }
}
