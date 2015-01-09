/**
 * Copyright 2011 - 2014 Xeiam LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xeiam.xchart.demo;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import com.xeiam.xchart.XChartPanel;
import com.xeiam.xchart.demo.charts.area.AreaChart01;
import com.xeiam.xchart.demo.charts.area.AreaChart02;
import com.xeiam.xchart.demo.charts.area.AreaLineChart03;
import com.xeiam.xchart.demo.charts.bar.BarChart01;
import com.xeiam.xchart.demo.charts.bar.BarChart02;
import com.xeiam.xchart.demo.charts.bar.BarChart03;
import com.xeiam.xchart.demo.charts.bar.BarChart04;
import com.xeiam.xchart.demo.charts.bar.BarChart05;
import com.xeiam.xchart.demo.charts.bar.BarChart06;
import com.xeiam.xchart.demo.charts.bar.BarChart07;
import com.xeiam.xchart.demo.charts.bar.BarChart08;
import com.xeiam.xchart.demo.charts.date.DateChart01;
import com.xeiam.xchart.demo.charts.date.DateChart02;
import com.xeiam.xchart.demo.charts.date.DateChart03;
import com.xeiam.xchart.demo.charts.date.DateChart04;
import com.xeiam.xchart.demo.charts.date.DateChart05;
import com.xeiam.xchart.demo.charts.date.DateChart06;
import com.xeiam.xchart.demo.charts.date.DateChart07;
import com.xeiam.xchart.demo.charts.line.LineChart01;
import com.xeiam.xchart.demo.charts.line.LineChart02;
import com.xeiam.xchart.demo.charts.line.LineChart03;
import com.xeiam.xchart.demo.charts.line.LineChart04;
import com.xeiam.xchart.demo.charts.line.LineChart05;
import com.xeiam.xchart.demo.charts.line.LineChart06;
import com.xeiam.xchart.demo.charts.realtime.RealtimeChart01;
import com.xeiam.xchart.demo.charts.realtime.RealtimeChart02;
import com.xeiam.xchart.demo.charts.scatter.ScatterChart01;
import com.xeiam.xchart.demo.charts.scatter.ScatterChart02;
import com.xeiam.xchart.demo.charts.scatter.ScatterChart03;
import com.xeiam.xchart.demo.charts.scatter.ScatterChart04;
import com.xeiam.xchart.demo.charts.theme.ThemeChart01;
import com.xeiam.xchart.demo.charts.theme.ThemeChart02;
import com.xeiam.xchart.demo.charts.theme.ThemeChart03;

/**
 * Class containing all XChart example charts
 *
 * @author timmolter
 */
public class XChartDemo extends JPanel implements TreeSelectionListener {

  /** The main split frame */
  private JSplitPane splitPane;

  /** The tree */
  private JTree tree;

  /** The panel for chart */
  private XChartPanel chartPanel;

  /** real-time chart example */
  final RealtimeChart01 realtimeChart01 = new RealtimeChart01();
  final RealtimeChart02 realtimeChart02 = new RealtimeChart02();
  Timer timer = new Timer();

  /**
   * Constructor
   */
  public XChartDemo() {

    super(new GridLayout(1, 0));

    // Create the nodes.
    DefaultMutableTreeNode top = new DefaultMutableTreeNode("XChart Example Charts");
    createNodes(top);

    // Create a tree that allows one selection at a time.
    tree = new JTree(top);
    tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

    // Listen for when the selection changes.
    tree.addTreeSelectionListener(this);

    // Create the scroll pane and add the tree to it.
    JScrollPane treeView = new JScrollPane(tree);

    // Create Chart Panel
    chartPanel = new XChartPanel(new AreaChart01().getChart());

    // Add the scroll panes to a split pane.
    splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    splitPane.setTopComponent(treeView);
    splitPane.setBottomComponent(chartPanel);

    Dimension minimumSize = new Dimension(130, 160);
    treeView.setMinimumSize(minimumSize);
    splitPane.setPreferredSize(new Dimension(700, 700));

    // Add the split pane to this panel.
    add(splitPane);

  }

  @Override
  public void valueChanged(TreeSelectionEvent e) {

    DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

    if (node == null) {
      return;
    }

    Object nodeInfo = node.getUserObject();
    // tree leaf
    if (node.isLeaf()) {
      ChartInfo chartInfo = (ChartInfo) nodeInfo;
      // displayURL(chartInfo.bookURL);
      chartPanel = new XChartPanel(chartInfo.getExampleChart());
      splitPane.setBottomComponent(chartPanel);

      // start running a simulated data feed for the sample real-time plot
      timer.cancel(); // just in case
      if (chartInfo.getExampleChartName().startsWith("RealtimeChart01")) {
        // set up real-time chart simulated data feed
        TimerTask chartUpdaterTask = new TimerTask() {

          @Override
          public void run() {

            realtimeChart01.updateData();
            chartPanel.updateSeries(RealtimeChart01.SERIES_NAME, realtimeChart01.getyData());
          }
        };
        timer = new Timer();
        timer.scheduleAtFixedRate(chartUpdaterTask, 0, 500);
      }
      else if (chartInfo.getExampleChartName().startsWith("RealtimeChart02")) {
        // set up real-time chart simulated data feed
        TimerTask chartUpdaterTask = new TimerTask() {

          @Override
          public void run() {

            realtimeChart02.updateData();
            chartPanel.updateSeries(RealtimeChart02.SERIES_NAME, realtimeChart02.getxData(), realtimeChart02.getyData());
          }
        };
        timer = new Timer();
        timer.scheduleAtFixedRate(chartUpdaterTask, 0, 500);
      }
    }
  }

