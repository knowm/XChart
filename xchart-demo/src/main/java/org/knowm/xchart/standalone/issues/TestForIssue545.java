package org.knowm.xchart.standalone.issues;

import java.awt.Font;
import java.text.ParseException;

import org.knowm.xchart.*;
import org.knowm.xchart.style.BubbleStyler;
import org.knowm.xchart.style.Styler;

public class TestForIssue545 {

  public static void main(String[] args) throws ParseException {

    BubbleChart chart = getBubbleChart();
    new SwingWrapper(chart).displayChart();

    XYChart xyChart = getXYChart();
    new SwingWrapper(xyChart).displayChart();
  }

  public static BubbleChart getBubbleChart() {
    double[] data = new double[] {1298.0, 0.0215, 279.0};

    BubbleChart chart =
        new BubbleChartBuilder()
            .width(800)
            .height(600)
            .title("Some title")
            .xAxisTitle("Volume")
            .yAxisTitle("Rate")
            .build();
    setBubbleStyler(chart);
    BubbleSeries bubbleSeries =
        chart.addSeries(
            "serieName", new double[] {data[0]}, new double[] {data[1]}, new double[] {data[2]});
    //    bubbleSeries.setCustomToolTips(true);
    //    String tooltip =
    //        new DecimalFormat("#%").format(data[1]) + " (" + ((int) data[2]) + "/" + ((int)
    // data[0]) + ")";
    //
    //    bubbleSeries.setToolTips(new String[] {tooltip});

    return chart;
  }

  public static void setBubbleStyler(BubbleChart chart) {

    BubbleStyler styler = chart.getStyler();
    styler.setLegendPosition(Styler.LegendPosition.InsideSW);
    styler.setLegendLayout(Styler.LegendLayout.Horizontal);
    styler.setYAxisDecimalPattern("%");
    styler.setXAxisTickMarkSpacingHint(50);
    styler.setAntiAlias(true);
    styler.setToolTipsEnabled(true);
    styler.setToolTipsAlwaysVisible(true);
    styler.setToolTipFont(new Font("SansSerif", Font.PLAIN, 14));
  }
  public static XYChart getXYChart() {

    // Create Chart
    XYChart chart =
            new XYChartBuilder()
                    .width(800)
                    .height(600)
                    .title("TestForIssue545")
                    .xAxisTitle("X")
                    .yAxisTitle("y")
                    .build();

    // Customize Chart
    chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideE);
    chart.getStyler().setToolTipsEnabled(true);
    chart.getStyler().setToolTipsAlwaysVisible(true);

    // Series
    XYSeries one = chart.addSeries("a", new double[] {1, 2, 3, 4, 5}, new double[] {-1, 6, 9, 6, 5});
    one.setCustomToolTips(true);
    one.setToolTips(new String[] {"test", "test1", "test2", "test3", "test4"});


    XYSeries two = chart.addSeries("b", new double[] {1, 2, 3, 4, 5}, new double[] {9, 7, 3, -3, 8});
    two.setToolTips(new String[] {"this", "is", "different", "data!", "wow!"});
    two.setCustomToolTips(true);

    return chart;
  }
}
