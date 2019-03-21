package org.knowm.xchart.demo;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.knowm.xchart.demo.charts.area.AreaChart01;
import org.knowm.xchart.demo.charts.area.AreaChart02;
import org.knowm.xchart.demo.charts.area.AreaChart03;
import org.knowm.xchart.demo.charts.area.AreaChart04;
import org.knowm.xchart.demo.charts.bar.BarChart01;
import org.knowm.xchart.demo.charts.bar.BarChart02;
import org.knowm.xchart.demo.charts.bar.BarChart03;
import org.knowm.xchart.demo.charts.bar.BarChart04;
import org.knowm.xchart.demo.charts.bar.BarChart05;
import org.knowm.xchart.demo.charts.bar.BarChart06;
import org.knowm.xchart.demo.charts.bar.BarChart07;
import org.knowm.xchart.demo.charts.bar.BarChart08;
import org.knowm.xchart.demo.charts.bar.BarChart09;
import org.knowm.xchart.demo.charts.bar.BarChart10;
import org.knowm.xchart.demo.charts.bar.BarChart11;
import org.knowm.xchart.demo.charts.bar.BarChart12;
import org.knowm.xchart.demo.charts.bubble.BubbleChart01;
import org.knowm.xchart.demo.charts.date.DateChart01;
import org.knowm.xchart.demo.charts.date.DateChart02;
import org.knowm.xchart.demo.charts.date.DateChart03;
import org.knowm.xchart.demo.charts.date.DateChart04;
import org.knowm.xchart.demo.charts.date.DateChart05;
import org.knowm.xchart.demo.charts.date.DateChart06;
import org.knowm.xchart.demo.charts.date.DateChart07;
import org.knowm.xchart.demo.charts.date.DateChart08;
import org.knowm.xchart.demo.charts.dial.DialChart01;
import org.knowm.xchart.demo.charts.line.LineChart01;
import org.knowm.xchart.demo.charts.line.LineChart02;
import org.knowm.xchart.demo.charts.line.LineChart03;
import org.knowm.xchart.demo.charts.line.LineChart04;
import org.knowm.xchart.demo.charts.line.LineChart05;
import org.knowm.xchart.demo.charts.line.LineChart06;
import org.knowm.xchart.demo.charts.line.LineChart07;
import org.knowm.xchart.demo.charts.line.LineChart08;
import org.knowm.xchart.demo.charts.ohlc.OHLCChart01;
import org.knowm.xchart.demo.charts.ohlc.OHLCChart02;
import org.knowm.xchart.demo.charts.ohlc.OHLCChart03;
import org.knowm.xchart.demo.charts.pie.PieChart01;
import org.knowm.xchart.demo.charts.pie.PieChart02;
import org.knowm.xchart.demo.charts.pie.PieChart03;
import org.knowm.xchart.demo.charts.pie.PieChart04;
import org.knowm.xchart.demo.charts.pie.PieChart05;
import org.knowm.xchart.demo.charts.pie.PieChart06;
import org.knowm.xchart.demo.charts.radar.RadarChart01;
import org.knowm.xchart.demo.charts.realtime.RealtimeChart01;
import org.knowm.xchart.demo.charts.realtime.RealtimeChart02;
import org.knowm.xchart.demo.charts.realtime.RealtimeChart03;
import org.knowm.xchart.demo.charts.realtime.RealtimeChart04;
import org.knowm.xchart.demo.charts.realtime.RealtimeChart05;
import org.knowm.xchart.demo.charts.realtime.RealtimeChart06;
import org.knowm.xchart.demo.charts.scatter.ScatterChart01;
import org.knowm.xchart.demo.charts.scatter.ScatterChart02;
import org.knowm.xchart.demo.charts.scatter.ScatterChart03;
import org.knowm.xchart.demo.charts.scatter.ScatterChart04;
import org.knowm.xchart.demo.charts.stick.StickChart01;
import org.knowm.xchart.demo.charts.theme.ThemeChart01;
import org.knowm.xchart.demo.charts.theme.ThemeChart02;
import org.knowm.xchart.demo.charts.theme.ThemeChart03;
import org.knowm.xchart.demo.charts.theme.ThemeChart04;
import org.knowm.xchart.internal.chartpart.Chart;

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

    ArrayList<ExampleChart> exampleList = getExampleCharts();

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
    // Area
    exList.add(new AreaChart01());
    exList.add(new AreaChart02());
    exList.add(new AreaChart03());
    exList.add(new AreaChart04());

    // Pie
    exList.add(new PieChart01());
    exList.add(new PieChart02());
    exList.add(new PieChart03());
    exList.add(new PieChart04());
    exList.add(new PieChart05());
    exList.add(new PieChart06());

    // Line
    exList.add(new LineChart01());
    exList.add(new LineChart02());
    exList.add(new LineChart03());
    exList.add(new LineChart04());
    exList.add(new LineChart05());
    exList.add(new LineChart06());
    exList.add(new LineChart07());
    exList.add(new LineChart08());

    // Scatter
    exList.add(new ScatterChart01());
    exList.add(new ScatterChart02());
    exList.add(new ScatterChart03());
    exList.add(new ScatterChart04());

    // Bar
    exList.add(new BarChart01());
    exList.add(new BarChart02());
    exList.add(new BarChart03());
    exList.add(new BarChart04());
    exList.add(new BarChart05());
    exList.add(new BarChart06());
    exList.add(new BarChart07());
    exList.add(new BarChart08());
    exList.add(new BarChart09());
    exList.add(new BarChart10());
    exList.add(new BarChart11());
    exList.add(new BarChart12());

    // Radar
    exList.add(new RadarChart01());

    // Dial
    exList.add(new DialChart01());

    // Stick
    exList.add(new StickChart01());

    // Bubble
    exList.add(new BubbleChart01());

    // OHLC
    exList.add(new OHLCChart01());
    exList.add(new OHLCChart02());
    exList.add(new OHLCChart03());

    // Theme
    exList.add(new ThemeChart01());
    exList.add(new ThemeChart02());
    exList.add(new ThemeChart03());
    exList.add(new ThemeChart04());

    // Date
    exList.add(new DateChart01());
    exList.add(new DateChart02());
    exList.add(new DateChart03());
    exList.add(new DateChart04());
    exList.add(new DateChart05());
    exList.add(new DateChart06());
    exList.add(new DateChart07());
    exList.add(new DateChart08());

    // Real-time category
    exList.add(new RealtimeChart01());
    exList.add(new RealtimeChart02());
    exList.add(new RealtimeChart03());
    exList.add(new RealtimeChart04());
    exList.add(new RealtimeChart05());
    exList.add(new RealtimeChart06());
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
