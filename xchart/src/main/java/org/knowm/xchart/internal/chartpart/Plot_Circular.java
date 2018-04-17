package org.knowm.xchart.internal.chartpart;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.style.Styler;

public abstract class Plot_Circular<ST extends Styler, S extends Series> extends Plot_<ST, S> {

  /**
   * Constructor
   *
   * @param chart
   */
  public Plot_Circular(Chart<ST, S> chart) {

    super(chart);
    initContentAndSurface(chart);
  }

  protected abstract void initContentAndSurface(Chart<ST, S> chart);

  @Override
  public void paint(Graphics2D g) {

    // calculate bounds
    double xOffset = chart.getStyler().getChartPadding();

    // double yOffset = chart.getChartTitle().getBounds().getHeight() + 2 *
    // chart.getStyler().getChartPadding();
    double yOffset =
        chart.getChartTitle().getBounds().getHeight() + chart.getStyler().getChartPadding();

    double width =
        chart.getWidth()
            - (chart.getStyler().getLegendPosition() == Styler.LegendPosition.OutsideE
                ? chart.getLegend().getBounds().getWidth()
                : 0)
            - 2 * chart.getStyler().getChartPadding()
            - (chart.getStyler().getLegendPosition() == Styler.LegendPosition.OutsideE
                    && chart.getStyler().isLegendVisible()
                ? chart.getStyler().getChartPadding()
                : 0);

    double height =
        chart.getHeight()
            - chart.getChartTitle().getBounds().getHeight()
            - (chart.getStyler().getLegendPosition() == Styler.LegendPosition.OutsideS
                ? chart.getLegend().getBounds().getHeight()
                : 0)
            - 2 * chart.getStyler().getChartPadding();

    this.bounds = new Rectangle2D.Double(xOffset, yOffset, width, height);

    super.paint(g);
  }
}
