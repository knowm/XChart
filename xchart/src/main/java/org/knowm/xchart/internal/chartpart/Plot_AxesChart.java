package org.knowm.xchart.internal.chartpart;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.style.AxesChartStyler;

/** @author timmolter */
public class Plot_AxesChart<ST extends AxesChartStyler, S extends Series> extends Plot_<ST, S> {

  /**
   * Constructor
   *
   * @param chart
   */
  Plot_AxesChart(Chart<ST, S> chart) {

    super(chart);
    this.plotSurface = new PlotSurface_AxesChart<ST, S>(chart);
  }

  @Override
  public void paint(Graphics2D g) {

    Rectangle2D yAxisBounds = chart.getAxisPair().getLeftYAxisBounds();
    Rectangle2D xAxisBounds = chart.getXAxis().getBounds();

    // calculate bounds
    double xOffset = xAxisBounds.getX();
    double yOffset = yAxisBounds.getY();
    double width = xAxisBounds.getWidth();
    double height = yAxisBounds.getHeight();
    this.bounds = new Rectangle2D.Double(xOffset, yOffset, width, height);

    super.paint(g);
  }
}
