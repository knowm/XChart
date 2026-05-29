package org.knowm.xchart.style;

import java.awt.Color;
import java.awt.Font;
import java.util.function.Function;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.style.theme.Theme;

public class XYStyler extends AxesChartStyler {

  private XYSeriesRenderStyle xySeriesRenderStyle;

  // Cursor ////////////////////////////////

  private Color cursorColor;
  private float cursorLineWidth;
  private Font cursorFont;
  private Color cursorFontColor;
  private Color cursorBackgroundColor;
  private Function<Double, String> customCursorXDataFormattingFunction;
  private Function<Double, String> customCursorYDataFormattingFunction;

  /** Constructor */
  public XYStyler() {

    setAllStyles();
  }

  @Override
  protected void setAllStyles() {

    super.setAllStyles();

    // Zoom ///////////////////////////
    // TODO set this from the theme
    xySeriesRenderStyle = XYSeriesRenderStyle.Line; // set default to line

    // Cursor ////////////////////////////////
    this.cursorColor = theme.getCursorColor();
    this.cursorLineWidth = theme.getCursorSize();
    this.cursorFont = theme.getCursorFont();
    this.cursorFontColor = theme.getCursorFontColor();
    this.cursorBackgroundColor = theme.getCursorBackgroundColor();
  }

  /**
   * Set the theme the styler should use
   *
   * @param theme
   * @deprecated Use the builder's {@code .theme(Theme)} method instead.
   */
  @Deprecated
  public void setTheme(Theme theme) {

    this.theme = theme;
    setAllStyles();
  }

  public XYSeriesRenderStyle getDefaultSeriesRenderStyle() {

    return xySeriesRenderStyle;
  }

  /**
   * Sets the default series render style for the chart (line, scatter, area, etc.) You can override
   * the series render style individually on each Series object.
   *
   * @param xySeriesRenderStyle
   */
  public XYStyler setDefaultSeriesRenderStyle(XYSeriesRenderStyle xySeriesRenderStyle) {

    this.xySeriesRenderStyle = xySeriesRenderStyle;
    return this;
  }

  public Color getCursorColor() {
    return cursorColor;
  }

  public XYStyler setCursorColor(Color cursorColor) {

    this.cursorColor = cursorColor;
    return this;
  }

  public float getCursorLineWidth() {

    return cursorLineWidth;
  }

  public XYStyler setCursorLineWidth(float cursorLineWidth) {

    this.cursorLineWidth = cursorLineWidth;
    return this;
  }

  public Font getCursorFont() {

    return cursorFont;
  }

  public XYStyler setCursorFont(Font cursorFont) {

    this.cursorFont = cursorFont;
    return this;
  }

  public Color getCursorFontColor() {

    return cursorFontColor;
  }

  public XYStyler setCursorFontColor(Color cursorFontColor) {

    this.cursorFontColor = cursorFontColor;
    return this;
  }

  public Color getCursorBackgroundColor() {

    return cursorBackgroundColor;
  }

  public XYStyler setCursorBackgroundColor(Color cursorBackgroundColor) {

    this.cursorBackgroundColor = cursorBackgroundColor;
    return this;
  }

  public Function<Double, String> getCustomCursorXDataFormattingFunction() {
    return customCursorXDataFormattingFunction;
  }

  /**
   * Set the custom function for formatting the cursor tooltip based on the series X-Axis data
   *
   * @param customCursorXDataFormattingFunction
   */
  public XYStyler setCustomCursorXDataFormattingFunction(
      Function<Double, String> customCursorXDataFormattingFunction) {
    this.customCursorXDataFormattingFunction = customCursorXDataFormattingFunction;
    return this;
  }

  public Function<Double, String> getCustomCursorYDataFormattingFunction() {
    return customCursorYDataFormattingFunction;
  }

  /**
   * Set the custom function for formatting the cursor tooltip based on the series Y-Axis data
   *
   * @param customCursorYDataFormattingFunction
   */
  public XYStyler setCustomCursorYDataFormattingFunction(
      Function<Double, String> customCursorYDataFormattingFunction) {
    this.customCursorYDataFormattingFunction = customCursorYDataFormattingFunction;
    return this;
  }
}
