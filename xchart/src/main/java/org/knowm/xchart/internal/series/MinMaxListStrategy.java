package org.knowm.xchart.internal.series;

import java.util.List;

public interface MinMaxListStrategy {
	final int XMIN = 0;
	final int XMAX = 1;
	final int YMIN = 2;
	final int YMAX = 3;
	public double[] calculateMinMax(List<?> xData, List<?> yData);
}
