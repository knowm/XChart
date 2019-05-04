package org.knowm.xchart.standalone.issues;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JTabbedPane;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.demo.ExampleChartTester;
import org.knowm.xchart.internal.chartpart.Chart;

public class TestForCoordinateLookup {

  static final int WIDTH = 465;
  static final int HEIGHT = 320;

  public static class CoordinatePrinter extends MouseAdapter {

    private Chart chart;

    public CoordinatePrinter(Chart chart) {

      this.chart = chart;
    }

    public void mousePressed(MouseEvent e) {

      double chartX = chart.getChartXFromCoordinate(e.getX());
      double chartY = chart.getChartYFromCoordinate(e.getY());

      double screenX = chart.getScreenXFromChart(chartX);
      double screenY = chart.getScreenYFromChart(chartY);
      System.out.println(
          String.format(
              "Mouse click: (%d, %d) Chart value: (%.3f, %.3f) Mouse point from chart value: (%.1f, %.1f)",
              e.getX(), e.getY(), chartX, chartY, screenX, screenY));
    }
  }

  public static void main(String[] args) {

    ExampleChartTester tester =
        new ExampleChartTester() {

          @Override
          protected void addCharts(JTabbedPane tabbedPane, Map<String, Chart> chartMap) {

            for (Entry<String, Chart> e : chartMap.entrySet()) {

              Chart chart = e.getValue();
              if (chart == null) {
                continue;
              }
              chart.getStyler().setToolTipsEnabled(true);
              XChartPanel chartPanel = new XChartPanel(chart);
              CoordinatePrinter mouseListener = new CoordinatePrinter(chart);
              chartPanel.addMouseListener(mouseListener);
              tabbedPane.addTab(e.getKey(), chartPanel);
            }
          }
        };

    tester.createAndShowGUI();
  }
}
