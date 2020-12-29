package org.knowm.xchart.internal.chartpart;

import java.awt.Rectangle;

public abstract class Annotation  implements ChartPart  {


  protected Chart chart;
  protected Rectangle bounds;


  public void init(Chart chart) {

    this.chart = chart;
  }
}
