/**
 * Copyright 2015-2016 Knowm Inc. (http://knowm.org) and contributors.
 * Copyright 2011-2015 Xeiam LLC (http://xeiam.com) and contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.knowm.xchart.internal.chartpart;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import org.knowm.xchart.CategorySeries;
import org.knowm.xchart.CategorySeries.CategorySeriesRenderStyle;
import org.knowm.xchart.internal.Series;
import org.knowm.xchart.internal.Utils;
import org.knowm.xchart.style.CategoryStyler;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.lines.SeriesLines;

/**
 * @author timmolter
 */
public class PlotContent_Category_Bar<ST extends Styler, S extends Series> extends PlotContent_ {

  CategoryStyler stylerCategory;

  /**
   * Constructor
   *
   * @param chart
   */
  protected PlotContent_Category_Bar(Chart<CategoryStyler, CategorySeries> chart) {

    super(chart);
    this.stylerCategory = chart.getStyler();
  }
  
  private static void drawStepBarLine(Graphics2D g, CategorySeries series, Path2D.Double path){
	  if (series.getLineColor() != null) {
		  g.setColor(series.getLineColor());
		  g.setStroke(series.getLineStyle());
		  g.draw(path);
	  }
  }
  
  private static void drawStepBarFill(Graphics2D g, CategorySeries series, Path2D.Double path){
	  if (series.getFillColor() != null){
		  g.setColor(series.getFillColor());
		  g.fill(path);
	  }
  }
  
  private static void drawStepBar(Graphics2D g, CategorySeries series, Path2D.Double path, ArrayList<Point2D.Double> returnPath){
	  Collections.reverse(returnPath);

	  //At the last center (0) point before drawing the path
	  Point2D.Double returnStart = returnPath.remove(0);
	  path.lineTo(returnStart.getX(), returnStart.getY());

	  drawStepBarLine(g, series, path);

	  //Add the rest of the returnPath so we can
	  //draw the fill
	  for (Point2D.Double point : returnPath){
	  	   path.lineTo(point.x, point.y);
	  }

	  drawStepBarFill(g, series, path); 
  }
  

