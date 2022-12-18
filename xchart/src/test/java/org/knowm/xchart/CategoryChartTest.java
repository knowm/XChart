package org.knowm.xchart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.knowm.xchart.style.Styler.ChartTheme.GGPlot2;
import static org.knowm.xchart.style.Styler.ChartTheme.XChart;

import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.knowm.xchart.custom.CustomGraphic;
import org.knowm.xchart.custom.CustomTheme;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.style.Styler;

public class CategoryChartTest {

  private CategoryChart chart;

  @BeforeEach
  void setUp() {
    chart = new CategoryChart(800, 600, GGPlot2);
  }

  @Test
  void constructor() {
    CategoryChartBuilder builder =
        new CategoryChartBuilder()
            .width(800)
            .height(600)
            .theme(Styler.ChartTheme.GGPlot2)
            .title("CategoryChart")
            .xAxisTitle("x-axis")
            .yAxisTitle("y-axis");

    assertAll(
        () -> assertDoesNotThrow(() -> new CategoryChart(800, 600)),
        () -> assertDoesNotThrow(() -> new CategoryChart(800, 600, new CustomTheme())),
        () -> assertDoesNotThrow(() -> new CategoryChart(800, 600, XChart)),
        () -> assertDoesNotThrow(() -> new CategoryChart(builder)));
  }

  @Test
  void alreadyContainsSeriesName() {
    assertThatThrownBy(
            () -> {
              chart.addSeries(
                  "a",
                  Arrays.asList(1, 2, 3, 4, 5),
                  Arrays.asList(10, 2, 30, 40, 50),
                  Arrays.asList(1, 3, 2, 1, 2));

              chart.addSeries(
                  "a", Arrays.asList("A", "B", "C", "D", "E"), Arrays.asList(10, 25, 30., 4, 5));
            })
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageMatching(
            "Series name >a< has already been used. Use unique names for each series!!!");
  }

  @Test
  void yDataIsNull() {
    assertThatThrownBy(
            () -> {
              chart.addSeries(
                  "a", Arrays.asList("A", "B", "C", "D", "E"), null, Arrays.asList(1, 2, 3, 4, 5));
            })
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageMatching("Y-Axis data cannot be null!!!");
  }

  @Test
  void yDataIsEmpty() {
    assertThatThrownBy(
            () -> {
              chart.addSeries(
                  "a",
                  Arrays.asList("A", "B", "C", "D", "E"),
                  Arrays.asList(),
                  Arrays.asList(1, 2, 3, 4, 5));
            })
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageMatching("Y-Axis data cannot be empty!!!");
  }

  @Test
  void xDataIsNull() {
    assertThatThrownBy(
            () -> {
              chart.addSeries(
                  "a",
                  Arrays.asList(),
                  Arrays.asList(1, 2, 3, 4, 5),
                  Arrays.asList(10, 20, 30, 40, 50));
            })
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageMatching("X-Axis data cannot be empty!!!");
  }

  @Test
  void errorBarSizeIsNotEqualToYDataSize() {
    assertThatThrownBy(
            () -> {
              chart.addSeries(
                  "a",
                  Arrays.asList("A", "B", "C", "D", "E"),
                  Arrays.asList(1, 2),
                  Arrays.asList(1, 3, 2, 1, 2));
            })
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageMatching("Error bars and Y-Axis sizes are not the same!!!");
  }

  @Test
  void xDataSizeIsNotEqualToYDataSize() {
    assertThatThrownBy(
            () -> {
              chart.addSeries(
                  "a",
                  Arrays.asList("A", "B", "C", "D"),
                  Arrays.asList(1, 2, 3, 4, 5),
                  Arrays.asList(1, 3, 2, 1, 2));
            })
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageMatching("X and Y-Axis sizes are not the same!!!");
  }

  @Test
  void checkAddSeries() {
    CategorySeries series =
        chart.addSeries(
            "fruit",
            Arrays.asList("Blue", "Red", "Green", "Yellow", "Orange"),
            Arrays.asList(-40, 40.8, 20, 60, 60),
            Arrays.asList(3, 3, 4, 3, 5));

    assertAll(
        () -> assertThat(series.getName()).isEqualTo("fruit"),
        () -> assertThat(series.getxAxisDataType()).isEqualTo(Series.DataType.String),
        () ->
            assertThat(series.getXData())
                .isEqualTo(Arrays.asList("Blue", "Red", "Green", "Yellow", "Orange")),
        () -> assertThat(series.getYData()).isEqualTo(Arrays.asList(-40, 40.8, 20, 60, 60)),
        () -> assertThat(series.getExtraValues()).isEqualTo(Arrays.asList(3, 3, 4, 3, 5)),
        () -> assertThat(series.getYData()).hasSize(5));
  }

