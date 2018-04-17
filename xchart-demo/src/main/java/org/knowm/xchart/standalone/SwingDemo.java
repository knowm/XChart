package org.knowm.xchart.standalone;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.demo.charts.area.AreaChart01;

/**
 * Class showing how to integrate a chart into a Swing JFrame
 *
 * @author timmolter
 */
public class SwingDemo {

  /**
   * Create the GUI and show it. For thread safety, this method should be invoked from the event
   * dispatch thread.
   */
  private static void createAndShowGUI() {

    // Create and set up the window.
    JFrame frame = new JFrame("XChart Swing Demo");
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    // Add content to the window.
    JPanel chartPanel = new XChartPanel(new AreaChart01().getChart());
    frame.add(chartPanel);

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
