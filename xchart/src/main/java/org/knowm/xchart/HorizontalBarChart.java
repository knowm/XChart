package org.knowm.xchart;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.knowm.xchart.internal.Utils;
import org.knowm.xchart.internal.chartpart.AxisPair;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.chartpart.Legend_HorizontalBar;
import org.knowm.xchart.internal.chartpart.Plot_HorizontalBar;
import org.knowm.xchart.internal.style.SeriesColorMarkerLineStyle;
import org.knowm.xchart.internal.style.SeriesColorMarkerLineStyleCycler;
import org.knowm.xchart.style.HorizontalBarStyler;
import org.knowm.xchart.style.Styler.ChartTheme;
import org.knowm.xchart.style.theme.Theme;

public class HorizontalBarChart extends Chart<HorizontalBarStyler, HorizontalBarSeries> {

  /**
   * Constructor - the default Chart Theme will be used (XChartTheme)
   *
   * @param width
   * @param height
   */
  public HorizontalBarChart(int width, int height) {

    super(width, height, new HorizontalBarStyler());
    axisPair = new AxisPair<HorizontalBarStyler, HorizontalBarSeries>(this);
    plot = new Plot_HorizontalBar<HorizontalBarStyler, HorizontalBarSeries>(this);
    legend = new Legend_HorizontalBar<HorizontalBarStyler, HorizontalBarSeries>(this);
  }

  /**
   * Constructor
   *
   * @param width
   * @param height
   * @param theme - pass in a instance of Theme class, probably a custom Theme.
   */
  public HorizontalBarChart(int width, int height, Theme theme) {

    this(width, height);
    styler.setTheme(theme);
  }

  /**
   * Constructor
   *
   * @param width
   * @param height
   * @param chartTheme - pass in the desired ChartTheme enum
   */
  public HorizontalBarChart(int width, int height, ChartTheme chartTheme) {

    this(width, height, chartTheme.newInstance(chartTheme));
  }

  /**
   * Constructor
   *
   * @param chartBuilder
   */
  public HorizontalBarChart(HorizontalBarChartBuilder chartBuilder) {

    this(chartBuilder.width, chartBuilder.height, chartBuilder.chartTheme);
    setTitle(chartBuilder.title);
    setXAxisTitle(chartBuilder.xAxisTitle);
    setYAxisTitle(chartBuilder.yAxisTitle);
  }

  /**
   * Add a series for a horizontal bar type chart using double arrays with error bars
   *
   * @param seriesName
   * @param xData the X-Axis data
   * @param yData the Y-Axis data
   * @return A Series object that you can set properties on
   */
  public HorizontalBarSeries addSeries(String seriesName, double[] xData, double[] yData) {

    return addSeries(
        seriesName,
        Utils.getNumberListFromDoubleArray(xData),
        Utils.getNumberListFromDoubleArray(yData));
  }

  /**
   * Add a series for a horizontal bar type chart using int arrays with error bars
   *
   * @param seriesName
   * @param xData the X-Axis data
   * @param yData the Y-Axis data
   * @return A Series object that you can set properties on
   */
  public HorizontalBarSeries addSeries(String seriesName, int[] xData, int[] yData) {

    return addSeries(
        seriesName, Utils.getNumberListFromIntArray(xData), Utils.getNumberListFromIntArray(yData));
  }

  /**
   * Add a series for a horizontal bar type chart using Lists
   *
   * @param seriesName
   * @param xData the X-Axis data
   * @param yData the Y-Axis data
   * @return A Series object that you can set properties on
   */
  public HorizontalBarSeries addSeries(
      String seriesName, List<? extends Number> xData, List<?> yData) {

    // Sanity checks
    sanityCheck(seriesName, xData, yData);

    HorizontalBarSeries series;
    if (xData != null) {

      // Sanity check
      if (xData.size() != yData.size()) {
        throw new IllegalArgumentException("X and Y-Axis sizes are not the same!!!");
      }

    } else { // generate xData
      xData = Utils.getGeneratedDataAsList(yData.size());
    }
    series = new HorizontalBarSeries(seriesName, xData, yData);

    seriesMap.put(seriesName, series);

    return series;
  }

