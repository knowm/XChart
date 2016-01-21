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
package org.knowm.xchart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * This class can be used to create histogram
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

    Double tempMax = -Double.MAX_VALUE;
    Double tempMin = Double.MAX_VALUE;
    for (Number number : data) {
      double value = number.doubleValue();
      if (value > tempMax) {
        tempMax = value;
      }
      if (value < tempMin) {
        tempMin = value;
      }
    }
    max = tempMax;
    min = tempMin;

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

      double doubleValue = ((Number) itr.next()).doubleValue();
      int bin = (int) ((doubleValue - min) / binSize); // changed this from numBins
      if (bin < 0) { /* this data is smaller than min */
        // System.out.println("less than");
      }
      else if (doubleValue == max) { // the value falls exactly on the max value
        tempYAxisData[bin - 1] += 1;
      }
      else if (bin > numBins || bin == numBins) { /* this data point is bigger than max */
        // System.out.println("greater than");
      }
      else {
        tempYAxisData[bin] += 1;
      }
    }
    yAxisData = new ArrayList<Double>(numBins);
    for (double d : tempYAxisData) {
      yAxisData.add(d);
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
