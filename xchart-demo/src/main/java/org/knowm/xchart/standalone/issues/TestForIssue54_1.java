package org.knowm.xchart.standalone.issues;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.knowm.xchart.BubbleChart;
import org.knowm.xchart.BubbleChartBuilder;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.CategorySeries.CategorySeriesRenderStyle;
import org.knowm.xchart.Histogram;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.Styler.ChartTheme;
import org.knowm.xchart.style.Styler.LegendPosition;

/**
 * Create a Chart matrix
 *
 * @author timmolter
 */
public class TestForIssue54_1 {

  static final int WIDTH = 465;
  static final int HEIGHT = 320;

  public static void main(String[] args) {

    List<Chart> charts = new ArrayList<Chart>();
    {
      Chart chart = getLineChart();
      chart.setTitle("Default axis");
      charts.add(chart);
    }
    {
      Chart chart = getLineChart();
      chart.setTitle("sin(x) on second axis with title");
      Series series = (Series) chart.getSeriesMap().get("y=sin(x)");
      series.setYAxisGroup(1);
      chart.setYAxisGroupTitle(1, "sin(x)");
      charts.add(chart);
    }

    {
      Chart chart = getAreaChart();
      chart.setTitle("Default axis");
      charts.add(chart);
    }
    //    {
    //      Chart chart = getAreaChart();
    //      chart.setTitle("b on second axis");
    //      Series series = (Series) chart.getSeriesMap().get("b");
    //      series.setYAxisGroup(1);
    //      charts.add(chart);
    //    }
    {
      Chart chart = getAreaChart();
      chart.setTitle("all different axis, b & c axis on right");
      Series series = (Series) chart.getSeriesMap().get("b");
      series.setYAxisGroup(1);
      series = (Series) chart.getSeriesMap().get("c");
      series.setYAxisGroup(2);
      chart.getStyler().setYAxisGroupPosition(1, Styler.YAxisPosition.Right);
      chart.getStyler().setYAxisGroupPosition(2, Styler.YAxisPosition.Right);
      charts.add(chart);
    }

    {
      Chart chart = getCaregoryChart();
      chart.setTitle("Default axis");
      charts.add(chart);
    }
    {
      Chart chart = getCaregoryChart();
      chart.setTitle("b on second axis, b on right");
      Series series = (Series) chart.getSeriesMap().get("b");
      series.setYAxisGroup(1);
      chart.getStyler().setYAxisGroupPosition(1, Styler.YAxisPosition.Right);
      charts.add(chart);
    }

    {
      Chart chart = getCategoryLineChart();
      chart.setTitle("Default axis");
      charts.add(chart);
    }
    {
      Chart chart = getCategoryLineChart();
      chart.setTitle("b&d on second axis");
      Series series = (Series) chart.getSeriesMap().get("b");
      series.setYAxisGroup(1);
      series = (Series) chart.getSeriesMap().get("d");
      series.setYAxisGroup(1);
      chart.getStyler().setYAxisGroupPosition(1, Styler.YAxisPosition.Right);
      charts.add(chart);
    }

    {
      Chart chart = getBubleChart();
      chart.setTitle("Default axis");
      charts.add(chart);
    }
    {
      Chart chart = getBubleChart();
      chart.setTitle("b on second axis");
      Series series = (Series) chart.getSeriesMap().get("b");
      series.setYAxisGroup(1);
      charts.add(chart);
    }

    // Chart chart = charts.get(3);
    // charts.clear();
    // charts.add(chart);
    {
      Chart chart = getLineChart();
      chart.setTitle("Default axis on right");
      chart.getStyler().setYAxisGroupPosition(0, Styler.YAxisPosition.Right);
      charts.add(chart);
    }

    new SwingWrapper<Chart>(charts).displayChartMatrix();
  }

  static Chart getLineChart() {
    XYChart chart =
        new XYChartBuilder().width(WIDTH).height(HEIGHT).xAxisTitle("X").yAxisTitle("Y").build();

    // Customize Chart
    chart.getStyler().setToolTipsEnabled(true);
    chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
    // generates sine data
    int size = 30;
    List<Integer> xData = new ArrayList<Integer>();
    List<Double> yData = new ArrayList<Double>();
    List<Integer> xData2 = new ArrayList<Integer>();
    List<Double> yData2 = new ArrayList<Double>();
    for (int i = 0; i <= size; i++) {
      double radians = (Math.PI / (size / 2) * i);
      int x = i - size / 2;
      xData.add(x);
      yData.add(-1 * Math.sin(radians));
      xData2.add(x);
      yData2.add(-10 * Math.cos(radians));
    }

    // Series
    chart.addSeries("y=sin(x)", xData, yData);
    chart.addSeries("y=cos(x)", xData2, yData2);
    return chart;
  }

