package org.knowm.xchart.internal.chartpart;

import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.knowm.xchart.internal.series.AxesChartSeriesCategory;
import org.knowm.xchart.internal.series.Series.DataType;
import org.knowm.xchart.style.AxesChartStyler;
import org.knowm.xchart.style.BoxStyler;
import org.knowm.xchart.style.CategoryStyler;
import org.knowm.xchart.style.HeatMapStyler;
import org.knowm.xchart.style.HorizontalBarStyler;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.knowm.xchart.style.XYStyler;

/** X-Axis. */
public class Axis_X<ST extends AxesChartStyler, S extends AxesChartSeries> extends Axis_<ST, S> {

  Axis_X(Chart<ST, S> chart) {

    super(chart, 0);
    axisTitle = new AxisTitle<>(chart, Axis_.Direction.X, null, 0);
    axisTick = new AxisTick<>(chart, Axis_.Direction.X, null);
  }

  @Override
  public void preparePaint() {

    double legendHeightOffset = 0;
    if (axesChartStyler.isLegendVisible()
        && axesChartStyler.getLegendPosition() == LegendPosition.OutsideS)
      legendHeightOffset = chart.getLegend().getBounds().getHeight();

    // calculate paint zone
    // |____________________|

    Rectangle2D leftYAxisBounds = chart.getAxisPair().getLeftYAxisBounds();
    Rectangle2D rightYAxisBounds = chart.getAxisPair().getRightYAxisBounds();

    double maxYAxisY =
        Math.max(
            leftYAxisBounds.getY() + leftYAxisBounds.getHeight(),
            rightYAxisBounds.getY() + rightYAxisBounds.getHeight());
    double xOffset = leftYAxisBounds.getWidth() + leftYAxisBounds.getX();
    double yOffset = maxYAxisY + axesChartStyler.getPlotMargin() - legendHeightOffset;

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

    // the Y-Axis was already drawn at this point so we know how much vertical room is left for
    // the X-Axis
    double height =
        chart.getHeight()
            - maxYAxisY
            - axesChartStyler.getChartPadding()
            - axesChartStyler.getPlotMargin();

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

    // now paint the X-Axis given the above paint zone
    this.axisTickCalculator = getAxisTickCalculatorForX(bounds.getWidth());
    axisTitle.paint(g);
    axisTick.paint(g);

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
  double getXAxisHeightHint(double workingSpace) {

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

    this.axisTickCalculator = getAxisTickCalculatorForX(workingSpace);

    // Axis tick labels
    double axisTickLabelsHeight = 0.0;
    if (axesChartStyler.isXAxisTicksVisible()) {

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

  private AxisTickCalculator_ getAxisTickCalculatorForX(double workingSpace) {
    List<Double> xData = new ArrayList<>();
    if (axesChartStyler instanceof HorizontalBarStyler) {
      Set<Double> uniqueXData = new LinkedHashSet<>();
      for (HorizontalBarSeries categorySeries :
          ((HorizontalBarChart) chart).getSeriesMap().values()) {
        List<Double> numericCategoryXData =
            categorySeries.getXData().stream()
                .filter(Objects::nonNull)
                .mapToDouble(Number::doubleValue)
                .boxed()
                .collect(Collectors.toList());
        uniqueXData.addAll(numericCategoryXData);
      }
      xData.addAll(uniqueXData);
    } else if (axesChartStyler instanceof HeatMapStyler) {
      List<?> categories = ((HeatMapChart) chart).getHeatMapSeries().getXData();
      xData =
          categories.stream()
              .filter(Objects::nonNull)
              .filter(it -> it instanceof Number)
              .mapToDouble(it -> ((Number) it).doubleValue())
              .boxed()
              .collect(Collectors.toList());
    } else if (axesChartStyler instanceof CategoryStyler) {
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
    } else if (axesChartStyler instanceof XYStyler) {
      Set<Double> uniqueXData = new LinkedHashSet<>();
      for (XYSeries xySeries : ((XYChart) chart).getSeriesMap().values()) {
        uniqueXData.addAll(Arrays.stream(xySeries.getXData()).boxed().collect(Collectors.toList()));
      }
      xData.addAll(uniqueXData);
    }

    if (axesChartStyler.getxAxisTickLabelsFormattingFunction() != null) {
      if (!xData.isEmpty()) { // TODO why would this be empty?
        return new AxisTickCalculator_Callback(
            axesChartStyler.getxAxisTickLabelsFormattingFunction(),
            Axis_.Direction.X,
            workingSpace,
            min,
            max,
            xData,
            axesChartStyler);
      }
      return new AxisTickCalculator_Callback(
          axesChartStyler.getxAxisTickLabelsFormattingFunction(),
          Axis_.Direction.X,
          workingSpace,
          min,
          max,
          axesChartStyler);

    } else if (axesChartStyler instanceof HorizontalBarStyler) {
      return new AxisTickCalculator_Number(
          Axis_.Direction.X, workingSpace, min, max, xData, axesChartStyler);
    } else if (axesChartStyler instanceof CategoryStyler || axesChartStyler instanceof BoxStyler) {

      if (chart.getSeriesMap().isEmpty()) {
        return new AxisTickCalculator_Category(
            Axis_.Direction.X, workingSpace, List.of(), DataType.String, axesChartStyler);
      }
      // TODO Cleanup? More elegant way?
      AxesChartSeriesCategory axesChartSeries =
          (AxesChartSeriesCategory) chart.getSeriesMap().values().iterator().next();
      List<?> categories = (List<?>) axesChartSeries.getXData();
      DataType axisType = chart.getAxisPair().getXAxis().getDataType();

      return new AxisTickCalculator_Category(
          Axis_.Direction.X, workingSpace, categories, axisType, axesChartStyler);

    } else if (getDataType() == DataType.Date && !(axesChartStyler instanceof HeatMapStyler)) {

      return new AxisTickCalculator_Date(Axis_.Direction.X, workingSpace, min, max, axesChartStyler);

    } else if (axesChartStyler.isXAxisLogarithmic()) {

      return new AxisTickCalculator_Logarithmic(
          Axis_.Direction.X, workingSpace, min, max, axesChartStyler);

    } else if (axesChartStyler instanceof HeatMapStyler) {

      List<?> categories = ((HeatMapChart) chart).getHeatMapSeries().getXData();
      DataType axisType = chart.getAxisPair().getXAxis().getDataType();

      return new AxisTickCalculator_Category(
          Axis_.Direction.X, workingSpace, categories, axisType, axesChartStyler);
    } else {
      if (!xData.isEmpty()) {
        return new AxisTickCalculator_Number(
            Axis_.Direction.X, workingSpace, min, max, xData, axesChartStyler);
      }
      return new AxisTickCalculator_Number(Axis_.Direction.X, workingSpace, min, max, axesChartStyler);
    }
  }

  @Override
  public double getScreenValue(double chartPoint) {

    double minVal = min;
    double maxVal = max;

    // min & max is not set in category charts with string labels
    if (min > max) {
      if (axesChartStyler instanceof CategoryStyler && !chart.getSeriesMap().isEmpty()) {
        AxesChartSeriesCategory axesChartSeries =
            (AxesChartSeriesCategory) chart.getSeriesMap().values().iterator().next();
        int count = axesChartSeries.getXData().size();
        minVal = 0;
        maxVal = count;
      }
    }

    double startOffset = bounds.getX();
    double workingSpace = bounds.getWidth();
    boolean isLog = axesChartStyler.isXAxisLogarithmic();

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
    return startOffset + pos;
  }

  @Override
  public double getChartValue(double screenPoint) {

    // a check if all axis data are the exact same values
    if (min == max) {
      return min;
    }

    double minVal = min;
    double maxVal = max;

    // min & max is not set in category charts with string labels
    if (min > max) {
      if (axesChartStyler instanceof CategoryStyler && !chart.getSeriesMap().isEmpty()) {
        AxesChartSeriesCategory axesChartSeries =
            (AxesChartSeriesCategory) chart.getSeriesMap().values().iterator().next();
        int count = axesChartSeries.getXData().size();
        minVal = 0;
        maxVal = count;
      }
    }

    double startOffset = bounds.getX();
    double workingSpace = bounds.getWidth();
    boolean isLog = axesChartStyler.isXAxisLogarithmic();

    double tickSpace = axesChartStyler.getPlotContentSize() * workingSpace;
    if (tickSpace < axesChartStyler.getXAxisTickMarkSpacingHint()) {
      return minVal;
    }

    double margin =
        org.knowm.xchart.internal.Utils.getTickStartOffset(workingSpace, tickSpace);

    minVal = isLog ? Math.log10(minVal) : minVal;
    maxVal = isLog ? Math.log10(maxVal) : maxVal;
    double value = ((screenPoint - margin - startOffset) * (maxVal - minVal) / tickSpace) + minVal;
    value = isLog ? Math.pow(10, value) : value;
    return value;
  }
}
