package org.knowm.xchart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/** This class can be used to create histogram */
public class Histogram {

  private final Collection<? extends Number> originalData;
  private final int numBins;
  private final double min;
  private final double max;
  private List<Double> xAxisData; // bin centers
  private List<Double> yAxisData; // frequency counts

  /**
   * Constructor
   *
   * @param data
   * @param numBins
   */
  public Histogram(Collection<? extends Number> data, int numBins) {

    // Sanity checks
    sanityCheck(data, numBins);

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

    // Sanity checks
    sanityCheck(data, numBins);
    if (max < min) {
      throw new IllegalArgumentException("max cannot be less than min!!!");
    }

    this.numBins = numBins;
    this.originalData = data;
    this.min = min;
    this.max = max;

    init();
  }

  private void sanityCheck(Collection<? extends Number> data, int numBins) {

    if (data == null) {
      throw new IllegalArgumentException("Histogram data cannot be null!!!");
    }
    if (data.isEmpty()) {
      throw new IllegalArgumentException("Histogram data cannot be empty!!!");
    }
    if (data.contains(null)) {
      throw new IllegalArgumentException("Histogram data cannot contain null!!!");
    }

    if (numBins <= 0) {
      throw new IllegalArgumentException("Histogram numBins cannot be less than or equal to 0!!!");
    }
  }

  private void init() {

    double[] tempYAxisData = new double[numBins];
    final double binSize = (max - min) / numBins;

    // y axis data
    Iterator<? extends Number> itr = originalData.iterator();
    double doubleValue = 0.0;
    int bin = 0;
    while (itr.hasNext()) {

      doubleValue = itr.next().doubleValue();

      /* this data is smaller than min, or this data point is bigger than max */
      if (doubleValue < min || doubleValue > max) {
        continue;
      }
      bin = (int) ((doubleValue - min) / binSize); // changed this from numBins
      if (bin < numBins) {
        tempYAxisData[bin] += 1;
      } else { // the value falls exactly on the max value
        tempYAxisData[bin - 1] += 1;
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
