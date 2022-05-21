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

  protected final XChartPanel<XYChart> xChartPanel;
  protected final XYChart xyChart;
  protected Rectangle bounds;

  protected final ChartButton resetButton;

  protected int x1, x2;
  protected boolean filtered;

  /**
   * Constructor
   *
   * @param xChartPanel
   * @param resetString
   */
  public ChartZoom(XYChart xyChart, XChartPanel<XYChart> xChartPanel, String resetString) {

    resetXY();

    this.xChartPanel = xChartPanel;
    this.xyChart = xyChart;
    xyChart.plot.plotContent.setChartZoom(this);

    resetButton = new ChartButton(xyChart, xChartPanel, resetString);
    resetButton.addActionListener(this);
    resetButton.setVisible(false);
  }

  protected void resetZoom() {

    resetFilter();
    filtered = false;
    resetButton.setVisible(false);

    resetXY();
    repaint();
  }

  public void resetXY() {
		x1 = -1;
	    x2 = -1;
  }
  
  private void repaint() {

    xChartPanel.invalidate();
    xChartPanel.repaint();
  }

  @Override
  public Rectangle2D getBounds() {

    return bounds;
  }

  @Override
  public void paint(Graphics2D graphic) {

    //    here either 1. the mouse was released and the chart was zoomed so we need the reset button
    // or 2. nothing should be drawn or 3. the zoom area
    //    should be drawn
	boolean isResetX1X2 = (x1 == -1 || x2 == -1);
    boolean needResetButton = resetButton.visible && isResetX1X2;
    
	if (needResetButton) { //
      resetButton.paint(graphic);
    } else if (isResetX1X2) {
      return;
    } else {
      graphic.setColor(xyChart.getStyler().getZoomSelectionColor());
      int xStart = Math.min(x1, x2);
      int width = Math.abs(x1 - x2);
      bounds = graphic.getClipBounds();
      graphic.fillRect(xStart, 0, width, (int) (bounds.height + bounds.getY()));
    }
  }

  public void mousePressed(MouseEvent event) {

    x1 = event.getX();
    repaint();
  }

  public void mouseDragged(MouseEvent event) {

    x2 = event.getX();
    repaint();
  }

  public void mouseReleased(MouseEvent event) {

    //    System.out.println("Mouse released");
    if (!isOverlapping()) {
      resetXY();
      return;
    }

    boolean isDragged = bounds != null && x2 != -1;
    
	if (isDragged) {
      int smallPoint;
      int bigPoint;
      boolean x1isBigpoint = x2 < x1;
	if (x1isBigpoint) {
        smallPoint = x2;
        bigPoint = x1;
      } else {
        smallPoint = x1;
        bigPoint = x2;
      }

      filtered = filterXByScreen(smallPoint, bigPoint);
      resetButton.setVisible(filtered && xyChart.getStyler().isZoomResetByButton());
    }

    resetXY();
    repaint();
  }

  public boolean filterXByScreen(int screenXmin, int screenXmax) {

    // convert screen coordinates to axis values
    double minValue = xyChart.axisPair.getXAxis().getChartValue(screenXmin);
    double maxValue = xyChart.axisPair.getXAxis().getChartValue(screenXmax);
    boolean filtered = false;
    if (isOnePointSeleted(minValue, maxValue)) {
      for (XYSeries series : xyChart.getSeriesMap().values()) {
        boolean isfiltered = series.filterXByValue(minValue, maxValue);
        if (isfiltered) {
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
        boolean betweenMinAndMax = x >= minValue && x <= maxValue;
		if (betweenMinAndMax) {
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
  public void mouseClicked(MouseEvent event) {

    if (!filtered) {
      return;
    }
    if (xyChart.getStyler().isZoomResetByDoubleClick() && event.getClickCount() == 2) {
      resetZoom();
      return;
    }
  }

  @Override
  public void actionPerformed(ActionEvent event) {

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
    double largestBeginning = Math.max(start, xyChart.plot.bounds.getX());
    double smallestEnding = Math.min(end, xyChart.plot.bounds.getX() + xyChart.plot.bounds.getWidth());
    
    if (largestBeginning < smallestEnding) {
      isOverlapping = true;
    }
    return isOverlapping;
  }
}