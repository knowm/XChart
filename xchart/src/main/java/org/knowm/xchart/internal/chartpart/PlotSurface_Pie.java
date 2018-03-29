package org.knowm.xchart.internal.chartpart;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.style.Styler;

/**
 * Draws the plot background and the plot border
 *
 * @author timmolter
 */
public class PlotSurface_Pie<ST extends Styler, S extends Series> extends PlotSurface_<ST, S> {

  private final ST styler;

  /**
   * Constructor
   *
   * @param chart
   */
  PlotSurface_Pie(Chart<ST, S> chart) {

    super(chart);
    this.styler = chart.getStyler();
  }

  @Override
  public void paint(Graphics2D g) {

    Rectangle2D bounds = getBounds();

    // paint plot background
    Shape rect =
        new Rectangle2D.Double(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
    g.setColor(styler.getPlotBackgroundColor());
    g.fill(rect);

    // paint plot border
    if (styler.isPlotBorderVisible()) {
      g.setColor(styler.getPlotBorderColor());
      // g.setStroke(getChartPainter().getstyler().getAxisTickMarksStroke());
      g.draw(rect);
    }
  }
}
