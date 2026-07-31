package org.knowm.xchart;

import java.util.Optional;
import org.knowm.xchart.internal.chartpart.RenderableSeries;
import org.knowm.xchart.internal.chartpart.RenderableSeries.LegendRenderType;
import org.knowm.xchart.internal.series.NoMarkersSeries;

/** A Series containing X, Y and bubble size data to be plotted on a Chart */
public class BubbleSeries extends NoMarkersSeries {

  private Optional<BubbleSeriesRenderStyle> bubbleSeriesRenderStyle = Optional.empty();

  /** whether to use the custom, per-data-point tooltip strings instead of the x/y axis values */
  private boolean customToolTips;

  /** the custom tooltip strings, one per data point, indexed the same as the x/y/size data */
  private String[] toolTips;

  /**
   * Constructor
   *
   * @param name
   * @param xData
   * @param yData
   * @param bubbleSizes
   */
  public BubbleSeries(String name, double[] xData, double[] yData, double[] bubbleSizes) {

    super(name, xData, yData, bubbleSizes, DataType.Number);
  }

  public Optional<BubbleSeriesRenderStyle> getBubbleSeriesRenderStyle() {

    return bubbleSeriesRenderStyle;
  }

  public BubbleSeries setBubbleSeriesRenderStyle(BubbleSeriesRenderStyle bubbleSeriesRenderStyle) {

    this.bubbleSeriesRenderStyle = Optional.ofNullable(bubbleSeriesRenderStyle);
    return this;
  }

  /**
   * @deprecated use {@link #setToolTipGenerator(ToolTipGenerator)} instead; will be removed in
   *     4.1.0
   */
  @Deprecated
  public boolean isCustomToolTips() {

    return customToolTips;
  }

  /**
   * Set whether to show the custom, per-data-point tooltip strings set via {@link
   * #setToolTips(String[])} instead of the default formatted x/y axis values. Requires tooltips to
   * be enabled on the styler.
   *
   * @param customToolTips true to show the per-data-point strings from {@link
   *     #setToolTips(String[])}; false to show the default formatted x/y axis values
   * @deprecated use {@link #setToolTipGenerator(ToolTipGenerator)} instead, which works on every
   *     chart type and takes precedence over these strings when both are set; will be removed in
   *     4.1.0
   */
  @Deprecated
  public BubbleSeries setCustomToolTips(boolean customToolTips) {

    this.customToolTips = customToolTips;
    return this;
  }

  /**
   * @deprecated use {@link #setToolTipGenerator(ToolTipGenerator)} instead; will be removed in
   *     4.1.0
   */
  @Deprecated
  public String[] getToolTips() {

    return toolTips;
  }

  /**
   * Set the custom tooltip strings, one per data point, indexed the same as the x/y/bubble-size
   * data. A null entry (or a null array) falls back to the default formatted x/y axis values for
   * that data point. Also requires {@link #setCustomToolTips(boolean)} to be set to true.
   *
   * @param toolTips the tooltip strings, one per data point; a null entry (or a null array) falls
   *     back to the default formatted x/y axis values for that data point
   * @deprecated use {@link #setToolTipGenerator(ToolTipGenerator)} instead, which works on every
   *     chart type and takes precedence over these strings when both are set; will be removed in
   *     4.1.0
   */
  @Deprecated
  public BubbleSeries setToolTips(String[] toolTips) {

    this.toolTips = toolTips;
    return this;
  }

  @Override
  public LegendRenderType getLegendRenderType() {

    return bubbleSeriesRenderStyle
        .orElseThrow(() -> new IllegalStateException("Bubble render style not set"))
        .getLegendRenderType();
  }

  public enum BubbleSeriesRenderStyle implements RenderableSeries {
    Round(LegendRenderType.Box);

    private final LegendRenderType legendRenderType;

    BubbleSeriesRenderStyle(LegendRenderType legendRenderType) {

      this.legendRenderType = legendRenderType;
    }

    @Override
    public LegendRenderType getLegendRenderType() {

      return legendRenderType;
    }
  }
}
