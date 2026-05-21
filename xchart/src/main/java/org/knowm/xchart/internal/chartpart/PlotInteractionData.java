package org.knowm.xchart.internal.chartpart;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlotInteractionData {

  Rectangle2D plotBounds;

  Rectangle2D getPlotBounds() {

    return plotBounds;
  }

  private final List<ToolTipData> toolTipDataList = new ArrayList<>();
  private final List<CursorData> cursorDataList = new ArrayList<>();

  void clear() {
    toolTipDataList.clear();
    cursorDataList.clear();
  }

  List<ToolTipData> getToolTipDataList() {
    return Collections.unmodifiableList(toolTipDataList);
  }

  List<CursorData> getCursorDataList() {
    return Collections.unmodifiableList(cursorDataList);
  }

  // Pattern 1: x+y label pair, default ellipse hit shape
  void addToolTip(double x, double y, String xValue, String yValue) {
    toolTipDataList.add(new ToolTipData(null, x, y, 0, xValue, yValue, null));
  }

  // Pattern 2: single label, default ellipse hit shape
  void addToolTip(double x, double y, String label) {
    toolTipDataList.add(new ToolTipData(null, x, y, 0, null, null, label));
  }

  // Pattern 3: x+y label pair with explicit hit shape
  void addToolTip(Shape shape, double x, double y, double w, String xValue, String yValue) {
    toolTipDataList.add(new ToolTipData(shape, x, y, w, xValue, yValue, null));
  }

  // Pattern 4: single label with explicit hit shape
  void addToolTip(Shape shape, double x, double y, double w, String label) {
    toolTipDataList.add(new ToolTipData(shape, x, y, w, null, null, label));
  }

  void addCursorPoint(double x, double y, String xValue, String yValue, String seriesName) {
    cursorDataList.add(new CursorData(x, y, xValue, yValue, seriesName));
  }

  static final class ToolTipData {

    final Shape shape; // null means use default ellipse
    final double x;
    final double y;
    final double w;
    final String xValue; // non-null for x+y pair
    final String yValue; // non-null for x+y pair
    final String label; // non-null for single-label case

    ToolTipData(
        Shape shape,
        double x,
        double y,
        double w,
        String xValue,
        String yValue,
        String label) {
      this.shape = shape;
      this.x = x;
      this.y = y;
      this.w = w;
      this.xValue = xValue;
      this.yValue = yValue;
      this.label = label;
    }
  }

  static final class CursorData {

    final double x;
    final double y;
    final String xValue;
    final String yValue;
    final String seriesName;

    CursorData(double x, double y, String xValue, String yValue, String seriesName) {
      this.x = x;
      this.y = y;
      this.xValue = xValue;
      this.yValue = yValue;
      this.seriesName = seriesName;
    }
  }
}
