package org.knowm.xchart.style.colors;

import java.awt.*;

/**
 * Pre-defined Colors used for various Chart Elements
 *
 * @author timmolter
 */
public enum ChartColor {

  /** BLACK */
  BLACK(new Color(0, 0, 0)),

  /** DARK_GREY */
  DARK_GREY(new Color(130, 130, 130)),

  /** GREY */
  GREY(new Color(210, 210, 210)),

  /** LIGHT_GREY */
  LIGHT_GREY(new Color(230, 230, 230)),

  /** WHITE */
  WHITE(new Color(255, 255, 255));

  final Color color;

  /**
   * Constructor
   *
   * @param color
   */
  ChartColor(Color color) {

    this.color = color;
  }

  /**
   * Get a AWT Color Object
   *
   * @param chartColor
   * @return a AWT Color Object
   */
  public static Color getAWTColor(ChartColor chartColor) {

    return chartColor.color;
  }
}
