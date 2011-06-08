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
import java.util.LinkedHashMap;
import java.util.Map;

import com.xeiam.xcharts.interfaces.IChartPart;
import com.xeiam.xcharts.series.Series;

/**
 * @author timmolter
 */
public class AxisPair implements IChartPart {

    /** the chart */
    private Chart chart;

    private Map<Integer, Series> seriesMap = new LinkedHashMap<Integer, Series>();

    int seriesCount = 0;

    Axis xAxis;
    Axis yAxis;

    /**
     * Constructor.
     * 
     * @param chart the chart
     */
    public AxisPair(Chart chart) {

        this.chart = chart;

        // add axes
        xAxis = new Axis(chart, this, Axis.Direction.X);
        yAxis = new Axis(chart, this, Axis.Direction.Y);
    }

    /**
     * @param xData
     * @param yData
     */
    public Series addSeries(String seriesName, double[] xData, double[] yData) {

        Series series;
        if (xData != null) {
            series = new Series(seriesName, xData, yData);
        } else { // generate xData
            double[] generatedXData = new double[yData.length];
            for (int i = 1; i < yData.length; i++) {
                generatedXData[i] = i;
            }
            series = new Series(seriesName, generatedXData, yData);
        }
        seriesMap.put(seriesCount++, series);

        // add min/max to axis
        xAxis.addMinMax(series.getxMin(), series.getxMax());
        yAxis.addMinMax(series.getyMin(), series.getyMax());

        return series;
    }

    protected Axis getXAxis() {
        return xAxis;
    }

    protected Axis getYAxis() {
        return yAxis;
    }

    protected Rectangle getChartTitleBounds() {
        return chart.getTitle().getBounds();
    }

    protected Rectangle getChartLegendBounds() {
        return chart.getLegend().getBounds();
    }

    protected Map<Integer, Series> getSeriesMap() {
        return seriesMap;
    }

    public static int getTickSpace(int workingSpace) {

        int tickSpace = (int) (workingSpace * 0.95);
        return tickSpace;
    }

    public static int getLeftMargin(int workingSpace, int tickSpace) {

        int marginSpace = workingSpace - tickSpace;
        int leftMargin = (int) (marginSpace / 2.0);
        return leftMargin;
    }

    @Override
    public void paint(Graphics2D g) {
        yAxis.paint(g);
        xAxis.paint(g);
    }

    @Override
    public Rectangle getBounds() {
        return null; // should never be called
    }

}
