package org.knowm.xchart.internal.series;

public interface MinMaxArrStrategy {
	final int XMIN = 0;
	final int XMAX = 1;
	final int YMIN = 2;
	final int YMAX = 3;
	public double[] calcualteMinMax(double[] xData, double[] yData);
}
