package org.knowm.xchart.standalone.issues;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.SwingWrapper;

/** Second Scale */
public class TestForIssue235 {

  public static void main(String[] args) {

    TestForIssue235 testForIssue235 = new TestForIssue235();
    CategoryChart chart = testForIssue235.getChart();
    new SwingWrapper<CategoryChart>(chart).displayChart();
  }

  public CategoryChart getChart() {

    // Create Chart
    CategoryChart chart = new CategoryChartBuilder().build();

    // Customize Chart
    chart.getStyler().setPlotGridVerticalLinesVisible(false);
    chart.getStyler().setStacked(true);
    Map<Object, Object> customTickLabelsMap = new HashMap<>();
    customTickLabelsMap.put("Blue", "zero\nzero");
    customTickLabelsMap.put("Red", "+Red+");
    customTickLabelsMap.put("Green", "+Green+");
    chart.setCustomXAxisTickLabelsMap(customTickLabelsMap);

    // Series
    chart.addSeries(
        "males",
        new ArrayList<>(Arrays.asList(new String[] {"Blue", "Red", "Green"})),
        new ArrayList<>(Arrays.asList(new Number[] {20, 60, 66})));

    //    Map<Object, Object> customTickLabelsMap = new HashMap<>();
    //    customTickLabelsMap.put(0.0, "zero\nzero");
    //    customTickLabelsMap.put(1.0, "+Red+");
    //    customTickLabelsMap.put(2.0, "+Green+");
    //    chart.setCustomCategoryLabels(customTickLabelsMap);

    return chart;
  }
}
