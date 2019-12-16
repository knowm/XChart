package org.knowm.xchart.standalone.issues;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.knowm.xchart.PdfboxGraphicsEncoder;
import org.knowm.xchart.demo.charts.area.AreaChart01;
import org.knowm.xchart.demo.charts.area.AreaChart02;
import org.knowm.xchart.demo.charts.line.LineChart01;
import org.knowm.xchart.demo.charts.line.LineChart02;
import org.knowm.xchart.demo.charts.pie.PieChart01;
import org.knowm.xchart.demo.charts.pie.PieChart02;
import org.knowm.xchart.internal.chartpart.Chart;

public class TestForIssue285 {

  public static void main(String[] args) throws IOException {

    List<Chart> charts = new ArrayList<>();
    charts.add(new AreaChart01().getChart());
    charts.add(new AreaChart02().getChart());
    charts.add(new LineChart01().getChart());
    charts.add(new LineChart02().getChart());
    charts.add(new PieChart01().getChart());
    charts.add(new PieChart02().getChart());

    PdfboxGraphicsEncoder.savePdfboxGraphics(charts, "./Multiple_Charts");
  }
}
