package org.knowm.xchart;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.knowm.xchart.demo.DemoChartsUtil;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.demo.charts.date.DateChart01;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.chartpart.Cursor;
import org.knowm.xchart.internal.chartpart.ToolTips;
import org.knowm.xchart.style.XYStyler;

@RunWith(Parameterized.class)
public class DemoChartsTest {

  private final ExampleChart chart;

  private static final Collection<Class<?>> SKIPPED_EXAMPLE_CHARTS =
      Arrays.asList(
          // because it uses ChartZoom which is hard to institate in tests, esp. in headless
          // environment
          DateChart01.class);

  @Parameterized.Parameters(name = "{1}")
  public static Collection<Object[]> chartDemos() {
    return DemoChartsUtil.getAllDemoCharts().stream()
        .filter(chart -> !SKIPPED_EXAMPLE_CHARTS.contains(chart.getClass()))
        .map(chart -> new Object[] {chart, chart.getExampleChartName()})
        .collect(Collectors.toList());
  }

  public DemoChartsTest(ExampleChart chart, String chartName) {
    this.chart = chart;
  }

  @Test
  public void shouldNotFailWhenRenderingAsBitmap() throws IOException {
    // given
    Chart chart = this.chart.getChart();
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
