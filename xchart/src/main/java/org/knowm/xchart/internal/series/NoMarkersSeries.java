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

    super(name, new SeriesDataValues(xData, yData, extraValues), axisType);

    // TODO why do we need this here?
    this.extraValues = extraValues;
    calculateMinMax();
  }

  @Override
  protected void calculateMinMax() {
	  setXYMinMax(MinMaxFactory
			  .getMinMaxCalculator()
			  .calcualteMinMax(getXData(), getYData()));
    // xData
    // System.out.println(xMin);
    // System.out.println(xMax);

    // yData
    // System.out.println(yMin);
    // System.out.println(yMax);
  }

}
