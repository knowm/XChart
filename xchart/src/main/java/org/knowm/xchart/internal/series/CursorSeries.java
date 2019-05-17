package org.knowm.xchart.internal.series;

import org.knowm.xchart.cursors.Cursor;

public abstract class CursorSeries extends AxesChartSeriesNumericalNoErrorBars {
   /**
    Constructor
    @param name
    @param xData
    @param yData
    @param extraValues
    @param xAxisDataType */
   public CursorSeries(String name, double[] xData, double[] yData, double[] extraValues, DataType xAxisDataType) {
      super(name, xData, yData, extraValues, xAxisDataType);
   }

   public Cursor getCursor() {
      return cursor;
   }

   public void setCursor(Cursor cursor) {
      this.cursor = cursor;
   }

   private Cursor cursor;
}
