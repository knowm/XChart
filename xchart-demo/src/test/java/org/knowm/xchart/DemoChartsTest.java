package org.knowm.xchart;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.knowm.xchart.demo.DemoChartsUtil;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.demo.charts.date.DateChart01;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.chartpart.Cursor;
import org.knowm.xchart.internal.chartpart.ToolTips;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.XYStyler;

public class DemoChartsTest {

  private static final Collection<Class<?>> SKIPPED_EXAMPLE_CHARTS =
      Arrays.asList(
          // because it uses ChartZoom which is hard to institate in tests, esp. in headless
          // environment
          DateChart01.class);

  public static Stream<ExampleChart<Chart<Styler, Series>>> chartDemos() {
    return DemoChartsUtil.getAllDemoCharts().stream()
        .filter(chart -> !SKIPPED_EXAMPLE_CHARTS.contains(chart.getClass()));
  }

  @ParameterizedTest
  @MethodSource("chartDemos")
  public void shouldNotFailWhenRenderingAsBitmap(ExampleChart<Chart<Styler, Series>> exampleChart)
      throws IOException {
    // given
    Chart chart = exampleChart.getChart();
    configureInteractiveFeatures(chart);

    // when
    BitmapEncoder.saveBitmap(chart, new ByteArrayOutputStream(), BitmapEncoder.BitmapFormat.PNG);

    // test

    // Don't fail
  }

  private void configureInteractiveFeatures(Chart chart) {
    new ToolTips(chart);
    if (chart instanceof XYChart && chart.getStyler() instanceof XYStyler) {
      new Cursor(chart);
    }
  }
}
