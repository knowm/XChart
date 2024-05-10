package org.knowm.xchart.standalone.issues;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

public class TestForIssue582 {

    public static void main(String[] args) throws ParseException {

        XYChart chart = getXYChart();
        new SwingWrapper(chart).displayChart();
    }

    public static XYChart getXYChart() {
        XYChart chart = new XYChartBuilder()
                .width(720)
                .height(480)
                .title("Deadlock Example")
                .xAxisTitle("Count")
                .yAxisTitle("Value")
                .build();

        // If the decimal places in the pattern is fewer than the largest data value, the deadlock occurs.
//        chart.getStyler().setDecimalPattern("#0.0000");
//        chart.getStyler().setDecimalPattern("#,###.00");

//        chart.getStyler().setDecimalPattern("$ #0.00");
//        chart.getStyler().setDecimalPattern("$ #0.000");

        chart.getStyler().setLocale(Locale.ITALIAN);
        chart.getStyler().setDecimalPattern("#0.000");

        double[] xValues = new double[]{1, 2, 3, 4, 5, 6, 7, 8};

        // The only value here is 0.001 which is one decimal place below the pattern.
      double[] yValues =  new double[] { 0.0, 0.0, 0.0, 0.0, 0.001, 0.0, 0.0, 0.0};
//      double[] yValues =  new double[] { 0.0, 0.0, 0.0, 0.0, -0.001, 0.0, 0.0, 0.0};
//      double[] yValues =  new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
//      double[] yValues =  new double[] { 0.0001, 0.0, 0.0, 0.0, -0.001, 0.0, 0.0, 0.0};
//      double[] yValues =  new double[] { -0.0002, -0.0002, -0.0002, -0.0002, -0.001, -0.0002, -0.0002, -0.0002};
//        double[] yValues = new double[]{0.0002, 0.0002, 0.0002, 0.0002, 0.0001, 0.0002, 0.0002, 0.0002};
//        double[] yValues =  new double[] { 20.0002, 20.0002, 20.0002, 20.0002, 20.001, 20.0002, 20.0002, 20.0002};

        chart.addSeries("main", xValues, yValues);


        return chart;
    }
}
