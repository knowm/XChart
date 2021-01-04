package org.knowm.xchart.demo.charts.date;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.demo.charts.ExampleChart;

/**
 * Year scale
 *
 * <p>Demonstrates the following:
 *
 * <ul>
 *   <li>Rotated 90 degrees X-Axis labels
 *   <li>Setting custom X-Axis tick labels
 *   <li>Setting custom cursor tool tip text
 */
public class DateChart09 implements ExampleChart<XYChart> {

  public static void main(String[] args) {

    ExampleChart<XYChart> exampleChart = new DateChart09();
    XYChart chart = exampleChart.getChart();
    new SwingWrapper<>(chart).displayChart();
  }

  @Override
  public XYChart getChart() {

    // Create Chart
    XYChart chart =
        new XYChartBuilder().width(800).height(600).title(getClass().getSimpleName()).build();

    // Customize Chart
    chart.getStyler().setLegendVisible(false);
    chart.getStyler().setXAxisLabelRotation(90);

    // Series
    List<Integer> xData = IntStream.range(0, 365).boxed().collect(Collectors.toList());
    Random random = new Random();

    List<Double> yData =
        IntStream.range(0, xData.size())
            .mapToDouble(x -> random.nextDouble())
            .boxed()
            .collect(Collectors.toList());

    chart.addSeries("blah", xData, yData);

    // set custom X-Axis tick labels
    LocalDateTime startTime = LocalDateTime.of(2001, Month.JANUARY, 1, 0, 0, 0);
    DateTimeFormatter xTickFormatter = DateTimeFormatter.ofPattern("LLL");
    chart
        .getStyler()
        .setxAxisTickLabelsFormattingFunction(
            x -> startTime.plusDays(x.longValue()).format(xTickFormatter));

    // set custom cursor tool tip text
    chart.getStyler().setCursorEnabled(true);
    DateTimeFormatter cursorXFormatter = DateTimeFormatter.ofPattern("LLL dd");
    chart
        .getStyler()
        .setCustomCursorXDataFormattingFunction(
            x -> startTime.plusDays(x.longValue()).format(cursorXFormatter));

    return chart;
  }

  @Override
  public String getExampleChartName() {

    return getClass().getSimpleName() + " - Custom Date Formatter Without Years";
  }
}
