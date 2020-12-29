package org.knowm.xchart.standalone.issues;

import java.util.ArrayList;
import java.util.List;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.Styler;

public class TestForIssue257 {

  public static void main(String[] args) {

    CategoryChart chart =
        new CategoryChartBuilder()
            .width(500)
            .height(500)
            .title("Average Weight and Height per Team")
            .xAxisTitle("Team_ID")
            .build();
    // Customize Chart

    chart.getStyler().setPlotGridVerticalLinesVisible(false);
    chart.getStyler().setStacked(true);
    chart.getStyler().setPlotGridHorizontalLinesVisible(true);
    chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideS);
    chart.getStyler().setLegendLayout(Styler.LegendLayout.Horizontal);
    List<String> category_values = new ArrayList<String>();

    for (int i = 1; i <= 15; i++) {
      category_values.add("K0" + i);
    }

    List<Double> height = new ArrayList<Double>();

    List<Double> weight = new ArrayList<Double>();

    for (int i = 1; i <= 15; i++) {
      weight.add((double) (i + 45));
      height.add((double) (i + 30));
    }

    chart.addSeries("Average_Height", category_values, height);
    chart.addSeries("Average_Weight", category_values, weight);

    new SwingWrapper<CategoryChart>(chart).displayChart();
  }
}
