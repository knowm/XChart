package org.knowm.xchart.internal.chartpart;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.style.Styler;

/** Chart Title */
public class ChartTitle<ST extends Styler, S extends Series> implements ChartPart {

  private final Chart<ST, S> chart;
  private Rectangle2D bounds;

  /**
   * Constructor
   *
   * @param chart
   */
  public ChartTitle(Chart<ST, S> chart) {

    this.chart = chart;
  }

  @Override
  public void paint(Graphics2D g) {

    g.setFont(chart.getStyler().getChartTitleFont());

    if (!chart.getStyler().isChartTitleVisible() || chart.getTitle().length() == 0) {
      return;
    }

    Object oldHint = g.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    // create rectangle first for sizing
    FontRenderContext frc = g.getFontRenderContext();
    TextLayout textLayout =
        new TextLayout(chart.getTitle(), chart.getStyler().getChartTitleFont(), frc);
    Rectangle2D textBounds = textLayout.getBounds();

    double xOffset = chart.getPlot().getBounds().getX(); // of plot left edge
    double yOffset = chart.getStyler().getChartPadding();

    // title box
    if (chart.getStyler().isChartTitleBoxVisible()) {

      // paint the chart title box
      double chartTitleBoxWidth = chart.getPlot().getBounds().getWidth();
      double chartTitleBoxHeight =
          textBounds.getHeight() + 2 * chart.getStyler().getChartTitlePadding();

      g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
      Shape rect =
          new Rectangle2D.Double(xOffset, yOffset, chartTitleBoxWidth, chartTitleBoxHeight);
      g.setColor(chart.getStyler().getChartTitleBoxBackgroundColor());
      g.fill(rect);
      g.setColor(chart.getStyler().getChartTitleBoxBorderColor());
      g.draw(rect);
    }

    // paint title
    xOffset =
        chart.getPlot().getBounds().getX()
            + (chart.getPlot().getBounds().getWidth() - textBounds.getWidth()) / 2.0;
    yOffset =
        chart.getStyler().getChartPadding()
            + textBounds.getHeight()
            + chart.getStyler().getChartTitlePadding();

    g.setColor(chart.getStyler().getChartFontColor());
    Shape shape = textLayout.getOutline(null);
    AffineTransform orig = g.getTransform();
    AffineTransform at = new AffineTransform();
    at.translate(xOffset, yOffset);
    g.transform(at);
    g.fill(shape);
    g.setTransform(orig);

    double width = 2 * chart.getStyler().getChartTitlePadding() + textBounds.getWidth();
    double height = 2 * chart.getStyler().getChartTitlePadding() + textBounds.getHeight();
    bounds =
        new Rectangle2D.Double(
            xOffset - chart.getStyler().getChartTitlePadding(),
            yOffset - textBounds.getHeight() - chart.getStyler().getChartTitlePadding(),
            width,
            height);
    // g.setColor(Color.blue);
    // g.draw(bounds);

    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldHint);
  }

  /**
   * get the height of the chart title including the chart title padding
   *
   * @return a Rectangle2D defining the height of the chart title including the chart title padding
   */
  private Rectangle2D getBoundsHint() {

    if (chart.getStyler().isChartTitleVisible() && chart.getTitle().length() > 0) {

      TextLayout textLayout =
          new TextLayout(
              chart.getTitle(),
              chart.getStyler().getChartTitleFont(),
              new FontRenderContext(null, true, false));
      Rectangle2D rectangle = textLayout.getBounds();
      double width = 2 * chart.getStyler().getChartTitlePadding() + rectangle.getWidth();
      double height = 2 * chart.getStyler().getChartTitlePadding() + rectangle.getHeight();

      return new Rectangle2D.Double(
          Double.NaN, Double.NaN, width, height); // Double.NaN indicates not sure yet.
    } else {
      return new Rectangle2D
          .Double(); // Constructs a new Rectangle2D, initialized to location (0, 0) and size (0,
      // 0).
    }
  }

  @Override
  public Rectangle2D getBounds() {

    if (bounds
        == null) { // was not drawn fully yet, just need the height hint. The Plot object will be
      // asking for it.
      bounds = getBoundsHint();
    }
    return bounds;
  }
}
