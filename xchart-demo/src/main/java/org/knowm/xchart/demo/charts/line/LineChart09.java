package org.knowm.xchart.demo.charts.line;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.Styler.CardinalPosition;

/**
 * Cursor
 *
 * <p>Demonstrates the following:
 *
 * <ul>
 *   <li>Cursor
 *   <li>Setting custom cursor tool tip text
 *   <li>Building a Chart with ChartBuilder
 */
public class LineChart09 implements ExampleChart<XYChart> {

  public static void main(String[] args) {

    ExampleChart<XYChart> exampleChart = new LineChart09();
    XYChart chart = exampleChart.getChart();
    new SwingWrapper<>(chart).displayChart();
  }

  @Override
  public XYChart getChart() {

    // Create Chart
    XYChart chart =
        new XYChartBuilder()
            .width(800)
            .height(600)
            .title(getClass().getSimpleName())
            .xAxisTitle("X")
            .yAxisTitle("Y")
            .build();

    // Customize Chart
    chart.getStyler().setLegendPosition(CardinalPosition.OutsideE);
    chart.getStyler().setAxisTitlesVisible(false);
    chart.getStyler().setLegendPosition(CardinalPosition.OutsideS);
    chart.getStyler().setLegendLayout(Styler.LegendLayout.Horizontal);

    chart.getStyler().setCursorEnabled(true);
    //    chart.getStyler().setCursorColor(Color.GREEN);
    //    chart.getStyler().setCursorLineWidth(30f);
    //    chart.getStyler().setCursorFont(new Font("Verdana", Font.BOLD, 12));
    //    chart.getStyler().setCursorFontColor(Color.ORANGE);
    //    chart.getStyler().setCursorBackgroundColor(Color.BLUE);
    //    chart.getStyler().setCustomCursorXDataFormattingFunction(x -> "hello xvalue: " + x);
    //    chart
    //        .getStyler()
    //        .setCustomCursorYDataFormattingFunction(y -> "hello yvalue divided by 2: " + y / 2);

    // Series
    chart.addSeries("a", new double[] {0, 3, 5, 7, 9}, new double[] {-3, 5, 9, 6, 5});
    chart.addSeries("b", new double[] {0, 2.7, 4.8, 6, 9}, new double[] {-1, 6, 4, 0, 4});
    chart.addSeries("c", new double[] {0, 1.5, 5, 8, 9}, new double[] {-2, -1, 1, 0, 1});

    return chart;
  }

  @Override
  public String getExampleChartName() {

    return getClass().getSimpleName() + " - Cursor";
  }
}
