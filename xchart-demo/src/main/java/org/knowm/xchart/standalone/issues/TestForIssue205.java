package org.knowm.xchart.standalone.issues;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.Histogram;
import org.knowm.xchart.SwingWrapper;

public class TestForIssue205 {

  public static CategoryChart getChart() {

    List<Double> myData = new ArrayList<>();
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
    return chart;
  }

  public static void main(String[] args) throws IOException {

    new SwingWrapper<CategoryChart>(getChart()).displayChart();
  }
}
