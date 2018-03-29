package org.knowm.xchart.internal.chartpart;

/** @author timmolter */
public interface RenderableSeries {

  enum LegendRenderType {
    Line,
    Scatter,
    Box,
    BoxNoOutline
  }

  LegendRenderType getLegendRenderType();
}
