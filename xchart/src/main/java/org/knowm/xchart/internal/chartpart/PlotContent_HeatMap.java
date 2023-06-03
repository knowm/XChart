package org.knowm.xchart.internal.chartpart;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.util.List;
import org.knowm.xchart.HeatMapChart;
import org.knowm.xchart.HeatMapSeries;
import org.knowm.xchart.internal.Utils;
import org.knowm.xchart.style.HeatMapStyler;

/**
 * @author Mr14huashao
 */
public class PlotContent_HeatMap<ST extends HeatMapStyler, S extends HeatMapSeries>
    extends PlotContent_<ST, S> {

  private final ST heatMapStyler;
  private final DecimalFormat df = new DecimalFormat("");

  /**
   * Constructor
   *
   * @param chart
   */
  PlotContent_HeatMap(Chart<ST, S> chart) {

    super(chart);
    heatMapStyler = chart.getStyler();
  }

  @Override
  protected void doPaint(Graphics2D g) {

    // X-Axis
    double xTickSpace = heatMapStyler.getPlotContentSize() * getBounds().getWidth();
    double xLeftMargin = Utils.getTickStartOffset((int) getBounds().getWidth(), xTickSpace);

    // Y-Axis
    double yTickSpace = heatMapStyler.getPlotContentSize() * getBounds().getHeight();
    double yTopMargin = Utils.getTickStartOffset((int) getBounds().getHeight(), yTickSpace);

    if (heatMapStyler.getHeatMapValueDecimalPattern() != null) {
      df.applyPattern(heatMapStyler.getHeatMapValueDecimalPattern());
    }

    Rectangle2D plotContentBounds = getBounds();

    HeatMapSeries series = ((HeatMapChart) chart).getHeatMapSeries();
    if (!series.isEnabled()) {
      return;
    }

    int x = 0;
    int y = 0;
    Number value = 0.0;
    List<? extends Number[]> list = series.getHeatData();
    List<?> xData = series.getXData();
    List<?> yData = series.getYData();
    double plotContentBoundsWidth = plotContentBounds.getWidth();
    double plotContentBoundsHeight = plotContentBounds.getHeight();
    double rectWidth = (plotContentBoundsWidth - 2 * xLeftMargin) / xData.size();
    double rectHeight = (plotContentBoundsHeight - 2 * yTopMargin) / yData.size();
    double xOffset = 0.0;
    double yOffset = 0.0;
    Rectangle2D rect = null;
    Color heatMapValueColor = null;
    for (Number[] numbers : list) {
      if (numbers == null) {
        continue;
      }
      x = numbers[0].intValue();
      y = numbers[1].intValue();
      value = numbers[2].doubleValue();
      if (x >= xData.size() || y >= yData.size()) {
        continue;
      }
      xOffset = getBounds().getX() + xLeftMargin + rectWidth * x;
      yOffset = getBounds().getY() + yTopMargin + rectHeight * (yData.size() - 1 - y);
      rect = new Rectangle2D.Double(xOffset, yOffset, rectWidth, rectHeight);
      heatMapValueColor = getColor(series, value.doubleValue());
      g.setColor(heatMapValueColor);
      g.fill(rect);

      // draw rect border
      if (heatMapStyler.isDrawBorder()) {
        g.setColor(heatMapValueColor);
        g.setStroke(SOLID_STROKE);
        g.draw(rect);
      }

      // show heat data value
      if (heatMapStyler.isShowValue()) {
        showValue(g, rect, df.format(numbers[2]));
      }

      if (heatMapStyler.isToolTipsEnabled()) {
        toolTips.addData(
            rect,
            rect.getCenterX(),
            rect.getCenterY() + heatMapStyler.getToolTipFont().getSize(),
            0,
            series.getName()
                + ": "
                + chart.getXAxisFormat().format(xData.get(x))
                + ", "
                + chart.getYAxisFormat().format(yData.get(y))
                + ", "
                + df.format(numbers[2]));
      }
    }
  }

  private Color getColor(HeatMapSeries series, double value) {

    Color color = null;
    Color[] rangeColors = chart.getStyler().getRangeColors();
    double min = series.getMin();
    double max = series.getMax();
    if (value <= min) {
      color = rangeColors[0];
    } else if (value >= max) {
      color = rangeColors[rangeColors.length - 1];
    } else {
      double valueRation = (value - min) / (max - min);
      if (heatMapStyler.isPiecewise()) {
        color = getPiecewiseColor(rangeColors, valueRation);
      } else {
        color = getGradientColor(rangeColors, valueRation);
      }
    }
    return color;
  }

  private Color getPiecewiseColor(Color[] rangeColors, double valueRation) {

    Color color = null;
    int splitNumber = chart.getStyler().getSplitNumber();
    int splitNumberIndex = (int) (valueRation * splitNumber);
    for (int i = 0; i < splitNumber; i++) {
      if (splitNumberIndex == 0) {
        color = rangeColors[0];
        break;
      } else if (splitNumberIndex == splitNumber - 1) {
        color = rangeColors[rangeColors.length - 1];
        break;
      } else {
        double index = (double) i / splitNumber * rangeColors.length;
        int beginColorIndex = (int) index;
        int endColorIndex = 0;
        if (rangeColors.length != 1) {
          endColorIndex = beginColorIndex + 1;
        } else {
          endColorIndex = beginColorIndex;
        }

        if (splitNumberIndex == i) {
          Color beginColor = rangeColors[beginColorIndex];
          Color endColor = rangeColors[endColorIndex];
          int red =
              (int)
                  (beginColor.getRed()
                      + (index - (int) index) * (endColor.getRed() - beginColor.getRed()));
          int green =
              (int)
                  (beginColor.getGreen()
                      + (index - (int) index) * (endColor.getGreen() - beginColor.getGreen()));
          int blue =
              (int)
                  (beginColor.getBlue()
                      + (index - (int) index) * (endColor.getBlue() - beginColor.getBlue()));
          color = new Color(red, green, blue);
          break;
        }
      }
    }
    return color;
  }

  private Color getGradientColor(Color[] rangeColors, double valueRation) {

    double index = valueRation * (rangeColors.length - 1);
    Color color = null;
    int beginColorIndex = (int) index;
    int endColorIndex = 0;

    if (rangeColors.length != 1) {
      endColorIndex = beginColorIndex + 1;
    } else {
      endColorIndex = beginColorIndex;
    }

    Color beginColor = rangeColors[beginColorIndex];
    Color endColor = rangeColors[endColorIndex];

    if ((int) index < rangeColors.length - 1) {
      int red =
          (int)
              (beginColor.getRed()
                  + (index - (int) index) * (endColor.getRed() - beginColor.getRed()));
      int green =
          (int)
              (beginColor.getGreen()
                  + (index - (int) index) * (endColor.getGreen() - beginColor.getGreen()));
      int blue =
          (int)
              (beginColor.getBlue()
                  + (index - (int) index) * (endColor.getBlue() - beginColor.getBlue()));
      color = new Color(red, green, blue);
    } else {
      color = endColor;
    }

    return color;
  }

  private void showValue(Graphics2D g, Rectangle2D rect, String value) {

    double rectCenterX = rect.getCenterX();
    double rectCenterY = rect.getCenterY();

    TextLayout textLayout =
        new TextLayout(
            value, heatMapStyler.getValueFont(), new FontRenderContext(null, true, false));
    Rectangle2D annotationRectangle = textLayout.getBounds();
    g.setColor(heatMapStyler.getValueFontColor());
    AffineTransform orig = g.getTransform();
    AffineTransform at = new AffineTransform();
    at.translate(
        rectCenterX - annotationRectangle.getWidth() / 2,
        rectCenterY + annotationRectangle.getHeight() / 2);
    g.transform(at);
    g.fill(textLayout.getOutline(null));
    g.setTransform(orig);
  }
}
