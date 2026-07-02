package org.knowm.xchart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.io.ByteArrayOutputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;

public class XYChartTest {
  private static final String digestType = "md5";

  // https://github.com/knowm/XChart/issues/799 — all-duplicate labels must not loop forever
  @Test
  public void yAxisFormatterAllDuplicateLabelsDoesNotHang() throws Exception {
    double[] xData = new double[] {0.0, 1.0, 2.0};
    double[] yData = new double[] {2.0, 1.0, 0.0};
    XYChart chart = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", xData, yData);
    chart.getStyler().setYAxisTickLabelsFormattingFunction(yValue -> "1");

    DigestOutputStream output =
        new DigestOutputStream(new ByteArrayOutputStream(), MessageDigest.getInstance(digestType));
    assertThatCode(() -> BitmapEncoder.saveBitmap(chart, output, BitmapEncoder.BitmapFormat.PNG))
        .doesNotThrowAnyException();
    output.close();
  }

  // https://github.com/knowm/XChart/issues/799 — empty-string labels must not crash TextLayout
  @Test
  public void yAxisFormatterEmptyStringLabelsDoesNotCrash() throws Exception {
    double[] xData = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0};
    double[] yData = {10.0, 20.0, 30.0, 40.0, 50.0, 60.0, 70.0, 80.0, 90.0, 100.0};
    XYChart chart = new XYChartBuilder().width(800).height(600).build();
    chart.addSeries("test", xData, yData);

    Map<Double, Double> yLabelMap = new HashMap<>();
    yLabelMap.put(10.0, 0.01);
    yLabelMap.put(50.0, 0.05);
    chart.setCustomYAxisTickLabelsFormatter(
        yValue -> {
          Double yLabel = yLabelMap.get(yValue);
          return yLabel == null ? "" : yLabel.toString();
        });