  /**
   * Create the tree
   *
   * @param top
   */
  private void createNodes(DefaultMutableTreeNode top) {

    // categories
    DefaultMutableTreeNode category = null;
    // leaves
    DefaultMutableTreeNode defaultMutableTreeNode = null;

    // Area category
    category = new DefaultMutableTreeNode("Area Charts");
    top.add(category);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("AreaChart01 - 3-Series", new AreaChart01().getChart()));
    category.add(defaultMutableTreeNode);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("AreaChart02 - Null Y-Axis Data Points", new AreaChart02().getChart()));
    category.add(defaultMutableTreeNode);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("AreaLineChart03 - Combination Are & Line Chart", new AreaLineChart03().getChart()));
    category.add(defaultMutableTreeNode);

    // Line category
    category = new DefaultMutableTreeNode("Line Charts");
    top.add(category);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("LineChart01 -  Logarithmic Y-Axis", new LineChart01().getChart()));
    category.add(defaultMutableTreeNode);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("LineChart02 - Customized Series Style", new LineChart02().getChart()));
    category.add(defaultMutableTreeNode);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("LineChart03 - Extensive Chart Customization", new LineChart03().getChart()));
    category.add(defaultMutableTreeNode);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("LineChart04 - Hundreds of Series on One Plot", new LineChart04().getChart()));
    category.add(defaultMutableTreeNode);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("LineChart05 - Scatter and Line", new LineChart05().getChart()));
    category.add(defaultMutableTreeNode);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("LineChart06 - Logarithmic Y-Axis with Error Bars", new LineChart06().getChart()));
    category.add(defaultMutableTreeNode);

    // Scatter category
    category = new DefaultMutableTreeNode("Scatter Charts");
    top.add(category);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("ScatterChart01 - Gaussian Blob", new ScatterChart01().getChart()));
    category.add(defaultMutableTreeNode);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("ScatterChart02 - Logarithmic Data", new ScatterChart02().getChart()));
    category.add(defaultMutableTreeNode);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("ScatterChart03 - Single point", new ScatterChart03().getChart()));
    category.add(defaultMutableTreeNode);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("ScatterChart04 - Error Bars", new ScatterChart04().getChart()));
    category.add(defaultMutableTreeNode);

    // Bar category
    category = new DefaultMutableTreeNode("Bar Charts");
    top.add(category);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("BarChart01 - Basic Bar Chart", new BarChart01().getChart()));
    category.add(defaultMutableTreeNode);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("BarChart02 - Date Categories", new BarChart02().getChart()));
    category.add(defaultMutableTreeNode);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("BarChart03 - Positive and Negative", new BarChart03().getChart()));
    category.add(defaultMutableTreeNode);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("BarChart04 - Missing Point in Series", new BarChart04().getChart()));
    category.add(defaultMutableTreeNode);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("BarChart05 - GGPlot2 Theme", new BarChart05().getChart()));
    category.add(defaultMutableTreeNode);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("BarChart06 - Histogram Overlapped", new BarChart06().getChart()));
    category.add(defaultMutableTreeNode);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("BarChart07 - Histogram Not Overlapped", new BarChart07().getChart()));
    category.add(defaultMutableTreeNode);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("BarChart08 - Histogram with Error Bars", new BarChart08().getChart()));
    category.add(defaultMutableTreeNode);

    // Theme category
    category = new DefaultMutableTreeNode("Chart Themes");
    top.add(category);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("ThemeChart01 - Default XChart Theme", new ThemeChart01().getChart()));
    category.add(defaultMutableTreeNode);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("ThemeChart02 - GGPlot2 Theme", new ThemeChart02().getChart()));
    category.add(defaultMutableTreeNode);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("ThemeChart03 - Matlab Theme", new ThemeChart03().getChart()));
    category.add(defaultMutableTreeNode);

    // Date category
    category = new DefaultMutableTreeNode("Date Charts");
    top.add(category);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("DateChart01 - Millisecond Scale", new DateChart01().getChart()));
    category.add(defaultMutableTreeNode);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("DateChart02 - Second Scale", new DateChart02().getChart()));
    category.add(defaultMutableTreeNode);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("DateChart03 - Minute Scale", new DateChart03().getChart()));
    category.add(defaultMutableTreeNode);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("DateChart04 - Hour Scale", new DateChart04().getChart()));
    category.add(defaultMutableTreeNode);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("DateChart05 - Day Scale", new DateChart05().getChart()));
    category.add(defaultMutableTreeNode);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("DateChart06 - Month Scale", new DateChart06().getChart()));
    category.add(defaultMutableTreeNode);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("DateChart07 - Year Scale", new DateChart07().getChart()));
    category.add(defaultMutableTreeNode);

    // Real-time category
    category = new DefaultMutableTreeNode("Real-time Charts");
    top.add(category);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("RealtimeChart01 - Fixed X-Axis Window", realtimeChart01.getChart()));
    category.add(defaultMutableTreeNode);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("RealtimeChart02 - Updating X-Axis Window", realtimeChart02.getChart()));
    category.add(defaultMutableTreeNode);

  }

  /**
   * Create the GUI and show it. For thread safety, this method should be invoked from the event dispatch thread.
   */
  private static void createAndShowGUI() {

    // Create and set up the window.
    JFrame frame = new JFrame("XChart Demo");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Add content to the window.
    frame.add(new XChartDemo());

    // Display the window.
    frame.pack();
    frame.setVisible(true);
  }

  public static void main(String[] args) {

    // Schedule a job for the event dispatch thread:
    // creating and showing this application's GUI.
    javax.swing.SwingUtilities.invokeLater(new Runnable() {

      @Override
      public void run() {

        createAndShowGUI();
      }
    });
  }
}
