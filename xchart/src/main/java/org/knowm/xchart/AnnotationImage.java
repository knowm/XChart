package org.knowm.xchart;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import org.knowm.xchart.internal.chartpart.Annotation;
import org.knowm.xchart.internal.chartpart.Chart;

public class AnnotationImage extends Annotation {

  // style
  private Color fontColor;
  private Font textFont;

  // internal
  private final Image image;
  private int startx;
  private int starty;

  /**
   * Constructor
   *
   * @param image
   * @param x
   * @param y
   * @param isValueInScreenSpace
   */
  public AnnotationImage(Image image, double x, double y, boolean isValueInScreenSpace) {

    this.image = image;
    this.x = x;
    this.y = y;
    this.isValueInScreenSpace = isValueInScreenSpace;
  }

  public void init(Chart chart) {

    super.init(chart);
    // TODO move these to an annotation styler
    if (fontColor == null) {
      fontColor = chart.getStyler().getChartFontColor();
    }
    if (textFont == null) {
      textFont = chart.getStyler().getLegendFont();
    }
  }

  @Override
  public Rectangle2D getBounds() {

    // TODO implement this correctly
    return bounds;
  }

  protected void calculatePosition() {

    if (isValueInScreenSpace) {
      startx = (int) x;
      starty = (int) y;
    } else {
      startx = (int) (getXAxisSreenValue(x) + 0.5) - image.getWidth(null) / 2;
      starty = (int) (getYAxisSreenValue(y) + 0.5) - image.getHeight(null) / 2;
    }
  }

  @Override
  public void paint(Graphics2D g) {

    if (!isVisible) {
      return;
    }
    bounds = g.getClipBounds();

    calculatePosition();
    g.drawImage(image, startx, starty, null);
  }

  public void setFontColor(Color fontColor) {
    this.fontColor = fontColor;
  }

  public void setTextFont(Font textFont) {
    this.textFont = textFont;
  }
}
