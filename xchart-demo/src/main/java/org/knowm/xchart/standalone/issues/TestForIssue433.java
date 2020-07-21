package org.knowm.xchart.standalone.issues;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

public class TestForIssue433 {

  public static void main(String[] args) throws ParseException {
    DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    List<Date> xData = new ArrayList<>();
    xData.add(sdf.parse("1970-01-01 15:29:38"));
    xData.add(sdf.parse("1970-01-01 15:29:39"));
    xData.add(sdf.parse("1970-01-01 15:29:40"));
    xData.add(sdf.parse("1970-01-01 15:29:41"));

    List<Double> yData = new ArrayList<>();
    yData.add(3.0);
    yData.add(33.0);
    yData.add(19.0);
    yData.add(8.0);

    // Create Chart
    XYChart chart =
        new XYChartBuilder()
            .width(1400)
            .height(200)
            .title("TestForIssue 433 and 428")
            .xAxisTitle("x")
            .yAxisTitle("y")
            .build();
    chart.addSeries("test", xData, yData);

    chart.getStyler().setDatePattern("HH:mm:ss");
    chart.getStyler().setXAxisTickMarkSpacingHint(chart.getWidth() / (xData.size() - 1));

    new SwingWrapper<>(chart).displayChart();
  }
}
