package org.knowm.xchart.internal.chartpart;

import java.awt.Rectangle;

public abstract class Annotation implements ChartPart {

  protected boolean isVisible = true;
  protected boolean isValueInScreenSpace;

  protected double x;
  protected double y;

  protected Chart chart;
  protected Rectangle bounds;

  public void init(Chart chart) {

    this.chart = chart;
  }

  public void setVisible(boolean visible) {
    isVisible = visible;
  }

  protected double getXAxisSreenValue(double chartSpaceValue) {
    return chart.getXAxis().getScreenValue(chartSpaceValue);
  }

  protected double getYAxisSreenValue(double chartSpaceValue) {
    return chart.getYAxis().getScreenValue(chartSpaceValue);
  }
}
