package org.knowm.xchart.internal.chartpart;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.IntPredicate;
import org.junit.jupiter.api.Test;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.CategorySeries;
import org.knowm.xchart.CategorySeries.CategorySeriesRenderStyle;
import org.knowm.xchart.HorizontalBarChart;
import org.knowm.xchart.HorizontalBarChartBuilder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.style.AxesChartStyler;
import org.knowm.xchart.style.CategoryStyler;
import org.knowm.xchart.style.HorizontalBarStyler;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.markers.SeriesMarkers;
import org.knowm.xchart.style.theme.GGPlot2Theme;
import org.knowm.xchart.style.theme.XChartTheme;

/**
 * Opt-in bar gradients: flat by default, base-to-value when enabled, including legend swatches.
 * Samples interior pixels instead of golden images.
 */
public class BarFillGradientTest {

  private static final int WIDTH = 640;
  private static final int HEIGHT = 420;

  private static final Color FILL = new Color(40, 70, 160);
  private static final Color FILL_B = new Color(170, 50, 40);
  private static final Color AREA = new Color(140, 90, 20);
  private static final Color STEP = new Color(90, 40, 140);
  private static final Color LINE = new Color(220, 100, 0);
  private static final Color LABEL = new Color(210, 0, 200);
  private static final Color ERROR = new Color(0, 150, 150);
  private static final Color LEGEND_TEXT = new Color(12, 34, 56);
  private static final Color ALPHA_FILL = new Color(80, 40, 160, 128);

  private static final int BRIGHTNESS_DELTA = 20;

  @Test
  public void stylersDefaultToFalseAndKeepTheOptInAcrossThemes() {

    CategoryChart category = new CategoryChartBuilder().width(200).height(150).build();
    CategoryStyler categoryStyler = category.getStyler();
    assertThat(categoryStyler.isBarFillGradientEnabled()).isFalse();
    assertThat(categoryStyler.setBarFillGradientEnabled(true)).isSameAs(categoryStyler);
    assertThat(categoryStyler.isBarFillGradientEnabled()).isTrue();
    categoryStyler.setLabelsVisible(true);
    categoryStyler.setTheme(new GGPlot2Theme());
    assertThat(categoryStyler.isBarFillGradientEnabled()).isTrue();
    assertThat(categoryStyler.isLabelsVisible()).isFalse();
    assertThat(categoryStyler.setBarFillGradientEnabled(false).isBarFillGradientEnabled())
        .isFalse();

    HorizontalBarChart horizontal = new HorizontalBarChartBuilder().width(200).height(150).build();
    HorizontalBarStyler horizontalStyler = horizontal.getStyler();
    assertThat(horizontalStyler.isBarFillGradientEnabled()).isFalse();
    assertThat(horizontalStyler.setBarFillGradientEnabled(true)).isSameAs(horizontalStyler);
    assertThat(horizontalStyler.isBarFillGradientEnabled()).isTrue();
    horizontalStyler.setLabelsVisible(true);
    horizontalStyler.setTheme(new XChartTheme());
    assertThat(horizontalStyler.isBarFillGradientEnabled()).isTrue();
    assertThat(horizontalStyler.isLabelsVisible()).isFalse();
  }

  @Test
  public void endColorLightensSaturatedAndBlackFills() {

    for (Color fill :
        Arrays.asList(Color.RED, Color.GREEN, Color.BLUE, Color.BLACK, new Color(255, 0, 0, 90))) {
      Color end = BarGradient.endColor(fill);
      assertThat(brightness(end)).as("end of %s", fill).isGreaterThan(brightness(fill) + 150);
      assertThat(end.getAlpha()).isEqualTo(fill.getAlpha());
    }
    assertThat(BarGradient.endColor(Color.RED)).isEqualTo(new Color(255, 102, 102));
    assertThat(BarGradient.colorAt(FILL, 0.0)).isEqualTo(FILL);
    assertThat(BarGradient.colorAt(FILL, 1.0)).isEqualTo(BarGradient.endColor(FILL));
    assertThat(BarGradient.colorAt(FILL, 2.0)).isEqualTo(BarGradient.endColor(FILL));
    Color mid = BarGradient.colorAt(FILL, 0.5);
    assertThat(brightness(mid))
        .isStrictlyBetween(brightness(FILL), brightness(BarGradient.endColor(FILL)));
  }

  @Test
  public void pureRedBarsGetAVisibleGradient() {

    CategoryChart chart = categoryChart();
    chart.getStyler().setBarFillGradientEnabled(true);
    chart.getStyler().setYAxisMin(0.0);
    chart.getStyler().setYAxisMax(10.0);
    chart.addSeries("a", Arrays.asList("x"), Arrays.asList(8)).setFillColor(Color.RED);
    BufferedImage image = BitmapEncoder.getBufferedImage(chart);
    assertVerticalGradient(image, onlyBox(image, Color.RED, true), Color.RED, true);

    HorizontalBarChart horizontal = horizontalChart();
    horizontal.getStyler().setBarFillGradientEnabled(true);
    horizontal.getStyler().setXAxisMin(0.0);
    horizontal.getStyler().setXAxisMax(10.0);
    horizontal.addSeries("a", Arrays.asList(8), Arrays.asList("x")).setFillColor(Color.RED);
    BufferedImage hImage = BitmapEncoder.getBufferedImage(horizontal);
    assertHorizontalGradient(hImage, onlyBox(hImage, Color.RED, true), Color.RED, true);
  }

  @Test
  public void automaticLabelColorUsesTheGradientUnderTheLabel() {

    // Flat fill is just dark enough for a light label; the lighter value end needs a dark one.
    Color fill = new Color(110, 110, 110);
    for (boolean gradient : new boolean[] {false, true}) {
      CategoryChart chart = categoryChart();
      chart.getStyler().setBarFillGradientEnabled(gradient);
      chart.getStyler().setYAxisMin(0.0);
      chart.getStyler().setYAxisMax(10.0);
      chart.getStyler().setLabelsVisible(true);
      chart.getStyler().setLabelsPosition(1.0);
      chart.getStyler().setLabelsFontColorAutomaticEnabled(true);
      chart.getStyler().setLabelsFontColorAutomaticLight(Color.WHITE);
      chart.getStyler().setLabelsFontColorAutomaticDark(LABEL);
      chart.addSeries("a", Arrays.asList("x"), Arrays.asList(8)).setFillColor(fill);
      BufferedImage image = BitmapEncoder.getBufferedImage(chart);
      Color expected =
          chart.getStyler().getLabelsFontColor(gradient ? BarGradient.endColor(fill) : fill);
      Color other = expected.equals(LABEL) ? Color.WHITE : LABEL;
      assertThat(expected).isNotEqualTo(other);
      if (gradient) {
        assertThat(expected).isEqualTo(LABEL);
        assertThat(countNear(image, null, LABEL, 2)).isGreaterThan(0);
      } else {
        assertThat(expected).isEqualTo(Color.WHITE);
        assertThat(countNear(image, null, LABEL, 2)).isZero();
      }
    }
  }

