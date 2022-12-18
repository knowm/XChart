package org.knowm.xchart.style;

import java.awt.Color;
import java.awt.Font;
import java.util.function.Function;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.style.colors.ChartColor;
import org.knowm.xchart.style.theme.Theme;

/**
 * @author timmolter
 */
public class XYStyler extends AxesChartStyler {

  private XYSeriesRenderStyle xySeriesRenderStyle;

  // Zoom ///////////////////////////
  private boolean isZoomEnabled;
  private Color zoomSelectionColor;
  private boolean zoomResetByDoubleClick;
  private boolean zoomResetByButton;

  // Cursor ////////////////////////////////

  private boolean isCursorEnabled;
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
    isZoomEnabled = false; // set default to false
    zoomSelectionColor = ChartColor.LIGHT_GREY.getColorTranslucent();
    zoomResetByDoubleClick = true;
    zoomResetByButton = true;

    // Cursor ////////////////////////////////
    this.isCursorEnabled = theme.isCursorEnabled();
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
   */
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
  // Zoom ///////////////////////////////

  public boolean isZoomEnabled() {
    return isZoomEnabled;
  }

  public Styler setZoomEnabled(boolean isZoomEnabled) {

    this.isZoomEnabled = isZoomEnabled;
    return this;
  }

  public Color getZoomSelectionColor() {

    return zoomSelectionColor;
  }

  public void setZoomSelectionColor(Color zoomSelectionColor) {

    this.zoomSelectionColor = zoomSelectionColor;
  }

  public boolean isZoomResetByDoubleClick() {

    return zoomResetByDoubleClick;
  }

  public void setZoomResetByDoubleClick(boolean zoomResetByDoubleClick) {

    this.zoomResetByDoubleClick = zoomResetByDoubleClick;
  }

  public boolean isZoomResetByButton() {

    return zoomResetByButton;
  }

  public void setZoomResetByButton(boolean zoomResetByButton) {

    this.zoomResetByButton = zoomResetByButton;
  }

  // Cursor ///////////////////////////////

  public boolean isCursorEnabled() {
    return isCursorEnabled;
  }

  public Styler setCursorEnabled(boolean isCursorEnabled) {

    this.isCursorEnabled = isCursorEnabled;
    return this;
  }

  public Color getCursorColor() {
    return cursorColor;
  }

  public Styler setCursorColor(Color cursorColor) {

    this.cursorColor = cursorColor;
    return this;
  }

  public float getCursorLineWidth() {

    return cursorLineWidth;
  }

  public Styler setCursorLineWidth(float cursorLineWidth) {

    this.cursorLineWidth = cursorLineWidth;
    return this;
  }

  public Font getCursorFont() {

    return cursorFont;
  }

  public Styler setCursorFont(Font cursorFont) {

    this.cursorFont = cursorFont;
    return this;
  }

  public Color getCursorFontColor() {

    return cursorFontColor;
  }

  public Styler setCursorFontColor(Color cursorFontColor) {

    this.cursorFontColor = cursorFontColor;
    return this;
  }

  public Color getCursorBackgroundColor() {

    return cursorBackgroundColor;
  }

  public Styler setCursorBackgroundColor(Color cursorBackgroundColor) {

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
  public void setCustomCursorXDataFormattingFunction(
      Function<Double, String> customCursorXDataFormattingFunction) {
    this.customCursorXDataFormattingFunction = customCursorXDataFormattingFunction;
  }

  public Function<Double, String> getCustomCursorYDataFormattingFunction() {
    return customCursorYDataFormattingFunction;
  }
  /**
   * Set the custom function for formatting the cursor tooltip based on the series Y-Axis data
   *
   * @param customCursorYDataFormattingFunction
   */
  public void setCustomCursorYDataFormattingFunction(
      Function<Double, String> customCursorYDataFormattingFunction) {
    this.customCursorYDataFormattingFunction = customCursorYDataFormattingFunction;
  }
}
