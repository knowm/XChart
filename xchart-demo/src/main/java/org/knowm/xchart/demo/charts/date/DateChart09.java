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
 *   <li>Rotated X-Axis labels
 *   <li>Setting a custom date formatter String
 */
public class DateChart09 implements ExampleChart<XYChart> {

  private static final LocalDateTime BEGINNING_OF_THE_YEAR =
      LocalDateTime.of(2011, Month.JANUARY, 1, 0, 0, 0, 0);

  public static void main(String[] args) {

    ExampleChart<XYChart> exampleChart = new DateChart09();
    XYChart chart = exampleChart.getChart();
    new SwingWrapper<XYChart>(chart).displayChart();
  }

  @Override
  public XYChart getChart() {

    // Create Chart
    XYChart chart =
        new XYChartBuilder()
            .width(800)
            .height(600)
            .title("Custom Date Formatter Without Years")
            .build();

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

    LocalDateTime startTime = LocalDateTime.of(2001, Month.JANUARY, 1, 0, 0, 0);
    DateTimeFormatter xTickFormatter = DateTimeFormatter.ofPattern("LLL");
    DateTimeFormatter cursorXFormatter = DateTimeFormatter.ofPattern("LLL dd");
    chart.setCustomXAxisTickLabelsFormatter(
        x -> startTime.plusDays(x.longValue()).format(xTickFormatter));
    chart.getStyler().setCursorEnabled(true);
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
