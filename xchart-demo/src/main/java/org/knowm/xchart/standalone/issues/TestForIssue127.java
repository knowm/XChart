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

/** @author timmolter */
public class TestForIssue127 {

  public static void main(String[] args) throws InterruptedException, ParseException {

    int[] x = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
    int[] y = new int[] {1, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1};
    // int[] x = new int[] { 0, 1, 2, 3, 4, 5, 6, 7 };
    // int[] y = new int[] { 1, 0, 1, 0, 1, 0, 0, 0 };

    XYChart chart =
        new XYChartBuilder().width(640).height(480).xAxisTitle("x").yAxisTitle("y").build();
    chart.setTitle("TEst");
    chart.getStyler().setLegendVisible(false);
    new SwingWrapper(chart).displayChart();
    Thread.sleep(1000);

    chart.addSeries("test", x, y);
    new SwingWrapper(chart).displayChart();
    Thread.sleep(1000);

    chart.removeSeries("test");
    new SwingWrapper(chart).displayChart();
    Thread.sleep(1000);

    DateFormat sdf = new SimpleDateFormat("dd-HH-mm");

    List<Date> xDate = new ArrayList<Date>();
    xDate.add(sdf.parse("25-01-00"));
    xDate.add(sdf.parse("25-02-00"));
    xDate.add(sdf.parse("25-03-00"));
    List<Double> yDate = new ArrayList<Double>();
    yDate.add(2d);
    yDate.add(3d);
    yDate.add(5d);
    chart.addSeries("test2", xDate, yDate);
    new SwingWrapper(chart).displayChart();
    Thread.sleep(1000);
  }
}
