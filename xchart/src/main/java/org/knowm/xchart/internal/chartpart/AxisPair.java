package org.knowm.xchart.internal.chartpart;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import org.knowm.xchart.CategorySeries.CategorySeriesRenderStyle;
import org.knowm.xchart.internal.series.AxesChartSeries;
import org.knowm.xchart.internal.series.AxesChartSeriesCategory;
import org.knowm.xchart.style.AxesChartStyler;
import org.knowm.xchart.style.BoxStyler;
import org.knowm.xchart.style.CategoryStyler;
import org.knowm.xchart.style.HorizontalBarStyler;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.knowm.xchart.style.Styler.YAxisPosition;

public class AxisPair<ST extends AxesChartStyler, S extends AxesChartSeries> implements ChartPart {

  private final Chart<ST, S> chart;

  private final Axis<ST, S> xAxis;
  private final Axis<ST, S> yAxis;
  private final TreeMap<Integer, Axis<ST, S>> yAxisMap;
  private final Rectangle2D.Double leftYAxisBounds;
  private final Rectangle2D.Double rightYAxisBounds;
  private Axis<ST, S> leftMainYAxis;
  private Axis<ST, S> rightMainYAxis;

  /**
   * The axis whose tick positions are used for horizontal gridlines and inner plot tick marks on
   * the left side. Equals {@code leftMainYAxis} when no axes are merged, otherwise equals the
   * master axis of the first left-side visual group.
   */
  private Axis<ST, S> leftGridlineMasterAxis;

  /** Same as {@link #leftGridlineMasterAxis} for the right side. */
  private Axis<ST, S> rightGridlineMasterAxis;

  /**
   * Constructor
   *
   * @param chart
   */
  public AxisPair(Chart<ST, S> chart) {

    this.chart = chart;

    // add axes
    xAxis = new Axis<ST, S>(chart, Axis.Direction.X, 0);
    yAxis = new Axis<ST, S>(chart, Axis.Direction.Y, 0);
    yAxisMap = new TreeMap<Integer, Axis<ST, S>>();
    yAxisMap.put(0, yAxis);
    leftYAxisBounds = new Rectangle2D.Double();
    rightYAxisBounds = new Rectangle2D.Double();
  }

