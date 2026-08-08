package org.knowm.xchart.standalone.issues;

import java.util.Locale;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.Styler.LegendLayout;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.knowm.xchart.style.colors.ChartColor;

/**
 * Demonstrates issue #1024 — after upgrading from 3.8.8 to 4.0.4 the x-axis line disappeared on
 * charts with a {@code LegendPosition.OutsideS} legend.
 *
 * <p>{@code Axis_X.preparePaint()} subtracted the legend height from the top of the x-axis bounds
 * even though the y-axis (whose bottom defines the plot bottom) had already reserved that space, so
 * with an {@code OutsideS} legend the x-axis bounds started a legend-height <em>inside</em> the plot
 * area. That was harmless while the axis line was positioned from the painted tick label geometry,
 * but 4.0.4 anchors the line and tick marks to the top of the x-axis bounds — placing them under
 * the plot, which is painted afterwards and covers them. The light grey plot background from the
 * original report makes the missing black line obvious.
 *
 * <p>Run this and look below the plot: the x-axis line and tick marks sit plotMargin under the grey
 * plot area, exactly as in 3.8.8.
 */
public class TestForIssue1024 {

  public static void main(String[] args) {

    new SwingWrapper<>(getChart()).displayChart();
  }

  /** Constructs and returns the chart without launching a window (headless-safe). */
  public static XYChart getChart() {

    XYChart chart = new XYChartBuilder().width(261).height(268).build();

    // styler settings verbatim from the issue report
    chart.getStyler().setPlotGridLinesColor(ChartColor.WHITE.getColor());

    chart
        .getStyler()
        .setLocale(Locale.GERMANY)
        .setChartTitleVisible(true)
        .setPlotBorderVisible(false)
        .setLegendBorderColor(null)
        .setPlotBackgroundColor(ChartColor.LIGHT_GREY.getColor())
        .setChartBackgroundColor(ChartColor.WHITE.getColor())
        .setLegendPadding(5)
        .setLegendPosition(LegendPosition.OutsideS)
        .setLegendLayout(LegendLayout.Horizontal);

    // two points sloping from 2 down to 1, x around 30000 so the German locale renders the
    // "30.000" tick label seen in the report's screenshots
    chart.addSeries("0", new double[] {30000, 31000}, new double[] {2, 1});

    return chart;
  }
}
