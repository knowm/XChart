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
package com.xeiam.xcharts.series;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.Arrays;

import com.xeiam.xcharts.series.markers.Marker;

/**
 * @author timmolter
 */
public class Series {

    private String name = "";

    protected double[] xData;

    protected double[] yData;

    /** the minimum value of axis range */
    private double xMin = 0d;

    /** the maximum value of axis range */
    private double xMax = 1d;

    /** the minimum value of axis range */
    private double yMin = 0d;

    /** the maximum value of axis range */
    private double yMax = 1d;

    /** Line Style */
    private BasicStroke stroke;

    /** Line Color */
    private Color strokeColor;

    /** Marker Style */
    private Marker marker;

    /** Marker Color */
    private Color markerColor;

    /**
     * Constructor
     * 
     * @param name
     * @param xData
     * @param yData
     */
    public Series(String name, double[] xData, double[] yData) {

        this.name = name;
        this.xData = xData;
        this.yData = yData;

        // xData
        double[] xDataClone = xData.clone();
        Arrays.sort(xDataClone);
        this.xMin = xDataClone[0];
        this.xMax = xDataClone[xDataClone.length - 1];

        // yData
        double[] yDataClone = yData.clone();
        Arrays.sort(yDataClone);
        this.yMin = yDataClone[0];
        this.yMax = yDataClone[yDataClone.length - 1];

        Color color = SeriesColor.getNextAWTColor();
        this.strokeColor = color;
        this.markerColor = color;

        this.marker = SeriesMarker.getNextMarker();
        this.stroke = SeriesLineStyle.getNextBasicStroke();

    }

    public String getName() {
        return name;
    }

    public double[] getxData() {
        return xData;
    }

    public double[] getyData() {
        return yData;
    }

    public double getxMin() {
        return xMin;
    }

    public double getxMax() {
        return xMax;
    }

    public double getyMin() {
        return yMin;
    }

    public double getyMax() {
        return yMax;
    }

    public BasicStroke getLineStyle() {
        return stroke;
    }

    public void setLineStyle(SeriesLineStyle lineStyle) {
        this.stroke = SeriesLineStyle.getBasicStroke(lineStyle);
    }

    public void setLineStyle(BasicStroke lineStyle) {
        this.stroke = lineStyle;
    }

    public Color getLineColor() {
        return strokeColor;
    }

    public void setLineColor(SeriesColor lineColor) {
        this.strokeColor = SeriesColor.getAWTColor(lineColor);
    }

    public void setLineColor(java.awt.Color lineColor) {
        this.strokeColor = lineColor;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(SeriesMarker marker) {
        this.marker = SeriesMarker.getMarker(marker);
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public Color getMarkerColor() {
        return markerColor;
    }

    public void setMarkerColor(SeriesColor lineColor) {
        this.markerColor = SeriesColor.getAWTColor(lineColor);
    }

    public void setMarkerColor(java.awt.Color lineColor) {
        this.markerColor = lineColor;
    }

}
