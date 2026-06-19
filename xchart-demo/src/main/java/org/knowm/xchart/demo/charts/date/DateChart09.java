package org.knowm.xchart.demo.charts.date;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XChartPanel;
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
    SwingWrapper<XYChart> wrapper = new SwingWrapper<>(chart);
    wrapper.displayChart();
    wrapper.getXChartPanel().setCursorEnabled(true);
  }

  @Override
  public XYChart getChart() {

    // Create Chart
    XYChart chart =
        new XYChartBuilder().width(800).height(600).title(getClass().getSimpleName()).build();

    // Customize Chart
    chart.getStyler().setLegendVisible(false);
    chart.getStyler().setXAxisLabelRotation(90);

    // One data point per month so each month gets a single, non-repeating tick label.
    // X values are day-of-year offsets from startTime; Y values are arbitrary sample data.
    LocalDateTime startTime = LocalDateTime.of(2001, Month.JANUARY, 1, 0, 0, 0);
    Random random = new Random();

    List<Integer> xData = new ArrayList<>();
    List<Double> yData = new ArrayList<>();
    for (int month = 0; month < 12; month++) {
      xData.add((int) ChronoUnit.DAYS.between(startTime, startTime.plusMonths(month)));
      yData.add(random.nextDouble());
    }

    chart.addSeries("Monthly Value", xData, yData);

    // set custom X-Axis tick labels
    DateTimeFormatter xTickFormatter = DateTimeFormatter.ofPattern("LLL");
    chart
        .getStyler()
        .setXAxisTickLabelsFormattingFunction(
            x -> startTime.plusDays(x.longValue()).format(xTickFormatter));

    // set custom cursor tool tip text
    DateTimeFormatter cursorXFormatter = DateTimeFormatter.ofPattern("LLL dd");
    chart
        .getStyler()
        .setCustomCursorXDataFormattingFunction(
            x -> startTime.plusDays(x.longValue()).format(cursorXFormatter));

    return chart;
  }

  @Override
  public void customizePanel(XChartPanel<XYChart> panel) {

    panel.setCursorEnabled(true);
  }

  @Override
  public String getExampleChartName() {

    return getClass().getSimpleName() + " - Custom Date Formatter Without Years";
  }
}
