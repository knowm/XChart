package org.knowm.xchart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.knowm.xchart.style.Styler.ChartTheme.GGPlot2;
import static org.knowm.xchart.style.Styler.ChartTheme.XChart;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.knowm.xchart.custom.CustomGraphic;
import org.knowm.xchart.custom.CustomTheme;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.Styler.ChartTheme;

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
        () -> assertThat(series.getYData()).containsExactly(-40.0, 40.8, 20.0, 60.0, 60.0),
        () -> assertThat(series.getExtraValues()).containsExactly(3.0, 3.0, 4.0, 3.0, 5.0),
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

    assertThat(fruit.getYData()).containsExactly(50.0, 10.0, -20.0, 40.0, 60.0);
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

    assertThat(fruit.getExtraValues()).containsExactly(3.0, 1.0, 2.0, 1.0, 2.0);
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

    for (CategorySeries series : chart.getSeriesCollection()) {
      assertThat(series.getChartCategorySeriesRenderStyle())
          .contains(chart.getStyler().getDefaultSeriesRenderStyle());
    }
  }

  /**
   * Regression test for <a href="https://github.com/knowm/XChart/issues/707">issue 707</a>.
   *
   * <p>The last bar in a Bar-style CategoryChart was drawn twice (overwriting its label) because
   * the bar loop reused the shared {@code path} variable that {@code closePath()} consumes after
   * the loop. The fix uses a local {@code barPath} so {@code path} stays {@code null} for bar
   * series and {@code closePath()} becomes a no-op.
   *
   * <p>To catch a visual regression we render to a {@link BufferedImage} and compare
   * pixel-diff counts (labels-on minus labels-off) between:
   *
   * <ol>
   *   <li>A 5-bar chart with all values present (all 5 labels rendered).
   *   <li>The same 5-bar chart where the last value is {@link Double#NaN} (4 labels rendered,
   *       same x/y axis extents and bar positions).
   * </ol>
   *
   * <p>When the bug is reintroduced the 5th label is overwritten by the bar fill, making its
   * diff indistinguishable from the NaN chart's diff. The assertion {@code diff5 > diffNaN}
   * therefore fails precisely when and only when the 5th label is missing.
   */
  @Test
  void issue707LastBarLabelIsVisibleAndNotOverwritten() throws Exception {
    List<Double> xData = Arrays.asList(0.0, 1.0, 2.0, 3.0, 4.0);

    // Chart A — all 5 values present; should render 5 labels.
    CategoryChart chart5On = buildIssue707Chart(xData, Arrays.asList(2.0, 1.5, 4.0, 3.77, 2.5), true);
    CategoryChart chart5Off = buildIssue707Chart(xData, Arrays.asList(2.0, 1.5, 4.0, 3.77, 2.5), false);

    // Chart B — last value is NaN so bar 5 is skipped; only 4 labels are rendered.
    CategoryChart chartNaNOn =
        buildIssue707Chart(
            xData, Arrays.asList(2.0, 1.5, 4.0, 3.77, Double.NaN), true);
    CategoryChart chartNaNOff =
        buildIssue707Chart(
            xData, Arrays.asList(2.0, 1.5, 4.0, 3.77, Double.NaN), false);

    int diff5 = countDiffPixels(BitmapEncoder.getBufferedImage(chart5On), BitmapEncoder.getBufferedImage(chart5Off));
    int diffNaN = countDiffPixels(BitmapEncoder.getBufferedImage(chartNaNOn), BitmapEncoder.getBufferedImage(chartNaNOff));

    // When the fix is in place, diff5 > diffNaN because the 5th label contributes pixels.
    // When the bug is reintroduced, the 5th label is overwritten and diff5 == diffNaN.
    assertThat(diff5)
        .as(
            "labels-on/off pixel diff for 5-bar chart (%d) must exceed that of the same chart "
                + "with NaN last bar (%d); equality means the 5th label was overwritten",
            diff5,
            diffNaN)
        .isGreaterThan(diffNaN);
  }

  private CategoryChart buildIssue707Chart(
      List<Double> xData, List<Double> yData, boolean labelsVisible) {
    CategoryChart chart = new CategoryChart(800, 600, ChartTheme.Matlab);
    chart.getStyler().setLabelsVisible(labelsVisible);
    chart.getStyler().setLabelsFontColorAutomaticEnabled(false);
    chart.getStyler().setLabelsFontColor(new Color(255, 0, 255));
    chart.addSeries("y(x)", xData, yData);
    return chart;
  }

  private int countDiffPixels(BufferedImage a, BufferedImage b) {
    int count = 0;
    for (int x = 0; x < a.getWidth(); x++) {
      for (int y = 0; y < a.getHeight(); y++) {
        if (a.getRGB(x, y) != b.getRGB(x, y)) count++;
      }
    }
    return count;
  }
}
