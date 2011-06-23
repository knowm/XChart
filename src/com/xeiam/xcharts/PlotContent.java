/**
 * Copyright 2011 Xeiam LLC.
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
package com.xeiam.xcharts;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Map;

import com.xeiam.xcharts.interfaces.IChartPart;
import com.xeiam.xcharts.series.Series;

/**
 * @author timmolter
 */
public class PlotContent implements IChartPart {

    private Chart chart;

    private Plot plot;

    public PlotContent(Chart chart, Plot plot) {
        this.chart = chart;
        this.plot = plot;
    }

    @Override
    public Rectangle getBounds() {
        return plot.getBounds();
    }

    @Override
    public void paint(Graphics2D g) {

        Rectangle bounds = plot.getBounds();

        Map<Integer, Series> seriesMap = chart.getAxisPair().getSeriesMap();
        for (Integer seriesId : seriesMap.keySet()) {

            Series series = seriesMap.get(seriesId);

            // X-Axis
            int xTickSpace = AxisPair.getTickSpace((int) bounds.getWidth());
            int xLeftMargin = AxisPair.getMargin((int) bounds.getWidth(), xTickSpace);

            // Y-Axis
            int yTickSpace = AxisPair.getTickSpace((int) bounds.getHeight());
            int yTopMargin = AxisPair.getMargin((int) bounds.getHeight(), yTickSpace);

            // data points
            double[] xData = series.getxData();
            double xMin = chart.getAxisPair().getXAxis().getMin();
            double xMax = chart.getAxisPair().getXAxis().getMax();
            double[] yData = series.getyData();
            double yMin = chart.getAxisPair().getYAxis().getMin();
            double yMax = chart.getAxisPair().getYAxis().getMax();

            int previousX = Integer.MIN_VALUE;
            int previousY = Integer.MIN_VALUE;

            for (int i = 0; i < xData.length; i++) {

                // if (Double.isInfinite(xData[i]) || Double.isNaN(xData[i])) {
                // throw new RuntimeException("Infinite or NaN values in xAxis Data not allowed!!!");
                // }
                //
                // if (Double.isInfinite(yData[i]) || Double.isNaN(yData[i])) {
                // throw new RuntimeException("Infinite or NaN values in yAxis Data not allowed!!!");
                // }

                if (!Double.isInfinite(xData[i]) && !Double.isNaN(xData[i]) && !Double.isInfinite(yData[i]) && !Double.isNaN(yData[i])) {

                    int xTransform = (int) (xLeftMargin + ((xData[i] - xMin) / (xMax - xMin) * xTickSpace));
                    int yTransform = (int) (bounds.getHeight() - (yTopMargin + (yData[i] - yMin) / (yMax - yMin) * yTickSpace));

                    // a check if all y data are the exact same values
                    if (Math.abs(xMax - xMin) / 5 == 0.0) {
                        xTransform = (int) (bounds.getWidth() / 2.0);
                    }

                    // a check if all y data are the exact same values
                    if (Math.abs(yMax - yMin) / 5 == 0.0) {
                        yTransform = (int) (bounds.getHeight() / 2.0);
                    }

                    int xOffset = (int) (bounds.getX() + xTransform - 1);
                    int yOffset = (int) (bounds.getY() + yTransform);

                    // paint line
                    if (series.getLineStyle() != null) {
                        if (previousX != Integer.MIN_VALUE && previousY != Integer.MIN_VALUE) {
                            g.setColor(series.getLineColor());
                            g.setStroke(series.getLineStyle());
                            g.drawLine(previousX, previousY, xOffset, yOffset);
                        }
                        previousX = xOffset;
                        previousY = yOffset;
                    }

                    // paint marker
                    if (series.getMarker() != null) {
                        g.setColor(series.getMarkerColor());
                        series.getMarker().paint(g, xOffset, yOffset);
                    }
                }
            }
        }
    }
}
