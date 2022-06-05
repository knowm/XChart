package org.knowm.xchart.internal.series;

public class MinMaxLowHigh implements MinMaxArrStrategy {

	private double[] lowData;
	private double[] highData;
	
	MinMaxLowHigh(double[] lowData, double[] highData) {
		this.setLowData(lowData);
		this.setHighData(highData);
	}
	
	
	@Override
	public double[] calcualteMinMax(double[] xData, double[] yData) {
		double[] minMaxs = new double[4];
	    setXMinMax(xData, minMaxs);
	    setYMinMax(yData, minMaxs);
		return minMaxs;
	}


	private void setYMinMax(double[] yData, double[] minMaxs) {
		final double[] yMinMax;
	    if (yData == null) {
	      yMinMax = findMinMax(getLowData(), getHighData());
	    } else {
	      yMinMax = findMinMax(yData, yData);
	    }
	    minMaxs[YMIN] = yMinMax[0];
	    minMaxs[YMAX] = yMinMax[1];
	}


	private void setXMinMax(double[] xData, double[] minMaxs) {
		double[] xMinMax = findMinMax(xData, xData);
	    minMaxs[XMIN] = xMinMax[0];
	    minMaxs[XMAX] = xMinMax[1];
	}

	private double[] findMinMax(double[] lows, double[] highs) {

		double min = Double.MAX_VALUE;
		double max = -Double.MAX_VALUE;

		for (int i = 0; i < highs.length; i++) {

		  if (!Double.isNaN(highs[i]) && highs[i] > max) {
		    max = highs[i];
		  }
		  if (!Double.isNaN(lows[i]) && lows[i] < min) {
		    min = lows[i];
		  }
		}

		return new double[] {min, max};
	}	

	private double[] getLowData() {
		return lowData;
	}


	private void setLowData(double[] lowData) {
		this.lowData = lowData;
	}


	private double[] getHighData() {
		return highData;
	}


	private void setHighData(double[] highData) {
		this.highData = highData;
	}

	
	
	
	
	
}