  @Test
  void updateNonExistentSeries() {
    chart.addSeries(
        "fruit",
        Arrays.asList("Blue", "Red", "Green", "Yellow", "Orange"),
        Arrays.asList(-40, 30, 20, 60, 60));
    chart.addSeries(
        "food",
        Arrays.asList("Blue", "Red", "Green", "Yellow", "Orange"),
        Arrays.asList(50, 10, -20, 40, 60));

    assertThatThrownBy(
            () -> {
              chart.updateCategorySeries("a", Arrays.asList(1, 2, 3, 4, 5), null, null);
            })
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageMatching("Series name >a< not found!!!");
  }

  @Test
  void updateXData() {
    CategorySeries fruit =
        chart.addSeries(
            "fruit",
            Arrays.asList("Blue", "Red", "Green", "Yellow", "Orange"),
            Arrays.asList(-40, 30, 20, 60, 60));

    chart.updateCategorySeries(
        "fruit", Arrays.asList("a", "b", "c", "d", "e"), Arrays.asList(-40, 30, 20, 60, 60), null);

    assertThat(fruit.getXData()).isEqualTo(Arrays.asList("a", "b", "c", "d", "e"));
  }

  @Test
  void updateYData() {
    CategorySeries fruit =
        chart.addSeries(
            "fruit",
            Arrays.asList("Blue", "Red", "Green", "Yellow", "Orange"),
            Arrays.asList(-40, 30, 20, 60, 60));

    chart.updateCategorySeries(
        "fruit",
        Arrays.asList("Blue", "Red", "Green", "Yellow", "Orange"),
        Arrays.asList(50, 10, -20, 40, 60),
        null);

    assertThat(fruit.getYData()).isEqualTo(Arrays.asList(50, 10, -20, 40, 60));
  }

  @Test
  void updateErrorBar() {
    CategorySeries fruit =
        chart.addSeries(
            "fruit",
            Arrays.asList("Blue", "Red", "Green", "Yellow", "Orange"),
            Arrays.asList(-40, 30, 20, 60, 60),
            Arrays.asList(5, 5, 10, 5, 5));

    chart.updateCategorySeries(
        "fruit",
        Arrays.asList("Blue", "Red", "Green", "Yellow", "Orange"),
        Arrays.asList(-40, 30, 20, 60, 60),
        Arrays.asList(3, 1, 2, 1, 2));

    assertThat(fruit.getExtraValues()).isEqualTo(Arrays.asList(3, 1, 2, 1, 2));
  }

  @Test
  void automaticallyCreatedWhenXDataIsNull() {
    CategorySeries fruit =
        chart.addSeries(
            "fruit",
            Arrays.asList("Blue", "Red", "Green", "Yellow", "Orange"),
            Arrays.asList(-40, 30, 20, 60, 60));

    chart.updateCategorySeries("fruit", null, Arrays.asList(-40, 30, 20, 60, 60), null);

    assertThat(fruit.getXData()).isEqualTo(Arrays.asList(1, 2, 3, 4, 5));
  }

  @Test
  void paint() {
    chart.addSeries(
        "fruit",
        Arrays.asList("Blue", "Red", "Green", "Yellow", "Orange"),
        Arrays.asList(-40, 30, 20, 60, 60));
    chart.addSeries(
        "food",
        Arrays.asList("Blue", "Red", "Green", "Yellow", "Orange"),
        Arrays.asList(-40, 30, 20, 60, 60));

    chart.paint(new CustomGraphic(), 20, 20);

    for (CategorySeries series : chart.getSeriesMap().values()) {
      CategorySeries.CategorySeriesRenderStyle seriesType =
          series.getChartCategorySeriesRenderStyle();
      if (seriesType != null) {
        assertThat(series.getChartCategorySeriesRenderStyle())
            .isEqualTo(chart.getStyler().getDefaultSeriesRenderStyle());
      }
    }
  }
}
