package com.xeiam.swing;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.xeiam.xcharts.Chart;
import com.xeiam.xcharts.JChartPanel;

public class SwingHelper {

    Chart[] charts;

    public SwingHelper(Chart chart) {
        this.charts = new Chart[1];
        charts[0] = chart;
    }

    public SwingHelper(Chart[] charts) {
        this.charts = charts;
    }

    public void displayChart() {

        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Create and set up the window.
                JFrame frame = new JFrame("XChart");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

                for (int i = 0; i < charts.length; i++) {
                    JPanel chartPanel = new JChartPanel(charts[i]);
                    frame.getContentPane().add(chartPanel);
                }

                // frame.setContentPane(newContentPane);

                // Display the window.
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}
