package org.knowm.xchart.internal.chartpart;

import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;
import org.knowm.xchart.ChartDataPoint;
import org.knowm.xchart.DataPointListener;

/**
 * Performs collision detection between the mouse and the per-data-point hit shapes collected in
 * {@link PlotInteractionData}, dispatching {@link DataPointListener} callbacks. Mirrors the {@link
 * ToolTips}/{@link Cursor} pattern: it is registered on the {@link org.knowm.xchart.XChartPanel} as
 * a mouse (motion) listener and fed fresh data after every repaint via {@link #setData}.
 */
public class DataPointDispatcher extends MouseAdapter {

  // Matches the default marker hit shape built in ToolTips.ToolTip when no explicit shape is given.
  private static final double MARGIN = 5;

  private final List<DataPointListener> listeners;
  private final List<ChartDataPoint> dataPoints = new ArrayList<>();
  private ChartDataPoint hovered = null;

  public DataPointDispatcher(List<DataPointListener> listeners) {
    this.listeners = listeners;
  }

  public void setData(PlotInteractionData data) {

    dataPoints.clear();
    if (data == null) {
      return;
    }
    for (PlotInteractionData.ToolTipData td : data.getToolTipDataList()) {
      Shape shape = td.shape != null ? td.shape : defaultMarkerShape(td.x, td.y);
      dataPoints.add(
          new ChartDataPoint(
              td.seriesName, td.dataPointIndex, td.label, td.xValue, td.yValue, td.x, td.y, shape));
    }
  }

  private static Shape defaultMarkerShape(double x, double y) {

    double halfSize = MARGIN * 1.5;
    double markerSize = MARGIN * 3;
    return new Ellipse2D.Double(x - halfSize, y - halfSize, markerSize, markerSize);
  }

  private ChartDataPoint hitTest(int x, int y) {

    for (ChartDataPoint dataPoint : dataPoints) {
      if (dataPoint.getShape().contains(x, y)) {
        return dataPoint;
      }
    }
    return null;
  }

  /** Whether the given panel coordinates fall on a rendered data point's hit shape. */
  public boolean isOverDataPoint(int x, int y) {
    return hitTest(x, y) != null;
  }

  @Override
  public void mouseMoved(MouseEvent e) {

    if (listeners.isEmpty()) {
      return;
    }
    ChartDataPoint hit = hitTest(e.getX(), e.getY());

    if (hit == null) {
      if (hovered != null) {
        ChartDataPoint exited = hovered;
        hovered = null;
        for (DataPointListener listener : listeners) {
          listener.onDataPointExit(exited, e);
        }
      }
      return;
    }

    if (!hit.equals(hovered)) {
      hovered = hit;
      for (DataPointListener listener : listeners) {
        listener.onDataPointHover(hit, e);
      }
    }
  }

  @Override
  public void mouseClicked(MouseEvent e) {

    if (listeners.isEmpty()) {
      return;
    }
    ChartDataPoint hit = hitTest(e.getX(), e.getY());
    if (hit != null) {
      for (DataPointListener listener : listeners) {
        listener.onDataPointClick(hit, e);
      }
    }
  }
}
