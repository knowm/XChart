package org.knowm.xchart;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.junit.jupiter.api.Test;

/** Issue #589: annotations must be able to resolve their Y value against a secondary Y-Axis. */
public class AnnotationYAxisGroupTest {

  /** Group 0 spans 0..10, group 1 spans 0..1000, so the same value lands in different places. */
  private XYChart buildTwoAxisChart() {

    XYChart chart = new XYChartBuilder().width(800).height(600).build();
    chart.addSeries("small", new double[] {0.0, 1.0, 2.0}, new double[] {0.0, 5.0, 10.0});
    XYSeries big =
        chart.addSeries("big", new double[] {0.0, 1.0, 2.0}, new double[] {0.0, 500.0, 1000.0});
    big.setYAxisGroup(1);
    chart.setYAxisGroupTitle(1, "big");
    return chart;
  }

  private void render(XYChart chart) throws IOException {

    OutputStream output = new ByteArrayOutputStream();
    ChartEncoder.saveChart(chart, output, "png");
  }

  @Test
  public void annotationLineHonorsYAxisGroup() throws IOException {

    // given
    XYChart chart = buildTwoAxisChart();
    AnnotationLine onPrimary = new AnnotationLine(5.0, false, false);
    AnnotationLine onSecondary = new AnnotationLine(5.0, false, false);
    onSecondary.setYAxisGroup(1);
    chart.addAnnotation(onPrimary);
    chart.addAnnotation(onSecondary);

    // when
    render(chart);

    // test - the value 5.0 is mid-range on group 0 but near the bottom on group 1
    assertEquals(
        (int) chart.getScreenYFromChart(5.0, 0), (int) onPrimary.getBounds().getY(), 1.0);
    assertEquals(
        (int) chart.getScreenYFromChart(5.0, 1), (int) onSecondary.getBounds().getY(), 1.0);
    assertNotEquals(onPrimary.getBounds().getY(), onSecondary.getBounds().getY());
  }

  @Test
  public void annotationTextHonorsYAxisGroup() throws IOException {

    // given
    XYChart chart = buildTwoAxisChart();
    // same text, so any X difference would come from the axis lookup, not glyph widths
    AnnotationText onPrimary = new AnnotationText("mark", 1.0, 5.0, false);
    AnnotationText onSecondary = new AnnotationText("mark", 1.0, 5.0, false);
    onSecondary.setYAxisGroup(1);
    chart.addAnnotation(onPrimary);
    chart.addAnnotation(onSecondary);

    // when
    render(chart);

    // test
    assertEquals(onPrimary.getBounds().getX(), onSecondary.getBounds().getX(), 0.001);
    assertNotEquals(onPrimary.getBounds().getY(), onSecondary.getBounds().getY());
  }

  @Test
  public void defaultsToPrimaryAxis() throws IOException {

    // given
    XYChart chart = buildTwoAxisChart();
    AnnotationLine defaulted = new AnnotationLine(5.0, false, false);
    AnnotationLine explicitPrimary = new AnnotationLine(5.0, false, false);
    explicitPrimary.setYAxisGroup(0);
    chart.addAnnotation(defaulted);
    chart.addAnnotation(explicitPrimary);

    // when
    render(chart);

    // test
    assertEquals(0, defaulted.getYAxisGroup());
    assertEquals(explicitPrimary.getBounds().getY(), defaulted.getBounds().getY(), 0.001);
  }

  @Test
  public void unknownGroupFallsBackToPrimaryAxis() throws IOException {

    // given
    XYChart chart = buildTwoAxisChart();
    AnnotationLine onPrimary = new AnnotationLine(5.0, false, false);
    AnnotationLine onMissingGroup = new AnnotationLine(5.0, false, false);
    onMissingGroup.setYAxisGroup(7); // no series uses group 7
    chart.addAnnotation(onPrimary);
    chart.addAnnotation(onMissingGroup);

    // when
    render(chart);

    // test - no NPE, and it lands on the primary axis
    assertEquals(onPrimary.getBounds().getY(), onMissingGroup.getBounds().getY(), 0.001);
  }

  @Test
  public void settersChainInEitherOrder() throws IOException {

    // given - base-class setters before and after subclass-specific ones
    XYChart chart = buildTwoAxisChart();
    AnnotationLine a = new AnnotationLine(0.0, false, false).setYAxisGroup(1).setValue(5.0);
    AnnotationLine b = new AnnotationLine(0.0, false, false).setValue(5.0).setYAxisGroup(1);
    AnnotationText c = new AnnotationText("x", 1.0, 0.0, false).setVisible(true).setY(5.0);
    chart.addAnnotation(a);
    chart.addAnnotation(b);
    chart.addAnnotation(c);

    // when
    render(chart);

    // test
    assertEquals(a.getBounds().getY(), b.getBounds().getY(), 0.001);
    assertEquals(1, a.getYAxisGroup());
    assertEquals(1, b.getYAxisGroup());
  }
}
