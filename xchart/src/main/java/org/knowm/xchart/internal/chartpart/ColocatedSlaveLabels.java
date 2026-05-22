package org.knowm.xchart.internal.chartpart;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Map;

import org.knowm.xchart.style.AxesChartStyler;

/**
 * Renders stacked "colocate" tick labels for slave axes in a merged Y-axis group.
 *
 * <p>In colocate mode, slave tick labels appear below each master tick label in the same axis
 * column rather than in a separate column. This class is instantiated and called only by {@link
 * AxisTickLabels} when {@code yAxis.getColocatedSlaves()} is non-empty.
 *
 * <p>It also provides {@link #maxSlaveWidth(Graphics2D)} so the owning {@code AxisTickLabels} can
 * account for slave label widths when computing the master column's total width.
 */
class ColocatedSlaveLabels {

  private final Axis_<?, ?> yAxis; // master axis
  private final AxesChartStyler styler;

  ColocatedSlaveLabels(Axis_<?, ?> yAxis, AxesChartStyler styler) {

    this.yAxis = yAxis;
    this.styler = styler;
  }

  /**
   * Returns the maximum label width across all slave tick labels, or {@code 0} if there are no
   * colocated slaves. Used by {@link AxisTickLabels} to widen {@code maxTickLabelWidth} when
   * any slave label is wider than the master's labels.
   */
  double maxSlaveWidth(Graphics2D g) {

    double maxWidth = 0;
    for (Axis_<?, ?> slave : yAxis.getColocatedSlaves()) {
      if (slave.getAxisTickCalculator() == null) {
        continue;
      }
      FontRenderContext frc = g.getFontRenderContext();
      for (String label : slave.getAxisTickCalculator().getTickLabels()) {
        if (label != null && !label.isEmpty()) {
          TextLayout tl = new TextLayout(label, styler.getAxisTickLabelsFont(), frc);
          double w = tl.getBounds().getWidth();
          if (w > maxWidth) {
            maxWidth = w;
          }
        }
      }
    }

    return maxWidth;
  }

  /**
   * Renders all slave tick labels stacked below the master labels.
   *
   * <p>Does nothing when there are no colocated slaves.
   *
   * @param g graphics context
   * @param xOffset left x-coordinate of the label column
   * @param yOffset top y-coordinate of the axis
   * @param height pixel height of the axis
   * @param maxTickLabelWidth final column width (master + widest slave)
   * @param masterLayouts rendered master-tick text layouts, used to measure master label height
   */
  void paint(
      Graphics2D g,
      double xOffset,
      double yOffset,
      double height,
      double maxTickLabelWidth,
      Map<Double, TextLayout> masterLayouts) {

    List<? extends Axis_<?, ?>> colocatedSlaves = yAxis.getColocatedSlaves();
    if (colocatedSlaves.isEmpty()) {
      return;
    }

    double masterLabelHeight = 0;
    for (TextLayout tl : masterLayouts.values()) {
      double h = tl.getBounds().getHeight();
      if (h > masterLabelHeight) {
        masterLabelHeight = h;
      }
    }
    double gap = styler.getMergedAxisColocatedSlaveLabelsGap();
    FontRenderContext frc = g.getFontRenderContext();

    for (int s = 0; s < colocatedSlaves.size(); s++) {
      Axis_<?, ?> slaveAxis = colocatedSlaves.get(s);
      if (slaveAxis.getAxisTickCalculator() == null) {
        continue;
      }

      g.setColor(styler.getYAxisGroupTickLabelsColorMap(slaveAxis.getYIndex()));
      double depthOffset = masterLabelHeight / 2.0 + gap + (s * (masterLabelHeight + gap));

      for (int i = 0; i < slaveAxis.getAxisTickCalculator().getTickLabels().size(); i++) {
        String label = slaveAxis.getAxisTickCalculator().getTickLabels().get(i);
        double tickLocation = slaveAxis.getAxisTickCalculator().getTickLocations().get(i);
        double flippedTickLocation = yOffset + height - tickLocation;

        if (label != null
            && flippedTickLocation > yOffset
            && flippedTickLocation < yOffset + height) {
          TextLayout layout = new TextLayout(label, styler.getAxisTickLabelsFont(), frc);
          Shape shape = layout.getOutline(null);
          Rectangle2D slaveBounds = shape.getBounds();

          double xPos;
          switch (styler.getYAxisLabelAlignment()) {
            case Right:
              xPos = xOffset + maxTickLabelWidth - slaveBounds.getWidth();
              break;
            case Centre:
              xPos = xOffset + (maxTickLabelWidth - slaveBounds.getWidth()) / 2;
              break;
            case Left:
            default:
              xPos = xOffset;
          }

          AffineTransform orig = g.getTransform();
          AffineTransform at = new AffineTransform();
          at.translate(xPos, flippedTickLocation + depthOffset + slaveBounds.getHeight() / 2.0);
          g.transform(at);
          g.fill(shape);
          g.setTransform(orig);
        }
      }
    }

    // Restore master color
    g.setColor(styler.getYAxisGroupTickLabelsColorMap(yAxis.getYIndex()));
  }
}
