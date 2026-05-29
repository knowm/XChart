package org.knowm.xchart.internal.chartpart;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import org.knowm.xchart.internal.series.AxesChartSeries;
import org.knowm.xchart.style.AxesChartStyler;

public class Plot_AxesChart<ST extends AxesChartStyler, S extends AxesChartSeries>
    extends Plot_<ST, S> {

  private final AxesChart<ST, S> axesChart;

  /**
   * Constructor
   *
   * @param chart
   */
  Plot_AxesChart(AxesChart<ST, S> chart) {

    super(chart);
    this.axesChart = chart;
    this.plotSurface = new PlotSurface_AxesChart<ST, S>(chart);
  }

  @Override
  public void paint(Graphics2D g) {

    Rectangle2D yAxisBounds = axesChart.getAxisPair().getLeftYAxisBounds();
    Rectangle2D xAxisBounds = axesChart.getXAxis().getBounds();

    // calculate bounds
    double xOffset = xAxisBounds.getX();
    double yOffset = yAxisBounds.getY();
    double width = xAxisBounds.getWidth();
    double height = yAxisBounds.getHeight();
    this.bounds = new Rectangle2D.Double(xOffset, yOffset, width, height);

    super.paint(g);
  }
}
