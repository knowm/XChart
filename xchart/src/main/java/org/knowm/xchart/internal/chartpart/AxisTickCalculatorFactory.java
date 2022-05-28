package org.knowm.xchart.internal.chartpart;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.knowm.xchart.internal.chartpart.Axis.Direction;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.style.AxesChartStyler;

public class AxisTickCalculatorFactory extends AxisTickFactory{
	
  @Override
  public AxisTickCalculator_ calculator(AxisTickCalculatorType calculatorType,
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
	  case AxisTickCalculator_Callback:
		  return new AxisTickCalculator_Callback(formattingCallback, axisDirection, workingSpace, minValue, maxValue, axisValues, styler);
	  case AxisTickCalculator_Category:
		  return new AxisTickCalculator_Category(axisDirection, workingSpace, categories, axisType, styler);
	  case AxisTickCalculator_Date:
		  return new AxisTickCalculator_Date(axisDirection, workingSpace, minValue, maxValue, styler);
	  case AxisTickCalculator_Logarithmic:
		  return new AxisTickCalculator_Logarithmic(axisDirection, workingSpace, minValue, maxValue, styler);
	  case AxisTickCalculator_Number:
		  return new AxisTickCalculator_Number(axisDirection, workingSpace, minValue, maxValue, axisValues, styler, yIndex);
	  default:
		  throw new RuntimeException(calculatorType.toString() + " is not existed");
	  }
  }
}
