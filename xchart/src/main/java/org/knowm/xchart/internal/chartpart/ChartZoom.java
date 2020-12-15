package org.knowm.xchart.internal.chartpart;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYSeries;

public class ChartZoom extends MouseAdapter implements ChartPart, ActionListener {

  protected XChartPanel<XYChart> xChartPanel;
  protected XYChart xyChart;

  protected Rectangle bounds;

  protected ChartButton resetButton;

  protected int x1, x2;
  protected boolean filtered;

  /**
   * Constructor
   *
   * @param xChartPanel
   */
  public ChartZoom(XChartPanel<XYChart> xChartPanel) {

    x1 = -1;
    x2 = -1;

    this.xChartPanel = xChartPanel;

    xyChart = this.xChartPanel.getChart();
    xyChart.addPlotPart(this);

    resetButton = new ChartButton("Reset Zoom");
    resetButton.setPosition(xyChart.getStyler().getZoomResetButtomPosition());
    resetButton.init(this.xChartPanel);
    resetButton.setVisible(false);
    resetButton.addActionListener(this);
  }

  protected void resetZoom() {

    resetFilter();
    filtered = false;
    resetButton.setVisible(false);

    x1 = -1;
    x2 = -1;
    repaint();
  }

  protected void repaint() {

    xChartPanel.invalidate();
    xChartPanel.repaint();
  }

  @Override
  public Rectangle2D getBounds() {

    return bounds;
  }

  @Override
  public void paint(Graphics2D g) {

    if (x1 == -1 || x2 == -1) {
      return;
    }
    g.setColor(xyChart.getStyler().getZoomSelectionColor());
    int xStart = Math.min(x1, x2);
    int width = Math.abs(x1 - x2);
    bounds = g.getClipBounds();
    g.fillRect(xStart, 0, width, (int) (bounds.height + bounds.getY()));
  }

  public void mousePressed(MouseEvent e) {

    x1 = e.getX();
    repaint();
  }

  public void mouseDragged(MouseEvent e) {

    x2 = e.getX();
    repaint();
  }

  public void mouseReleased(MouseEvent e) {

    if (!isOverlapping()) {
      x1 = -1;
      x2 = -1;
      return;
    }

    if (bounds != null && x2 != -1) {
      int smallPoint;
      int bigPoint;
      if (x2 < x1) {
        smallPoint = x2;
        bigPoint = x1;
      } else {
        smallPoint = x1;
        bigPoint = x2;
      }

      filtered = filterXByScreen(smallPoint, bigPoint);
      resetButton.setVisible(filtered && xyChart.getStyler().isZoomResetByButton());
    }

    x1 = -1;
    x2 = -1;
    repaint();
  }

  public boolean filterXByScreen(int screenXmin, int screenXmax) {

    // convert screen coordinates to axis values
    double minValue = xyChart.axisPair.getXAxis().getChartValue(screenXmin);
    double maxValue = xyChart.axisPair.getXAxis().getChartValue(screenXmax);
    boolean filtered = false;
    if (isOnePointSeleted(minValue, maxValue)) {
      for (XYSeries series : xyChart.getSeriesMap().values()) {
        boolean f = series.filterXByValue(minValue, maxValue);
        if (f) {
          filtered = true;
        }
      }
    } else {
      if (!isAllPointsSelected()) {
        filtered = true;
      }
    }
    return filtered;
  }
  /**
   * Is there a point selected in all series.
   *
   * @param minValue
   * @param maxValue
   * @return
   */
  private boolean isOnePointSeleted(double minValue, double maxValue) {

    boolean isOnePointSeleted = false;
    double[] xData = null;
    for (XYSeries series : xyChart.getSeriesMap().values()) {
      xData = series.getXData();
      for (double x : xData) {
        if (x >= minValue && x <= maxValue) {
          isOnePointSeleted = true;
          break;
        }
      }
    }
    return isOnePointSeleted;
  }

  public void resetFilter() {

    for (XYSeries series : xyChart.getSeriesMap().values()) {
      series.resetFilter();
    }
  }

  public void filterXByIndex(int startIndex, int endIndex) {

    for (XYSeries series : xyChart.getSeriesMap().values()) {
      series.filterXByIndex(startIndex, endIndex);
    }
  }

  /**
   * Whether all points are selected in all series.
   *
   * @return
   */
  private boolean isAllPointsSelected() {

    boolean isAllPointsSelected = true;
    for (XYSeries series : xyChart.getSeriesMap().values()) {
      if (!series.isAllXData()) {
        isAllPointsSelected = false;
        break;
      }
    }
    return isAllPointsSelected;
  }

  @Override
  public void mouseClicked(MouseEvent e) {

    if (!filtered) {
      return;
    }
    if (xyChart.getStyler().isZoomResetByDoubleClick() && e.getClickCount() == 2) {
      resetZoom();
      return;
    }
  }

  @Override
  public void actionPerformed(ActionEvent e) {

    // reset button pressed
    resetZoom();
  }
  /**
   * Whether the selectZoom overlaps with the chart.plot
   *
   * @return true:overlapping, false: No overlap
   */
  private boolean isOverlapping() {

    boolean isOverlapping = false;
    double start = x1;
    double end = x2;
    if (x1 > x2) {
      start = x2;
      end = x1;
    }
    // If the two intervals overlap, then largest beginning must be smaller than the smallest ending
    if (Math.max(start, xyChart.plot.bounds.getX())
        < Math.min(end, xyChart.plot.bounds.getX() + xyChart.plot.bounds.getWidth())) {
      isOverlapping = true;
    }
    return isOverlapping;
  }
}
