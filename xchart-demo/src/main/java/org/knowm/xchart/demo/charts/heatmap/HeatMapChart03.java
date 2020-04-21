package org.knowm.xchart.demo.charts.heatmap;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.knowm.xchart.HeatMapChart;
import org.knowm.xchart.HeatMapChartBuilder;
import org.knowm.xchart.HeatMapSeries;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;

/**
 * HeatMap Piecewise
 *
 * <p>Demonstrates the following:
 *
 * <ul>
 *   <li>Set rangeColors 5 color
 *   <li>Piecewise
 *   <li>ShowValue
 *   <li>HeatMapSeries setMin
 *   <li>HeatMapSeries setMax
 *   <li>ToolTipsEnabled
 */
public class HeatMapChart03 implements ExampleChart<HeatMapChart> {

  public static void main(String[] args) {

    ExampleChart<HeatMapChart> exampleChart = new HeatMapChart03();
    HeatMapChart chart = exampleChart.getChart();
    new SwingWrapper<HeatMapChart>(chart).displayChart();
  }

  @Override
  public HeatMapChart getChart() {

    // Create Chart
    HeatMapChart chart =
        new HeatMapChartBuilder()
            .width(1000)
            .height(600)
            .title("Sales per employee per weekday")
            .xAxisTitle("Employee name")
            .yAxisTitle("Working day")
            .build();

    chart.getStyler().setPlotContentSize(0.999);
    chart.getStyler().setLegendFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
    chart.getStyler().setToolTipsEnabled(true);
    chart.getStyler().setPiecewise(true);
    chart.getStyler().setShowValue(true);

    Color[] rangeColors = {Color.YELLOW, Color.CYAN, Color.GREEN, Color.BLUE, Color.RED};
    chart.getStyler().setRangeColors(rangeColors);

    List<String> xData = new ArrayList<String>();
    xData.add("Tim");
    xData.add("Tom");
    xData.add("Lida");
    xData.add("Mark");
    xData.add("LiLei");
    xData.add("Lukas");
    xData.add("Marie");

    List<String> yData = new ArrayList<String>();
    yData.add("Monday");
    yData.add("Tuesday");
    yData.add("Wedesday");
    yData.add("Thursday");
    yData.add("Friday");

    Number[][] data = {
      {0, 0, 146}, {0, 1, 830}, {0, 2, 120}, {0, 3, 356}, {0, 4, 456}, {1, 0, 756}, {1, 1, 450},
      {1, 2, 562}, {1, 3, 610}, {1, 4, 258}, {2, 0, 666}, {2, 1, 777}, {2, 2, 555}, {2, 3, 445},
      {2, 4, 236}, {3, 0, 123}, {3, 1, 456}, {3, 2, 789}, {3, 3, 987}, {3, 4, 654}, {4, 0, 321},
      {4, 1, 753}, {4, 2, 951}, {4, 3, 159}, {4, 4, 269}, {5, 0, 358}, {5, 1, 820}, {5, 2, 635},
      {5, 3, 469}, {5, 4, 562}, {6, 0, 632}, {6, 1, 547}, {6, 2, 350}, {6, 3, 269}, {6, 4, 658}
    };

    HeatMapSeries heatMapSeries =
        chart.addSeries("Sales per employee", xData, yData, Arrays.asList(data));
    heatMapSeries.setMin(0);
    heatMapSeries.setMax(1000);
    return chart;
  }

  @Override
  public String getExampleChartName() {

    return getClass().getSimpleName() + " - HeatMap Piecewise";
  }
}
