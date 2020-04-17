package org.knowm.xchart.demo.charts.heatmap;

import java.awt.Color;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.knowm.xchart.HeatMapChart;
import org.knowm.xchart.HeatMapChartBuilder;
import org.knowm.xchart.HeatMapSeries;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;

/**
 * HeatMap large
 *
 * <p>Demonstrates the following:
 *
 * <ul>
 *   <li>Set rangeColors
 *   <li>HeatMapSeries setMin
 *   <li>HeatMapSeries setMax
 */
public class HeatMapChart02 implements ExampleChart<HeatMapChart> {

  public static void main(String[] args) {

    ExampleChart<HeatMapChart> exampleChart = new HeatMapChart02();
    HeatMapChart chart = exampleChart.getChart();
    new SwingWrapper<HeatMapChart>(chart).displayChart();
  }

  @Override
  public HeatMapChart getChart() {

    // Create Chart
    HeatMapChart chart =
        new HeatMapChartBuilder().width(800).height(600).title(getClass().getSimpleName()).build();

    chart.getStyler().setPlotContentSize(0.999);
    chart.getStyler().setXAxisMaxLabelCount(10);

    Color[] rangeColors = {new Color(255, 182, 193), new Color(255, 20, 147), new Color(139, 0, 0)};
    chart.getStyler().setRangeColors(rangeColors);

    List<String> xData = new ArrayList<>();
    for (BigDecimal bd = new BigDecimal("-2.0");
        bd.doubleValue() <= 2;
        bd = bd.add(new BigDecimal("0.04"))) {
      xData.add(bd.toString());
    }
    List<String> yData = new ArrayList<>();
    for (BigDecimal bd = new BigDecimal("-2.0");
        bd.doubleValue() <= 2;
        bd = bd.add(new BigDecimal("0.02"))) {
      yData.add(bd.toString());
    }
    List<Number[]> heatData = new ArrayList<>();
    for (int i = 0; i < xData.size(); i++) {
      for (int j = 0; j < yData.size(); j++) {
        Number[] numbers = {
          i,
          j,
          Math.sin(Double.parseDouble(xData.get(i))) * Math.sin(Double.parseDouble(yData.get(j)))
        };
        heatData.add(numbers);
      }
    }

    HeatMapSeries heatMapSeries = chart.addSeries("heatMap", xData, yData, heatData);
    heatMapSeries.setMin(-1);
    heatMapSeries.setMax(1);
    return chart;
  }

  @Override
  public String getExampleChartName() {

    return getClass().getSimpleName() + " - HeatMap Large";
  }
}
