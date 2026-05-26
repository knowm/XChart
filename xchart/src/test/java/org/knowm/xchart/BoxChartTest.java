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
    assertDoesNotThrow(
        () -> BitmapEncoder.saveBitmap(chart, out, BitmapEncoder.BitmapFormat.PNG));
  }
}