  @Override
  public void paint(Graphics2D g) {

    prepareForPaint();

    leftMainYAxis = null;
    rightMainYAxis = null;
    leftGridlineMasterAxis = null;
    rightGridlineMasterAxis = null;

    ST styler = chart.getStyler();

    final int chartPadding = styler.getChartPadding();
    final int paddingBetweenAxes = chartPadding;

    int tickMargin = (styler.isYAxisTicksVisible() ? (styler.getPlotMargin()) : 0);

    // ------------------------------------------------------------------
    // Step 1: Apply master/slave relationships for merged visual groups.
    // The master is the axis with the lowest logical index in each group.
    // Reset all axes to independent mode first, then wire up slaves.
    // ------------------------------------------------------------------
    Map<Integer, Integer> mergeMap = styler.getYAxisGroupMergeMap();
    if (!mergeMap.isEmpty()) {
      // Reset all axes
      for (Axis<ST, S> ya : yAxisMap.values()) {
        ya.setMasterAxis(null);
        ya.setAxisLineOwner(true);
      }
      // Wire: slave → master, suppress slave's axis line
      for (Entry<Integer, Integer> entry : mergeMap.entrySet()) {
        int slaveIndex = entry.getKey();
        int masterIndex = entry.getValue();
        if (slaveIndex == masterIndex) {
          continue; // this IS the master entry
        }
        Axis<ST, S> slaveAxis = yAxisMap.get(slaveIndex);
        Axis<ST, S> masterAxis = yAxisMap.get(masterIndex);
        if (slaveAxis != null && masterAxis != null) {
          slaveAxis.setMasterAxis(masterAxis);
          slaveAxis.setAxisLineOwner(false);
        }
      }
    } else {
      // No merging — ensure clean state
      for (Axis<ST, S> ya : yAxisMap.values()) {
        ya.setMasterAxis(null);
        ya.setAxisLineOwner(true);
      }
    }

    // ------------------------------------------------------------------
    // Step 2: Build a visual-group → sorted-logical-indices mapping so
    // that masters (lowest index) are painted before slaves.
    //   visualGroupOrder: visual group ID → List<logical index> ascending
    // ------------------------------------------------------------------
    // For axes on the left side
    TreeMap<Integer, List<Integer>> leftVisualGroups = new TreeMap<>();
    TreeMap<Integer, List<Integer>> rightVisualGroups = new TreeMap<>();

    for (Integer logicalIndex : yAxisMap.keySet()) {
      YAxisPosition pos = styler.getYAxisGroupPosistion(logicalIndex);
      boolean onRight = (pos == YAxisPosition.Right);

      int visualId = styler.getYAxisVisualGroup(logicalIndex);
      TreeMap<Integer, List<Integer>> target = onRight ? rightVisualGroups : leftVisualGroups;
      target.computeIfAbsent(visualId, k -> new ArrayList<>()).add(logicalIndex);
    }
    // Sort each visual group's list ascending so masters come first
    for (List<Integer> indices : leftVisualGroups.values()) {
      Collections.sort(indices);
    }
    for (List<Integer> indices : rightVisualGroups.values()) {
      Collections.sort(indices);
    }

    // ------------------------------------------------------------------
    // Step 3: Paint left-side axes.
    // For each visual group the master (indices[0]) is painted innermost
    // (closest to the plot); slaves are painted further out.
    // ------------------------------------------------------------------
    leftYAxisBounds.width = 0;
    int leftCount = 0;
    double leftStart = chartPadding;

    int desiredLeftYAxisWidth = styler.getYAxisLeftWidthHint();
    if (desiredLeftYAxisWidth > 0) {
      double widthEstimation = 0;
      // Must preparePaint masters before slaves (ordering already correct: ascending index)
      for (List<Integer> groupIndices : leftVisualGroups.values()) {
        for (int i = 0; i < groupIndices.size(); i++) {
          // In colocate mode slaves have no column of their own
          if (styler.isMergedAxisColocateSlaveLabels() && i > 0) continue;
          int logIdx = groupIndices.get(i);
          Axis<ST, S> ya = yAxisMap.get(logIdx);
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
    double leftStartFirst = leftStart;

    for (Entry<Integer, List<Integer>> groupEntry : leftVisualGroups.entrySet()) {
      List<Integer> groupIndices = groupEntry.getValue();
      // Paint slaves (higher indices) outermost, master (index 0 of list) innermost.
      // Order: slaves first (left to right = outermost to innermost), then master.
      // groupIndices is sorted ascending: [masterIdx, slave1, slave2, ...]
      // We paint slaves in descending order of index (outermost first), then master.
      int masterLogIdx = groupIndices.get(0);
      Axis<ST, S> masterAxis = yAxisMap.get(masterLogIdx);
      masterAxis.clearColocatedSlaves();

      boolean colocate = styler.isMergedAxisColocateSlaveLabels() && groupIndices.size() > 1;

      if (colocate) {
        // ---- Colocate mode: slaves render inline on the master column, no separate column ----
        // Prime the master so slaves can build a synchronized calculator.
        if (masterAxis.getAxisTickCalculator() == null) {
          masterAxis.preparePaint();
        }
        for (int i = 1; i < groupIndices.size(); i++) {
          Axis<ST, S> slaveAxis = yAxisMap.get(groupIndices.get(i));
          slaveAxis.preparePaint(); // builds AxisTickCalculator_Synchronized from master
          masterAxis.addColocatedSlave(slaveAxis);
        }
        // Re-preparePaint master so its width hint picks up all slave label widths.
        masterAxis.preparePaint();
        Rectangle2D.Double bounds = (Rectangle2D.Double) masterAxis.getBounds();
        bounds.x = leftStart;
        masterAxis.paint(g);
        leftStart += paddingBetweenAxes + bounds.getWidth() + tickMargin;
        leftYAxisBounds.width += bounds.getWidth();
        leftCount++;
        leftMainYAxis = masterAxis;
      } else {
        // ---- Normal mode: each slave gets its own column ----
        // Paint slaves from outermost (last in list) to innermost
        for (int i = groupIndices.size() - 1; i >= 1; i--) {
          int slaveLogIdx = groupIndices.get(i);
          Axis<ST, S> slaveAxis = yAxisMap.get(slaveLogIdx);
          // Ensure master is ready:
          if (masterAxis.getAxisTickCalculator() == null) {
            masterAxis.preparePaint();
          }
          slaveAxis.preparePaint(); // Now uses AxisTickCalculator_Synchronized
          Rectangle2D.Double bounds = (Rectangle2D.Double) slaveAxis.getBounds();
          bounds.x = leftStart;
          slaveAxis.paint(g);
          leftStart += paddingBetweenAxes + bounds.getWidth() + tickMargin;
          leftYAxisBounds.width += bounds.getWidth();
          leftCount++;
          leftMainYAxis = slaveAxis;
        }

        // Paint master (innermost, closest to plot)
        masterAxis.preparePaint();
        Rectangle2D.Double bounds = (Rectangle2D.Double) masterAxis.getBounds();
        bounds.x = leftStart;
        masterAxis.paint(g);
        leftStart += paddingBetweenAxes + bounds.getWidth() + tickMargin;
        leftYAxisBounds.width += bounds.getWidth();
        leftCount++;
        leftMainYAxis = masterAxis;
      }

      // The gridline master is always the master axis of the first left visual group
      if (leftGridlineMasterAxis == null) {
        leftGridlineMasterAxis = masterAxis;
      }
    }

    if (leftCount > 1) {
      leftYAxisBounds.width += (leftCount - 1) * paddingBetweenAxes;
    }
    leftYAxisBounds.width += leftCount * tickMargin;

    // ------------------------------------------------------------------
    // Step 4: Paint right-side axes (mirror of left, but reversed).
    // ------------------------------------------------------------------
    rightYAxisBounds.width = 0;

    double legendWidth = 0;
    if (styler.getLegendPosition() == LegendPosition.OutsideE && styler.isLegendVisible()) {
      legendWidth = chart.getLegend().getBounds().getWidth() + styler.getChartPadding();
    }
    double rightEnd = chart.getWidth() - legendWidth - chartPadding;

    rightYAxisBounds.x = rightEnd;

    int rightCount = 0;

    // traverse visual groups in descending key order so that the lowest visual group ID
    // ends up closest to the plot area on the right side
    for (Entry<Integer, List<Integer>> groupEntry : rightVisualGroups.descendingMap().entrySet()) {
      List<Integer> groupIndices = groupEntry.getValue();
      int masterLogIdx = groupIndices.get(0); // lowest = master
      Axis<ST, S> masterAxis = yAxisMap.get(masterLogIdx);
      masterAxis.clearColocatedSlaves();

      boolean colocate = styler.isMergedAxisColocateSlaveLabels() && groupIndices.size() > 1;

      if (colocate) {
        // ---- Colocate mode ----
        if (masterAxis.getAxisTickCalculator() == null) {
          masterAxis.preparePaint();
        }
        for (int i = 1; i < groupIndices.size(); i++) {
          Axis<ST, S> slaveAxis = yAxisMap.get(groupIndices.get(i));
          slaveAxis.preparePaint();
          masterAxis.addColocatedSlave(slaveAxis);
        }
        masterAxis.preparePaint(); // re-prep so width hint accounts for slave labels
        Rectangle2D.Double bounds = (Rectangle2D.Double) masterAxis.getBounds();
        double approxWidth = bounds.getWidth();
        double xOffset = rightEnd - approxWidth;
        bounds.x = xOffset;
        rightYAxisBounds.x = xOffset;
        masterAxis.paint(g);
        rightYAxisBounds.width += approxWidth;
        rightEnd -= paddingBetweenAxes + approxWidth + tickMargin;
        rightCount++;
        rightMainYAxis = masterAxis;
      } else {
        // ---- Normal mode: each slave gets its own column ----
        // Paint slaves outermost (farthest from plot)
        for (int i = groupIndices.size() - 1; i >= 1; i--) {
          int slaveLogIdx = groupIndices.get(i);
          Axis<ST, S> slaveAxis = yAxisMap.get(slaveLogIdx);
          if (masterAxis.getAxisTickCalculator() == null) {
            masterAxis.preparePaint();
          }
          slaveAxis.preparePaint();
          Rectangle2D.Double bounds = (Rectangle2D.Double) slaveAxis.getBounds();
          double approxWidth = bounds.getWidth();
          double xOffset = rightEnd - approxWidth;
          bounds.x = xOffset;
          rightYAxisBounds.x = xOffset;
          slaveAxis.paint(g);
          rightYAxisBounds.width += approxWidth;
          rightEnd -= paddingBetweenAxes + approxWidth + tickMargin;
          rightCount++;
          rightMainYAxis = slaveAxis;
        }

        // Paint master innermost (closest to plot)
        masterAxis.preparePaint();
        Rectangle2D.Double bounds = (Rectangle2D.Double) masterAxis.getBounds();
        double approxWidth = bounds.getWidth();
        double xOffset = rightEnd - approxWidth;
        bounds.x = xOffset;
        rightYAxisBounds.x = xOffset;
        masterAxis.paint(g);
        rightYAxisBounds.width += approxWidth;
        rightEnd -= paddingBetweenAxes + approxWidth + tickMargin;
        rightCount++;
        rightMainYAxis = masterAxis;
      }

      if (rightGridlineMasterAxis == null) {
        rightGridlineMasterAxis = masterAxis;
      }
    }

    // ------------------------------------------------------------------
    // Step 5: Fallback defaults (same semantics as before for single-axis
    // charts and charts without any merging).
    // ------------------------------------------------------------------
    if (leftMainYAxis == null) {
      leftMainYAxis = yAxis;
    }
    if (rightMainYAxis == null) {
      rightMainYAxis = yAxis;
    }
    if (leftGridlineMasterAxis == null) {
      leftGridlineMasterAxis = leftMainYAxis;
    }
    if (rightGridlineMasterAxis == null) {
      rightGridlineMasterAxis = rightMainYAxis;
    }

    if (rightCount > 1) {
      rightYAxisBounds.width += (rightCount - 1) * paddingBetweenAxes;
    }
    rightYAxisBounds.width += rightCount * tickMargin;

    // fill left & right bounds
    Rectangle2D.Double bounds = (java.awt.geom.Rectangle2D.Double) yAxis.getBounds();
    leftYAxisBounds.x = leftStartFirst;
    leftYAxisBounds.y = bounds.y;
    leftYAxisBounds.height = bounds.height;

    rightYAxisBounds.y = bounds.y;
    rightYAxisBounds.height = bounds.height;

    xAxis.preparePaint();
    xAxis.paint(g);
  }

  private void prepareForPaint() {

    yAxisMap.clear();
    yAxisMap.put(0, yAxis);
    boolean mainYAxisUsed = false;
    if (chart.getSeriesMap() != null) {
      for (S series : chart.getSeriesMap().values()) {
        if (!series.isEnabled()) {
          continue;
        }
        int yIndex = series.getYAxisGroup();
        if (!mainYAxisUsed && yIndex == 0) {
          mainYAxisUsed = true;
        }
        if (yAxisMap.containsKey(yIndex)) {
          continue;
        }
        yAxisMap.put(yIndex, new Axis<ST, S>(chart, Axis.Direction.Y, yIndex));
      }
    }

    // set the axis data types, making sure all are compatible
    xAxis.setDataType(null);
    for (Axis<ST, S> ya : yAxisMap.values()) {
      ya.setDataType(null);
    }
    for (S series : chart.getSeriesMap().values()) {
      xAxis.setDataType(series.getxAxisDataType());
      if (!series.isEnabled()) {
        continue;
      }

      getYAxis(series.getYAxisGroup()).setDataType(series.getyAxisDataType());
      if (!mainYAxisUsed) {
        yAxis.setDataType(series.getyAxisDataType());
      }

      if (series.getYAxisDecimalPattern() != null) {
        chart
            .getStyler()
            .putYAxisGroupDecimalPatternMap(
                series.getYAxisGroup(), series.getYAxisDecimalPattern());
      }
    }

    // calculate axis min and max
    xAxis.resetMinMax();
    for (Axis<ST, S> ya : yAxisMap.values()) {
      ya.resetMinMax();
    }

    // if no series, we still want to plot an empty plot with axes. Since there are no min and max
    // with no series added, we just fake it arbitrarily.
    if (chart.getSeriesMap() == null || chart.getSeriesMap().size() < 1) {
      setDefaultAxisMinMax();
    } else {
      int disabledCount = 0; // maybe all are disabled, so we check this condition
      for (S series : chart.getSeriesMap().values()) {
        // add min/max to axes
        // System.out.println(series.getxMin());
        // System.out.println(series.getxMax());
        // System.out.println(series.getyMin());
        // System.out.println(series.getyMax());
        // System.out.println("****");
        if (!series.isEnabled()) {
          disabledCount++;
          continue;
        }
        xAxis.addMinMax(series.getXMin(), series.getXMax());

        getYAxis(series.getYAxisGroup()).addMinMax(series.getYMin(), series.getYMax());
        if (!mainYAxisUsed) {
          yAxis.addMinMax(series.getYMin(), series.getYMax());
        }
      }
      if (disabledCount == chart.getSeriesMap().values().size()) {
        setDefaultAxisMinMax();
      }
    }

    overrideMinMaxForXAxis();
    for (Axis<ST, S> ya : yAxisMap.values()) {
      overrideMinMaxForYAxis(ya);
    }

    // logarithmic sanity check
    if (chart.getStyler().isXAxisLogarithmic() && xAxis.getMin() <= 0.0) {
      throw new IllegalArgumentException(
          "Series data (accounting for error bars too) cannot be less or equal to zero for a logarithmic X-Axis!!!");
    }
    if (chart.getStyler().isYAxisLogarithmic()) {
      for (Axis<ST, S> ya : yAxisMap.values()) {
        if (ya.getMin() <= 0.0) {
          // System.out.println(getMin());
          throw new IllegalArgumentException(
              "Series data (accounting for error bars too) cannot be less or equal to zero for a logarithmic Y-Axis!!!");
        }
      }
    }
    // infinity checks
    if (xAxis.getMin() == Double.POSITIVE_INFINITY || xAxis.getMax() == Double.POSITIVE_INFINITY) {
      throw new IllegalArgumentException(
          "Series data (accounting for error bars too) cannot be equal to Double.POSITIVE_INFINITY!!!");
    }
    for (Axis<ST, S> ya : yAxisMap.values()) {
      if (ya.getMin() == Double.POSITIVE_INFINITY || ya.getMax() == Double.POSITIVE_INFINITY) {
        throw new IllegalArgumentException(
            "Series data (accounting for error bars too) cannot be equal to Double.POSITIVE_INFINITY!!!");
      }
      if (ya.getMin() == Double.NEGATIVE_INFINITY || ya.getMax() == Double.NEGATIVE_INFINITY) {
        throw new IllegalArgumentException(
            "Series data (accounting for error bars too) cannot be equal to Double.NEGATIVE_INFINITY!!!");
      }
    }

    if (xAxis.getMin() == Double.NEGATIVE_INFINITY || xAxis.getMax() == Double.NEGATIVE_INFINITY) {
      throw new IllegalArgumentException(
          "Series data (accounting for error bars too) cannot be equal to Double.NEGATIVE_INFINITY!!!");
    }
  }

  /**
   * Sets a default minimum and maximum on all axes, for cases where there are no series to compute
   * a range from.
   */
  private void setDefaultAxisMinMax() {
    double xMin = chart.getStyler().isXAxisLogarithmic() ? 0.1 : -1.0;
    double yMin = chart.getStyler().isYAxisLogarithmic() ? 0.1 : -1.0;
    xAxis.addMinMax(xMin, 1);
    for (Axis<ST, S> ya : yAxisMap.values()) {
      ya.addMinMax(yMin, 1);
    }
  }

  Axis<ST, S> getYAxis(int yIndex) {

    return yAxisMap.get(yIndex);
  }

  /** Here we can add special case min max calculations and take care of manual min max settings. */
  private void overrideMinMaxForXAxis() {

    double overrideXAxisMinValue = xAxis.getMin();
    double overrideXAxisMaxValue = xAxis.getMax();

    if (chart.getStyler() instanceof HorizontalBarStyler) {
      if (xAxis.getMin() > 0.0) {
        overrideXAxisMinValue = 0.0;
      }
      if (xAxis.getMax() < 0.0) {
        overrideXAxisMaxValue = 0.0;
      }
    }

    // override min and maxValue if specified
    if (chart.getStyler().getXAxisMin() != null) {

      overrideXAxisMinValue = chart.getStyler().getXAxisMin();
    }
    if (chart.getStyler().getXAxisMax() != null) {

      overrideXAxisMaxValue = chart.getStyler().getXAxisMax();
    }
    xAxis.setMin(overrideXAxisMinValue);
    xAxis.setMax(overrideXAxisMaxValue);
  }

  private void overrideMinMaxForYAxis(Axis yAxis) {

    double overrideYAxisMinValue = yAxis.getMin();
    double overrideYAxisMaxValue = yAxis.getMax();

    if (chart.getStyler() instanceof CategoryStyler) {

      CategoryStyler categoryStyler = (CategoryStyler) chart.getStyler();
      if (categoryStyler.getDefaultSeriesRenderStyle() == CategorySeriesRenderStyle.Bar
          || categoryStyler.getDefaultSeriesRenderStyle() == CategorySeriesRenderStyle.Stick) {

        // if stacked, we need to completely re-calculate min and max.
        if (categoryStyler.isStacked()) {

          AxesChartSeriesCategory axesChartSeries =
              (AxesChartSeriesCategory) chart.getSeriesMap().values().iterator().next();
          List<?> categories = (List<?>) axesChartSeries.getXData();

          int numCategories = categories.size();
          double[] accumulatedStackOffsetPos = new double[numCategories];
          double[] accumulatedStackOffsetNeg = new double[numCategories];

          for (S series : chart.getSeriesMap().values()) {

            AxesChartSeriesCategory axesChartSeriesCategory = (AxesChartSeriesCategory) series;

            if (!series.isEnabled()) {
              continue;
            }

            int categoryCounter = 0;
            Iterator<? extends Number> yItr = axesChartSeriesCategory.getYData().iterator();
            while (yItr.hasNext()) {

              Number next = yItr.next();
              // skip when a value is null
              if (next == null) {
                categoryCounter++;
                continue;
              }

              if (next.doubleValue() > 0) {
                accumulatedStackOffsetPos[categoryCounter] += next.doubleValue();
              } else if (next.doubleValue() < 0) {
                accumulatedStackOffsetNeg[categoryCounter] += next.doubleValue();
              }
              categoryCounter++;
            }
          }

          double max = accumulatedStackOffsetPos[0];
          for (int i = 1; i < accumulatedStackOffsetPos.length; i++) {
            if (accumulatedStackOffsetPos[i] > max) {
              max = accumulatedStackOffsetPos[i];
            }
          }

          double min = accumulatedStackOffsetNeg[0];
          for (int i = 1; i < accumulatedStackOffsetNeg.length; i++) {
            if (accumulatedStackOffsetNeg[i] < min) {
              min = accumulatedStackOffsetNeg[i];
            }
          }

          overrideYAxisMaxValue = max;
          overrideYAxisMinValue = min;
          // System.out.println("overrideYAxisMaxValue: " + overrideYAxisMaxValue);
          // System.out.println("overrideYAxisMinValue: " + overrideYAxisMinValue);
        }

        // override min/max value for bar charts' Y-Axis
        // There is a special case where it's desired to anchor the axis min or max to zero, like in
        // the case of bar and stick charts.
        if (yAxis.getMin() > 0.0) {
          overrideYAxisMinValue = 0.0;
        }
        if (yAxis.getMax() < 0.0) {
          overrideYAxisMaxValue = 0.0;
        }
      }
    }

    // override min and maxValue if specified

    if (!(chart.getStyler() instanceof BoxStyler)) {

      // min
      if (chart.getStyler().getYAxisMin(yAxis.getYIndex()) != null) {
        overrideYAxisMinValue = chart.getStyler().getYAxisMin(yAxis.getYIndex());
      } else if (chart.getStyler().getYAxisMin() != null) {
        overrideYAxisMinValue = chart.getStyler().getYAxisMin();
      }
      // max
      if (chart.getStyler().getYAxisMax(yAxis.getYIndex()) != null) {
        overrideYAxisMaxValue = chart.getStyler().getYAxisMax(yAxis.getYIndex());
      } else if (chart.getStyler().getYAxisMax() != null) {
        overrideYAxisMaxValue = chart.getStyler().getYAxisMax();
      }
    }

    yAxis.setMin(overrideYAxisMinValue);
    yAxis.setMax(overrideYAxisMaxValue);
  }

  // Getters & Setters /////////////////////////////////////////////////

  public Axis<ST, S> getXAxis() {

    return xAxis;
  }

  Axis<ST, S> getYAxis() {

    return yAxis;
  }

  @Override
  public Rectangle2D getBounds() {

    return null; // should never be called
  }

  Rectangle2D.Double getLeftYAxisBounds() {

    return leftYAxisBounds;
  }

  Rectangle2D.Double getRightYAxisBounds() {

    return rightYAxisBounds;
  }

  Axis<ST, S> getLeftMainYAxis() {

    return leftMainYAxis;
  }

  Axis<ST, S> getRightMainYAxis() {

    return rightMainYAxis;
  }

  /**
   * Returns the axis whose tick positions should drive horizontal gridlines and left-side inner
   * plot tick marks. When axes are merged this is the master of the lowest-index visual group on
   * the left side; otherwise it equals {@link #getLeftMainYAxis()}.
   */
  Axis<ST, S> getGridlineMasterAxis() {

    return leftGridlineMasterAxis;
  }

  /**
   * Returns the axis whose tick positions should drive right-side inner plot tick marks. When axes
   * are merged this is the master of the lowest-index visual group on the right side; otherwise it
   * equals {@link #getRightMainYAxis()}.
   */
  Axis<ST, S> getRightGridlineMasterAxis() {

    return rightGridlineMasterAxis;
  }
}
