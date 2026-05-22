package org.knowm.xchart.internal.chartpart;

import java.awt.Graphics2D;

/**
 * Minimal rendering contract for all XChart chart types. Use this type (instead of the generic
 * {@code Chart<?,?>}) whenever code only needs to paint a chart or query its dimensions/title —
 * e.g. bitmap/vector/PDF encoders.
 */
public interface IChart {

  void paint(Graphics2D g, int width, int height);

  int getWidth();

  int getHeight();

  String getTitle();
}
