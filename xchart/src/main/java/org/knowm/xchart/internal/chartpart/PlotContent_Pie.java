/**
 * Copyright 2015-2017 Knowm Inc. (http://knowm.org) and contributors.
 * Copyright 2011-2015 Xeiam LLC (http://xeiam.com) and contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.knowm.xchart.internal.chartpart;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Arc2D.Double;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.util.Map;

import org.knowm.xchart.PieSeries;
import org.knowm.xchart.PieSeries.PieSeriesRenderStyle;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.style.PieStyler;
import org.knowm.xchart.style.PieStyler.AnnotationType;
import org.knowm.xchart.style.label.DataLabeller;
import org.knowm.xchart.style.Styler;

/**
 * @author timmolter
 */
public class PlotContent_Pie<ST extends Styler, S extends Series> extends PlotContent_ {

  PieStyler pieStyler;
  DecimalFormat df = new DecimalFormat("#.0");

  /**
   * Constructor
   *
   * @param chart
   */
  protected PlotContent_Pie(Chart<PieStyler, PieSeries> chart) {

    super(chart);
    pieStyler = chart.getStyler();
  }

  @Override
  public void doPaint(Graphics2D g) {

    // pie getBounds()
    double pieFillPercentage = pieStyler.getPlotContentSize();

    double halfBorderPercentage = (1 - pieFillPercentage) / 2.0;
    double width = pieStyler.isCircular() ? Math.min(getBounds().getWidth(), getBounds().getHeight()) : getBounds().getWidth();
    double height = pieStyler.isCircular() ? Math.min(getBounds().getWidth(), getBounds().getHeight()) : getBounds().getHeight();

    Rectangle2D pieBounds = new Rectangle2D.Double(

        getBounds().getX() + getBounds().getWidth() / 2 - width / 2 + halfBorderPercentage * width,

        getBounds().getY() + getBounds().getHeight() / 2 - height / 2 + halfBorderPercentage * height,

        width * pieFillPercentage,

        height * pieFillPercentage);

    // g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
    // g.setColor(Color.black);
    // g.draw(pieBounds);

    // get total
    double total = 0.0;

    Map<String, PieSeries> map = chart.getSeriesMap();
    for (PieSeries series : map.values()) {

      if (!series.isEnabled()) {
        continue;
      }
      total += series.getValue().doubleValue();
    }

    // draw total value if visible
    if (pieStyler.isSumVisible()) {
    	DecimalFormat totalDf = (pieStyler.getDecimalPattern() == null) ? df : new DecimalFormat(pieStyler.getDecimalPattern());

    	String annotation = totalDf.format(total);

        TextLayout textLayout = new TextLayout(annotation, pieStyler.getSumFont(), new FontRenderContext(null, true, false));
        Shape shape = textLayout.getOutline(null);
        g.setColor(pieStyler.getChartFontColor());

        // compute center
        Rectangle2D annotationRectangle = textLayout.getBounds();
        double xCenter = pieBounds.getX() + pieBounds.getWidth() / 2 - annotationRectangle.getWidth() / 2;
        double yCenter = pieBounds.getY() + pieBounds.getHeight() / 2 + annotationRectangle.getHeight() / 2;

        // set text
        AffineTransform orig = g.getTransform();
        AffineTransform at = new AffineTransform();

        at.translate(xCenter, yCenter);
        g.transform(at);
        g.fill(shape);
        g.setTransform(orig);

    }

    // draw pie slices
    // double curValue = 0.0;
    // double curValue = 0.0;
    double startAngle = pieStyler.getStartAngleInDegrees() + 90;
    DataLabeller dataLabeller = pieStyler.getDataLabeller();
    if(dataLabeller != null) {
      dataLabeller.startPaint(g);
    }

    map = chart.getSeriesMap();
    for (PieSeries series : map.values()) {

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

        Double pieShape = new Arc2D.Double(pieBounds.getX(), pieBounds.getY(), pieBounds.getWidth(), pieBounds.getHeight(), startAngle, arcAngle, Arc2D.PIE);
        g.fill(pieShape);
        g.setColor(pieStyler.getPlotBackgroundColor());
        g.draw(pieShape);
        labelShape = pieShape;
      }

      // donut
      else {

        Shape donutSlice = getDonutSliceShape(pieBounds, pieStyler.getDonutThickness(), startAngle, arcAngle);
        g.fill(donutSlice);
        g.setColor(pieStyler.getPlotBackgroundColor());
        g.draw(donutSlice);
        labelShape = donutSlice;
      }

      // curValue += y.doubleValue();

      if (pieStyler.hasAnnotations()) {

        // draw annotation
        String annotation = "";
        if (pieStyler.getAnnotationType() == AnnotationType.Value) {

          if (pieStyler.getDecimalPattern() != null) {

            DecimalFormat df = new DecimalFormat(pieStyler.getDecimalPattern());
            annotation = df.format(y);
          }
          else {
            annotation = y.toString();
          }
        }
        else if (pieStyler.getAnnotationType() == AnnotationType.Label) {
          annotation = series.getName();
        }
        else if (pieStyler.getAnnotationType() == AnnotationType.LabelAndPercentage) {
          double percentage = y.doubleValue() / total * 100;
          annotation = series.getName() + " (" + df.format(percentage) + "%)";
        }
        else if (pieStyler.getAnnotationType() == AnnotationType.Percentage) {
          double percentage = y.doubleValue() / total * 100;
          annotation = df.format(percentage) + "%";
        }

        TextLayout textLayout = new TextLayout(annotation, pieStyler.getAnnotationsFont(), new FontRenderContext(null, true, false));
        Rectangle2D annotationRectangle = textLayout.getBounds();

        double xCenter = pieBounds.getX() + pieBounds.getWidth() / 2 - annotationRectangle.getWidth() / 2;
        double yCenter = pieBounds.getY() + pieBounds.getHeight() / 2 + annotationRectangle.getHeight() / 2;
        double angle = (arcAngle + startAngle) - arcAngle / 2;
        double xOffset = xCenter + Math.cos(Math.toRadians(angle)) * (pieBounds.getWidth() / 2 * pieStyler.getAnnotationDistance());
        double yOffset = yCenter - Math.sin(Math.toRadians(angle)) * (pieBounds.getHeight() / 2 * pieStyler.getAnnotationDistance());

        // get annotation width
        Shape shape = textLayout.getOutline(null);
        Rectangle2D annotationBounds = shape.getBounds2D();
        double annotationWidth = annotationBounds.getWidth();
        // System.out.println("annotationWidth= " + annotationWidth);
        double annotationHeight = annotationBounds.getHeight();
        // System.out.println("annotationHeight= " + annotationHeight);

        // get slice area
        double xOffset1 = xCenter + Math.cos(Math.toRadians(startAngle)) * (pieBounds.getWidth() / 2 * pieStyler.getAnnotationDistance());
        double yOffset1 = yCenter - Math.sin(Math.toRadians(startAngle)) * (pieBounds.getHeight() / 2 * pieStyler.getAnnotationDistance());
        double xOffset2 = xCenter + Math.cos(Math.toRadians((arcAngle + startAngle))) * (pieBounds.getWidth() / 2 * pieStyler.getAnnotationDistance());
        double yOffset2 = yCenter - Math.sin(Math.toRadians((arcAngle + startAngle))) * (pieBounds.getHeight() / 2 * pieStyler.getAnnotationDistance());
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
        }
        else if (xDiff <= yDiff) { // assume more horizontally orientated slice
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
            double xOffsetStart = xCenter + Math.cos(Math.toRadians(angle)) * (pieBounds.getWidth() / 2.01);
            double xOffsetEnd = xCenter + Math.cos(Math.toRadians(angle)) * (pieBounds.getWidth() / endPoint);
            double yOffsetStart = yCenter - Math.sin(Math.toRadians(angle)) * (pieBounds.getHeight() / 2.01);
            double yOffsetEnd = yCenter - Math.sin(Math.toRadians(angle)) * (pieBounds.getHeight() / endPoint);

            g.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
            Shape line = new Line2D.Double(xOffsetStart, yOffsetStart, xOffsetEnd, yOffsetEnd);
            g.draw(line);

            // annotation
            at.translate(xOffset - Math.sin(Math.toRadians(angle - 90)) * annotationWidth / 2 + 3, yOffset);
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

      
      // add data labels
      if(dataLabeller != null) {
        //maybe another option to construct this label
        String annotation = series.getName() + " (" + df.format(y) + ")";
        
        double xCenter = pieBounds.getX() + pieBounds.getWidth() / 2 ;// - annotationRectangle.getWidth() / 2;
        double yCenter = pieBounds.getY() + pieBounds.getHeight() / 2; // + annotationRectangle.getHeight() / 2;
        double angle = (arcAngle + startAngle) - arcAngle / 2;
        double xOffset = xCenter + Math.cos(Math.toRadians(angle)) * (pieBounds.getWidth() / 2 * pieStyler.getAnnotationDistance());
        double yOffset = yCenter - Math.sin(Math.toRadians(angle)) * (pieBounds.getHeight() / 2 * pieStyler.getAnnotationDistance());

        dataLabeller.addData(labelShape, xOffset, yOffset + 10, 0, annotation);
      }
      startAngle += arcAngle;
    }
    // add data labels
    if(dataLabeller != null) {
      dataLabeller.paint(g);
    }
  }

