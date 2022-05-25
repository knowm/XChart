package org.knowm.xchart.internal.series;

import java.util.List;

import org.knowm.xchart.internal.series.Series.DataType;

public class MinMaxFactory {
	public static MinMaxArrStrategy getMinMaxCalculator(double[] extras) {
		if (extras == null) {
			return new MinMaxUtilsWithoutExtra();
		}
		return new MinMaxUtilsExtraValue(extras);
	}
	
	public static MinMaxArrStrategy getMinMaxCalculator() {
		return new MinMaxUtilsWithoutExtra();
	}
	
	public static MinMaxArrStrategy getMinMaxCalculator(double[] lowData, double[] highData) {
		return new MinMaxLowHigh(lowData, highData);
	}
	
	public static MinMaxListStrategy getMinMaxCalculator(List<? extends Number> extraValues, DataType xAxisDataType, DataType yAxisType) {
		return new MinMaxListWithExtraValue(extraValues, xAxisDataType, yAxisType);
	}

	public static MinMaxListStrategy getMinMaxCalculator(double xMin, double xMax, double yMin, double yMax) {
		return new MinMaxListWithoutExtraValue(xMin, xMax, yMin, yMax);
	}
	
}
