package org.knowm.xchart.standalone.issues;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.Histogram;
import org.knowm.xchart.SwingWrapper;

public class TestForIssue205 {

  public static void main(String[] args) throws IOException {

    List<Double> myData = new ArrayList();
    myData.add(10.0);
    myData.add(20.0);
    myData.add(10.0);
    myData.add(30.0);
    myData.add(40.0);
    myData.add(20.0);
    myData.add(30.0);
    myData.add(10.0);
    myData.add(40.0);
    myData.add(50.0);
    myData.add(10.0);
    myData.add(10.0);

    int numBins = 3;
    Histogram histogram = new Histogram(myData, numBins);
    CategoryChart chart =
        new CategoryChartBuilder()
            .width(800)
            .height(600)
            .title("Xchart Histogram")
            .xAxisTitle("Mean")
            .yAxisTitle("Count")
            .build();
    chart.getStyler().setAvailableSpaceFill(.96);
    chart.getStyler().setOverlapped(false);
    chart.addSeries("histogram ", histogram.getxAxisData(), histogram.getyAxisData());
    //    BitmapEncoder.saveBitmap(chart, "\\MyHistogram", BitmapFormat.JPG);
    new SwingWrapper<CategoryChart>(chart).displayChart();

    System.out.println(
        "Bins :" + histogram.getxAxisData()); // Bins :[16.666666666666668, 30.000000000000004,
    // 43.333333333333336]
    System.out.println("frequency :" + histogram.getyAxisData()); // frequency :[7.0, 2.0, 3.0]
  }
}
