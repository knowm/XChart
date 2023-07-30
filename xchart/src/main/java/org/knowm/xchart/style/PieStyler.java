package org.knowm.xchart.style;

import java.awt.Color;
import java.awt.Font;
import org.knowm.xchart.PieSeries.PieSeriesRenderStyle;
import org.knowm.xchart.style.colors.FontColorDetector;
import org.knowm.xchart.style.theme.Theme;

public class PieStyler extends Styler {

  private PieSeriesRenderStyle chartPieSeriesRenderStyle;
  private boolean isCircular;
  private double startAngleInDegrees;

  private double donutThickness;
  private boolean isSumVisible;
  private Font sumFont;
  private String sumFormat;
  private ClockwiseDirectionType clockwiseDirectionType = ClockwiseDirectionType.COUNTER_CLOCKWISE;
  private float sliceBorderWidth = 0;

  // labels //////////////////////
  private boolean isLabelsVisible;
  private Font labelsFont;
  private Color labelsFontColor;
  private double labelsDistance;
  private LabelType labelType;
  private boolean isForceAllLabelsVisible;
  private boolean isLabelsFontColorAutomaticEnabled;
  private Color labelsFontColorAutomaticLight;
  private Color labelsFontColorAutomaticDark;

  public PieStyler() {

    setAllStyles();
  }

  @Override
  void setAllStyles() {

    super.setAllStyles();

    this.chartPieSeriesRenderStyle = PieSeriesRenderStyle.Pie;
    this.isCircular = theme.isCircular();
    this.startAngleInDegrees = theme.getStartAngleInDegrees();

    this.donutThickness = theme.getDonutThickness();

    this.isSumVisible = theme.isSumVisible();
    this.sumFont = theme.getSumFont();

    this.isLabelsVisible = true; // default to true
    this.labelsFont = theme.getBaseFont();
    this.labelsFontColor = theme.getChartFontColor();
    this.labelsDistance = theme.getLabelsDistance();
    this.labelType = theme.getLabelType();
    this.isForceAllLabelsVisible = theme.setForceAllLabelsVisible();
    isLabelsFontColorAutomaticEnabled = theme.isLabelsFontColorAutomaticEnabled();
    labelsFontColorAutomaticLight = theme.getLabelsFontColorAutomaticLight();
    labelsFontColorAutomaticDark = theme.getLabelsFontColorAutomaticDark();
  }

  public PieSeriesRenderStyle getDefaultSeriesRenderStyle() {

    return chartPieSeriesRenderStyle;
  }

  /**
   * Sets the default series render style for the chart (line, scatter, area, etc.) You can override
   * the series render style individually on each Series object.
   *
   * @param chartPieSeriesRenderStyle
   */
  public PieStyler setDefaultSeriesRenderStyle(PieSeriesRenderStyle chartPieSeriesRenderStyle) {

    this.chartPieSeriesRenderStyle = chartPieSeriesRenderStyle;
    return this;
  }

  public boolean isCircular() {

    return isCircular;
  }

