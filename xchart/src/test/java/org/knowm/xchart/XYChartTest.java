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
    chart.getStyler().setyAxisTickLabelsFormattingFunction(yValue -> "1");

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
}
