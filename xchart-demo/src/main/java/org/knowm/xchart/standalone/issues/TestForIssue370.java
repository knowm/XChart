package org.knowm.xchart.standalone.issues;

import java.util.ArrayList;
import java.util.List;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.style.Styler.CardinalPosition;

public class TestForIssue370 {

  public static void main(String[] args) {

    TestForIssue370 testForIssue370 = new TestForIssue370();
    List<XYChart> charts = new ArrayList<>();
    charts.add(testForIssue370.getChart("Group yAxis DecimalPattern", false));
    charts.add(testForIssue370.getChart("Group yAxis DecimalPattern Logarithmic", true));
    new SwingWrapper<XYChart>(charts).displayChartMatrix();
  }

  public XYChart getChart(String title, boolean isYAxisLogarithmic) {

    // Create Chart
    XYChart chart =
        new XYChartBuilder()
            .width(600)
            .height(400)
            .title(title)
            .xAxisTitle("X")
            .yAxisTitle("Y")
            .build();

    // Customize Chart
    chart.getStyler().setLegendPosition(CardinalPosition.OutsideE);
    chart.getStyler().setAxisTitlesVisible(false);
    chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Line);
    chart.getStyler().setToolTipsEnabled(true);
    chart.getStyler().setToolTipsAlwaysVisible(true);
    chart.getStyler().setYAxisLogarithmic(isYAxisLogarithmic);

    // Series
    chart.addSeries("a", new double[] {1, 2, 3, 4, 5}, new double[] {400, 200, 300, 200, 100});
    XYSeries b =
        chart.addSeries(
            "b",
            new double[] {1, 2, 3, 4, 5},
            new double[] {0.00012328, 0.0015467, 0.019879, 0.19859, 1.59681});
    b.setYAxisGroup(1);
    if (isYAxisLogarithmic) {
      b.setYAxisDecimalPattern("#0.##E0");
    } else {
      b.setYAxisDecimalPattern("0.0000");
    }

    return chart;
  }
}
