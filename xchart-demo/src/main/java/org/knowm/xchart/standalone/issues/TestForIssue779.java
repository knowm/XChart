package org.knowm.xchart.standalone.issues;

import java.awt.Color;
import java.util.Arrays;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.SwingWrapper;

/**
 * Demonstrates bar label visibility with custom fill colors in various positions.
 *
 * <ul>
 *   <li>Chart 1: labels at default position (inside bar, mid) with custom fill color — should be
 *       visible
 *   <li>Chart 2: labels at default position without custom fill color — should be visible
 *   <li>Chart 3: stack-sum labels (above bar tip) with custom fill color — may show white-on-white
 *       bug
 * </ul>
 *
 * @see <a href="https://github.com/knowm/XChart/issues/779">Issue #779</a>
 */
public class TestForIssue779 {

  public static void main(String[] args) {
    // Chart 1: with setFillColor - labels inside bar (default position)
    CategoryChart chartWithColor =
        new CategoryChartBuilder()
            .width(500)
            .height(400)
            .title("With setFillColor, labels inside (should be visible)")
            .xAxisTitle("X")
            .yAxisTitle("Y")
            .build();
    chartWithColor.addSeries("test 1", Arrays.asList(0, 1, 2, 3), Arrays.asList(4, 5, 4, 3));
    chartWithColor.getSeries("test 1").setFillColor(new Color(10, 150, 235));
    chartWithColor.getStyler().setLabelsVisible(true);

    // Chart 2: without setFillColor - labels inside bar
    CategoryChart chartNoColor =
        new CategoryChartBuilder()
            .width(500)
            .height(400)
            .title("Without setFillColor, labels inside (should be visible)")
            .xAxisTitle("X")
            .yAxisTitle("Y")
            .build();
    chartNoColor.addSeries("test 1", Arrays.asList(0, 1, 2, 3), Arrays.asList(4, 5, 4, 3));
    chartNoColor.getStyler().setLabelsVisible(true);

    // Chart 3: stack-sum labels render above the bar tip — with custom fill color the
    // auto-contrast logic may pick white text against the white chart background (bug)
    CategoryChart chartStackSum =
        new CategoryChartBuilder()
            .width(500)
            .height(400)
            .title("With setFillColor, stack-sum labels above bar (check visibility)")
            .xAxisTitle("X")
            .yAxisTitle("Y")
            .build();
    chartStackSum.addSeries("test 1", Arrays.asList(0, 1, 2, 3), Arrays.asList(4, 5, 4, 3));
    chartStackSum.getSeries("test 1").setFillColor(new Color(10, 150, 235));
    chartStackSum.getStyler().setLabelsVisible(true);
    chartStackSum.getStyler().setStacked(true);
    chartStackSum.getStyler().setShowStackSum(true);

    new SwingWrapper<>(Arrays.asList(chartWithColor, chartNoColor, chartStackSum))
        .displayChartMatrix();
  }
}
