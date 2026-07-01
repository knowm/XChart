package org.knowm.xchart.standalone.issues;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.Styler.LegendPosition;

/**
 * Reproducer / how-to for https://github.com/knowm/XChart/issues/761
 *
 * <p>"Update addSeries() to take in modern date and time classes."
 *
 * <p>addSeries()'s X-axis parameter is a {@code List<?>}, and XChart now accepts the {@code
 * java.time} types (Instant, ZonedDateTime, OffsetDateTime, LocalDateTime, LocalDate, LocalTime) in
 * addition to the legacy {@link java.util.Date}. No conversion by the caller is required. Zone-less
 * types (LocalDateTime/LocalDate/LocalTime) are resolved using the styler timezone so the axis
 * labels line up with the data.
 */
public class TestForIssue761 {

  public static void main(String[] args) {

    new SwingWrapper<>(getChart()).displayChart();
  }

  /** Constructs and returns the chart without launching a window (headless-safe). */
  public static XYChart getChart() {

    XYChart chart =
        new XYChartBuilder()
            .width(800)
            .height(600)
            .title("java.time on the X-axis (issue 761)")
            .xAxisTitle("Time")
            .yAxisTitle("Value")
            .build();

    chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
    chart.getStyler().setDatePattern("HH:mm");
    // Resolve the zone-less LocalDateTime values in UTC so the demo is deterministic everywhere.
    chart.getStyler().setTimezone(java.util.TimeZone.getTimeZone(ZoneId.of("UTC")));

    // X data as java.time.LocalDateTime — no java.util.Date needed
    List<LocalDateTime> xData = new ArrayList<>();
    List<Double> yData = new ArrayList<>();
    LocalDateTime start = LocalDateTime.of(2021, 1, 1, 9, 0);
    double[] values = {3.0, 5.0, 4.0, 7.0, 6.5, 9.0};
    for (int i = 0; i < values.length; i++) {
      xData.add(start.plusMinutes(30L * i));
      yData.add(values[i]);
    }

    chart.addSeries("readings", xData, yData);

    return chart;
  }
}
