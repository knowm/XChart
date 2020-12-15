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

public class OHLCChart04 implements ExampleChart<OHLCChart> {

  public static void main(String[] args) {

    ExampleChart<OHLCChart> exampleChart = new OHLCChart04();
    OHLCChart chart = exampleChart.getChart();
    new SwingWrapper<OHLCChart>(chart).displayChart();
  }

  @Override
  public OHLCChart getChart() {

    // Create Chart
    OHLCChart chart = new OHLCChartBuilder().width(800).height(600).title("Prices").build();

    // Customize Chart
    chart.getStyler().setLegendPosition(Styler.CardinalPosition.InsideS);
    chart.getStyler().setLegendLayout(Styler.LegendLayout.Horizontal);
    chart.getStyler().setToolTipsEnabled(true);
    chart.getStyler().setYAxisDecimalPattern("##.00");

    List<Date> xData = new ArrayList<Date>();
    List<Double> openData = new ArrayList<Double>();
    List<Double> highData = new ArrayList<Double>();
    List<Double> lowData = new ArrayList<Double>();
    List<Double> closeData = new ArrayList<Double>();

    OHLCChart01.populateData(xData, openData, highData, lowData, closeData);
    List<Long> volumeData = new ArrayList<>();
    Random random = new Random();
    for (int i = 0; i < xData.size(); i++) {
      volumeData.add((long) random.nextInt(100000));
    }
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
