package org.knowm.xchart.internal.chartpart;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;

public class AnnotationImage extends Annotation {


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

  public AnnotationImage(
      Image image, double xValue, double yValue, boolean valueInScreenCoordinate) {

    this.image = image;
    this.xValue = xValue;
    this.yValue = yValue;
    this.valueInScreenCoordinate = valueInScreenCoordinate;
  }

  public void init(Chart chart) {

    super.init(chart);
    if (fontColor == null) {
      fontColor = chart.getStyler().getChartFontColor();
    }
    if (textFont == null) {
      textFont = chart.getStyler().getLegendFont();
    }
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
      startx = (int) (chart.getXAxis().getScreenValue(xValue) + 0.5) - image.getWidth(null) / 2;
      starty = (int) (chart.getYAxis().getScreenValue(yValue) + 0.5) - image.getHeight(null) / 2;
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
