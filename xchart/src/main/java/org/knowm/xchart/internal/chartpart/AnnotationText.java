package org.knowm.xchart.internal.chartpart;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;

//TODO don't have this be a ChartPart??
public class AnnotationText implements ChartPart {

  protected XChartPanel xChartPanel;
  protected Chart chart;
  protected Rectangle bounds;

  // properties
  protected String text;
  protected boolean visible = true;
  protected Color fontColor;
  protected Font textFont;
  // button position
  protected double xValue;
  protected double yValue;
  protected boolean valueInScreenCoordinate = false;

  // internal
  double startx;
  double starty;

  public AnnotationText(String text, double xValue, double yValue, boolean valueInScreenCoordinate) {

    this.text = text;
    this.xValue = xValue;
    this.yValue = yValue;
    this.valueInScreenCoordinate = valueInScreenCoordinate;
  }

  public void init(XChartPanel<XYChart> chartPanel) {

    this.xChartPanel = chartPanel;
    chart = chartPanel.getChart();
    if (fontColor == null) {
      fontColor = chart.getStyler().getChartFontColor();
    }
    if (textFont == null) {
      textFont = chart.getStyler().getLegendFont();
    }
    chart.addPlotPart(this);
  }

  @Override
  public Rectangle2D getBounds() {

    return bounds;
  }

  protected void calculatePosition(Rectangle2D textBounds) {

    if (valueInScreenCoordinate) {
      startx = xValue;
      starty = yValue;
    } else {
      startx = chart.axisPair.getXAxis().getScreenValue(xValue) - textBounds.getWidth() / 2;
      starty = chart.axisPair.getYAxis().getScreenValue(yValue) + textBounds.getHeight() / 2;
    }
  }

  @Override
  public void paint(Graphics2D g) {

    if (!visible) {
      return;
    }
    bounds = g.getClipBounds();

    Object oldHint = g.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    g.setColor(fontColor);
    g.setFont(textFont);

    FontRenderContext frc = g.getFontRenderContext();
    TextLayout tl = new TextLayout(text, textFont, frc);
    Shape shape = tl.getOutline(null);

    Rectangle2D textBounds = shape.getBounds2D();
    calculatePosition(textBounds);

    AffineTransform orig = g.getTransform();
    AffineTransform at = new AffineTransform();
    at.translate(startx, starty);
    g.transform(at);
    g.fill(shape);
    g.setTransform(orig);

    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldHint);
  }

  public String getText() {

    return text;
  }

  public void setText(String text) {

    this.text = text;
  }

  public boolean isVisible() {

    return visible;
  }

  public void setVisible(boolean visible) {

    this.visible = visible;
  }

  public Color getFontColor() {

    return fontColor;
  }

  public void setFontColor(Color fontColor) {

    this.fontColor = fontColor;
  }

  public Font getTextFont() {

    return textFont;
  }

  public void setTextFont(Font textFont) {

    this.textFont = textFont;
  }

  public double getxValue() {

    return xValue;
  }

  public void setxValue(double xValue) {

    this.xValue = xValue;
  }

  public double getyValue() {

    return yValue;
  }

  public void setyValue(double yValue) {

    this.yValue = yValue;
  }

  public boolean isValueInScreenCoordinate() {

    return valueInScreenCoordinate;
  }

  public void setValueInScreenCoordinate(boolean valueInScreenCoordinate) {

    this.valueInScreenCoordinate = valueInScreenCoordinate;
  }
}
