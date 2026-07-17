package org.knowm.xchart.standalone.issues;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.Styler;

/**
 * Reproducer for https://github.com/knowm/XChart/issues/577
 *
 * <p>"When displaying horizontal legends, and too many series, the legends are extending beyond the
 * chart, and beyond the image, and are truncated. This happens often when the series names are
 * long." (reported with a horizontal, OutsideS legend)
 *
 * <p>Expected: legends should go to the next line when the current line is too long.
 *
 * <p>Before the fix, all legend entries were laid out on a single row, so the centered OutsideS
 * legend spilled past both image edges and got truncated. Entries now wrap onto additional rows,
 * keeping the whole legend within the image width.
 */
public class TestForIssue577 {

  public static void main(String[] args) {

    new SwingWrapper<>(getChart()).displayChart();
  }

  /** Constructs and returns the chart without launching a window (headless-safe). */
  public static XYChart getChart() {

    // Create chart
    XYChart chart =
        new XYChartBuilder()
            .width(900)
            .height(500)
            .title("Horizontal Legend Wrapping Demo")
            .xAxisTitle("X")
            .yAxisTitle("Y")
            .build();

    // General Styler settings: a horizontal legend below the plot
    chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideS);
    chart.getStyler().setLegendLayout(Styler.LegendLayout.Horizontal);
    chart.getStyler().setLegendVisible(true);

    // Many series with long names so a single legend row would overflow the image width
    String[] seriesNames = {
      "Temperature Sensor North",
      "Temperature Sensor South",
      "Humidity Sensor East Wing",
      "Pressure Gauge Basement",
      "Wind Speed Rooftop Array",
      "Solar Irradiance Panel 12",
      "CO2 Concentration Lobby",
      "Particulate Matter PM2.5"
    };
    for (int i = 0; i < seriesNames.length; i++) {
      chart.addSeries(
          seriesNames[i], new double[] {0, 1, 2}, new double[] {i, i + 1, i});
    }

    return chart;
  }
}
