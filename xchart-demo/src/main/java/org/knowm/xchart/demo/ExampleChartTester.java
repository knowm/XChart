package org.knowm.xchart.demo;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.WindowConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.demo.charts.RealtimeExampleChart;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.style.Styler;

/** Class containing all XChart example charts */
public class ExampleChartTester extends JPanel implements TreeSelectionListener {

  public final class ExampleChartInfo {

    String exampleChartName;
    ExampleChart exampleChart;

    public ExampleChartInfo(ExampleChart exampleChart) {

      this.exampleChartName = exampleChart.getClass().getSimpleName();
      this.exampleChart = exampleChart;
    }

    public void setExampleChartName(String exampleChartName) {

      this.exampleChartName = exampleChartName;
    }

    public String getExampleChartName() {

      return exampleChartName;
    }

    public ExampleChart getExampleChart() {

      return exampleChart;
    }

    @Override
    public String toString() {

      return this.exampleChartName;
    }
  }

  /** The main split frame */
  protected JSplitPane splitPane;

  /** The tree */
  protected JTree tree;

  /** The tabbed pane for charts */
  protected JTabbedPane tabbedPane;

  Timer timer = new Timer();

  ArrayList<ExampleChart> exampleList;
  Set<Class> excludeSet;

  /** Constructor */
  public ExampleChartTester() {

    super(new GridLayout(1, 0));
  }

  protected void init() {

    // Create the nodes.
    DefaultMutableTreeNode top = new DefaultMutableTreeNode("XChart Example Charts");
    createNodes(top);
    tree = new JTree(top);

    // Create a tree that allows one selection at a time.
    tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

    // Listen for when the selection changes.
    tree.addTreeSelectionListener(this);

    // Create the scroll pane and add the tree to it.
    JScrollPane treeView = new JScrollPane(tree);

    // Create Chart Panel
    tabbedPane = new JTabbedPane();

    for (int i = 0; i < tree.getRowCount(); i++) {
      tree.expandRow(i);
    }

    // select first leaf
    DefaultMutableTreeNode firstLeaf = top.getFirstLeaf();
    tree.setSelectionPath(new TreePath(firstLeaf.getPath()));

    // Add the scroll panes to a split pane.
    splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    splitPane.setTopComponent(treeView);
    splitPane.setBottomComponent(tabbedPane);

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
      if (!(nodeInfo instanceof ExampleChartInfo)) {
        return;
      }
      ExampleChartInfo chartInfo = (ExampleChartInfo) nodeInfo;
      // displayURL(chartInfo.bookURL);
      int tabCount = tabbedPane.getTabCount();
      for (int i = tabCount - 1; i >= 0; i--) {
        tabbedPane.remove(i);
      }

      Map<String, Chart> charts = getCharts(chartInfo);
      addCharts(tabbedPane, charts);

      // start running a simulated data feed for the sample real-time plot
      timer.cancel(); // just in case
      ExampleChart exampleChart = chartInfo.getExampleChart();
      if (exampleChart instanceof RealtimeExampleChart) {
        final RealtimeExampleChart realtimeChart = (RealtimeExampleChart) exampleChart;
        TimerTask chartUpdaterTask =
            new TimerTask() {

              @Override
              public void run() {

                realtimeChart.updateData();
                tabbedPane.revalidate();
                tabbedPane.repaint();
              }
            };
        timer = new Timer();
        timer.scheduleAtFixedRate(chartUpdaterTask, 0, 500);
      }
    }
  }

  protected Map<String, Chart> getCharts(ExampleChartInfo chartInfo) {

    Chart<?, ?> chart = chartInfo.getExampleChart().getChart();
    HashMap<String, Chart> map = new HashMap<String, Chart>();
    map.put(chart.getTitle(), chart);
    return map;
  }

  protected void addCharts(JTabbedPane tabbedPane, Map<String, Chart> chartMap) {

    for (Entry<String, Chart> e : chartMap.entrySet()) {

      Chart chart = e.getValue();
      if (chart == null) {
        continue;
      }
      XChartPanel chartPanel = new XChartPanel(chart);
      tabbedPane.addTab(e.getKey(), chartPanel);
    }
  }

  /**
   * Create the tree
   *
   * @param top
   */
  private void createNodes(DefaultMutableTreeNode top) {

    exampleList = getExampleCharts();

    // categories
    DefaultMutableTreeNode category = null;
    // leaves
    DefaultMutableTreeNode defaultMutableTreeNode;
    String categoryName = "";

    for (ExampleChart exampleChart : exampleList) {
      if (skipExampleChart(exampleChart)) {
        continue;
      }
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
      defaultMutableTreeNode = new DefaultMutableTreeNode(new ExampleChartInfo(exampleChart));
      category.add(defaultMutableTreeNode);
    }
  }

  protected boolean skipExampleChart(ExampleChart exampleChart) {

    if (excludeSet != null && excludeSet.contains(exampleChart.getClass())) {
      return true;
    }

    return false;
  }

  protected ArrayList<ExampleChart> getExampleCharts() {

    if (exampleList != null && !exampleList.isEmpty()) {
      return exampleList;
    }

    ArrayList<ExampleChart> exList = new ArrayList<ExampleChart>();

    List<ExampleChart<Chart<Styler, Series>>> demoCharts = DemoChartsUtil.getAllDemoCharts();
    if (demoCharts != null) {

      exList.addAll(demoCharts);
    }
    return exList;
  }

  public Set<Class> getExcludeSet() {

    return excludeSet;
  }

  public void setExcludeSet(Set<Class> excludeSet) {

    this.excludeSet = excludeSet;
  }

  public ArrayList<ExampleChart> getExampleList() {

    return exampleList;
  }

  public void setExampleList(ArrayList<ExampleChart> exampleList) {

    this.exampleList = exampleList;
  }

  /**
   * Create the GUI and show it. For thread safety, this method should be invoked from the event
   * dispatch thread.
   */
  public JFrame createAndShowGUI() {

    final JFrame frame = new JFrame("XChart Demo");
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    init();

    // Schedule a job for the event dispatch thread:
    // creating and showing this application's GUI.

    javax.swing.SwingUtilities.invokeLater(
        new Runnable() {

          @Override
          public void run() {

            // Create and set up the window.

            // Add content to the window.
            frame.add(ExampleChartTester.this);

            // Display the window.
            frame.pack();
            frame.setVisible(true);
          }
        });

    return frame;
  }

  public static void main(String[] args) {

    ExampleChartTester tester = new ExampleChartTester();
    tester.createAndShowGUI();
  }
}
