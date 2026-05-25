package org.knowm.xchart.standalone.issues;

import java.awt.Color;
import java.util.Arrays;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.SwingWrapper;

/**
 * Demonstrates that bar tip labels remain visible when a custom fill color is set via
 * setFillColor().
 *
 * <p>Both charts should show visible labels above each bar.
 *
 * @see <a href="https://github.com/knowm/XChart/issues/779">Issue #779</a>
 */
public class TestForIssue779 {

  public static void main(String[] args) {
    // Chart 1: with setFillColor - labels should be visible
    CategoryChart chartWithColor =
        new CategoryChartBuilder()
            .width(500)
            .height(400)
            .title("With setFillColor (labels visible)")
            .xAxisTitle("X")
            .yAxisTitle("Y")
            .build();
    chartWithColor.addSeries("test 1", Arrays.asList(0, 1, 2, 3), Arrays.asList(4, 5, 4, 3));
    chartWithColor.getSeriesMap().get("test 1").setFillColor(new Color(10, 150, 235));
    chartWithColor.getStyler().setLabelsVisible(true);

    // Chart 2: without setFillColor - labels are visible correctly
    CategoryChart chartNoColor =
        new CategoryChartBuilder()
            .width(500)
            .height(400)
            .title("Without setFillColor (labels visible)")
            .xAxisTitle("X")
            .yAxisTitle("Y")
            .build();
    chartNoColor.addSeries("test 1", Arrays.asList(0, 1, 2, 3), Arrays.asList(4, 5, 4, 3));
    chartNoColor.getStyler().setLabelsVisible(true);

    new SwingWrapper<>(Arrays.asList(chartWithColor, chartNoColor)).displayChartMatrix();
  }
}
