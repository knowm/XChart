package org.knowm.xchart.internal.chartpart;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * All components of a chart that need to be painted should implement this interface
 *
 * @author timmolter
 */
interface ChartPart {

  BasicStroke SOLID_STROKE =
      new BasicStroke(
          1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10.0f, new float[] {3.0f, 0.0f}, 0.0f);

  Rectangle2D getBounds();

  void paint(final Graphics2D g);
}
