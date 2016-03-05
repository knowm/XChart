/*
 * Copyright 2016 Knowm Inc..
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.knowm.xchart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StackedBarChart {

    private final Collection<String> labels;
    private final List<?> xAxisData; // X Values
    private List<List<Double>> yAxisData; // frequency counts
    private final Collection<Collection<? extends Number>> originalData;

    public StackedBarChart(Collection<String> labels, Collection<?> xData, Collection<? extends Number>... dataMatrix) {

        this.originalData = new ArrayList();
        for (Collection data : dataMatrix) {
            originalData.add(data);
        }
        this.xAxisData = (List<?>) xData;
        this.labels = labels;

        // Sanity check
        if (xData.size() != originalData.size() || labels.size() != xData.size()) {
            throw new IllegalArgumentException("X Axis Data is different than number of collections in data matrix");
        }

        init();

    }

    private void init() {

        int numBins = xAxisData.size();
        int numStacks = labels.size();
        double[][] tempYAxisData = new double[numStacks][numBins];

        // y axis data
        for (int x = 0; x < xAxisData.size(); x++) {
            int i = 0;
            for (Collection<? extends Number> data : originalData) {
                int y = 0;
                for (Number value : data) {
                    if (i < x) {
                        continue;
                    }
                    double doubleValue = (value).doubleValue();
                    tempYAxisData[x][y] += doubleValue;
                    y++;
                }
                i++;
            }
        }
        yAxisData = new ArrayList();
        for (double[] row : tempYAxisData) {
            List arrayRow = new ArrayList();
            for (double value : row) {
                arrayRow.add(value);
            }
            yAxisData.add(arrayRow);
        }
    }

    public Collection<?> getxAxisData() {

        return xAxisData;
    }

    public List<List<Double>> getyAxisData() {

        return yAxisData;
    }

    public Collection<Collection<? extends Number>> getOriginalData() {

        return originalData;
    }

    public Chart_Category addChartElements(Chart_Category chart) {
        return addChartElements(chart, true);
    }
    
    /**
     * Adds data elements to chart 
     * 
     * @param chart Chart to render
     * @param overlapped true if stacked false if placed side by side
     * @return 
     */
    public Chart_Category addChartElements(Chart_Category chart, boolean overlapped) {

        // Customize Chart
        chart.getStyler().setBarsOverlapped(overlapped);

        int i = 0;
        for (String label : labels) {
            chart.addSeries(label, xAxisData, yAxisData.get(i));
            i++;
        }
        return chart;
    }

}
