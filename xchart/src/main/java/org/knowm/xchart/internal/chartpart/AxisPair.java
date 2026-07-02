package org.knowm.xchart.internal.chartpart;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.TreeMap;

import org.knowm.xchart.CategorySeries.CategorySeriesRenderStyle;
import org.knowm.xchart.internal.series.AxesChartSeries;
import org.knowm.xchart.internal.series.AxesChartSeriesCategory;
import org.knowm.xchart.style.AxesChartStyler;
import org.knowm.xchart.style.BoxStyler;
import org.knowm.xchart.style.CategoryStyler;
import org.knowm.xchart.style.HorizontalBarStyler;
import org.knowm.xchart.style.Styler.LegendPosition;

public class AxisPair<ST extends AxesChartStyler, S extends AxesChartSeries> implements ChartPart {

  // Fraction of the axis range reserved as headroom when data labels are drawn outside the bars, so
  // they are not clipped at the plot edge.
  private static final double OUTSIDE_LABELS_AXIS_PADDING = 0.05;

  private final AxesChart<ST, S> chart;

  private final Axis_X<ST, S> xAxis;
  private final Axis_Y<ST, S> yAxis;
  private final TreeMap<Integer, Axis_Y<ST, S>> yAxisMap;
  private final Rectangle2D.Double leftYAxisBounds;
  private final Rectangle2D.Double rightYAxisBounds;
  private Axis_Y<ST, S> leftMainYAxis;
  private Axis_Y<ST, S> rightMainYAxis;

  /**
   * The axis whose tick positions are used for horizontal gridlines and inner plot tick marks on
   * the left side. Equals {@code leftMainYAxis} when no axes are merged, otherwise equals the
   * master axis of the first left-side visual group.
   */
  private Axis_Y<ST, S> leftGridlineMasterAxis;

  /** Same as {@link #leftGridlineMasterAxis} for the right side. */
  private Axis_Y<ST, S> rightGridlineMasterAxis;

