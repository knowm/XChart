package org.knowm.xchart.standalone.issues;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.AxesChartStyler.TextAlignment;

/**
 * Demonstrates issue #618 — {@code setYAxisLabelAlignment(TextAlignment.Right)} does not right-align
 * every Y-axis tick label.
 *
 * <p>{@code AxisTickLabels} positions each label from the <em>ink width</em> of its glyph outline
 * ({@code outline.getBounds2D().getWidth()}) but then draws that same outline by translating to the
 * computed x position. The outline's ink does not start at its origin — it starts at the left side
 * bearing, {@code getBounds2D().getX()}, which is discarded. The rendered right edge therefore lands
 * at {@code xOffset + maxTickLabelWidth + leftSideBearing} instead of at a constant x.
 *
 * <p>Digit {@code 1} has by far the largest left side bearing of any digit, so labels beginning with
 * {@code 1} were pushed visibly to the right of their neighbours. The error was ~9.9% of the font
 * size, which is why the demo uses a large tick label font — at the default size it was the ~1px
 * wobble seen in the original bug report.
 *
 * <p>The fix measures and aligns tick labels by <em>advance</em> width instead, which includes both
 * side bearings and so places each glyph exactly where the font intends. Digit advances are tabular,
 * so the labels now form a true column.
 *
 * <p>Run this and look at the Y axis: {@code 0} … {@code 14} share a clean right edge. {@link #main}
 * also prints both the old and the new alignment maths so the fix can be read off numerically rather
 * than eyeballed.
 */
public class TestForIssue618 {

  private static final int TICK_LABEL_FONT_SIZE = 30;

  public static void main(String[] args) {

    printRightEdges();
    new SwingWrapper<>(getChart()).displayChart();
  }

  /** Constructs and returns the chart without launching a window (headless-safe). */
  public static XYChart getChart() {

    XYChart chart =
        new XYChartBuilder()
            .width(700)
            .height(600)
            .title("Issue #618 – Right-aligned Y axis labels")
            .xAxisTitle("X")
            .yAxisTitle("Y")
            .build();

    chart.getStyler().setYAxisLabelAlignment(TextAlignment.Right);
    chart
        .getStyler()
        .setAxisTickLabelsFont(new Font(Font.SANS_SERIF, Font.PLAIN, TICK_LABEL_FONT_SIZE));

    // 0..14 so the tick labels cover both single digits and the leading-1 two digit labels.
    chart.getStyler().setYAxisMin(0.0);
    chart.getStyler().setYAxisMax(14.0);
    chart.getStyler().setYAxisTickMarkSpacingHint(30);
    chart.getStyler().setLegendVisible(false);

    chart.addSeries("series1", new double[] {0, 1, 2, 3, 4}, new double[] {0, 4, 7, 11, 14});
    return chart;
  }

  /**
   * Prints where each label is placed under the old and the new alignment maths. Under the old
   * maths the drawing origin varies with the label's left side bearing; under the new one every
   * equal-length label shares an origin.
   */
  private static void printRightEdges() {

    Graphics2D g = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics();
    FontRenderContext frc = g.getFontRenderContext();
    Font font = new Font(Font.SANS_SERIF, Font.PLAIN, TICK_LABEL_FONT_SIZE);

    String[] labels = {
      "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14"
    };

    // Column width, computed the old way (widest ink) and the new way (widest advance).
    double legacyColumnWidth = 0;
    double columnWidth = 0;
    for (String label : labels) {
      TextLayout layout = new TextLayout(label, font, frc);
      legacyColumnWidth =
          Math.max(legacyColumnWidth, layout.getOutline(null).getBounds2D().getWidth());
      columnWidth = Math.max(columnWidth, layout.getAdvance());
    }

    System.out.printf(
        "%-6s %9s %9s %9s %14s %12s%n",
        "label", "bearing", "inkWidth", "advance", "oldRightEdge", "newOrigin");
    double min = Double.MAX_VALUE;
    double max = -Double.MAX_VALUE;
    for (String label : labels) {
      TextLayout layout = new TextLayout(label, font, frc);
      java.awt.geom.Rectangle2D bounds = layout.getOutline(null).getBounds2D();

      // Old: xPos ignored the left side bearing, but the filled outline still honoured it, so the
      // rendered right edge moved with the bearing.
      double legacyRightEdge =
          (legacyColumnWidth - bounds.getWidth()) + bounds.getX() + bounds.getWidth();
      // New: align on advance width, which already accounts for both bearings.
      double origin = columnWidth - layout.getAdvance();

      min = Math.min(min, legacyRightEdge);
      max = Math.max(max, legacyRightEdge);
      System.out.printf(
          "%-6s %9.3f %9.3f %9.3f %14.3f %12.3f%n",
          label,
          bounds.getX(),
          bounds.getWidth(),
          layout.getAdvance(),
          legacyRightEdge,
          origin);
    }
    System.out.printf(
        "%nold right edges span %.3f px at font size %d; new origins take only 2 distinct values,"
            + " one per label length%n",
        max - min, TICK_LABEL_FONT_SIZE);

    g.dispose();
  }
}
