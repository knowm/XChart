package org.knowm.xchart.style;

import java.awt.*;
import org.knowm.xchart.style.theme.Theme;

public class RadarStyler extends Styler {

  // radar chart
  private RadarRenderStyle radarRenderStyle;
  private boolean isCircular;
  private double startAngleInDegrees;

  // radii tick marks
  private boolean radiiTicksMarksVisible;
  private Color radiiTickMarksColor;
  private BasicStroke radiiTickMarksStroke;
  private int radiiTickMarksCount;

  // radii  labels
  private boolean isRadiiTitleVisible;
  private Font radiiTitleFont;
  private int radiiTitlePadding;

  // series style
  private int markerSize;
  private boolean isSeriesFilled = true;

  public RadarStyler() {

    setAllStyles();
  }

  @Override
  void setAllStyles() {

    super.setAllStyles();

    this.radarRenderStyle = RadarRenderStyle.Polygon;
    this.isCircular = theme.isCircular();
    this.startAngleInDegrees = theme.getStartAngleInDegrees();

    this.markerSize = theme.getMarkerSize();

    this.radiiTicksMarksVisible = theme.isAxisTicksMarksVisible();
    this.radiiTickMarksColor = theme.getPlotGridLinesColor();
    this.radiiTickMarksStroke = theme.getPlotGridLinesStroke();
    this.radiiTickMarksCount = 5;

    this.isRadiiTitleVisible = theme.isXAxisTitleVisible() || theme.isYAxisTitleVisible();
    this.radiiTitleFont = theme.getAxisTitleFont();
    this.radiiTitlePadding = theme.getAxisTitlePadding();
  }

  public boolean isCircular() {

    return isCircular;
  }

  /**
   * Sets whether or not the radar chart is forced to be circular. Otherwise it's shape is oval,
   * matching the containing plot.
   *
   * @param isCircular
   */
  public RadarStyler setCircular(boolean isCircular) {

    this.isCircular = isCircular;
    return this;
  }

  public double getStartAngleInDegrees() {

    return startAngleInDegrees;
  }

  /**
   * Sets the start angle in degrees. Zero degrees is straight up.
   *
   * @param startAngleInDegrees
   */
  public RadarStyler setStartAngleInDegrees(double startAngleInDegrees) {

    this.startAngleInDegrees = startAngleInDegrees;
    return this;
  }

  /**
   * Set the theme the styler should use
   *
   * @param theme
   */
  public RadarStyler setTheme(Theme theme) {

    this.theme = theme;
    setAllStyles();
    return this;
  }

  public int getMarkerSize() {

    return markerSize;
  }

  /**
   * Sets the size of the markers (in pixels)
   *
   * @param markerSize
   */
  public RadarStyler setMarkerSize(int markerSize) {

    this.markerSize = markerSize;
    return this;
  }

  public boolean isRadiiTicksMarksVisible() {

    return radiiTicksMarksVisible;
  }

  public void setRadiiTicksMarksVisible(boolean radiiTicksMarksVisible) {

    this.radiiTicksMarksVisible = radiiTicksMarksVisible;
  }

  public Color getRadiiTickMarksColor() {

    return radiiTickMarksColor;
  }

  public void setRadiiTickMarksColor(Color radiiTickMarksColor) {

    this.radiiTickMarksColor = radiiTickMarksColor;
  }

  public BasicStroke getRadiiTickMarksStroke() {

    return radiiTickMarksStroke;
  }

  public void setRadiiTickMarksStroke(BasicStroke radiiTickMarksStroke) {

    this.radiiTickMarksStroke = radiiTickMarksStroke;
  }

  public boolean isRadiiTitleVisible() {

    return isRadiiTitleVisible;
  }

  public void setRadiiTitleVisible(boolean radiiTitleVisible) {

    this.isRadiiTitleVisible = radiiTitleVisible;
  }

  public Font getRadiiTitleFont() {

    return radiiTitleFont;
  }

  public void setRadiiTitleFont(Font radiiTitleFont) {

    this.radiiTitleFont = radiiTitleFont;
  }

  public int getRadiiTitlePadding() {

    return radiiTitlePadding;
  }

  public void setRadiiTitlePadding(int radiiTitlePadding) {

    this.radiiTitlePadding = radiiTitlePadding;
  }

  public int getRadiiTickMarksCount() {

    return radiiTickMarksCount;
  }

  public void setRadiiTickMarksCount(int radiiTickMarksCount) {

    this.radiiTickMarksCount = radiiTickMarksCount;
  }

  public boolean isSeriesFilled() {

    return isSeriesFilled;
  }

  public void setSeriesFilled(boolean seriesFilled) {

    this.isSeriesFilled = seriesFilled;
  }

  public RadarRenderStyle getRadarRenderStyle() {

    return radarRenderStyle;
  }

  public void setRadarRenderStyle(RadarRenderStyle radarRenderStyle) {

    this.radarRenderStyle = radarRenderStyle;
  }

  public enum RadarRenderStyle {
    Polygon,
    Circle;
  }
}
