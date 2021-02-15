package org.knowm.xchart.internal.chartpart;

import java.awt.geom.Rectangle2D;
import org.knowm.xchart.style.Styler;

public abstract class Annotation implements ChartPart {

  protected boolean isVisible = true;
  protected boolean isValueInScreenSpace;

  protected Chart chart;
  protected Styler styler;
  protected Rectangle2D bounds;

  public Annotation(boolean isValueInScreenSpace) {
    this.isValueInScreenSpace = isValueInScreenSpace;
  }

  public void init(Chart chart) {

    this.chart = chart;
    this.styler = chart.getStyler();
  }

  @Override
  public Rectangle2D getBounds() {

    return bounds;
  }

  public void setVisible(boolean visible) {
    isVisible = visible;
  }

  protected int getXAxisScreenValue(double chartSpaceValue) {
    return (int) chart.getXAxis().getScreenValue(chartSpaceValue);
  }

  protected int getYAxisScreenValue(double chartSpaceValue) {
    return (int) chart.getYAxis().getScreenValue(chartSpaceValue);
  }

  protected int getXAxisScreenValueForMax() {
    return (int) chart.getPlot().plotSurface.getBounds().getMaxX();
  }

  protected int getXAxisScreenValueForMin() {
    return (int) chart.getPlot().plotSurface.getBounds().getMinX();
  }

  protected int getYAxisScreenValueForMax() {
    return (int) chart.getPlot().plotSurface.getBounds().getMaxY();
  }

  protected int getYAxisScreenValueForMin() {
    return (int) chart.getPlot().plotSurface.getBounds().getMinY();
  }
}
