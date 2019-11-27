package org.knowm.xchart.standalone.issues;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.internal.chartpart.SelectionZoom;
import org.knowm.xchart.style.Styler.LegendPosition;

public class TestForIssue349 implements ExampleChart<XYChart> {

  public static void main(String[] args) {

    ExampleChart<XYChart> exampleChart = new TestForIssue349();
    XYChart chart = exampleChart.getChart();
    XChartPanel chartPanel = new XChartPanel(chart);
    SelectionZoom sz = new SelectionZoom();
    sz.getResetButton().setPosition(LegendPosition.InsideNW);
    sz.init(chartPanel);

    // Create and set up the window.
    final JFrame frame = new JFrame("TestForIssue349");

    // Schedule a job for the event-dispatching thread:
    // creating and showing this application's GUI.
    try {
      javax.swing.SwingUtilities.invokeAndWait(
          new Runnable() {

            @Override
            public void run() {

              frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
              frame.add(chartPanel);

              // Display the window.
              frame.pack();
              frame.setVisible(true);
            }
          });
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
  }

  @Override
  public XYChart getChart() {

    // Create Chart
    XYChart chart = new XYChartBuilder().width(800).height(600).build();

    // Customize Chart
    chart.getStyler().setChartTitleVisible(false);
    chart.getStyler().setLegendVisible(false);

    List<Integer> xData = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    List<Double> yData = Arrays.asList(1.1, 2.2, 7.3, 8.4, 4.5, 6.6, 2.7, 6.8, 4.9, 3.10);

    List<Double> xData2 = Arrays.asList(4.5, 6.5, 7.5);
    List<Double> yData2 = Arrays.asList(3.4, 5.6, 3.7);

    // Series
    chart.addSeries("1", xData, yData);
    chart.addSeries("2", xData2, yData2);

    return chart;
  }
}
