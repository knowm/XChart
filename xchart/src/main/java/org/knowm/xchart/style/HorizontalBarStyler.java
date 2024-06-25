package org.knowm.xchart.style;

import java.awt.*;
import org.knowm.xchart.style.colors.FontColorDetector;
import org.knowm.xchart.style.theme.Theme;

public class HorizontalBarStyler extends AxesChartStyler {

  private double availableSpaceFill;

  // labels //////////////////////
  private boolean isLabelsVisible = false;
  private Font labelsFont;
  private Color labelsFontColor;
  private int labelsRotation;
  private double labelsPosition;
  private boolean isLabelsFontColorAutomaticEnabled;
  private Color labelsFontColorAutomaticLight;
  private Color labelsFontColorAutomaticDark;

  /** Constructor */
  public HorizontalBarStyler() {

    setAllStyles();
  }

  @Override
  protected void setAllStyles() {

    super.setAllStyles();

    availableSpaceFill = theme.getAvailableSpaceFill();
    isLabelsVisible = false;
    labelsFont = theme.getBaseFont();
    labelsFontColor = theme.getChartFontColor();
    labelsRotation = 0;
    labelsPosition = 0.5;
    isLabelsFontColorAutomaticEnabled = theme.isLabelsFontColorAutomaticEnabled();
    labelsFontColorAutomaticLight = theme.getLabelsFontColorAutomaticLight();
    labelsFontColorAutomaticDark = theme.getLabelsFontColorAutomaticDark();
  }

  public double getAvailableSpaceFill() {

    return availableSpaceFill;
  }

  /**
   * Sets the available space for rendering each category as a percentage. For a bar chart with one
   * series, it will be the width of the bar as a percentage of the maximum space alloted for the
   * bar. If there are three series and three bars, the three bars will share the available space.
   * This affects all category series render types, not only bar charts. Full width is 100%, i.e.
   * 1.0
   *
   * @param availableSpaceFill
   */
  public HorizontalBarStyler setAvailableSpaceFill(double availableSpaceFill) {

    this.availableSpaceFill = availableSpaceFill;
    return this;
  }

  public boolean isLabelsVisible() {

    return isLabelsVisible;
  }

  /**
   * Sets if labels should be added to charts. Each chart type has a different annotation type
   *
   * @param labelsVisible
   */
  public HorizontalBarStyler setLabelsVisible(boolean labelsVisible) {

    this.isLabelsVisible = labelsVisible;
    return this;
  }

  public Font getLabelsFont() {

    return labelsFont;
  }

  /**
   * Sets the Font used for chart labels
   *
   * @param labelsFont
   */
  public HorizontalBarStyler setLabelsFont(Font labelsFont) {

    this.labelsFont = labelsFont;
    return this;
  }

  public Color getLabelsFontColor() {
    return labelsFontColor;
  }

  public Color getLabelsFontColor(Color backgroundColor) {

    return FontColorDetector.getAutomaticFontColor(
        backgroundColor, labelsFontColorAutomaticDark, labelsFontColorAutomaticLight);
  }

  /**
   * Sets the color of the Font used for chart labels
   *
   * @param labelsFontColor
   */
  public HorizontalBarStyler setLabelsFontColor(Color labelsFontColor) {
    this.labelsFontColor = labelsFontColor;
    return this;
  }

  public int getLabelsRotation() {
    return labelsRotation;
  }

  /**
   * Sets the rotation (in degrees) for chart labels.
   *
   * @param labelsRotation
   */
  public HorizontalBarStyler setLabelsRotation(int labelsRotation) {
    this.labelsRotation = labelsRotation;
    return this;
  }

  public double getLabelsPosition() {

    return labelsPosition;
  }

  /**
   * A number between 0 and 1 setting the vertical position of the data label. Default is 0.5
   * placing it in the center.
   *
   * @param labelsPosition
   * @return
   */
  public HorizontalBarStyler setLabelsPosition(double labelsPosition) {

    if (labelsPosition < 0 || labelsPosition > 1) {
      throw new IllegalArgumentException("Annotations position must between 0 and 1!!!");
    }
    this.labelsPosition = labelsPosition;
    return this;
  }

  public boolean isLabelsFontColorAutomaticEnabled() {
    return isLabelsFontColorAutomaticEnabled;
  }

  public HorizontalBarStyler setLabelsFontColorAutomaticEnabled(
      boolean isLabelsFontColorAutomaticEnabled) {
    this.isLabelsFontColorAutomaticEnabled = isLabelsFontColorAutomaticEnabled;
    return this;
  }

  public Color getLabelsFontColorAutomaticLight() {
    return labelsFontColorAutomaticLight;
  }

  public HorizontalBarStyler setLabelsFontColorAutomaticLight(Color labelsFontColorAutomaticLight) {
    this.labelsFontColorAutomaticLight = labelsFontColorAutomaticLight;
    return this;
  }

  public Color getLabelsFontColorAutomaticDark() {
    return labelsFontColorAutomaticDark;
  }

  public HorizontalBarStyler setLabelsFontColorAutomaticDark(Color labelsFontColorAutomaticDark) {
    this.labelsFontColorAutomaticDark = labelsFontColorAutomaticDark;
    return this;
  }

  /**
   * Set the theme the styler should use
   *
   * @param theme
   */
  public void setTheme(Theme theme) {

    this.theme = theme;
    setAllStyles();
  }
}
