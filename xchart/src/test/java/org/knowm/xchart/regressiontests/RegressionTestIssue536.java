package org.knowm.xchart.regressiontests;

import static java.lang.Double.NaN;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.markers.SeriesMarkers;

/** Regression test for <a href="https://github.com/knowm/XChart/issues/536">issue 536</a>. */
public class RegressionTestIssue536 {

  @Test
  public void issue546RegressionTest() throws Exception {

    XYChart chart = new XYChart(800, 600);

    List<? extends Number> times = Arrays.asList(1L, 2L, 3L);
    List<? extends Number> values =
        times.stream().mapToDouble(x -> NaN).boxed().collect(Collectors.toList());
    XYSeries series = chart.addSeries("Series", times, values);
    series.setMarker(SeriesMarkers.NONE);

    byte[] bytes = BitmapEncoder.getBitmapBytes(chart, BitmapEncoder.BitmapFormat.PNG);
  }
}
