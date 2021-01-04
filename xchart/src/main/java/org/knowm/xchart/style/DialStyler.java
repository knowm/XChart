package org.knowm.xchart.style;

import static org.knowm.xchart.style.colors.ChartColor.BLUE;
import static org.knowm.xchart.style.colors.ChartColor.GREEN;
import static org.knowm.xchart.style.colors.ChartColor.RED;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
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
  private double donutThickness = .17;

  private double lowerFrom = 0;
  private double lowerTo = 0.2;
  private Color lowerColor = GREEN.getColor();

  private double middleFrom = 0.2;
  private double middleTo = .8;
  private Color middleColor = Color.LIGHT_GRAY;

  private double upperFrom = 0.8;
  private double upperTo = 1;
  private Color upperColor = RED.getColor();

  //
  private double arrowLengthPercentage = 0.7;
  private double arrowArcAngle = 20;
  private double arrowArcPercentage = 0.15;
  private Color arrowColor = BLUE.getColor();

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

  public double getMiddleFrom() {

    return middleFrom;
  }

  public DialStyler setMiddleFrom(double middleFrom) {

    this.middleFrom = middleFrom;
    return this;
  }

  public double getMiddleTo() {

    return middleTo;
  }

  public DialStyler setMiddleTo(double middleTo) {

    this.middleTo = middleTo;
    return this;
  }

  public Color getMiddleColor() {

    return middleColor;
  }

  public DialStyler setMiddleColor(Color middleColor) {

    this.middleColor = middleColor;
    return this;
  }

  public double getLowerFrom() {

    return lowerFrom;
  }

  public DialStyler setLowerFrom(double lowerFrom) {

    this.lowerFrom = lowerFrom;
    return this;
  }

  public double getLowerTo() {

    return lowerTo;
  }

  public DialStyler setLowerTo(double lowerTo) {

    this.lowerTo = lowerTo;
    return this;
  }

  public Color getLowerColor() {

    return lowerColor;
  }

  public DialStyler setLowerColor(Color lowerColor) {

    this.lowerColor = lowerColor;
    return this;
  }

  public double getUpperFrom() {

    return upperFrom;
  }

  public DialStyler setUpperFrom(double upperFrom) {

    this.upperFrom = upperFrom;
    return this;
  }

  public double getUpperTo() {

    return upperTo;
  }

  public DialStyler setUpperTo(double upperTo) {

    this.upperTo = upperTo;
    return this;
  }

  public Color getUpperColor() {

    return upperColor;
  }

  public DialStyler setUpperColor(Color upperColor) {

    this.upperColor = upperColor;
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

  public Color getArrowColor() {

    return arrowColor;
  }

  /**
   * Set the line color of the series
   *
   * @param color
   */
  public DialStyler setArrowColor(java.awt.Color color) {

    this.arrowColor = color;
    return this;
  }

  public boolean isLabelsVisible() {

    return isLabelsVisible;
  }

  /**
   * Sets if labels should be added to charts.
   *
   * @param labelsVisible
   */
  public DialStyler setLabelVisible(boolean labelsVisible) {

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
  public DialStyler setLabelFont(Font labelsFont) {

    this.labelsFont = labelsFont;
    return this;
  }
}
