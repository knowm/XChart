package org.knowm.xchart.internal.chartpart;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.knowm.xchart.*;
import org.knowm.xchart.internal.Utils;
import org.knowm.xchart.internal.series.AxesChartSeries;
import org.knowm.xchart.internal.series.AxesChartSeriesCategory;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.internal.series.Series.DataType;
import org.knowm.xchart.style.*;
import org.knowm.xchart.style.Styler.InfoPanelPosition;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.knowm.xchart.style.Styler.YAxisPosition;

/** Axis */
public class Axis<ST extends AxesChartStyler, S extends AxesChartSeries> implements ChartPart {

  private final Chart<ST, S> chart;
  private final Rectangle2D.Double bounds;

  private final ST axesChartStyler;
  /** the axis title */
  private final AxisTitle<ST, S> axisTitle;
  /** the axis tick */
  private final AxisTick<ST, S> axisTick;
  /** the axis direction */
  private final Direction direction;
  /** the axis group index * */
  private final int index;
  /** the dataType */
  private Series.DataType dataType;
  /** the axis tick calculator */
  private AxisTickCalculator_ axisTickCalculator;

  private double min;
  private double max;

  private Function<Double, String> customFormattingFunction;

  /**
   * Constructor
   *
   * @param chart the Chart
   * @param direction the axis direction (X or Y)
   * @param index the y-axis index (not relevant for x-axes)
   */
  public Axis(Chart<ST, S> chart, Direction direction, int index) {

    this.chart = chart;
    this.axesChartStyler = chart.getStyler();

    this.direction = direction;
    this.index = index;
    bounds = new Rectangle2D.Double();
    axisTitle =
        new AxisTitle<ST, S>(chart, direction, direction == Direction.Y ? this : null, index);
    axisTick = new AxisTick<ST, S>(chart, direction, direction == Direction.Y ? this : null);
  }

  /** Reset the default min and max values in preparation for calculating the actual min and max */
  void resetMinMax() {

    min = Double.MAX_VALUE;
    max = -1 * Double.MAX_VALUE;
  }

  /**
   * @param min
   * @param max
   */
  void addMinMax(double min, double max) {

    // System.out.println(min);
    // System.out.println(max);
    // NaN indicates String axis data, so min and max play no role
    if (Double.isNaN(this.min) || min < this.min) {
      this.min = min;
    }
    if (Double.isNaN(this.max) || max > this.max) {
      this.max = max;
    }

    // System.out.println(this.min);
    // System.out.println(this.max);
  }

