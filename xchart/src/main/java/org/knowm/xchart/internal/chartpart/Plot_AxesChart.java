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

    // When the window is resized to be very small (especially with multiple Y-axis groups),
    // axis columns can consume more space than the total chart width, leaving a zero or
    // negative plot area. Painting with degenerate geometry causes NullPointerException in
    // AWT path operations, so bail out early. We still assign a zero-size bounds so that
    // downstream components (e.g. Legend_) that read getBounds() always receive a non-null
    // Rectangle2D. See issue #712.
    if (width <= 0 || height <= 0) {
      this.bounds = new Rectangle2D.Double(xOffset, yOffset, 0, 0);
      return;
    }

    this.bounds = new Rectangle2D.Double(xOffset, yOffset, width, height);

    super.paint(g);
  }
}
