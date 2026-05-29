package org.knowm.xchart.internal.series;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

/**
 * Package-private helper that performs min/max calculations for series data. Extracted from the
 * series classes to make the logic independently testable.
 */
class SeriesMinMaxCalculator {

  private SeriesMinMaxCalculator() {}

  /**
   * Finds the min and max of a numeric double array, ignoring NaN values.
   *
   * @param data the data array
   * @return {@code double[]{min, max}}
   */
  static double[] findMinMax(double[] data) {

    double min = Double.MAX_VALUE;
    double max = -Double.MAX_VALUE;

    for (double dataPoint : data) {
      if (Double.isNaN(dataPoint)) {
        continue;
      }
      if (dataPoint < min) {
        min = dataPoint;
      }
      if (dataPoint > max) {
        max = dataPoint;
      }
    }

    return new double[] {min, max};
  }

  /**
   * Finds the min and max of a numeric double array while accounting for symmetric error bars.
   *
   * @param data the data array
   * @param errorBars the error-bar array (same length as {@code data})
   * @return {@code double[]{min, max}}
   */
  static double[] findMinMaxWithErrorBars(double[] data, double[] errorBars) {

    double min = Double.MAX_VALUE;
    double max = -Double.MAX_VALUE;

    for (int i = 0; i < data.length; i++) {
      double d = data[i];
      double eb = errorBars[i];
      if (d - eb < min) {
        min = d - eb;
      }
      if (d + eb > max) {
        max = d + eb;
      }
    }

    return new double[] {min, max};
  }

  /**
   * Finds the min and max of a collection whose element type is determined by {@code dataType}.
   *
   * <p>Returns {@code double[]{NaN, NaN}} for {@link Series.DataType#String} data (no numeric
   * range).
   *
   * @param data the data collection
   * @param dataType the type of values stored in {@code data}
   * @return {@code double[]{min, max}}
   */
  static double[] findMinMax(Collection<?> data, Series.DataType dataType) {

    double min = Double.MAX_VALUE;
    double max = -Double.MAX_VALUE;

    for (Object dataPoint : data) {

      if (dataPoint == null) {
        continue;
      }

      double value;

      if (dataType == Series.DataType.Number) {
        value = ((Number) dataPoint).doubleValue();
      } else if (dataType == Series.DataType.Date) {
        value = ((Date) dataPoint).getTime();
      } else {
        // String axis: no numeric range
        return new double[] {Double.NaN, Double.NaN};
      }

      if (value < min) {
        min = value;
      }
      if (value > max) {
        max = value;
      }
    }

    return new double[] {min, max};
  }

  /**
   * Finds the min and max of a collection of numbers while accounting for symmetric error bars.
   *
   * @param data the data collection
   * @param errorBars the error-bar collection (same size as {@code data})
   * @return {@code double[]{min, max}}
   */
  static double[] findMinMaxWithErrorBars(
      Collection<? extends Number> data, Collection<? extends Number> errorBars) {

    double min = Double.MAX_VALUE;
    double max = -Double.MAX_VALUE;

    Iterator<? extends Number> itr = data.iterator();
    Iterator<? extends Number> ebItr = errorBars.iterator();
    while (itr.hasNext()) {
      double d = itr.next().doubleValue();
      double eb = ebItr.next().doubleValue();
      if (d - eb < min) {
        min = d - eb;
      }
      if (d + eb > max) {
        max = d + eb;
      }
    }

    return new double[] {min, max};
  }
}
