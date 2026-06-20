package org.knowm.xchart.standalone.issues;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.knowm.xchart.ChartButtonConfig;
import org.knowm.xchart.ChartButtonConfig.ChartButtonPosition;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.colors.ChartColor;

/**
 * Demonstrates issue #975 — chart button styling moved out of {@code Styler}/{@code Theme} into
 * {@link ChartButtonConfig} on {@link XChartPanel}.
 *
 * <p>Previously, {@code Styler} carried six {@code chartButton*} fields that were exclusively used
 * by the Swing layer ({@code ChartButton}, {@code ChartZoom}), polluting every headless render
 * path. These have been removed from {@code Styler} and {@code Theme} and replaced by the new
 * {@link ChartButtonConfig} class, which lives entirely within the Swing rendering layer.
 *
 * <p>This demo shows the zoom-reset button using a custom {@code ChartButtonConfig}: red
 * background, white text, and positioned in the bottom-right corner. Drag a selection on the chart
 * to trigger the zoom and reveal the styled reset button.
 */
public class TestForIssue975 {

  public static void main(String[] args) {

    SwingUtilities.invokeLater(
        () -> {
          XYChart chart = getChart();

          XChartPanel<XYChart> panel = new XChartPanel<>(chart);

          // Customise button styling via ChartButtonConfig — no longer touches the Styler.
          panel.setChartButtonConfig(
              new ChartButtonConfig()
                  .setBackgroundColor(ChartColor.RED.getColor())
                  .setBorderColor(ChartColor.DARK_GREY.getColor())
                  .setFontColor(java.awt.Color.WHITE)
                  .setMargin(8)
                  .setPosition(ChartButtonPosition.InsideSE));

          panel.setZoomEnabled(true);
          panel.setZoomResetByButton(true);

          JFrame frame = new JFrame("Issue #975 – ChartButtonConfig demo");
          frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          frame.getContentPane().add(panel);
          frame.pack();
          frame.setVisible(true);
        });
  }

  /** Constructs and returns the chart without launching a window (headless-safe). */
  public static XYChart getChart() {

    XYChart chart =
        new XYChartBuilder()
            .width(700)
            .height(400)
            .title("Issue #975 – ChartButtonConfig demo")
            .xAxisTitle("X")
            .yAxisTitle("Y")
            .build();

    chart.addSeries("series1", new double[]{1, 2, 3, 4, 5}, new double[]{2, 4, 3, 7, 5});
    chart.addSeries("series2", new double[]{1, 2, 3, 4, 5}, new double[]{5, 3, 6, 2, 8});
    return chart;
  }
}
