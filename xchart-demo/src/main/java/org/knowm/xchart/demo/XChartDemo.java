package org.knowm.xchart.demo;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.WindowConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.demo.charts.RealtimeExampleChart;
import org.knowm.xchart.demo.charts.area.AreaChart01;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.style.Styler;

/** Class containing all XChart example charts */
public class XChartDemo extends JPanel implements TreeSelectionListener {

  /** The main split frame */
  private final JSplitPane splitPane;

  /** The tree */
  private final JTree tree;

  /** The panel for chart */
  protected XChartPanel chartPanel;

  Timer timer = new Timer();

  /** Constructor */
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
      chartPanel = new XChartPanel(chartInfo.getExampleChart().getChart());
      splitPane.setBottomComponent(chartPanel);

      // start running a simulated data feed for the sample real-time plot
      timer.cancel(); // just in case
      if (chartInfo.getExampleChart() instanceof RealtimeExampleChart) {
        final RealtimeExampleChart realtimeChart =
            (RealtimeExampleChart) chartInfo.getExampleChart();
        TimerTask chartUpdaterTask =
            new TimerTask() {

              @Override
              public void run() {

                realtimeChart.updateData();
                chartPanel.revalidate();
                chartPanel.repaint();
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
    DefaultMutableTreeNode defaultMutableTreeNode;

    List<ExampleChart<Chart<Styler, Series>>> exampleList = DemoChartsUtil.getAllDemoCharts();
    String categoryName = "";
    for (ExampleChart exampleChart : exampleList) {
      String name = exampleChart.getClass().getSimpleName();
      name = name.substring(0, name.indexOf("Chart"));
      if (!categoryName.equals(name)) {
        String label = name.equals("") ? "Chart Themes" : (name + " Charts");
        if (label.equals("Realtime Charts")) {
          label = "Real-time Charts";
        }
        category = new DefaultMutableTreeNode(label);
        top.add(category);
        categoryName = name;
      }
      defaultMutableTreeNode =
          new DefaultMutableTreeNode(
              new ChartInfo(exampleChart.getExampleChartName(), exampleChart));
      category.add(defaultMutableTreeNode);
    }
  }

  /**
   * Create the GUI and show it. For thread safety, this method should be invoked from the event
   * dispatch thread.
   */
  private static void createAndShowGUI() {

    // Create and set up the window.
    JFrame frame = new JFrame("XChart Demo");
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    // Add content to the window.
    frame.add(new XChartDemo());

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
