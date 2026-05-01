package org.knowm.xchart.standalone.issues;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.markers.SeriesMarkers;

/**
 * Demonstrates merged Y-axes: two series with very different value ranges (1–5 on the left and
 * 100–500 on the left) share a single axis line. Two further series on the right side similarly
 * share one axis line.
 *
 * <p>Left axis: scale A (0–5) + scale B (100–500) — merged via {@code mergeYAxisGroups(0, 1)}.
 * Right axis: scale C (0–1000) + scale D (0.01–0.05) — merged via {@code mergeYAxisGroups(2, 3)}.
 */
public class MergedYAxisDemo {

  public static void main(String[] args) {

    // ---- data ----
    List<Double> x = new LinkedList<>();
    List<Double> scaleA = new LinkedList<>();
    List<Double> scaleB = new LinkedList<>();
    List<Double> scaleC = new LinkedList<>();
    List<Double> scaleD = new LinkedList<>();

    for (int i = 1; i <= 10; i++) {
      x.add((double) i);
      scaleA.add((double) i * 0.5);           // range ~0.5 – 5
      scaleB.add((double) i * 50.0);          // range 50 – 500
      scaleC.add((double) i * 100.0);         // range 100 – 1000
      scaleD.add(i * 0.005);                   // range 0.005 – 0.05
    }

    // ---- chart ----
    XYChart chart =
        new XYChartBuilder()
            .title("Merged Y-Axes Demo")
            .xAxisTitle("X")
            .width(800)
            .height(500)
            .build();

    // Series for left side (groups 0 and 1)
    XYSeries seriesA = chart.addSeries("Scale A (0–5)", x, scaleA);
    seriesA.setYAxisGroup(0);
    seriesA.setMarker(SeriesMarkers.CIRCLE);

    XYSeries seriesB = chart.addSeries("Scale B (50–500)", x, scaleB);
    seriesB.setYAxisGroup(1);
    seriesB.setMarker(SeriesMarkers.SQUARE);

    // Series for right side (groups 2 and 3)
    XYSeries seriesC = chart.addSeries("Scale C (100–1000)", x, scaleC);
    seriesC.setYAxisGroup(2);
    seriesC.setMarker(SeriesMarkers.DIAMOND);

    XYSeries seriesD = chart.addSeries("Scale D (0.005–0.05)", x, scaleD);
    seriesD.setYAxisGroup(3);
    seriesD.setMarker(SeriesMarkers.TRIANGLE_UP);

    // Position right-side axes
    chart.getStyler().setYAxisGroupPosition(2, Styler.YAxisPosition.Right);
    chart.getStyler().setYAxisGroupPosition(3, Styler.YAxisPosition.Right);

    // Axis titles
    chart.setYAxisGroupTitle(0, "A");
    chart.setYAxisGroupTitle(1, "B");
    chart.setYAxisGroupTitle(2, "C");
    chart.setYAxisGroupTitle(3, "D");

    // ---- KEY API: merge the axes ----
    chart.getStyler().mergeYAxisGroups(0, 1);  // groups 0 and 1 share one left axis line
    chart.getStyler().mergeYAxisGroups(2, 3);  // groups 2 and 3 share one right axis line

    // ---- Colocate slave labels on the master axis column (opt-in) ----
    // Slave labels (group 1 left, group 3 right) appear stacked below the master's labels
    // for each tick, rather than in a separate column.
    chart.getStyler().setMergedAxisColocateSlaveLabels(true);
    // Give slave axes a distinct color so they are easy to distinguish
    chart.getStyler().setYAxisGroupTickLabelsColorMap(1, new Color(200, 80, 0));   // left slave
    chart.getStyler().setYAxisGroupTickLabelsColorMap(3, new Color(200, 80, 0));   // right slave

    chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideSE);

    new SwingWrapper<>(chart).displayChart();
  }
}
