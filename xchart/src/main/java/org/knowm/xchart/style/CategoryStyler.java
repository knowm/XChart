package org.knowm.xchart.style;

import java.awt.Color;
import java.awt.Font;
import org.knowm.xchart.CategorySeries.CategorySeriesRenderStyle;
import org.knowm.xchart.style.colors.FontColorDetector;
import org.knowm.xchart.style.theme.Theme;

/** @author timmolter */
public class CategoryStyler extends AxesChartStyler {

  private CategorySeriesRenderStyle chartCategorySeriesRenderStyle;

  private double availableSpaceFill;
  private boolean isOverlapped;
  private boolean isStacked;

  // labels //////////////////////
  private boolean isLabelsVisible = false;
  private boolean showStackSum = false;
  private Font labelsFont;
  private Color labelsFontColor;
  private int labelsRotation;
  private double labelsPosition;
  private boolean isLabelsFontColorAutomaticEnabled;
  private Color labelsFontColorAutomaticLight;
  private Color labelsFontColorAutomaticDark;

  /** Constructor */
  public CategoryStyler() {

    setAllStyles();
  }

  @Override
  protected void setAllStyles() {

    super.setAllStyles();
    this.chartCategorySeriesRenderStyle = CategorySeriesRenderStyle.Bar; // set default to bar

    availableSpaceFill = theme.getAvailableSpaceFill();
    isOverlapped = theme.isOverlapped();
    isStacked = false;
    isLabelsVisible = false;
    labelsFont = theme.getBaseFont();
    labelsFontColor = theme.getChartFontColor();
    labelsRotation = 0;
    labelsPosition = 0.5;
    isLabelsFontColorAutomaticEnabled = theme.isLabelsFontColorAutomaticEnabled();
    labelsFontColorAutomaticLight = theme.getLabelsFontColorAutomaticLight();
    labelsFontColorAutomaticDark = theme.getLabelsFontColorAutomaticDark();
  }

  public CategorySeriesRenderStyle getDefaultSeriesRenderStyle() {

    return chartCategorySeriesRenderStyle;
  }

  /**
   * Sets the default series render style for the chart (bar, stick, line, scatter, area, etc.) You
   * can override the series render style individually on each Series object.
   *
   * @param chartCategorySeriesRenderStyle
   */
  public CategoryStyler setDefaultSeriesRenderStyle(
      CategorySeriesRenderStyle chartCategorySeriesRenderStyle) {

    this.chartCategorySeriesRenderStyle = chartCategorySeriesRenderStyle;
    return this;
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
  public CategoryStyler setAvailableSpaceFill(double availableSpaceFill) {

    this.availableSpaceFill = availableSpaceFill;
    return this;
  }

  public boolean isOverlapped() {

    return isOverlapped;
  }

  /**
   * set whether or not series renderings (i.e. bars, stick, etc.) are overlapped. Otherwise they
   * are placed side-by-side.
   *
   * @param isOverlapped
   */
  public CategoryStyler setOverlapped(boolean isOverlapped) {

    this.isOverlapped = isOverlapped;
    return this;
  }

  public boolean isStacked() {

    return isStacked;
  }

  /**
   * Set whether or not series renderings (i.e. bars, stick, etc.) are stacked.
   *
   * @param isStacked
   */
  public CategoryStyler setStacked(boolean isStacked) {

    this.isStacked = isStacked;
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
  public CategoryStyler setLabelsVisible(boolean labelsVisible) {

    this.isLabelsVisible = labelsVisible;
    return this;
  }

  public boolean isShowStackSum() {

    return showStackSum;
  }

  /**
   * If the category chart is set to be "stacked", the total value of the stack can be painted above
   * the stack.
   *
   * @param showStackSum
   * @return
   */
  public CategoryStyler setShowStackSum(boolean showStackSum) {

    this.showStackSum = showStackSum;
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
  public CategoryStyler setLabelsFont(Font labelsFont) {

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
  public CategoryStyler setLabelsFontColor(Color labelsFontColor) {
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
  public CategoryStyler setLabelsRotation(int labelsRotation) {
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
  public CategoryStyler setLabelsPosition(double labelsPosition) {

    if (labelsPosition < 0 || labelsPosition > 1) {
      throw new IllegalArgumentException("Annotations position must between 0 and 1!!!");
    }
    this.labelsPosition = labelsPosition;
    return this;
  }

  public boolean isLabelsFontColorAutomaticEnabled() {
    return isLabelsFontColorAutomaticEnabled;
  }

  public CategoryStyler setLabelsFontColorAutomaticEnabled(
      boolean isLabelsFontColorAutomaticEnabled) {
    this.isLabelsFontColorAutomaticEnabled = isLabelsFontColorAutomaticEnabled;
    return this;
  }

  public Color getLabelsFontColorAutomaticLight() {
    return labelsFontColorAutomaticLight;
  }

  public CategoryStyler setLabelsFontColorAutomaticLight(Color labelsFontColorAutomaticLight) {
    this.labelsFontColorAutomaticLight = labelsFontColorAutomaticLight;
    return this;
  }

  public Color getLabelsFontColorAutomaticDark() {
    return labelsFontColorAutomaticDark;
  }

  public CategoryStyler setLabelsFontColorAutomaticDark(Color labelsFontColorAutomaticDark) {
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
