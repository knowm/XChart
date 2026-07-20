package org.knowm.xchart.internal.chartpart;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;

import org.knowm.xchart.internal.chartpart.Axis_.Direction;
import org.knowm.xchart.internal.series.AxesChartSeries;
import org.knowm.xchart.style.AxesChartStyler;
import org.knowm.xchart.style.Styler.YAxisPosition;

/** Axis tick labels */
public class AxisTickLabels<ST extends AxesChartStyler, S extends AxesChartSeries>
    implements ChartPart {

  private final AxesChart<ST, S> chart;
  private final Direction direction;
  private final Axis_<?, ?> yAxis;
  private final ColocatedSlaveLabels colocatedSlaveLabels;
  private Rectangle2D bounds;

  /**
   * Constructor
   *
   * @param chart
   * @param direction
   */
  AxisTickLabels(AxesChart<ST, S> chart, Direction direction, Axis_<?, ?> yAxis) {

    this.chart = chart;
    this.direction = direction;
    this.yAxis = yAxis;
    this.colocatedSlaveLabels =
        yAxis != null ? new ColocatedSlaveLabels(yAxis, chart.getStyler()) : null;
  }

  @Override
  public void paint(Graphics2D g) {

    ST styler = chart.getStyler();
    g.setFont(styler.getAxisTickLabelsFont());

    if (direction == Axis_.Direction.Y && styler.isYAxisTicksVisible()) { // Y-Axis

      g.setColor(styler.getYAxisGroupTickLabelsColorMap(yAxis.getYIndex()));
      boolean onRight = styler.getYAxisGroupPosistion(yAxis.getYIndex()) == YAxisPosition.Right;

      double xOffset;
      if (onRight) {
        xOffset =
            yAxis.getBounds().getX()
                + (styler.isYAxisTicksVisible()
                    ? styler.getAxisTickMarkLength() + styler.getAxisTickPadding()
                    : 0);
      } else {
        double xWidth = yAxis.getAxisTitle().getBounds().getWidth();
        xOffset = yAxis.getAxisTitle().getBounds().getX() + xWidth;
      }
      double yOffset = yAxis.getBounds().getY();
      double height = yAxis.getBounds().getHeight();
      double maxTickLabelWidth = 0;
      Map<Double, TextLayout> axisLabelTextLayouts = new HashMap<Double, TextLayout>();
      Map<Double, String> axisLabelStrings = new HashMap<Double, String>();

      for (int i = 0; i < yAxis.getAxisTickCalculator().getTickLabels().size(); i++) {

        String tickLabel = yAxis.getAxisTickCalculator().getTickLabels().get(i);
        //         System.out.println("** " + tickLabel);
        double tickLocation = yAxis.getAxisTickCalculator().getTickLocations().get(i);
        double flippedTickLocation = yOffset + height - tickLocation;

        if (tickLabel != null
            && !tickLabel.isEmpty()
            && flippedTickLocation > yOffset
            && flippedTickLocation < yOffset + height) { // some are null for logarithmic axes
          FontRenderContext frc = g.getFontRenderContext();
          double labelWidth =
              TickLabelMetrics.width(tickLabel, styler.getAxisTickLabelsFont(), frc);
          if (labelWidth > maxTickLabelWidth) {
            maxTickLabelWidth = labelWidth;
          }
          axisLabelStrings.put(tickLocation, tickLabel);
          // axisLabelTextLayouts is used by colocatedSlaveLabels; use a space for TeX labels
          String layoutText = TexRenderer.isTeX(tickLabel) ? " " : tickLabel;
          axisLabelTextLayouts.put(
              tickLocation, new TextLayout(layoutText, styler.getAxisTickLabelsFont(), frc));
        }
      }

      // Also account for the widths of any colocated slave labels so the column is wide enough.
      double slaveMaxWidth = colocatedSlaveLabels.maxSlaveWidth(g);
      if (slaveMaxWidth > maxTickLabelWidth) {
        maxTickLabelWidth = slaveMaxWidth;
      }

      for (Map.Entry<Double, String> tick : axisLabelStrings.entrySet()) {
        final Double tickLocation = tick.getKey();
        final String tickLabel = tick.getValue();

        Rectangle2D tickLabelBounds;
        if (TexRenderer.isTeX(tickLabel)) {
          tickLabelBounds = TexRenderer.getBounds(tickLabel, styler.getAxisTickLabelsFont());
        } else {
          FontRenderContext frc2 = g.getFontRenderContext();
          tickLabelBounds =
              new TextLayout(tickLabel, styler.getAxisTickLabelsFont(), frc2)
                  .getOutline(null)
                  .getBounds2D();
        }

        double flippedTickLocation = yOffset + height - tickLocation;

        double labelWidth =
            TickLabelMetrics.width(tickLabel, styler.getAxisTickLabelsFont(), g.getFontRenderContext());
        double xPos =
            TickLabelMetrics.alignedXPos(
                xOffset, maxTickLabelWidth, labelWidth, styler.getYAxisLabelAlignment());

        double yPos = flippedTickLocation + tickLabelBounds.getHeight() / 2.0;

        if (TexRenderer.isTeX(tickLabel)) {
          // For TeX, (xPos, yPos) is top-left; adjust so icon is vertically centred
          double topLeft = flippedTickLocation - tickLabelBounds.getHeight() / 2.0;
          TexRenderer.render(
              g, tickLabel, xPos, topLeft, styler.getAxisTickLabelsFont(),
              styler.getYAxisGroupTickLabelsColorMap(yAxis.getYIndex()));
        } else {
          FontRenderContext frc = g.getFontRenderContext();
          TextLayout axisLabelTextLayout =
              new TextLayout(tickLabel, styler.getAxisTickLabelsFont(), frc);
          Shape shape = axisLabelTextLayout.getOutline(null);
          AffineTransform orig = g.getTransform();
          AffineTransform at = new AffineTransform();
          at.translate(xPos, yPos);
          g.transform(at);
          g.fill(shape);
          g.setTransform(orig);
        }
      }

      // Render colocated slave labels stacked below each master label
      colocatedSlaveLabels.paint(
          g, xOffset, yOffset, height, maxTickLabelWidth, axisLabelTextLayouts);

      // bounds
      bounds = new Rectangle2D.Double(xOffset, yOffset, maxTickLabelWidth, height);
      // g.setColor(Color.blue);
      // g.draw(bounds);

    }
    // X-Axis
    else if (direction == Axis_.Direction.X && styler.isXAxisTicksVisible()) {

      g.setColor(styler.getXAxisTickLabelsColor());
      double xOffset = chart.getXAxis().getBounds().getX();
      double yOffset = chart.getXAxis().getAxisTitle().getBounds().getY();
      double width = chart.getXAxis().getBounds().getWidth();
      double maxTickLabelHeight = 0;

      // determine maxTickLabelY
      int maxTickLabelY = 0;
      for (int i = 0; i < chart.getXAxis().getAxisTickCalculator().getTickLabels().size(); i++) {

        String tickLabel = chart.getXAxis().getAxisTickCalculator().getTickLabels().get(i);
        // System.out.println("tickLabel: " + tickLabel);
        double tickLocation = chart.getXAxis().getAxisTickCalculator().getTickLocations().get(i);
        double shiftedTickLocation = xOffset + tickLocation;

        // discard null, empty, and out of bounds labels
        if (tickLabel != null
            && !tickLabel.isEmpty()
            && shiftedTickLocation > xOffset
            && shiftedTickLocation < xOffset + width) {
          // some are null for logarithmic axes

          Rectangle2D tickLabelBounds;
          if (TexRenderer.isTeX(tickLabel)) {
            tickLabelBounds = TexRenderer.getBounds(tickLabel, styler.getAxisTickLabelsFont());
          } else {
            FontRenderContext frc = g.getFontRenderContext();
            TextLayout textLayout =
                new TextLayout(tickLabel, styler.getAxisTickLabelsFont(), frc);
            AffineTransform rot =
                AffineTransform.getRotateInstance(
                    -1 * Math.toRadians(styler.getXAxisLabelRotation()), 0, 0);
            tickLabelBounds = textLayout.getOutline(rot).getBounds2D();
          }
          if (tickLabelBounds.getBounds().height > maxTickLabelY) {
            maxTickLabelY = tickLabelBounds.getBounds().height;
          }
        }
      }

      // System.out.println("axisTick.getTickLabels().size(): " + axisTick.getTickLabels().size());
      for (int i = 0; i < chart.getXAxis().getAxisTickCalculator().getTickLabels().size(); i++) {

        String tickLabel = chart.getXAxis().getAxisTickCalculator().getTickLabels().get(i);
        // System.out.println("tickLabel: " + tickLabel);
        double tickLocation = chart.getXAxis().getAxisTickCalculator().getTickLocations().get(i);
        double shiftedTickLocation = xOffset + tickLocation;

        // discard null, empty, and out of bounds labels
        if (tickLabel != null
            && !tickLabel.isEmpty()
            && shiftedTickLocation > xOffset
            && shiftedTickLocation < xOffset + width) { // some are null for logarithmic axes

          if (TexRenderer.isTeX(tickLabel)) {
            Rectangle2D tickLabelBounds =
                TexRenderer.getBounds(tickLabel, styler.getAxisTickLabelsFont());
            double xPos;
            switch (styler.getXAxisLabelAlignment()) {
              case Left:
                xPos = shiftedTickLocation;
                break;
              case Right:
                xPos = shiftedTickLocation - tickLabelBounds.getWidth();
                break;
              case Centre:
              default:
                xPos = shiftedTickLocation - tickLabelBounds.getWidth() / 2.0;
            }
            double yPos = yOffset - tickLabelBounds.getHeight();
            if (xPos > 0 && xPos + tickLabelBounds.getWidth() < chart.getWidth()) {
              TexRenderer.render(
                  g, tickLabel, xPos, yPos, styler.getAxisTickLabelsFont(),
                  styler.getXAxisTickLabelsColor());
            }
            if (tickLabelBounds.getHeight() > maxTickLabelHeight) {
              maxTickLabelHeight = tickLabelBounds.getHeight();
            }
          } else {
            FontRenderContext frc = g.getFontRenderContext();
            TextLayout textLayout =
                new TextLayout(tickLabel, styler.getAxisTickLabelsFont(), frc);
            // System.out.println(textLayout.getOutline(null).getBounds().toString());

            // Shape shape = v.getOutline();
            AffineTransform rot =
                AffineTransform.getRotateInstance(
                    -1 * Math.toRadians(styler.getXAxisLabelRotation()), 0, 0);
            Shape shape = textLayout.getOutline(rot);
            Rectangle2D tickLabelBounds = shape.getBounds2D();

            int tickLabelY = tickLabelBounds.getBounds().height;
            int yAlignmentOffset;
            switch (styler.getXAxisLabelAlignmentVertical()) {
              case Right:
                yAlignmentOffset = maxTickLabelY - tickLabelY;
                break;
              case Centre:
                yAlignmentOffset = (maxTickLabelY - tickLabelY) / 2;
                break;
              case Left:
              default:
                yAlignmentOffset = 0;
            }

            AffineTransform orig = g.getTransform();
            AffineTransform at = new AffineTransform();
            double xPos;
            switch (styler.getXAxisLabelAlignment()) {
              case Left:
                xPos = shiftedTickLocation;
                break;
              case Right:
                xPos = shiftedTickLocation - tickLabelBounds.getWidth();
                break;
              case Centre:
              default:
                xPos = shiftedTickLocation - tickLabelBounds.getWidth() / 2.0;
            }
            //          System.out.println("tickLabelBounds: " + tickLabelBounds.toString());
            double shiftX =
                -1
                    * tickLabelBounds.getX()
                    * Math.sin(Math.toRadians(styler.getXAxisLabelRotation()));
            double shiftY =
                -1 * (tickLabelBounds.getY() + tickLabelBounds.getHeight() + yAlignmentOffset);
            // System.out.println(shiftX);
            // System.out.println("shiftY: " + shiftY);
            at.translate(xPos + shiftX, yOffset + shiftY);

            if (xPos > 0 && xPos + tickLabelBounds.getWidth() < chart.getWidth()) {
              g.transform(at);
              g.fill(shape);
              g.setTransform(orig);

              //            // debug box
              //            g.setColor(Color.MAGENTA);
              //            g.draw(
              //                new Rectangle2D.Double(
              //                    xPos,
              //                    yOffset - tickLabelBounds.getHeight(),
              //                    tickLabelBounds.getWidth(),
              //                    tickLabelBounds.getHeight()));
              //            g.setColor(chart.getStyler().getAxisTickLabelsColor());
            }
            //          else { // discarding based on the outside edges of the tick labels
            //            System.out.println("discarding: " + tickLabel);
            //          }
            if (tickLabelBounds.getHeight() > maxTickLabelHeight) {
              maxTickLabelHeight = tickLabelBounds.getHeight();
            }
          }
        }
        //        else {// discarding based on the center of the tick labels
        //          System.out.println("discarding: " + tickLabel);
        //        }
      }

      // bounds
      bounds =
          new Rectangle2D.Double(xOffset, yOffset - maxTickLabelHeight, width, maxTickLabelHeight);
      //      g.setColor(Color.blue);
      //      g.draw(bounds);

    } else {
      bounds = new Rectangle2D.Double();
    }
  }

  @Override
  public Rectangle2D getBounds() {

    return bounds;
  }
}
