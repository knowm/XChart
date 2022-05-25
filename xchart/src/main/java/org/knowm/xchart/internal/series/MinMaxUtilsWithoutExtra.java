package org.knowm.xchart.internal.series;

public class MinMaxUtilsWithoutExtra extends MinMaxUtils {

	@Override
	public double[] calcualteMinMax(double[] xData, double[] yData) {
		sanityCheck(xData, yData);
		double[] minMaxs = new double[4];
		setXMinMax(minMaxs, xData);
		setYMinMax(minMaxs, yData);
		return minMaxs;
	}
}
