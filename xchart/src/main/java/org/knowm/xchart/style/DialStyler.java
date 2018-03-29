package org.knowm.xchart.style;

import java.awt.*;

public class DialStyler extends Styler {

  private boolean isCircular;

  // helper tick lines
  private boolean axisTicksMarksVisible;
  private Color axisTickMarksColor;
  private Stroke axisTickMarksStroke;
  private double[] axisTickValues = {0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1};
  private String[] axisTickLabels = {
    "0", "10", "20", "30", "40", "50", "60", "70", "80", "90", "100"
  };

  // variable labels
  private boolean axisTitleVisible;
  private Font axisTitleFont;
  private int axisTitlePadding;
  private boolean axisTickLabelsVisible = true;

  // donut area
  private double arcAngle = 270;
  private double donutThickness = .11;

  private double normalFrom = 0;
  private double normalTo = 1;
  private Color normalColor = Color.LIGHT_GRAY;

  private double greenFrom = 0;
  private double greenTo = 0.2;
  private Color greenColor = Color.GREEN;

  private double redFrom = 0.8;
  private double redTo = 1;
  private Color redColor = Color.RED;

  //
  private double arrowLengthPercentage = 0.85;
  private double arrowArcAngle = 20;
  private double arrowArcPercentage = 0.15;

  public DialStyler() {

    this.setAllStyles();
    super.setAllStyles();
  }

  @Override
  void setAllStyles() {

    this.isCircular = theme.isCircular();

    // Annotations ////////////////////////////////
    this.hasAnnotations = true;

    this.axisTickMarksColor = theme.getAxisTickMarksColor();
    this.axisTickMarksStroke = theme.getAxisTickMarksStroke();
    this.axisTicksMarksVisible = theme.isAxisTicksMarksVisible();

    this.axisTitleVisible = theme.isXAxisTitleVisible() || theme.isYAxisTitleVisible();
    this.axisTitleFont = theme.getAxisTitleFont();
    this.axisTitlePadding = theme.getAxisTitlePadding();
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
  public DialStyler setCircular(boolean isCircular) {

    this.isCircular = isCircular;
    return this;
  }

  /**
   * Set the theme the styler should use
   *
   * @param theme
   */
  public DialStyler setTheme(Theme theme) {

    this.theme = theme;
    super.setAllStyles();
    return this;
  }

  public boolean isAxisTicksMarksVisible() {

    return axisTicksMarksVisible;
  }

  public void setAxisTicksMarksVisible(boolean axisTicksMarksVisible) {

    this.axisTicksMarksVisible = axisTicksMarksVisible;
  }

  public Color getAxisTickMarksColor() {

    return axisTickMarksColor;
  }

  public void setAxisTickMarksColor(Color axisTickMarksColor) {

    this.axisTickMarksColor = axisTickMarksColor;
  }

  public Stroke getAxisTickMarksStroke() {

    return axisTickMarksStroke;
  }

  public void setAxisTickMarksStroke(Stroke axisTickMarksStroke) {

    this.axisTickMarksStroke = axisTickMarksStroke;
  }

  public boolean isAxisTitleVisible() {

    return axisTitleVisible;
  }

  public void setAxisTitleVisible(boolean axisTitleVisible) {

    this.axisTitleVisible = axisTitleVisible;
  }

  public Font getAxisTitleFont() {

    return axisTitleFont;
  }

  public void setAxisTitleFont(Font axisTitleFont) {

    this.axisTitleFont = axisTitleFont;
  }

  public int getAxisTitlePadding() {

    return axisTitlePadding;
  }

  public void setAxisTitlePadding(int axisTitlePadding) {

    this.axisTitlePadding = axisTitlePadding;
  }

  public double[] getAxisTickValues() {

    return axisTickValues;
  }

  public void setAxisTickValues(double[] axisTickValues) {

    this.axisTickValues = axisTickValues;
  }

  public String[] getAxisTickLabels() {

    return axisTickLabels;
  }

  public void setAxisTickLabels(String[] axisTickLabels) {

    this.axisTickLabels = axisTickLabels;
  }

  public double getNormalFrom() {

    return normalFrom;
  }

  public void setNormalFrom(double normalFrom) {

    this.normalFrom = normalFrom;
  }

  public double getNormalTo() {

    return normalTo;
  }

  public void setNormalTo(double normalTo) {

    this.normalTo = normalTo;
  }

  public Color getNormalColor() {

    return normalColor;
  }

  public void setNormalColor(Color normalColor) {

    this.normalColor = normalColor;
  }

  public double getGreenFrom() {

    return greenFrom;
  }

  public void setGreenFrom(double greenFrom) {

    this.greenFrom = greenFrom;
  }

  public double getGreenTo() {

    return greenTo;
  }

  public void setGreenTo(double greenTo) {

    this.greenTo = greenTo;
  }

  public Color getGreenColor() {

    return greenColor;
  }

  public void setGreenColor(Color greenColor) {

    this.greenColor = greenColor;
  }

  public double getRedFrom() {

    return redFrom;
  }

  public void setRedFrom(double redFrom) {

    this.redFrom = redFrom;
  }

  public double getRedTo() {

    return redTo;
  }

  public void setRedTo(double redTo) {

    this.redTo = redTo;
  }

  public Color getRedColor() {

    return redColor;
  }

  public void setRedColor(Color redColor) {

    this.redColor = redColor;
  }

  public double getArcAngle() {

    return arcAngle;
  }

  public void setArcAngle(double arcAngle) {

    this.arcAngle = arcAngle;
  }

  public boolean isAxisTickLabelsVisible() {

    return axisTickLabelsVisible;
  }

  public void setAxisTickLabelsVisible(boolean axisTickLabelsVisible) {

    this.axisTickLabelsVisible = axisTickLabelsVisible;
  }

  public double getDonutThickness() {

    return donutThickness;
  }

  public void setDonutThickness(double donutThickness) {

    this.donutThickness = donutThickness;
  }

  public double getArrowLengthPercentage() {

    return arrowLengthPercentage;
  }

  public void setArrowLengthPercentage(double arrowLengthPercentage) {

    this.arrowLengthPercentage = arrowLengthPercentage;
  }

  public double getArrowArcAngle() {

    return arrowArcAngle;
  }

  public void setArrowArcAngle(double arrowArcAngle) {

    this.arrowArcAngle = arrowArcAngle;
  }

  public double getArrowArcPercentage() {

    return arrowArcPercentage;
  }

  public void setArrowArcPercentage(double arrowArcPercentage) {

    this.arrowArcPercentage = arrowArcPercentage;
  }
}
