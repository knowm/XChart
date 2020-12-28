package org.knowm.xchart.standalone.issues;

import java.awt.Font;
import java.text.ParseException;

import org.knowm.xchart.BubbleChart;
import org.knowm.xchart.BubbleChartBuilder;
import org.knowm.xchart.BubbleSeries;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.BubbleStyler;
import org.knowm.xchart.style.Styler;

public class TestForIssue545 {


  public static void main(String[] args) throws ParseException {

    BubbleChart chart = getBubbleChart();
    new SwingWrapper(chart).displayChart();
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
//        new DecimalFormat("#%").format(data[1]) + " (" + ((int) data[2]) + "/" + ((int) data[0]) + ")";
//
//    bubbleSeries.setToolTips(new String[] {tooltip});

    return chart;
  }

  public static void setBubbleStyler(BubbleChart chart) {

    BubbleStyler styler = chart.getStyler();
    styler.setLegendPosition(Styler.CardinalPosition.InsideSW);
    styler.setLegendLayout(Styler.LegendLayout.Horizontal);
    styler.setYAxisDecimalPattern("%");
    styler.setXAxisTickMarkSpacingHint(50);
    styler.setAntiAlias(true);
    styler.setToolTipsEnabled(true);
    styler.setToolTipsAlwaysVisible(true);
    styler.setHasAnnotations(true);
    styler.setToolTipFont(new Font("SansSerif", Font.PLAIN, 14));
  }
}
