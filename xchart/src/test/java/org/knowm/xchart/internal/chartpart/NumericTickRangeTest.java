package org.knowm.xchart.internal.chartpart;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.function.Function;
import org.junit.jupiter.api.Test;
import org.knowm.xchart.internal.chartpart.Axis_.Direction;
import org.knowm.xchart.style.XYStyler;

class NumericTickRangeTest {

  private static Function<Double, String> within(double min, double max) {
    return value -> {
      assertThat(value).isBetween(min, max);
      return Double.toString(value);
    };
  }

  @Test
  void formatsOnlyVisibleGridMultiplesIncludingExactBounds() {
    for (Direction direction : Direction.values()) {
      AxisTickCalculator ticks =
          new AxisTickCalculator_Callback(within(-2, 3), direction, 600, -2, 3, new XYStyler());
      assertThat(ticks.getTickLabels()).first().isEqualTo("-2.0");
      assertThat(ticks.getTickLabels()).last().isEqualTo("3.0");
    }
  }

  @Test
  void equallySpacedDataOutsideTheVisibleBandIsNeverFormatted() {
    AxisTickCalculator ticks =
        new AxisTickCalculator_Callback(
            within(1, 3), Direction.X, 600, 1, 3, List.of(1d, 2d, 3d, 4d), new XYStyler());
    assertThat(ticks.getTickLabels()).containsExactly("1.0", "2.0", "3.0");
  }

  @Test
  void builtInNumberLayoutKeepsTheGridForSevenDecimalPlaces() {
    XYStyler styler = new XYStyler();
    styler.setXAxisDecimalPattern("0.0000000");
    AxisTickCalculator ticks = new AxisTickCalculator_Number(Direction.X, 755, 0, 1e-5, styler);
    assertThat(ticks.getTickLabels())
        .containsExactly(
            "0.0000000", "0.0000020", "0.0000040", "0.0000060", "0.0000080", "0.0000100");
  }

  @Test
  void builtInFormatterHandlesFiniteLimitsAtBothEnds() {
    for (double[] limits :
        new double[][] {{1e308, Double.MAX_VALUE}, {-Double.MAX_VALUE, -1e308}}) {
      AxisTickCalculator ticks =
          new AxisTickCalculator_Number(Direction.X, 800, limits[0], limits[1], new XYStyler());
      assertThat(ticks.getTickLabels()).isNotEmpty();
      assertThat(ticks.getTickLocations()).allMatch(Double::isFinite);
    }
  }

  @Test
  void aNarrowBandStillEmitsAnInRangeTick() {
    double min = 2e-12;
    double max = 7e-12;
    AxisTickCalculator ticks =
        new AxisTickCalculator_Callback(
            within(min, max), Direction.Y, 600, min, max, new XYStyler());
    assertThat(ticks.getTickLabels()).containsExactly(Double.toString(4.5e-12));
    assertThat(ticks.getTickLocations()).containsExactly(300.0);
  }
}
