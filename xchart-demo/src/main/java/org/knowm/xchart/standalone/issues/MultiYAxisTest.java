package org.knowm.xchart.standalone.issues;

import java.util.LinkedList;
import java.util.List;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.markers.SeriesMarkers;

public class MultiYAxisTest {

  public static void main(String[] args) throws Exception {

    List<Double> timeData = new LinkedList<Double>();
    List<Double> th1Data = new LinkedList<Double>();
    List<Double> th2Data = new LinkedList<Double>();

    // Generate data
    for (int i = 0; i < 20; i++) {
      timeData.add(2.5 * i);
      th1Data.add(10.1 * i);
      th2Data.add((1.1 * i) * (1.1 * i));
    }
    XYChart c = new XYChartBuilder().title("Test Data").xAxisTitle("Time").build();
    c.setYAxisGroupTitle(0, "A");
    c.setYAxisGroupTitle(1, "B");
    c.getStyler().setYAxisGroupPosition(1, Styler.YAxisPosition.Right);
    c.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);

    // series 1
    XYSeries s1 = c.addSeries("th1", timeData, th1Data);
    s1.setYAxisGroup(0);
    s1.setMarker(SeriesMarkers.NONE);

    // series 2
    XYSeries s2 = c.addSeries("th2", timeData, th2Data);
    s2.setYAxisGroup(1);
    s2.setMarker(SeriesMarkers.NONE);

    new SwingWrapper<XYChart>(c).displayChart();
  }
}