  @Test
  public void legendSwatchesFollowAllNegativeBars() {

    CategoryChart chart = categoryChart();
    chart.getStyler().setBarFillGradientEnabled(true);
    chart.getStyler().setLegendVisible(true);
    chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideE);
    chart.getStyler().setLegendBackgroundColor(Color.WHITE);
    chart.getStyler().setYAxisMin(-30.0);
    chart.getStyler().setYAxisMax(-1.0);
    chart.addSeries("bars", Arrays.asList("c"), Arrays.asList(-20)).setFillColor(FILL);
    BufferedImage image = BitmapEncoder.getBufferedImage(chart);
    Rectangle legend = legendRegion(chart);
    assertVerticalGradient(image, onlyBox(image, legend, FILL, true), FILL, false);
    List<Rectangle> bars = boxes(image, null, FILL, true);
    bars.removeIf(legend::intersects);
    assertThat(bars).hasSize(1);
    assertVerticalGradient(image, bars.get(0), FILL, false);

    HorizontalBarChart horizontal = horizontalChart();
    horizontal.getStyler().setBarFillGradientEnabled(true);
    horizontal.getStyler().setLegendVisible(true);
    horizontal.getStyler().setLegendPosition(Styler.LegendPosition.OutsideE);
    horizontal.getStyler().setLegendBackgroundColor(Color.WHITE);
    horizontal.getStyler().setXAxisMin(-30.0);
    horizontal.getStyler().setXAxisMax(-1.0);
    horizontal.addSeries("bars", Arrays.asList(-20), Arrays.asList("c")).setFillColor(FILL);
    BufferedImage hImage = BitmapEncoder.getBufferedImage(horizontal);
    Rectangle hLegend = legendRegion(horizontal);
    assertHorizontalGradient(hImage, onlyBox(hImage, hLegend, FILL, true), FILL, false);
  }

  @Test
  public void vectorGraphicsGetTheFlatFill() {

    de.erichseifert.vectorgraphics2d.VectorGraphics2D vg =
        new de.erichseifert.vectorgraphics2d.VectorGraphics2D();
    vg.setColor(Color.GRAY);
    BarGradient.fill(vg, new Rectangle(0, 0, 10, 10), FILL, true, 0, 10, 0, 0);
    assertThat(vg.getPaint()).isEqualTo(FILL);
    assertThat(vg.getColor()).isEqualTo(FILL);
  }

  @Test
  public void barsAreFlatByDefaultAndAfterDisabling() {

    CategoryChart category = categoryChart();
    category.getStyler().setYAxisMin(0.0);
    category.getStyler().setYAxisMax(10.0);
    category.addSeries("a", Arrays.asList("c"), Arrays.asList(10)).setFillColor(FILL);

    BufferedImage flat = BitmapEncoder.getBufferedImage(category);
    Rectangle flatBox = onlyBox(flat, FILL, false);
    assertFlatVertical(flat, flatBox, FILL);

    category.getStyler().setBarFillGradientEnabled(true);
    BufferedImage gradient = BitmapEncoder.getBufferedImage(category);
    Rectangle gradientBox = onlyBox(gradient, FILL, true);
    assertClose(flatBox, gradientBox);
    assertVerticalGradient(gradient, gradientBox, FILL, true);

    category.getStyler().setBarFillGradientEnabled(false);
    BufferedImage disabled = BitmapEncoder.getBufferedImage(category);
    assertFlatVertical(disabled, onlyBox(disabled, FILL, false), FILL);

    HorizontalBarChart horizontal = horizontalChart();
    horizontal.getStyler().setXAxisMin(0.0);
    horizontal.getStyler().setXAxisMax(10.0);
    horizontal.addSeries("a", Arrays.asList(10), Arrays.asList("c")).setFillColor(FILL);

    BufferedImage flatH = BitmapEncoder.getBufferedImage(horizontal);
    assertFlatHorizontal(flatH, onlyBox(flatH, FILL, false), FILL);

    horizontal.getStyler().setBarFillGradientEnabled(true);
    BufferedImage gradientH = BitmapEncoder.getBufferedImage(horizontal);
    assertHorizontalGradient(gradientH, onlyBox(gradientH, FILL, true), FILL, true);

    horizontal.getStyler().setBarFillGradientEnabled(false);
    BufferedImage disabledH = BitmapEncoder.getBufferedImage(horizontal);
    assertFlatHorizontal(disabledH, onlyBox(disabledH, FILL, false), FILL);
  }

  @Test
  public void negativeBarsAreBrighterAwayFromZero() {

    CategoryChart span = categoryChart();
    span.getStyler().setBarFillGradientEnabled(true);
    span.getStyler().setYAxisMin(-10.0);
    span.getStyler().setYAxisMax(10.0);
    span.addSeries("a", Arrays.asList("p", "n"), Arrays.asList(8, -8)).setFillColor(FILL);
    BufferedImage spanImage = BitmapEncoder.getBufferedImage(span);
    List<Rectangle> spanBoxes = boxes(spanImage, null, FILL, true);
    assertThat(spanBoxes).hasSize(2);
    assertVerticalGradient(spanImage, spanBoxes.get(0), FILL, true);
    assertVerticalGradient(spanImage, spanBoxes.get(1), FILL, false);

    CategoryChart negativeOnly = categoryChart();
    negativeOnly.getStyler().setBarFillGradientEnabled(true);
    negativeOnly.getStyler().setYAxisMin(-30.0);
    negativeOnly.getStyler().setYAxisMax(-1.0);
    negativeOnly.addSeries("a", Arrays.asList("n"), Arrays.asList(-20)).setFillColor(FILL);
    BufferedImage negativeImage = BitmapEncoder.getBufferedImage(negativeOnly);
    assertVerticalGradient(negativeImage, onlyBox(negativeImage, FILL, true), FILL, false);

    HorizontalBarChart spanH = horizontalChart();
    spanH.getStyler().setBarFillGradientEnabled(true);
    spanH.getStyler().setXAxisMin(-10.0);
    spanH.getStyler().setXAxisMax(10.0);
    spanH.addSeries("a", Arrays.asList(8, -8), Arrays.asList("p", "n")).setFillColor(FILL);
    BufferedImage spanHImage = BitmapEncoder.getBufferedImage(spanH);
    List<Rectangle> spanHBoxes = boxes(spanHImage, null, FILL, true);
    spanHBoxes.sort(Comparator.comparingInt((Rectangle r) -> r.y).thenComparingInt(r -> r.x));
    assertThat(spanHBoxes).hasSize(2);
    assertHorizontalGradient(spanHImage, spanHBoxes.get(0), FILL, true);
    assertHorizontalGradient(spanHImage, spanHBoxes.get(1), FILL, false);

    HorizontalBarChart negativeH = horizontalChart();
    negativeH.getStyler().setBarFillGradientEnabled(true);
    negativeH.getStyler().setXAxisMin(-30.0);
    negativeH.getStyler().setXAxisMax(-1.0);
    negativeH.addSeries("a", Arrays.asList(-20), Arrays.asList("n")).setFillColor(FILL);
    BufferedImage negativeHImage = BitmapEncoder.getBufferedImage(negativeH);
    assertHorizontalGradient(negativeHImage, onlyBox(negativeHImage, FILL, true), FILL, false);
  }

  @Test
  public void groupedAndStackedSegmentsKeepGeometryAndDirection() {

    CategoryChart groupedOff = grouped(false);
    CategoryChart groupedOn = grouped(true);
    BufferedImage groupedOffImage = BitmapEncoder.getBufferedImage(groupedOff);
    BufferedImage groupedOnImage = BitmapEncoder.getBufferedImage(groupedOn);
    assertSameYRange(groupedOff, groupedOn);
    Rectangle groupedAOff = onlyBox(groupedOffImage, FILL, false);
    Rectangle groupedBOff = onlyBox(groupedOffImage, FILL_B, false);
    Rectangle groupedAOn = onlyBox(groupedOnImage, FILL, true);
    Rectangle groupedBOn = onlyBox(groupedOnImage, FILL_B, true);
    assertClose(groupedAOff, groupedAOn);
    assertClose(groupedBOff, groupedBOn);
    assertFlatVertical(groupedOffImage, groupedAOff, FILL);
    assertVerticalGradient(groupedOnImage, groupedAOn, FILL, true);
    assertVerticalGradient(groupedOnImage, groupedBOn, FILL_B, true);

    CategoryChart stackedOff = stacked(false);
    CategoryChart stackedOn = stacked(true);
    BufferedImage stackedOffImage = BitmapEncoder.getBufferedImage(stackedOff);
    BufferedImage stackedOnImage = BitmapEncoder.getBufferedImage(stackedOn);
    assertSameYRange(stackedOff, stackedOn);

    List<Rectangle> aOff = boxes(stackedOffImage, null, FILL, false);
    List<Rectangle> bOff = boxes(stackedOffImage, null, FILL_B, false);
    List<Rectangle> aOn = boxes(stackedOnImage, null, FILL, true);
    List<Rectangle> bOn = boxes(stackedOnImage, null, FILL_B, true);
    assertThat(aOn).hasSize(2);
    assertThat(bOn).hasSize(2);
    assertClose(aOff.get(0), aOn.get(0));
    assertClose(aOff.get(1), aOn.get(1));
    assertClose(bOff.get(0), bOn.get(0));
    assertClose(bOff.get(1), bOn.get(1));

    // Left category is the positive stack (B on top of A); right category is the negative stack.
    assertVerticalGradient(stackedOnImage, aOn.get(0), FILL, true);
    assertVerticalGradient(stackedOnImage, bOn.get(0), FILL_B, true);
    assertThat(Math.abs(aOn.get(0).y - (bOn.get(0).y + bOn.get(0).height))).isLessThanOrEqualTo(2);
    assertVerticalGradient(stackedOnImage, aOn.get(1), FILL, false);
    assertVerticalGradient(stackedOnImage, bOn.get(1), FILL_B, false);
    assertThat(Math.abs(bOn.get(1).y - (aOn.get(1).y + aOn.get(1).height))).isLessThanOrEqualTo(2);
    assertFlatVertical(stackedOffImage, aOff.get(0), FILL);
    assertFlatVertical(stackedOffImage, bOff.get(1), FILL_B);
  }

  @Test
  public void zeroLengthNullClippingAndLogAxisRender() {

    CategoryChart sparse = categoryChart();
    sparse.getStyler().setBarFillGradientEnabled(true);
    sparse.getStyler().setYAxisMin(-2.0);
    sparse.getStyler().setYAxisMax(10.0);
    sparse
        .addSeries("a", Arrays.asList("z", "missing", "value"), Arrays.asList(0, null, 8))
        .setFillColor(FILL);
    BufferedImage sparseImage = BitmapEncoder.getBufferedImage(sparse);
    List<Rectangle> sparseBoxes = boxes(sparseImage, null, FILL, true);
    assertThat(sparseBoxes).hasSize(1);
    assertVerticalGradient(sparseImage, sparseBoxes.get(0), FILL, true);

    CategoryChart clipped = categoryChart();
    clipped.getStyler().setBarFillGradientEnabled(true);
    clipped.getStyler().setYAxisMin(4.0);
    clipped.getStyler().setYAxisMax(8.0);
    clipped
        .addSeries("a", Arrays.asList("below", "inside", "above"), Arrays.asList(1, 6, 12))
        .setFillColor(FILL);
    BufferedImage clippedImage = BitmapEncoder.getBufferedImage(clipped);
    List<Rectangle> clippedBoxes = boxes(clippedImage, null, FILL, true);
    assertThat(clippedBoxes).hasSize(2);
    assertVerticalGradient(clippedImage, clippedBoxes.get(0), FILL, true);
    assertVerticalGradient(clippedImage, clippedBoxes.get(1), FILL, true);
    assertThat(clippedBoxes.get(1).height).isGreaterThan(clippedBoxes.get(0).height);
    // The bar above yAxisMax is clipped, but its gradient is clamped to the plot, so the visible
    // top still reaches the end color.
    assertEndsNear(clippedImage, clippedBoxes.get(1), FILL, true);

    CategoryChart logOff = logCategory(false);
    CategoryChart logOn = logCategory(true);
    BufferedImage logOffImage = BitmapEncoder.getBufferedImage(logOff);
    BufferedImage logOnImage = BitmapEncoder.getBufferedImage(logOn);
    assertSameYRange(logOff, logOn);
    assertThat(yAxis(logOn).getMin()).isEqualTo(0.1);
    assertThat(yAxis(logOn).getMax()).isEqualTo(10.0);
    List<Rectangle> logOffBoxes = boxes(logOffImage, null, FILL, false);
    List<Rectangle> logOnBoxes = boxes(logOnImage, null, FILL, true);
    assertThat(logOnBoxes).hasSize(2);
    assertClose(logOffBoxes.get(0), logOnBoxes.get(0));
    assertClose(logOffBoxes.get(1), logOnBoxes.get(1));
    // 0.5 transforms to a negative log but still grows up from the positive axis minimum.
    assertVerticalGradient(logOnImage, logOnBoxes.get(0), FILL, true);
    assertVerticalGradient(logOnImage, logOnBoxes.get(1), FILL, true);

    HorizontalBarChart sparseH = horizontalChart();
    sparseH.getStyler().setBarFillGradientEnabled(true);
    sparseH.getStyler().setXAxisMin(-2.0);
    sparseH.getStyler().setXAxisMax(10.0);
    sparseH
        .addSeries("a", Arrays.asList(0, null, 8), Arrays.asList("z", "missing", "value"))
        .setFillColor(FILL);
    BufferedImage sparseHImage = BitmapEncoder.getBufferedImage(sparseH);
    List<Rectangle> sparseHBoxes = boxes(sparseHImage, null, FILL, true);
    sparseHBoxes.sort(Comparator.comparingInt((Rectangle r) -> r.y));
    assertThat(sparseHBoxes).hasSize(1);
    assertHorizontalGradient(sparseHImage, sparseHBoxes.get(0), FILL, true);

    HorizontalBarChart clippedH = horizontalChart();
    clippedH.getStyler().setBarFillGradientEnabled(true);
    clippedH.getStyler().setXAxisMin(4.0);
    clippedH.getStyler().setXAxisMax(8.0);
    clippedH
        .addSeries("a", Arrays.asList(1, 6, 12), Arrays.asList("below", "inside", "above"))
        .setFillColor(FILL);
    BufferedImage clippedHImage = BitmapEncoder.getBufferedImage(clippedH);
    List<Rectangle> clippedHBoxes = boxes(clippedHImage, null, FILL, true);
    clippedHBoxes.sort(Comparator.comparingInt((Rectangle r) -> r.y));
    assertThat(clippedHBoxes).hasSize(2);
    assertHorizontalGradient(clippedHImage, clippedHBoxes.get(0), FILL, true);
    assertHorizontalGradient(clippedHImage, clippedHBoxes.get(1), FILL, true);
    assertThat(clippedHBoxes.get(1).width).isGreaterThan(clippedHBoxes.get(0).width);
    assertEndsNear(clippedHImage, clippedHBoxes.get(1), FILL, false);

    HorizontalBarChart logHOff = logHorizontal(false);
    HorizontalBarChart logHOn = logHorizontal(true);
    BufferedImage logHOffImage = BitmapEncoder.getBufferedImage(logHOff);
    BufferedImage logHOnImage = BitmapEncoder.getBufferedImage(logHOn);
    assertSameXRange(logHOff, logHOn);
    assertThat(xAxis(logHOn).getMin()).isEqualTo(0.1);
    assertThat(xAxis(logHOn).getMax()).isEqualTo(10.0);
    List<Rectangle> logHOffBoxes = boxes(logHOffImage, null, FILL, false);
    List<Rectangle> logHOnBoxes = boxes(logHOnImage, null, FILL, true);
    logHOffBoxes.sort(Comparator.comparingInt((Rectangle r) -> r.y));
    logHOnBoxes.sort(Comparator.comparingInt((Rectangle r) -> r.y));
    assertThat(logHOnBoxes).hasSize(2);
    assertClose(logHOffBoxes.get(0), logHOnBoxes.get(0));
    assertClose(logHOffBoxes.get(1), logHOnBoxes.get(1));
    assertHorizontalGradient(logHOnImage, logHOnBoxes.get(0), FILL, true);
    assertHorizontalGradient(logHOnImage, logHOnBoxes.get(1), FILL, true);
  }

  @Test
  public void legendSwatchesUseThePositiveBarGradient() {

    for (Styler.LegendLayout layout : Styler.LegendLayout.values()) {
      CategoryChart enabled = legendCategory(true, layout);
      BufferedImage enabledImage = BitmapEncoder.getBufferedImage(enabled);
      Rectangle swatch = onlyBox(enabledImage, legendRegion(enabled), FILL, true);
      assertThat(swatch.width).isBetween(14, 24);
      assertThat(swatch.height).isBetween(14, 24);
      assertVerticalGradient(enabledImage, swatch, FILL, true);

      CategoryChart disabled = legendCategory(false, layout);
      BufferedImage disabledImage = BitmapEncoder.getBufferedImage(disabled);
      assertFlatVertical(
          disabledImage, onlyBox(disabledImage, legendRegion(disabled), FILL, false), FILL);

      HorizontalBarChart enabledH = legendHorizontal(true, layout);
      BufferedImage enabledHImage = BitmapEncoder.getBufferedImage(enabledH);
      Rectangle swatchH = onlyBox(enabledHImage, legendRegion(enabledH), FILL, true);
      assertThat(swatchH.width).isBetween(14, 24);
      assertThat(swatchH.height).isBetween(14, 24);
      assertHorizontalGradient(enabledHImage, swatchH, FILL, true);

      HorizontalBarChart disabledH = legendHorizontal(false, layout);
      BufferedImage disabledHImage = BitmapEncoder.getBufferedImage(disabledH);
      assertFlatHorizontal(
          disabledHImage, onlyBox(disabledHImage, legendRegion(disabledH), FILL, false), FILL);
    }
  }

  @Test
  public void nonBarSeriesAndXyAreaStayFlat() {

    CategoryChart mixed = categoryChart();
    mixed.getStyler().setBarFillGradientEnabled(true);
    mixed.getStyler().setLegendVisible(true);
    mixed.getStyler().setLegendPosition(Styler.LegendPosition.OutsideE);
    mixed.getStyler().setYAxisMin(0.0);
    mixed.getStyler().setYAxisMax(10.0);

    CategorySeries area = mixed.addSeries("area", Arrays.asList("a", "b"), Arrays.asList(6, 7));
    area.setChartCategorySeriesRenderStyle(CategorySeriesRenderStyle.Area);
    area.setFillColor(AREA);
    area.setLineColor(Color.BLACK);
    area.setMarker(SeriesMarkers.NONE);

    CategorySeries step = mixed.addSeries("step", Arrays.asList("a", "b"), Arrays.asList(5, 8));
    step.setChartCategorySeriesRenderStyle(CategorySeriesRenderStyle.SteppedBar);
    step.setFillColor(STEP);
    step.setLineColor(Color.BLACK);

    CategorySeries line = mixed.addSeries("line", Arrays.asList("a", "b"), Arrays.asList(4, 6));
    line.setChartCategorySeriesRenderStyle(CategorySeriesRenderStyle.Line);
    line.setLineColor(LINE);
    line.setMarker(SeriesMarkers.NONE);

    CategorySeries bar = mixed.addSeries("bar", Arrays.asList("a", "b"), Arrays.asList(7, 7));
    bar.setChartCategorySeriesRenderStyle(CategorySeriesRenderStyle.Bar);
    bar.setFillColor(FILL);

    BufferedImage image = BitmapEncoder.getBufferedImage(mixed);
    assertSolid(image, AREA);
    assertSolid(image, STEP);
    assertSolid(image, LINE);
    assertVerticalGradient(image, largestBox(image, null, FILL, true), FILL, true);

    Rectangle legend = legendRegion(mixed);
    assertVerticalGradient(image, onlyBox(image, legend, FILL, true), FILL, true);
    assertFlatVertical(image, onlyBox(image, legend, STEP, false), STEP);
    assertThat(boxes(image, legend, AREA, false)).isEmpty();
    assertThat(boxes(image, legend, LINE, true)).isEmpty();

    XYChart xy = new XYChartBuilder().width(WIDTH).height(HEIGHT).build();
    prepareAxes(xy.getStyler());
    xy.getStyler().setLegendVisible(true);
    xy.getStyler().setXAxisMin(0.0);
    xy.getStyler().setXAxisMax(10.0);
    xy.getStyler().setYAxisMin(0.0);
    xy.getStyler().setYAxisMax(10.0);
    XYSeries xyArea = xy.addSeries("area", new double[] {0, 5, 10}, new double[] {4, 8, 6});
    xyArea.setXYSeriesRenderStyle(XYSeriesRenderStyle.Area);
    xyArea.setFillColor(AREA);
    xyArea.setLineColor(Color.BLACK);
    xyArea.setMarker(SeriesMarkers.NONE);
    BufferedImage xyImage = BitmapEncoder.getBufferedImage(xy);
    assertSolid(xyImage, AREA);
    assertThat(boxes(xyImage, legendRegion(xy), AREA, true)).isEmpty();
  }

  @Test
  public void paintAfterGradientKeepsSolidColors() {

    CategoryChart chart = categoryChart();
    chart.getStyler().setBarFillGradientEnabled(true);
    chart.getStyler().setLegendVisible(true);
    chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideE);
    chart.getStyler().setChartFontColor(LEGEND_TEXT);
    chart.getStyler().setLegendFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
    chart.getStyler().setLabelsVisible(true);
    chart.getStyler().setLabelsFontColorAutomaticEnabled(false);
    chart.getStyler().setLabelsFontColor(LABEL);
    chart.getStyler().setLabelsFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
    chart.getStyler().setYAxisDecimalPattern("0");
    chart.getStyler().setErrorBarsColor(ERROR);
    chart.getStyler().setErrorBarsColorSeriesColor(false);
    chart.getStyler().setYAxisMin(0.0);
    chart.getStyler().setYAxisMax(10.0);

    CategorySeries bar =
        chart.addSeries("bar", Arrays.asList("a", "b"), Arrays.asList(6, 6), Arrays.asList(1, 1));
    bar.setFillColor(FILL);
    bar.setChartCategorySeriesRenderStyle(CategorySeriesRenderStyle.Bar);

    CategorySeries line = chart.addSeries("line", Arrays.asList("a", "b"), Arrays.asList(3, 5));
    line.setChartCategorySeriesRenderStyle(CategorySeriesRenderStyle.Line);
    line.setLineColor(LINE);
    line.setMarker(SeriesMarkers.NONE);

    BufferedImage image = BitmapEncoder.getBufferedImage(chart);
    assertVerticalGradient(image, largestBox(image, null, FILL, true), FILL, true);
    assertThat(countNear(image, null, LABEL, 2)).isGreaterThan(5);
    assertThat(countNear(image, null, ERROR, 2)).isGreaterThan(8);
    assertThat(countNear(image, legendRegion(chart), LEGEND_TEXT, 2)).isGreaterThan(5);
    assertSolid(image, LINE);

    HorizontalBarChart horizontal = horizontalChart();
    horizontal.getStyler().setBarFillGradientEnabled(true);
    horizontal.getStyler().setLegendVisible(true);
    horizontal.getStyler().setLegendPosition(Styler.LegendPosition.OutsideE);
    horizontal.getStyler().setChartFontColor(LEGEND_TEXT);
    horizontal.getStyler().setLegendFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
    horizontal.getStyler().setLabelsVisible(true);
    horizontal.getStyler().setLabelsFontColorAutomaticEnabled(false);
    horizontal.getStyler().setLabelsFontColor(LABEL);
    horizontal.getStyler().setLabelsFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
    horizontal.getStyler().setXAxisDecimalPattern("0");
    horizontal.getStyler().setXAxisMin(0.0);
    horizontal.getStyler().setXAxisMax(10.0);
    horizontal.addSeries("first", Arrays.asList(6), Arrays.asList("c")).setFillColor(FILL);
    horizontal.addSeries("later", Arrays.asList(8), Arrays.asList("c")).setFillColor(FILL_B);

    BufferedImage horizontalImage = BitmapEncoder.getBufferedImage(horizontal);
    assertHorizontalGradient(
        horizontalImage, largestBox(horizontalImage, null, FILL, true), FILL, true);
    assertHorizontalGradient(
        horizontalImage, largestBox(horizontalImage, null, FILL_B, true), FILL_B, true);
    assertThat(countNear(horizontalImage, null, LABEL, 2)).isGreaterThan(5);
    assertThat(countNear(horizontalImage, legendRegion(horizontal), LEGEND_TEXT, 2))
        .isGreaterThan(5);
  }

  @Test
  public void alphaFillIsPreserved() {

    CategoryChart category = categoryChart();
    category.getStyler().setBarFillGradientEnabled(true);
    category.getStyler().setYAxisMin(0.0);
    category.getStyler().setYAxisMax(10.0);
    category.addSeries("a", Arrays.asList("c"), Arrays.asList(10)).setFillColor(ALPHA_FILL);
    BufferedImage image = BitmapEncoder.getBufferedImage(category);
    assertAlphaGradient(image, alphaBox(image), true);

    HorizontalBarChart horizontal = horizontalChart();
    horizontal.getStyler().setBarFillGradientEnabled(true);
    horizontal.getStyler().setXAxisMin(0.0);
    horizontal.getStyler().setXAxisMax(10.0);
    horizontal.addSeries("a", Arrays.asList(10), Arrays.asList("c")).setFillColor(ALPHA_FILL);
    BufferedImage horizontalImage = BitmapEncoder.getBufferedImage(horizontal);
    assertAlphaGradient(horizontalImage, alphaBox(horizontalImage), false);
  }

  private static CategoryChart grouped(boolean gradient) {

    CategoryChart chart = categoryChart();
    chart.getStyler().setBarFillGradientEnabled(gradient);
    chart.getStyler().setYAxisMin(0.0);
    chart.getStyler().setYAxisMax(10.0);
    chart.addSeries("a", Arrays.asList("c"), Arrays.asList(6)).setFillColor(FILL);
    chart.addSeries("b", Arrays.asList("c"), Arrays.asList(8)).setFillColor(FILL_B);
    return chart;
  }

  private static CategoryChart stacked(boolean gradient) {

    CategoryChart chart = categoryChart();
    chart.getStyler().setBarFillGradientEnabled(gradient);
    chart.getStyler().setStacked(true);
    chart.addSeries("a", Arrays.asList("p", "n"), Arrays.asList(4, -3)).setFillColor(FILL);
    chart.addSeries("b", Arrays.asList("p", "n"), Arrays.asList(5, -2)).setFillColor(FILL_B);
    return chart;
  }

  private static CategoryChart logCategory(boolean gradient) {

    CategoryChart chart = categoryChart();
    chart.getStyler().setBarFillGradientEnabled(gradient);
    chart.getStyler().setYAxisLogarithmic(true);
    chart.getStyler().setYAxisMin(0.1);
    chart.getStyler().setYAxisMax(10.0);
    chart
        .addSeries("a", Arrays.asList("small", "min", "large"), Arrays.asList(0.5, 0.1, 5))
        .setFillColor(FILL);
    return chart;
  }

  private static HorizontalBarChart logHorizontal(boolean gradient) {

    HorizontalBarChart chart = horizontalChart();
    chart.getStyler().setBarFillGradientEnabled(gradient);
    chart.getStyler().setXAxisLogarithmic(true);
    chart.getStyler().setXAxisMin(0.1);
    chart.getStyler().setXAxisMax(10.0);
    chart
        .addSeries("a", Arrays.asList(0.5, 0.1, 5), Arrays.asList("small", "min", "large"))
        .setFillColor(FILL);
    return chart;
  }

  private static CategoryChart legendCategory(boolean gradient, Styler.LegendLayout layout) {

    CategoryChart chart = categoryChart();
    chart.getStyler().setBarFillGradientEnabled(gradient);
    chart.getStyler().setLegendVisible(true);
    chart.getStyler().setLegendLayout(layout);
    chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideE);
    chart.getStyler().setLegendBackgroundColor(Color.WHITE);
    chart.getStyler().setYAxisMin(0.0);
    chart.getStyler().setYAxisMax(10.0);
    chart.addSeries("bars", Arrays.asList("c"), Arrays.asList(8)).setFillColor(FILL);
    return chart;
  }

  private static HorizontalBarChart legendHorizontal(boolean gradient, Styler.LegendLayout layout) {

    HorizontalBarChart chart = horizontalChart();
    chart.getStyler().setBarFillGradientEnabled(gradient);
    chart.getStyler().setLegendVisible(true);
    chart.getStyler().setLegendLayout(layout);
    chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideE);
    chart.getStyler().setLegendBackgroundColor(Color.WHITE);
    chart.getStyler().setXAxisMin(0.0);
    chart.getStyler().setXAxisMax(10.0);
    chart.addSeries("bars", Arrays.asList(8), Arrays.asList("c")).setFillColor(FILL);
    return chart;
  }

  private static CategoryChart categoryChart() {

    CategoryChart chart = new CategoryChartBuilder().width(WIDTH).height(HEIGHT).build();
    prepare(chart.getStyler());
    return chart;
  }

  private static HorizontalBarChart horizontalChart() {

    HorizontalBarChart chart = new HorizontalBarChartBuilder().width(WIDTH).height(HEIGHT).build();
    prepare(chart.getStyler());
    return chart;
  }

  private static void prepare(CategoryStyler styler) {

    prepareAxes(styler);
    styler.setAvailableSpaceFill(0.8);
  }

  private static void prepare(HorizontalBarStyler styler) {

    prepareAxes(styler);
    styler.setAvailableSpaceFill(0.8);
  }

  private static void prepareAxes(AxesChartStyler styler) {

    styler.setAntiAlias(false);
    styler.setTextAntiAlias(false);
    styler.setPlotGridLinesVisible(false);
    styler.setPlotTicksMarksVisible(false);
    styler.setAxisTicksMarksVisible(false);
    styler.setLegendVisible(false);
    styler.setChartTitleVisible(false);
    styler.setAxisTitlesVisible(false);
    styler.setPlotBorderVisible(false);
    styler.setPlotBackgroundColor(Color.WHITE);
    styler.setChartBackgroundColor(Color.WHITE);
  }

  private static void assertSameYRange(Chart<?, ?> flat, Chart<?, ?> gradient) {

    assertThat(yAxis(gradient).getMin()).isEqualTo(yAxis(flat).getMin());
    assertThat(yAxis(gradient).getMax()).isEqualTo(yAxis(flat).getMax());
  }

  private static void assertSameXRange(Chart<?, ?> flat, Chart<?, ?> gradient) {

    assertThat(xAxis(gradient).getMin()).isEqualTo(xAxis(flat).getMin());
    assertThat(xAxis(gradient).getMax()).isEqualTo(xAxis(flat).getMax());
  }

  private static Axis_Y<?, ?> yAxis(Chart<?, ?> chart) {

    return ((AxesChart<?, ?>) chart).getYAxis();
  }

  private static Axis_X<?, ?> xAxis(Chart<?, ?> chart) {

    return ((AxesChart<?, ?>) chart).getXAxis();
  }

  private static Rectangle legendRegion(Chart<?, ?> chart) {

    Legend_<?, ?> legend = ((Chart<?, ?>) chart).getLegend();
    int x = (int) Math.floor(legend.xOffset);
    int y = (int) Math.floor(legend.yOffset);
    int w = (int) Math.ceil(legend.getBounds().getWidth());
    int h = (int) Math.ceil(legend.getBounds().getHeight());
    return new Rectangle(x, y, w, h);
  }

  private static void assertVerticalGradient(
      BufferedImage image, Rectangle box, Color fill, boolean brighterAtTop) {

    Color top = new Color(image.getRGB(sampleX(box), box.y + inset(box.height)));
    Color bottom =
        new Color(image.getRGB(sampleX(box), box.y + box.height - 1 - inset(box.height)));
    Color valueEnd = brighterAtTop ? top : bottom;
    Color baseEnd = brighterAtTop ? bottom : top;
    assertEndpointOrder(valueEnd, baseEnd, fill);
  }

  private static void assertHorizontalGradient(
      BufferedImage image, Rectangle box, Color fill, boolean brighterAtRight) {

    int y = box.y + box.height / 3;
    Color left = new Color(image.getRGB(box.x + inset(box.width), y));
    Color right = new Color(image.getRGB(box.x + box.width - 1 - inset(box.width), y));
    Color valueEnd = brighterAtRight ? right : left;
    Color baseEnd = brighterAtRight ? left : right;
    assertEndpointOrder(valueEnd, baseEnd, fill);
  }

  private static void assertFlatVertical(BufferedImage image, Rectangle box, Color fill) {

    Color top = new Color(image.getRGB(sampleX(box), box.y + inset(box.height)));
    Color bottom =
        new Color(image.getRGB(sampleX(box), box.y + box.height - 1 - inset(box.height)));
    assertFlat(top, bottom, fill);
  }

  private static void assertFlatHorizontal(BufferedImage image, Rectangle box, Color fill) {

    int y = box.y + box.height / 2;
    Color left = new Color(image.getRGB(box.x + inset(box.width), y));
    Color right = new Color(image.getRGB(box.x + box.width - 1 - inset(box.width), y));
    assertFlat(left, right, fill);
  }

  private static void assertEndsNear(
      BufferedImage image, Rectangle box, Color fill, boolean vertical) {

    Color valueEnd;
    if (vertical) {
      valueEnd = new Color(image.getRGB(sampleX(box), box.y + 1));
    } else {
      valueEnd = new Color(image.getRGB(box.x + box.width - 2, box.y + box.height / 3));
    }
    assertThat(maxChannel(valueEnd, BarGradient.endColor(fill)))
        .as("visible value end %s vs end color %s", valueEnd, BarGradient.endColor(fill))
        .isLessThanOrEqualTo(6);
  }

  private static void assertEndpointOrder(Color valueEnd, Color baseEnd, Color fill) {

    Color bright = BarGradient.endColor(fill);
    assertThat(brightness(valueEnd))
        .as(
            "value end %s should be brighter than base %s (fill %s, bright %s)",
            valueEnd, baseEnd, fill, bright)
        .isGreaterThan(brightness(baseEnd) + BRIGHTNESS_DELTA);
    assertThat(maxChannel(valueEnd, bright)).isLessThan(maxChannel(valueEnd, fill));
    assertThat(maxChannel(baseEnd, fill)).isLessThan(maxChannel(baseEnd, bright));
  }

  private static void assertFlat(Color a, Color b, Color fill) {

    assertThat(maxChannel(a, fill)).as("sample %s vs fill %s", a, fill).isLessThanOrEqualTo(4);
    assertThat(maxChannel(b, fill)).as("sample %s vs fill %s", b, fill).isLessThanOrEqualTo(4);
    assertThat(Math.abs(brightness(a) - brightness(b))).isLessThanOrEqualTo(6);
  }

  private static void assertAlphaGradient(BufferedImage image, Rectangle box, boolean vertical) {

    Color base;
    Color tip;
    if (vertical) {
      int x = sampleX(box);
      tip = new Color(image.getRGB(x, box.y + inset(box.height)));
      base = new Color(image.getRGB(x, box.y + box.height - 1 - inset(box.height)));
    } else {
      int y = box.y + box.height / 3;
      base = new Color(image.getRGB(box.x + inset(box.width), y));
      tip = new Color(image.getRGB(box.x + box.width - 1 - inset(box.width), y));
    }
    Color blendedFill = blend(ALPHA_FILL, Color.WHITE);
    Color blendedBright = blend(BarGradient.endColor(ALPHA_FILL), Color.WHITE);
    Color opaqueFill = new Color(ALPHA_FILL.getRed(), ALPHA_FILL.getGreen(), ALPHA_FILL.getBlue());
    Color opaqueBright =
        new Color(
            BarGradient.endColor(ALPHA_FILL).getRed(),
            BarGradient.endColor(ALPHA_FILL).getGreen(),
            BarGradient.endColor(ALPHA_FILL).getBlue());
    assertThat(brightness(tip)).isGreaterThan(brightness(base) + BRIGHTNESS_DELTA);
    assertThat(maxChannel(base, blendedFill)).isLessThan(maxChannel(base, opaqueFill));
    assertThat(maxChannel(tip, blendedBright)).isLessThan(maxChannel(tip, opaqueBright));
  }

  private static void assertSolid(BufferedImage image, Color color) {

    // The gradient end is the fill blended toward white, so a translucent flat fill over a white
    // plot also lands in that range. A gradient shows up as many distinct shades instead.
    int flat = countNear(image, null, color, 4);
    java.util.Set<Integer> shades = new java.util.HashSet<>();
    int w = image.getWidth();
    int h = image.getHeight();
    int[] pixels = image.getRGB(0, 0, w, h, null, 0, w);
    for (int rgb : pixels) {
      if (maxChannel(rgb, color) > 12 && inGradient(rgb, color, 3)) {
        shades.add(rgb & 0xFFFFFF);
      }
    }
    assertThat(flat).as("flat pixels of %s", color).isGreaterThan(30);
    assertThat(shades.size()).as("gradient shades of %s: %s", color, shades).isLessThanOrEqualTo(4);
  }

  private static int countNear(BufferedImage image, Rectangle region, Color color, int tolerance) {

    int x0 = 0;
    int y0 = 0;
    int x1 = image.getWidth();
    int y1 = image.getHeight();
    if (region != null) {
      x0 = Math.max(0, region.x);
      y0 = Math.max(0, region.y);
      x1 = Math.min(image.getWidth(), region.x + region.width);
      y1 = Math.min(image.getHeight(), region.y + region.height);
    }
    int w = image.getWidth();
    int[] pixels = image.getRGB(0, 0, w, image.getHeight(), null, 0, w);
    int count = 0;
    for (int y = y0; y < y1; y++) {
      for (int x = x0; x < x1; x++) {
        if (maxChannel(pixels[y * w + x], color) <= tolerance) {
          count++;
        }
      }
    }
    return count;
  }

  private static Rectangle alphaBox(BufferedImage image) {

    Color start = blend(ALPHA_FILL, Color.WHITE);
    Color end = blend(BarGradient.endColor(ALPHA_FILL), Color.WHITE);
    List<Rectangle> found = boxes(image, null, rgb -> betweenColors(rgb, start, end, 4));
    assertThat(found).as("alpha gradient box").hasSize(1);
    return found.get(0);
  }

  private static Rectangle onlyBox(BufferedImage image, Color fill, boolean gradient) {

    return onlyBox(image, null, fill, gradient);
  }

  private static Rectangle onlyBox(
      BufferedImage image, Rectangle region, Color fill, boolean gradient) {

    List<Rectangle> found = boxes(image, region, fill, gradient);
    assertThat(found).as("boxes for %s gradient=%s", fill, gradient).hasSize(1);
    return found.get(0);
  }

  private static Rectangle largestBox(
      BufferedImage image, Rectangle region, Color fill, boolean gradient) {

    List<Rectangle> found = boxes(image, region, fill, gradient);
    assertThat(found).as("boxes for %s", fill).isNotEmpty();
    Rectangle largest = found.get(0);
    for (Rectangle box : found) {
      if (box.width * box.height > largest.width * largest.height) {
        largest = box;
      }
    }
    return largest;
  }

  private static List<Rectangle> boxes(
      BufferedImage image, Rectangle region, Color fill, boolean gradient) {

    return boxes(image, region, rgb -> match(rgb, fill, gradient));
  }

  private static List<Rectangle> boxes(
      BufferedImage image, Rectangle region, IntPredicate matches) {

    int w = image.getWidth();
    int h = image.getHeight();
    int x0 = 0;
    int y0 = 0;
    int x1 = w;
    int y1 = h;
    if (region != null) {
      x0 = Math.max(0, region.x);
      y0 = Math.max(0, region.y);
      x1 = Math.min(w, region.x + region.width);
      y1 = Math.min(h, region.y + region.height);
    }
    int[] pixels = image.getRGB(0, 0, w, h, null, 0, w);
    boolean[] seen = new boolean[w * h];
    ArrayDeque<Integer> stack = new ArrayDeque<>();
    List<Rectangle> found = new ArrayList<>();
    for (int y = y0; y < y1; y++) {
      for (int x = x0; x < x1; x++) {
        int start = y * w + x;
        if (seen[start] || !matches.test(pixels[start])) {
          continue;
        }
        int minX = x;
        int maxX = x;
        int minY = y;
        int maxY = y;
        seen[start] = true;
        stack.push(start);
        while (!stack.isEmpty()) {
          int p = stack.pop();
          int px = p % w;
          int py = p / w;
          minX = Math.min(minX, px);
          maxX = Math.max(maxX, px);
          minY = Math.min(minY, py);
          maxY = Math.max(maxY, py);
          consider(stack, seen, pixels, w, px - 1, py, x0, y0, x1, y1, matches);
          consider(stack, seen, pixels, w, px + 1, py, x0, y0, x1, y1, matches);
          consider(stack, seen, pixels, w, px, py - 1, x0, y0, x1, y1, matches);
          consider(stack, seen, pixels, w, px, py + 1, x0, y0, x1, y1, matches);
        }
        int bw = maxX - minX + 1;
        int bh = maxY - minY + 1;
        if (bw >= 8 && bh >= 8) {
          found.add(new Rectangle(minX, minY, bw, bh));
        }
      }
    }
    found.sort(Comparator.comparingInt((Rectangle r) -> r.x).thenComparingInt(r -> r.y));
    return found;
  }

  private static void consider(
      ArrayDeque<Integer> stack,
      boolean[] seen,
      int[] pixels,
      int w,
      int x,
      int y,
      int x0,
      int y0,
      int x1,
      int y1,
      IntPredicate matches) {

    if (x < x0 || y < y0 || x >= x1 || y >= y1) {
      return;
    }
    int i = y * w + x;
    if (seen[i] || !matches.test(pixels[i])) {
      return;
    }
    seen[i] = true;
    stack.push(i);
  }

  private static boolean match(int rgb, Color fill, boolean gradient) {

    if (gradient) {
      return inGradient(rgb, fill, 3);
    }
    return maxChannel(rgb, fill) <= 2;
  }

  /** True when {@code rgb} lies within {@code tolerance} of the fill-to-end color segment. */
  private static boolean inGradient(int rgb, Color fill, int tolerance) {

    Color end = BarGradient.endColor(fill);
    double[] p = {(rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF};
    double[] a = {fill.getRed(), fill.getGreen(), fill.getBlue()};
    double[] d = {end.getRed() - a[0], end.getGreen() - a[1], end.getBlue() - a[2]};
    double len2 = d[0] * d[0] + d[1] * d[1] + d[2] * d[2];
    double t = 0.0;
    if (len2 > 0) {
      t = ((p[0] - a[0]) * d[0] + (p[1] - a[1]) * d[1] + (p[2] - a[2]) * d[2]) / len2;
      t = Math.max(0.0, Math.min(1.0, t));
    }
    double dist2 = 0.0;
    for (int i = 0; i < 3; i++) {
      double diff = p[i] - (a[i] + d[i] * t);
      dist2 += diff * diff;
    }
    return dist2 <= (double) tolerance * tolerance * 3;
  }

  private static boolean between(int value, int a, int b, int tolerance) {

    return value >= Math.min(a, b) - tolerance && value <= Math.max(a, b) + tolerance;
  }

  private static boolean betweenColors(int rgb, Color start, Color end, int tolerance) {

    int r = (rgb >> 16) & 0xFF;
    int g = (rgb >> 8) & 0xFF;
    int b = rgb & 0xFF;
    return between(r, start.getRed(), end.getRed(), tolerance)
        && between(g, start.getGreen(), end.getGreen(), tolerance)
        && between(b, start.getBlue(), end.getBlue(), tolerance);
  }

  private static void assertClose(Rectangle flat, Rectangle gradient) {

    assertThat(Math.abs(flat.x - gradient.x)).isLessThanOrEqualTo(1);
    assertThat(Math.abs(flat.y - gradient.y)).isLessThanOrEqualTo(1);
    assertThat(Math.abs(flat.width - gradient.width)).isLessThanOrEqualTo(1);
    assertThat(Math.abs(flat.height - gradient.height)).isLessThanOrEqualTo(1);
  }

  private static int sampleX(Rectangle box) {

    return box.x + Math.max(inset(box.width), box.width / 5);
  }

  private static int inset(int span) {

    return Math.max(2, Math.min(4, (span - 1) / 4));
  }

  private static int brightness(Color color) {

    return color.getRed() + color.getGreen() + color.getBlue();
  }

  private static int maxChannel(Color sample, Color expected) {

    return Math.max(
        Math.abs(sample.getRed() - expected.getRed()),
        Math.max(
            Math.abs(sample.getGreen() - expected.getGreen()),
            Math.abs(sample.getBlue() - expected.getBlue())));
  }

  private static int maxChannel(int rgb, Color expected) {

    int r = (rgb >> 16) & 0xFF;
    int g = (rgb >> 8) & 0xFF;
    int b = rgb & 0xFF;
    return Math.max(
        Math.abs(r - expected.getRed()),
        Math.max(Math.abs(g - expected.getGreen()), Math.abs(b - expected.getBlue())));
  }

  private static Color blend(Color foreground, Color background) {

    float alpha = foreground.getAlpha() / 255f;
    int r = Math.round(foreground.getRed() * alpha + background.getRed() * (1f - alpha));
    int g = Math.round(foreground.getGreen() * alpha + background.getGreen() * (1f - alpha));
    int b = Math.round(foreground.getBlue() * alpha + background.getBlue() * (1f - alpha));
    return new Color(r, g, b);
  }
}
