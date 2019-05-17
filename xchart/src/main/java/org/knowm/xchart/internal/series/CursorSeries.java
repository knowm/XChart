package org.knowm.xchart.internal.series;

import java.awt.Color;
import org.knowm.xchart.cursors.Cursor;

public abstract class CursorSeries extends AxesChartSeriesNumericalNoErrorBars {
   private Cursor cursor;
   private Color cursorColor = Color.DARK_GRAY;

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

   public Color getCursorColor() {
      return cursorColor;
   }

   public void setCursorColor(Color cursorColor) {
      this.cursorColor = cursorColor;
   }
}
