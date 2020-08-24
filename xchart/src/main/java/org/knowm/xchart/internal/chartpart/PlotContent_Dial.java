package org.knowm.xchart.internal.chartpart;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Map;
import org.knowm.xchart.DialSeries;
import org.knowm.xchart.style.DialStyler;

public class PlotContent_Dial<ST extends DialStyler, S extends DialSeries>
    extends PlotContent_<ST, S> {

  private final ST styler;
  private final NumberFormat df = DecimalFormat.getPercentInstance();
  private double height_r;

  PlotContent_Dial(Chart<ST, S> chart) {

    super(chart);
    styler = chart.getStyler();
  }

  @Override
  public void doPaint(Graphics2D g) {

    Rectangle2D pieBounds = getPieBounds();

    // get total
    boolean axisTickLabelsVisible = styler.isAxisTickLabelsVisible();
    double arcAngle = styler.getArcAngle();
    double donutThickness = styler.getDonutThickness();
    int axisTitlePadding = styler.getAxisTitlePadding();

    double[] axisTickValues = styler.getAxisTickValues();
    int markCount = axisTickValues.length;
    String[] axisTickLabels = styler.getAxisTickLabels();

    double[] fromArr = {styler.getNormalFrom(), styler.getGreenFrom(), styler.getRedFrom()};
    double[] toArr = {styler.getNormalTo(), styler.getGreenTo(), styler.getRedTo()};
    Color[] donutColorArr = {styler.getNormalColor(), styler.getGreenColor(), styler.getRedColor()};

    double dountStartAngle = (arcAngle) / 2 + 90;
    // draw shape
    for (int i = 0; i < donutColorArr.length; i++) {
      double from = fromArr[i];
      double to = toArr[i];
      if (to <= from || to < 0 || from < 0) {
        continue;
      }
      double totalAngle = (to - from) * arcAngle;
      double startAngle = dountStartAngle - from * arcAngle - totalAngle;
      Shape donutSlice =
          PlotContent_Pie.getDonutSliceShape(pieBounds, donutThickness, startAngle, totalAngle);
      g.setColor(donutColorArr[i]);
      g.fill(donutSlice);
      g.draw(donutSlice);
    }

    double xDiameter = pieBounds.getWidth() / 2;
    double yDiameter = pieBounds.getHeight() / 2;

    double xCenter = pieBounds.getX() + xDiameter;
    double yCenter = pieBounds.getY() + yDiameter;

    if (markCount > 0 && styler.isAxisTicksMarksVisible()) {
      g.setColor(styler.getAxisTickMarksColor());
      g.setStroke(styler.getAxisTickMarksStroke());

      for (int i = 0; i < markCount; i++) {
        double angle = -axisTickValues[i] * arcAngle + (arcAngle) / 2 + 90;
        double radians = Math.toRadians(angle);
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);

        double xOffset = xCenter + cos * xDiameter;
        double yOffset = yCenter - sin * yDiameter;
        double xOffset2 = xCenter + cos * xDiameter * (1 - donutThickness);
        double yOffset2 = yCenter - sin * yDiameter * (1 - donutThickness);

        Line2D.Double line = new Line2D.Double(xOffset2, yOffset2, xOffset, yOffset);
        g.setColor(styler.getAxisTickMarksColor());
        g.setStroke(styler.getAxisTickMarksStroke());
        g.draw(line);

        if (!axisTickLabelsVisible) {
          continue;
        }
        String annotation = axisTickLabels[i];

        TextLayout textLayout =
            new TextLayout(
                annotation, styler.getAxisTitleFont(), new FontRenderContext(null, true, false));

        Rectangle2D annotationBounds = textLayout.getBounds();
        double annotationWidth = annotationBounds.getWidth();
        double annotationHeight = annotationBounds.getHeight();

        // calculate corrections
        double xc;
        double yc = 0;
        if (axisTickValues[i] < 0.49) {
          xc = 0;
        } else if (axisTickValues[i] > 0.51) {
          xc = -annotationWidth;
        } else {
          xc = -annotationWidth / 2;
          yc = annotationHeight / 2;
        }
        xOffset2 = xCenter + cos * (xDiameter - axisTitlePadding) * (1 - donutThickness);
        yOffset2 = yCenter - sin * (yDiameter - axisTitlePadding) * (1 - donutThickness);

        double tx = xOffset2 + xc;
        double ty = yOffset2 + yc + annotationHeight / 2;

        g.setColor(styler.getChartFontColor());
        g.setFont(styler.getAnnotationsFont());
        AffineTransform orig = g.getTransform();
        AffineTransform at = new AffineTransform();

        at.translate(tx, ty);

        g.transform(at);
        textLayout.draw(g, 0, 0);
        g.setTransform(orig);
      }
    }

    Map<String, S> map = chart.getSeriesMap();
    for (S series : map.values()) {
      if (!series.isEnabled()) {
        continue;
      }

      // draw title
      if (styler.isAxisTitleVisible()) {
        TextLayout textLayout =
            new TextLayout(
                series.getName(),
                styler.getAxisTitleFont(),
                new FontRenderContext(null, true, false));

        Rectangle2D annotationBounds = textLayout.getBounds();
        double annotationWidth = annotationBounds.getWidth();
        double annotationHeight = annotationBounds.getHeight();

        // calculate corrections
        double tx = xCenter - annotationWidth / 2;
        double ty = yCenter - yDiameter / 2 + annotationHeight / 2;

        g.setColor(styler.getChartFontColor());
        g.setFont(styler.getAxisTitleFont());
        textLayout.draw(g, (float) tx, (float) ty);
      }

      double value = series.getValue();
      // draw title
      if (styler.hasAnnotations()) {
        String annotation = series.getAnnotation();
        if (annotation == null) {
          if (styler.getDecimalPattern() != null) {
            DecimalFormat df = new DecimalFormat(styler.getDecimalPattern());
            annotation = df.format(value);
          } else {
            annotation = df.format(value);
          }
        }
        if (!annotation.isEmpty()) {
          TextLayout textLayout =
              new TextLayout(
                  annotation,
                  styler.getAnnotationsFont(),
                  new FontRenderContext(null, true, false));

          Rectangle2D annotationBounds = textLayout.getBounds();
          double annotationWidth = annotationBounds.getWidth();
          double annotationHeight = annotationBounds.getHeight();

          double tx = xCenter - annotationWidth / 2;
          double ty = yCenter + annotationHeight / 2;
          if (styler.getArcAngle() > 180) {
            ty += height_r * Math.cos(Math.toRadians((360 - styler.getArcAngle()) / 2)) / 2;
          } else {
            ty -= yDiameter / 4;
          }

          g.setColor(styler.getChartFontColor());
          g.setFont(styler.getAxisTitleFont());
          textLayout.draw(g, (float) tx, (float) ty);
        }
      }

      // draw arrow
      double angle = -value * arcAngle + (arcAngle) / 2 + 90;

      double radians = Math.toRadians(angle);
      double arrowLengthPercentage = styler.getArrowLengthPercentage();
      double arrowArcAngle = styler.getArrowArcAngle();
      double arrowArcPercentage = styler.getArrowArcPercentage();
      double xOffset = xCenter + Math.cos(radians) * (xDiameter * arrowLengthPercentage);
      double yOffset = yCenter - Math.sin(radians) * (yDiameter * arrowLengthPercentage);

      Path2D.Double path = new Path2D.Double();
      if (styler.isToolTipsEnabled()) {
        String annotation = series.getAnnotation();
        if (annotation == null) {
          if (styler.getDecimalPattern() != null) {
            DecimalFormat df = new DecimalFormat(styler.getDecimalPattern());
            annotation = df.format(value);
          } else {
            annotation = df.format(value);
          }
        }
        chart.toolTips.addData(path, xOffset, yOffset + 10, 0, annotation);
      }
      path.moveTo(xCenter, yCenter);

      double[][] angleValues = {
        {-arrowArcAngle, arrowArcPercentage}, {0, 1}, {arrowArcAngle, arrowArcPercentage}
      };
      for (double[] ds : angleValues) {
        radians = Math.toRadians(angle - ds[0]);

        double diameterPerct = arrowLengthPercentage * ds[1];
        xOffset = xCenter + Math.cos(radians) * (xDiameter * diameterPerct);
        yOffset = yCenter - Math.sin(radians) * (yDiameter * diameterPerct);
        path.lineTo(xOffset, yOffset);
      }

      path.closePath();
      g.setColor(series.getFillColor());
      g.fill(path);
      g.setColor(series.getLineColor());
      g.draw(path);
    }
  }

  private Rectangle2D getPieBounds() {

    double pieFillPercentage = styler.getPlotContentSize();
    double halfBorderPercentage = (1 - pieFillPercentage) / 2.0;

    double boundsWidth = getBounds().getWidth();
    double boundsHeight = getBounds().getHeight();
    double pieBounds_x = getBounds().getX();
    double pieBounds_y = getBounds().getY();
    double pieBounds_w = 0.0;
    double pieBounds_h = 0.0;

    double r = boundsHeight * pieFillPercentage / 2;
    if (styler.isCircular()) {
      if (styler.getArcAngle() > 180) {
        double cos = Math.cos(Math.toRadians((360 - styler.getArcAngle()) / 2));
        r = r + r * (1 - cos) / (1 + cos);
        if (2 * r > boundsWidth * pieFillPercentage) {
          r = boundsWidth * pieFillPercentage / 2;
          pieBounds_x += boundsWidth * halfBorderPercentage;
          pieBounds_y += (boundsHeight - r - r * cos) / 2;
        } else {
          pieBounds_x += boundsWidth / 2 - r;
          pieBounds_y += boundsHeight * halfBorderPercentage;
        }
      } else {
        r = boundsHeight * pieFillPercentage;
        double sin = Math.sin(Math.toRadians(styler.getArcAngle() / 2));
        if (2 * sin * r > boundsWidth * pieFillPercentage) {
          r = boundsWidth * pieFillPercentage / 2 / sin;
          pieBounds_x += boundsWidth * halfBorderPercentage - r * (1 - sin);
          pieBounds_y += (boundsHeight - r) / 2;
        } else {
          pieBounds_x += boundsWidth / 2 - r;
          pieBounds_y += boundsHeight * halfBorderPercentage;
        }
      }
      pieBounds_w = r * 2;
      pieBounds_h = r * 2;
    } else {
      pieBounds_x += boundsWidth * halfBorderPercentage;
      pieBounds_y += boundsHeight * halfBorderPercentage;
      pieBounds_w = boundsWidth * pieFillPercentage;

      if (styler.getArcAngle() > 180) {

        double cos = Math.cos(Math.toRadians((360 - styler.getArcAngle()) / 2));
        r = r + r * (1 - cos) / (1 + cos);
        pieBounds_h = r * 2;
      } else {
        pieBounds_h = boundsHeight * pieFillPercentage * 2;
      }
    }

    height_r = r;
    Rectangle2D pieBounds =
        new Rectangle2D.Double(pieBounds_x, pieBounds_y, pieBounds_w, pieBounds_h);

    return pieBounds;
  }
}
