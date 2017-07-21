/**
 * Copyright 2015-2017 Knowm Inc. (http://knowm.org) and contributors.
 * Copyright 2011-2015 Xeiam LLC (http://xeiam.com) and contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.knowm.xchart;

import org.knowm.xchart.internal.chartpart.RenderableSeries;
import org.knowm.xchart.internal.chartpart.RenderableSeries.LegendRenderType;
import org.knowm.xchart.internal.series.NoMarkersSeries;

/**
 * A Series containing X, Y and bubble size data to be plotted on a Chart
 *
 * @author timmolter
 */
public class BubbleSeries extends NoMarkersSeries {

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

  private BubbleSeriesRenderStyle bubbleSeriesRenderStyle = null;

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

  public BubbleSeriesRenderStyle getBubbleSeriesRenderStyle() {

    return bubbleSeriesRenderStyle;
  }

  public void setBubbleSeriesRenderStyle(BubbleSeriesRenderStyle bubbleSeriesRenderStyle) {

    this.bubbleSeriesRenderStyle = bubbleSeriesRenderStyle;
  }

  @Override
  public LegendRenderType getLegendRenderType() {

    return bubbleSeriesRenderStyle.getLegendRenderType();
  }

}