    assertThatCode(() -> BitmapEncoder.getBitmapBytes(chart, BitmapEncoder.BitmapFormat.PNG))
        .doesNotThrowAnyException();
  }

  // X-axis variant: empty-string labels from a custom X-axis formatter must not crash TextLayout
  @Test
  public void xAxisFormatterEmptyStringLabelsDoesNotCrash() throws Exception {
    double[] xData = {1.0, 2.0, 3.0, 4.0, 5.0};
    double[] yData = {10.0, 20.0, 30.0, 40.0, 50.0};
    XYChart chart = new XYChartBuilder().width(800).height(600).build();
    chart.addSeries("test", xData, yData);

    Map<Double, String> xLabelMap = new HashMap<>();
    xLabelMap.put(1.0, "A");
    xLabelMap.put(3.0, "C");
    chart.setCustomXAxisTickLabelsFormatter(
        xValue -> xLabelMap.getOrDefault(xValue, ""));

    assertThatCode(() -> BitmapEncoder.getBitmapBytes(chart, BitmapEncoder.BitmapFormat.PNG))
        .doesNotThrowAnyException();
  }

  // https://github.com/knowm/XChart/issues/712 — painting a tiny chart (degenerate bounds) must not NPE
  @Test
  public void paintingTinyChartDoesNotThrow() throws Exception {

    // A 1×1 pixel chart has a zero/negative plot area after axis columns are subtracted; the
    // guard in Plot_AxesChart.paint() must bail out without throwing.
    XYChart chart = new XYChartBuilder().width(1).height(1).build();
    chart.addSeries("s", new double[] {1, 2, 3}, new double[] {1, 2, 3});

    assertThatCode(() -> BitmapEncoder.getBitmapBytes(chart, BitmapEncoder.BitmapFormat.PNG))
        .doesNotThrowAnyException();
  }

  // Variant: multiple Y-axis groups (more axis columns → plot area goes negative sooner)
  @Test
  public void paintingTinyChartWithMultipleYAxisGroupsDoesNotThrow() throws Exception {

    XYChart chart = new XYChartBuilder().width(1).height(1).build();
    chart.addSeries("a", new double[] {1, 2, 3}, new double[] {10, 20, 30});
    XYSeries b = chart.addSeries("b", new double[] {1, 2, 3}, new double[] {100, 200, 300});
    b.setYAxisGroup(1);
    XYSeries c = chart.addSeries("c", new double[] {1, 2, 3}, new double[] {1000, 2000, 3000});
    c.setYAxisGroup(2);

    assertThatCode(() -> BitmapEncoder.getBitmapBytes(chart, BitmapEncoder.BitmapFormat.PNG))
        .doesNotThrowAnyException();
  }

  // https://github.com/knowm/XChart/issues/834 — custom formatter must not break logarithmic axis
  @Test
  public void customYAxisFormatterPreservesLogarithmicScale() throws Exception {
    double[] xData = new double[] {1, 2, 3, 4, 5, 6, 7, 8, 9};
    double[] yData = new double[] {1, 10, 100, 1e3, 1e4, 1e5, 1e6, 1e7, 1e8};
    XYChart chart = new XYChartBuilder().width(800).height(600).build();
    chart.addSeries("test", xData, yData);
    chart.getStyler().setYAxisLogarithmic(true);

    List<Double> seenValues = new ArrayList<>();
    chart.setCustomYAxisTickLabelsFormatter(
        value -> {
          seenValues.add(value);
          return String.valueOf(value);
        });

    BitmapEncoder.getBitmapBytes(chart, BitmapEncoder.BitmapFormat.PNG);

    // Every value the formatter receives must be a power of ten (log10 is a whole number).
    // A linear fallback would produce evenly-spaced values like 0, 1e7, 2e7 ... which fail this.
    assertThat(seenValues).isNotEmpty();
    for (double v : seenValues) {
      double log = Math.log10(v);
      assertThat(Math.abs(log - Math.round(log)))
          .as("Expected power-of-ten tick value but got %s", v)
          .isLessThan(1e-9);
    }
  }

  // https://github.com/knowm/XChart/issues/834 — same for logarithmic X-axis
  @Test
  public void customXAxisFormatterPreservesLogarithmicScale() throws Exception {
    double[] xData = new double[] {1, 10, 100, 1e3, 1e4, 1e5};
    double[] yData = new double[] {1, 2, 3, 4, 5, 6};
    XYChart chart = new XYChartBuilder().width(800).height(600).build();
    chart.addSeries("test", xData, yData);
    chart.getStyler().setXAxisLogarithmic(true);

    List<Double> seenValues = new ArrayList<>();
    chart.setCustomXAxisTickLabelsFormatter(
        value -> {
          seenValues.add(value);
          return String.valueOf(value);
        });

    BitmapEncoder.getBitmapBytes(chart, BitmapEncoder.BitmapFormat.PNG);

    assertThat(seenValues).isNotEmpty();
    for (double v : seenValues) {
      double log = Math.log10(v);
      assertThat(Math.abs(log - Math.round(log)))
          .as("Expected power-of-ten tick value but got %s", v)
          .isLessThan(1e-9);
    }
  }

  @Test
  public void addSeriesUsesConfiguredDefaultSeriesRenderStyleImmediately() {
    XYChart chart = new XYChartBuilder().width(800).height(600).build();
    chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Area);

    XYSeries series = chart.addSeries("test", new double[] {1, 2, 3}, new double[] {1, 2, 3});

    assertThat(series.getXYSeriesRenderStyle()).contains(XYSeriesRenderStyle.Area);
  }

  @Test
  public void addSeriesUsesDefaultLineRenderStyleImmediately() {
    XYChart chart = new XYChartBuilder().width(800).height(600).build();

    XYSeries series = chart.addSeries("test", new double[] {1, 2, 3}, new double[] {1, 2, 3});

    assertThat(series.getXYSeriesRenderStyle()).contains(XYSeriesRenderStyle.Line);
  }

  @Test
  public void explicitSeriesRenderStyleSurvivesPaint() throws Exception {
    XYChart chart = new XYChartBuilder().width(800).height(600).build();
    chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Area);
    XYSeries series = chart.addSeries("test", new double[] {1, 2, 3}, new double[] {1, 2, 3});
    series.setXYSeriesRenderStyle(XYSeriesRenderStyle.Scatter);

    BitmapEncoder.getBitmapBytes(chart, BitmapEncoder.BitmapFormat.PNG);

    assertThat(series.getXYSeriesRenderStyle()).contains(XYSeriesRenderStyle.Scatter);
  }
}
