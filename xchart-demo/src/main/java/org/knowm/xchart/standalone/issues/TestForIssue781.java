package org.knowm.xchart.standalone.issues;

import java.util.Arrays;
import java.util.List;
import org.knowm.xchart.OHLCChart;
import org.knowm.xchart.OHLCChartBuilder;
import org.knowm.xchart.OHLCSeries;
import org.knowm.xchart.SwingWrapper;

/**
 * Demonstrates that a doji candle (open == close) renders as a horizontal cross rather than just a
 * vertical line.
 *
 * @see <a href="https://github.com/knowm/XChart/issues/781">Issue #781</a>
 */
public class TestForIssue781 {

  public static void main(String[] args) {
    new SwingWrapper<>(getChart()).displayChart();
  }

  public static OHLCChart getChart() {

    OHLCChart chart =
        new OHLCChartBuilder().width(800).height(600).title("Issue 781 - Doji Candle").build();

    // Three candles: normal up, doji (open == close), normal down
    List<Double> xData = Arrays.asList(1.0, 2.0, 3.0);
    List<Double> openData = Arrays.asList(100.0, 110.0, 115.0);
    List<Double> highData = Arrays.asList(108.0, 118.0, 120.0);
    List<Double> lowData = Arrays.asList(95.0, 102.0, 105.0);
    List<Double> closeData = Arrays.asList(106.0, 110.0, 107.0); // middle candle: open == close

    OHLCSeries series = chart.addSeries("Candles", xData, openData, highData, lowData, closeData);
    series.setOhlcSeriesRenderStyle(OHLCSeries.OHLCSeriesRenderStyle.Candle);

    return chart;
  }
}
