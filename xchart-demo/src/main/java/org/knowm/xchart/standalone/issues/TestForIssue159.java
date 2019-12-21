package org.knowm.xchart.standalone.issues;

import java.awt.Color;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.XYStyler;
import org.knowm.xchart.style.colors.XChartSeriesColors;
import org.knowm.xchart.style.lines.SeriesLines;
import org.knowm.xchart.style.markers.SeriesMarkers;

public class TestForIssue159 {

  public static void main(String[] args) throws Exception {

    XYChart chart = new XYChartBuilder().width(800).height(600).title("LineChart").xAxisTitle("X").yAxisTitle("Y").build();

    XYStyler styler = chart.getStyler();
    styler.setChartTitleVisible(false);
    styler.setLegendVisible(false);
//    styler.chartBackgroundColor = Color.white;
//
//    styler.axisTicksLineVisible = false;
//    styler.plotGridVerticalLinesVisible = false;

    styler.setXAxisTitleVisible(false);
    styler.setYAxisTitleVisible(false);

    styler.setAxisTickLabelsColor(Color.darkGray);
    styler.setDatePattern("MM-dd");
    styler.setYAxisMin(1.0);
    styler.setYAxisMax(10.0);
    styler.setXAxisTickMarkSpacingHint(200);

    List<Date> xData = new ArrayList<Date>();
    List<Integer> yData = new ArrayList<Integer>();

    yData.add(1);
    yData.add(2);
    yData.add(4);
    yData.add(8);
    yData.add(10);

    LocalDate localDate = LocalDate.now();
    xData.add(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
    xData.add(Date.from(localDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
    xData.add(Date.from(localDate.plusDays(2).atStartOfDay(ZoneId.systemDefault()).toInstant()));
    xData.add(Date.from(localDate.plusDays(3).atStartOfDay(ZoneId.systemDefault()).toInstant()));
    xData.add(Date.from(localDate.plusDays(4).atStartOfDay(ZoneId.systemDefault()).toInstant()));


// Series
    XYSeries series = chart.addSeries("My Data", xData, yData);
    series.setLineColor(XChartSeriesColors.RED);
    series.setMarkerColor(Color.RED);
    series.setMarker(SeriesMarkers.CIRCLE);
    series.setLineStyle(SeriesLines.SOLID);

    new SwingWrapper(chart).displayChart();
  }
}
