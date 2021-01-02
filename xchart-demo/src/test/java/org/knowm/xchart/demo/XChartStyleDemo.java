package org.knowm.xchart.demo;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.WindowConstants;
import javax.swing.event.TreeSelectionEvent;
import org.knowm.xchart.XChartPanel;

public class XChartStyleDemo extends XChartDemo {

  private JSplitPane styleSplitPane;
  private ChartStylePanel stylePanel;

  public XChartStyleDemo() {
    styleSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    styleSplitPane.setLeftComponent(this);

    stylePanel = new ChartStylePanel(chartPanel);
    styleSplitPane.setRightComponent(stylePanel);
  }

  @Override
  public void valueChanged(TreeSelectionEvent e) {
    XChartPanel oldChartPanel = chartPanel;
    super.valueChanged(e);
    if (chartPanel != oldChartPanel) {
      stylePanel.changeChart(chartPanel);
    }
  }

  /**
   * Create the GUI and show it. For thread safety, this method should be invoked from the event
   * dispatch thread.
   */
  private static void createAndShowGUI() {

    // Create and set up the window.
    JFrame frame = new JFrame("XChart Style Demo");
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    XChartStyleDemo demo = new XChartStyleDemo();

    // Add content to the window.
    frame.add(demo.styleSplitPane);

    // Display the window.
    frame.pack();
    frame.setVisible(true);
  }

  public static void main(String[] args) {

    // Schedule a job for the event dispatch thread:
    // creating and showing this application's GUI.
    javax.swing.SwingUtilities.invokeLater(
        new Runnable() {

          @Override
          public void run() {

            createAndShowGUI();
          }
        });
  }
}
