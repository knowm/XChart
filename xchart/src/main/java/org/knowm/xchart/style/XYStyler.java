package org.knowm.xchart.style;

import java.awt.Color;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.style.theme.Theme;

/** @author timmolter */
public class XYStyler extends AxesChartStyler {

  private XYSeriesRenderStyle xySeriesRenderStyle;

  // Zoom ///////////////////////////
  private boolean isZoomEnabled;
  private Color zoomSelectionColor;
  private boolean zoomResetByDoubleClick;
  private boolean zoomResetByButton;
  private CardinalPosition zoomResetButtomPosition;

  /** Constructor */
  public XYStyler() {

    setAllStyles();
  }

  @Override
  protected void setAllStyles() {

    super.setAllStyles();
    xySeriesRenderStyle = XYSeriesRenderStyle.Line; // set default to line
    isZoomEnabled = false; // set default to false
    zoomSelectionColor = new Color(155, 155, 155, 128);
    zoomResetByDoubleClick = true;
    zoomResetByButton = true;
    zoomResetButtomPosition = CardinalPosition.InsideN;
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

  public CardinalPosition getZoomResetButtomPosition() {
    return zoomResetButtomPosition;
  }

  public void setZoomResetButtomPosition(CardinalPosition zoomResetButtomPosition) {
    this.zoomResetButtomPosition = zoomResetButtomPosition;
  }
}
