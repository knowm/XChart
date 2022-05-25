package org.knowm.xchart;

import java.util.Date;
import org.knowm.xchart.internal.series.Series.DataType;


public class DataTypeFactory {
	public DataType getType(Object dataPoint) {

		if (dataPoint instanceof Number) {
		      return DataType.Number;
		    } else if (dataPoint instanceof Date) {
		      return DataType.Date;
		    } else if (dataPoint instanceof String) {
		      return DataType.String;
		    } else {
		      throw new IllegalArgumentException(
		          "Series data must be either Number, Date or String type!!!");
		    }
	}
}
