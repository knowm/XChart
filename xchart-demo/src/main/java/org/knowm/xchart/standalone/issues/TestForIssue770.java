package org.knowm.xchart.standalone.issues;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.knowm.xchart.OHLCChart;
import org.knowm.xchart.OHLCChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.Styler;

/**
 * Demonstrates the fix for issue #770 — zoom support for OHLCChart.
 *
 * <p>Before the fix: zoom was gated by {@code instanceof XYChart} checks throughout the codebase;
 * {@code OHLCStyler} did not expose zoom settings and {@code OHLCSeries} had no filter methods.
 *
 * <p>After the fix:
 *
 * <ul>
 *   <li>Zoom fields moved from {@code XYStyler} to {@code AxesChartStyler} so all axes-based
 *       charts inherit them.
 *   <li>{@code OHLCSeries} gained {@code filterXByValue}, {@code filterXByIndex}, {@code
 *       resetFilter}, {@code isAllXData}.
 *   <li>{@code ChartZoom} generalised to {@code Chart<? extends AxesChartStyler, ?>}.
 *   <li>{@code XChartPanel} and {@code PlotContent_} wired zoom for {@code OHLCChart}.
 * </ul>
 *
 * <p>Usage: drag to select an x-range to zoom in; double-click or press Reset to restore.
 */
public class TestForIssue770 {

  public static void main(String[] args) {

    new SwingWrapper<>(getChart()).displayChart();
  }

  public static OHLCChart getChart() {

    OHLCChart chart =
        new OHLCChartBuilder()
            .width(900)
            .height(500)
            .title("Issue #770 — OHLCChart zoom")
            .xAxisTitle("Date")
            .yAxisTitle("Price")
            .theme(Styler.ChartTheme.GGPlot2)
            .build();

    chart.getStyler().setZoomEnabled(true);
    chart.getStyler().setZoomResetByDoubleClick(true);
    chart.getStyler().setZoomResetByButton(true);

    // Generate 200 candle bars
    int n = 200;
    List<Date> xData = new ArrayList<>();
    List<Double> openData = new ArrayList<>();
    List<Double> highData = new ArrayList<>();
    List<Double> lowData = new ArrayList<>();
    List<Double> closeData = new ArrayList<>();

    long base = System.currentTimeMillis() - (long) n * 86_400_000L;
    double price = 100.0;
    for (int i = 0; i < n; i++) {
      xData.add(new Date(base + (long) i * 86_400_000L));
      double open = price;
      double close = open + (Math.random() - 0.49) * 4;
      double high = Math.max(open, close) + Math.random() * 2;
      double low = Math.min(open, close) - Math.random() * 2;
      openData.add(open);
      highData.add(high);
      lowData.add(low);
      closeData.add(close);
      price = close;
    }

    chart.addSeries("OHLC", xData, openData, highData, lowData, closeData);
    return chart;
  }
}
