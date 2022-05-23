package org.knowm.xchart;

import java.awt.Graphics2D;
import java.util.List;

import org.knowm.xchart.internal.Utils;
import org.knowm.xchart.internal.chartpart.AxisPair;
import org.knowm.xchart.internal.chartpart.Legend_Bubble;
import org.knowm.xchart.internal.chartpart.Plot_Bubble;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.internal.style.SeriesColorMarkerLineStyle;
import org.knowm.xchart.style.BubbleStyler;
import org.knowm.xchart.style.Styler.ChartTheme;
import org.knowm.xchart.style.theme.Theme;

/** @author timmolter */
public class BubbleChart extends AbstractChart<BubbleStyler, BubbleSeries> {

  /**
   * Constructor - the default Chart Theme will be used (XChartTheme)
   *
   * @param width
   * @param height
   */
  public BubbleChart(int width, int height) {

    super(width, height, new BubbleStyler());
    axisPair = new AxisPair<BubbleStyler, BubbleSeries>(this);
    plot = new Plot_Bubble<BubbleStyler, BubbleSeries>(this);
    legend = new Legend_Bubble<BubbleStyler, BubbleSeries>(this);
  }

  /**
   * Constructor
   *
   * @param width
   * @param height
   * @param theme - pass in a instance of Theme class, probably a custom Theme.
   */
  public BubbleChart(int width, int height, Theme theme) {

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
  public BubbleChart(int width, int height, ChartTheme chartTheme) {

    this(width, height, chartTheme.newInstance(chartTheme));
  }

  /**
   * Constructor
   *
   * @param chartBuilder
   */
  public BubbleChart(BubbleChartBuilder chartBuilder) {

    this(chartBuilder.getWidth(), chartBuilder.getHeight(), chartBuilder.getChartTheme());
    setTitle(chartBuilder.getTitle());
    setXAxisTitle(chartBuilder.xAxisTitle);
    setYAxisTitle(chartBuilder.yAxisTitle);
  }

  /**
   * Add a series for a Bubble type chart using using double arrays
   *
   * @param seriesName
   * @param xData the X-Axis data
   * @param yData the Y-Axis data
   * @param bubbleData the bubble data
   * @return A Series object that you can set properties on
   */
  public BubbleSeries addSeries(
      String seriesName,
      List<? extends Number> xData,
      List<? extends Number> yData,
      List<? extends Number> bubbleData) {

    return addSeries(
        seriesName,
        Utils.getDoubleArrayFromNumberList(xData),
        Utils.getDoubleArrayFromNumberList(yData),
        Utils.getDoubleArrayFromNumberList(bubbleData));
  }

  /**
   * Add a series for a Bubble type chart using using Lists
   *
   * @param seriesName
   * @param xData the X-Axis data
   * @param yData the Y-Axis data
   * @param bubbleData the bubble data
   * @return
   */
  public BubbleSeries addSeries(
      String seriesName, double[] xData, double[] yData, double[] bubbleData) {

    // Sanity checks
    sanityCheck(seriesName, xData, yData, bubbleData);

    BubbleSeries series = generateBubbleSeries(seriesName, xData, yData, bubbleData);

    seriesMap.put(seriesName, series);

    return series;
  }

  private BubbleSeries generateBubbleSeries(String seriesName, double[] xData, double[] yData, double[] bubbleData) {
	BubbleSeries series;
    if (xData != null) {
      series = new BubbleSeries(seriesName, xData, yData, bubbleData);
    } else { // generate xData
      series =
          new BubbleSeries(
              seriesName, Utils.getGeneratedDataAsArray(yData.length), yData, bubbleData);
    }
	return series;
  }

  /**
   * Update a series by updating the X-Axis, Y-Axis and bubble data
   *
   * @param seriesName
   * @param newXData - set null to be automatically generated as a list of increasing Integers
   *     starting from 1 and ending at the size of the new Y-Axis data list.
   * @param newYData
   * @param newBubbleData - set null if there are no error bars
   * @return
   */
  public BubbleSeries updateBubbleSeries(
      String seriesName,
      List<?> newXData,
      List<? extends Number> newYData,
      List<? extends Number> newBubbleData) {

    return updateBubbleSeries(
        seriesName,
        Utils.getDoubleArrayFromNumberList(newXData),
        Utils.getDoubleArrayFromNumberList(newYData),
        Utils.getDoubleArrayFromNumberList(newBubbleData));
  }

  /**
   * Update a series by updating the X-Axis, Y-Axis and bubble data
   *
   * @param seriesName
   * @param newXData - set null to be automatically generated as a list of increasing Integers
   *     starting from 1 and ending at the size of the new Y-Axis data list.
   * @param newYData
   * @param newBubbleData - set null if there are no error bars
   * @return
   */
  public BubbleSeries updateBubbleSeries(
      String seriesName, double[] newXData, double[] newYData, double[] newBubbleData) {

    BubbleSeries series = getSeriesMap().get(seriesName);
    checkSeriesValidity(seriesName, series);
    double[] xData = newXData;
    if (newXData == null) {
      xData = Utils.getGeneratedDataAsArray(newYData.length);
    }
    series.replaceData(xData, newYData, newBubbleData);
    return series;
  }

  private void checkSeriesValidity(String seriesName, BubbleSeries series) {
	if (series == null) {
      throw new IllegalArgumentException("Series name >" + seriesName + "< not found!!!");
    }
  }

  ///////////////////////////////////////////////////
  // Internal Members and Methods ///////////////////
  ///////////////////////////////////////////////////

  private void sanityCheck(String seriesName, double[] xData, double[] yData, double[] bubbleData) {

    seriesNameDuplicateCheck(seriesName);
    sanityCheckYData(yData);
    if (bubbleData == null) {
      throw new IllegalArgumentException("Bubble data cannot be null!!! >" + seriesName);
    }
    if (bubbleData.length == 0) {
      throw new IllegalArgumentException("Bubble data cannot be empty!!! >" + seriesName);
    }
    if (xData != null) {
      if (xData.length == 0) {
    	  throw new IllegalArgumentException("X-Axis data cannot be empty!!! >" + seriesName);
      }
      if (xData.length != yData.length) {
          throw new IllegalArgumentException("X and Y-Axis sizes are not the same!!!");
      }
    }
    if (bubbleData.length != yData.length) {
      throw new IllegalArgumentException(
          "Bubble Data and Y-Axis sizes are not the same!!! >" + seriesName);
    }
  }

  @Override
  public void paint(Graphics2D graphics, int width, int height) {

    settingPaint(width, height);

    doPaint(graphics);
  }

  @Override
  protected void specificSetting() {
    for (BubbleSeries bubbleSeries : getSeriesMap().values()) {
        final boolean isBubbleSeriesRenderStyleSet = (bubbleSeries.getBubbleSeriesRenderStyle() == null);// would be directly set
        if (isBubbleSeriesRenderStyleSet) { // wasn't overridden, use default from Style Manager
          bubbleSeries.setBubbleSeriesRenderStyle(getStyler().getDefaultSeriesRenderStyle());
        }
      }
  }

  /** set the series color based on theme */
  // set the series types if they are not set. Legend and Plot need it.
  @Override
  protected void setSeriesDefaultForNullPart(Series series, SeriesColorMarkerLineStyle seriesColorMarkerLineStyle) {
	  BubbleSeries bubbleSeries = (BubbleSeries) series;
	  if (bubbleSeries.getLineStyle() == null) { // wasn't set manually
		  bubbleSeries.setLineStyle(seriesColorMarkerLineStyle.getStroke());
	  }
	  if (bubbleSeries.getLineColor() == null) { // wasn't set manually
		  bubbleSeries.setLineColor(seriesColorMarkerLineStyle.getColor());
	  }
	  if (bubbleSeries.getFillColor() == null) { // wasn't set manually
		  bubbleSeries.setFillColor(seriesColorMarkerLineStyle.getColor());
	  }
  }

  private void sanityCheckYData(double[] yData) {
	 if (yData == null) {
	   throw new IllegalArgumentException("Y-Axis data connot be null !!!");
	 }
	 if (yData.length == 0) {
	   throw new IllegalArgumentException("Y-Axis data connot be empyt !!!");
	 }
  }
}
