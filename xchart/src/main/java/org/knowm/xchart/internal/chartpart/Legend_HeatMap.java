package org.knowm.xchart.internal.chartpart;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import org.knowm.xchart.HeatMapChart;
import org.knowm.xchart.HeatMapSeries;
import org.knowm.xchart.style.HeatMapStyler;
import org.knowm.xchart.style.Styler;

/** @author Mr14huashao */
public class Legend_HeatMap<ST extends HeatMapStyler, S extends HeatMapSeries>
    extends Legend_<ST, S> {

  private static final int LEGEND_MARGIN = 6;

  private static final String SPLIT = " ~ ";

  private final DecimalFormat df = new DecimalFormat("");

  public Legend_HeatMap(Chart<ST, S> chart) {

    super(chart);
  }

  @Override
  public void paint(Graphics2D g) {

    if (!chart.getStyler().isLegendVisible()) {
      return;
    }

    if (chart.getSeriesMap().isEmpty()) {
      return;
    }

    // if the area to draw a chart on is so small, don't even bother
    if (chart.getPlot().getBounds().getWidth() < 30) {
      return;
    }
    Rectangle2D bounds = getBounds();

    switch (chart.getStyler().getLegendPosition()) {
      case OutsideE:
        xOffset = chart.getWidth() - bounds.getWidth() - LEGEND_MARGIN;
        yOffset =
            chart.getPlot().getBounds().getY()
                + (chart.getPlot().getBounds().getHeight() - bounds.getHeight()) / 2.0;
        break;
      case OutsideS:
        xOffset =
            chart.getPlot().getBounds().getX()
                + (chart.getPlot().getBounds().getWidth() - bounds.getWidth()) / 2.0;
        yOffset = chart.getHeight() - bounds.getHeight() - LEGEND_MARGIN;
        break;

      default:
        break;
    }

    // draw legend box background and border
    Shape rect = new Rectangle2D.Double(xOffset, yOffset, bounds.getWidth(), bounds.getHeight());
    g.setColor(chart.getStyler().getLegendBackgroundColor());
    g.fill(rect);
    g.setStroke(SOLID_STROKE);
    g.setColor(chart.getStyler().getLegendBorderColor());
    g.draw(rect);

    doPaint(g);
  }

  @Override
  public void doPaint(Graphics2D g) {

    // Draw legend content inside legend box
    double startx = xOffset + chart.getStyler().getLegendPadding();
    double starty = yOffset + chart.getStyler().getLegendPadding();

    Object oldHint = g.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    Color[] rangeColors = chart.getStyler().getRangeColors();
    HeatMapSeries heatMapSeries = ((HeatMapChart) chart).getHeatMapSeries();
    if (chart.getStyler().isPiecewise()) {
      paintPiecewise(g, startx, starty, rangeColors, heatMapSeries);
    } else {
      paintGradient(g, startx, starty, rangeColors, heatMapSeries);
    }
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldHint);
  }

  @Override
  public double getSeriesLegendRenderGraphicHeight(S series) {

    return 0;
  }

  @Override
  public Rectangle2D getBounds() {

    if (chart.getStyler().getHeatMapValueDecimalPattern() != null) {
      df.applyPattern(chart.getStyler().getHeatMapValueDecimalPattern());
    }
    double weight = 0;
    double height = 0;
    HeatMapSeries heatMapSeries = ((HeatMapChart) chart).getHeatMapSeries();
    double min = heatMapSeries.getMin();
    double max = heatMapSeries.getMax();
    if (chart.getStyler().isPiecewise()) {
      int splitNumber = chart.getStyler().getSplitNumber();
      double step = (max - min) / splitNumber;
      String text = "";
      TextLayout textLayout = null;
      for (int i = 0; i < splitNumber; i++) {
        if (i == 0) {
          text = df.format(min) + SPLIT + df.format(min + step);
        } else if (i == splitNumber - 1) {
          text = df.format(min + step * i) + SPLIT + df.format(max);
        } else {
          text = df.format(min + step * i) + SPLIT + df.format(min + step * (i + 1));
        }
        textLayout =
            new TextLayout(
                text, chart.getStyler().getLegendFont(), new FontRenderContext(null, true, false));

        if (chart.getStyler().getLegendLayout() == Styler.LegendLayout.Vertical) {
          weight = Math.max(weight, textLayout.getBounds().getWidth());
          height +=
              chart.getStyler().getLegendFont().getSize() + chart.getStyler().getLegendPadding();
        } else {
          weight +=
              BOX_SIZE
                  + chart.getStyler().getLegendPadding()
                  + textLayout.getBounds().getWidth()
                  + chart.getStyler().getLegendPadding();
        }
      }
      if (chart.getStyler().getLegendLayout() == Styler.LegendLayout.Vertical) {
        weight =
            chart.getStyler().getLegendPadding()
                + BOX_SIZE
                + chart.getStyler().getLegendPadding()
                + weight
                + chart.getStyler().getLegendPadding();
        height += chart.getStyler().getLegendPadding();
      } else {
        weight += chart.getStyler().getLegendPadding();
        height =
            chart.getStyler().getLegendPadding()
                + chart.getStyler().getLegendFont().getSize()
                + chart.getStyler().getLegendPadding();
      }
    } else {

      TextLayout textLayoutMin =
          new TextLayout(
              min + "",
              chart.getStyler().getLegendFont(),
              new FontRenderContext(null, true, false));

      TextLayout textLayoutMax =
          new TextLayout(
              max + "",
              chart.getStyler().getLegendFont(),
              new FontRenderContext(null, true, false));

      if (chart.getStyler().getLegendLayout() == Styler.LegendLayout.Vertical) {
        weight =
            chart.getStyler().getLegendPadding()
                + chart.getStyler().getGradientColorColumnWeight()
                + chart.getStyler().getLegendPadding()
                + Math.max(
                    textLayoutMin.getBounds().getWidth(), textLayoutMax.getBounds().getWidth())
                + chart.getStyler().getLegendPadding();
        height =
            chart.getStyler().getLegendPadding()
                + chart.getStyler().getLegendFont().getSize()
                + chart.getStyler().getGradientColorColumnHeight()
                + chart.getStyler().getLegendFont().getSize()
                + chart.getStyler().getLegendPadding();

      } else {
        weight =
            chart.getStyler().getLegendPadding()
                + textLayoutMin.getBounds().getWidth()
                + chart.getStyler().getGradientColorColumnHeight()
                + textLayoutMax.getBounds().getWidth()
                + chart.getStyler().getLegendPadding();
        height =
            chart.getStyler().getLegendPadding()
                + chart.getStyler().getLegendFont().getSize()
                + chart.getStyler().getLegendPadding()
                + chart.getStyler().getGradientColorColumnWeight()
                + chart.getStyler().getLegendPadding();
      }
    }

    return new Rectangle2D.Double(0, 0, weight, height);
  }

  private void paintPiecewise(
      Graphics2D g,
      double startx,
      double starty,
      Color[] rangeColors,
      HeatMapSeries heatMapSeries) {

    int splitNumber = chart.getStyler().getSplitNumber();
    TextLayout textLayout = null;
    Rectangle2D boxRect = null;
    String text = "";
    double min = heatMapSeries.getMin();
    double max = heatMapSeries.getMax();
    double step = (max - min) / splitNumber;
    double y = 0;
    AffineTransform orig = g.getTransform();
    AffineTransform at = null;
    Color splitColor = null;
    int beginColorIndex = 0;
    int endColorIndex = 1;
    Color beginColor = null;
    Color endColor = null;
    int red = 0;
    int green = 0;
    int blue = 0;
    double index = 0;
    for (int i = 0; i < splitNumber; i++) {
      index = (double) i / splitNumber * rangeColors.length;
      if (i == 0) {
        text = df.format(min) + SPLIT + df.format(min + step);
        splitColor = rangeColors[0];
      } else if (i == splitNumber - 1) {
        text = df.format(min + step * i) + SPLIT + df.format(max);
        splitColor = rangeColors[rangeColors.length - 1];
      } else {
        text = df.format(min + step * i) + SPLIT + df.format(min + step * (i + 1));
        beginColorIndex = (int) index;
        if (rangeColors.length != 1) {
          endColorIndex = beginColorIndex + 1;
        } else {
          endColorIndex = beginColorIndex;
        }

        beginColor = rangeColors[beginColorIndex];
        endColor = rangeColors[endColorIndex];
        red =
            (int)
                (beginColor.getRed()
                    + (index - (int) index) * (endColor.getRed() - beginColor.getRed()));
        green =
            (int)
                (beginColor.getGreen()
                    + (index - (int) index) * (endColor.getGreen() - beginColor.getGreen()));
        blue =
            (int)
                (beginColor.getBlue()
                    + (index - (int) index) * (endColor.getBlue() - beginColor.getBlue()));
        splitColor = new Color(red, green, blue);
      }

      textLayout =
          new TextLayout(
              text, chart.getStyler().getLegendFont(), new FontRenderContext(null, true, false));

      if (chart.getStyler().getLegendLayout() == Styler.LegendLayout.Vertical) {
        y =
            starty
                + chart.getStyler().getLegendPadding() * (splitNumber - i - 1)
                + chart.getStyler().getLegendFont().getSize() * (splitNumber - i - 1);
      } else {
        if (i > 0) {
          startx += BOX_SIZE + chart.getStyler().getLegendPadding();
        }
        y = starty;
      }
      boxRect = new Rectangle2D.Double(startx, y, BOX_SIZE, textLayout.getBounds().getHeight());
      g.setColor(splitColor);
      g.fill(boxRect);

      at = new AffineTransform();
      at.translate(
          startx + BOX_SIZE + chart.getStyler().getLegendPadding(),
          y + textLayout.getBounds().getHeight());
      g.transform(at);
      g.setColor(chart.getStyler().getChartFontColor());
      g.setFont(chart.getStyler().getLegendFont());
      textLayout.draw(g, 0, 0);
      g.setTransform(orig);
      if (chart.getStyler().getLegendLayout() == Styler.LegendLayout.Horizontal) {
        startx += textLayout.getBounds().getWidth() + chart.getStyler().getLegendPadding();
      }
    }
  }

  private void paintGradient(
      Graphics2D g,
      double startx,
      double starty,
      Color[] rangeColors,
      HeatMapSeries heatMapSeries) {

    TextLayout textLayoutMin =
        new TextLayout(
            heatMapSeries.getMin() + "",
            chart.getStyler().getLegendFont(),
            new FontRenderContext(null, true, false));
    Point2D start = null;
    Point2D end = null;
    Rectangle2D rect = null;
    // paint gradient color Column
    if (chart.getStyler().getLegendLayout() == Styler.LegendLayout.Vertical) {
      start = new Point2D.Double(startx, starty + chart.getStyler().getGradientColorColumnHeight());
      end = new Point2D.Double(startx, starty + chart.getStyler().getLegendFont().getSize());
      rect =
          new Rectangle2D.Double(
              startx,
              starty + chart.getStyler().getLegendFont().getSize(),
              chart.getStyler().getGradientColorColumnWeight(),
              chart.getStyler().getGradientColorColumnHeight());
    } else {
      start =
          new Point2D.Double(
              startx + textLayoutMin.getBounds().getWidth(),
              starty
                  + chart.getStyler().getLegendFont().getSize()
                  + chart.getStyler().getLegendPadding());
      end =
          new Point2D.Double(
              startx
                  + textLayoutMin.getBounds().getWidth()
                  + chart.getStyler().getGradientColorColumnHeight(),
              starty
                  + chart.getStyler().getLegendFont().getSize()
                  + chart.getStyler().getLegendPadding());

      rect =
          new Rectangle2D.Double(
              startx + textLayoutMin.getBounds().getWidth(),
              starty
                  + chart.getStyler().getLegendFont().getSize()
                  + chart.getStyler().getLegendPadding(),
              chart.getStyler().getGradientColorColumnHeight(),
              chart.getStyler().getGradientColorColumnWeight());
    }

    float[] fractions = new float[rangeColors.length];
    for (int i = 0; i < rangeColors.length; i++) {
      if (i == 0) {
        fractions[i] = 0;
      } else if (i == rangeColors.length - 1) {
        fractions[i] = 1;
      } else {
        fractions[i] = (float) i / (rangeColors.length - 1);
      }
    }
    LinearGradientPaint lgp = new LinearGradientPaint(start, end, fractions, rangeColors);
    g.setPaint(lgp);
    g.fill(rect);

    TextLayout textLayoutMax =
        new TextLayout(
            heatMapSeries.getMax() + "",
            chart.getStyler().getLegendFont(),
            new FontRenderContext(null, true, false));

    double tx = 0;
    double ty = 0;
    // paint max
    if (chart.getStyler().getLegendLayout() == Styler.LegendLayout.Vertical) {
      tx =
          startx
              + chart.getStyler().getGradientColorColumnWeight()
              + chart.getStyler().getLegendPadding();
      ty = starty + textLayoutMax.getBounds().getHeight();
    } else {
      tx =
          startx
              + textLayoutMin.getBounds().getWidth()
              + chart.getStyler().getGradientColorColumnHeight();
      ty = starty + chart.getStyler().getLegendFont().getSize();
    }

    g.setColor(chart.getStyler().getChartFontColor());
    g.setFont(chart.getStyler().getLegendFont());
    textLayoutMax.draw(g, (float) tx, (float) ty);

    // paint min
    if (chart.getStyler().getLegendLayout() == Styler.LegendLayout.Vertical) {
      tx =
          startx
              + chart.getStyler().getGradientColorColumnWeight()
              + chart.getStyler().getLegendPadding();
      ty =
          starty
              + chart.getStyler().getGradientColorColumnHeight()
              + chart.getStyler().getLegendFont().getSize() * 2;
    } else {
      tx = startx;
      ty = starty + chart.getStyler().getLegendFont().getSize();
    }
    g.setColor(chart.getStyler().getChartFontColor());
    g.setFont(chart.getStyler().getLegendFont());
    textLayoutMin.draw(g, (float) tx, (float) ty);
  }
}
