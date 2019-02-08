package org.knowm.xchart.standalone.issues;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import org.knowm.xchart.BubbleSeries;
import org.knowm.xchart.CategorySeries;
import org.knowm.xchart.OHLCSeries;
import org.knowm.xchart.PieSeries;
import org.knowm.xchart.RadarSeries;
import org.knowm.xchart.demo.ExampleChartTester;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.demo.charts.dial.DialChart01;
import org.knowm.xchart.demo.charts.realtime.RealtimeChart01;
import org.knowm.xchart.demo.charts.realtime.RealtimeChart02;
import org.knowm.xchart.demo.charts.realtime.RealtimeChart03;
import org.knowm.xchart.demo.charts.realtime.RealtimeChart04;
import org.knowm.xchart.demo.charts.realtime.RealtimeChart05;
import org.knowm.xchart.demo.charts.realtime.RealtimeChart06;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.series.AxesChartSeries;
import org.knowm.xchart.internal.series.AxesChartSeriesNumericalNoErrorBars;
import org.knowm.xchart.internal.series.Series;

public class TestForIssue227 {

  static final int WIDTH = 465;
  static final int HEIGHT = 320;

  public static void main(String[] args) {

    ExampleChartTester tester =
        new ExampleChartTester() {

          @Override
          protected Map<String, Chart> getCharts(ExampleChartInfo chartInfo) {

            LinkedHashMap<String, Chart> map = new LinkedHashMap<String, Chart>();
            ExampleChart ec = chartInfo.getExampleChart();
            Chart c = getChartWithCustomTooltip(ec);
            if (c != null) {
              c.getStyler().setToolTipsAlwaysVisible(true);
              map.put("Custom tooltips always visible", c);
            }

            map.put("Custom tooltips", getChartWithCustomTooltip(ec));
            map.put("Default tooltips", getChart(ec));

            return map;
          }
        };

    HashSet<Class> excludeSet = new HashSet();
    // dial has annotation
    excludeSet.add(DialChart01.class);

    // in real time charts tooltips must be set for each data point
    excludeSet.add(RealtimeChart01.class);
    excludeSet.add(RealtimeChart02.class);
    excludeSet.add(RealtimeChart03.class);
    excludeSet.add(RealtimeChart04.class);
    excludeSet.add(RealtimeChart05.class);
    excludeSet.add(RealtimeChart06.class);

    tester.setExcludeSet(excludeSet);
    tester.createAndShowGUI();
  }

  private static Chart getChartWithCustomTooltip(ExampleChart ec) {

    Chart chart = getChart(ec);
    Map<String, Series> seriesMap = chart.getSeriesMap();

    boolean flag = false;
    for (Series series : seriesMap.values()) {
      if (series instanceof PieSeries) {
        String[] toolTips = getToolTips(series.getName(), 1);
        ((PieSeries) series).setToolTip(toolTips[0]);
        flag = true;
        continue;
      } else if (series instanceof RadarSeries) {
        int count = ((RadarSeries) series).getValues().length;
        String[] toolTips = getToolTips(series.getName(), count);
        ((RadarSeries) series).setTooltipOverrides(toolTips);
        flag = true;
        continue;
      }
      if (!(series instanceof AxesChartSeries)) {
        System.out.println(series.getClass());
        continue;
      }
      int count = 0;
      if (series instanceof AxesChartSeriesNumericalNoErrorBars) {
        count = ((AxesChartSeriesNumericalNoErrorBars) series).getXData().length;
      } else if (series instanceof CategorySeries) {
        count = ((CategorySeries) series).getYData().size();
      } else if (series instanceof OHLCSeries) {
        count = ((OHLCSeries) series).getOpenData().length;
      } else if (series instanceof BubbleSeries) {
        count = ((BubbleSeries) series).getXData().length;
      } else {
        System.out.println(series.getClass());
      }

      if (count <= 0) {
        continue;
      }
      String[] toolTips = getToolTips(series.getName(), count);
      ((AxesChartSeries) series).setToolTips(toolTips);
      flag = true;
    }
    if (!flag) {
      System.out.println("Skipping " + ec.getClass().getSimpleName());
      return null;
    }
    return chart;
  }

  private static Chart getChart(ExampleChart ec) {

    Chart chart = ec.getChart();
    chart.getStyler().setToolTipsEnabled(true);
    return chart;
  }

  private static String[] getToolTips(String name, int count) {

    // only show 10 tooltips
    int x = (int) Math.ceil(count / 10.0);
    String[] t = new String[count];
    for (int i = 0; i < t.length; i++) {
      if (i % x == 0) {
        t[i] = "Custom tt - " + name + " Point " + (i + 1);
      } else {
        t[i] = null; // No tooltip
      }
    }
    return t;
  }
}
