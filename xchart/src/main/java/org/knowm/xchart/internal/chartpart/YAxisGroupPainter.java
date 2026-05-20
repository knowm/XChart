package org.knowm.xchart.internal.chartpart;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.knowm.xchart.internal.series.AxesChartSeries;
import org.knowm.xchart.style.AxesChartStyler;
import org.knowm.xchart.style.Styler.YAxisPosition;

/**
 * Encapsulates all rendering logic for merged / visual Y-axis groups.
 *
 * <p>Called by {@link AxisPair#paint(Graphics2D)} to wire master/slave relationships, partition
 * axes into visual groups per side, and paint the left-side and right-side columns. The caller
 * retrieves the results (gridline masters, main axes, adjusted start positions) via getters and
 * applies fallback defaults (Step 5) itself.
 */
class YAxisGroupPainter<ST extends AxesChartStyler, S extends AxesChartSeries> {

  private final Chart<ST, S> chart;
  private final TreeMap<Integer, Axis_Y<ST, S>> yAxisMap;

  // Results populated by paintLeft() / paintRight()
  private Axis_Y<ST, S> leftGridlineMasterAxis;
  private Axis_Y<ST, S> rightGridlineMasterAxis;
  private Axis_Y<ST, S> leftMainYAxis;
  private Axis_Y<ST, S> rightMainYAxis;

  /** The adjusted x position used as the left origin; set during paintLeft(). */
  private double leftStartUsed;

  YAxisGroupPainter(Chart<ST, S> chart, TreeMap<Integer, Axis_Y<ST, S>> yAxisMap) {

    this.chart = chart;
    this.yAxisMap = yAxisMap;
  }

  // ---------------------------------------------------------------------------
  // Step 1 — wire master / slave relationships
  // ---------------------------------------------------------------------------

  /**
   * Resets all axes to independent mode, then wires master/slave relationships according to the
   * merge map in the styler. Safe to call even when no merging is configured (all axes reset to
   * independent).
   */
  void wireRelationships() {

    ST styler = chart.getStyler();
    Map<Integer, Integer> mergeMap = styler.getYAxisGroupMergeMap();

    // Always reset to clean state first
    for (Axis_Y<ST, S> ya : yAxisMap.values()) {
      ya.setMasterAxis(null);
      ya.setAxisLineOwner(true);
    }

    for (Entry<Integer, Integer> entry : mergeMap.entrySet()) {
      int slaveIndex = entry.getKey();
      int masterIndex = entry.getValue();
      if (slaveIndex == masterIndex) {
        continue; // this IS the master entry
      }
      Axis_Y<ST, S> slaveAxis = yAxisMap.get(slaveIndex);
      Axis_Y<ST, S> masterAxis = yAxisMap.get(masterIndex);
      if (slaveAxis != null && masterAxis != null) {
        slaveAxis.setMasterAxis(masterAxis);
        slaveAxis.setAxisLineOwner(false);
      }
    }
  }

  // ---------------------------------------------------------------------------
  // Step 2+3 — partition into visual groups then paint the left side
  // ---------------------------------------------------------------------------

