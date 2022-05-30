package org.knowm.xchart.internal.chartpart;

import java.util.List;
import java.util.function.Function;

import org.knowm.xchart.internal.chartpart.Axis.Direction;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.style.AxesChartStyler;

public abstract class AxisTickCalculatorFactory {
  abstract AxisTickCalculator_ calculator(AxisTickCalculatorType calculatorType,
		  Function<Double, String> formattingCallback,
	      Direction axisDirection,
	      double workingSpace,
	      double minValue,
	      double maxValue,
	      List<Double> axisValues,
	      AxesChartStyler styler,
	      List<?> categories,
	      Series.DataType axisType,
	      int yIndex);
}
