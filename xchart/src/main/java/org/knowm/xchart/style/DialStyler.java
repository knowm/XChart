package org.knowm.xchart.style;

import java.awt.*;
import org.knowm.xchart.style.theme.Theme;

public class DialStyler extends Styler {

  private boolean isCircular;

  // helper tick lines
  private boolean axisTicksMarksVisible;
  private Color axisTickMarksColor;
  private BasicStroke axisTickMarksStroke;
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

  private boolean isLabelsVisible;
  private Font labelsFont;

  public DialStyler() {

    setAllStyles();
  }

  @Override
  void setAllStyles() {

    super.setAllStyles();

    this.isCircular = theme.isCircular();

    this.axisTickMarksColor = theme.getAxisTickMarksColor();
    this.axisTickMarksStroke = theme.getAxisTickMarksStroke();
    this.axisTicksMarksVisible = theme.isAxisTicksMarksVisible();

    this.axisTitleVisible = theme.isXAxisTitleVisible() || theme.isYAxisTitleVisible();
    this.axisTitleFont = theme.getAxisTitleFont();
    this.axisTitlePadding = theme.getAxisTitlePadding();

    this.isLabelsVisible = true;
    labelsFont = theme.getBaseFont();
  }

  /**
   * Set the theme the styler should use
   *
   * @param theme
   */
  public DialStyler setTheme(Theme theme) {

    this.theme = theme;
    setAllStyles();
    return this;
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

  public boolean isAxisTicksMarksVisible() {

    return axisTicksMarksVisible;
  }

  public DialStyler setAxisTicksMarksVisible(boolean axisTicksMarksVisible) {

    this.axisTicksMarksVisible = axisTicksMarksVisible;
    return this;
  }

  public Color getAxisTickMarksColor() {

    return axisTickMarksColor;
  }

  public DialStyler setAxisTickMarksColor(Color axisTickMarksColor) {

    this.axisTickMarksColor = axisTickMarksColor;
    return this;
  }

  public BasicStroke getAxisTickMarksStroke() {

    return axisTickMarksStroke;
  }

  public DialStyler setAxisTickMarksStroke(BasicStroke axisTickMarksStroke) {

    this.axisTickMarksStroke = axisTickMarksStroke;
    return this;
  }

  public boolean isAxisTitleVisible() {

    return axisTitleVisible;
  }

  public DialStyler setAxisTitleVisible(boolean axisTitleVisible) {

    this.axisTitleVisible = axisTitleVisible;
    return this;
  }

  public Font getAxisTitleFont() {

    return axisTitleFont;
  }

  public DialStyler setAxisTitleFont(Font axisTitleFont) {

    this.axisTitleFont = axisTitleFont;
    return this;
  }

  public int getAxisTitlePadding() {

    return axisTitlePadding;
  }

  public DialStyler setAxisTitlePadding(int axisTitlePadding) {

    this.axisTitlePadding = axisTitlePadding;
    return this;
  }

  public double[] getAxisTickValues() {

    return axisTickValues;
  }

  public DialStyler setAxisTickValues(double[] axisTickValues) {

    this.axisTickValues = axisTickValues;
    return this;
  }

  public String[] getAxisTickLabels() {

    return axisTickLabels;
  }

  public DialStyler setAxisTickLabels(String[] axisTickLabels) {

    this.axisTickLabels = axisTickLabels;
    return this;
  }

  public double getNormalFrom() {

    return normalFrom;
  }

  public DialStyler setNormalFrom(double normalFrom) {

    this.normalFrom = normalFrom;
    return this;
  }

  public double getNormalTo() {

    return normalTo;
  }

  public DialStyler setNormalTo(double normalTo) {

    this.normalTo = normalTo;
    return this;
  }

  public Color getNormalColor() {

    return normalColor;
  }

  public DialStyler setNormalColor(Color normalColor) {

    this.normalColor = normalColor;
    return this;
  }

  public double getGreenFrom() {

    return greenFrom;
  }

  public DialStyler setGreenFrom(double greenFrom) {

    this.greenFrom = greenFrom;
    return this;
  }

  public double getGreenTo() {

    return greenTo;
  }

  public DialStyler setGreenTo(double greenTo) {

    this.greenTo = greenTo;
    return this;
  }

  public Color getGreenColor() {

    return greenColor;
  }

  public DialStyler setGreenColor(Color greenColor) {

    this.greenColor = greenColor;
    return this;
  }

  public double getRedFrom() {

    return redFrom;
  }

  public DialStyler setRedFrom(double redFrom) {

    this.redFrom = redFrom;
    return this;
  }

  public double getRedTo() {

    return redTo;
  }

  public DialStyler setRedTo(double redTo) {

    this.redTo = redTo;
    return this;
  }

  public Color getRedColor() {

    return redColor;
  }

  public DialStyler setRedColor(Color redColor) {

    this.redColor = redColor;
    return this;
  }

  public double getArcAngle() {

    return arcAngle;
  }

  public DialStyler setArcAngle(double arcAngle) {

    this.arcAngle = arcAngle;
    return this;
  }

  public boolean isAxisTickLabelsVisible() {

    return axisTickLabelsVisible;
  }

  public DialStyler setAxisTickLabelsVisible(boolean axisTickLabelsVisible) {

    this.axisTickLabelsVisible = axisTickLabelsVisible;
    return this;
  }

  public double getDonutThickness() {

    return donutThickness;
  }

  public DialStyler setDonutThickness(double donutThickness) {

    this.donutThickness = donutThickness;
    return this;
  }

  public double getArrowLengthPercentage() {

    return arrowLengthPercentage;
  }

  public DialStyler setArrowLengthPercentage(double arrowLengthPercentage) {

    this.arrowLengthPercentage = arrowLengthPercentage;
    return this;
  }

  public double getArrowArcAngle() {

    return arrowArcAngle;
  }

  public DialStyler setArrowArcAngle(double arrowArcAngle) {

    this.arrowArcAngle = arrowArcAngle;
    return this;
  }

  public double getArrowArcPercentage() {

    return arrowArcPercentage;
  }

  public DialStyler setArrowArcPercentage(double arrowArcPercentage) {

    this.arrowArcPercentage = arrowArcPercentage;
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
  public DialStyler setLabelsVisible(boolean labelsVisible) {

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
  public DialStyler setLabelsFont(Font labelsFont) {

    this.labelsFont = labelsFont;
    return this;
  }
}
