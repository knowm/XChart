package org.knowm.xchart.demo.charts.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.demo.charts.ExampleChart;

/**
 * Month scale
 *
 * <p>Demonstrates the following:
 *
 * <ul>
 *   <li>Setting custom Y-Axis tick labels
 */
public class DateChart06 implements ExampleChart<XYChart> {

  public static void main(String[] args) {

    ExampleChart<XYChart> exampleChart = new DateChart06();
    XYChart chart = exampleChart.getChart();
    new SwingWrapper<>(chart).displayChart();
  }

  @Override
  public XYChart getChart() {

    // Create Chart
    XYChart chart = new XYChartBuilder().width(800).height(600).title("Month Scale").build();

    // Customize Chart
    chart.getStyler().setLegendVisible(false);

    // Series
    List<Date> xData = new ArrayList<>();
    List<Double> yData = new ArrayList<>();

    Random random = new Random();

    DateFormat sdf = new SimpleDateFormat("yyyy-MM");
    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
    Date date = null;
    for (int i = 1; i <= 14; i++) {
      try {
        date = sdf.parse("2013-" + (2 * i + random.nextInt(1)));
      } catch (ParseException e) {
        e.printStackTrace();
      }
      xData.add(date);
      yData.add(Math.random() * i);
    }

    chart.addSeries("blah", xData, yData);

    chart
        .getStyler()
        .setyAxisTickLabelsFormattingFunction(x -> NumberWordConverter.convert(x.intValue()));

    return chart;
  }

  @Override
  public String getExampleChartName() {

    return getClass().getSimpleName() + " - Month Scale with custom Y-Axis tick labels";
  }

  static class NumberWordConverter {

    public static final String[] units = {
      "",
      "one",
      "two",
      "three",
      "four",
      "five",
      "six",
      "seven",
      "eight",
      "nine",
      "ten",
      "eleven",
      "twelve",
      "thirteen",
      "fourteen",
      "fifteen",
      "sixteen",
      "seventeen",
      "eighteen",
      "nineteen"
    };

    public static final String[] tens = {
      "", // 0
      "", // 1
      "twenty", // 2
      "thirty", // 3
      "forty", // 4
      "fifty", // 5
      "sixty", // 6
      "seventy", // 7
      "eighty", // 8
      "ninety" // 9
    };

    public static String convert(final int n) {
      //      System.out.println("n = " + n);

      if (n == 0) {
        return "zero";
      }

      if (n < 0) {
        return "minus " + convert(-n);
      }

      if (n < 20) {
        return units[n];
      }

      if (n < 100) {
        return tens[n / 10] + ((n % 10 != 0) ? " " : "") + units[n % 10];
      }

      if (n < 1000) {
        return units[n / 100] + " hundred" + ((n % 100 != 0) ? " " : "") + convert(n % 100);
      }

      if (n < 1000000) {
        return convert(n / 1000) + " thousand" + ((n % 1000 != 0) ? " " : "") + convert(n % 1000);
      }

      if (n < 1000000000) {
        return convert(n / 1000000)
            + " million"
            + ((n % 1000000 != 0) ? " " : "")
            + convert(n % 1000000);
      }

      return convert(n / 1000000000)
          + " billion"
          + ((n % 1000000000 != 0) ? " " : "")
          + convert(n % 1000000000);
    }

    //    public static void main(final String[] args) {
    //      final Random generator = new Random();
    //
    //      int n;
    //      for (int i = 0; i < 20; i++) {
    //        n = generator.nextInt(Integer.MAX_VALUE);
    //
    //        System.out.printf("%10d = '%s'%n", n, convert(n));
    //      }
    //
    //      n = 1000;
    //      System.out.printf("%10d = '%s'%n", n, convert(n));
    //
    //      n = 2000;
    //      System.out.printf("%10d = '%s'%n", n, convert(n));
    //
    //      n = 10000;
    //      System.out.printf("%10d = '%s'%n", n, convert(n));
    //
    //      n = 11000;
    //      System.out.printf("%10d = '%s'%n", n, convert(n));
    //
    //      n = 999999999;
    //      System.out.printf("%10d = '%s'%n", n, convert(n));
    //
    //      n = Integer.MAX_VALUE;
    //      System.out.printf("%10d = '%s'%n", n, convert(n));
    //    }
  }
}
