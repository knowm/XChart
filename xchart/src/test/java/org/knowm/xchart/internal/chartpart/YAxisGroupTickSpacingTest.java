package org.knowm.xchart.internal.chartpart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.AxesChartStyler;
import org.knowm.xchart.style.XYStyler;

class YAxisGroupTickSpacingTest {

  @Test
  void groupOverridesAreIndependentAndCanBeRemoved() {
    AxesChartStyler styler = new XYStyler();
    styler.setYAxisTickMarkSpacingHint(40);
    assertThat(styler.setYAxisTickMarkSpacingHint(1, 120)).isSameAs(styler);
    assertThat(styler.getYAxisTickMarkSpacingHint(1)).isEqualTo(120);
    assertThat(styler.getYAxisTickMarkSpacingHint(0)).isNull();
    assertThat(styler.getYAxisTickMarkSpacingHint()).isEqualTo(40);
    styler.setYAxisTickMarkSpacingHint(60);
    assertThat(styler.getYAxisTickMarkSpacingHint(1)).isEqualTo(120);
    styler.setYAxisTickMarkSpacingHint(1, null);
    assertThat(styler.getYAxisTickMarkSpacingHint(1)).isNull();
    assertThat(styler.getYAxisTickMarkSpacingHint()).isEqualTo(60);
    styler.setYAxisTickMarkSpacingHint(1, 0);
    assertThat(styler.getYAxisTickMarkSpacingHint(1)).isZero();
    assertThatThrownBy(() -> styler.setYAxisTickMarkSpacingHint(1, -1))
        .isInstanceOf(IllegalArgumentException.class);
    assertThat(styler.getYAxisTickMarkSpacingHint(1)).isZero();
  }

  @Test
  void renderedGroupsHaveIndependentSpacingAndRestoreTheirOriginalTicks() {
    XYChart chart = chart();
    paint(chart);
    List<Double> primary = ticks(chart, 0);
    List<Double> secondary = ticks(chart, 1);
    chart.getStyler().setYAxisTickMarkSpacingHint(1, 180);
    paint(chart);
    assertThat(ticks(chart, 0)).isEqualTo(primary);
    assertThat(ticks(chart, 1).size()).isLessThan(secondary.size());
    chart.getStyler().setYAxisTickMarkSpacingHint(1, null);
    paint(chart);
    assertThat(ticks(chart, 1)).isEqualTo(secondary);
  }

  @Test
  void customFormattingKeepsGroupSpecificSpacing() {
    XYChart chart = chart();
    chart.getStyler().setYAxisTickLabelsFormattingFunction(value -> "value " + value);
    chart.getStyler().setYAxisTickMarkSpacingHint(0, 35);
    chart.getStyler().setYAxisTickMarkSpacingHint(1, 180);
    paint(chart);
    assertThat(ticks(chart, 1).size()).isLessThan(ticks(chart, 0).size());
    assertThat(chart.axisPair.getYAxis(1).getAxisTickCalculator().getTickLabels())
        .allMatch(label -> label.startsWith("value "));
  }

  @Test
  void categoryChartUsesNumericYAxisGroupHints() {
    CategoryChart chart = new CategoryChartBuilder().width(800).height(600).build();
    List<String> categories = Arrays.asList("A", "B", "C", "D", "E", "F");
    List<Integer> values = Arrays.asList(0, 20, 40, 60, 80, 100);
    chart.addSeries("first", categories, values);
    chart.addSeries("second", categories, values).setYAxisGroup(1);
    chart.getStyler().setYAxisTickMarkSpacingHint(0, 35);
    chart.getStyler().setYAxisTickMarkSpacingHint(1, 180);
    paint(chart);
    assertThat(chart.axisPair.getYAxis(1).getAxisTickCalculator().getTickLocations().size())
        .isLessThan(chart.axisPair.getYAxis(0).getAxisTickCalculator().getTickLocations().size());
  }

  @Test
  void mergedAxesFollowTheMasterSpacingHint() {
    XYChart chart = chart();
    chart.getStyler().mergeYAxisGroups(0, 1);
    chart.getStyler().setYAxisTickMarkSpacingHint(0, 180);
    chart.getStyler().setYAxisTickMarkSpacingHint(1, 35);
    paint(chart);
    assertThat(ticks(chart, 1)).isEqualTo(ticks(chart, 0));
  }

  @Test
  void smallPlotsKeepExplicitGroupHints() {
    XYStyler styler = new XYStyler();
    styler.setPlotContentSize(1.0);
    styler.setYAxisTickMarkSpacingHint(0, 100);
    AxisTickCalculator_Number wide =
        new AxisTickCalculator_Number(Axis_.Direction.Y, 150, 0, 100, styler, 0);
    styler.setYAxisTickMarkSpacingHint(0, 30);
    AxisTickCalculator_Number narrow =
        new AxisTickCalculator_Number(Axis_.Direction.Y, 150, 0, 100, styler, 0);
    assertThat(wide.getTickLocations().size()).isLessThan(narrow.getTickLocations().size());
    styler.setYAxisTickMarkSpacingHint(0, 200);
    assertThat(
            new AxisTickCalculator_Number(Axis_.Direction.Y, 150, 0, 100, styler, 0)
                .getTickLocations())
        .isEmpty();
  }

  @Test
  void xAxisDoesNotUseYAxisGroupHints() {
    XYStyler styler = new XYStyler();
    AxisTickCalculator_Number before =
        new AxisTickCalculator_Number(Axis_.Direction.X, 800, 0, 100, styler);
    styler.setYAxisTickMarkSpacingHint(0, 500);
    AxisTickCalculator_Number after =
        new AxisTickCalculator_Number(Axis_.Direction.X, 800, 0, 100, styler);
    assertThat(after.getTickLocations()).isEqualTo(before.getTickLocations());
  }

  private XYChart chart() {
    XYChart chart = new XYChartBuilder().width(800).height(600).build();
    double[] x = {0, 1, 2, 3, 4, 5};
    double[] y = {0, 20, 40, 60, 80, 100};
    chart.addSeries("first", x, y);
    chart.addSeries("second", x, y).setYAxisGroup(1);
    return chart;
  }

  private List<Double> ticks(XYChart chart, int group) {
    return new ArrayList<>(
        chart.axisPair.getYAxis(group).getAxisTickCalculator().getTickLocations());
  }

  private void paint(Chart<?, ?> chart) {
    BufferedImage image = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
    Graphics2D graphics = image.createGraphics();
    try {
      chart.paint(graphics, 800, 600);
    } finally {
      graphics.dispose();
    }
  }
}
