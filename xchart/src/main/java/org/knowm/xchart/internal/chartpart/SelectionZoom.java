package org.knowm.xchart.internal.chartpart;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.internal.chartpart.components.ChartButton;

public class SelectionZoom extends MouseAdapter implements ChartPart, ActionListener {
  protected XChartPanel<XYChart> chartPanel;
  protected XYChart chart;

  protected Rectangle bounds;
  protected Color selectionColor;
  protected boolean resetByDoubleClick;
  protected boolean resetByButton;
  protected ChartButton resetButton;

  protected int x, x2;
  protected boolean filtered;

  public SelectionZoom() {

    x = -1;
    x2 = -1;
    selectionColor = new Color(0, 0, 192, 128);
    resetByDoubleClick = true;
    resetByButton = true;
    resetButton = new ChartButton("Reset Zoom");
  }

  public void init(XChartPanel<XYChart> chartPanel) {

    this.chartPanel = chartPanel;
    chart = chartPanel.getChart();
    chartPanel.addMouseListener(this);
    chartPanel.addMouseMotionListener(this);
    chart.addPlotPart(this);
    resetButton.init(chartPanel);
    resetButton.setVisible(false);
    resetButton.addActionListener(this);
  }

  protected void resetZoom() {

    chart.resetFilter();
    filtered = false;
    resetButton.setVisible(false);

    x = -1;
    x2 = -1;
    repaint();
  }

  protected void repaint() {

    chartPanel.invalidate();
    chartPanel.repaint();
  }

  @Override
  public Rectangle2D getBounds() {

    return bounds;
  }

  @Override
  public void paint(Graphics2D g) {

    if (x == -1 || x2 == -1) {
      return;
    }
    g.setColor(selectionColor);
    int xStart = Math.min(x, x2);
    int width = Math.abs(x - x2);
    bounds = g.getClipBounds();
    g.fillRect(xStart, 0, width, (int) (bounds.height + bounds.getY()));
  }

  public void mousePressed(MouseEvent e) {

    x = e.getX();
    repaint();
  }

  public void mouseDragged(MouseEvent e) {

    x2 = e.getX();
    repaint();
  }

  public void mouseReleased(MouseEvent e) {

    if (!isOverlapping()) {
      x = -1;
      x2 = -1;
      return;
    }

    if (bounds != null && x2 != -1) {
      int smallPoint;
      int bigPoint;
      if (x2 < x) {
        smallPoint = x2;
        bigPoint = x;
      } else {
        smallPoint = x;
        bigPoint = x2;
      }

      filtered = chart.filterXByScreen(smallPoint, bigPoint);
      resetButton.setVisible(filtered && resetByButton);
    }

    x = -1;
    x2 = -1;
    repaint();
  }

  @Override
  public void mouseClicked(MouseEvent e) {

    if (!filtered) {
      return;
    }
    if (resetByDoubleClick && e.getClickCount() == 2) {
      resetZoom();
      return;
    }
  }

  @Override
  public void actionPerformed(ActionEvent e) {

    // reset button pressed
    resetZoom();
  }

  public Color getSelectionColor() {

    return selectionColor;
  }

  public void setSelectionColor(Color selectionColor) {

    this.selectionColor = selectionColor;
  }

  public boolean isResetByDoubleClick() {

    return resetByDoubleClick;
  }

  public void setResetByDoubleClick(boolean resetByDoubleClick) {

    this.resetByDoubleClick = resetByDoubleClick;
  }

  public boolean isResetByButton() {

    return resetByButton;
  }

  public void setResetByButton(boolean resetByButton) {

    this.resetByButton = resetByButton;
    resetButton.setVisible(filtered && resetByButton);
  }

  public ChartButton getResetButton() {

    return resetButton;
  }

  public void setResetButton(ChartButton resetButton) {

    this.resetButton = resetButton;
  }

  /**
   * Whether the selectZoom overlaps with the chart.plot
   *
   * @return true:overlapping, false: No overlap
   */
  private boolean isOverlapping() {

    boolean isOverlapping = false;
    double start = x;
    double end = x2;
    if (x > x2) {
      start = x2;
      end = x;
    }
    // If the two intervals overlap, then largest beginning must be smaller than the smallest ending
    if (Math.max(start, chart.plot.bounds.getX())
        < Math.min(end, chart.plot.bounds.getX() + chart.plot.bounds.getWidth())) {
      isOverlapping = true;
    }
    return isOverlapping;
  }
}
