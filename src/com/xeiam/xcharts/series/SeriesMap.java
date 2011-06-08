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

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author timmolter
 */
public class SeriesMap {

    private Map<Integer, Series> seriesMap = new LinkedHashMap<Integer, Series>();

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
        seriesMap.put(0, series);
        return series;
    }

    /**
     * @return
     */
    public Integer[] getLegendItems() {
        return seriesMap.keySet().toArray(new Integer[seriesMap.size()]);
    }

}
