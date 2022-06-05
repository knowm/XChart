package org.knowm.xchart.internal.series;

import java.util.Comparator;
import java.util.List;

public class MinMaxListWithoutExtraValue implements MinMaxListStrategy {
	private double xMinDefault;
	private double xMaxDefault;
	private double yMinDefault;
	private double yMaxDefault;
	
	public MinMaxListWithoutExtraValue(double xMin, double xMax, double yMin, double yMax) {
		setxMinDefault(xMin);
		setxMaxDefault(xMax);
		setyMinDefault(yMin);
		setyMaxDefault(yMax);
	}
	
	@Override
	public double[] calculateMinMax(List<?> xData, List<?> yData) {
		double[] minMaxs = new double[4];
	    minMaxs[XMIN] = getMin(xData, getxMinDefault());
	    minMaxs[XMAX] = getMax(xData, getxMaxDefault());
	    minMaxs[YMIN] = getMin(yData, getyMinDefault());
	    minMaxs[YMAX] = getMax(yData, getyMaxDefault());
		return minMaxs;
	}
	private static double getMin(List<?> list, double defaultValue) {
		if (list.isEmpty() || !(list.get(0) instanceof Number)) {
			return defaultValue;
		}
		return list.stream()
				.map(x -> (Number) x)
				.min(Comparator.comparing(Number::doubleValue))
				.orElse(defaultValue)
				.doubleValue();
	}
	
	private static double getMax(List<?> list, double defaultValue) {
		if (list.isEmpty() || !(list.get(0) instanceof Number)) {
			return defaultValue;
		}
		return list.stream()
				.map(x -> (Number) x)
				.max(Comparator.comparing(Number::doubleValue))
				.orElse(defaultValue)
				.doubleValue();
	}

	private double getxMinDefault() {
		return xMinDefault;
	}

	private void setxMinDefault(double xMinDefault) {
		this.xMinDefault = xMinDefault;
	}

	private double getxMaxDefault() {
		return xMaxDefault;
	}

	private void setxMaxDefault(double xMaxDefault) {
		this.xMaxDefault = xMaxDefault;
	}

	private double getyMinDefault() {
		return yMinDefault;
	}

	private void setyMinDefault(double yMinDefault) {
		this.yMinDefault = yMinDefault;
	}

	private double getyMaxDefault() {
		return yMaxDefault;
	}

	private void setyMaxDefault(double yMaxDefault) {
		this.yMaxDefault = yMaxDefault;
	}
}
