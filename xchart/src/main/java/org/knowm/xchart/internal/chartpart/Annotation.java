package org.knowm.xchart.internal.chartpart;

import java.awt.geom.Rectangle2D;
import org.knowm.xchart.style.Styler;

public abstract class Annotation implements ChartPart {

  protected boolean isVisible = true;
  protected boolean isValueInScreenSpace;

  protected Chart chart;
  protected Styler styler;
  protected Rectangle2D bounds;

  public void init(Chart chart) {

    this.chart = chart;
    this.styler = chart.getStyler();
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
