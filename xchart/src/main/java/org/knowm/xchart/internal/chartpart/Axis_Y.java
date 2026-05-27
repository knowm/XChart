package org.knowm.xchart.internal.chartpart;

import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategorySeries;
import org.knowm.xchart.HeatMapChart;
import org.knowm.xchart.HorizontalBarChart;
import org.knowm.xchart.HorizontalBarSeries;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.internal.series.AxesChartSeries;
import org.knowm.xchart.internal.series.Series.DataType;
import org.knowm.xchart.style.AxesChartStyler;
import org.knowm.xchart.style.CategoryStyler;
import org.knowm.xchart.style.HeatMapStyler;
import org.knowm.xchart.style.HorizontalBarStyler;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.knowm.xchart.style.Styler.YAxisPosition;
import org.knowm.xchart.style.XYStyler;

/** Y-Axis. */
public class Axis_Y<ST extends AxesChartStyler, S extends AxesChartSeries> extends Axis_<ST, S> {

  // Merged-axis support ///////////////////////////////////////////

  /**
   * When non-null this axis is a slave in a merged visual group. The master's tick pixel positions
   * are borrowed; only label values differ.
   */
  private Axis_Y<?, ?> masterAxis = null;

  /**
   * When {@code false} the axis line is suppressed because another axis in the same visual group
   * already drew it. Defaults to {@code true}.
   */
  private boolean axisLineOwner = true;

  /**
   * When colocate-slave mode is active, the master axis holds references to all slave axes whose
   * labels will be rendered stacked below the master's labels on the same column.
   */
  private final List<Axis_Y<?, ?>> colocatedSlaves = new ArrayList<>();

  Axis_Y(Chart<ST, S> chart, int index) {

    super(chart, index);
    axisTitle = new AxisTitle<>(chart, Axis_.Direction.Y, this, index);
    axisTick = new AxisTick<>(chart, Axis_.Direction.Y, this);
  }

  @Override
  public void preparePaint() {

    double legendHeightOffset = 0;
    if (axesChartStyler.isLegendVisible()
        && axesChartStyler.getLegendPosition() == LegendPosition.OutsideS)
      legendHeightOffset = chart.getLegend().getBounds().getHeight();

    // calculate paint zone
    // ----
    // |
    // |
    // |
    // |
    // ----
    double xOffset = 0; // this will be updated on AxisPair.paint() method
    double yOffset =
        chart.getChartTitle().getBounds().getHeight() + axesChartStyler.getChartPadding();

    /////////////////////////
    int i = 1; // just twice through is all it takes
    double width = 60; // arbitrary, final width depends on Axis tick labels
    double height;
    do {
      double legendWidthOffset = 0;
      if (axesChartStyler.isLegendVisible()
          && axesChartStyler.getLegendPosition() == LegendPosition.OutsideE)
        legendWidthOffset = axesChartStyler.getChartPadding();

      double approximateXAxisWidth =
          chart.getWidth()
              - width // y-axis approx. width
              - (axesChartStyler.getLegendPosition() == LegendPosition.OutsideE
                  ? chart.getLegend().getBounds().getWidth()
                  : 0)
              - 2 * axesChartStyler.getChartPadding()
              - (axesChartStyler.isYAxisTicksVisible() ? (axesChartStyler.getPlotMargin()) : 0)
              - legendWidthOffset;

      height =
          chart.getHeight()
              - yOffset
              - chart.getXAxis().getXAxisHeightHint(approximateXAxisWidth)
              - axesChartStyler.getPlotMargin()
              - axesChartStyler.getChartPadding()
              - legendHeightOffset;

      width = getYAxisWidthHint(height);

    } while (i-- > 0);

    /////////////////////////

    bounds.setRect(xOffset, yOffset, width, height);
  }

