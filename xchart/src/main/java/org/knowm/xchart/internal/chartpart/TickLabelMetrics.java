package org.knowm.xchart.internal.chartpart;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;

import org.knowm.xchart.style.AxesChartStyler.TextAlignment;

/**
 * Shared width and alignment maths for axis tick labels.
 *
 * <p>Tick labels are drawn by translating to an x position and filling the glyph outline. The
 * outline's ink does not begin at its origin — it begins at the left side bearing. Measuring a label
 * by its ink width ({@code outline.getBounds2D().getWidth()}) and then aligning on that width
 * therefore shifts each label by its own bearing, because the bearing is dropped from the
 * measurement but still honoured when drawing. Digit {@code 1} has by far the largest bearing of any
 * digit, so labels beginning with {@code 1} drifted right of their neighbours (issue #618).
 *
 * <p>Advance width is the correct metric here: it is the distance the pen moves, so it includes both
 * side bearings and positions the glyph exactly as the font intends. It also makes digit columns
 * line up, since digit advances are tabular in virtually every font.
 */
final class TickLabelMetrics {

  private TickLabelMetrics() {}

  /**
   * Returns the layout width of a tick label — the advance width for ordinary text, or the rendered
   * bounds for TeX labels, which are drawn as an icon from their top-left corner and so have no
   * bearing to account for.
   */
  static double width(String label, Font font, FontRenderContext frc) {

    if (TexRenderer.isTeX(label)) {
      return TexRenderer.getBounds(label, font).getWidth();
    }
    return new TextLayout(label, font, frc).getAdvance();
  }

  /**
   * Returns the x position at which a label of the given width should be drawn to satisfy the
   * requested alignment within a column of {@code columnWidth} starting at {@code xOffset}.
   */
  static double alignedXPos(
      double xOffset, double columnWidth, double labelWidth, TextAlignment alignment) {

    switch (alignment) {
      case Right:
        return xOffset + columnWidth - labelWidth;
      case Centre:
        return xOffset + (columnWidth - labelWidth) / 2;
      case Left:
      default:
        return xOffset;
    }
  }
}
