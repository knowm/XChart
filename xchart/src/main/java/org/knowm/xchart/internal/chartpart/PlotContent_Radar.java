package org.knowm.xchart.internal.chartpart;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Map;
import org.knowm.xchart.RadarChart;
import org.knowm.xchart.RadarSeries;
import org.knowm.xchart.style.RadarStyler;

public class PlotContent_Radar<ST extends RadarStyler, S extends RadarSeries>
    extends PlotContent_<ST, S> {

  private final RadarStyler styler;
  private static final NumberFormat df = DecimalFormat.getPercentInstance();

  /**
   * Constructor
   *
   * @param chart
   */
  PlotContent_Radar(Chart<ST, S> chart) {

    super(chart);
    styler = chart.getStyler();
  }

  @Override
  public void doPaint(Graphics2D g) {

    // pre-calculate some often-used values

    double boundsWidth = getBounds().getWidth();
    double boundsHeight = getBounds().getHeight();

    double radarWidth;
    double radarHeight;
    if (styler.isCircular()) {
      radarWidth =
          Math.min(boundsWidth, boundsHeight) * styler.getPlotContentSize()
              - 2 * styler.getRadiiTitlePadding();
      radarHeight = radarWidth;
    } else {
      radarWidth = boundsWidth * styler.getPlotContentSize() - 2 * styler.getRadiiTitlePadding();
      radarHeight = boundsHeight * styler.getPlotContentSize() - 2 * styler.getRadiiTitlePadding();
    }

    double radarStartX = getBounds().getX() + boundsWidth / 2.0 - radarWidth / 2.0;
    double radarStartY = getBounds().getY() + boundsHeight / 2.0 - radarHeight / 2.0;
    double radarRadiusX = radarWidth / 2.0;
    double radarRadiusY = radarHeight / 2.0;
    double radarCenterX = radarStartX + radarWidth / 2;
    double radarCenterY = radarStartY + radarHeight / 2;

    String[] radiiLabels = ((RadarChart) chart).getRadiiLabels();
    double radiiAngle = 360.0 / radiiLabels.length;
    int numRadiiLabels = radiiLabels.length;
    double[] cosArr = new double[numRadiiLabels];
    double[] sinArr = new double[numRadiiLabels];
    double startAngle = styler.getStartAngleInDegrees() + 90;
    for (int i = 0; i < numRadiiLabels; i++) {
      double radians = Math.toRadians(startAngle);
      double cos = Math.cos(radians);
      double sin = Math.sin(radians);
      cosArr[i] = cos;
      sinArr[i] = sin;
      startAngle += radiiAngle;
    }

    // paint radii lines and labels
    startAngle = styler.getStartAngleInDegrees() + 90;
    for (int i = 0; i < numRadiiLabels; i++) {
      double cos = cosArr[i];
      double sin = sinArr[i];

      // draw radii lines
      if (styler.isRadiiTicksMarksVisible()) {

        double xOffset = radarCenterX + cos * radarRadiusX;
        double yOffset = radarCenterY - sin * radarRadiusY;
        Line2D.Double line = new Line2D.Double(radarCenterX, radarCenterY, xOffset, yOffset);

        g.setColor(styler.getRadiiTickMarksColor());
        g.setStroke(styler.getRadiiTickMarksStroke());
        g.draw(line);
      }

      // draw radii labels
      if (styler.isRadiiTitleVisible()) {

        String radiiLabel = radiiLabels[i];
        TextLayout textLayout =
            new TextLayout(
                radiiLabel, styler.getRadiiTitleFont(), new FontRenderContext(null, true, false));
        Shape shape = textLayout.getOutline(null);
        Rectangle2D labelBounds = shape.getBounds2D();
        double labelWidth = labelBounds.getWidth();
        double labelHeight = labelBounds.getHeight();
        double xOffset = radarCenterX + cos * radarRadiusX;
        double yOffset = radarCenterY - sin * radarRadiusY;

        xOffset =
            xOffset
                - Math.sin(Math.toRadians(startAngle - 90))
                    * (labelWidth / 1.5 + styler.getRadiiTitlePadding())
                - (labelWidth / 2);
        yOffset =
            yOffset
                - (Math.cos(Math.toRadians(startAngle - 90)) - 1) * labelHeight / 2
                - (Math.cos(Math.toRadians(startAngle - 90)) * 1.4 * styler.getRadiiTitlePadding());

        g.setColor(styler.getAnnotationsFontColor());
        g.setFont(styler.getAnnotationsFont());
        AffineTransform orig = g.getTransform();
        AffineTransform at = new AffineTransform();

        at.translate(xOffset, yOffset);

        g.transform(at);
        g.fill(shape);
        g.setTransform(orig);
      }

      startAngle += radiiAngle;
    }

    // draw radii tick marks ie. concentric circles/polygons
    int markCount = styler.getRadiiTickMarksCount();
    if (markCount > 0 && styler.isRadiiTicksMarksVisible()) {
      g.setColor(styler.getRadiiTickMarksColor());
      g.setStroke(styler.getRadiiTickMarksStroke());
      // draw circular tick mark
      if (styler.getRadarRenderStyle() == RadarStyler.RadarRenderStyle.Circle) {
        Ellipse2D.Double markShape = new Ellipse2D.Double(0, 0, 0, 0);
        double winc = radarRadiusX / markCount;
        double hinc = radarRadiusY / markCount;

        double newXd = radarRadiusX;
        double newYd = radarRadiusY;
        for (int i = 0; i < markCount; i++) {
          markShape.width = newXd * 2;
          markShape.height = newYd * 2;
          markShape.x = radarCenterX - newXd;
          markShape.y = radarCenterY - newYd;

          if (i == 0) {
            g.setColor(styler.getRadiiTickMarksColor().darker());
            //            g.setStroke(styler.getAxisTickMarksStroke());
          } else {
            g.setColor(styler.getRadiiTickMarksColor());
          }

          g.draw(markShape);
          newXd -= winc;
          newYd -= hinc;
        }
      }

      // draw polygon tick marks
      else if (styler.getRadarRenderStyle() == RadarStyler.RadarRenderStyle.Polygon) {
        double winc = radarRadiusX / markCount;
        double hinc = radarRadiusY / markCount;

        for (int markerInd = 0; markerInd < markCount; markerInd++) {
          Path2D.Double path = new Path2D.Double();
          for (int varInd = 0; varInd < numRadiiLabels; varInd++) {
            double cos = cosArr[varInd];
            double sin = sinArr[varInd];
            double xOffset = radarCenterX + cos * (radarRadiusX - markerInd * winc);
            double yOffset = radarCenterY - sin * (radarRadiusY - markerInd * hinc);

            if (varInd == 0) {
              path.moveTo(xOffset, yOffset);
            } else {
              path.lineTo(xOffset, yOffset);
            }
          }
          path.closePath();
          if (markerInd == 0) {
            g.setColor(styler.getRadiiTickMarksColor().darker());
            //            g.setStroke(styler.getAxisTickMarksStroke());
          } else {
            g.setColor(styler.getRadiiTickMarksColor());
          }
          g.draw(path);
        }
      }
    }

    // series lines and markers and Tooltips
    NumberFormat decimalFormat =
        (styler.getDecimalPattern() == null) ? df : new DecimalFormat(styler.getDecimalPattern());
    Map<String, S> map = chart.getSeriesMap();
    for (S series : map.values()) {

      if (!series.isEnabled()) {
        continue;
      }

      double[] values = series.getValues();
      String[] tooltipOverrides = series.getTooltipOverrides();

      g.setColor(series.getFillColor());

      Path2D.Double path = new Path2D.Double();
      for (int i = 0; i < numRadiiLabels; i++) {

        double cos = cosArr[i];
        double sin = sinArr[i];

        double value = values[i];
        double xOffset = radarCenterX + cos * (radarRadiusX * value);
        double yOffset = radarCenterY - sin * (radarRadiusY * value);

        if (i == 0) {
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
          if (tooltipOverrides != null) {
            label = tooltipOverrides[i];
          }
          if (label == null) {
            String ystr = decimalFormat.format(value);
            label = series.getName() + " (" + radiiLabels[i] + ": " + ystr + ")";
          }
          this.toolTips.addData(xOffset, yOffset, label);
        }
      }
      path.closePath();
      g.setStroke(series.getLineStyle());
      g.setColor(series.getLineColor());
      g.draw(path);
      if (styler.isSeriesFilled()) {
        g.setColor(series.getFillColor());
        g.fill(path);
      }
    }
  }
}
