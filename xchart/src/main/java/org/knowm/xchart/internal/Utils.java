package org.knowm.xchart.internal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/** @author timmolter */
public class Utils {

  /** Private Constructor */
  private Utils() {}

  /**
   * Gets the offset for the beginning of the tick marks
   *
   * @param workingSpace
   * @param tickSpace
   * @return
   */
  public static double getTickStartOffset(double workingSpace, double tickSpace) {

    double marginSpace = workingSpace - tickSpace;
    return marginSpace / 2.0;
  }

  public static double pow(double base, int exponent) {

    if (exponent > 0) {
      return Math.pow(base, exponent);
    } else {
      return 1.0 / Math.pow(base, -1 * exponent);
    }
  }

  public static List<Double> getNumberListFromDoubleArray(double[] data) {

    if (data == null) {
      return null;
    }

    List<Double> dataNumber;
    dataNumber = new ArrayList<Double>();
    for (double d : data) {
      dataNumber.add(d);
    }
    return dataNumber;
  }

  public static List<Double> getNumberListFromIntArray(int[] data) {

    if (data == null) {
      return null;
    }

    List<Double> dataNumber;
    dataNumber = new ArrayList<Double>();
    for (double d : data) {
      dataNumber.add(d);
    }
    return dataNumber;
  }

  public static List<Double> getGeneratedDataAsList(int length) {

    List<Double> generatedData = new ArrayList<Double>();
    for (int i = 1; i < length + 1; i++) {
      generatedData.add((double) i);
    }
    return generatedData;
  }

  public static double[] getDoubleArrayFromFloatArray(float[] data) {

    if (data == null) {
      return null;
    }
    double[] doubles = new double[data.length];

    for (int i = 0; i < data.length; i++) {
      doubles[i] = data[i];
    }
    return doubles;
  }

  public static double[] getDoubleArrayFromIntArray(int[] data) {

    if (data == null) {
      return null;
    }
    double[] doubles = new double[data.length];

    for (int i = 0; i < data.length; i++) {
      doubles[i] = data[i];
    }
    return doubles;
  }

  public static double[] getDoubleArrayFromNumberList(List<?> data) {

    if (data == null) {
      return null;
    }
    double[] doubles = new double[data.size()];

    for (int i = 0; i < data.size(); i++) {

      if (data.get(i) == null) {
        doubles[i] = Double.NaN;
      } else {
        doubles[i] = ((Number) data.get(i)).doubleValue();
      }
    }
    return doubles;
  }

  public static double[] getDoubleArrayFromDateList(List<?> data) {

    if (data == null) {
      return null;
    }
    double[] doubles = new double[data.size()];

    for (int i = 0; i < data.size(); i++) {
      doubles[i] = ((Date) data.get(i)).getTime();
    }
    return doubles;
  }

  public static double[] getGeneratedDataAsArray(int length) {

    double[] generatedData = new double[length];
    for (int i = 0; i < length; i++) {
      generatedData[i] = ((double) i + 1);
    }
    return generatedData;
  }
}
