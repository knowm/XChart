package org.knowm.xchart.internal.chartpart.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.chartpart.ChartPart;

public class ChartImage implements ChartPart {

  protected XChartPanel chartPanel;
  protected Chart chart;
  protected Rectangle bounds;

  // properties
  protected boolean visible = true;
  protected Color fontColor;
  protected Font textFont;
  // button position
  protected double xValue;
  protected double yValue;
  protected boolean valueInScreenCoordinate = false;
  protected Image image;

  // internal
  int startx;
  int starty;

  public ChartImage(Image image, double xValue, double yValue, boolean valueInScreenCoordinate) {

    this.image = image;
    this.xValue = xValue;
    this.yValue = yValue;
    this.valueInScreenCoordinate = valueInScreenCoordinate;
  }

  public void init(XChartPanel<XYChart> chartPanel) {

    this.chartPanel = chartPanel;
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

  protected void calculatePosition() {

    if (valueInScreenCoordinate) {
      startx = (int) xValue;
      starty = (int) yValue;
    } else {
      startx = (int) (chart.getScreenXFromChart(xValue) + 0.5) - image.getWidth(null) / 2;
      starty = (int) (chart.getScreenYFromChart(yValue) + 0.5) - image.getHeight(null) / 2;
    }
  }

  @Override
  public void paint(Graphics2D g) {

    if (!visible) {
      return;
    }
    bounds = g.getClipBounds();

    calculatePosition();
    g.drawImage(image, startx, starty, null);
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

  public Image getImage() {

    return image;
  }

  public void setImage(Image image) {

    this.image = image;
  }
}
