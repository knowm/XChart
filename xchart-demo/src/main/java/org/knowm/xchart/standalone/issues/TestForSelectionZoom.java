package org.knowm.xchart.standalone.issues;

import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JTabbedPane;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.demo.ExampleChartTester;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.chartpart.SelectionZoom;

public class TestForSelectionZoom {

  static final int WIDTH = 465;
  static final int HEIGHT = 320;

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
              if (chart instanceof XYChart) {
                SelectionZoom sz = new SelectionZoom();
                sz.init(chartPanel);
              }
              tabbedPane.addTab(e.getKey(), chartPanel);
            }
          }

          @Override
          protected boolean skipExampleChart(ExampleChart exampleChart) {

            if (exampleChart.getClass().getSimpleName().startsWith("RealtimeChart")) {
              return true;
            }
            Chart chart = exampleChart.getChart();
            if (chart instanceof XYChart) {
              return false;
            }
            return true;
          }
        };

    tester.createAndShowGUI();
  }
}
