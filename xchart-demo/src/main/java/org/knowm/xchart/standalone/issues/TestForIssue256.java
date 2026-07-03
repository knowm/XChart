package org.knowm.xchart.standalone.issues;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;

/**
 * Reproducer / how-to for https://github.com/knowm/XChart/issues/256
 *
 * <p>"It is possible to change the decimal pattern format of the y axis. But it would be nice to be
 * able to override the whole label formatting. E.g. to be able to apply time and date formatting on
 * the y values."
 *
 * <p>This is already supported: {@code styler.setYAxisTickLabelsFormattingFunction(Function<Double,
 * String>)} completely replaces the default (decimal-pattern) formatting for Y-axis tick labels.
 * The function receives the raw tick value as a {@code Double} and returns the exact String to
 * render — so time/date formatting, units, currency, or any custom logic is possible. The same
 * hook exists for the X-axis via {@code setXAxisTickLabelsFormattingFunction(...)}.
 */
public class TestForIssue256 {

  public static void main(String[] args) {

    new SwingWrapper<>(getChart()).displayChart();
  }

  /** Constructs and returns the chart without launching a window (headless-safe). */
  public static XYChart getChart() {

    XYChart chart =
        new XYChartBuilder()
            .width(800)
            .height(600)
            .title("Custom Y-Axis label formatting: time-of-day (issue 256)")
            .xAxisTitle("Sample #")
            .yAxisTitle("Time of day")
            .build();

    chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Line);
    chart.getStyler().setLegendVisible(false);

    // Y values are epoch-millis timestamps (here: a base time plus a few minutes per sample).
    long base = 0L; // 1970-01-01T00:00:00Z, kept simple/deterministic for the demo
    double[] xData = new double[12];
    double[] yData = new double[12];
    for (int i = 0; i < xData.length; i++) {
      xData[i] = i;
      yData[i] = base + (i * 7 + (i % 3) * 3) * 60_000L; // minutes -> millis
    }
    chart.addSeries("event time", xData, yData);

    // The key: override the whole Y-axis label formatting. Instead of the default decimal pattern,
    // interpret each tick value as epoch millis and render it as HH:mm.
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    chart
        .getStyler()
        .setYAxisTickLabelsFormattingFunction(
            value -> timeFormat.format(new Date(value.longValue())));

    return chart;
  }
}
