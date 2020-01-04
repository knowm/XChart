package org.knowm.xchart.standalone.issues;

import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategorySeries;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.demo.charts.area.AreaChart01;
import org.knowm.xchart.demo.charts.bar.BarChart06;
import org.knowm.xchart.demo.charts.bar.BarChart09;
import org.knowm.xchart.demo.charts.date.DateChart05;
import org.knowm.xchart.internal.chartpart.Chart;

/** Create a Chart matrix */
public class TestForTickCalculatorCustom {

  public static void main(String[] args) {

    List<Chart> charts = new ArrayList<>();

    charts.add(new AreaChart01().getChart());
    charts.add(new BarChart06().getChart());
    charts.add(new DateChart05().getChart());
    charts.add(new BarChart09().getChart());

    {
      XYChart chart = new AreaChart01().getChart();
      Map<Object, Object> customXAxisTickLabelsMap = new HashMap<>();
      customXAxisTickLabelsMap.put(0, "zero");
      customXAxisTickLabelsMap.put(3, "3.5");
      customXAxisTickLabelsMap.put(5, " ");
      customXAxisTickLabelsMap.put(9, "nine");

      Map<Object, Object> customYAxisTickLabelsMap = new HashMap<>();
      customYAxisTickLabelsMap.put(1.0, "max c");
      customYAxisTickLabelsMap.put(6.0, "max b");
      customYAxisTickLabelsMap.put(9.0, "max a");

      chart.setCustomXAxisTickLabelsMap(customXAxisTickLabelsMap);
      chart.setCustomYAxisTickLabelsMap(customYAxisTickLabelsMap);
      chart.setTitle("AreaChart01 - custom x&y axis labels");
      charts.add(chart);
    }
    {
      // issue 171
      // Is there a way to display the labels, say, for every 5 x-value
      Chart chart = new BarChart06().getChart();

      CategorySeries series = (CategorySeries) chart.getSeriesMap().get("histogram 1");
      List<?> xData = (List<?>) series.getXData();
      Map<Object, Object> customTickLabelsMap = new HashMap<>();
      // for category charts 0 means first category, 1 means second category, ...

      for (int i = 0; i < xData.size(); i += 5) {
        customTickLabelsMap.put(xData.get(i), xData.get(i));
      }

      chart.setCustomXAxisTickLabelsMap(customTickLabelsMap);
      chart.setTitle("Score Histogram - x axis labels on each 5th category");
      charts.add(chart);
    }
    {
      // issue 159 & 132
      // The graph however displays labels on the x axis inbetween these points. Doesn't seem to be
      // possible to prevent this
      // This usage of custom tick labels will create a label only where data points are
      XYChart chart = new DateChart05().getChart();
      chart.setTitle("Day scale - x axis labels on every data point");

      XYSeries xySeries = chart.getSeriesMap().get("blah");
      double[] xData = xySeries.getXData();
      Map<Object, Object> customTickLabelsMap = new HashMap<>();
      SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");

      for (double d : xData) {
        Date date = new Date((long) d);
        String label = sdf.format(date);
        customTickLabelsMap.put(d, label);
      }

      chart.setCustomXAxisTickLabelsMap(customTickLabelsMap);
      charts.add(chart);
    }
    {
      CategoryChart chart = new BarChart09().getChart();

      Map<Object, Object> customTickLabelsMap = new HashMap<>();

      customTickLabelsMap.put("A", "-A-");
      customTickLabelsMap.put("D", "+D+");

      chart.setCustomXAxisTickLabelsMap(customTickLabelsMap);
      chart.setTitle("Value vs. Letter - x axis labels by category name");
      charts.add(chart);
    }

    for (Chart chart : charts) {
      chart.getStyler().setToolTipsEnabled(true);
    }

    JFrame frame = new SwingWrapper<>(charts, 2, 4).displayChartMatrix();
    Dimension preferredSize = new Dimension(1920, 1000);
    frame.setPreferredSize(preferredSize);
    frame.setSize(preferredSize);
    frame.setVisible(true);
  }
}
