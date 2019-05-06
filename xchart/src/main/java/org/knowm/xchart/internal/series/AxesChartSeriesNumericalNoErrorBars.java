package org.knowm.xchart.internal.series;

import java.util.List;


/**
 * A Series containing X and Y data to be plotted on a Chart with X and Y Axes. xData can be Number
 * or Date(epochtime), hence a double[]
 *
 * @author timmolter
 */
public abstract class AxesChartSeriesNumericalNoErrorBars<T> extends MarkerSeries {

  /**
   * Constructor
   *
   * @param name
   * @param xData
   * @param yData
   * @param xAxisDataType
   */
  public AxesChartSeriesNumericalNoErrorBars(
      String name, DataType xAxisDataType) {

    super(name, xAxisDataType);

    //calculateMinMax(); // should do this after vals are initialized
  }

  /**
   * This is an internal method which shouldn't be called from client code. Use
   * XYChart.updateXYSeries or CategoryChart.updateXYSeries instead!
   *
   * @param newXData
   * @param newYData
   * @param newExtraValues
   */
//  public void replaceData(double[] newXData, double[] newYData, double[] newExtraValues) {
//
//    // Sanity check
//    if (newExtraValues != null && newExtraValues.length != newYData.length) {
//      throw new IllegalArgumentException("error bars and Y-Axis sizes are not the same!!!");
//    }
//    if (newXData.length != newYData.length) {
//      throw new IllegalArgumentException("X and Y-Axis sizes are not the same!!!");
//    }
//
//    xData = newXData;
//    yData = newYData;
//    extraValues = newExtraValues;
//    calculateMinMax();
//  }
  
  
  /** 
   * Update name with dateUpdated or some such
   */
	public void replaceData() {
		calculateMinMax();
	}


  @Override
  protected void calculateMinMax() {
	  calculateMinMax(true);
  }
  
  protected void calculateMinMax(boolean inclExtraValues) {

    xMin=Double.MAX_VALUE;
    xMax=-Double.MAX_VALUE;
    yMin=Double.MAX_VALUE;
    yMax=-Double.MAX_VALUE;
    
    List<? extends T> data = getData();
	for (int i = 0; i < data.size(); i++) {
		T obj = data.get(i);
		double x = getX(i, obj).doubleValue();
    	if(!Double.isNaN(x)) {
            if(x< xMin)
            	xMin=x;
            if (x> xMax)
                xMax=x;
    	}
    	
    	double y = getY(i,obj).doubleValue();
    	if(!Double.isNaN(y)) {
    		double eb = hasExtraValues() ? getExtraValue(i,obj).doubleValue() : 0;
    		if(!inclExtraValues)
    			eb=0;
            if(y-eb<yMin)
            	yMin=y-eb;
            if (y+eb>yMax)
                yMax=y+eb;
    	}
	}

  }

	public abstract List<? extends T> getData();
	public abstract Number getX(int observationi, T obj);
	public abstract Number getY(int observationi, T obj);
	public abstract Number getExtraValue(int observationi, T obj);
	public abstract boolean hasExtraValues();
	  
}
