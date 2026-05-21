package org.knowm.xchart.standalone.issues;

import java.awt.Color;
import java.io.IOException;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.style.Styler;

public class TestForIssue488 {

  public static PieChart getChart() {

    PieChart chart =
        new PieChartBuilder().width(200).height(200).theme(Styler.ChartTheme.GGPlot2).build();

    chart.getStyler().setLegendVisible(false);

    Color[] sliceColors =
        new Color[] {
          new Color(238, 88, 88), // red
          new Color(213, 122, 212), // purple
          new Color(106, 230, 182), // mentol
          new Color(88, 155, 238) // baby blue
        };

    chart.getStyler().setSeriesColors(sliceColors);
    chart.getStyler().setChartTitleBoxVisible(false);
    chart.getStyler().setLabelsVisible(false);
    chart.getStyler().setChartBackgroundColor(new Color(255, 255, 255));
    chart.getStyler().setPlotBackgroundColor(new Color(255, 255, 255));
    chart.getStyler().setPlotBorderVisible(false);

    chart.addSeries("laptop", 65);
    chart.addSeries("pc", 10);
    chart.addSeries("tel", 12.5);
    chart.addSeries("winda", 12.5);
    return chart;
  }

  public static void main(String[] args) throws IOException {

    BitmapEncoder.saveBitmap(getChart(), "./Sample_Chart", BitmapEncoder.BitmapFormat.PNG);
  }
}