  @Override
  public void doPaint(Graphics2D g) {

    // X-Axis
    double xTickSpace = stylerCategory.getPlotContentSize() * getBounds().getWidth();
    // System.out.println("xTickSpace: " + xTickSpace);
    double xLeftMargin = Utils.getTickStartOffset(getBounds().getWidth(), xTickSpace);
    // System.out.println("xLeftMargin: " + xLeftMargin);
    Map<String, CategorySeries> seriesMap = chart.getSeriesMap();
    int numCategories = seriesMap.values().iterator().next().getXData().size();
    double gridStep = xTickSpace / numCategories;
    // System.out.println("gridStep: " + gridStep);

    // Y-Axis
    double yMin = chart.getAxisPair().getYAxis().getMin();
    double yMax = chart.getAxisPair().getYAxis().getMax();

    // figure out the general form of the chart
    int chartForm = 1; // 1=positive, -1=negative, 0=span
    if (yMin > 0.0 && yMax > 0.0) {
      chartForm = 1; // positive chart
    }
    else if (yMin < 0.0 && yMax < 0.0) {
      chartForm = -1; // negative chart
    }
    else {
      chartForm = 0;// span chart
    }
    // System.out.println(yMin);
    // System.out.println(yMax);
    // System.out.println("chartForm: " + chartForm);

    double yTickSpace = stylerCategory.getPlotContentSize() * getBounds().getHeight();

    double yTopMargin = Utils.getTickStartOffset(getBounds().getHeight(), yTickSpace);

    // plot series
    int seriesCounter = 0;
    double[] accumulatedStackOffsetPos = new double[numCategories];
    double[] accumulatedStackOffsetNeg = new double[numCategories];
    for (CategorySeries series : seriesMap.values()) {

      if (!series.isEnabled()) {
        continue;
      }

      // for line series
      double previousX = -Double.MAX_VALUE;
      double previousY = -Double.MAX_VALUE;

      Iterator<? extends Number> yItr = series.getYData().iterator();
      Iterator<? extends Number> ebItr = null;
      Collection<? extends Number> errorBars = series.getExtraValues();
      if (errorBars != null) {
        ebItr = errorBars.iterator();
      }

      //Stepped bars are drawn in chunks
      //rather than for each inidivdual bar
      Path2D.Double steppedPath = null;
      ArrayList<Point2D.Double> steppedReturnPath = null;
      
      int categoryCounter = 0;
      while (yItr.hasNext()) {

        Number next = yItr.next();
        // skip when a value is null
        if (next == null) {

          previousX = -Double.MAX_VALUE;
          previousY = -Double.MAX_VALUE;
          categoryCounter++;
          continue;
        }

        double y = next.doubleValue();

        double yTop = 0.0;
        double yBottom = 0.0;
        switch (chartForm) {
        case 1: // positive chart
          // check for points off the chart draw area due to a custom yMin
          if (y < yMin) {
            categoryCounter++;
            continue;
          }
          yTop = y;
          yBottom = yMin;
          if (stylerCategory.isStacked()) {
            yTop += accumulatedStackOffsetPos[categoryCounter];
            yBottom += accumulatedStackOffsetPos[categoryCounter];
            accumulatedStackOffsetPos[categoryCounter] += (yTop - yBottom);
          }
          break;
        case -1: // negative chart
          // check for points off the chart draw area due to a custom yMin
          if (y > yMax) {
            categoryCounter++;
            continue;
          }
          yTop = yMax;
          yBottom = y;
          if (stylerCategory.isStacked()) {
            yTop -= accumulatedStackOffsetNeg[categoryCounter];
            yBottom -= accumulatedStackOffsetNeg[categoryCounter];
            accumulatedStackOffsetNeg[categoryCounter] += (yTop - yBottom);
          }
          break;
        case 0: // span chart
          if (y >= 0.0) { // positive
            yTop = y;
            if (series.getChartCategorySeriesRenderStyle() == CategorySeriesRenderStyle.Bar || 
            		series.getChartCategorySeriesRenderStyle() == CategorySeriesRenderStyle.Stick ||
            		series.getChartCategorySeriesRenderStyle() == CategorySeriesRenderStyle.SteppedBar) {
              yBottom = 0.0;
            }
            else {
              yBottom = y;
            }
            if (stylerCategory.isStacked()) {
              yTop += accumulatedStackOffsetPos[categoryCounter];
              yBottom += accumulatedStackOffsetPos[categoryCounter];
              accumulatedStackOffsetPos[categoryCounter] += (yTop - yBottom);
            }
          }
          else {
              if (series.getChartCategorySeriesRenderStyle() == CategorySeriesRenderStyle.Bar || 
              		series.getChartCategorySeriesRenderStyle() == CategorySeriesRenderStyle.Stick ||
              		series.getChartCategorySeriesRenderStyle() == CategorySeriesRenderStyle.SteppedBar) {
              yTop = 0.0;
            }
            else {
              yTop = y; // yTransform uses yTop, and for non-bars and stick, it's the same as yBottom.
            }
            yBottom = y;
            if (stylerCategory.isStacked()) {
              yTop -= accumulatedStackOffsetNeg[categoryCounter];
              yBottom -= accumulatedStackOffsetNeg[categoryCounter];
              accumulatedStackOffsetNeg[categoryCounter] += (yTop - yBottom);
            }
          }
          break;
        default:
          break;
        }

        double yTransform = getBounds().getHeight() - (yTopMargin + (yTop - yMin) / (yMax - yMin) * yTickSpace);
        double yOffset = getBounds().getY() + yTransform;

        double zeroTransform = getBounds().getHeight() - (yTopMargin + (yBottom - yMin) / (yMax - yMin) * yTickSpace);
        double zeroOffset = getBounds().getY() + zeroTransform;
        double xOffset;
        double barWidth;
        
        
        {
            double barWidthPercentage = stylerCategory.getAvailableSpaceFill();
            //SteppedBars can not have any space between them
            if (series.getChartCategorySeriesRenderStyle() == CategorySeriesRenderStyle.SteppedBar)
            	barWidthPercentage = 1;
        	
            if (stylerCategory.isOverlapped() || stylerCategory.isStacked()) {
            	
                barWidth = gridStep * barWidthPercentage;
                double barMargin = gridStep * (1 - barWidthPercentage) / 2;
                xOffset = getBounds().getX() + xLeftMargin + gridStep * categoryCounter++ + barMargin;
              }
              else {
            	  
                barWidth = gridStep / chart.getSeriesMap().size() * barWidthPercentage;
                double barMargin = gridStep * (1 - barWidthPercentage) / 2;
                xOffset = getBounds().getX() + xLeftMargin + gridStep * categoryCounter++ + seriesCounter * barWidth + barMargin;
              }
        }
        
        //SteppedBar. Partially drawn in loop, partially after loop.
        if (series.getChartCategorySeriesRenderStyle() == CategorySeriesRenderStyle.SteppedBar){
        	
        	double yCenter = zeroOffset;
        	double yTip = yOffset;
        	double stepLength = gridStep;
       	
        	//yTip should be the value end, yCenter the center (0) end.
        	if (y < 0){
        		   
        		   yTip = zeroOffset;
        		   yCenter = yOffset;
        	}
        	
        	//Init in first iteration
    		if (steppedPath == null){
    			steppedPath = new Path2D.Double();
    			steppedReturnPath = new ArrayList<Point2D.Double>();
    			steppedPath.moveTo(xOffset, yCenter); 
    		}
    		else if (stylerCategory.isStacked()){
    			//If a section of a stacked graph has changed from positive
    			//to negative or vice-versa, draw what we've stored up so far
    			//and resume with a blank slate.
			   if ( (previousY > 0 && y < 0) || (previousY < 0 && y > 0)  ){
				   	drawStepBar(g,series,steppedPath,steppedReturnPath);
				  
			   		steppedPath.reset();
			 		steppedReturnPath.clear();
			 		steppedPath.moveTo(xOffset, yCenter); 
			   }
    		}
        		

        	if (!yItr.hasNext() ){
        		   
        		//Shift the far point of the final bar backwards
        		//by the same amount its start was shifted forward.
    			if (  !(stylerCategory.isOverlapped() || stylerCategory.isStacked()) ) {
    			   
    				double singleBarStep = stepLength / (double)chart.getSeriesMap().size();
    				stepLength -= (seriesCounter * singleBarStep);
    			}
        	}
        		
        	//Draw the vertical line to the new y position, and the horizontal flat of the bar.
        	steppedPath.lineTo(xOffset,  yTip);
        	steppedPath.lineTo(xOffset + stepLength, yTip);

        	 //Add the corresponding centerline (or equivalent) to the return path
        	//Could be simplfied and removed for non-stacked graphs
        	steppedReturnPath.add(new Point2D.Double(xOffset, yCenter));
        	steppedReturnPath.add(new Point2D.Double(xOffset + stepLength, yCenter));  

        	previousY = y;
        }
        // paint series
        else if (series.getChartCategorySeriesRenderStyle() == CategorySeriesRenderStyle.Bar) {

          // paint bar
          Path2D.Double path = new Path2D.Double();
          path.moveTo(xOffset, yOffset);
          path.lineTo(xOffset + barWidth, yOffset);
          path.lineTo(xOffset + barWidth, zeroOffset);
          path.lineTo(xOffset, zeroOffset);
          path.closePath();

          g.setColor(series.getFillColor());
          g.fill(path);

          // TODO maybe we want outlines of the bars?
          // Legend markers now also draw the outline. It has been disabled for CategorySeriesRenderStyle.Bar
          // in Legend_Marker.java. Modify accordingly if you are enabling bar outlines.
          // if (series.getLineColor() != null) {
          // path = new Path2D.Double();
          // int halfLineWidth = (int) (series.getLineStyle().getLineWidth() / 2 + .1);
          // path.moveTo(xOffset + halfLineWidth, yOffset + halfLineWidth);
          // path.lineTo(xOffset + halfLineWidth + barWidth - halfLineWidth * 2, yOffset + halfLineWidth);
          // path.lineTo(xOffset + halfLineWidth + barWidth - halfLineWidth * 2, zeroOffset - halfLineWidth);
          // path.lineTo(xOffset + halfLineWidth, zeroOffset - halfLineWidth);
          // path.closePath();
          //
          // g.setStroke(series.getLineStyle());
          // g.setColor(series.getLineColor());
          // g.draw(path);
          // }

          if (stylerCategory.hasAnnotations() && next != null) {

            DecimalFormat twoPlaces = new DecimalFormat("#.#");
            if (stylerCategory.getYAxisDecimalPattern() != null) {
              twoPlaces = new DecimalFormat(stylerCategory.getYAxisDecimalPattern());
            }
            String numberAsString = twoPlaces.format(next);

            TextLayout textLayout = new TextLayout(numberAsString, stylerCategory.getAnnotationsFont(), new FontRenderContext(null, true, false));
            Rectangle2D annotationRectangle = textLayout.getBounds();

            double annotationX = xOffset + barWidth / 2 - annotationRectangle.getWidth() / 2;
            double annotationY;
            if (next.doubleValue() >= 0.0) {
              annotationY = yOffset - 4;
            }
            else {
              annotationY = zeroOffset + 4 + annotationRectangle.getHeight();
            }
            Shape shape = textLayout.getOutline(null);
            g.setColor(stylerCategory.getChartFontColor());
            g.setFont(stylerCategory.getAnnotationsFont());
            AffineTransform orig = g.getTransform();
            AffineTransform at = new AffineTransform();
            at.translate(annotationX, annotationY);
            g.transform(at);
            g.fill(shape);
            g.setTransform(orig);

          }

        }
        else if (CategorySeriesRenderStyle.Stick.equals(series.getChartCategorySeriesRenderStyle())) {

          // paint stick
          if (series.getLineStyle() != SeriesLines.NONE) {

            g.setColor(series.getLineColor());
            g.setStroke(series.getLineStyle());
            Shape line = new Line2D.Double(xOffset + barWidth / 2, zeroOffset, xOffset + barWidth / 2, yOffset);
            g.draw(line);
          }

          // paint marker
          if (series.getMarker() != null) {
            g.setColor(series.getMarkerColor());

            if (y <= 0) {
              series.getMarker().paint(g, xOffset + barWidth / 2, zeroOffset, stylerCategory.getMarkerSize());
            }
            else {
              series.getMarker().paint(g, xOffset + barWidth / 2, yOffset, stylerCategory.getMarkerSize());
            }
          }
        }
        else {

          // paint line
          if (series.getChartCategorySeriesRenderStyle() == CategorySeriesRenderStyle.Line) {

            if (series.getLineStyle() != SeriesLines.NONE) {

              if (previousX != -Double.MAX_VALUE && previousY != -Double.MAX_VALUE) {
                g.setColor(series.getLineColor());
                g.setStroke(series.getLineStyle());
                Shape line = new Line2D.Double(previousX, previousY, xOffset + barWidth / 2, yOffset);
                g.draw(line);
              }
            }
          }

          previousX = xOffset + barWidth / 2;
          previousY = yOffset;

          // paint marker
          if (series.getMarker() != null) {
            g.setColor(series.getMarkerColor());
            series.getMarker().paint(g, previousX, previousY, stylerCategory.getMarkerSize());
          }

        }

        // paint error bars

        if (errorBars != null) {

          double eb = ebItr.next().doubleValue();

          // set error bar style
          if (stylerCategory.isErrorBarsColorSeriesColor()) {
            g.setColor(series.getLineColor());
          }
          else {
            g.setColor(stylerCategory.getErrorBarsColor());
          }
          g.setStroke(errorBarStroke);

          // Top value
          double errorBarLength = ((eb) / (yMax - yMin) * yTickSpace);
          double topEBOffset = yOffset - errorBarLength;

          // Bottom value
          double bottomEBOffset = yOffset + errorBarLength;

          // Draw it
          double errorBarOffset = xOffset + barWidth / 2;
          Shape line = new Line2D.Double(errorBarOffset, topEBOffset, errorBarOffset, bottomEBOffset);
          g.draw(line);
          line = new Line2D.Double(errorBarOffset - 3, bottomEBOffset, errorBarOffset + 3, bottomEBOffset);
          g.draw(line);
          line = new Line2D.Double(errorBarOffset - 3, topEBOffset, errorBarOffset + 3, topEBOffset);
          g.draw(line);
        }

      }
      
      //Final drawing of a steppedBar is done after the main loop,
      //as it continues on null and we may end up missing the final iteration.
      if (steppedPath != null && !steppedReturnPath.isEmpty())
	  {
    	  drawStepBar(g, series, steppedPath, steppedReturnPath);
	  }
      
      seriesCounter++;
    }
  }

}
