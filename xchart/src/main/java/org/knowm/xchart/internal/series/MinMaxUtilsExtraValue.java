package org.knowm.xchart.internal.series;

public class MinMaxUtilsExtraValue extends MinMaxUtils {
	private double[] extraValues;
	
	public MinMaxUtilsExtraValue(double[] extraValue) {
		setExtraValues(extraValue);
	}
	
	@Override
	public double[] calcualteMinMax(double[] xData, double[] yData) {
		sanityCheck(xData, yData);
		double[] minMaxs = new double[4];
		
		setXMinMax(minMaxs, xData);
		setYMinMaxWithErrorBars(minMaxs, findMinMaxWithErrorBars(yData, getExtraValues()));
	    
	    return minMaxs;
	}

	private void setYMinMaxWithErrorBars(double[] minMaxs, double[] yMinMax) {
		minMaxs[YMIN] = yMinMax[0];
		minMaxs[YMAX] = yMinMax[1];
	}
	
	  /**
	   * Finds the min and max of a dataset accounting for error bars
	   *
	   * @param data
	   * @param errorBars
	   * @return
	   */
	private double[] findMinMaxWithErrorBars(double[] data, double[] errorBars) {
		assert data.length == errorBars.length;
		double min = Double.MAX_VALUE;
		double max = -Double.MAX_VALUE;
		for (int i = 0; i < data.length; i++) {
		  double datum = data[i];
		  double errorBar = errorBars[i];
		  if (datum - errorBar < min) {
		    min = datum - errorBar;
		  }
		  if (datum + errorBar > max) {
		    max = datum + errorBar;
		  }
		}
		return new double[] {min, max};
	}
	
	private void setExtraValues(double[] extras) {
		this.extraValues = extras;
	}
	
	private double[] getExtraValues() {
		return this.extraValues;
	}

}
