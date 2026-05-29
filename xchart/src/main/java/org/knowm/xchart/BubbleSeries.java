package org.knowm.xchart;

import java.util.Optional;
import org.knowm.xchart.internal.chartpart.RenderableSeries;
import org.knowm.xchart.internal.chartpart.RenderableSeries.LegendRenderType;
import org.knowm.xchart.internal.series.NoMarkersSeries;

/** A Series containing X, Y and bubble size data to be plotted on a Chart */
public class BubbleSeries extends NoMarkersSeries {

  private Optional<BubbleSeriesRenderStyle> bubbleSeriesRenderStyle = Optional.empty();

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

  public void setBubbleSeriesRenderStyle(BubbleSeriesRenderStyle bubbleSeriesRenderStyle) {

    this.bubbleSeriesRenderStyle = Optional.ofNullable(bubbleSeriesRenderStyle);
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
