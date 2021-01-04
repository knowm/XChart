package org.knowm.xchart.style;

import org.knowm.xchart.BubbleSeries.BubbleSeriesRenderStyle;
import org.knowm.xchart.style.theme.Theme;

/** @author timmolter */
public class BubbleStyler extends AxesChartStyler {

  private BubbleSeriesRenderStyle bubbleChartSeriesRenderStyle;

  /** Constructor */
  public BubbleStyler() {

    setAllStyles();
  }

  @Override
  protected void setAllStyles() {

    super.setAllStyles();
    bubbleChartSeriesRenderStyle = BubbleSeriesRenderStyle.Round; // set default to Round
  }

  public BubbleSeriesRenderStyle getDefaultSeriesRenderStyle() {

    return bubbleChartSeriesRenderStyle;
  }

  /**
   * Sets the default series render style for the chart (Round is the only one for now) You can
   * override the series render style individually on each Series object.
   *
   * @param bubbleChartSeriesRenderStyle
   */
  public BubbleStyler setDefaultSeriesRenderStyle(
      BubbleSeriesRenderStyle bubbleChartSeriesRenderStyle) {

    this.bubbleChartSeriesRenderStyle = bubbleChartSeriesRenderStyle;
    return this;
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
}
