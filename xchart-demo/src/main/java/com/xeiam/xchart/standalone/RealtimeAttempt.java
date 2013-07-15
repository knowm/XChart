/*
 * Copyright 2013 Xeiam, LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xeiam.xchart.standalone;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.Series;
import com.xeiam.xchart.SeriesMarker;
import com.xeiam.xchart.XChartPanel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author rossjourdain
 */
public class RealtimeAttempt {
    
    private Chart chart;
    private JPanel chartPanel;

    public static void main(String[] args) throws Exception {
        
        // Setup the panel
        final RealtimeAttempt realtimeAttempt = new RealtimeAttempt();
        realtimeAttempt.buildPanel();
                
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                
                // Create and set up the window.
                JFrame frame = new JFrame("XChart");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(realtimeAttempt.getChartPanel());

                // Display the window.
                frame.pack();
                frame.setVisible(true);
            }
        });
        
        // Simulate a data feed
        TimerTask chartUpdaterTask = new TimerTask() {
            @Override
            public void run() {
                realtimeAttempt.updateData();
            }
        };

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(chartUpdaterTask, 0, 1000);

    }
    
    public void buildPanel() {
        Collection<Number> yData = getRandomData(5);
        
        // Create Chart
        chart = new Chart(500, 400);
        chart.setChartTitle("Sample Chart");
        chart.setXAxisTitle("X");
        chart.setYAxisTitle("Y");
        Series series = chart.addSeries("y(x)", null, yData);
        series.setMarker(SeriesMarker.CIRCLE);
        
        chartPanel = new XChartPanel(chart);
    }
    
    public void updateData() {
        // Get some new data
        Collection<Number> newData = getRandomData(1);
        
        
        // Replace the existing
        ArrayList<Number> replacementData = new ArrayList<Number>();
        
        Series oldSeries = (Series) chart.getSeriesMap().values().toArray()[0];
        Collection<Number> oldData = oldSeries.getyData();
        replacementData.addAll(oldData);
        
        replacementData.addAll(newData);
        
        // Limit the total number of points
        while (replacementData.size() > 20) {
            replacementData.remove(0);
        }
        
        // Swap out the data
        chart.getSeriesMap().clear();
        Series newSeries = chart.addSeries(oldSeries.getName(), null, replacementData);
        newSeries.setLineColor(oldSeries.getStrokeColor());
        newSeries.setLineStyle(oldSeries.getStroke());
        newSeries.setMarkerColor(oldSeries.getMarkerColor());
        //etc
        
        // Re-display the chart
        chartPanel.revalidate();
        chartPanel.repaint();
    }

    public Chart getChart() {
        return chart;
    }

    public JPanel getChartPanel() {
        return chartPanel;
    }
    
    private static Collection<Number> getRandomData(int numPoints) {
        ArrayList<Number> data = new ArrayList<Number>();
        for (int i = 0; i < numPoints; i++) {
            data.add(Math.random() * 100);
        }
        return data;
    }
}