  static Chart getAreaChart() {

    // Create Chart
    XYChart chart =
        new XYChartBuilder()
            .width(WIDTH)
            .height(HEIGHT)
            .title("Area chart")
            .xAxisTitle("X")
            .yAxisTitle("Y")
            .build();

    // Customize Chart
    chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
    chart.getStyler().setAxisTitlesVisible(true);
    chart.setYAxisGroupTitle(0, "a");
    chart.setYAxisGroupTitle(1, "b");
    chart.setYAxisGroupTitle(2, "c");

    chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Area);
    chart.getStyler().setToolTipsEnabled(true);

    // Series
    chart.addSeries("a", new double[] {0, 3, 6, 9, 12}, new double[] {-1, 5, 9, 6, 5});
    chart.addSeries("b", new double[] {1, 4, 7, 10, 13}, new double[] {-10, 50, 90, 60, 50});
    chart.addSeries("c", new double[] {2, 5, 8, 11, 14}, new double[] {-100, 500, 900, 600, 500});

    return chart;
  }

  static CategoryChart getCaregoryChart() {

    // Create Chart
    CategoryChart chart =
        new CategoryChartBuilder()
            .width(WIDTH)
            .height(HEIGHT)
            .title("Score Histogram")
            .xAxisTitle("Mean")
            .yAxisTitle("Count")
            .build();

    // Customize Chart
    chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
    chart.getStyler().setAvailableSpaceFill(.96);
    chart.getStyler().setPlotGridVerticalLinesVisible(false);
    chart.getStyler().setToolTipsEnabled(true);
    chart.getStyler().setToolTipType(Styler.ToolTipType.yLabels);

    // Series
    List<Integer> data = getGaussianData(1000);
    ArrayList<Integer> data2 = new ArrayList<Integer>(10000);
    // Add each item twice
    for (Integer val : data) {
      data2.add(val);
      data2.add(val);
    }
    Histogram histogram1 = new Histogram(data, 10, -30, 30);
    chart.addSeries("a", histogram1.getxAxisData(), histogram1.getyAxisData());
    Histogram histogram2 = new Histogram(data2, 10, -30, 30);
    chart.addSeries("b", histogram2.getxAxisData(), histogram2.getyAxisData());

    return chart;
  }

  static List<Integer> getGaussianData(int count) {

    List<Integer> data = new ArrayList<Integer>(count);
    Random r = new Random();
    for (int i = 0; i < count; i++) {
      data.add((int) (r.nextGaussian() * 10));
    }
    return data;
  }

  static CategoryChart getCategoryLineChart() {

    // Create Chart
    CategoryChart chart =
        new CategoryChartBuilder()
            .width(WIDTH)
            .height(HEIGHT)
            .theme(ChartTheme.GGPlot2)
            .title("ThreadPool Benchmark")
            .xAxisTitle("Threads")
            .yAxisTitle("Executions")
            .build();

    // Customize Chart
    chart.getStyler().setDefaultSeriesRenderStyle(CategorySeriesRenderStyle.Line);
    chart.getStyler().setXAxisLabelRotation(270);
    chart.getStyler().setLegendPosition(LegendPosition.OutsideE);
    chart.getStyler().setAvailableSpaceFill(0);
    chart.getStyler().setOverlapped(true);
    chart.getStyler().setToolTipsEnabled(true);

    // Declare data
    List<String> xAxisKeys =
        Arrays.asList("release-0.5", "release-0.6", "release-0.7", "release-0.8", "release-0.9");
    String[] seriesNames = new String[] {"a", "b", "c", "d"};

    Integer[][] dataPerSeries =
        new Integer[][] {
          {5, 20, 15, 25, 35},
          {10, 40, 30, 50, 70},
          {20, 80, 60, 100, 140},
          {45, 121, 151, 231, 381},
        };

    // Series
    for (int i = 0; i < seriesNames.length; i++) {
      chart.addSeries(seriesNames[i], xAxisKeys, Arrays.asList(dataPerSeries[i]));
    }

    return chart;
  }

  static BubbleChart getBubleChart() {

    // Create Chart
    BubbleChart chart =
        new BubbleChartBuilder()
            .width(WIDTH)
            .height(HEIGHT)
            .title("BubbleChart01")
            .xAxisTitle("X")
            .yAxisTitle("Y")
            .build();
    chart.getStyler().setToolTipsEnabled(true);
    // Customize Chart

    // Series
    double[] xData = new double[] {1, 2.0, 3.0, 4.0};
    double[] yData = new double[] {10, 4, 7, 7.7};
    double[] bubbleData = new double[] {17, 40, 50, 51};

    double[] yData2 = new double[] {10, 20, 30, 40};
    double[] bubbleData2 = new double[] {37, 35, 80, 27};

    chart.addSeries("a", xData, yData, bubbleData);
    chart.addSeries("b", xData, yData2, bubbleData2);

    return chart;
  }
}