  private Shape getDonutSliceShape(Rectangle2D pieBounds, double thickness, double start, double extent) {

    thickness = thickness / 2;

    GeneralPath generalPath = new GeneralPath();
    GeneralPath dummy = new GeneralPath(); // used to find arc endpoints

    Shape outer = new Arc2D.Double(pieBounds.getX(), pieBounds.getY(), pieBounds.getWidth(), pieBounds.getHeight(), start, extent, Arc2D.OPEN);
    Shape inner = new Arc2D.Double(pieBounds.getX() + pieBounds.getWidth() * thickness, pieBounds.getY() + pieBounds.getHeight() * thickness, pieBounds.getWidth() - 2 * pieBounds.getWidth()
        * thickness, pieBounds.getHeight() - 2 * pieBounds.getHeight() * thickness, start + extent, -extent, Arc2D.OPEN);
    generalPath.append(outer, false);

    dummy.append(new Arc2D.Double(pieBounds.getX() + pieBounds.getWidth() * thickness, pieBounds.getY() + pieBounds.getHeight() * thickness, pieBounds.getWidth() - 2 * pieBounds.getWidth()
        * thickness, pieBounds.getHeight() - 2 * pieBounds.getHeight() * thickness, start, extent, Arc2D.OPEN), false);

    Point2D point = dummy.getCurrentPoint();

    if (point != null) {
      generalPath.lineTo(point.getX(), point.getY());
    }
    generalPath.append(inner, false);

    dummy.append(new Arc2D.Double(pieBounds.getX(), pieBounds.getY(), pieBounds.getWidth(), pieBounds.getHeight(), start + extent, -extent, Arc2D.OPEN), false);

    point = dummy.getCurrentPoint();
    generalPath.lineTo(point.getX(), point.getY());
    return generalPath;
  }
}