  @Override
  public void paint(java.awt.Graphics2D g) {

    Object oldHint = g.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
    g.setRenderingHint(
        RenderingHints.KEY_ANTIALIASING,
        chart.getStyler().getAntiAlias()
            ? RenderingHints.VALUE_ANTIALIAS_ON
            : RenderingHints.VALUE_ANTIALIAS_OFF);

    // fill in Axis with sub-components
    boolean onRight = axesChartStyler.getYAxisGroupPosistion(index) == YAxisPosition.Right;
    if (onRight) {
      axisTick.paint(g);
      axisTitle.paint(g);
    } else {
      axisTitle.paint(g);
      axisTick.paint(g);
    }

    // now we know the real bounds width after ticks and title are painted
    bounds.width =
        (axesChartStyler.isYAxisTitleVisible() ? axisTitle.getBounds().getWidth() : 0)
            + axisTick.getBounds().getWidth();

    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldHint);
  }

  double getYAxisWidthHint(double workingSpace) {

    // Axis title
    double titleHeight = 0.0;
    String yAxisTitle = chart.getYAxisGroupTitle(index);
    if (yAxisTitle != null
        && !yAxisTitle.trim().equalsIgnoreCase("")
        && axesChartStyler.isYAxisTitleVisible()) {
      TextLayout textLayout =
          new TextLayout(
              yAxisTitle,
              axesChartStyler.getAxisTitleFont(),
              new FontRenderContext(null, true, false));
      Rectangle2D rectangle = textLayout.getBounds();
      titleHeight = rectangle.getHeight() + axesChartStyler.getAxisTitlePadding();
    }

    this.axisTickCalculator = getAxisTickCalculatorForY(workingSpace);

    // Axis tick labels
    double axisTickLabelsHeight = 0.0;
    if (axesChartStyler.isYAxisTicksVisible()) {

      // find the widest label using the actual renderer (TeX or plain text)
      double maxLabelWidth = 0;
      for (int i = 0; i < axisTickCalculator.getTickLabels().size(); i++) {
        String lbl = axisTickCalculator.getTickLabels().get(i);
        if (lbl == null || lbl.isEmpty()) {
          continue;
        }
        double w;
        if (TexRenderer.isTeX(lbl)) {
          w = TexRenderer.getBounds(lbl, axesChartStyler.getAxisTickLabelsFont()).getWidth();
        } else {
          w =
              new TextLayout(
                      lbl,
                      axesChartStyler.getAxisTickLabelsFont(),
                      new FontRenderContext(null, true, false))
                  .getBounds()
                  .getWidth();
        }
        if (w > maxLabelWidth) {
          maxLabelWidth = w;
        }
      }

      axisTickLabelsHeight =
          maxLabelWidth
              + axesChartStyler.getAxisTickPadding()
              + axesChartStyler.getAxisTickMarkLength();

      // When colocate mode is on, factor in the widest slave label too.
      if (axesChartStyler.isMergedAxisColocateSlaveLabels() && !colocatedSlaves.isEmpty()) {
        for (Axis_Y<?, ?> slave : colocatedSlaves) {
          AxisTickCalculator slaveCalc =
              new AxisTickCalculator_Synchronized(
                  workingSpace,
                  slave.min,
                  slave.max,
                  axisTickCalculator.getTickLocations(),
                  axesChartStyler,
                  slave.index);
          double slaveMaxWidth = 0;
          for (int i = 0; i < slaveCalc.getTickLabels().size(); i++) {
            String lbl = slaveCalc.getTickLabels().get(i);
            if (lbl == null || lbl.isEmpty()) {
              continue;
            }
            double w;
            if (TexRenderer.isTeX(lbl)) {
              w = TexRenderer.getBounds(lbl, axesChartStyler.getAxisTickLabelsFont()).getWidth();
            } else {
              w =
                  new TextLayout(
                          lbl,
                          axesChartStyler.getAxisTickLabelsFont(),
                          new FontRenderContext(null, true, false))
                      .getBounds()
                      .getWidth();
            }
            if (w > slaveMaxWidth) {
              slaveMaxWidth = w;
            }
          }
          if (slaveMaxWidth > 0) {
            double slaveWidth =
                slaveMaxWidth
                    + axesChartStyler.getAxisTickPadding()
                    + axesChartStyler.getAxisTickMarkLength();
            if (slaveWidth > axisTickLabelsHeight) {
              axisTickLabelsHeight = slaveWidth;
            }
          }
        }
      }
    }
    return titleHeight + axisTickLabelsHeight;
  }

  @SuppressWarnings("unchecked")
  private AxisTickCalculator getAxisTickCalculatorForY(double workingSpace) {

    // Slave axis in a merged visual group: borrow master's pixel positions and translate labels.
    if (masterAxis != null && masterAxis.getAxisTickCalculator() != null) {
      return new AxisTickCalculator_Synchronized(
          workingSpace,
          min,
          max,
          masterAxis.getAxisTickCalculator().getTickLocations(),
          axesChartStyler,
          index);
    }

    List<Double> yData = new ArrayList<>();
    if (axesChartStyler instanceof HorizontalBarStyler) {
      Set<Double> uniqueYData = new LinkedHashSet<>();
      for (HorizontalBarSeries categorySeries :
          (Collection<HorizontalBarSeries>) (Collection<?>) chart.getSeriesMap().values()) {
        uniqueYData.addAll(
            categorySeries.getYData().stream()
                .filter(Objects::nonNull)
                .filter(it -> it instanceof Number)
                .mapToDouble(it -> ((Number) it).doubleValue())
                .boxed()
                .collect(Collectors.toList()));
      }
      yData.addAll(uniqueYData);
      Collections.reverse(yData);
    } else if (axesChartStyler instanceof HeatMapStyler) {
      List<?> categories = ((HeatMapChart) chart).getHeatMapSeries().getYData();
      yData =
          categories.stream()
              .filter(Objects::nonNull)
              .filter(it -> it instanceof Number)
              .mapToDouble(it -> ((Number) it).doubleValue())
              .boxed()
              .collect(Collectors.toList());
    } else if (axesChartStyler instanceof CategoryStyler) {
      Set<Double> uniqueYData = new LinkedHashSet<>();
      for (CategorySeries categorySeries :
          (Collection<CategorySeries>) (Collection<?>) chart.getSeriesMap().values()) {
        uniqueYData.addAll(
            categorySeries.getYData().stream()
                .filter(Objects::nonNull)
                .mapToDouble(Number::doubleValue)
                .boxed()
                .collect(Collectors.toList()));
      }
      yData.addAll(uniqueYData);
    } else if (axesChartStyler instanceof XYStyler) {
      Set<Double> uniqueYData = new LinkedHashSet<>();
      for (XYSeries xySeries :
          (Collection<XYSeries>) (Collection<?>) chart.getSeriesMap().values()) {
        uniqueYData.addAll(Arrays.stream(xySeries.getYData()).boxed().collect(Collectors.toList()));
      }
      yData.addAll(uniqueYData);
    }

    if (axesChartStyler.getyAxisTickLabelsFormattingFunction() != null) {
      if (!yData.isEmpty()) {
        return new AxisTickCalculator_Callback(
            axesChartStyler.getyAxisTickLabelsFormattingFunction(),
            Axis_.Direction.Y,
            workingSpace,
            min,
            max,
            yData,
            axesChartStyler);
      }
      return new AxisTickCalculator_Callback(
          axesChartStyler.getyAxisTickLabelsFormattingFunction(),
          Axis_.Direction.Y,
          workingSpace,
          min,
          max,
          axesChartStyler);

    } else if (axesChartStyler.isYAxisLogarithmic() && getDataType() != DataType.Date) {

      return new AxisTickCalculator_Logarithmic(
          Axis_.Direction.Y, workingSpace, min, max, axesChartStyler, getYIndex());
    } else if (axesChartStyler instanceof HorizontalBarStyler) {
      List<?> categories =
          ((Collection<HorizontalBarSeries>) (Collection<?>) chart.getSeriesMap().values())
              .stream()
                  .flatMap(it -> it.getYData().stream())
                  .distinct()
                  .collect(Collectors.toCollection(ArrayList::new));
      Collections.reverse(categories);
      DataType axisType = chart.getAxisPair().getYAxis().getDataType();

      return new AxisTickCalculator_Category(
          Axis_.Direction.Y, workingSpace, categories, axisType, axesChartStyler);
    } else if (axesChartStyler instanceof HeatMapStyler) {

      List<?> categories = ((HeatMapChart) chart).getHeatMapSeries().getYData();
      DataType axisType = chart.getAxisPair().getYAxis().getDataType();

      return new AxisTickCalculator_Category(
          Axis_.Direction.Y, workingSpace, categories, axisType, axesChartStyler);
    } else {
      if (!yData.isEmpty()) {
        return new AxisTickCalculator_Number(
            Axis_.Direction.Y, workingSpace, min, max, yData, axesChartStyler);
      }
      return new AxisTickCalculator_Number(
          Axis_.Direction.Y, workingSpace, min, max, axesChartStyler, getYIndex());
    }
  }

  /** Sets the master axis for this slave. Pass {@code null} to reset to independent mode. */
  public void setMasterAxis(Axis_Y<?, ?> masterAxis) {
    this.masterAxis = masterAxis;
  }

  public Axis_Y<?, ?> getMasterAxis() {

    return masterAxis;
  }

  /**
   * Controls whether this axis draws the vertical axis line. Set to {@code false} for slave axes
   * that share the master's line.
   */
  public void setAxisLineOwner(boolean axisLineOwner) {
    this.axisLineOwner = axisLineOwner;
  }

  public boolean isAxisLineOwner() {

    return axisLineOwner;
  }

  /** Clears the list of slave axes whose labels will be colocated on this (master) axis. */
  void clearColocatedSlaves() {
    colocatedSlaves.clear();
  }

  /** Adds a slave axis whose labels will be rendered stacked below this master's labels. */
  void addColocatedSlave(Axis_Y<?, ?> slave) {
    colocatedSlaves.add(slave);
  }

  /** Returns the (possibly empty) list of colocated slave axes registered on this master. */
  @Override
  public List<Axis_Y<?, ?>> getColocatedSlaves() {

    return colocatedSlaves;
  }

  @Override
  public double getScreenValue(double chartPoint) {

    double startOffset = 0; // bounds.getY();
    double workingSpace = bounds.getHeight();
    boolean isLog = axesChartStyler.isYAxisLogarithmic();

    double minVal = min;
    double maxVal = max;

    // a check if all axis data are the exact same values
    if (min == max) {
      return workingSpace / 2;
    }

    minVal = isLog ? Math.log10(minVal) : minVal;
    maxVal = isLog ? Math.log10(maxVal) : maxVal;
    chartPoint = isLog ? Math.log10(chartPoint) : chartPoint;

    double pos = computeTickPosition(workingSpace, minVal, maxVal, chartPoint);
    if (pos < 0) {
      return workingSpace / 2;
    }
    double tickLabelPosition = startOffset + pos;
    tickLabelPosition = bounds.getHeight() - tickLabelPosition + bounds.getY();
    return tickLabelPosition;
  }

  @Override
  public double getChartValue(double screenPoint) {

    // a check if all axis data are the exact same values
    if (min == max) {
      return min;
    }

    double minVal = min;
    double maxVal = max;
    double workingSpace = bounds.getHeight();
    boolean isLog = axesChartStyler.isYAxisLogarithmic();

    // y increments top to bottom
    screenPoint = bounds.getHeight() - screenPoint + bounds.getY();

    double tickSpace = axesChartStyler.getPlotContentSize() * workingSpace;
    if (tickSpace < axesChartStyler.getXAxisTickMarkSpacingHint()) {
      return minVal;
    }

    double margin =
        org.knowm.xchart.internal.Utils.getTickStartOffset(workingSpace, tickSpace);

    minVal = isLog ? Math.log10(minVal) : minVal;
    maxVal = isLog ? Math.log10(maxVal) : maxVal;
    double value = ((screenPoint - margin) * (maxVal - minVal) / tickSpace) + minVal;
    value = isLog ? Math.pow(10, value) : value;
    return value;
  }
}
