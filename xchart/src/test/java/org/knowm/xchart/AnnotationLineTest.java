package org.knowm.xchart;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.junit.Assert;
import org.junit.Test;

public class AnnotationLineTest {

  @Test
  public void annotationLineShouldNotSpanOverWholeWidth() throws IOException {
    // given
    double[] xData = new double[] {0.0, 1.0, 2.0};
    double[] yData = new double[] {2.0, 1.0, 0.0};
    XYChart chart = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", xData, yData);

    // when
    AnnotationLine annotation = new AnnotationLine(0.5d, false, false);
    chart.addAnnotation(annotation);
    OutputStream output = new ByteArrayOutputStream();
    BitmapEncoder.saveBitmap(chart, output, BitmapEncoder.BitmapFormat.PNG);

    // test
    Assert.assertTrue(annotation.getBounds().getX() > 50);
    Assert.assertTrue(annotation.getBounds().getWidth() < 500);
  }
}
