package org.knowm.xchart.internal.chartpart;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.*;
import java.awt.geom.Arc2D.Double;
import java.text.DecimalFormat;
import java.util.Map;
import org.knowm.xchart.PieSeries;
import org.knowm.xchart.PieSeries.PieSeriesRenderStyle;
import org.knowm.xchart.style.PieStyler;
import org.knowm.xchart.style.PieStyler.ClockwiseDirectionType;
import org.knowm.xchart.style.PieStyler.LabelType;

public class PlotContent_Pie<ST extends PieStyler, S extends PieSeries>
    extends PlotContent_<ST, S> {

  private final ST pieStyler;
  private final DecimalFormat df = new DecimalFormat("#.0");

  /**
   * Constructor
   *
   * @param chart
   */
  PlotContent_Pie(Chart<ST, S> chart) {

    super(chart);
    pieStyler = chart.getStyler();
  }

  // TODO get rid of this
  public static Shape getDonutSliceShape(
      Rectangle2D pieBounds, double thickness, double start, double extent) {

    thickness = thickness / 2;

    GeneralPath generalPath = new GeneralPath();
    GeneralPath dummy = new GeneralPath(); // used to find arc endpoints

    double x = pieBounds.getX();
    double y = pieBounds.getY();
    double width = pieBounds.getWidth();
    double height = pieBounds.getHeight();
    Shape outer = new Arc2D.Double(x, y, width, height, start, extent, Arc2D.OPEN);
    double wt = width * thickness;
    double ht = height * thickness;
    Shape inner =
        new Arc2D.Double(
            x + wt, y + ht, width - 2 * wt, height - 2 * ht, start + extent, -extent, Arc2D.OPEN);
    generalPath.append(outer, false);

    dummy.append(
        new Arc2D.Double(
            x + wt, y + ht, width - 2 * wt, height - 2 * ht, start, extent, Arc2D.OPEN),
        false);

    Point2D point = dummy.getCurrentPoint();

    if (point != null) {
      generalPath.lineTo(point.getX(), point.getY());
    }
    generalPath.append(inner, false);

    dummy.append(new Arc2D.Double(x, y, width, height, start + extent, -extent, Arc2D.OPEN), false);

    point = dummy.getCurrentPoint();

    if (point != null) {
      generalPath.lineTo(point.getX(), point.getY());
    }

    return generalPath;
  }

  @Override
  public void doPaint(Graphics2D g) {

    // Apply the given pattern to decimalPattern if decimalPattern is not null
    if (pieStyler.getDecimalPattern() != null) {
      df.applyPattern(pieStyler.getDecimalPattern());
    }

    // pie getBounds()
    double pieFillPercentage = pieStyler.getPlotContentSize();

    double halfBorderPercentage = (1 - pieFillPercentage) / 2.0;
    double width =
        pieStyler.isCircular()
            ? Math.min(getBounds().getWidth(), getBounds().getHeight())
            : getBounds().getWidth();
    double height =
        pieStyler.isCircular()
            ? Math.min(getBounds().getWidth(), getBounds().getHeight())
            : getBounds().getHeight();

    Rectangle2D pieBounds =
        new Rectangle2D.Double(
            getBounds().getX()
                + getBounds().getWidth() / 2
                - width / 2
                + halfBorderPercentage * width,
            getBounds().getY()
                + getBounds().getHeight() / 2
                - height / 2
                + halfBorderPercentage * height,
            width * pieFillPercentage,
            height * pieFillPercentage);

    //    g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
    //    g.setColor(Color.black);
    //    g.draw(pieBounds);

    //    g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
    //    g.setColor(Color.red);
    //    g.draw(getBounds());

    // get total
    double total = 0.0;

    Map<String, S> map = chart.getSeriesMap();
    for (S series : map.values()) {

      if (!series.isEnabled() || series.getValue() == null) {
        continue;
      }
      total += series.getValue().doubleValue();
    }

    // draw pie slices
    double startAngle = pieStyler.getStartAngleInDegrees() + 90;
    paintSlices(g, pieBounds, total, startAngle);
    paintLabels(g, pieBounds, total, startAngle);
    paintSum(g, pieBounds, total);
  }

  private void paintSlices(Graphics2D g, Rectangle2D pieBounds, double total, double startAngle) {

    double borderAngle = startAngle;

    Map<String, S> map = chart.getSeriesMap();
    double xCenter = pieBounds.getX() + pieBounds.getWidth() / 2;
    double yCenter = pieBounds.getY() + pieBounds.getHeight() / 2;
    for (S series : map.values()) {

      if (!series.isEnabled() || series.getValue() == null) {
        continue;
      }

      Number y = series.getValue();

      // draw slice/donut
      double arcAngle = (y.doubleValue() * 360 / total);
      g.setColor(series.getFillColor());

      // CLOCKWISE, startAngle minus arcAngle
      if (ClockwiseDirectionType.CLOCKWISE == pieStyler.getClockwiseDirectionType()) {
        startAngle -= arcAngle;
      }

      Shape toolTipShape;
      // slice
      if (PieSeriesRenderStyle.Pie == series.getChartPieSeriesRenderStyle()) {

        Double pieShape =
            new Arc2D.Double(
                pieBounds.getX(),
                pieBounds.getY(),
                pieBounds.getWidth(),
                pieBounds.getHeight(),
                startAngle,
                arcAngle,
                Arc2D.PIE);
        g.fill(pieShape);
        g.setColor(pieStyler.getPlotBackgroundColor());
        g.draw(pieShape);
        toolTipShape = pieShape;
      }

      // donut
      else {

        Shape donutSlice =
            getDonutSliceShape(pieBounds, pieStyler.getDonutThickness(), startAngle, arcAngle);
        g.fill(donutSlice);
        g.setColor(pieStyler.getPlotBackgroundColor());
        g.draw(donutSlice);
        toolTipShape = donutSlice;
      }

      // TOOLTIPS ////////////////////////////////////////////////////
      // TOOLTIPS ////////////////////////////////////////////////////
      // TOOLTIPS ////////////////////////////////////////////////////

      if (pieStyler.isToolTipsEnabled()) {
        // add data labels
        // maybe another option to construct this label
        // TODO use tool tip label type enum and customize this label
        String toolTipLabel = series.getName() + " (" + df.format(y) + ")";

        double angle = (arcAngle + startAngle) - arcAngle / 2;
        double xOffset =
            xCenter
                + Math.cos(Math.toRadians(angle))
                    * (pieBounds.getWidth() / 2 * pieStyler.getLabelsDistance());
        double yOffset =
            yCenter
                - Math.sin(Math.toRadians(angle))
                    * (pieBounds.getHeight() / 2 * pieStyler.getLabelsDistance());

        toolTips.addData(toolTipShape, xOffset, yOffset + 10, 0, toolTipLabel);
      }

      // TOOLTIPS ////////////////////////////////////////////////////
      // TOOLTIPS ////////////////////////////////////////////////////
      // TOOLTIPS ////////////////////////////////////////////////////

      // COUNTER_CLOCKWISE, startAngle plus arcAngle
      if (ClockwiseDirectionType.COUNTER_CLOCKWISE == pieStyler.getClockwiseDirectionType()) {
        startAngle += arcAngle;
      }
    }

    // draw border between the slices
    float borderWidth = chart.getStyler().getSliceBorderWidth();
    if (borderWidth > 0) {
      Color color = pieStyler.getPlotBackgroundColor();
      g.setColor(color);
      for (S series : map.values()) {
        if (!series.isEnabled() || series.getValue() == null) {
          continue;
        }
        Number y = series.getValue();
        double arcAngle = y.doubleValue() * 360 / total;
        borderAngle += arcAngle;
        double xBorder = (pieBounds.getWidth() / 2.0) * Math.cos(Math.toRadians(borderAngle));
        double yBorder = (pieBounds.getHeight() / 2.0) * Math.sin(Math.toRadians(borderAngle));
        xBorder = xBorder + pieBounds.getX() + pieBounds.getWidth() / 2.0;
        yBorder = pieBounds.getY() + pieBounds.getHeight() / 2.0 - yBorder;
        Shape line = new Line2D.Double(xCenter, yCenter, xBorder, yBorder);
        g.setStroke(new BasicStroke(borderWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.draw(line);
      }
    }
  }

  private void paintLabels(Graphics2D g, Rectangle2D pieBounds, double total, double startAngle) {

    Map<String, S> map = chart.getSeriesMap();
    for (S series : map.values()) {

      if (!series.isEnabled() || series.getValue() == null) {
        continue;
      }

      Number y = series.getValue();

      // draw slice/donut
      double arcAngle = (y.doubleValue() * 360 / total);
      // CLOCKWISE, startAngle minus arcAngle
      if (ClockwiseDirectionType.CLOCKWISE == pieStyler.getClockwiseDirectionType()) {
        startAngle -= arcAngle;
      }

      // curValue += y.doubleValue();

      if (pieStyler.isLabelsVisible()) {

        // draw label
        String label = "";
        if (pieStyler.getLabelType() == LabelType.Value) {

          if (pieStyler.getDecimalPattern() != null) {
            label = df.format(y);
          } else {
            label = y.toString();
          }
        } else if (pieStyler.getLabelType() == LabelType.Name) {
          label = series.getName();
        } else if (pieStyler.getLabelType() == LabelType.NameAndPercentage) {
          double percentage = y.doubleValue() / total * 100;
          label = series.getName() + " (" + df.format(percentage) + "%)";
        } else if (pieStyler.getLabelType() == LabelType.Percentage) {
          double percentage = y.doubleValue() / total * 100;
          label = df.format(percentage) + "%";
        } else if (pieStyler.getLabelType() == LabelType.NameAndValue) {
          if (pieStyler.getDecimalPattern() != null) {
            label = series.getName() + " (" + df.format(y) + ")";
          } else {
            label = series.getName() + " (" + y.toString() + ")";
          }
        } else if (pieStyler.getLabelType() == LabelType.Custom) {
          label = pieStyler.getLabelGenerator().generateSeriesLabel(series);
        }

        TextLayout textLayout =
            new TextLayout(
                label, pieStyler.getLabelsFont(), new FontRenderContext(null, true, false));
        Rectangle2D labelRectangle = textLayout.getBounds();

        double xCenter =
            pieBounds.getX() + pieBounds.getWidth() / 2 - labelRectangle.getWidth() / 2;
        double yCenter =
            pieBounds.getY() + pieBounds.getHeight() / 2 + labelRectangle.getHeight() / 2;
        double angle = (arcAngle + startAngle) - arcAngle / 2;
        double xOffset =
            xCenter
                + Math.cos(Math.toRadians(angle))
                    * (pieBounds.getWidth() / 2 * pieStyler.getLabelsDistance());
        double yOffset =
            yCenter
                - Math.sin(Math.toRadians(angle))
                    * (pieBounds.getHeight() / 2 * pieStyler.getLabelsDistance());

        // get annotation width
        Shape shape = textLayout.getOutline(null);
        Rectangle2D labelBounds = shape.getBounds2D();
        double labelWidth = labelBounds.getWidth();
        // System.out.println("annotationWidth= " + annotationWidth);
        double labelHeight = labelBounds.getHeight();
        // System.out.println("annotationHeight= " + annotationHeight);

        // get slice area
        double xOffset1 =
            xCenter
                + Math.cos(Math.toRadians(startAngle))
                    * (pieBounds.getWidth() / 2 * pieStyler.getLabelsDistance());
        double yOffset1 =
            yCenter
                - Math.sin(Math.toRadians(startAngle))
                    * (pieBounds.getHeight() / 2 * pieStyler.getLabelsDistance());
        double xOffset2 =
            xCenter
                + Math.cos(Math.toRadians((arcAngle + startAngle)))
                    * (pieBounds.getWidth() / 2 * pieStyler.getLabelsDistance());
        double yOffset2 =
            yCenter
                - Math.sin(Math.toRadians((arcAngle + startAngle)))
                    * (pieBounds.getHeight() / 2 * pieStyler.getLabelsDistance());
        // System.out.println("xOffset1= " + xOffset1);
        // System.out.println("yOffset1= " + yOffset1);
        // System.out.println("xOffset2= " + xOffset2);
        // System.out.println("yOffset2= " + yOffset2);
        double xDiff = Math.abs(xOffset1 - xOffset2);
        double yDiff = Math.abs(yOffset1 - yOffset2);
        // System.out.println("xDiff= " + xDiff);
        // System.out.println("yDiff= " + yDiff);
        // double max = Math.max(xDiff, yDiff);
        // System.out.println(" ================== ");
        boolean labelWillFit = false;
        if (xDiff >= yDiff) { // assume more vertically orientated slice
          if (labelWidth < xDiff) {
            labelWillFit = true;
          }
        } else if (xDiff <= yDiff) { // assume more horizontally orientated slice
          if (labelHeight < yDiff) {
            labelWillFit = true;
          }
        }

        // draw label
        if (pieStyler.isForceAllLabelsVisible() || labelWillFit) {

          if (pieStyler.isLabelsFontColorAutomaticEnabled()) {
            g.setColor(pieStyler.getLabelsFontColor(series.getFillColor()));
          } else {
            g.setColor(pieStyler.getLabelsFontColor());
          }

          g.setFont(pieStyler.getLabelsFont());
          AffineTransform orig = g.getTransform();
          AffineTransform at = new AffineTransform();

          // inside
          if (pieStyler.getLabelsDistance() <= 1.0) {
            at.translate(xOffset, yOffset);
          }

          // outside
          else {

            // Tick Mark
            xCenter = pieBounds.getX() + pieBounds.getWidth() / 2;
            yCenter = pieBounds.getY() + pieBounds.getHeight() / 2;
            // double endPoint = Math.min((2.0 - (pieStyler.getAnnotationDistance() - 1)), 1.95);
            double endPoint = (3.0 - pieStyler.getLabelsDistance());
            double xOffsetStart =
                xCenter + Math.cos(Math.toRadians(angle)) * (pieBounds.getWidth() / 2.01);
            double xOffsetEnd =
                xCenter + Math.cos(Math.toRadians(angle)) * (pieBounds.getWidth() / endPoint);
            double yOffsetStart =
                yCenter - Math.sin(Math.toRadians(angle)) * (pieBounds.getHeight() / 2.01);
            double yOffsetEnd =
                yCenter - Math.sin(Math.toRadians(angle)) * (pieBounds.getHeight() / endPoint);

            g.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
            Shape line = new Line2D.Double(xOffsetStart, yOffsetStart, xOffsetEnd, yOffsetEnd);
            g.draw(line);

            // annotation
            at.translate(
                xOffset - Math.sin(Math.toRadians(angle - 90)) * labelWidth / 2 + 3, yOffset);
          }

          g.transform(at);
          g.fill(shape);
          g.setTransform(orig);
        }
      }
      // else {
      // System.out.println("Won't fit.");
      // System.out.println("xDiff= " + xDiff);
      // System.out.println("yDiff= " + yDiff);
      // System.out.println("annotationWidth= " + annotationWidth);
      // System.out.println("annotationHeight= " + annotationHeight);
      //
      // }
      // COUNTER_CLOCKWISE, startAngle plus arcAngle
      if (ClockwiseDirectionType.COUNTER_CLOCKWISE == pieStyler.getClockwiseDirectionType()) {
        startAngle += arcAngle;
      }
    }
  }

  private void paintSum(Graphics2D g, Rectangle2D pieBounds, double total) {

    // draw total value if visible
    if (pieStyler.isSumVisible()) {
      String label =
          pieStyler.getSumFormat() == null || pieStyler.getSumFormat().isEmpty()
              ? df.format(total)
              : String.format(pieStyler.getSumFormat(), total);

      TextLayout textLayout =
          new TextLayout(label, pieStyler.getSumFont(), new FontRenderContext(null, true, false));
      Shape shape = textLayout.getOutline(null);
      g.setColor(pieStyler.getChartFontColor());

      // compute center
      Rectangle2D labelRectangle = textLayout.getBounds();
      double xCenter = pieBounds.getX() + pieBounds.getWidth() / 2 - labelRectangle.getWidth() / 2;
      double yCenter =
          pieBounds.getY() + pieBounds.getHeight() / 2 + labelRectangle.getHeight() / 2;

      // set text
      AffineTransform orig = g.getTransform();
      AffineTransform at = new AffineTransform();

      at.translate(xCenter, yCenter);
      g.transform(at);
      g.fill(shape);
      g.setTransform(orig);
    }
  }
}
