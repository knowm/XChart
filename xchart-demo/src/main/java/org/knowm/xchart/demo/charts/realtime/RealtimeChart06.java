package org.knowm.xchart.demo.charts.realtime;

import java.util.*;
import org.knowm.xchart.*;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.demo.charts.RealtimeExampleChart;
import org.knowm.xchart.demo.charts.ohlc.OHLCChart01;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.Styler.ChartTheme;

/**
 * Real-time OHLC Chart
 *
 * <p>Demonstrates the following:
 *
 * <ul>
 *   <li>real-time chart updates with SwingWrapper
 *   <li>Matlab Theme
 */
public class RealtimeChart06 implements ExampleChart<OHLCChart>, RealtimeExampleChart {

  private OHLCChart ohlcChart;

  private List<Date> xData = new ArrayList<Date>();
  private List<Double> openData = new ArrayList<Double>();
  private List<Double> highData = new ArrayList<Double>();
  private List<Double> lowData = new ArrayList<Double>();
  private List<Double> closeData = new ArrayList<Double>();

  public static final String SERIES_NAME = "series1";

  public static void main(String[] args) {

    // Setup the panel
    final RealtimeChart06 realtimeChart01 = new RealtimeChart06();
    realtimeChart01.go();
  }

  private void go() {

    final SwingWrapper<OHLCChart> swingWrapper = new SwingWrapper<OHLCChart>(getChart());
    swingWrapper.displayChart();

    // Simulate a data feed
    TimerTask chartUpdaterTask =
        new TimerTask() {

          @Override
          public void run() {

            updateData();

            javax.swing.SwingUtilities.invokeLater(
                new Runnable() {

                  @Override
                  public void run() {

                    swingWrapper.repaintChart();
                  }
                });
          }
        };

    Timer timer = new Timer();
    timer.scheduleAtFixedRate(chartUpdaterTask, 0, 500);
  }

  @Override
  public OHLCChart getChart() {

    // Create Chart
    ohlcChart =
        new OHLCChartBuilder()
            .width(800)
            .height(600)
            .title("Real-time Prices Chart")
            .theme(ChartTheme.Matlab)
            .build();

    // Customize Chart
    ohlcChart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideS);
    ohlcChart.getStyler().setLegendLayout(Styler.LegendLayout.Horizontal);
    // generate data
    OHLCChart01.populateData(xData, openData, highData, lowData, closeData);

    ohlcChart.addSeries(SERIES_NAME, xData, openData, highData, lowData, closeData);

    return ohlcChart;
  }

  public void updateData() {

    OHLCChart01.populateData(
        xData.get(xData.size() - 1),
        closeData.get(closeData.size() - 1),
        1,
        xData,
        openData,
        highData,
        lowData,
        closeData);

    // Limit the total number of points
    while (xData.size() > 50) {
      xData.remove(0);
      openData.remove(0);
      highData.remove(0);
      lowData.remove(0);
      closeData.remove(0);
    }

    ohlcChart.updateOHLCSeries(SERIES_NAME, xData, openData, highData, lowData, closeData);
  }
}
