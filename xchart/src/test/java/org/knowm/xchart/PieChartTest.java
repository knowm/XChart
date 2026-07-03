package org.knowm.xchart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.knowm.xchart.style.Styler.ChartTheme.GGPlot2;
import static org.knowm.xchart.style.Styler.ChartTheme.XChart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.knowm.xchart.custom.CustomTheme;

public class PieChartTest {

  private PieChart chart;

  @BeforeEach
  void setUp() {
    chart = new PieChart(800, 600, GGPlot2);
  }

  @Test
  void constructor() {
    PieChartBuilder builder =
        new PieChartBuilder().width(800).height(600).theme(GGPlot2).title("PieChart");

    assertAll(
        () -> assertDoesNotThrow(() -> new PieChart(800, 600)),
        () -> assertDoesNotThrow(() -> new PieChart(800, 600, new CustomTheme())),
        () -> assertDoesNotThrow(() -> new PieChart(800, 600, XChart)),
        () -> assertDoesNotThrow(() -> new PieChart(builder)));
  }

  @Test
  void alreadyContainsSeriesName() {
    assertThatThrownBy(
            () -> {
              chart.addSeries("a", 100);
              chart.addSeries("a", 200);
            })
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageMatching(
            "Series name >a< has already been used. Use unique names for each series!!!");
  }

  /** Regression test for <a href="https://github.com/knowm/XChart/issues/688">issue 688</a>. */
  @Test
  void negativeValueThrows() {
    assertThatThrownBy(() -> chart.addSeries("a", -100))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageMatching("Value cannot be negative!!! >a");
  }

  @Test
  void nullValueThrows() {
    assertThatThrownBy(() -> chart.addSeries("a", null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageMatching("Value cannot be null!!! >a");
  }

  @Test
  void nanValueThrows() {
    assertThatThrownBy(() -> chart.addSeries("a", Double.NaN))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageMatching("Value cannot be NaN or infinite!!! >a");
  }

  @Test
  void infiniteValueThrows() {
    assertThatThrownBy(() -> chart.addSeries("a", Double.POSITIVE_INFINITY))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageMatching("Value cannot be NaN or infinite!!! >a");
  }

  @Test
  void updateWithNegativeValueThrows() {
    chart.addSeries("a", 100);
    assertThatThrownBy(() -> chart.updatePieSeries("a", -50))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageMatching("Value cannot be negative!!! >a");
  }

  @Test
  void zeroValueIsAllowed() {
    PieSeries series = chart.addSeries("a", 0);
    assertThat(series.getValue().doubleValue()).isEqualTo(0.0);
  }

  @Test
  void validValuesAreAdded() {
    chart.addSeries("a", 100);
    chart.addSeries("b", 200.5);

    assertAll(
        () -> assertThat(chart.getSeriesCollection()).hasSize(2),
        () -> assertThat(chart.getSeries("a").getValue().doubleValue()).isEqualTo(100.0),
        () -> assertThat(chart.getSeries("b").getValue().doubleValue()).isEqualTo(200.5));
  }

  @Test
  void updateWithValidValue() {
    chart.addSeries("a", 100);
    PieSeries series = chart.updatePieSeries("a", 250);
    assertThat(series.getValue().doubleValue()).isEqualTo(250.0);
  }
}
