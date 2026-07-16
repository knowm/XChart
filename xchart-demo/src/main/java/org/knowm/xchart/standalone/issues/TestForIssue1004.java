package org.knowm.xchart.standalone.issues;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import org.knowm.xchart.AnnotationImage;
import org.knowm.xchart.AnnotationLine;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

/**
 * Issue #1004 — {@link AnnotationImage} screen-space placement centers a NON-square image
 * vertically using the image WIDTH instead of its HEIGHT.
 *
 * <p>A tall image (here 40&nbsp;wide &times; 140&nbsp;tall) is placed with {@code
 * isValueInScreenSpace = true} at a fixed screen point. A crosshair (two screen-space {@link
 * AnnotationLine}s) marks that exact point, and the image itself has a red line drawn across its own
 * true vertical center.
 *
 * <p><b>Before the fix:</b> the image's red center line sits {@code (width - height) / 2 = 50px}
 * BELOW the crosshair intersection — the image is not centered on the target point.
 *
 * <p><b>After the fix:</b> the image's red center line lines up with the crosshair.
 *
 * <p>Square images are unaffected, which is why this went unnoticed.
 */
public class TestForIssue1004 {

  private static final int IMG_W = 40;
  private static final int IMG_H = 140;

  /** Screen-space target point (pixels; y measured from the BOTTOM, per AnnotationImage). */
  private static final int TARGET_X = 400;
  private static final int TARGET_Y = 300;

  /** A tall, non-square image with a clearly marked vertical center. */
  private static BufferedImage makeTallImage() {

    BufferedImage img = new BufferedImage(IMG_W, IMG_H, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = img.createGraphics();
    g.setColor(new Color(0, 120, 215, 160));
    g.fillRect(0, 0, IMG_W, IMG_H);
    g.setColor(Color.DARK_GRAY);
    g.drawRect(0, 0, IMG_W - 1, IMG_H - 1);
    // red line across the image's TRUE vertical center
    g.setColor(Color.RED);
    g.setStroke(new BasicStroke(2f));
    g.drawLine(0, IMG_H / 2, IMG_W, IMG_H / 2);
    g.dispose();
    return img;
  }

  public static XYChart getChart() {

    XYChart chart = new XYChart(900, 600);
    chart.setTitle("Issue #1004 — tall image should be centered on the crosshair");

    chart.addSeries("series", new double[] {0, 1, 2, 3}, new double[] {0, 1, 4, 9});

    // crosshair marking the intended center of the image (screen space)
    chart.addAnnotation(new AnnotationLine(TARGET_X, true, true)); // vertical line at x=TARGET_X
    chart.addAnnotation(new AnnotationLine(TARGET_Y, false, true)); // horizontal line at y=TARGET_Y

    // the tall image, placed in screen space at the same point
    chart.addAnnotation(new AnnotationImage(makeTallImage(), TARGET_X, TARGET_Y, true));

    return chart;
  }

  public static void main(String[] args) {
    new SwingWrapper<>(getChart()).displayChart();
  }
}
