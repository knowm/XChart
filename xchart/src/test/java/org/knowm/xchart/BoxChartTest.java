package org.knowm.xchart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BoxChartTest {

  private BoxChart chart;

  @BeforeEach
  void setUp() {

    chart = new BoxChartBuilder().title("test").xAxisTitle("X").yAxisTitle("Y").build();
  }

  // https://github.com/knowm/XChart/issues/891
  @Test
  void replaceDataWithEmptyListThrowsIllegalArgument() {

    BoxSeries series = chart.addSeries("test", Arrays.asList(1, 2, 3));

    assertThatThrownBy(() -> series.replaceData(Collections.emptyList()))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("empty");
  }

  // https://github.com/knowm/XChart/issues/891
  @Test
  void replaceDataWithNullListThrowsIllegalArgument() {

    BoxSeries series = chart.addSeries("test", Arrays.asList(1, 2, 3));

    assertThatThrownBy(() -> series.replaceData((java.util.List<Number>) null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("null");
  }

  // https://github.com/knowm/XChart/issues/891
  @Test
  void replaceDataWithContainingNullThrowsIllegalArgument() {

    BoxSeries series = chart.addSeries("test", Arrays.asList(1, 2, 3));

    assertThatThrownBy(() -> series.replaceData(Arrays.asList(1, null, 3)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("null");
  }

  // https://github.com/knowm/XChart/issues/891
  @Test
  void replaceDataWithValidListSucceeds() {

    BoxSeries series = chart.addSeries("test", Arrays.asList(1, 2, 3));

    assertAll(
        () -> assertDoesNotThrow(() -> series.replaceData(Arrays.asList(4, 5, 6))),
        () -> assertThat(series.getYData()).hasSize(3));
  }

  // https://github.com/knowm/XChart/issues/890
  @Test
  void addSeriesWithSingleDataPointDoesNotThrow() {

    assertDoesNotThrow(() -> chart.addSeries("single", Arrays.asList(42)));
  }

  // https://github.com/knowm/XChart/issues/890
  @Test
  void chartWithSingleDataPointCanBeSaved() {

    chart.addSeries("single", Arrays.asList(42));

    java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
    assertDoesNotThrow(() -> BitmapEncoder.saveBitmap(chart, out, BitmapEncoder.BitmapFormat.PNG));
  }

  // https://github.com/knowm/XChart/issues/816
  @Test
  void boxWidthFractionDefaultsToLegacy() {

    assertThat(chart.getStyler().getBoxWidthFraction()).isEqualTo(-1);
  }

  // https://github.com/knowm/XChart/issues/816
  @Test
  void boxWidthFractionRoundTrips() {

    chart.getStyler().setBoxWidthFraction(0.4);

    assertThat(chart.getStyler().getBoxWidthFraction()).isEqualTo(0.4);
  }

  // https://github.com/knowm/XChart/issues/816
  @Test
  void chartWithBoxWidthFractionCanBeSaved() {

    chart.addSeries("aaa", Arrays.asList(40, 30, 20, 60, 50));
    chart.addSeries("bbb", Arrays.asList(-20, -10, -30, -15, -25));
    chart.getStyler().setBoxWidthFraction(0.3);

    java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
    assertDoesNotThrow(() -> BitmapEncoder.saveBitmap(chart, out, BitmapEncoder.BitmapFormat.PNG));
  }

  @Test
  void addSeriesAcceptsNullFreeImmutableLists() {
    assertThat(chart.addSeries("two", java.util.List.of(1, 2)).getYData())
        .containsExactly(1.0, 2.0);
    assertThat(chart.addSeries("three", java.util.List.of(1, 2, 3)).getYData())
        .containsExactly(1.0, 2.0, 3.0);
  }

  @Test
  void replaceDataAcceptsNullFreeImmutableLists() {
    BoxSeries series = chart.addSeries("test", Arrays.asList(1, 2, 3));
    series.replaceData(java.util.List.of(4, 5));
    assertThat(series.getYData()).containsExactly(4.0, 5.0);
    chart.updateBoxSeries("test", java.util.List.of(6, 7, 8));
    assertThat(series.getYData()).containsExactly(6.0, 7.0, 8.0);
  }

  @Test
  void addSeriesStillRejectsNullElements() {
    assertThatThrownBy(() -> chart.addSeries("test", Arrays.asList(1, null, 3)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("null");
  }
}
