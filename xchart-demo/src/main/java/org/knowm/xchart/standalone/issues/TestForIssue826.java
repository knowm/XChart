package org.knowm.xchart.standalone.issues;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.SwingWrapper;

public class TestForIssue826 {

  public static void main(String[] args) throws ParseException {

    CategoryChart chart = getChart();
    new SwingWrapper(chart).displayChart();
  }

  public static CategoryChart getChart() {
    final CategoryChart chart =
        new CategoryChartBuilder().width(600).height(400).xAxisTitle("X").yAxisTitle("Y").build();

    ArrayList<String> years =
        new ArrayList<String>(
            Arrays.asList(
                new String[] {
                  "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020", "2021",
                  "2022"
                }));

    ArrayList<Number> yAData =
        new ArrayList<Number>(
            Arrays.asList(
                new Number[] {
                  4438887, 4365843, 4050498, 4757380, 4429130, 4692889, 4354087, 4530343, 4572770,
                  4150489, 4487793
                }));

    ArrayList<Number> yBData =
        new ArrayList<Number>(
            Arrays.asList(
                new Number[] {
                  3198714, 3144079, 2859215, 3430605, 3839149, 4042579, 3741823, 3890162, 3731367,
                  3751216, 4008249
                }));

    chart.getStyler().setOverlapped(true);
    chart.getStyler().setYAxisDecimalPattern("###,###.##");

    chart.addSeries("A", years, yAData);
    chart.addSeries("B", years, yBData);

    //    chart.getStyler().setYAxisMin(2600000.0);
    chart.setTitle(Double.toString(2600000.0));

    return chart;
  }
}
