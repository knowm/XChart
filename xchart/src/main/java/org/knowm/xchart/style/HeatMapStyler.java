package org.knowm.xchart.style;

import java.awt.Color;
import java.awt.Font;
import java.util.function.Function;
import org.knowm.xchart.style.colors.ChartColor;
import org.knowm.xchart.style.theme.Theme;

/** @author Mr14huashao */
public class HeatMapStyler extends AxesChartStyler {

  private boolean isPiecewise;

  private boolean isPiecewiseRanged = true;

  private int splitNumber;

  /** default range colors, {'#00FFFF'} */
  private static final Color[] DEFAULT_RANGE_COLORS = {
    new Color(255, 255, 255), new Color(0, 255, 255)
  };

  private Color[] rangeColors;

  private boolean isDrawBorder;

  private boolean showValue;

  private Font valueFont;

  private Color valueFontColor;

  // heatData value min
  double min;

  // heatData value max
  double max;

  private int gradientColorColumnWeight;

  private int gradientColorColumnHeight;

  private String heatMapValueDecimalPattern;

  private Function<Double, String> heatMapDecimalValueFormatter;

  /**
   * Set the theme the styler should use
   *
   * @param theme
   */
  public void setTheme(Theme theme) {

    this.theme = theme;
    setAllStyles();
  }

  @Override
  public void setAllStyles() {

    super.setAllStyles();

    rangeColors = new Color[3];
    rangeColors[0] = new Color(255, 165, 0); // #FF5A00
    rangeColors[1] = new Color(255, 69, 0); // #FF4500
    rangeColors[2] = new Color(139, 0, 0); // #8B0000

    splitNumber = 5;
    valueFont = new Font(Font.SANS_SERIF, Font.PLAIN, 16);
    valueFontColor = ChartColor.getAWTColor(ChartColor.BLACK);
    min = Double.MIN_VALUE;
    max = Double.MAX_VALUE;
    gradientColorColumnWeight = 30;
    gradientColorColumnHeight = 200;
  }

  @Override
  public Styler setLegendPosition(CardinalPosition cardinalPosition) {

    if (!CardinalPosition.OutsideE.equals(cardinalPosition)
        && !CardinalPosition.OutsideS.equals(cardinalPosition)) {
      throw new IllegalArgumentException(
          "HeatMapStyler LegendPosition must be OutsideE or OutsideS!!!");
    }
    return super.setLegendPosition(cardinalPosition);
  }

  public boolean isPiecewise() {

    return isPiecewise;
  }

  public HeatMapStyler setPiecewise(boolean isPiecewise) {

    this.isPiecewise = isPiecewise;
    return this;
  }

  public int getSplitNumber() {

    return splitNumber;
  }

  public HeatMapStyler setSplitNumber(int splitNumber) {

    if (splitNumber > 0) {
      this.splitNumber = splitNumber;
    } else {
      this.splitNumber = 1;
    }
    return this;
  }

  public Color[] getRangeColors() {

    return rangeColors;
  }

  public HeatMapStyler setRangeColors(Color[] rangeColors) {

    if (rangeColors != null && rangeColors.length > 0) {
      if (rangeColors.length == 1) {
        this.rangeColors = new Color[2];
        this.rangeColors[0] = rangeColors[0];
        this.rangeColors[1] = rangeColors[0];
      }
      this.rangeColors = rangeColors;
    } else {
      this.rangeColors = DEFAULT_RANGE_COLORS;
    }
    return this;
  }

  public boolean isDrawBorder() {

    return isDrawBorder;
  }

  public HeatMapStyler setDrawBorder(boolean isDrawBorder) {

    this.isDrawBorder = isDrawBorder;
    return this;
  }

  public boolean isShowValue() {

    return showValue;
  }

  public HeatMapStyler setShowValue(boolean showValue) {

    this.showValue = showValue;
    return this;
  }

  public Font getValueFont() {

    return valueFont;
  }

  public HeatMapStyler setValueFont(Font valueFont) {

    this.valueFont = valueFont;
    return this;
  }

  public Color getValueFontColor() {

    return valueFontColor;
  }

  public HeatMapStyler setValueFontColor(Color valueFontColor) {

    this.valueFontColor = valueFontColor;
    return this;
  }

  public double getMin() {

    return min;
  }

  public HeatMapStyler setMin(double min) {

    this.min = min;
    return this;
  }

  public double getMax() {

    return max;
  }

  public HeatMapStyler setMax(double max) {

    this.max = max;
    return this;
  }

  public int getGradientColorColumnWeight() {

    return gradientColorColumnWeight;
  }

  public HeatMapStyler setGradientColorColumnWeight(int gradientColorColumnWeight) {

    if (gradientColorColumnWeight >= 10) {
      this.gradientColorColumnWeight = gradientColorColumnWeight;
    } else {
      this.gradientColorColumnWeight = 10;
    }
    return this;
  }

  public int getGradientColorColumnHeight() {

    return gradientColorColumnHeight;
  }

  public HeatMapStyler setGradientColorColumnHeight(int gradientColorColumnHeight) {

    if (gradientColorColumnHeight >= 100) {
      this.gradientColorColumnHeight = gradientColorColumnHeight;
    } else {
      this.gradientColorColumnHeight = 100;
    }
    return this;
  }

  public String getHeatMapValueDecimalPattern() {

    return heatMapValueDecimalPattern;
  }

  public HeatMapStyler setHeatMapValueDecimalPattern(String heatMapValueDecimalPattern) {

    this.heatMapValueDecimalPattern = heatMapValueDecimalPattern;
    return this;
  }

  public Function<Double, String> getHeatMapDecimalValueFormatter() {
    return heatMapDecimalValueFormatter;
  }

  public void setHeatMapDecimalValueFormatter(
      Function<Double, String> heatMapDecimalValueFormatter) {
    this.heatMapDecimalValueFormatter = heatMapDecimalValueFormatter;
  }

  public boolean isPiecewiseRanged() {
    return isPiecewiseRanged;
  }

  public void setPiecewiseRanged(boolean piecewiseRanged) {
    if (piecewiseRanged) {
      setPiecewise(true);
    }
    isPiecewiseRanged = piecewiseRanged;
  }
}