  /**
   * Constructor
   *
   * @param chart
   */
  public AxisPair(AxesChart<ST, S> chart) {

    this.chart = chart;

    // add axes
    xAxis = new Axis_X<>(chart);
    yAxis = new Axis_Y<>(chart, 0);
    yAxisMap = new TreeMap<>();
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

    // Steps 1–4: wire relationships, build visual groups, paint both sides
    YAxisGroupPainter<ST, S> groupPainter = new YAxisGroupPainter<>(chart, yAxisMap);
    groupPainter.wireRelationships();

    // Left side
    groupPainter.paintLeft(g, chartPadding, leftYAxisBounds, styler.getYAxisLeftWidthHint());

    // Right side
    double legendWidth = 0;
    if (styler.getLegendPosition() == LegendPosition.OutsideE && styler.isLegendVisible()) {
      legendWidth = chart.getLegend().getBounds().getWidth() + styler.getChartPadding();
    }
    double rightEnd = chart.getWidth() - legendWidth - chartPadding;
    rightYAxisBounds.x = rightEnd;
    groupPainter.paintRight(g, rightEnd, chartPadding, rightYAxisBounds);

    // Step 5: fallback defaults (unchanged semantics for single-axis / non-merged charts)
    leftMainYAxis =
        groupPainter.getLeftMainYAxis() != null ? groupPainter.getLeftMainYAxis() : yAxis;
    rightMainYAxis =
        groupPainter.getRightMainYAxis() != null ? groupPainter.getRightMainYAxis() : yAxis;
    leftGridlineMasterAxis =
        groupPainter.getLeftGridlineMasterAxis() != null
            ? groupPainter.getLeftGridlineMasterAxis()
            : leftMainYAxis;
    rightGridlineMasterAxis =
        groupPainter.getRightGridlineMasterAxis() != null
            ? groupPainter.getRightGridlineMasterAxis()
            : rightMainYAxis;

    // Fill left & right bounds
    Rectangle2D.Double bounds = (Rectangle2D.Double) yAxis.getBounds();
    leftYAxisBounds.x = groupPainter.getLeftStartUsed();
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
        yAxisMap.put(yIndex, new Axis_Y<>(chart, yIndex));
      }
    }

    // set the axis data types, making sure all are compatible
    xAxis.setDataType(null);
    for (Axis_Y<ST, S> ya : yAxisMap.values()) {
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
    for (Axis_Y<ST, S> ya : yAxisMap.values()) {
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
    for (Axis_Y<ST, S> ya : yAxisMap.values()) {
      overrideMinMaxForYAxis(ya);
    }

    // logarithmic sanity check
    if (chart.getStyler().isXAxisLogarithmic() && xAxis.getMin() <= 0.0) {
      throw new IllegalArgumentException(
          "Series data (accounting for error bars too) cannot be less or equal to zero for a logarithmic X-Axis!!!");
    }
    if (chart.getStyler().isYAxisLogarithmic()) {
      for (Axis_Y<ST, S> ya : yAxisMap.values()) {
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
    for (Axis_Y<ST, S> ya : yAxisMap.values()) {
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
    for (Axis_Y<ST, S> ya : yAxisMap.values()) {
      ya.addMinMax(yMin, 1);
    }
  }

  Axis_Y<ST, S> getYAxis(int yIndex) {

    return yAxisMap.get(yIndex);
  }

  /** Here we can add special case min max calculations and take care of manual min max settings. */
  private void overrideMinMaxForXAxis() {

    double overrideXAxisMinValue = xAxis.getMin();
    double overrideXAxisMaxValue = xAxis.getMax();

    if (chart.getStyler() instanceof HorizontalBarStyler) {
      HorizontalBarStyler horizontalBarStyler = (HorizontalBarStyler) chart.getStyler();
      if (xAxis.getMin() > 0.0) {
        overrideXAxisMinValue = 0.0;
      }
      if (xAxis.getMax() < 0.0) {
        overrideXAxisMaxValue = 0.0;
      }

      // When labels are drawn outside the bars, reserve axis headroom so they are not clipped at
      // the plot edge.
      if (horizontalBarStyler.isLabelsVisible() && horizontalBarStyler.getLabelsPosition() > 1) {
        double extra =
            (overrideXAxisMaxValue - overrideXAxisMinValue) * OUTSIDE_LABELS_AXIS_PADDING;
        if (overrideXAxisMaxValue > 0.0) {
          overrideXAxisMaxValue += extra;
        }
        if (overrideXAxisMinValue < 0.0) {
          overrideXAxisMinValue -= extra;
        }
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

  private void overrideMinMaxForYAxis(Axis_Y<ST, S> yAxis) {

    double overrideYAxisMinValue = yAxis.getMin();
    double overrideYAxisMaxValue = yAxis.getMax();

    if (chart.getStyler() instanceof CategoryStyler) {

      CategoryStyler categoryStyler = (CategoryStyler) chart.getStyler();

      // If stacked, recalculate min and max from the per-category stack sums. This is independent of
      // the render style (bar, stick, area, ...) since stacking sums the series values the same way,
      // and it also covers charts where individual series override the default render style.
      if (categoryStyler.isStacked() && !chart.getSeriesMap().isEmpty()) {

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
          double[] yArr = axesChartSeriesCategory.getYData();
          for (double next : yArr) {

            // skip when a value is NaN (was null in the original list)
            if (Double.isNaN(next)) {
              categoryCounter++;
              continue;
            }

            if (next > 0) {
              accumulatedStackOffsetPos[categoryCounter] += next;
            } else if (next < 0) {
              accumulatedStackOffsetNeg[categoryCounter] += next;
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
      }

      if (categoryStyler.getDefaultSeriesRenderStyle() == CategorySeriesRenderStyle.Bar
          || categoryStyler.getDefaultSeriesRenderStyle() == CategorySeriesRenderStyle.Stick
          || categoryStyler.getDefaultSeriesRenderStyle() == CategorySeriesRenderStyle.Area) {

        // override min/max value for bar charts' Y-Axis
        // There is a special case where it's desired to anchor the axis min or max to zero, like in
        // the case of bar and stick charts.
        if (yAxis.getMin() > 0.0) {
          overrideYAxisMinValue = 0.0;
        }
        if (yAxis.getMax() < 0.0) {
          overrideYAxisMaxValue = 0.0;
        }

        // When labels are drawn outside the bars, reserve axis headroom so they are not clipped at
        // the plot edge.
        if (categoryStyler.isLabelsVisible() && categoryStyler.getLabelsPosition() > 1) {
          double extra =
              (overrideYAxisMaxValue - overrideYAxisMinValue) * OUTSIDE_LABELS_AXIS_PADDING;
          if (overrideYAxisMaxValue > 0.0) {
            overrideYAxisMaxValue += extra;
          }
          if (overrideYAxisMinValue < 0.0) {
            overrideYAxisMinValue -= extra;
          }
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

  public Axis_X<ST, S> getXAxis() {

    return xAxis;
  }

  Axis_Y<ST, S> getYAxis() {

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

  Axis_Y<ST, S> getLeftMainYAxis() {

    return leftMainYAxis;
  }

  Axis_Y<ST, S> getRightMainYAxis() {

    return rightMainYAxis;
  }

  /**
   * Returns the axis whose tick positions should drive horizontal gridlines and left-side inner
   * plot tick marks. When axes are merged this is the master of the lowest-index visual group on
   * the left side; otherwise it equals {@link #getLeftMainYAxis()}.
   */
  Axis_Y<ST, S> getLeftGridlineMasterAxis() {

    return leftGridlineMasterAxis;
  }

  /**
   * Returns the axis whose tick positions should drive right-side inner plot tick marks. When axes
   * are merged this is the master of the lowest-index visual group on the right side; otherwise it
   * equals {@link #getRightMainYAxis()}.
   */
  Axis_Y<ST, S> getRightGridlineMasterAxis() {

    return rightGridlineMasterAxis;
  }
}
