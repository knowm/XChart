package org.knowm.xchart.standalone.issues;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.knowm.xchart.demo.ExampleChartTester;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.demo.charts.radar.RadarChart01;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.style.RadarStyler;

public class TestForIssue316 {

  static final int WIDTH = 465;
  static final int HEIGHT = 320;

  public static void main(String[] args) {

    ExampleChartTester tester =
        new ExampleChartTester() {

          @Override
          protected Map<String, Chart> getCharts(ExampleChartInfo chartInfo) {

            LinkedHashMap<String, Chart> map = new LinkedHashMap<String, Chart>();
            ExampleChart ec = chartInfo.getExampleChart();

            map.put("No fill", getChartWithoutFill(ec));
            map.put("Default fill", getChart(ec));

            return map;
          }
        };

    ArrayList<ExampleChart> exampleList = new ArrayList<ExampleChart>(1);
    exampleList.add(new RadarChart01());
    tester.setExampleList(exampleList);
    tester.createAndShowGUI();
  }

  private static Chart getChart(ExampleChart ec) {

    Chart chart = ec.getChart();
    return chart;
  }
  
  private static Chart getChartWithoutFill(ExampleChart ec) {
    
    Chart chart = ec.getChart();
    ((RadarStyler)chart.getStyler()).setSeriesFilled(false);
    return chart;
  }

}
