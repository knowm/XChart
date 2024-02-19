package org.knowm.xchart;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;
import org.knowm.xchart.internal.chartpart.Axis;
import org.knowm.xchart.internal.chartpart.AxisPair;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.style.theme.XChartTheme;

public class HeatMapChartTest {

  java.util.List<Integer> xData;
  java.util.List<String> yData;
  java.util.List<Number[]> data;

  {
    int N = 12;
    xData = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
    yData =
        Arrays.asList(
            "January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December");
    data = new ArrayList<>();
    for (int i = 0; i < N; i++) {
      for (int j = 0; j < N; j++) {
        data.add(new Integer[] {i, j, (i * 2 + j * 3) % 7});
      }
    }
  }

  @Test
  public void shouldApplyMaxLabelCountOnlyToXAxis() throws Exception {
    // given
    HeatMapChart chart = new HeatMapChart(800, 600);
    chart.addSeries("only", xData, yData, data);
    chart.getStyler().setTheme(new XChartTheme());

    // when
    chart.getStyler().setXAxisMaxLabelCount(5);
    BitmapEncoder.saveBitmap(chart, new ByteArrayOutputStream(), BitmapEncoder.BitmapFormat.PNG);

    // test
    Assert.assertEquals(5, getXAxis(chart).getAxisTickCalculator().getTickLocations().size());
    Assert.assertEquals(12, getYAxis(chart).getAxisTickCalculator().getTickLocations().size());
  }

  private AxisPair<?, ?> getAxisPair(Chart<?, ?> chart) throws ReflectiveOperationException {
    Field f = Chart.class.getDeclaredField("axisPair");
    f.setAccessible(true);
    return (AxisPair<?, ?>) f.get(chart);
  }

  public Axis<?, ?> getXAxis(Chart<?, ?> chart) throws ReflectiveOperationException {
    return getAxisPair(chart).getXAxis();
  }

  public Axis<?, ?> getYAxis(Chart<?, ?> chart) throws ReflectiveOperationException {
    Field f = AxisPair.class.getDeclaredField("yAxis");
    f.setAccessible(true);
    return (Axis<?, ?>) f.get(getAxisPair(chart));
  }
}
