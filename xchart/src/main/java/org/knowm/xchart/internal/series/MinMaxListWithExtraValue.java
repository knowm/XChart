package org.knowm.xchart.internal.series;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.knowm.xchart.internal.series.Series.DataType;

public class MinMaxListWithExtraValue implements MinMaxListStrategy {
	private List<? extends Number> extraValues;
	private DataType yAxisType;
	private DataType xAxisDataType;
	
	public MinMaxListWithExtraValue(List<? extends Number> extraValues, DataType xAxisDataType, DataType yAxisType) {
		setExtraValues(extraValues);
		setxAxisDataType(xAxisDataType);
		setyAxisType(yAxisType);
	}
	
	@Override
	public double[] calculateMinMax(List<?> xData, List<?> yData) {
		double[] minMaxs = new double[4];
		calculateXMinMax(minMaxs, xData);
		calculateYMinMax(minMaxs, yData);
		return minMaxs;
	}

	private void calculateYMinMax(double[] minMaxs, List<?> yData) {
		double[] yMinMax;
		if (getExtraValues() == null) {
		  yMinMax = findMinMax(yData, getyAxisType());
		} else {
		  yMinMax = findMinMaxWithErrorBars((List<? extends Number>)yData, getExtraValues());
		}
		minMaxs[YMIN] = yMinMax[0];
		minMaxs[YMAX] = yMinMax[1];
	}

	private void calculateXMinMax(double[] minMaxs, List<?> xData) {
		double[] xMinMax = findMinMax(xData, getxAxisDataType());
		minMaxs[XMIN] = xMinMax[0];
		minMaxs[XMAX] = xMinMax[1];
	}
	
	private double[] findMinMaxWithErrorBars(
		Collection<? extends Number> data, Collection<? extends Number> errorBars) {

		double min = Double.MAX_VALUE;
		double max = -Double.MAX_VALUE;

		Iterator<? extends Number> iterator = data.iterator();
		Iterator<? extends Number> errorBarItr = errorBars.iterator();
		while (iterator.hasNext()) {
			double bigDecimal = iterator.next().doubleValue();
			double errorBar = errorBarItr.next().doubleValue();
			if (bigDecimal - errorBar < min) {
				min = bigDecimal - errorBar;
			}
			if (bigDecimal + errorBar > max) {
				max = bigDecimal + errorBar;
			}
		}
		return new double[] {min, max};
	}
	
	double[] findMinMax(Collection<?> data, DataType dataType) {

		double min = Double.MAX_VALUE;
		double max = -Double.MAX_VALUE;

		for (Object dataPoint : data) {

			if (dataPoint == null) {
				continue;
			}
			if (dataType == DataType.String) {
				return new double[] {Double.NaN, Double.NaN};
			}
		      
			if (dataValue(dataType, dataPoint) < min) {
				min = dataValue(dataType, dataPoint);
			}
			if (dataValue(dataType, dataPoint) > max) {
				max = dataValue(dataType, dataPoint);
			}
		}

		return new double[] {min, max};
	}
	
	private double dataValue(DataType dataType, Object dataPoint) {
		if (dataType == DataType.Number) {
			return ((Number) dataPoint).doubleValue();
		}
		return ((Date)dataPoint).getTime();
	}

	private DataType getxAxisDataType() {
		return xAxisDataType;
	}

	private void setxAxisDataType(DataType xAxisDataType) {
		this.xAxisDataType = xAxisDataType;
	}

	private DataType getyAxisType() {
		return yAxisType;
	}

	private void setyAxisType(DataType yAxisType) {
		this.yAxisType = yAxisType;
	}

	private List<? extends Number> getExtraValues() {
		return extraValues;
	}

	private void setExtraValues(List<? extends Number> extraValues) {
		this.extraValues = extraValues;
	}
}
