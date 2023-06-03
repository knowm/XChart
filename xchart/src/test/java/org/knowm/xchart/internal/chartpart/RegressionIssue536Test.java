package org.knowm.xchart.internal.chartpart;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import org.junit.jupiter.api.Test;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.markers.SeriesMarkers;

/** Regression test for <a href="https://github.com/knowm/XChart/issues/536">issue 536</a>. */
public class RegressionIssue536Test {

  @Test
  public void issue536RegressionTest() throws Exception {

    String series = "ABC";

    List<Date> x = new ArrayList<>(); // List of dates
    List<Double> y = new ArrayList<>();

    XYChart chart =
        new XYChartBuilder()
            .width(800)
            .height(720)
            .theme(Styler.ChartTheme.XChart)
            .title("XChart")
            .xAxisTitle("Date")
            .yAxisTitle("%Diff ")
            .build();
    //    chart.getStyler().setPlotBackgroundColor(java.awt.Color.BLACK);
    chart.getStyler().setPlotMargin(0);
    chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideE);
    chart.getStyler().setXAxisLabelRotation(90);
    chart.getStyler().setYAxisGroupPosition(1, Styler.YAxisPosition.Right);

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
    chart.getStyler().setTimezone(TimeZone.getTimeZone("UTC"));

    x.add(sdf.parse("2020-10-19")); // dates on X
    //    System.out.println("x.get(0).getTime() = " + x.get(0).getTime());
    //    System.out.println("sdf.parse(\"2020-10-19\") = " +
    // sdf.parse("2020-10-19").toGMTString());
    x.add(sdf.parse("2020-10-20"));
    x.add(sdf.parse("2020-10-21"));
    x.add(sdf.parse("2020-10-22"));
    x.add(sdf.parse("2020-10-23"));
    x.add(sdf.parse("2020-10-24"));
    x.add(sdf.parse("2020-10-25"));
    x.add(sdf.parse("2020-10-26"));
    x.add(sdf.parse("2020-10-27"));
    x.add(sdf.parse("2020-10-28"));

    y.add(2.1);
    y.add(4.5);
    y.add(3.2);
    y.add(5.6);
    y.add(2.5);
    y.add(3.8);
    y.add(5.1);
    y.add(7.4);
    y.add(4.8);
    y.add(2.7);

    XYSeries xyseries = chart.addSeries(series, x, y);
    xyseries.setMarker(SeriesMarkers.NONE);
    xyseries.setYAxisGroup(1);
    byte[] bytes = BitmapEncoder.getBitmapBytes(chart, BitmapEncoder.BitmapFormat.PNG);

    List<String> tickLabels = chart.axisPair.getXAxis().getAxisTickCalculator().getTickLabels();

    assertThat(tickLabels.size()).isEqualTo(13);
    assertThat(tickLabels.get(0)).isEqualTo("10-18");
    boolean areAllLabelsUnique =
        chart.axisPair.getXAxis().getAxisTickCalculator().areAllTickLabelsUnique(tickLabels);
    assertThat(areAllLabelsUnique).isEqualTo(true);
  }
}