  /**
   * Paints all left-side Y-axis columns.
   *
   * <p>After this call, {@link #getLeftMainYAxis()}, {@link #getLeftGridlineMasterAxis()}, and
   * {@link #getLeftStartUsed()} carry the results.
   *
   * @param g graphics context
   * @param chartPadding chart padding; used as the initial leftStart and as inter-axis gap
   * @param leftYAxisBounds output bounds rectangle updated in-place
   * @param desiredLeftYAxisWidth optional minimum total width (0 = auto)
   * @return the x position just past the innermost left column (= left edge of the plot area)
   */
  double paintLeft(
      Graphics2D g,
      int chartPadding,
      Rectangle2D.Double leftYAxisBounds,
      int desiredLeftYAxisWidth) {

    ST styler = chart.getStyler();
    int paddingBetweenAxes = chartPadding;
    int tickMargin = styler.isYAxisTicksVisible() ? styler.getPlotMargin() : 0;

    TreeMap<Integer, List<Integer>> leftVisualGroups = buildVisualGroups(false);

    double leftStart = chartPadding;
    int leftCount = 0;

    // Honour the caller's minimum-width hint
    if (desiredLeftYAxisWidth > 0) {
      double widthEstimation = 0;
      for (List<Integer> groupIndices : leftVisualGroups.values()) {
        for (int i = 0; i < groupIndices.size(); i++) {
          if (styler.isMergedAxisColocateSlaveLabels() && i > 0) {
            continue; // colocate slaves share the master column
          }
          Axis_Y<ST, S> ya = yAxisMap.get(groupIndices.get(i));
          ya.preparePaint();
          widthEstimation += ya.getBounds().getWidth();
          leftCount++;
        }
      }
      if (leftCount > 1) {
        widthEstimation += (leftCount - 1) * paddingBetweenAxes;
      }
      widthEstimation += leftCount * tickMargin;
      if (widthEstimation < desiredLeftYAxisWidth) {
        leftStart = desiredLeftYAxisWidth - widthEstimation;
      }
      leftCount = 0;
    }

    leftStartUsed = leftStart;
    leftYAxisBounds.width = 0;

    for (Entry<Integer, List<Integer>> groupEntry : leftVisualGroups.entrySet()) {
      List<Integer> groupIndices = groupEntry.getValue();
      Axis_Y<ST, S> masterAxis = yAxisMap.get(groupIndices.get(0));
      masterAxis.clearColocatedSlaves();

      boolean colocate = styler.isMergedAxisColocateSlaveLabels() && groupIndices.size() > 1;

      if (colocate) {
        // Slaves render inline on the master column — no separate column per slave
        if (masterAxis.getAxisTickCalculator() == null) {
          masterAxis.preparePaint();
        }
        for (int i = 1; i < groupIndices.size(); i++) {
          Axis_Y<ST, S> slave = yAxisMap.get(groupIndices.get(i));
          slave.preparePaint(); // builds AxisTickCalculator_Synchronized from master
          masterAxis.addColocatedSlave(slave);
        }
        masterAxis.preparePaint(); // re-prep so width hint accounts for all slave label widths
        Rectangle2D.Double bounds = (Rectangle2D.Double) masterAxis.getBounds();
        bounds.x = leftStart;
        masterAxis.paint(g);
        leftStart += paddingBetweenAxes + bounds.getWidth() + tickMargin;
        leftYAxisBounds.width += bounds.getWidth();
        leftCount++;
        leftMainYAxis = masterAxis;
      } else {
        // Each slave gets its own column; paint slaves outermost → innermost
        for (int i = groupIndices.size() - 1; i >= 1; i--) {
          Axis_Y<ST, S> slave = yAxisMap.get(groupIndices.get(i));
          if (masterAxis.getAxisTickCalculator() == null) {
            masterAxis.preparePaint();
          }
          slave.preparePaint();
          Rectangle2D.Double bounds = (Rectangle2D.Double) slave.getBounds();
          bounds.x = leftStart;
          slave.paint(g);
          leftStart += paddingBetweenAxes + bounds.getWidth() + tickMargin;
          leftYAxisBounds.width += bounds.getWidth();
          leftCount++;
          leftMainYAxis = slave;
        }
        masterAxis.preparePaint();
        Rectangle2D.Double bounds = (Rectangle2D.Double) masterAxis.getBounds();
        bounds.x = leftStart;
        masterAxis.paint(g);
        leftStart += paddingBetweenAxes + bounds.getWidth() + tickMargin;
        leftYAxisBounds.width += bounds.getWidth();
        leftCount++;
        leftMainYAxis = masterAxis;
      }

      if (leftGridlineMasterAxis == null) {
        leftGridlineMasterAxis = masterAxis;
      }
    }

    if (leftCount > 1) {
      leftYAxisBounds.width += (leftCount - 1) * paddingBetweenAxes;
    }
    leftYAxisBounds.width += leftCount * tickMargin;

    return leftStart;
  }

  // ---------------------------------------------------------------------------
  // Step 4 — paint the right side (mirror of paintLeft)
  // ---------------------------------------------------------------------------