  /**
   * Sets whether or not the pie chart is forced to be circular. Otherwise it's shape is oval,
   * matching the containing plot.
   *
   * @param isCircular
   */
  public PieStyler setCircular(boolean isCircular) {

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
  public PieStyler setStartAngleInDegrees(double startAngleInDegrees) {

    this.startAngleInDegrees = startAngleInDegrees;
    return this;
  }

  public double getLabelsDistance() {

    return labelsDistance;
  }

  /**
   * Sets the distance of the pie chart's annotation where 0 is the center, 1 is at the edge and
   * greater than 1 is outside of the pie chart.
   *
   * @param labelsDistance
   */
  public void setLabelsDistance(double labelsDistance) {

    this.labelsDistance = labelsDistance;
  }

  public LabelType getLabelType() {

    return labelType;
  }

  /**
   * Sets the Pie chart's annotation type
   *
   * @param labelType
   */
  public PieStyler setLabelType(LabelType labelType) {

    this.labelType = labelType;
    return this;
  }

  public boolean isForceAllLabelsVisible() {

    return isForceAllLabelsVisible;
  }

  /**
   * By default, only the labels that will "fit", as determined algorithmically, will be drawn.
   * Otherwise, you can end up with annotations drawn overlapping. If `drawAllAnnotations` is set
   * true with this method, it will override the algorithmic determination, and always draw all the
   * annotations, one for each slice. You can also try playing around with the method
   * `setStartAngleInDegrees` so the the slices are orientated in a more optimal way. You can also
   * try changing the font size. Also, you can order the slices so that a small slice is followed by
   * a larger slice, while setting this method with `true`.
   *
   * @param forceAllLabelsVisible
   */
  public PieStyler setForceAllLabelsVisible(boolean forceAllLabelsVisible) {

    this.isForceAllLabelsVisible = forceAllLabelsVisible;
    return this;
  }

  public double getDonutThickness() {

    return donutThickness;
  }

  /**
   * Sets the thickness of the donut ring for donut style pie chart series.
   *
   * @param donutThickness - Valid range is between 0 and 1.
   */
  public PieStyler setDonutThickness(double donutThickness) {

    this.donutThickness = donutThickness;
    return this;
  }

  public boolean isSumVisible() {

    return isSumVisible;
  }

  /**
   * Set the Format to be applied to the sum, the default is just to display the sum as a number
   * using the PieStyler DecimalFormat. This allows a separate Formatter @see
   * java.util.Formatter#format()
   *
   * @param sumFormat Format to use for the sum display, the Double sum value will be passed to this
   *     to generate the overall sum string.
   * @return PieStyler so that modifiers can be chained.
   */
  public PieStyler setSumFormat(String sumFormat) {
    this.sumFormat = sumFormat;
    return this;
  }

  /**
   * Access the current sumFormat value, a value of "" or null implies use the original sum
   * formatted using the PieStyler DecimalFormat.
   *
   * @return Formatter string to be used when displaying the sum value or <code>null</code>
   */
  public String getSumFormat() {
    return sumFormat;
  }

  /**
   * Sets whether or not the sum is visible in the centre of the pie chart.
   *
   * @param isSumVisible
   */
  public PieStyler setSumVisible(boolean isSumVisible) {

    this.isSumVisible = isSumVisible;
    return this;
  }

  public Font getSumFont() {

    return sumFont;
  }

  /**
   * Sets the font for the sum.
   *
   * @param sumFont font
   */
  public PieStyler setSumFont(Font sumFont) {

    this.sumFont = sumFont;
    return this;
  }

  /**
   * Sets the font size for the sum.
   *
   * @param sumFontSize
   */
  public PieStyler setSumFontSize(float sumFontSize) {

    this.sumFont = this.sumFont.deriveFont(sumFontSize);
    return this;
  }

  public boolean isLabelsVisible() {

    return isLabelsVisible;
  }

  /**
   * Sets if annotations should be added to charts. Each chart type has a different annotation type
   *
   * @param labelsVisible
   */
  public PieStyler setLabelsVisible(boolean labelsVisible) {

    this.isLabelsVisible = labelsVisible;
    return this;
  }

  public Font getLabelsFont() {

    return labelsFont;
  }

  /**
   * Sets the Font used for chart annotations
   *
   * @param labelsFont
   */
  public PieStyler setLabelsFont(Font labelsFont) {

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
   * Sets the color of the Font used for chart annotations
   *
   * @param labelsFontColor
   */
  public PieStyler setLabelsFontColor(Color labelsFontColor) {
    this.labelsFontColor = labelsFontColor;
    return this;
  }

  public boolean isLabelsFontColorAutomaticEnabled() {
    return isLabelsFontColorAutomaticEnabled;
  }

  public PieStyler setLabelsFontColorAutomaticEnabled(boolean isLabelsFontColorAutomaticEnabled) {
    this.isLabelsFontColorAutomaticEnabled = isLabelsFontColorAutomaticEnabled;
    return this;
  }

  public Color getLabelsFontColorAutomaticLight() {
    return labelsFontColorAutomaticLight;
  }

  public PieStyler setLabelsFontColorAutomaticLight(Color labelsFontColorAutomaticLight) {
    this.labelsFontColorAutomaticLight = labelsFontColorAutomaticLight;
    return this;
  }

  public Color getLabelsFontColorAutomaticDark() {
    return labelsFontColorAutomaticDark;
  }

  public PieStyler setLabelsFontColorAutomaticDark(Color labelsFontColorAutomaticDark) {
    this.labelsFontColorAutomaticDark = labelsFontColorAutomaticDark;
    return this;
  }
  /**
   * Set the theme the styler should use
   *
   * @param theme
   */
  public PieStyler setTheme(Theme theme) {

    this.theme = theme;
    setAllStyles();
    return this;
  }

  public ClockwiseDirectionType getClockwiseDirectionType() {
    return clockwiseDirectionType;
  }

  public PieStyler setClockwiseDirectionType(ClockwiseDirectionType clockwiseDirectionType) {
    this.clockwiseDirectionType = clockwiseDirectionType;
    return this;
  }

  // used to add border width
  public PieStyler setSliceBorderWidth(double sliceBorderWidth) {
    this.sliceBorderWidth = (float) sliceBorderWidth;
    return this;
  }

  public float getSliceBorderWidth() {
    return sliceBorderWidth;
  }

  public enum LabelType {
    Value,
    Percentage,
    Name,
    NameAndPercentage,
    NameAndValue
  }

  public enum ClockwiseDirectionType {
    CLOCKWISE,
    COUNTER_CLOCKWISE
  }
}
