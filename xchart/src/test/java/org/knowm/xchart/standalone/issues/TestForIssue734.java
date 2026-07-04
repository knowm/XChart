package org.knowm.xchart.standalone.issues;

import java.util.Arrays;
import java.util.List;
import org.knowm.xchart.OHLCChart;
import org.knowm.xchart.OHLCChartBuilder;
import org.knowm.xchart.OHLCSeries;
import org.knowm.xchart.SwingWrapper;

/**
 * Demonstrates the two doji shapes reported in issue #734 (a duplicate of issue #781):
 *
 * <ul>
 *   <li>open == high == low == close renders as a flat horizontal dash ("___") rather than a dot.
 *   <li>open == close with distinct high/low renders as a cross ("+") rather than a lone vertical
 *       line ("|").
 * </ul>
 *
 * @see <a href="https://github.com/knowm/XChart/issues/734">Issue #734</a>
 * @see <a href="https://github.com/knowm/XChart/issues/781">Issue #781</a>
 */
public class TestForIssue734 {

  public static void main(String[] args) {
    new SwingWrapper<>(getChart()).displayChart();
  }

  public static OHLCChart getChart() {

    OHLCChart chart =
        new OHLCChartBuilder().width(800).height(600).title("Issue 734 - Doji Candles").build();

    // Four candles:
    //  1. normal up
    //  2. flat: open == high == low == close  -> should render as "___"
    //  3. cross: open == close, high/low differ -> should render as "+"
    //  4. normal down
    List<Double> xData = Arrays.asList(1.0, 2.0, 3.0, 4.0);
    List<Double> openData = Arrays.asList(100.0, 110.0, 110.0, 115.0);
    List<Double> highData = Arrays.asList(108.0, 110.0, 118.0, 120.0);
    List<Double> lowData = Arrays.asList(95.0, 110.0, 102.0, 105.0);
    List<Double> closeData = Arrays.asList(106.0, 110.0, 110.0, 107.0);

    OHLCSeries series = chart.addSeries("Candles", xData, openData, highData, lowData, closeData);
    series.setOhlcSeriesRenderStyle(OHLCSeries.OHLCSeriesRenderStyle.Candle);

    return chart;
  }
}
