/**
 * Copyright (c) 2011-2013 Xeiam LLC.
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
import com.xeiam.xchart.demo.charts.line.LineChart01;
import com.xeiam.xchart.demo.charts.line.LineChart02;
import com.xeiam.xchart.demo.charts.line.LineChart03;
import com.xeiam.xchart.demo.charts.line.LineChart04;
import com.xeiam.xchart.demo.charts.line.LineChart05;
import com.xeiam.xchart.demo.charts.line.LineChart06;
import com.xeiam.xchart.demo.charts.line.LineChart07;
import com.xeiam.xchart.demo.charts.line.LineChart08;
import com.xeiam.xchart.demo.charts.line.LineChart09;
import com.xeiam.xchart.demo.charts.line.LineChart10;
import com.xeiam.xchart.demo.charts.line.LineChart11;
import com.xeiam.xchart.demo.charts.scatter.ScatterChart01;

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
  private JPanel chartPanel;

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
    chartPanel = new XChartPanel(new LineChart01().getChart());

    // Add the scroll panes to a split pane.
    splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    splitPane.setTopComponent(treeView);
    splitPane.setBottomComponent(chartPanel);

    Dimension minimumSize = new Dimension(100, 125);
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
    DefaultMutableTreeNode chart = null;

    // First category
    category = new DefaultMutableTreeNode("Line Charts");
    top.add(category);

    chart = new DefaultMutableTreeNode(new ChartInfo("LineChart01 - Manual Data", new LineChart01().getChart()));
    category.add(chart);

    chart = new DefaultMutableTreeNode(new ChartInfo("LineChart02 - Customized series style", new LineChart02().getChart()));
    category.add(chart);

    chart = new DefaultMutableTreeNode(new ChartInfo("LineChart03 - Multiple curves on one Chart", new LineChart03().getChart()));
    category.add(chart);

    chart = new DefaultMutableTreeNode(new ChartInfo("LineChart04 - Date Axis", new LineChart04().getChart()));
    category.add(chart);

    chart = new DefaultMutableTreeNode(new ChartInfo("LineChart05 - Vertical and horizontal lines", new LineChart05().getChart()));
    category.add(chart);

    chart = new DefaultMutableTreeNode(new ChartInfo("LineChart06 - Single point", new LineChart06().getChart()));
    category.add(chart);

    chart = new DefaultMutableTreeNode(new ChartInfo("LineChart07 - Longs as X-Axis data", new LineChart07().getChart()));
    category.add(chart);

    chart = new DefaultMutableTreeNode(new ChartInfo("LineChart08 - Error bars", new LineChart08().getChart()));
    category.add(chart);

    chart = new DefaultMutableTreeNode(new ChartInfo("LineChart09 - Extensive chart customization", new LineChart09().getChart()));
    category.add(chart);

    chart = new DefaultMutableTreeNode(new ChartInfo("LineChart10 - Plots Hundreds of Series on One Plot", new LineChart10().getChart()));
    category.add(chart);

    chart = new DefaultMutableTreeNode(new ChartInfo("LineChart11 - Using ChartBuilder to Make a Chart", new LineChart11().getChart()));
    category.add(chart);

    // Second category
    category = new DefaultMutableTreeNode("Scatter Charts");
    top.add(category);

    chart = new DefaultMutableTreeNode(new ChartInfo("ScatterChart01 - Gaussian Blob Scatter Plot", new ScatterChart01().getChart()));
    category.add(chart);

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
