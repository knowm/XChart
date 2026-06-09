package org.knowm.xchart.standalone.issues;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.Styler;

/**
 * Demonstrates memory-efficient primitive-array usage for XYChart (issue #879).
 *
 * <p>The root cause of the perceived high memory usage in issue #879 was two-fold:
 *
 * <ol>
 *   <li>JDK 8's Parallel GC is less aggressive at reclaiming heap than JDK 21's G1GC. Adding
 *       {@code -XX:+UseG1GC} on JDK 8 restores comparable behaviour. This is not an XChart bug.
 *   <li>Passing {@code double[]} / {@code int[]} to {@link
 *       org.knowm.xchart.CategoryChart#addSeries} previously boxed every value into a {@code
 *       List<Double>}, consuming ~3× more memory per element (24 bytes vs 8 bytes for a primitive
 *       double). This has been fixed: data is now stored internally as {@code double[]}.
 * </ol>
 *
 * <p>Click "Add New Data" repeatedly while monitoring heap usage with VisualVM to verify behaviour.
 */
public class TestForIssue879 {

  public static final int NUM_DATA_POINTS = 18_000;

  public static void main(String[] args) {

    double[] xData = new double[NUM_DATA_POINTS];
    double[] yData = new double[NUM_DATA_POINTS];

    long timestamp = System.currentTimeMillis();
    for (int i = 0; i < NUM_DATA_POINTS; i++) {
      xData[i] = timestamp + i;
      yData[i] = Math.random() * 100;
    }

    XYChart chart = getChart(xData, yData);
    XChartPanel<XYChart> chartPanel = new XChartPanel<>(chart);

    JButton button = new JButton("Add New Data");
    button.addActionListener(
        e -> {
          Color randomColor =
              new Color(
                  (int) (Math.random() * 255),
                  (int) (Math.random() * 255),
                  (int) (Math.random() * 255));
          chart.getSeriesMap().get("mockData").setMarkerColor(randomColor);
          chart.getSeriesMap().get("mockData").setLineColor(randomColor);
          chart.getSeriesMap().get("mockData").setFillColor(randomColor);
          SwingUtilities.invokeLater(
              () -> {
                chart.updateXYSeries("mockData", xData, yData, null);
                chartPanel.repaint();
              });
        });

    JFrame frame = new JFrame("Issue #879 — XYChart memory test");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(new BorderLayout());
    frame.add(button, BorderLayout.SOUTH);
    frame.add(chartPanel, BorderLayout.CENTER);
    frame.pack();
    frame.setVisible(true);
  }

  /** Constructs and returns the chart without launching a window (headless-safe). */
  public static XYChart getChart(double[] xData, double[] yData) {

    XYChart chart =
        new XYChartBuilder()
            .width(800)
            .height(600)
            .title("Issue #879 — XYChart with 18 000 points (double[] path, no boxing)")
            .xAxisTitle("Time (ms)")
            .yAxisTitle("Value")
            .theme(Styler.ChartTheme.GGPlot2)
            .build();

    chart.addSeries("mockData", xData, yData);

    return chart;
  }
}
