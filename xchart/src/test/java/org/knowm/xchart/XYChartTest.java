package org.knowm.xchart;

import static org.assertj.core.api.Assertions.assertThatCode;

import java.io.ByteArrayOutputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

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

  // https://github.com/knowm/XChart/issues/834 — custom formatter must not break logarithmic axis
  @Test
  public void customYAxisFormatterPreservesLogarithmicScale() throws Exception {
    double[] xData = new double[] {1, 2, 3, 4, 5, 6, 7, 8, 9};
    double[] yData = new double[] {1, 10, 100, 1e3, 1e4, 1e5, 1e6, 1e7, 1e8};
    XYChart chart = new XYChartBuilder().width(800).height(600).build();
    chart.addSeries("test", xData, yData);
    chart.getStyler().setYAxisLogarithmic(true);
    chart.setCustomYAxisTickLabelsFormatter(
        value -> {
          if (value < 1e3) return String.format("%.0f nJ", value);
          else if (value < 1e6) return String.format("%.2f µJ", value / 1e3);
          else if (value < 1e9) return String.format("%.2f mJ", value / 1e6);
          else return String.format("%.2f J", value / 1e9);
        });

    assertThatCode(() -> BitmapEncoder.getBitmapBytes(chart, BitmapEncoder.BitmapFormat.PNG))
        .doesNotThrowAnyException();
  }

  // https://github.com/knowm/XChart/issues/834 — same for logarithmic X-axis
  @Test
  public void customXAxisFormatterPreservesLogarithmicScale() throws Exception {
    double[] xData = new double[] {1, 10, 100, 1e3, 1e4, 1e5};
    double[] yData = new double[] {1, 2, 3, 4, 5, 6};
    XYChart chart = new XYChartBuilder().width(800).height(600).build();
    chart.addSeries("test", xData, yData);
    chart.getStyler().setXAxisLogarithmic(true);
    chart.setCustomXAxisTickLabelsFormatter(value -> String.format("10^%.0f", Math.log10(value)));

    assertThatCode(() -> BitmapEncoder.getBitmapBytes(chart, BitmapEncoder.BitmapFormat.PNG))
        .doesNotThrowAnyException();
  }
}
