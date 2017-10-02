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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Map;

import org.knowm.xchart.XYSeries;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.internal.Utils;
import org.knowm.xchart.style.AxesChartStyler;
import org.knowm.xchart.style.lines.SeriesLines;

/**
 * @author timmolter
 */
public class PlotContent_XY<ST extends AxesChartStyler, S extends XYSeries> extends PlotContent_<ST, S> {

	/**
	 * Interface to support different forms of error marks
	 * @author bjef8061
	 *
	 */
	private interface ErrorPainter {


		/**
		 * Add error marks for new data point
		 * @param x position along x axis
		 * @param yTop y position of top of error region
		 * @param yBottom y position of bottom of error region
		 * @param g Graphics context
		 */
		void draw(double x, double yTop, double yBottom, Graphics2D g);

		/**
		 * Apply any final operations. Should be called once all points have
		 * been drawn
		 * @param g Graphics context
		 */
		void cleanup(Graphics2D g);

	}
	
	/**
	 * Painter for standard error bars
	 * @author bjef8061
	 */
	private class ErrorBarPainter implements ErrorPainter {

		final private Color barColor;

		/**
		 * @param barColor Colour of error bars
		 */
		public ErrorBarPainter(Color barColor) {
			this.barColor = barColor;
		}

		@Override
		public void draw(double x, double yTop, double yBottom, Graphics2D g) {
			Line2D.Double line = new Line2D.Double();
			g.setColor(barColor);
			g.setStroke(errorBarStroke);
			line.setLine(x, yTop, x, yBottom);
			g.draw(line);
			line.setLine(x - 3, yBottom, x + 3, yBottom);
			g.draw(line);
			line.setLine(x - 3, yTop, x + 3, yTop);
			g.draw(line);
		}

		@Override
		public void cleanup(Graphics2D g) {}

	}

	/**
	 * Painter for confidence regions, drawn as continuous areas
	 * @author bjef8061
	 *
	 */
	private class ConfidenceAreaPainter implements ErrorPainter {

		private double previousX = Double.NaN;
		private double previousYT = Double.NaN;
		private double previousYB = Double.NaN;
		private Path2D.Double errorPathUpper;
		private ArrayList<double[]> errorPathLower;
		private Color fillColor;

		/**
		 * @param fillColor Colour to fill the region
		 */
		public ConfidenceAreaPainter(Color fillColor) {
			this.fillColor=fillColor;
		}

		@Override
		public void draw(double x, double yTop, double yBottom, Graphics2D g) {
			// Use similar approach to XYSeriesRenderStyle.Area
			if (x == Double.NaN && yTop == Double.NaN && yBottom == Double.NaN) {
				closePath(g);
			}

			if (previousX != Double.NaN && previousYT != Double.NaN && previousYB != Double.NaN) {
				if (errorPathUpper == null || errorPathLower == null) {
					errorPathUpper = new Path2D.Double();
					errorPathLower = new ArrayList<double[]>();
					errorPathUpper.moveTo(previousX, previousYT);
				}
				errorPathUpper.lineTo(x, yTop);
				errorPathLower.add(new double[]{x, yBottom});
			}
			if (x < previousX) {
				throw new RuntimeException("X-Data must be in ascending order for Continuous Error Bars!!!");
			}

			previousX = x;
			previousYT = yTop;
			previousYB = yBottom;
		}

		private void closePath(Graphics2D g) {
			if (errorPathUpper != null && errorPathLower != null) {
				int l = errorPathLower.size();
				for(int i = 0; i<l; ++i) {
					double[] point = errorPathLower.get(l-i-1);
					errorPathUpper.lineTo(point[0], point[1]);
				}
				errorPathUpper.closePath();
				g.setColor(fillColor);
				g.fill(errorPathUpper);
			}
			errorPathUpper = null;
			errorPathLower = null;   
		}

		@Override
		public void cleanup(Graphics2D g) {
			this.closePath(g);
		}

	}

