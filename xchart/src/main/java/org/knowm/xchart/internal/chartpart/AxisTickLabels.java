package org.knowm.xchart.internal.chartpart;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.knowm.xchart.internal.chartpart.Axis.Direction;
import org.knowm.xchart.internal.series.AxesChartSeries;
import org.knowm.xchart.style.AxesChartStyler;
import org.knowm.xchart.style.Styler.YAxisPosition;

/** Axis tick labels */
public class AxisTickLabels<ST extends AxesChartStyler, S extends AxesChartSeries>
    implements ChartPart {

  private final Chart<ST, S> chart;
  private final Direction direction;
  private final Axis yAxis;
  private Rectangle2D bounds;

  /**
   * Constructor
   *
   * @param chart
   * @param direction
   */
  AxisTickLabels(Chart<ST, S> chart, Direction direction, Axis yAxis) {

    this.chart = chart;
    this.direction = direction;
    this.yAxis = yAxis;
  }

  @Override
  public void paint(Graphics2D g) {

    ST styler = chart.getStyler();
    g.setFont(styler.getAxisTickLabelsFont());

    if (direction == Axis.Direction.Y && styler.isYAxisTicksVisible()) { // Y-Axis

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

      for (int i = 0; i < yAxis.getAxisTickCalculator().getTickLabels().size(); i++) {

        String tickLabel = yAxis.getAxisTickCalculator().getTickLabels().get(i);
        //         System.out.println("** " + tickLabel);
        double tickLocation = yAxis.getAxisTickCalculator().getTickLocations().get(i);
        double flippedTickLocation = yOffset + height - tickLocation;

        if (tickLabel != null
            && flippedTickLocation > yOffset
            && flippedTickLocation < yOffset + height) { // some are null for logarithmic axes
          FontRenderContext frc = g.getFontRenderContext();
          TextLayout axisLabelTextLayout =
              new TextLayout(tickLabel, styler.getAxisTickLabelsFont(), frc);
          Rectangle2D tickLabelBounds = axisLabelTextLayout.getBounds();
          double boundWidth = tickLabelBounds.getWidth();
          if (boundWidth > maxTickLabelWidth) {
            maxTickLabelWidth = boundWidth;
          }
          axisLabelTextLayouts.put(tickLocation, axisLabelTextLayout);
        }
      }

      // Also account for the widths of any colocated slave labels so the column is wide enough.
      for (Object slaveObj : yAxis.getColocatedSlaves()) {
        Axis slave = (Axis) slaveObj;
        if (slave.getAxisTickCalculator() == null) continue;
        FontRenderContext frc = g.getFontRenderContext();
        for (String slaveLabel : slave.getAxisTickCalculator().getTickLabels()) {
          if (slaveLabel != null && !slaveLabel.isEmpty()) {
            TextLayout tl = new TextLayout(slaveLabel, styler.getAxisTickLabelsFont(), frc);
            double w = tl.getBounds().getWidth();
            if (w > maxTickLabelWidth) maxTickLabelWidth = w;
          }
        }
      }

      for (Map.Entry<Double, TextLayout> tick : axisLabelTextLayouts.entrySet()) {
        final Double tickLocation = tick.getKey();
        final TextLayout axisLabelTextLayout = tick.getValue();

        Shape shape = axisLabelTextLayout.getOutline(null);
        Rectangle2D tickLabelBounds = shape.getBounds();

        double flippedTickLocation = yOffset + height - tickLocation;

        AffineTransform orig = g.getTransform();
        AffineTransform at = new AffineTransform();
        double boundWidth = tickLabelBounds.getWidth();
        double xPos;
        switch (styler.getYAxisLabelAlignment()) {
          case Right:
            xPos = xOffset + maxTickLabelWidth - boundWidth;
            break;
          case Centre:
            xPos = xOffset + (maxTickLabelWidth - boundWidth) / 2;
            break;
          case Left:
          default:
            xPos = xOffset;
        }
        at.translate(xPos, flippedTickLocation + tickLabelBounds.getHeight() / 2.0);
        g.transform(at);
        g.fill(shape);
        g.setTransform(orig);
      }

      // Render colocated slave labels stacked below each master label
      List colocatedSlaves = yAxis.getColocatedSlaves();
      if (!colocatedSlaves.isEmpty()) {
        // Measure master label height (max across all rendered labels)
        double masterLabelHeight = 0;
        for (TextLayout tl : axisLabelTextLayouts.values()) {
          double h = tl.getBounds().getHeight();
          if (h > masterLabelHeight) masterLabelHeight = h;
        }
        double GAP = styler.getMergedAxisColocatedSlaveLabelsGap();

        FontRenderContext frc = g.getFontRenderContext();
        for (int s = 0; s < colocatedSlaves.size(); s++) {
          Axis slaveAxis = (Axis) colocatedSlaves.get(s);
          if (slaveAxis.getAxisTickCalculator() == null) continue;

          g.setColor(styler.getYAxisGroupTickLabelsColorMap(slaveAxis.getYIndex()));
          double slaveDepthOffset = masterLabelHeight / 2.0 + GAP + (s * (masterLabelHeight + GAP));

          for (int i = 0; i < slaveAxis.getAxisTickCalculator().getTickLabels().size(); i++) {
            String slaveLabel = slaveAxis.getAxisTickCalculator().getTickLabels().get(i);
            double tickLocation = slaveAxis.getAxisTickCalculator().getTickLocations().get(i);
            double flippedTickLocation = yOffset + height - tickLocation;

            if (slaveLabel != null
                && flippedTickLocation > yOffset
                && flippedTickLocation < yOffset + height) {
              TextLayout slaveLayout =
                  new TextLayout(slaveLabel, styler.getAxisTickLabelsFont(), frc);
              Shape slaveShape = slaveLayout.getOutline(null);
              Rectangle2D slaveBounds = slaveShape.getBounds();
              double slaveWidth = slaveBounds.getWidth();
              double slaveHeight = slaveBounds.getHeight();

              double xPos;
              switch (styler.getYAxisLabelAlignment()) {
                case Right:
                  xPos = xOffset + maxTickLabelWidth - slaveWidth;
                  break;
                case Centre:
                  xPos = xOffset + (maxTickLabelWidth - slaveWidth) / 2;
                  break;
                case Left:
                default:
                  xPos = xOffset;
              }
              double yPos = flippedTickLocation + slaveDepthOffset + slaveHeight / 2.0;

              AffineTransform orig = g.getTransform();
              AffineTransform at = new AffineTransform();
              at.translate(xPos, yPos);
              g.transform(at);
              g.fill(slaveShape);
              g.setTransform(orig);
            }
          }
        }
        // Restore master color
        g.setColor(styler.getYAxisGroupTickLabelsColorMap(yAxis.getYIndex()));
      }

      // bounds
      bounds = new Rectangle2D.Double(xOffset, yOffset, maxTickLabelWidth, height);
      // g.setColor(Color.blue);
      // g.draw(bounds);

    }
    // X-Axis
    else if (direction == Axis.Direction.X && styler.isXAxisTicksVisible()) {

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

        // discard null and out of bounds labels
        if (tickLabel != null
            && shiftedTickLocation > xOffset
            && shiftedTickLocation < xOffset + width) {
          // some are null for logarithmic axes

          FontRenderContext frc = g.getFontRenderContext();
          TextLayout textLayout = new TextLayout(tickLabel, styler.getAxisTickLabelsFont(), frc);
          // System.out.println(textLayout.getOutline(null).getBounds().toString());

          // Shape shape = v.getOutline();
          AffineTransform rot =
              AffineTransform.getRotateInstance(
                  -1 * Math.toRadians(styler.getXAxisLabelRotation()), 0, 0);
          Shape shape = textLayout.getOutline(rot);
          Rectangle2D tickLabelBounds = shape.getBounds2D();
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

        // discard null and out of bounds labels
        if (tickLabel != null
            && shiftedTickLocation > xOffset
            && shiftedTickLocation < xOffset + width) { // some are null for logarithmic axes

          FontRenderContext frc = g.getFontRenderContext();
          TextLayout textLayout = new TextLayout(tickLabel, styler.getAxisTickLabelsFont(), frc);
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
