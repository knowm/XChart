package org.knowm.xchart;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.knowm.xchart.internal.chartpart.AxisPair;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.chartpart.Legend_HeatMap;
import org.knowm.xchart.internal.chartpart.Plot_HeatMap;
import org.knowm.xchart.style.HeatMapStyler;
import org.knowm.xchart.style.Styler.ChartTheme;
import org.knowm.xchart.style.theme.Theme;

/** @author Mr14huashao */
public class HeatMapChart extends Chart<HeatMapStyler, HeatMapSeries> {

  private HeatMapSeries heatMapSeries;

  /**
   * Constructor - the default Chart Theme will be used (XChartTheme)
   *
   * @param width
   * @param height
   */
  public HeatMapChart(int width, int height) {

    super(width, height, new HeatMapStyler());

    axisPair = new AxisPair<HeatMapStyler, HeatMapSeries>(this);
    plot = new Plot_HeatMap<HeatMapStyler, HeatMapSeries>(this);
    legend = new Legend_HeatMap<HeatMapStyler, HeatMapSeries>(this);
  }

  /**
   * Constructor
   *
   * @param width
   * @param height
   * @param theme - pass in a instance of Theme class, probably a custom Theme.
   */
  public HeatMapChart(int width, int height, Theme theme) {

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
  public HeatMapChart(int width, int height, ChartTheme chartTheme) {

    this(width, height, chartTheme.newInstance(chartTheme));
  }

  /**
   * Constructor
   *
   * @param heatMapChartBuilder
   */
  public HeatMapChart(HeatMapChartBuilder heatMapChartBuilder) {

    this(heatMapChartBuilder.getWidth(), heatMapChartBuilder.getHeight(), heatMapChartBuilder.getChartTheme());
    setTitle(heatMapChartBuilder.getTitle());
    setXAxisTitle(heatMapChartBuilder.xAxisTitle);
    setYAxisTitle(heatMapChartBuilder.yAxisTitle);
  }

  /**
   * Add a series for a HeatMap type chart using using int arrays
   *
   * @param seriesName
   * @param xData the X-Axis data
   * @param yData the Y-Axis data
   * @param heatData the heat data
   * @return
   */
  public HeatMapSeries addSeries(String seriesName, int[] xData, int[] yData, int[][] heatData) {

    return addSeries(seriesName, arrayToList(xData), arrayToList(yData), arrayToList(heatData));
  }

  /**
   * Add a series for a HeatMap type chart using using List<?>
   *
   * @param seriesName
   * @param xData the X-Axis data
   * @param yData Y-Axis data
   * @param heatData the heat data
   * @return
   */
  public HeatMapSeries addSeries(
      String seriesName, List<?> xData, List<?> yData, List<Number[]> heatData) {

    if (heatMapSeries != null) {
      throw new RuntimeException("HeatMapSeries can only be added once!!!");
    }
    sanityCheck(xData, yData, heatData);
    heatMapSeries = new HeatMapSeries(seriesName, xData, yData, heatData);
    seriesMap.put(seriesName, heatMapSeries);
    return heatMapSeries;
  }

  /**
   * Update a series by updating the X-Axis, Y-Axis and heat data
   *
   * @param seriesName
   * @param xData
   * @param yData
   * @param heatData heat data value, {{1,5,3,7,...},{8,4,5,8,...},{1,9,12,15,...},...}
   * @return
   */
  public HeatMapSeries updateSeries(String seriesName, int[] xData, int[] yData, int[][] heatData) {

    return updateSeries(seriesName, arrayToList(xData), arrayToList(yData), arrayToList(heatData));
  }

  /**
   * Update a series by updating the X-Axis, Y-Axis and heat data
   *
   * @param seriesName
   * @param xData
   * @param yData
   * @param heatData heat data, {[0,0,1],[0,1,3],[0,2,2],[0,3,18],[1,0,26],[1,1,6],[1,2,7],...}
   * @return
   */
  public HeatMapSeries updateSeries(
      String seriesName, List<?> xData, List<?> yData, List<Number[]> heatData) {

    Map<String, HeatMapSeries> seriesMap = getSeriesMap();
    HeatMapSeries series = seriesMap.get(seriesName);
    if (series == null) {
      throw new IllegalArgumentException("Series name >" + seriesName + "< not found!!!");
    }

    series.replaceData(xData, yData, heatData);
    return series;
  }

  public HeatMapSeries getHeatMapSeries() {

    return heatMapSeries;
  }

  @Override
  public void paint(Graphics2D g, int width, int height) {

    if (heatMapSeries == null) {
      return;
    }
    setWidth(width);
    setHeight(height);

    prepareForPaint();
    // setSeriesStyles();

    paintBackground(g);

    axisPair.paint(g);
    plot.paint(g);
    chartTitle.paint(g);
    legend.paint(g);
    annotations.forEach(x -> x.paint(g));
  }

  private List<Integer> arrayToList(int[] data) {

    List<Integer> list = new ArrayList<>();
    for (int datum : data) {
      list.add(datum);
    }
    return list;
  }

  private List<Number[]> arrayToList(int[][] heatData) {

    List<Number[]> list = new ArrayList<>();
    Number[] numbers = null;
    int[] array = null;
    for (int i = 0; i < heatData.length; i++) {
      array = heatData[i];
      for (int j = 0; j < array.length; j++) {
        numbers = new Number[3];
        numbers[0] = i;
        numbers[1] = j;
        numbers[2] = heatData[i][j];
        list.add(numbers);
      }
    }
    return list;
  }

  private void sanityCheck(List<?> xData, List<?> yData, List<Number[]> heatData) {

    if (xData == null) {
      throw new IllegalArgumentException("X-Axis data cannot be null!!!");
    }
    if (xData.size() == 0) {
      throw new IllegalArgumentException("X-Axis data cannot be empty!!!");
    }
    if (yData == null) {
      throw new IllegalArgumentException("Y-Axis data cannot be null!!!");
    }
    if (yData.size() == 0) {
      throw new IllegalArgumentException("Y-Axis data cannot be empty!!!");
    }
    if (heatData == null) {
      throw new IllegalArgumentException("Heat data cannot be null!!!");
    }
    if (heatData.size() == 0) {
      throw new IllegalArgumentException("Heat data cannot be empty!!!");
    }
    for (Number[] numbers : heatData) {
      if (numbers != null) {
        if (numbers.length != 3) {
          throw new IllegalArgumentException("Heat data column length is not equal to 3!!!");
        }
        if (numbers[0] == null || numbers[1] == null || numbers[2] == null) {
          throw new IllegalArgumentException(
              "All values in the heat data column cannot be null!!!");
        }
        if (numbers[0].intValue() < 0 || numbers[1].intValue() < 0) {
          throw new IllegalArgumentException("numbers[0] and numbers[1] cannot be less than 0!!!");
        }
      }
    }
  }

  private void prepareForPaint() {
    if (styler.getMin() != Double.MIN_VALUE) {
      heatMapSeries.setMin(styler.getMin());
    }

    if (styler.getMax() != Double.MAX_VALUE) {
      heatMapSeries.setMax(styler.getMax());
    }
  }
}
