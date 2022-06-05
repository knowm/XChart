package org.knowm.xchart.internal.series;

import org.knowm.xchart.internal.Utils;

public abstract class MinMaxUtils implements MinMaxArrStrategy {
	protected void sanityCheck(double[] xData, double[] yData) {
		if (xData == null || xData.length == 0) {
			throw new IllegalArgumentException("X Data Is not Valid !!!");
		}
		if (yData == null || yData.length == 0) {
			throw new IllegalArgumentException("Y Data Is not Valid !!!");
		}
	}
	
	protected void setYMinMax(double[] minMaxs, double[] yData) {
		minMaxs[YMIN] = Utils.findMin(yData);
		minMaxs[YMAX] = Utils.findMax(yData);
	}

	protected void setXMinMax(double[] minMaxs, double[] xData) {
		minMaxs[XMIN] = Utils.findMin(xData);
		minMaxs[XMAX] = Utils.findMax(xData);
	}
}
