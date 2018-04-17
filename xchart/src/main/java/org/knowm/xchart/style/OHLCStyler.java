package org.knowm.xchart.style;

import org.knowm.xchart.OHLCSeries;
import org.knowm.xchart.OHLCSeries.OHLCSeriesRenderStyle;

/** @author arthurmcgibbon */
public class OHLCStyler extends AxesChartStyler {

  private OHLCSeriesRenderStyle ohlcSeriesRenderStyle;

  /** Constructor */
  public OHLCStyler() {

    this.setAllStyles();
    super.setAllStyles();
  }

  @Override
  protected void setAllStyles() {

    ohlcSeriesRenderStyle = OHLCSeriesRenderStyle.Candle; // set default to candle
  }

  public OHLCSeries.OHLCSeriesRenderStyle getDefaultSeriesRenderStyle() {

    return ohlcSeriesRenderStyle;
  }

  /**
   * Sets the default series render style for the chart (candle, hilo, etc.) You can override the
   * series render style individually on each Series object.
   *
   * @param ohlcSeriesRenderStyle
   */
  public OHLCStyler setDefaultSeriesRenderStyle(OHLCSeriesRenderStyle ohlcSeriesRenderStyle) {

    this.ohlcSeriesRenderStyle = ohlcSeriesRenderStyle;
    return this;
  }

  /**
   * Set the theme the styler should use
   *
   * @param theme
   */
  public void setTheme(Theme theme) {

    this.theme = theme;
    super.setAllStyles();
  }
}
