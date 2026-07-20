package org.knowm.xchart.internal.chartpart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import org.junit.jupiter.api.Test;
import org.knowm.xchart.style.AxesChartStyler.TextAlignment;

// https://github.com/knowm/XChart/issues/618
// setYAxisLabelAlignment(TextAlignment.Right) did not right-align every Y axis tick label.
// AxisTickLabels measured each label by the ink width of its glyph outline but drew the label by
// translating to the computed x position and filling that same outline, whose ink starts at the
// left side bearing. The bearing was dropped from the measurement yet still honoured when drawing,
// so the rendered right edge landed at maxTickLabelWidth + bearing rather than at a constant x.
// Digit 1 has the largest bearing of any digit, so labels starting with 1 drifted right.
//
// Exercises the alignment maths directly (no XChartPanel / Swing display) so it runs headless.
class RegressionIssue618Test {

  private static final String[] LABELS = {
    "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14"
  };

  private static final double COLUMN_X = 17.0;
  private static final double TOLERANCE = 0.001;

  private final Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 30);
  private final FontRenderContext frc = frc();

  private static FontRenderContext frc() {

    Graphics2D g = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics();
    FontRenderContext frc = g.getFontRenderContext();
    g.dispose();
    return frc;
  }

  /** The ink right edge of a label as actually rendered, i.e. after the outline fill. */
  private double renderedRightEdge(String label, TextAlignment alignment) {

    double columnWidth = columnWidth();
    double xPos =
        TickLabelMetrics.alignedXPos(
            COLUMN_X, columnWidth, TickLabelMetrics.width(label, font, frc), alignment);
    Rectangle2D ink = new TextLayout(label, font, frc).getOutline(null).getBounds2D();
    return xPos + ink.getX() + ink.getWidth();
  }

  private double renderedLeftEdge(String label, TextAlignment alignment) {

    double columnWidth = columnWidth();
    double xPos =
        TickLabelMetrics.alignedXPos(
            COLUMN_X, columnWidth, TickLabelMetrics.width(label, font, frc), alignment);
    return xPos + new TextLayout(label, font, frc).getOutline(null).getBounds2D().getX();
  }

  private double columnWidth() {

    double max = 0;
    for (String label : LABELS) {
      max = Math.max(max, TickLabelMetrics.width(label, font, frc));
    }
    return max;
  }

  @Test
  void rightAlignedLabelsOfEqualLengthShareAnOrigin() {

    // The reported symptom: '1' was drawn ~3px right of '0' at this font size, and '10'..'14' right
    // of each other. Digit advances are tabular, so equal-length labels must share a pen origin.
    assertSharedOrigin("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
    assertSharedOrigin("10", "11", "12", "13", "14");
  }

  private void assertSharedOrigin(String... labels) {

    double expected = xPos(labels[0], TextAlignment.Right);
    for (String label : labels) {
      assertThat(xPos(label, TextAlignment.Right))
          .as("origin of '%s'", label)
          .isCloseTo(expected, within(TOLERANCE));
    }
  }

  private double xPos(String label, TextAlignment alignment) {

    return TickLabelMetrics.alignedXPos(
        COLUMN_X, columnWidth(), TickLabelMetrics.width(label, font, frc), alignment);
  }

  @Test
  void rightEdgesAreLessRaggedThanInkWidthAlignment() {

    // Guards against a regression to the pre-fix maths, which aligned on ink width and so shifted
    // every label by its own left side bearing. What residual raggedness remains is right side
    // bearing — a property of the glyph outlines themselves, which tabular alignment neither can
    // nor should remove: '7' reaches further right than '5' in a spreadsheet column too.
    double legacy = legacyInkRightEdgeSpread();
    assertThat(legacy).as("pre-fix spread, sanity check").isGreaterThan(2.0);
    assertThat(inkRightEdgeSpread()).as("post-fix spread").isLessThan(legacy);
  }

  /** Spread of rendered ink right edges under the current, advance-based alignment. */
  private double inkRightEdgeSpread() {

    double min = Double.MAX_VALUE;
    double max = -Double.MAX_VALUE;
    for (String label : LABELS) {
      double edge = renderedRightEdge(label, TextAlignment.Right);
      min = Math.min(min, edge);
      max = Math.max(max, edge);
    }
    return max - min;
  }

  /** Spread of rendered ink right edges under the pre-fix maths, reproduced here for comparison. */
  private double legacyInkRightEdgeSpread() {

    double legacyColumnWidth = 0;
    for (String label : LABELS) {
      legacyColumnWidth =
          Math.max(
              legacyColumnWidth,
              new TextLayout(label, font, frc).getOutline(null).getBounds2D().getWidth());
    }

    double min = Double.MAX_VALUE;
    double max = -Double.MAX_VALUE;
    for (String label : LABELS) {
      Rectangle2D ink = new TextLayout(label, font, frc).getOutline(null).getBounds2D();
      // Legacy: xPos ignored the bearing, but the filled outline still honoured it.
      double edge = (COLUMN_X + legacyColumnWidth - ink.getWidth()) + ink.getX() + ink.getWidth();
      min = Math.min(min, edge);
      max = Math.max(max, edge);
    }
    return max - min;
  }

  @Test
  void rightAlignedLabelsEndInTheSameAdvanceColumn() {

    // Labels of equal length occupy the same advance column regardless of leading digit: '1' used
    // to sit ~3px right of '0' at this font size, and '10'..'14' right of '9'.
    for (String label : LABELS) {
      double advanceRightEdge =
          TickLabelMetrics.alignedXPos(
                  COLUMN_X, columnWidth(), TickLabelMetrics.width(label, font, frc),
                  TextAlignment.Right)
              + TickLabelMetrics.width(label, font, frc);
      assertThat(advanceRightEdge)
          .as("advance right edge of '%s'", label)
          .isCloseTo(COLUMN_X + columnWidth(), within(TOLERANCE));
    }
  }

  @Test
  void leftAlignedLabelsShareALeftAdvanceEdge() {

    for (String label : LABELS) {
      assertThat(
              TickLabelMetrics.alignedXPos(
                  COLUMN_X, columnWidth(), TickLabelMetrics.width(label, font, frc),
                  TextAlignment.Left))
          .as("left edge of '%s'", label)
          .isCloseTo(COLUMN_X, within(TOLERANCE));
    }
  }

  @Test
  void centredLabelsAreEvenlyInset() {

    double columnWidth = columnWidth();
    for (String label : LABELS) {
      double labelWidth = TickLabelMetrics.width(label, font, frc);
      double xPos =
          TickLabelMetrics.alignedXPos(COLUMN_X, columnWidth, labelWidth, TextAlignment.Centre);
      double leftGap = xPos - COLUMN_X;
      double rightGap = COLUMN_X + columnWidth - (xPos + labelWidth);
      assertThat(leftGap).as("gaps around '%s'", label).isCloseTo(rightGap, within(TOLERANCE));
    }
  }

  @Test
  void advanceWidthIsNeverNarrowerThanTheInk() {

    // The column must reserve room for both side bearings, otherwise the widest label is clipped.
    for (String label : LABELS) {
      double ink = new TextLayout(label, font, frc).getOutline(null).getBounds2D().getWidth();
      assertThat(TickLabelMetrics.width(label, font, frc))
          .as("advance width of '%s'", label)
          .isGreaterThanOrEqualTo(ink);
    }
  }

  @Test
  void renderedInkStaysInsideTheColumn() {

    double columnWidth = columnWidth();
    for (TextAlignment alignment : TextAlignment.values()) {
      for (String label : LABELS) {
        assertThat(renderedLeftEdge(label, alignment))
            .as("'%s' ink left edge, %s aligned", label, alignment)
            .isGreaterThanOrEqualTo(COLUMN_X - TOLERANCE);
        assertThat(renderedRightEdge(label, alignment))
            .as("'%s' ink right edge, %s aligned", label, alignment)
            .isLessThanOrEqualTo(COLUMN_X + columnWidth + TOLERANCE);
      }
    }
  }
}
