/**
 * Copyright 2013 Xeiam LLC.
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
package com.xeiam.xchart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * This class can be used to create histogram data for histogram bar charts
 * 
 * @author timmolter
 */
public class Histogram {

  private List<Double> xAxisData; // bin centers
  private List<Double> yAxisData; // frequency counts
  private final Collection<? extends Number> originalData;
  private final int numBins;
  private final double min;
  private final double max;

  /**
   * Constructor
   * 
   * @param data
   * @param numBins
   */
  public Histogram(Collection<? extends Number> data, int numBins) {

    this.numBins = numBins;
    this.originalData = data;

    List<Double> dataAsList = new ArrayList<Double>();
    Iterator<? extends Number> itr = data.iterator();
    while (itr.hasNext()) {
      dataAsList.add(((Number) itr.next()).doubleValue());
    }
    Collections.sort(dataAsList);
    this.min = dataAsList.get(0);
    this.max = dataAsList.get(dataAsList.size() - 1);

    init();
  }

  /**
   * Constructor
   * 
   * @param data
   * @param numBins
   * @param min
   * @param max
   */
  public Histogram(Collection<? extends Number> data, int numBins, double min, double max) {

    this.numBins = numBins;
    this.originalData = data;
    this.min = min;
    this.max = max;

    init();
  }

  private void init() {

    double[] tempYAxisData = new double[numBins];
    final double binSize = (max - min) / numBins;

    // y axis data
    Iterator<? extends Number> itr = originalData.iterator();
    while (itr.hasNext()) {

      int bin = (int) ((((Number) itr.next()).doubleValue() - min) / binSize); // changed this from numBins
      if (bin < 0) { /* this data is smaller than min */
        // System.out.println("less than");
      }
      else if (bin > numBins) { /* this data point is bigger than max */
        // System.out.println("greater than");
      }
      else if (bin == numBins) { // this falls right on the edge of the max bin
        tempYAxisData[bin - 1] += 1;
      }
      else {
        tempYAxisData[bin] += 1;
      }
    }
    yAxisData = new ArrayList<Double>(numBins);
    for (double d : tempYAxisData) {
      yAxisData.add(new Double(d));
    }

    // x axis data
    xAxisData = new ArrayList<Double>(numBins);
    for (int i = 0; i < numBins; i++) {
      xAxisData.add(((i * (max - min)) / numBins + min) + binSize / 2);
    }
  }

  public List<Double> getxAxisData() {

    return xAxisData;
  }

  public List<Double> getyAxisData() {

    return yAxisData;
  }

  public Collection<? extends Number> getOriginalData() {

    return originalData;
  }

  public int getNumBins() {

    return numBins;
  }

  public double getMin() {

    return min;
  }

  public double getMax() {

    return max;
  }

}
