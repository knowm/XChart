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
            Axis_.Direction.X, 900, categories, Series.DataType.String, styler);

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
              Axis_.Direction.X, 900, categories, Series.DataType.String, styler);
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
            Axis_.Direction.X, 900, categories, Series.DataType.String, styler);

    // test
    assertThat(calculator.tickLabels.size()).isEqualTo(6);
    assertThat(calculator.tickLocations)
        .isEqualTo(Arrays.asList(105.0, 243.0, 381.0, 519.0, 657.0, 795.0));
  }

  @Test
  public void shouldAutoSkipLabelsWhenCrowded() {
    // 20 categories in 200px: gridStep ≈ 8.5 px < default spacingHint (74 px)
    // skipFactor = ceil(74 / 8.5) = 9 → labels at indices 0, 9, 18 → 3 labels
    List<String> categories =
        Arrays.asList(
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q",
            "r", "s", "t");
    CategoryStyler styler = new CategoryStyler();

    AxisTickCalculator_Category calculator =
        new AxisTickCalculator_Category(
            Axis_.Direction.X, 200, categories, Series.DataType.String, styler);

    assertThat(calculator.tickLabels.size()).isLessThan(categories.size());
    assertThat(calculator.tickLabels.get(0)).isEqualTo("a");
  }

  @Test
  public void shouldNotAutoSkipWhenSpacingHintIsDisabled() {
    // Setting spacingHint to 0 disables auto-skip; all labels should appear
    List<String> categories = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j");
    CategoryStyler styler = new CategoryStyler();
    styler.setXAxisTickMarkSpacingHint(0);

    AxisTickCalculator_Category calculator =
        new AxisTickCalculator_Category(
            Axis_.Direction.X, 200, categories, Series.DataType.String, styler);

    assertThat(calculator.tickLabels.size()).isEqualTo(categories.size());
  }
}
