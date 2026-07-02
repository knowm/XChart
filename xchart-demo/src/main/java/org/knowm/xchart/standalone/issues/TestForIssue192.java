package org.knowm.xchart.standalone.issues;

import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.PieStyler.LabelType;

/**
 * Reproduces issue #192: show all labels and percentages on a pie chart.
 *
 * <p>By default, XChart only draws a slice's label if it algorithmically determines the text will
 * "fit" the slice, so very small (or very large) slices end up with no label. Calling {@code
 * setForceAllLabelsVisible(true)} overrides that check and draws a label for every slice.
 *
 * <p>The data below mixes tiny slices (which would normally be dropped) with a dominant slice to
 * demonstrate the fix. Compare {@link #getChartDefault()} (labels dropped) with {@link
 * #getChartForced()} (all labels shown).
 */
public class TestForIssue192 {

  public static void main(String[] args) {

    // Default behavior: tiny/huge slice labels are dropped.
    new SwingWrapper<>(getChartDefault()).displayChart();
    // Fix: force all labels visible, pushed outside the pie so they don't overlap.
    new SwingWrapper<>(getChartForced()).displayChart();
  }

  public static PieChart getChartDefault() {

    PieChart chart =
        new PieChartBuilder()
            .width(700)
            .height(500)
            .title("Issue 192 - Default (some labels dropped)")
            .build();

    chart.getStyler().setLabelType(LabelType.NameAndPercentage);

    addSeries(chart);
    return chart;
  }

  public static PieChart getChartForced() {

    PieChart chart =
        new PieChartBuilder()
            .width(700)
            .height(500)
            .title("Issue 192 - forceAllLabelsVisible=true")
            .build();

    chart.getStyler().setLabelType(LabelType.NameAndPercentage);
    // The fix: always draw a label for every slice, even tiny/huge ones.
    chart.getStyler().setForceAllLabelsVisible(true);
    // Push labels outside the pie with leader lines so they don't overlap on small slices.
    chart.getStyler().setLabelsDistance(1.15);
    // When labelsDistance > 1.0, shrink the pie to leave room for the outside labels.
    chart.getStyler().setPlotContentSize(.7);

    addSeries(chart);
    return chart;
  }

  private static void addSeries(PieChart chart) {

    chart.addSeries("Dominant", 90);
    chart.addSeries("Tiny A", 1);
    chart.addSeries("Tiny B", 2);
    chart.addSeries("Small C", 3);
    chart.addSeries("Small D", 4);
  }
}