  /**
   * Update a series by updating the X-Axis and Y-Axis
   *
   * @param seriesName
   * @param newXData
   * @param newYData - set null to be automatically generated as a list of increasing Integers
   *     starting from 1 and ending at the size of the new X-Axis data list.
   * @return
   */
  public HorizontalBarSeries updateCategorySeries(
      String seriesName, List<? extends Number> newXData, List<?> newYData) {

    Map<String, HorizontalBarSeries> seriesMap = getSeriesMap();
    HorizontalBarSeries series = seriesMap.get(seriesName);
    if (series == null) {
      throw new IllegalArgumentException("Series name >" + seriesName + "< not found!!!");
    }
    if (newYData == null) {
      // generate Y-Data
      List<Integer> generatedYData = new ArrayList<Integer>();
      for (int i = 1; i <= newXData.size(); i++) {
        generatedYData.add(i);
      }
      series.replaceData(newXData, generatedYData);
    } else {
      series.replaceData(newXData, newYData);
    }

    return series;
  }

  /**
   * Update a series by updating the X-Axis, Y-Axis and error bar data
   *
   * @param seriesName
   * @param newXData
   * @param newYData - set null to be automatically generated as a list of increasing Integers
   *     starting from 1 and ending at the size of the new X-Axis data list.
   * @return
   */
  public HorizontalBarSeries updateCategorySeries(
      String seriesName, double[] newXData, double[] newYData) {

    return updateCategorySeries(
        seriesName,
        Utils.getNumberListFromDoubleArray(newXData),
        Utils.getNumberListFromDoubleArray(newYData));
  }

  ///////////////////////////////////////////////////
  // Internal Members and Methods ///////////////////
  ///////////////////////////////////////////////////

  private void sanityCheck(String seriesName, List<? extends Number> xData, List<?> yData) {

    if (seriesMap.containsKey(seriesName)) {
      throw new IllegalArgumentException(
          "Series name >"
              + seriesName
              + "< has already been used. Use unique names for each series!!!");
    }
    if (yData == null) {
      throw new IllegalArgumentException("Y-Axis data cannot be null!!!");
    }
    if (yData.size() == 0) {
      throw new IllegalArgumentException("Y-Axis data cannot be empty!!!");
    }
    if (xData != null && xData.size() == 0) {
      throw new IllegalArgumentException("X-Axis data cannot be empty!!!");
    }
  }

  @Override
  public void paint(Graphics2D g, int width, int height) {

    setWidth(width);
    setHeight(height);

    setSeriesStyles();

    paintBackground(g);

    axisPair.paint(g);
    plot.paint(g);
    chartTitle.paint(g);
    legend.paint(g);
    annotations.forEach(x -> x.paint(g));
  }

  /** set the series color, marker and line style based on theme */
  private void setSeriesStyles() {

    SeriesColorMarkerLineStyleCycler seriesColorMarkerLineStyleCycler =
        new SeriesColorMarkerLineStyleCycler(
            getStyler().getSeriesColors(),
            getStyler().getSeriesMarkers(),
            getStyler().getSeriesLines());
    for (HorizontalBarSeries series : getSeriesMap().values()) {

      SeriesColorMarkerLineStyle seriesColorMarkerLineStyle =
          seriesColorMarkerLineStyleCycler.getNextSeriesColorMarkerLineStyle();

      if (series.getLineStyle() == null) { // wasn't set manually
        series.setLineStyle(seriesColorMarkerLineStyle.getStroke());
      }
      if (series.getLineColor() == null) { // wasn't set manually
        series.setLineColor(seriesColorMarkerLineStyle.getColor());
      }
      if (series.getFillColor() == null) { // wasn't set manually
        series.setFillColor(seriesColorMarkerLineStyle.getColor());
      }
    }
  }
}
