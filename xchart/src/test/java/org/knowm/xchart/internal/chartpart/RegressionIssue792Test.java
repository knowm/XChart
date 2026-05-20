package org.knowm.xchart.internal.chartpart;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.XYChart;

/** Regression test for <a href="https://github.com/knowm/XChart/issues/792">issue 792</a>. */
public class RegressionIssue792Test {

  @Test
  public void customFormattingFunctionWithDuplicateLabelsShouldPreserveAllTicks() throws Exception {

    // given - 8 data points; formatter intentionally returns " " for odd x values
    double[] yData = new double[] {100, 90, 90, 89, 80, 101, 102, 99};
    XYChart chart = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", null, yData);
    chart
        .getStyler()
        .setxAxisTickLabelsFormattingFunction(
            x -> x.intValue() % 2 == 0 ? String.valueOf(x.intValue()) : " ");

    // when
    BitmapEncoder.getBitmapBytes(chart, BitmapEncoder.BitmapFormat.PNG);

    // then - all tick positions must be present; duplicate " " labels must not be collapsed
    List<String> tickLabels = chart.axisPair.getXAxis().getAxisTickCalculator().getTickLabels();
    long blankCount = tickLabels.stream().filter(" "::equals).count();
    assertThat(blankCount)
        .as("odd-x tick labels should all be preserved as \" \", not collapsed by uniqueness check")
        .isGreaterThan(1);
  }
}
