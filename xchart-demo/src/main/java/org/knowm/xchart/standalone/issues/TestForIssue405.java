package org.knowm.xchart.standalone.issues;

import org.knowm.xchart.demo.*;
import java.awt.Dimension;
import java.awt.GridLayout;
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
import org.knowm.xchart.demo.charts.date.DateChart01a;
import org.knowm.xchart.demo.charts.date.DateChart03a;
import org.knowm.xchart.demo.charts.line.LineChart03a;

/** Class containing XChart example charts with MultipleAxes with colored Y-Axis-Title*/
public class TestForIssue405 extends JPanel implements TreeSelectionListener {

  /** The main split frame */
  private final JSplitPane splitPane;

  /** The tree */
  private final JTree tree;

  /** The panel for chart */
  protected XChartPanel chartPanel;

  /** Constructor */
  public TestForIssue405() {

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
    chartPanel = new XChartPanel(new LineChart03a().getChart());

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
    }
  }

  /**
   * Create the tree
   *
   * @param top
   */
  private void createNodes(DefaultMutableTreeNode top) {

    // categories
    DefaultMutableTreeNode category;
    // leaves
    DefaultMutableTreeNode defaultMutableTreeNode;

    // Line
    category = new DefaultMutableTreeNode("Line Charts");
    top.add(category);

    defaultMutableTreeNode =
        new DefaultMutableTreeNode(
            new ChartInfo(
                "LineChart03a - Extensive Chart Customization (Y-Axis Title color)", new LineChart03a().getChart()));
    category.add(defaultMutableTreeNode);

    // Date
    category = new DefaultMutableTreeNode("Date Charts");
    top.add(category);

    defaultMutableTreeNode =
        new DefaultMutableTreeNode(
            new ChartInfo(
                "DateChart01a - Millisecond Scale with Two Separate Y Axis Groups (title & color)",
                new DateChart01a().getChart()));
    category.add(defaultMutableTreeNode);

    defaultMutableTreeNode =
        new DefaultMutableTreeNode(
            new ChartInfo(
                "DateChart03a - Minute Scale with Two Separate Y Axis Groups (titled & colored)",
                new DateChart03a().getChart()));
    category.add(defaultMutableTreeNode);
  }

  /**
   * Create the GUI and show it. For thread safety, this method should be invoked from the event
   * dispatch thread.
   */
  private static void createAndShowGUI() {

    // Create and set up the window.
    JFrame frame = new JFrame("XChart colored Y-Axis-Title Demo");
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    // Add content to the window.
    frame.add(new TestForIssue405());

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
