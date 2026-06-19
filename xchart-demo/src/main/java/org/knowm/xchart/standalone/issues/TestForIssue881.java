package org.knowm.xchart.standalone.issues;

import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.PieStyler.AnnotationType;

/**
 * Reproduces issue #881: AnnotationType and setAnnotationDistance removed since 3.8.1.
 *
 * <p>The deprecated shims {@code setAnnotationType(AnnotationType)} and {@code
 * setAnnotationDistance(double)} are restored and delegate to the new API ({@code
 * setLabelType(LabelType)} and {@code setLabelsDistance(double)}).
 */
public class TestForIssue881 {

  public static void main(String[] args) {

    new SwingWrapper<>(getChart()).displayChart();
  }

  @SuppressWarnings("deprecation")
  public static PieChart getChart() {

    PieChart chart =
        new PieChartBuilder().width(600).height(500).title("Issue 881 - AnnotationType shim").build();

    // Old API that broke after 3.8.1 — now restored as deprecated shims
    chart.getStyler().setAnnotationType(AnnotationType.LabelAndPercentage);
    chart.getStyler().setAnnotationDistance(1.15);
    chart.getStyler().setForceAllLabelsVisible(true);
    // When labelsDistance > 1.0, reduce plotContentSize to leave room for outside labels
    chart.getStyler().setPlotContentSize(.7);

    chart.addSeries("Java", 43);
    chart.addSeries("Python", 30);
    chart.addSeries("C++", 15);
    chart.addSeries("Other", 12);

    return chart;
  }
}
