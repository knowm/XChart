package org.knowm.xchart.demo.charts.realtime;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.Histogram;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.demo.charts.RealtimeExampleChart;
import org.knowm.xchart.style.Styler.ChartTheme;

/**
 * Real-time Category Chart
 *
 * <p>Demonstrates the following:
 *
 * <ul>
 *   <li>real-time chart updates with SwingWrapper
 */
public class RealtimeChart05 implements ExampleChart<CategoryChart>, RealtimeExampleChart {

  private CategoryChart categoryChart;

  private List<String> xData;
  private List<Double> yData;
  public static final String SERIES_NAME = "series1";

  public static void main(String[] args) {

    // Setup the panel
    final RealtimeChart05 realtimeChart01 = new RealtimeChart05();
    realtimeChart01.go();
  }

  private void go() {

    final SwingWrapper<CategoryChart> swingWrapper = new SwingWrapper<CategoryChart>(getChart());
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
  public CategoryChart getChart() {

    xData =
        new CopyOnWriteArrayList<String>(
            Arrays.asList(new String[] {"Blue", "Red", "Green", "Yellow", "Orange"}));
    Histogram histogram = new Histogram(getGaussianData(1000), 5, -10, 10);
    yData = histogram.getyAxisData();

    // Create Chart
    categoryChart =
        new CategoryChartBuilder()
            .width(500)
            .height(400)
            .theme(ChartTheme.Matlab)
            .title("Real-time Category Chart")
            .build();

    categoryChart.addSeries(SERIES_NAME, xData, yData);

    return categoryChart;
  }

  public void updateData() {

    // Get some new data

    Histogram histogram = new Histogram(getGaussianData(1000), 5, -10, 10);
    yData = histogram.getyAxisData();

    categoryChart.updateCategorySeries(SERIES_NAME, xData, yData, null);
  }

  private List<Double> getGaussianData(int count) {

    List<Double> data = new CopyOnWriteArrayList<Double>();
    Random r = new Random();
    for (int i = 0; i < count; i++) {
      data.add(r.nextGaussian() * 5);
      // data.add(r.nextDouble() * 60 - 30);
    }
    return data;
  }
}
