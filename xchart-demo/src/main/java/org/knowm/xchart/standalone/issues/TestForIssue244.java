package org.knowm.xchart.standalone.issues;

import java.util.ArrayList;
import java.util.List;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.style.AxesChartStyler;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.knowm.xchart.style.Styler.YAxisPosition;

/** @author timmolter */
public class TestForIssue244 {

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
      chart.setTitle("sin(x) on second axis");
      Series series = (Series) chart.getSeriesMap().get("y=sin(x)");
      series.setYAxisGroup(1);
      chart.setYAxisGroupTitle(1, "sin(x) [-1, 1]");
      chart.setYAxisGroupTitle(0, "cos(x) [-10, 10]");
      chart.getStyler().setYAxisGroupPosition(1, YAxisPosition.Right);
      charts.add(chart);
    }

    {
      Chart chart = getLineChart();
      chart.setTitle("2 axis, default y max & y min");
      Series series = (Series) chart.getSeriesMap().get("y=sin(x)");
      series.setYAxisGroup(1);
      chart.setYAxisGroupTitle(1, "sin(x) [-1, 1]");
      chart.setYAxisGroupTitle(0, "cos(x) [-10, 10]");
      chart.getStyler().setYAxisGroupPosition(1, YAxisPosition.Right);

      AxesChartStyler styler = (AxesChartStyler) chart.getStyler();
      styler.setYAxisMax(20.0);
      styler.setYAxisMin(-20.0);

      charts.add(chart);
    }
    {
      Chart chart = getLineChart();
      chart.setTitle("2 axis, max on group 0");
      Series series = (Series) chart.getSeriesMap().get("y=sin(x)");
      series.setYAxisGroup(1);
      chart.setYAxisGroupTitle(1, "sin(x) [-1, 1]");
      chart.setYAxisGroupTitle(0, "cos(x) [-10, 10]");
      chart.getStyler().setYAxisGroupPosition(1, YAxisPosition.Right);

      AxesChartStyler styler = (AxesChartStyler) chart.getStyler();
      styler.setYAxisMax(0, 20.0);
      styler.setYAxisMin(0, -20.0);

      charts.add(chart);
    }

    {
      Chart chart = getLineChart();
      chart.setTitle("2 axis, max on group 0, 1");
      Series series = (Series) chart.getSeriesMap().get("y=sin(x)");
      series.setYAxisGroup(1);
      chart.setYAxisGroupTitle(1, "sin(x) [-1, 1]");
      chart.setYAxisGroupTitle(0, "cos(x) [-10, 10]");
      chart.getStyler().setYAxisGroupPosition(1, YAxisPosition.Right);

      AxesChartStyler styler = (AxesChartStyler) chart.getStyler();
      styler.setYAxisMax(0, 20.0);
      styler.setYAxisMin(0, -20.0);
      styler.setYAxisMax(1, 2.0);
      styler.setYAxisMin(1, -2.0);

      charts.add(chart);
    }

    {
      Chart chart = getLineChart();
      chart.setTitle("2 axis, max on group 0, 1, and default max");
      Series series = (Series) chart.getSeriesMap().get("y=sin(x)");
      series.setYAxisGroup(1);
      chart.setYAxisGroupTitle(1, "sin(x) [-1, 1]");
      chart.setYAxisGroupTitle(0, "cos(x) [-10, 10]");
      chart.getStyler().setYAxisGroupPosition(1, YAxisPosition.Right);

      AxesChartStyler styler = (AxesChartStyler) chart.getStyler();
      // these 2 lines will be overwritten by group max settings
      styler.setYAxisMax(100.0);
      styler.setYAxisMin(-100.0);

      styler.setYAxisMax(0, 20.0);
      styler.setYAxisMin(0, -20.0);
      styler.setYAxisMax(1, 2.0);
      styler.setYAxisMin(1, -2.0);

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
}