  public void preparePaint() {

    double legendHeightOffset = 0;
    if (axesChartStyler.isLegendVisible()
        && axesChartStyler.getLegendPosition() == LegendPosition.OutsideS)
      legendHeightOffset = chart.getLegend().getBounds().getHeight();

    double infoPanelHeightOffset = 0;
    if (axesChartStyler.isInfoPanelVisible()
        && axesChartStyler.getInfoPanelPosition() == InfoPanelPosition.OutsideS)
      infoPanelHeightOffset =
          axesChartStyler.getInfoPanelPadding() / 2 + chart.getInfoPanel().getBounds().getHeight();

    // determine Axis bounds
    if (direction == Direction.Y) { // Y-Axis - gets called first

      // calculate paint zone
      // ----
      // |
      // |
      // |
      // |
      // ----
      // double xOffset = chart.getAxisPair().getYAxisXOffset();
      double xOffset = 0; // this will be updated on AxisPair.paint() method
      // double yOffset = chart.getChartTitle().getBounds().getHeight() < .1 ?
      // axesChartStyler.getChartPadding() : chart.getChartTitle().getBounds().getHeight()
      // + axesChartStyler.getChartPadding();
      double yOffset =
          chart.getChartTitle().getBounds().getHeight() + axesChartStyler.getChartPadding();

      /////////////////////////
      int i = 1; // just twice through is all it takes
      double width = 60; // arbitrary, final width depends on Axis tick labels
      double height;
      do {
        // System.out.println("width before: " + width);

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
                - legendHeightOffset
                - infoPanelHeightOffset;

        width = getYAxisWidthHint(height);
        //         System.out.println("width after: " + width);

        // System.out.println("height: " + height);

      } while (i-- > 0);

      /////////////////////////

      // bounds = new Rectangle2D.Double(xOffset, yOffset, width, height);
      bounds.setRect(xOffset, yOffset, width, height);

    } else { // X-Axis

      // calculate paint zone
      // |____________________|

      Rectangle2D leftYAxisBounds = chart.getAxisPair().getLeftYAxisBounds();
      Rectangle2D rightYAxisBounds = chart.getAxisPair().getRightYAxisBounds();

      double maxYAxisY =
          Math.max(
              leftYAxisBounds.getY() + leftYAxisBounds.getHeight(),
              rightYAxisBounds.getY() + rightYAxisBounds.getHeight());
      double xOffset = leftYAxisBounds.getWidth() + leftYAxisBounds.getX();
      double yOffset =
          maxYAxisY + axesChartStyler.getPlotMargin() - legendHeightOffset - infoPanelHeightOffset;

      double legendWidth = 0;
      if (axesChartStyler.getLegendPosition() == LegendPosition.OutsideE
          && axesChartStyler.isLegendVisible()) {
        legendWidth = chart.getLegend().getBounds().getWidth() + axesChartStyler.getChartPadding();
      }
      double width =
          chart.getWidth()
              - leftYAxisBounds.getWidth() // y-axis was already painted
              - rightYAxisBounds.getWidth() // y-axis was already painted
              - leftYAxisBounds.getX() // use left y-axis x instead of padding
              - 1 * axesChartStyler.getChartPadding() // right y-axis padding

              // - tickMargin is included in left & right y axis bounds

              - legendWidth;

      // double height = this.getXAxisHeightHint(width);
      // System.out.println("height: " + height);
      // the Y-Axis was already draw at this point so we know how much vertical room is left for the
      // X-Axis
      double height =
          chart.getHeight()
              - maxYAxisY
              - axesChartStyler.getChartPadding()
              - axesChartStyler.getPlotMargin();
      // System.out.println("height2: " + height2);

      bounds.setRect(xOffset, yOffset, width, height);
    }
  }

