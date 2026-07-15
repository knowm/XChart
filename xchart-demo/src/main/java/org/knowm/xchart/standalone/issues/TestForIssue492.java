package org.knowm.xchart.standalone.issues;

import java.awt.event.MouseEvent;
import java.util.Arrays;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.ChartDataPoint;
import org.knowm.xchart.DataPointListener;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.style.Styler.LegendPosition;

/**
 * Issue #492: Collision detection — fire a callback when the mouse hovers over or clicks an
 * individual rendered shape (e.g. the first bar in a bar chart).
 *
 * <p>Hover over the bars and left-click them to see events printed to the console. Right-click a bar
 * to get a custom per-data-point context menu (the default Save As / Print menu is auto-suppressed
 * over data points; right-click the empty plot area to see it still works there).
 */
public class TestForIssue492 {

  public static void main(String[] args) {

    CategoryChart chart = getChart();

    SwingWrapper<CategoryChart> sw = new SwingWrapper<>(chart);
    sw.displayChart();

    XChartPanel<CategoryChart> panel = sw.getXChartPanel();
    panel.addDataPointListener(
        new DataPointListener() {

          @Override
          public void onDataPointHover(ChartDataPoint dataPoint, MouseEvent e) {
            System.out.println("HOVER " + describe(dataPoint));
          }

          @Override
          public void onDataPointExit(ChartDataPoint dataPoint, MouseEvent e) {
            System.out.println("EXIT  " + describe(dataPoint));
          }

          @Override
          public void onDataPointClick(ChartDataPoint dataPoint, MouseEvent e) {
            // NOTE: isPopupTrigger() is unreliable in a click handler; check the button instead.
            if (SwingUtilities.isRightMouseButton(e)) {
              System.out.println("RIGHT-CLICK " + describe(dataPoint));
              showContextMenu(dataPoint, e);
            } else {
              System.out.println("LEFT-CLICK  " + describe(dataPoint));
            }
          }
        });
  }

  private static void showContextMenu(ChartDataPoint dataPoint, MouseEvent e) {

    JPopupMenu menu = new JPopupMenu();
    JMenuItem drillDown = new JMenuItem("Drill into " + describe(dataPoint));
    drillDown.addActionListener(
        ev -> JOptionPane.showMessageDialog(e.getComponent(), describe(dataPoint)));
    menu.add(drillDown);
    menu.show(e.getComponent(), e.getX(), e.getY());
  }

  private static String describe(ChartDataPoint dataPoint) {
    return dataPoint.getSeriesName()
        + " point #"
        + dataPoint.getDataPointIndex()
        + " ("
        + dataPoint.getXValue()
        + ", "
        + dataPoint.getYValue()
        + ")";
  }

  public static CategoryChart getChart() {

    CategoryChart chart =
        new CategoryChartBuilder()
            .width(800)
            .height(600)
            .title(TestForIssue492.class.getSimpleName())
            .xAxisTitle("Score")
            .yAxisTitle("Number")
            .build();

    chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
    chart.getStyler().setPlotGridLinesVisible(false);

    chart.addSeries("test 1", Arrays.asList(0, 1, 2, 3, 4), Arrays.asList(4, 5, 9, 6, 5));

    return chart;
  }
}
