package org.knowm.xchart.internal.chartpart;

import java.util.List;
import java.util.function.Function;

import org.knowm.xchart.internal.chartpart.Axis.Direction;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.style.AxesChartStyler;

public class AxisTickCalculatorTypeFactory extends AxisTickCalculatorFactory{
	
  @Override
  public AxisTickCalculator_ calculator(
		  AxisTickCalculatorType calculatorType,
		  Function<Double, String> formattingCallback,
	      Direction axisDirection,
	      double workingSpace,
	      double minValue,
	      double maxValue,
	      List<Double> axisValues,
	      AxesChartStyler styler,
	      List<?> categories,
	      Series.DataType axisType,
	      int yIndex) {
	  switch(calculatorType) {
	  case CallbackWithAxis:
		  return new AxisTickCalculator_Callback(formattingCallback, axisDirection, workingSpace, minValue, maxValue, axisValues, styler);
	  case CallbackWithoutAxis:
		  return new AxisTickCalculator_Callback(formattingCallback, axisDirection, workingSpace, minValue, maxValue, styler);
	  case Category:
		  return new AxisTickCalculator_Category(axisDirection, workingSpace, categories, axisType, styler);
	  case Date:
		  return new AxisTickCalculator_Date(axisDirection, maxValue, maxValue, maxValue, styler);
	  case LogarithmicWithIndex:
		  return new AxisTickCalculator_Logarithmic(axisDirection, maxValue, maxValue, maxValue, styler, yIndex);
	  case LogarithmicWithoutIndex:
		  return new AxisTickCalculator_Logarithmic(axisDirection, maxValue, maxValue, maxValue, styler);
	  case Number:
		  return new AxisTickCalculator_Number(axisDirection, maxValue, maxValue, maxValue, styler);
	  case NumberWithAxis:
		  return new AxisTickCalculator_Number(axisDirection, maxValue, maxValue, maxValue, axisValues, styler);
	  case NumberWithIndex:
		  return new AxisTickCalculator_Number(axisDirection, maxValue, maxValue, maxValue, styler, yIndex);
	  default:
		  throw new RuntimeException(calculatorType.toString() + " is not existed");
	  }
  }
}
