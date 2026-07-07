package org.knowm.xchart.internal.chartpart;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;

/** Regression test for <a href="https://github.com/knowm/XChart/issues/634">issue 634</a>. */
public class RegressionIssue634Test {

  /**
   * With all-positive data and a small plot content size, the Y-axis tick generator used to emit a
   * tick one grid step below the axis minimum (getFirstPosition -> minValue - (minValue % gridStep) -
   * gridStep), which is negative whenever the minimum is smaller than one grid step. That negative
   * tick leaked past the tick-label renderer (which clips to the taller axis-column bounds) and drew
   * a negative label in the bottom plot margin. After the fix, ticks outside [minValue, maxValue] are
   * pruned, so no tick label represents a value outside the data band.
   */
  @Test
  public void noTickLabelOutsideDataBandForAllPositiveData() {

    List<String> x =
        Arrays.asList("others", "aaaaaaaa", "bbbbbbbb", "ccccccc", "ddddddd", "eeeeeee");
    List<Long> y =
        Arrays.asList(23073516369L, 1466500686L, 1248980380L, 1248980380L, 60126163L, 60126163L);

    CategoryChart chart =
        new CategoryChartBuilder().width(1200).height(500).title("Issue #634").build();
    chart.getStyler().setPlotContentSize(0.8);
    chart.getStyler().setStacked(true);
    // Formatter tags any value it is asked to format so we can detect an out-of-range tick label.
    chart
        .getStyler()
        .setYAxisTickLabelsFormattingFunction(
            value -> (value < 0 ? "NEG:" : "") + value.longValue());
    chart.getStyler().setYAxisMin(y.stream().reduce(Long::min).orElse(0L).doubleValue());
    chart.addSeries("1111111", x, y);

    // triggers the tick calculation
    BitmapEncoder.getBufferedImage(chart);

    List<String> tickLabels = chart.axisPair.getYAxis().getAxisTickCalculator().getTickLabels();
    assertThat(tickLabels).isNotEmpty();
    // No label may represent a negative value when all data is positive...
    assertThat(tickLabels)
        .as("no negative Y-axis tick label should be rendered for all-positive data")
        .noneMatch(label -> label.startsWith("NEG:"));
    // ...and every rendered tick value must lie within the data band [minValue, maxValue].
    double minValue = 60126163L;
    double maxValue = 23073516369L;
    for (String label : tickLabels) {
      double value = Long.parseLong(label);
      assertThat(value)
          .as("tick value %s must be within [%s, %s]", value, minValue, maxValue)
          .isBetween(minValue, maxValue);
    }
  }
}
