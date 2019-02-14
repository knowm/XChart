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
import org.knowm.xchart.style.PieStyler.AnnotationType;

/** @author timmolter */
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

    // g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
    // g.setColor(Color.black);
    // g.draw(pieBounds);

    // get total
    double total = 0.0;

    Map<String, S> map = chart.getSeriesMap();
    for (S series : map.values()) {

      if (!series.isEnabled()) {
        continue;
      }
      total += series.getValue().doubleValue();
    }

    // draw pie slices
    // double curValue = 0.0;
    // double curValue = 0.0;
    double startAngle = pieStyler.getStartAngleInDegrees() + 90;

    paintSlices(g, pieBounds, total, startAngle);
    paintAnnotations(g, pieBounds, total, startAngle);
    paintSum(g, pieBounds, total);
  }

  private void paintSlices(Graphics2D g, Rectangle2D pieBounds, double total, double startAngle) {
    boolean toolTipsEnabled = chart.getStyler().isToolTipsEnabled();

    Map<String, S> map = chart.getSeriesMap();
    for (S series : map.values()) {

      if (!series.isEnabled()) {
        continue;
      }

      Number y = series.getValue();
      Shape labelShape;

      // draw slice/donut
      double arcAngle = (y.doubleValue() * 360 / total);
      g.setColor(series.getFillColor());

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
        labelShape = pieShape;
      }

      // donut
      else {

        Shape donutSlice =
            getDonutSliceShape(pieBounds, pieStyler.getDonutThickness(), startAngle, arcAngle);
        g.fill(donutSlice);
        g.setColor(pieStyler.getPlotBackgroundColor());
        g.draw(donutSlice);
        labelShape = donutSlice;
      }

      // add data labels
      // maybe another option to construct this label
      String annotation = series.getName() + " (" + df.format(y) + ")";

      double xCenter =
          pieBounds.getX() + pieBounds.getWidth() / 2; // - annotationRectangle.getWidth() / 2;
      double yCenter =
          pieBounds.getY() + pieBounds.getHeight() / 2; // + annotationRectangle.getHeight() / 2;
      double angle = (arcAngle + startAngle) - arcAngle / 2;
      double xOffset =
          xCenter
              + Math.cos(Math.toRadians(angle))
                  * (pieBounds.getWidth() / 2 * pieStyler.getAnnotationDistance());
      double yOffset =
          yCenter
              - Math.sin(Math.toRadians(angle))
                  * (pieBounds.getHeight() / 2 * pieStyler.getAnnotationDistance());

      if (toolTipsEnabled) {
        String tt = series.getToolTip();
        if (tt != null) {
          chart.toolTips.addData(labelShape, xOffset, yOffset + 10, 0, tt);
        } else {
          chart.toolTips.addData(labelShape, xOffset, yOffset + 10, 0, annotation);
        }
      }
      startAngle += arcAngle;
    }
  }

  private void paintAnnotations(
      Graphics2D g, Rectangle2D pieBounds, double total, double startAngle) {
    Map<String, S> map = chart.getSeriesMap();
    for (S series : map.values()) {

      if (!series.isEnabled()) {
        continue;
      }

      Number y = series.getValue();

      // draw slice/donut
      double arcAngle = (y.doubleValue() * 360 / total);

      // curValue += y.doubleValue();

      if (pieStyler.hasAnnotations()) {

        // draw annotation
        String annotation = "";
        if (pieStyler.getAnnotationType() == AnnotationType.Value) {

          if (pieStyler.getDecimalPattern() != null) {

            DecimalFormat df = new DecimalFormat(pieStyler.getDecimalPattern());
            annotation = df.format(y);
          } else {
            annotation = y.toString();
          }
        } else if (pieStyler.getAnnotationType() == AnnotationType.Label) {
          annotation = series.getName();
        } else if (pieStyler.getAnnotationType() == AnnotationType.LabelAndPercentage) {
          double percentage = y.doubleValue() / total * 100;
          annotation = series.getName() + " (" + df.format(percentage) + "%)";
        } else if (pieStyler.getAnnotationType() == AnnotationType.Percentage) {
          double percentage = y.doubleValue() / total * 100;
          annotation = df.format(percentage) + "%";
        } else if (pieStyler.getAnnotationType() == AnnotationType.LabelAndValue) {
          if (pieStyler.getDecimalPattern() != null) {
            DecimalFormat df = new DecimalFormat(pieStyler.getDecimalPattern());
            annotation = series.getName() + " (" + df.format(y) + ")";
          } else {
            annotation = series.getName() + " (" + y.toString() + ")";
          }
        }

        TextLayout textLayout =
            new TextLayout(
                annotation,
                pieStyler.getAnnotationsFont(),
                new FontRenderContext(null, true, false));
        Rectangle2D annotationRectangle = textLayout.getBounds();

        double xCenter =
            pieBounds.getX() + pieBounds.getWidth() / 2 - annotationRectangle.getWidth() / 2;
        double yCenter =
            pieBounds.getY() + pieBounds.getHeight() / 2 + annotationRectangle.getHeight() / 2;
        double angle = (arcAngle + startAngle) - arcAngle / 2;
        double xOffset =
            xCenter
                + Math.cos(Math.toRadians(angle))
                    * (pieBounds.getWidth() / 2 * pieStyler.getAnnotationDistance());
        double yOffset =
            yCenter
                - Math.sin(Math.toRadians(angle))
                    * (pieBounds.getHeight() / 2 * pieStyler.getAnnotationDistance());

        // get annotation width
        Shape shape = textLayout.getOutline(null);
        Rectangle2D annotationBounds = shape.getBounds2D();
        double annotationWidth = annotationBounds.getWidth();
        // System.out.println("annotationWidth= " + annotationWidth);
        double annotationHeight = annotationBounds.getHeight();
        // System.out.println("annotationHeight= " + annotationHeight);

        // get slice area
        double xOffset1 =
            xCenter
                + Math.cos(Math.toRadians(startAngle))
                    * (pieBounds.getWidth() / 2 * pieStyler.getAnnotationDistance());
        double yOffset1 =
            yCenter
                - Math.sin(Math.toRadians(startAngle))
                    * (pieBounds.getHeight() / 2 * pieStyler.getAnnotationDistance());
        double xOffset2 =
            xCenter
                + Math.cos(Math.toRadians((arcAngle + startAngle)))
                    * (pieBounds.getWidth() / 2 * pieStyler.getAnnotationDistance());
        double yOffset2 =
            yCenter
                - Math.sin(Math.toRadians((arcAngle + startAngle)))
                    * (pieBounds.getHeight() / 2 * pieStyler.getAnnotationDistance());
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
        boolean annotationWillFit = false;
        if (xDiff >= yDiff) { // assume more vertically orientated slice
          if (annotationWidth < xDiff) {
            annotationWillFit = true;
          }
        } else if (xDiff <= yDiff) { // assume more horizontally orientated slice
          if (annotationHeight < yDiff) {
            annotationWillFit = true;
          }
        }

        // draw annotation
        if (pieStyler.isDrawAllAnnotations() || annotationWillFit) {

          g.setColor(pieStyler.getChartFontColor());
          g.setFont(pieStyler.getAnnotationsFont());
          AffineTransform orig = g.getTransform();
          AffineTransform at = new AffineTransform();

          // inside
          if (pieStyler.getAnnotationDistance() <= 1.0) {
            at.translate(xOffset, yOffset);
          }

          // outside
          else {

            // Tick Mark
            xCenter = pieBounds.getX() + pieBounds.getWidth() / 2;
            yCenter = pieBounds.getY() + pieBounds.getHeight() / 2;
            // double endPoint = Math.min((2.0 - (pieStyler.getAnnotationDistance() - 1)), 1.95);
            double endPoint = (3.0 - pieStyler.getAnnotationDistance());
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
                xOffset - Math.sin(Math.toRadians(angle - 90)) * annotationWidth / 2 + 3, yOffset);
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
      startAngle += arcAngle;
    }
  }

  private void paintSum(Graphics2D g, Rectangle2D pieBounds, double total) {
    // draw total value if visible
    if (pieStyler.isSumVisible()) {
      DecimalFormat totalDf =
              (pieStyler.getDecimalPattern() == null)
                      ? df
                      : new DecimalFormat(pieStyler.getDecimalPattern());

      String annotation = totalDf.format(total);

      TextLayout textLayout =
          new TextLayout(
              annotation, pieStyler.getSumFont(), new FontRenderContext(null, true, false));
      Shape shape = textLayout.getOutline(null);
      g.setColor(pieStyler.getChartFontColor());

      // compute center
      Rectangle2D annotationRectangle = textLayout.getBounds();
      double xCenter =
          pieBounds.getX() + pieBounds.getWidth() / 2 - annotationRectangle.getWidth() / 2;
      double yCenter =
          pieBounds.getY() + pieBounds.getHeight() / 2 + annotationRectangle.getHeight() / 2;

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
