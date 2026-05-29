package org.knowm.xchart.internal.series;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;

class SeriesMinMaxCalculatorTest {

  // -----------------------------------------------------------------------
  // findMinMax(double[])
  // -----------------------------------------------------------------------

  @Test
  void findMinMaxDoubleArray_basicValues() {

    double[] result = SeriesMinMaxCalculator.findMinMax(new double[] {3.0, 1.0, 4.0, 1.5, 9.0});

    assertAll(
        () -> assertThat(result[0]).isEqualTo(1.0),
        () -> assertThat(result[1]).isEqualTo(9.0));
  }

  @Test
  void findMinMaxDoubleArray_nanValuesAreIgnored() {

    double[] result =
        SeriesMinMaxCalculator.findMinMax(new double[] {Double.NaN, 2.0, Double.NaN, 5.0});

    assertAll(
        () -> assertThat(result[0]).isEqualTo(2.0),
        () -> assertThat(result[1]).isEqualTo(5.0));
  }

  @Test
  void findMinMaxDoubleArray_negativeValues() {

    double[] result = SeriesMinMaxCalculator.findMinMax(new double[] {-5.0, -1.0, -10.0});

    assertAll(
        () -> assertThat(result[0]).isEqualTo(-10.0),
        () -> assertThat(result[1]).isEqualTo(-1.0));
  }

  @Test
  void findMinMaxDoubleArray_singleElement() {

    double[] result = SeriesMinMaxCalculator.findMinMax(new double[] {42.0});

    assertAll(
        () -> assertThat(result[0]).isEqualTo(42.0),
        () -> assertThat(result[1]).isEqualTo(42.0));
  }

  // -----------------------------------------------------------------------
  // findMinMaxWithErrorBars(double[], double[])
  // -----------------------------------------------------------------------

  @Test
  void findMinMaxWithErrorBars_expandsRange() {

    // values: 5, 10 — error bars: 2, 3
    // effective range: [5-2, 10+3] = [3, 13]
    double[] result =
        SeriesMinMaxCalculator.findMinMaxWithErrorBars(
            new double[] {5.0, 10.0}, new double[] {2.0, 3.0});

    assertAll(
        () -> assertThat(result[0]).isEqualTo(3.0),
        () -> assertThat(result[1]).isEqualTo(13.0));
  }

  @Test
  void findMinMaxWithErrorBars_zeroErrorBars_sameAsPlain() {

    double[] plain = SeriesMinMaxCalculator.findMinMax(new double[] {1.0, 7.0, 4.0});
    double[] withZeroEb =
        SeriesMinMaxCalculator.findMinMaxWithErrorBars(
            new double[] {1.0, 7.0, 4.0}, new double[] {0.0, 0.0, 0.0});

    assertAll(
        () -> assertThat(withZeroEb[0]).isEqualTo(plain[0]),
        () -> assertThat(withZeroEb[1]).isEqualTo(plain[1]));
  }

  // -----------------------------------------------------------------------
  // findMinMax(Collection<?>, DataType)
  // -----------------------------------------------------------------------

  @Test
  void findMinMaxCollection_numberType() {

    List<Number> data = Arrays.asList(3, 1, 4, 1, 5, 9, 2, 6);
    double[] result = SeriesMinMaxCalculator.findMinMax(data, Series.DataType.Number);

    assertAll(
        () -> assertThat(result[0]).isEqualTo(1.0),
        () -> assertThat(result[1]).isEqualTo(9.0));
  }

  @Test
  void findMinMaxCollection_dateType() {

    Date d1 = new Date(1_000L);
    Date d2 = new Date(5_000L);
    Date d3 = new Date(3_000L);
    List<Date> data = Arrays.asList(d1, d2, d3);
    double[] result = SeriesMinMaxCalculator.findMinMax(data, Series.DataType.Date);

    assertAll(
        () -> assertThat(result[0]).isEqualTo(1_000.0),
        () -> assertThat(result[1]).isEqualTo(5_000.0));
  }

  @Test
  void findMinMaxCollection_stringType_returnsNaN() {

    List<String> data = Arrays.asList("a", "b", "c");
    double[] result = SeriesMinMaxCalculator.findMinMax(data, Series.DataType.String);

    assertAll(
        () -> assertThat(result[0]).isNaN(),
        () -> assertThat(result[1]).isNaN());
  }

  @Test
  void findMinMaxCollection_nullElementsAreIgnored() {

    List<Number> data = Arrays.asList(null, 2.0, null, 8.0);
    double[] result = SeriesMinMaxCalculator.findMinMax(data, Series.DataType.Number);

    assertAll(
        () -> assertThat(result[0]).isEqualTo(2.0),
        () -> assertThat(result[1]).isEqualTo(8.0));
  }

  // -----------------------------------------------------------------------
  // findMinMaxWithErrorBars(Collection<Number>, Collection<Number>)
  // -----------------------------------------------------------------------

  @Test
  void findMinMaxWithErrorBarsCollection_expandsRange() {

    List<Number> data = Arrays.asList(5.0, 10.0);
    List<Number> eb = Arrays.asList(2.0, 3.0);
    double[] result = SeriesMinMaxCalculator.findMinMaxWithErrorBars(data, eb);

    assertAll(
        () -> assertThat(result[0]).isEqualTo(3.0),
        () -> assertThat(result[1]).isEqualTo(13.0));
  }
}
