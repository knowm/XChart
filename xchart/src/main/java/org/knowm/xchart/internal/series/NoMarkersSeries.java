package org.knowm.xchart.internal.series;

/**
 * A Series containing X and Y data to be plotted on a Chart with X and Y Axes, values associated
 * with each X-Y point, could be used for bubble sizes for example, but no error bars, as the min
 * and max are calculated differently. No markers.
 *
 * @author timmolter
 */
public abstract class NoMarkersSeries extends AxesChartSeriesNumericalNoErrorBars {

  /**
   * Constructor
   *
   * @param name
   * @param xData
   * @param yData
   * @param extraValues
   */
  protected NoMarkersSeries(
      String name, double[] xData, double[] yData, double[] extraValues, Series.DataType axisType) {

    super(name, xData, yData, extraValues, axisType);

    this.extraValues = extraValues;
    calculateMinMax();
  }

  @Override
  protected void calculateMinMax() {

    // xData
    double[] xMinMax = findMinMax(xData);
    xMin = xMinMax[0];
    xMax = xMinMax[1];
    // System.out.println(xMin);
    // System.out.println(xMax);

    // yData
    double[] yMinMax = null;
    yMinMax = findMinMax(yData);
    yMin = yMinMax[0];
    yMax = yMinMax[1];
    // System.out.println(yMin);
    // System.out.println(yMax);
  }
}
