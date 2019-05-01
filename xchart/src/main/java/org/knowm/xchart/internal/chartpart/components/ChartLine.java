package org.knowm.xchart.internal.chartpart.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.chartpart.ChartPart;

public class ChartLine implements ChartPart {

  protected XChartPanel chartPanel;
  protected Chart chart;
  protected Rectangle bounds;

  // properties
  protected boolean visible = true;
  protected Color color = new Color(114, 147, 203);
  protected BasicStroke stroke = SOLID_STROKE;

  protected double value;
  protected boolean vertical;
  protected boolean valueInScreenCoordinate = false;

  public ChartLine(double value, boolean vertical, boolean valueInScreenCoordinate) {

    super();
    this.value = value;
    this.vertical = vertical;
    this.valueInScreenCoordinate = valueInScreenCoordinate;
  }

  public void init(XChartPanel<XYChart> chartPanel) {

    this.chartPanel = chartPanel;
    chart = chartPanel.getChart();
    chart.addPlotPart(this);
  }

  @Override
  public Rectangle2D getBounds() {

    return bounds;
  }

  @Override
  public void paint(Graphics2D g) {

    if (!visible) {
      return;
    }

    bounds = g.getClipBounds();
    int x1 = 0, x2 = 0, y1 = 0, y2 = 0;

    if (vertical) {
      y1 = (int) bounds.getY();
      y2 = (int) (bounds.getY() + bounds.getHeight());
    } else {
      x1 = (int) bounds.getX();
      x2 = (int) (bounds.getX() + bounds.getWidth());
    }

    if (valueInScreenCoordinate) {
      if (vertical) {
        x1 = (int) value;
        x2 = x1;
      } else {
        y1 = (int) value;
        y2 = y1;
      }
    } else {
      if (vertical) {
        x1 = (int) chart.getScreenXFromChart(value);
        x2 = x1;
      } else {
        y1 = (int) chart.getScreenYFromChart(value);
        y2 = y1;
      }
    }

    
    g.setStroke(stroke);
    g.setColor(color);
    g.drawLine(x1, y1, x2, y2);
  }

  public Color getColor() {

    return color;
  }

  public void setColor(Color color) {

    this.color = color;
  }

  public boolean isVisible() {

    return visible;
  }

  public void setVisible(boolean visible) {

    this.visible = visible;
  }

  public BasicStroke getStroke() {
  
    return stroke;
  }

  public void setStroke(BasicStroke stroke) {
  
    this.stroke = stroke;
  }
  
}
