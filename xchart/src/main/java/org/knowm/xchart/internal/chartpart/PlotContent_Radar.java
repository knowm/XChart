package org.knowm.xchart.internal.chartpart;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Map;
import org.knowm.xchart.RadarChart;
import org.knowm.xchart.RadarChart.RadarRenderStyle;
import org.knowm.xchart.RadarSeries;
import org.knowm.xchart.style.RadarStyler;

public class PlotContent_Radar<ST extends RadarStyler, S extends RadarSeries>
    extends PlotContent_<ST, S> {

  private static final int MARGIN = 5;
  private final RadarStyler styler;
  private final NumberFormat df = DecimalFormat.getPercentInstance();

  double radarX;
  double radarY;
  double xCenter;
  double yCenter;
  double xDiameter;
  double yDiameter;

  PlotContent_Radar(Chart<ST, S> chart) {

    super(chart);
    styler = chart.getStyler();
  }

  protected void calculatePlotVariables(double widthCorrection, double heightCorrection) {

    double fillPercentage = styler.getPlotContentSize();
    double boundsWidth = getBounds().getWidth();
    double boundsHeight = getBounds().getHeight();

    double halfBorderPercentage = (1 - fillPercentage) / 2.0;

    double width;
    double height;
    if (styler.isCircular()) {
      width = Math.min(boundsWidth, boundsHeight);
      height = width;
    } else {
      width = boundsWidth;
      height = boundsHeight;
    }
    double radarW;
    double radarH;

    radarX = getBounds().getX() + boundsWidth / 2 - width / 2 + halfBorderPercentage * width;
    radarY = getBounds().getY() + boundsHeight / 2 - height / 2 + halfBorderPercentage * height;
    radarW = width * fillPercentage;
    radarH = height * fillPercentage;
    xDiameter = (radarW - widthCorrection) / 2;
    yDiameter = (radarH - heightCorrection) / 2;

    xCenter = radarX + radarW / 2;
    yCenter = radarY + radarH / 2;
  }

  @Override
  public void doPaint(Graphics2D g) {

    calculatePlotVariables(0, 0);

    String[] variableLabels = ((RadarChart) chart).getVariableLabels();
    int variableCount = variableLabels.length;

    Map<String, S> map = chart.getSeriesMap();
    RadarChart radarChart = (RadarChart) chart;

    double angleForSeries = 360.0 / variableCount;

    double[] cosArr = new double[variableCount];
    double[] sinArr = new double[variableCount];

    Shape[] labelShapes = null;
    double[] labelX = null;
    double[] labelY = null;

    boolean axisTitleVisible = styler.isAxisTitleVisible();
    if (axisTitleVisible) {
      labelShapes = new Shape[variableCount];
      labelX = new double[variableCount];
      labelY = new double[variableCount];
    }

    double startAngle = styler.getStartAngleInDegrees() + 90;
    for (int i = 0; i < variableCount; i++) {
      double radians = Math.toRadians(startAngle);
      double cos = Math.cos(radians);
      double sin = Math.sin(radians);
      cosArr[i] = cos;
      sinArr[i] = sin;

      if (axisTitleVisible) {
        String annotation = variableLabels[i];

        TextLayout textLayout =
            new TextLayout(
                annotation, styler.getAxisTitleFont(), new FontRenderContext(null, true, false));
        Shape shape = textLayout.getOutline(null);
        labelShapes[i] = shape;
      }
      startAngle += angleForSeries;
    }

    if (axisTitleVisible) {
      Rectangle clipBounds = g.getClipBounds();

      double leftEdge = clipBounds.getX() + MARGIN;
      double rightEdge = clipBounds.getMaxX() - MARGIN * 2;

      double topEdge = clipBounds.getY() + MARGIN;
      double bottomEdge = clipBounds.getMaxY() - MARGIN * 2;
      startAngle = styler.getStartAngleInDegrees() + 90;

      int tryCount = 0;

      int axisTitlePadding = styler.getAxisTitlePadding();
      for (int i = 0; i < variableCount; i++) {
        double cos = cosArr[i];
        double sin = sinArr[i];

        Shape shape = labelShapes[i];
        Rectangle2D annotationBounds = shape.getBounds2D();
        double annotationWidth = annotationBounds.getWidth();
        double annotationHeight = annotationBounds.getHeight();

        double xOffset = xCenter - annotationWidth / 2 + cos * (xDiameter + axisTitlePadding);
        double yOffset = yCenter + annotationHeight / 2 - sin * (yDiameter + axisTitlePadding);
        double tx =
            xOffset
                - Math.sin(Math.toRadians(startAngle - 90))
                    * (annotationWidth / 2 + axisTitlePadding);
        double ty;

        if (Math.abs(startAngle - 90) <= 15 || Math.abs(startAngle - 270) <= 15) {
          ty = yOffset;
        } else {
          ty = yOffset + Math.cos(Math.toRadians(startAngle - 90)) * annotationHeight;
        }

        double x = tx;
        double y = ty;

        x = Math.max(x, leftEdge);
        x = Math.min(x, rightEdge - annotationWidth);
        y = Math.max(y, topEdge);
        y = Math.min(y, bottomEdge - annotationHeight);

        double wcorr = Math.abs(x - tx);
        double hcorr = Math.abs(y - ty);
        if (wcorr > 0 || hcorr > 0) {
          if (tryCount < variableCount) {
            if (wcorr > 0) {
              xDiameter -= Math.abs(wcorr / cos);
            }
            if (hcorr > 0) {
              yDiameter -= Math.abs(hcorr / sin);
            }

            if (styler.isCircular()) {
              xDiameter = Math.min(xDiameter, yDiameter);
              yDiameter = xDiameter;
            }

            tryCount++;
            i = -1;
            startAngle = styler.getStartAngleInDegrees() + 90;
            continue;
          }
        }

        labelX[i] = x;
        labelY[i] = y;

        startAngle += angleForSeries;
      }
    }

    startAngle = styler.getStartAngleInDegrees() + 90;
    for (int i = 0; i < variableCount; i++) {
      double cos = cosArr[i];
      double sin = sinArr[i];

      // draw grid lines
      if (styler.isPlotGridLinesVisible()) {
        double xOffset = xCenter + cos * xDiameter;
        double yOffset = yCenter - sin * yDiameter;
        Line2D.Double line = new Line2D.Double(xCenter, yCenter, xOffset, yOffset);
        g.setColor(styler.getPlotGridLinesColor());
        g.setStroke(styler.getPlotGridLinesStroke());
        g.draw(line);
      }

      // draw variable names
      if (axisTitleVisible) {
        g.setColor(styler.getChartFontColor());
        g.setFont(styler.getAnnotationsFont());
        AffineTransform orig = g.getTransform();
        AffineTransform at = new AffineTransform();

        at.translate(labelX[i], labelY[i]);

        Shape shape = labelShapes[i];
        g.transform(at);
        g.fill(shape);
        g.setTransform(orig);
      }

      startAngle += angleForSeries;
    }

    int markCount = styler.getAxisTickMarksCount();

    if (markCount > 0 && styler.isAxisTicksMarksVisible()) {
      g.setColor(styler.getAxisTickMarksColor());
      g.setStroke(styler.getAxisTickMarksStroke());
      // draw circular marker
      if (radarChart.getRadarRenderStyle() == RadarRenderStyle.Circle) {
        Ellipse2D.Double markShape = new Ellipse2D.Double(0, 0, 0, 0);
        double winc = xDiameter / markCount;
        double hinc = yDiameter / markCount;

        double newXd = xDiameter;
        double newYd = yDiameter;
        for (int i = 0; i < markCount; i++) {
          markShape.width = newXd * 2;
          markShape.height = newYd * 2;
          markShape.x = xCenter - newXd;
          markShape.y = yCenter - newYd;
          g.draw(markShape);
          newXd -= winc;
          newYd -= hinc;
        }
      }

      // draw polygon marker
      if (radarChart.getRadarRenderStyle() == RadarRenderStyle.Polygon) {
        double winc = xDiameter / markCount;
        double hinc = yDiameter / markCount;

        for (int markerInd = 0; markerInd < markCount; markerInd++) {
          Path2D.Double path = new Path2D.Double();
          for (int varInd = 0; varInd < variableCount; varInd++) {
            double cos = cosArr[varInd];
            double sin = sinArr[varInd];
            double xOffset = xCenter + cos * (xDiameter - markerInd * winc);
            double yOffset = yCenter - sin * (yDiameter - markerInd * hinc);

            if (varInd == 0) {
              path.moveTo(xOffset, yOffset);
            } else {
              path.lineTo(xOffset, yOffset);
            }
          }
          path.closePath();
          g.draw(path);
        }
      }
    }

    Path2D.Double[] paths = new Path2D.Double[variableCount];
    for (int i = 0; i < paths.length; i++) {
      paths[i] = new Path2D.Double();
    }

    NumberFormat decimalFormat =
        (styler.getDecimalPattern() == null) ? df : new DecimalFormat(styler.getDecimalPattern());

    for (S series : map.values()) {

      if (!series.isEnabled()) {
        continue;
      }

      double[] values = series.getValues();
      String[] toolTips = series.getTooltipOverrides();

      g.setColor(series.getFillColor());

      Path2D.Double path = new Path2D.Double();
      for (int varInd = 0; varInd < variableCount; varInd++) {
        double cos = cosArr[varInd];
        double sin = sinArr[varInd];

        double perct = values[varInd];
        double xOffset = xCenter + cos * (xDiameter * perct);
        double yOffset = yCenter - sin * (yDiameter * perct);

        if (varInd == 0) {
          path.moveTo(xOffset, yOffset);
        } else {
          path.lineTo(xOffset, yOffset);
        }

        // paint marker
        if (series.getMarker() != null) {
          g.setColor(series.getMarkerColor());
          series.getMarker().paint(g, xOffset, yOffset, styler.getMarkerSize());
        }

        // add data labels
        if (chart.getStyler().isToolTipsEnabled()) {
          String label = null;
          if (toolTips != null) {
            label = toolTips[varInd];
          }
          if (label == null) {
            String ystr = decimalFormat.format(perct);
            label = series.getName() + " (" + variableLabels[varInd] + ": " + ystr + ")";
          }
          chart.toolTips.addData(xOffset, yOffset, label);
        }
      }
      path.closePath();
      g.setColor(series.getLineColor());
      g.draw(path);
      g.setColor(series.getFillColor());
      g.fill(path);
    }
  }
}