	private final ST xyStyler;

	/**
	 * Constructor
	 *
	 * @param chart
	 */
	PlotContent_XY(Chart<ST, S> chart) {

		super(chart);
		xyStyler = chart.getStyler();
	}

	@Override
	public void doPaint(Graphics2D g) {

		// X-Axis
		double xTickSpace = xyStyler.getPlotContentSize() * getBounds().getWidth();
		double xLeftMargin = Utils.getTickStartOffset((int) getBounds().getWidth(), xTickSpace);

		// Y-Axis
		double yTickSpace = xyStyler.getPlotContentSize() * getBounds().getHeight();
		double yTopMargin = Utils.getTickStartOffset((int) getBounds().getHeight(), yTickSpace);

		double xMin = chart.getXAxis().getMin();
		double xMax = chart.getXAxis().getMax();

		Line2D.Double line = new Line2D.Double();

		// logarithmic
		if (xyStyler.isXAxisLogarithmic()) {
			xMin = Math.log10(xMin);
			xMax = Math.log10(xMax);
		}

		Map<String, S> map = chart.getSeriesMap();

		for (S series : map.values()) {

			if (!series.isEnabled()) {
				continue;
			}
			Axis yAxis = chart.getYAxis(series.getYAxisGroup());
			double yMin = yAxis.getMin();
			double yMax = yAxis.getMax();
			if (xyStyler.isYAxisLogarithmic()) {
				yMin = Math.log10(yMin);
				yMax = Math.log10(yMax);
			}

			// data points
			double[] xData = series.getXData();
			double[] yData = series.getYData();

			double previousX = -Double.MAX_VALUE;
			double previousY = -Double.MAX_VALUE;

			Path2D.Double path = null;
			double[] errorBars = series.getExtraValues();

			// Configure error bars
			ErrorPainter errorBarPainter = null;
			if(errorBars!=null) {
				if(xyStyler.isErrorBarsContinuous()) {
					Color color = Color.LIGHT_GRAY;
					color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 127);
					errorBarPainter = new ConfidenceAreaPainter(color);
				} else {
					errorBarPainter = new ErrorBarPainter( xyStyler.isErrorBarsColorSeriesColor() ? series.getLineColor() : xyStyler.getErrorBarsColor());
				}
			}

			for (int i = 0; i < xData.length; i++) {

				double x = xData[i];
				if (xyStyler.isXAxisLogarithmic()) {
					x = Math.log10(x);
				}

				double next = yData[i];
				if (next == Double.NaN) {

					// for area charts
					closePath(g, path, previousX, getBounds(), yTopMargin);
					path = null;

					previousX = -Double.MAX_VALUE;
					previousY = -Double.MAX_VALUE;
					continue;
				}

				double yOrig = yData[i];

				double y;

				if (xyStyler.isYAxisLogarithmic()) {
					y = Math.log10(yOrig);
				} else {
					y = yOrig;
				}

				double xTransform = xLeftMargin + ((x - xMin) / (xMax - xMin) * xTickSpace);
				double yTransform = getBounds().getHeight() - (yTopMargin + (y - yMin) / (yMax - yMin) * yTickSpace);

				// a check if all x data are the exact same values
				if (Math.abs(xMax - xMin) / 5 == 0.0) {
					xTransform = getBounds().getWidth() / 2.0;
				}

				// a check if all y data are the exact same values
				if (Math.abs(yMax - yMin) / 5 == 0.0) {
					yTransform = getBounds().getHeight() / 2.0;
				}

				double xOffset = getBounds().getX() + xTransform;
				double yOffset = getBounds().getY() + yTransform;

				// paint line

				boolean isSeriesLineOrArea = XYSeriesRenderStyle.Line == series.getXYSeriesRenderStyle()
						|| XYSeriesRenderStyle.Area == series.getXYSeriesRenderStyle();
				boolean isSeriesStepLineOrStepArea =  XYSeriesRenderStyle.Step == series.getXYSeriesRenderStyle()
						|| XYSeriesRenderStyle.StepArea == series.getXYSeriesRenderStyle();

				if (isSeriesLineOrArea || isSeriesStepLineOrStepArea) {
					if (series.getLineStyle() != SeriesLines.NONE) {

						if (previousX != -Double.MAX_VALUE && previousY != -Double.MAX_VALUE) {
							g.setColor(series.getLineColor());
							g.setStroke(series.getLineStyle());
							if (isSeriesLineOrArea) {
								line.setLine(previousX, previousY, xOffset, yOffset);
								g.draw(line);
							} else {
								if (previousX != xOffset) {
									line.setLine(previousX, previousY, xOffset, previousY);
									g.draw(line);
								}
								if (previousY != yOffset) {
									line.setLine(xOffset, previousY, xOffset, yOffset);
									g.draw(line);
								}
							}
						}
					}
				}

				// paint area
				if (XYSeriesRenderStyle.Area == series.getXYSeriesRenderStyle()
						|| XYSeriesRenderStyle.StepArea == series.getXYSeriesRenderStyle()) {

					if (previousX != -Double.MAX_VALUE && previousY != -Double.MAX_VALUE) {

						double yBottomOfArea = getBounds().getY() + getBounds().getHeight() - yTopMargin;

						if (path == null) {
							path = new Path2D.Double();
							path.moveTo(previousX, yBottomOfArea);
							path.lineTo(previousX, previousY);
						}
						if (XYSeriesRenderStyle.Area == series.getXYSeriesRenderStyle()) {
							path.lineTo(xOffset, yOffset);
						} else {
							if (previousX != xOffset) {
								path.lineTo(xOffset, previousY);
							}
							if (previousY != yOffset) {
								path.lineTo(xOffset, yOffset);
							}
						}
					}
					if (xOffset < previousX) {
						throw new RuntimeException("X-Data must be in ascending order for Area Charts!!!");
					}
				}

				previousX = xOffset;
				previousY = yOffset;

				// paint marker
				if (series.getMarker() != null) {
					g.setColor(series.getMarkerColor());
					series.getMarker().paint(g, xOffset, yOffset, xyStyler.getMarkerSize());
				}

				// paint error bars
				if (errorBarPainter != null && errorBars != null) {

					double eb = errorBars[i];

					// Top value
					double topValue;
					if (xyStyler.isYAxisLogarithmic()) {
						topValue = yOrig + eb;
						topValue = Math.log10(topValue);
					} else {
						topValue = y + eb;
					}
					double topEBTransform = getBounds().getHeight() - (yTopMargin + (topValue - yMin) / (yMax - yMin) * yTickSpace);
					double topEBOffset = getBounds().getY() + topEBTransform;

					// Bottom value
					double bottomValue;
					if (xyStyler.isYAxisLogarithmic()) {
						bottomValue = yOrig - eb;
						bottomValue = Math.log10(bottomValue);
					} else {
						bottomValue = y - eb;
					}
					double bottomEBTransform = getBounds().getHeight() - (yTopMargin + (bottomValue - yMin) / (yMax - yMin) * yTickSpace);
					double bottomEBOffset = getBounds().getY() + bottomEBTransform;

					// Draw it
					errorBarPainter.draw(xOffset, topEBOffset, bottomEBOffset, g);

				}

				// add data labels
				if (chart.getStyler().isToolTipsEnabled()) {
					chart.toolTips.addData(xOffset, yOffset, chart.getXAxisFormat().format(x), chart.getYAxisFormat().format(yOrig));
				}
			}

			if (errorBarPainter!=null) errorBarPainter.cleanup(g);

			// close any open path for area charts
			g.setColor(series.getFillColor());
			closePath(g, path, previousX, getBounds(), yTopMargin);
		}
	}
}