  /**
   * Paints all right-side Y-axis columns.
   *
   * <p>After this call, {@link #getRightMainYAxis()} and {@link #getRightGridlineMasterAxis()}
   * carry the results.
   *
   * @param g graphics context
   * @param rightEnd the x position of the right boundary (chart width minus legend/padding)
   * @param chartPadding inter-axis gap
   * @param rightYAxisBounds output bounds rectangle updated in-place (x and width)
   * @return the updated rightEnd after all right columns have been painted
   */
  double paintRight(
      Graphics2D g,
      double rightEnd,
      int chartPadding,
      Rectangle2D.Double rightYAxisBounds) {

    ST styler = chart.getStyler();
    int paddingBetweenAxes = chartPadding;
    int tickMargin = styler.isYAxisTicksVisible() ? styler.getPlotMargin() : 0;

    TreeMap<Integer, List<Integer>> rightVisualGroups = buildVisualGroups(true);

    rightYAxisBounds.width = 0;
    int rightCount = 0;

    // Traverse in descending key order so the lowest visual group ID ends up innermost (closest
    // to the plot area)
    for (Entry<Integer, List<Integer>> groupEntry :
        rightVisualGroups.descendingMap().entrySet()) {
      List<Integer> groupIndices = groupEntry.getValue();
      Axis_Y<ST, S> masterAxis = yAxisMap.get(groupIndices.get(0));
      masterAxis.clearColocatedSlaves();

      boolean colocate = styler.isMergedAxisColocateSlaveLabels() && groupIndices.size() > 1;

      if (colocate) {
        if (masterAxis.getAxisTickCalculator() == null) {
          masterAxis.preparePaint();
        }
        for (int i = 1; i < groupIndices.size(); i++) {
          Axis_Y<ST, S> slave = yAxisMap.get(groupIndices.get(i));
          slave.preparePaint();
          masterAxis.addColocatedSlave(slave);
        }
        masterAxis.preparePaint();
        Rectangle2D.Double bounds = (Rectangle2D.Double) masterAxis.getBounds();
        double w = bounds.getWidth();
        double xOffset = rightEnd - w;
        bounds.x = xOffset;
        rightYAxisBounds.x = xOffset;
        masterAxis.paint(g);
        rightYAxisBounds.width += w;
        rightEnd -= paddingBetweenAxes + w + tickMargin;
        rightCount++;
        rightMainYAxis = masterAxis;
      } else {
        // Slaves outermost first
        for (int i = groupIndices.size() - 1; i >= 1; i--) {
          Axis_Y<ST, S> slave = yAxisMap.get(groupIndices.get(i));
          if (masterAxis.getAxisTickCalculator() == null) {
            masterAxis.preparePaint();
          }
          slave.preparePaint();
          Rectangle2D.Double bounds = (Rectangle2D.Double) slave.getBounds();
          double w = bounds.getWidth();
          double xOffset = rightEnd - w;
          bounds.x = xOffset;
          rightYAxisBounds.x = xOffset;
          slave.paint(g);
          rightYAxisBounds.width += w;
          rightEnd -= paddingBetweenAxes + w + tickMargin;
          rightCount++;
          rightMainYAxis = slave;
        }
        masterAxis.preparePaint();
        Rectangle2D.Double bounds = (Rectangle2D.Double) masterAxis.getBounds();
        double w = bounds.getWidth();
        double xOffset = rightEnd - w;
        bounds.x = xOffset;
        rightYAxisBounds.x = xOffset;
        masterAxis.paint(g);
        rightYAxisBounds.width += w;
        rightEnd -= paddingBetweenAxes + w + tickMargin;
        rightCount++;
        rightMainYAxis = masterAxis;
      }

      if (rightGridlineMasterAxis == null) {
        rightGridlineMasterAxis = masterAxis;
      }
    }

    if (rightCount > 1) {
      rightYAxisBounds.width += (rightCount - 1) * paddingBetweenAxes;
    }
    rightYAxisBounds.width += rightCount * tickMargin;

    return rightEnd;
  }

  // ---------------------------------------------------------------------------
  // Private helpers
  // ---------------------------------------------------------------------------

  /**
   * Partitions all Y-axes into visual groups for one side.
   *
   * @param onRight {@code true} for right-side axes, {@code false} for left
   * @return TreeMap keyed by visual group ID → sorted list of logical axis indices (ascending)
   */
  private TreeMap<Integer, List<Integer>> buildVisualGroups(boolean onRight) {

    ST styler = chart.getStyler();
    TreeMap<Integer, List<Integer>> visualGroups = new TreeMap<>();

    for (Integer logicalIndex : yAxisMap.keySet()) {
      YAxisPosition pos = styler.getYAxisGroupPosistion(logicalIndex);
      if ((pos == YAxisPosition.Right) != onRight) {
        continue;
      }
      int visualId = styler.getYAxisVisualGroup(logicalIndex);
      visualGroups.computeIfAbsent(visualId, k -> new ArrayList<>()).add(logicalIndex);
    }

    for (List<Integer> indices : visualGroups.values()) {
      Collections.sort(indices);
    }

    return visualGroups;
  }

  // ---------------------------------------------------------------------------
  // Result getters
  // ---------------------------------------------------------------------------

  Axis_Y<ST, S> getLeftMainYAxis() {

    return leftMainYAxis;
  }

  Axis_Y<ST, S> getRightMainYAxis() {

    return rightMainYAxis;
  }

  Axis_Y<ST, S> getLeftGridlineMasterAxis() {

    return leftGridlineMasterAxis;
  }

  Axis_Y<ST, S> getRightGridlineMasterAxis() {

    return rightGridlineMasterAxis;
  }

  /** The adjusted x start used as {@code leftYAxisBounds.x}; valid after {@link #paintLeft}. */
  double getLeftStartUsed() {

    return leftStartUsed;
  }
}
