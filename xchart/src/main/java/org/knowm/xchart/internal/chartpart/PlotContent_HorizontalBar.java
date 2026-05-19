package org.knowm.xchart.internal.chartpart;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.*;
import java.util.*;
import org.knowm.xchart.HorizontalBarSeries;
import org.knowm.xchart.internal.Utils;
import org.knowm.xchart.style.HorizontalBarStyler;

public class PlotContent_HorizontalBar<
        ST extends HorizontalBarStyler, S extends HorizontalBarSeries>
    extends PlotContent_<ST, S> {

  private final ST styler;

  /**
   * Constructor
   *
   * @param chart
   */
  PlotContent_HorizontalBar(Chart<ST, S> chart) {

    super(chart);
    this.styler = chart.getStyler();
  }

  @Override
  public void doPaint(Graphics2D g) {

    // X-Axis
    double yTickSpace = styler.getPlotContentSize() * getBounds().getHeight();
    // System.out.println("xTickSpace: " + xTickSpace);
    double yVerticalMargin = Utils.getTickStartOffset(getBounds().getHeight(), yTickSpace);
    // System.out.println("xLeftMargin: " + xLeftMargin);
    Map<String, S> seriesMap = chart.getSeriesMap();
    int numCategories = seriesMap.values().iterator().next().getYData().size();
    double gridStep = yTickSpace / numCategories;
    // System.out.println("gridStep: " + gridStep);

    // Y-Axis
    double xMin = chart.getXAxis().getMin();
    double xMax = chart.getXAxis().getMax();

    // figure out the general form of the chart
    final int chartForm; // 1=positive, -1=negative, 0=span
    if (xMin > 0.0 && xMax > 0.0) {
      chartForm = 1; // positive chart
    } else if (xMin < 0.0 && xMax < 0.0) {
      chartForm = -1; // negative chart
    } else {
      chartForm = 0; // span chart
    }
    // System.out.println(xMin);
    // System.out.println(xMax);
    // System.out.println("chartForm: " + chartForm);

    double xTickSpace = styler.getPlotContentSize() * getBounds().getWidth();

    double xHorizontalMargin = Utils.getTickStartOffset(getBounds().getWidth(), xTickSpace);

    // plot series
    int seriesCounter = 0;

    for (S series : seriesMap.values()) {

      if (!series.isEnabled()) {
        continue;
      }

      xMin = chart.getXAxis().getMin();
      xMax = chart.getXAxis().getMax();
      if (styler.isXAxisLogarithmic()) {
        xMin = Math.log10(xMin);
        xMax = Math.log10(xMax);
      }

      Iterator<? extends Number> xItr = series.getXData().iterator();
      Iterator<?> yItr = series.getYData().iterator();

      int categoryCounter = 0;
      while (xItr.hasNext()) {

        Number next = xItr.next();
        Object nextCat = yItr.next();
        // skip when a value is null
        if (next == null) {

          categoryCounter++;
          continue;
        }

        double xOrig = next.doubleValue();
        double x;
        if (styler.isXAxisLogarithmic()) {
          x = Math.log10(xOrig);
        } else {
          x = xOrig;
        }

        double xRight = 0.0;
        double xLeft = 0.0;
        switch (chartForm) {
          case 1: // positive chart
            // check for points off the chart draw area due to a custom yMin
            if (x < xMin) {
              categoryCounter++;
              continue;
            }
            xRight = x;
            xLeft = xMin;
            break;

          case -1: // negative chart
            // check for points off the chart draw area due to a custom yMin
            if (x > xMax) {
              categoryCounter++;
              continue;
            }
            xRight = xMax;
            xLeft = x;
            break;
          case 0: // span chart
            if (x >= 0.0) { // positive
              xRight = x;
              xLeft = 0.0;
            } else {
              xRight = 0.0;
              xLeft = x;
            }
            break;
          default:
            break;
        }

        double xTransform = (xHorizontalMargin + (xRight - xMin) / (xMax - xMin) * xTickSpace);
        double xOffset = getBounds().getX() + xTransform;

        double zeroTransform = (xHorizontalMargin + (xLeft - xMin) / (xMax - xMin) * xTickSpace);
        double zeroOffset = getBounds().getX() + zeroTransform;
        double yOffset;
        double barHeight;

        {
          double barHeightPercentage = styler.getAvailableSpaceFill();
          barHeight = gridStep / chart.getSeriesMap().size() * barHeightPercentage;
          double barMargin = gridStep * (1 - barHeightPercentage) / 2;
          yOffset =
              getBounds().getY()
                  + yVerticalMargin
                  + gridStep * categoryCounter++
                  + seriesCounter * barHeight
                  + barMargin;
        }

        // paint series
        // paint bar
        Path2D.Double barPath = new Path2D.Double();
        barPath.moveTo(zeroOffset, yOffset);
        barPath.lineTo(xOffset, yOffset);
        barPath.lineTo(xOffset, yOffset + barHeight);
        barPath.lineTo(zeroOffset, yOffset + barHeight);
        barPath.closePath();

        g.setColor(series.getFillColor());
        g.fill(barPath);

        if (styler.isLabelsVisible() && next != null) {
          drawLabels(g, next, xOffset, yOffset, zeroOffset, barHeight, series.getFillColor());
        }

        // add data labels
        if (chart.getStyler().isToolTipsEnabled()) {
          Rectangle2D.Double rect =
              new Rectangle2D.Double(
                  zeroOffset, yOffset, Math.abs(xOffset - zeroOffset), barHeight);
          double xPoint;
          if (x < 0) {
            xPoint = -zeroOffset;
          } else {
            xPoint = xOffset;
          }

          toolTips.addData(
              rect,
              xPoint,
              yOffset,
              barHeight,
              chart.getXAxisFormat().format(xOrig),
              chart.getYAxisFormat().format(nextCat));
        }
      }

      seriesCounter++;
    }
  }

  private void drawLabels(
      Graphics2D g,
      Number next,
      double xOffset,
      double yOffset,
      double zeroOffset,
      double barHeight,
      Color seriesColor) {

    String numberAsString = chart.getXAxisFormat().format(next);

    TextLayout textLayout =
        new TextLayout(
            numberAsString, styler.getLabelsFont(), new FontRenderContext(null, true, false));

    AffineTransform rot =
        AffineTransform.getRotateInstance(-1 * Math.toRadians(styler.getLabelsRotation()), 0, 0);
    Shape shape = textLayout.getOutline(rot);
    Rectangle2D labelRectangle = textLayout.getBounds();

    double labelY;
    if (styler.getLabelsRotation() > 0) {
      double labelYDelta = labelRectangle.getWidth() / 2 - labelRectangle.getHeight() / 2;
      double rotationOffset = labelYDelta * styler.getLabelsRotation() / 90;
      labelY = yOffset + barHeight / 2 + labelRectangle.getHeight() / 2 + rotationOffset + 1;
    } else {
      labelY = yOffset + barHeight / 2 + labelRectangle.getHeight() / 2;
    }
    double labelX;

    if (next.doubleValue() >= 0.0) {
      labelX =
          xOffset
              + (zeroOffset - xOffset) * (1 - styler.getLabelsPosition())
              - labelRectangle.getWidth() * styler.getLabelsPosition();
    } else {
      labelX =
          zeroOffset
              - (zeroOffset - xOffset) * (1 - styler.getLabelsPosition())
              - labelRectangle.getWidth() * (1 - styler.getLabelsPosition());
    }

    if (styler.isLabelsFontColorAutomaticEnabled()) {
      g.setColor(styler.getLabelsFontColor(seriesColor));
    } else {
      g.setColor(styler.getLabelsFontColor());
    }

    g.setFont(styler.getLabelsFont());
    AffineTransform orig = g.getTransform();
    AffineTransform at = new AffineTransform();
    at.translate(labelX, labelY);
    g.transform(at);
    g.fill(shape);
    g.setTransform(orig);
  }
}
