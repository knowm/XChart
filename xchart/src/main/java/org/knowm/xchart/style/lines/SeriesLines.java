package org.knowm.xchart.style.lines;

import java.awt.*;

/**
 * Pre-defined Line Styles used for Series Lines
 *
 * @author timmolter
 */
public interface SeriesLines {

  BasicStroke NONE = new NoneStroke();
  BasicStroke SOLID = new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
  BasicStroke DASH_DOT =
      new BasicStroke(
          2.0f,
          BasicStroke.CAP_BUTT,
          BasicStroke.JOIN_MITER,
          10.0f,
          new float[] {3.0f, 1.0f},
          0.0f);
  BasicStroke DASH_DASH =
      new BasicStroke(
          2.0f,
          BasicStroke.CAP_BUTT,
          BasicStroke.JOIN_MITER,
          10.0f,
          new float[] {3.0f, 3.0f},
          0.0f);
  BasicStroke DOT_DOT =
      new BasicStroke(
          2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10.0f, new float[] {2.0f}, 0.0f);

  BasicStroke[] getSeriesLines();
}