  @Override
  public void paint(Graphics2D g) {

    Object oldHint = g.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    // determine Axis bounds
    if (direction == Direction.Y) { // Y-Axis - gets called first

      /////////////////////////

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
      double width =
          (axesChartStyler.isYAxisTitleVisible() ? axisTitle.getBounds().getWidth() : 0)
              + axisTick.getBounds().getWidth();

      bounds.width = width;
      // g.setColor(Color.yellow);
      // g.draw(bounds);

    } else { // X-Axis

      // calculate paint zone
      // |____________________|

      // g.setColor(Color.yellow);
      // g.draw(bounds);

      // now paint the X-Axis given the above paint zone
      this.axisTickCalculator = getAxisTickCalculator(bounds.getWidth());
      axisTitle.paint(g);
      axisTick.paint(g);
    }

    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldHint);
  }

  /**
   * The vertical Y-Axis is drawn first, but to know the lower bounds of it, we need to know how
   * high the X-Axis paint zone is going to be. Since the tick labels could be rotated, we need to
   * actually determine the tick labels first to get an idea of how tall the X-Axis tick labels will
   * be.
   *
   * @return the x-axis height hint
   */
  private double getXAxisHeightHint(double workingSpace) {

    // Axis title
    double titleHeight = 0.0;
    if (chart.getXAxisTitle() != null
        && !chart.getXAxisTitle().trim().equalsIgnoreCase("")
        && axesChartStyler.isXAxisTitleVisible()) {
      TextLayout textLayout =
          new TextLayout(
              chart.getXAxisTitle(),
              axesChartStyler.getAxisTitleFont(),
              new FontRenderContext(null, true, false));
      Rectangle2D rectangle = textLayout.getBounds();
      titleHeight = rectangle.getHeight() + axesChartStyler.getAxisTitlePadding();
    }

    this.axisTickCalculator = getAxisTickCalculator(workingSpace);

    // Axis tick labels
    double axisTickLabelsHeight = 0.0;
    if (axesChartStyler.isXAxisTicksVisible()) {

      // get some real tick labels
      // System.out.println("XAxisHeightHint");
      // System.out.println("workingSpace: " + workingSpace);

      String sampleLabel = "";
      // find the longest String in all the labels
      for (int i = 0; i < axisTickCalculator.getTickLabels().size(); i++) {
        // System.out.println("label: " + axisTickCalculator.getTickLabels().get(i));
        if (axisTickCalculator.getTickLabels().get(i) != null
            && axisTickCalculator.getTickLabels().get(i).length() > sampleLabel.length()) {
          sampleLabel = axisTickCalculator.getTickLabels().get(i);
        }
      }
      // System.out.println("sampleLabel: " + sampleLabel);

      // get the height of the label including rotation
      TextLayout textLayout =
          new TextLayout(
              sampleLabel.length() == 0 ? " " : sampleLabel,
              axesChartStyler.getAxisTickLabelsFont(),
              new FontRenderContext(null, true, false));
      AffineTransform rot =
          axesChartStyler.getXAxisLabelRotation() == 0
              ? null
              : AffineTransform.getRotateInstance(
                  -1 * Math.toRadians(axesChartStyler.getXAxisLabelRotation()));
      Shape shape = textLayout.getOutline(rot);
      Rectangle2D rectangle = shape.getBounds();

      axisTickLabelsHeight =
          rectangle.getHeight()
              + axesChartStyler.getAxisTickPadding()
              + axesChartStyler.getAxisTickMarkLength();
    }
    return titleHeight + axisTickLabelsHeight;
  }

  private double getYAxisWidthHint(double workingSpace) {

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

    this.axisTickCalculator = getAxisTickCalculator(workingSpace);

    // Axis tick labels
    double axisTickLabelsHeight = 0.0;
    if (axesChartStyler.isYAxisTicksVisible()) {

      // get some real tick labels
      // System.out.println("XAxisHeightHint");
      // System.out.println("workingSpace: " + workingSpace);

      String sampleLabel = "";
      // find the longest String in all the labels
      for (int i = 0; i < axisTickCalculator.getTickLabels().size(); i++) {
        if (axisTickCalculator.getTickLabels().get(i) != null
            && axisTickCalculator.getTickLabels().get(i).length() > sampleLabel.length()) {
          sampleLabel = axisTickCalculator.getTickLabels().get(i);
        }
      }

      // get the height of the label including rotation
      TextLayout textLayout =
          new TextLayout(
              sampleLabel.length() == 0 ? " " : sampleLabel,
              axesChartStyler.getAxisTickLabelsFont(),
              new FontRenderContext(null, true, false));
      Rectangle2D rectangle = textLayout.getBounds();

      axisTickLabelsHeight =
          rectangle.getWidth()
              + axesChartStyler.getAxisTickPadding()
              + axesChartStyler.getAxisTickMarkLength();
    }
    return titleHeight + axisTickLabelsHeight;
  }

  private AxisTickCalculator_ getAxisTickCalculator(double workingSpace) {

    // check if a label override map for the y axis is present
    Map<Object, Object> customTickLabelsMap =
        chart.getAxisPair().getCustomTickLabelsMap(getDirection(), index);
    if (customTickLabelsMap != null) {

      if (getDirection() == Direction.X && axesChartStyler instanceof CategoryStyler) {

        // get the first series
        AxesChartSeriesCategory axesChartSeries =
            (AxesChartSeriesCategory) chart.getSeriesMap().values().iterator().next();
        // get the first categories, could be Number Date or String
        List<?> categories = (List<?>) axesChartSeries.getXData();

        // add the custom tick labels for the categories
        Map<Double, Object> axisTickValueLabelMap = new LinkedHashMap<>();
        for (Entry<Object, Object> entry : customTickLabelsMap.entrySet()) {
          int index = categories.indexOf(entry.getKey());
          if (index == -1) {
            throw new IllegalArgumentException(
                "Could not find category index for " + entry.getKey());
          }
          axisTickValueLabelMap.put((double) index, entry.getValue());
        }

        return new AxisTickCalculator_Override(
            getDirection(),
            workingSpace,
            axesChartStyler,
            axisTickValueLabelMap,
            chart.getAxisPair().getXAxis().getDataType(),
            categories.size());
      } else {

        // add the custom tick labels for the values
        Map<Double, Object> axisTickValueLabelMap = new LinkedHashMap<>();
        for (Entry<Object, Object> entry : customTickLabelsMap.entrySet()) {
          Number axisTickValue = (Number) entry.getKey();
          axisTickValueLabelMap.put(axisTickValue.doubleValue(), entry.getValue());
        }

        return new AxisTickCalculator_Override(
            getDirection(), workingSpace, min, max, axesChartStyler, axisTickValueLabelMap);
      }
    }

    // X-Axis
    if (getDirection() == Direction.X) {
      List<Double> xData = new ArrayList<>();
      if (axesChartStyler instanceof HeatMapStyler) {
        List<?> categories = (List<?>) ((HeatMapChart) chart).getHeatMapSeries().getXData();
        xData =
            categories.stream()
                .filter(Objects::nonNull)
                .filter(it -> it instanceof Number)
                .mapToDouble(it -> ((Number) it).doubleValue())
                .boxed()
                .collect(Collectors.toList());
      }
      if (axesChartStyler instanceof CategoryStyler) {
        Set<Double> uniqueXData = new LinkedHashSet<>();
        for (CategorySeries categorySeries : ((CategoryChart) chart).getSeriesMap().values()) {
          List<Double> numericCategoryXData =
              categorySeries.getXData().stream()
                  .filter(Objects::nonNull)
                  .filter(x -> x instanceof Number)
                  .mapToDouble(x -> ((Number) x).doubleValue())
                  .boxed()
                  .collect(Collectors.toList());
          uniqueXData.addAll(numericCategoryXData);
        }
        xData.addAll(uniqueXData);
      }
      if (axesChartStyler instanceof XYStyler) {
        Set<Double> uniqueXData = new LinkedHashSet<>();
        for (XYSeries xySeries : ((XYChart) chart).getSeriesMap().values()) {
          uniqueXData.addAll(
              Arrays.stream(xySeries.getXData()).boxed().collect(Collectors.toList()));
        }
        xData.addAll(uniqueXData);
      }

      if (customFormattingFunction != null) {
        if (!xData.isEmpty()) {
          return new AxisTickCalculator_Callback(
              customFormattingFunction, getDirection(), workingSpace, min, max, xData, axesChartStyler);
        }
        return new AxisTickCalculator_Callback(
            customFormattingFunction, getDirection(), workingSpace, min, max, axesChartStyler);
      }

      if (axesChartStyler instanceof CategoryStyler || axesChartStyler instanceof BoxStyler) {

        // TODO Cleanup? More elegant way?
        AxesChartSeriesCategory axesChartSeries =
            (AxesChartSeriesCategory) chart.getSeriesMap().values().iterator().next();
        List<?> categories = (List<?>) axesChartSeries.getXData();
        DataType axisType = chart.getAxisPair().getXAxis().getDataType();

        return new AxisTickCalculator_Category(
            getDirection(), workingSpace, categories, axisType, axesChartStyler);

      } else if (getDataType() == Series.DataType.Date
          && !(axesChartStyler instanceof HeatMapStyler)) {

        return new AxisTickCalculator_Date(getDirection(), workingSpace, min, max, axesChartStyler);

      } else if (axesChartStyler.isXAxisLogarithmic()) {

        return new AxisTickCalculator_Logarithmic(
            getDirection(), workingSpace, min, max, axesChartStyler);

      } else if (axesChartStyler instanceof HeatMapStyler) {

        List<?> categories = (List<?>) ((HeatMapChart) chart).getHeatMapSeries().getXData();
        DataType axisType = chart.getAxisPair().getXAxis().getDataType();

        return new AxisTickCalculator_Category(
            getDirection(), workingSpace, categories, axisType, axesChartStyler);
      } else {
        if (!xData.isEmpty()) {
          return new AxisTickCalculator_Number(
              getDirection(), workingSpace, min, max, xData, axesChartStyler);
        }
        return new AxisTickCalculator_Number(
            getDirection(), workingSpace, min, max, axesChartStyler);
      }
    }

    // Y-Axis
    else {

      List<Double> yData = new ArrayList<>();
      if (axesChartStyler instanceof HeatMapStyler) {
        List<?> categories = (List<?>) ((HeatMapChart) chart).getHeatMapSeries().getYData();
        yData =
            categories.stream()
                .filter(Objects::nonNull)
                .filter(it -> it instanceof Number)
                .mapToDouble(it -> ((Number) it).doubleValue())
                .boxed()
                .collect(Collectors.toList());
      }
      if (axesChartStyler instanceof CategoryStyler) {
        Set<Double> uniqueYData = new LinkedHashSet<>();
        for (CategorySeries categorySeries : ((CategoryChart) chart).getSeriesMap().values()) {
          uniqueYData.addAll(
              categorySeries.getYData().stream()
                  .filter(Objects::nonNull)
                  .mapToDouble(Number::doubleValue)
                  .boxed()
                  .collect(Collectors.toList()));
        }
        yData.addAll(uniqueYData);
      }
      if (axesChartStyler instanceof XYStyler) {
        Set<Double> uniqueYData = new LinkedHashSet<>();
        for (XYSeries xySeries : ((XYChart) chart).getSeriesMap().values()) {
          uniqueYData.addAll(
              Arrays.stream(xySeries.getYData()).boxed().collect(Collectors.toList()));
        }
        yData.addAll(uniqueYData);
      }

      if (customFormattingFunction != null) {
        if (!yData.isEmpty()) {
          return new AxisTickCalculator_Callback(
              customFormattingFunction, getDirection(), workingSpace, min, max, yData, axesChartStyler);
        }
        return new AxisTickCalculator_Callback(
            customFormattingFunction, getDirection(), workingSpace, min, max, axesChartStyler);
      }

      if (axesChartStyler.isYAxisLogarithmic() && getDataType() != Series.DataType.Date) {

        return new AxisTickCalculator_Logarithmic(
            getDirection(), workingSpace, min, max, axesChartStyler, getYIndex());
      } else if (axesChartStyler instanceof HeatMapStyler) {

        List<?> categories = (List<?>) ((HeatMapChart) chart).getHeatMapSeries().getYData();
        DataType axisType = chart.getAxisPair().getYAxis().getDataType();

        return new AxisTickCalculator_Category(
            getDirection(), workingSpace, categories, axisType, axesChartStyler);
      } else {
        if (!yData.isEmpty()) {
          return new AxisTickCalculator_Number(
              getDirection(), workingSpace, min, max, yData, axesChartStyler);
        }
        return new AxisTickCalculator_Number(
            getDirection(), workingSpace, min, max, axesChartStyler, getYIndex());
      }
    }
  }

  Series.DataType getDataType() {

    return dataType;
  }

  // Getters /////////////////////////////////////////////////

  public void setDataType(Series.DataType dataType) {

    if (dataType != null && this.dataType != null && this.dataType != dataType) {
      throw new IllegalArgumentException(
          "Different Axes (e.g. Date, Number, String) cannot be mixed on the same chart!!");
    }
    this.dataType = dataType;
  }

  double getMin() {

    return min;
  }

  void setMin(double min) {

    this.min = min;
  }

  double getMax() {

    return max;
  }

  void setMax(double max) {

    this.max = max;
  }

  AxisTick<ST, S> getAxisTick() {

    return axisTick;
  }

  private Direction getDirection() {

    return direction;
  }

  AxisTitle<ST, S> getAxisTitle() {

    return axisTitle;
  }

  public AxisTickCalculator_ getAxisTickCalculator() {

    return this.axisTickCalculator;
  }

  @Override
  public Rectangle2D getBounds() {

    return bounds;
  }

  public int getYIndex() {

    return index;
  }

  /**
   * Converts a chart coordinate value to screen coordinate. Same as AxisTickCalculators
   * calculation.
   *
   * @param chartPoint value in chart coordinate system
   * @return Coordinate of screen. eg: MouseEvent.getX(), MouseEvent.getY()
   */
  public double getScreenValue(double chartPoint) {

    double minVal = min;
    double maxVal = max;

    // min & max is not set in category charts with string labels
    if (min > max) {
      if (getDirection() == Direction.X) {
        if (axesChartStyler instanceof CategoryStyler) {
          AxesChartSeriesCategory axesChartSeries =
              (AxesChartSeriesCategory) chart.getSeriesMap().values().iterator().next();
          int count = axesChartSeries.getXData().size();
          minVal = 0;
          maxVal = count;
        }
      }
    }

    double workingSpace;
    double startOffset;
    boolean isLog;
    if (direction == Direction.X) {
      startOffset = bounds.getX();
      workingSpace = bounds.getWidth();
      isLog = axesChartStyler.isXAxisLogarithmic();
    } else {
      startOffset = 0; // bounds.getY();
      workingSpace = bounds.getHeight();
      isLog = axesChartStyler.isYAxisLogarithmic();
    }

    // a check if all axis data are the exact same values
    if (min == max) {
      return workingSpace / 2;
    }

    // tick space - a percentage of the working space available for ticks
    double tickSpace = axesChartStyler.getPlotContentSize() * workingSpace; // in plot space

    // this prevents an infinite loop when the plot gets sized really small.
    if (tickSpace < axesChartStyler.getXAxisTickMarkSpacingHint()) {
      return workingSpace / 2;
    }

    // where the tick should begin in the working space in pixels
    double margin = Utils.getTickStartOffset(workingSpace, tickSpace);

    minVal = isLog ? Math.log10(minVal) : minVal;
    maxVal = isLog ? Math.log10(maxVal) : maxVal;
    chartPoint = isLog ? Math.log10(chartPoint) : chartPoint;
    double tickLabelPosition =
        startOffset + margin + ((chartPoint - minVal) / (maxVal - minVal) * tickSpace);

    if (direction == Direction.Y) {
      tickLabelPosition = bounds.getHeight() - tickLabelPosition + bounds.getY();
    }
    return tickLabelPosition;
  }

  /**
   * Converts a screen coordinate to chart coordinate value. Reverses the AxisTickCalculators
   * calculation.
   *
   * @param screenPoint Coordinate of screen. eg: MouseEvent.getX(), MouseEvent.getY()
   * @return value in chart coordinate system
   */
  public double getChartValue(double screenPoint) {

    // a check if all axis data are the exact same values
    if (min == max) {
      return min;
    }

    double minVal = min;
    double maxVal = max;

    // min & max is not set in category charts with string labels
    if (min > max) {
      if (getDirection() == Direction.X) {
        if (axesChartStyler instanceof CategoryStyler) {
          AxesChartSeriesCategory axesChartSeries =
              (AxesChartSeriesCategory) chart.getSeriesMap().values().iterator().next();
          int count = axesChartSeries.getXData().size();
          minVal = 0;
          maxVal = count;
        }
      }
    }

    double workingSpace;
    double startOffset;
    boolean isLog;
    if (direction == Direction.X) {
      startOffset = bounds.getX();
      workingSpace = bounds.getWidth();
      isLog = axesChartStyler.isXAxisLogarithmic();
    } else {
      startOffset = 0; // bounds.getY();
      workingSpace = bounds.getHeight();
      screenPoint = bounds.getHeight() - screenPoint + bounds.getY(); // y increments top to bottom
      isLog = axesChartStyler.isYAxisLogarithmic();
    }

    // tick space - a percentage of the working space available for ticks
    double tickSpace = axesChartStyler.getPlotContentSize() * workingSpace; // in plot space

    // this prevents an infinite loop when the plot gets sized really small.
    if (tickSpace < axesChartStyler.getXAxisTickMarkSpacingHint()) {
      return minVal;
    }

    // where the tick should begin in the working space in pixels
    double margin = Utils.getTickStartOffset(workingSpace, tickSpace);

    // given tickLabelPositon (screenPoint) find value
    // double tickLabelPosition =
    //       margin + ((value - min) / (max - min) * tickSpace);

    minVal = isLog ? Math.log10(minVal) : minVal;
    maxVal = isLog ? Math.log10(maxVal) : maxVal;
    double value = ((screenPoint - margin - startOffset) * (maxVal - minVal) / tickSpace) + minVal;
    value = isLog ? Math.pow(10, value) : value;
    return value;
  }

  public void setCustomFormattingFunction(Function<Double, String> customFormattingFunction) {
    this.customFormattingFunction = customFormattingFunction;
  }

  /** An axis direction */
  public enum Direction {

    /** the constant to represent X axis */
    X,

    /** the constant to represent Y axis */
    Y
  }
}
