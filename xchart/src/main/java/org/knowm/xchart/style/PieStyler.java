package org.knowm.xchart.style;

import java.awt.*;
import org.knowm.xchart.PieSeries.PieSeriesRenderStyle;

/** @author timmolter */
public class PieStyler extends Styler {

  private PieSeriesRenderStyle chartPieSeriesRenderStyle;
  private boolean isCircular;
  private double startAngleInDegrees;
  private double annotationDistance;
  private AnnotationType annotationType;
  private boolean drawAllAnnotations;
  private double donutThickness;
  private boolean isSumVisible;
  private Font sumFont;
  private String sumFormat;
  private ClockwiseDirectionType clockwiseDirectionType = ClockwiseDirectionType.COUNTER_CLOCKWISE;
  private float borderWidth = 0;

  public PieStyler() {

    setAllStyles();
  }

  @Override
  void setAllStyles() {

    super.setAllStyles();

    this.chartPieSeriesRenderStyle = PieSeriesRenderStyle.Pie;
    this.isCircular = theme.isCircular();
    this.startAngleInDegrees = theme.getStartAngleInDegrees();
    this.annotationDistance = theme.getAnnotationDistance();
    this.annotationType = theme.getAnnotationType();
    this.drawAllAnnotations = theme.isDrawAllAnnotations();
    this.donutThickness = theme.getDonutThickness();

    // Annotations ////////////////////////////////
    this.hasAnnotations = true;

    this.isSumVisible = theme.isSumVisible();
    this.sumFont = theme.getSumFont();
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

  public double getAnnotationDistance() {

    return annotationDistance;
  }

  /**
   * Sets the distance of the pie chart's annotation where 0 is the center, 1 is at the edge and
   * greater than 1 is outside of the pie chart.
   *
   * @param annotationDistance
   */
  public void setAnnotationDistance(double annotationDistance) {

    this.annotationDistance = annotationDistance;
  }

  public AnnotationType getAnnotationType() {

    return annotationType;
  }

  /**
   * Sets the Pie chart's annotation type
   *
   * @param annotationType
   */
  public PieStyler setAnnotationType(AnnotationType annotationType) {

    this.annotationType = annotationType;
    return this;
  }

  public boolean isDrawAllAnnotations() {

    return drawAllAnnotations;
  }

  /**
   * By default, only the annotations that will "fit", as determined algorithmically, will be drawn.
   * Otherwise, you can end up with annotations drawn overlapping. If `drawAllAnnotations` is set
   * true with this method, it will override the algorithmic determination, and always draw all the
   * annotations, one for each slice. You can also try playing around with the method
   * `setStartAngleInDegrees` so the the slices are orientated in a more optimal way. You can also
   * try changing the font size. Also, you can order the slices so that a small slice is followed by
   * a larger slice, while setting this method with `true`.
   *
   * @param drawAllAnnotations
   */
  public PieStyler setDrawAllAnnotations(boolean drawAllAnnotations) {

    this.drawAllAnnotations = drawAllAnnotations;
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
  public void setDonutThickness(double donutThickness) {

    this.donutThickness = donutThickness;
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
  public PieStyler setBorderWidth(double borderWidth) {
    this.borderWidth = (float) borderWidth;
    return this;
  }

  public float getBorderWidth() {
    return borderWidth;
  }

  public enum AnnotationType {
    Value,
    Percentage,
    Label,
    LabelAndPercentage,
    LabelAndValue
  }

  public enum ClockwiseDirectionType {
    CLOCKWISE,
    COUNTER_CLOCKWISE
  }
}
