package org.knowm.xchart.internal.chartpart;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.style.CategoryStyler;

public class AxisTickCalculatorCategoryTest {

  @Test
  public void shouldHonorMaxAxisLabelCount() {
    // given
    List<String> categories = Arrays.asList("one", "two", "three", "four", "five", "six");
    CategoryStyler styler = new CategoryStyler();
    styler.setXAxisMaxLabelCount(3);

    // when
    AxisTickCalculator_Category calculator =
        new AxisTickCalculator_Category(
            Axis.Direction.X, 900, categories, Series.DataType.String, styler);

    // test
    assertThat(calculator.tickLabels.size()).isEqualTo(3);
  }

  @Test
  public void shouldFailIfMaxAxisLabelCountIsOne() {
    // given
    List<String> categories = Arrays.asList("one", "two", "three", "four", "five", "six");
    CategoryStyler styler = new CategoryStyler();
    styler.setXAxisMaxLabelCount(1);

    // when & test
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> {
          new AxisTickCalculator_Category(
              Axis.Direction.X, 900, categories, Series.DataType.String, styler);
        });
  }

  @Test
  public void shouldAllowAllLabelsIfThereisEnoughSpace() {
    // given
    List<String> categories = Arrays.asList("one", "two", "three", "four", "five", "six");
    CategoryStyler styler = new CategoryStyler();

    // when
    AxisTickCalculator_Category calculator =
        new AxisTickCalculator_Category(
            Axis.Direction.X, 900, categories, Series.DataType.String, styler);

    // test
    assertThat(calculator.tickLabels.size()).isEqualTo(6);
    assertThat(calculator.tickLocations)
        .isEqualTo(Arrays.asList(105.0, 243.0, 381.0, 519.0, 657.0, 795.0));
  }
}
