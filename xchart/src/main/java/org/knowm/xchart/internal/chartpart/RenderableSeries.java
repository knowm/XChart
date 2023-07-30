package org.knowm.xchart.internal.chartpart;

public interface RenderableSeries {

  LegendRenderType getLegendRenderType();

  enum LegendRenderType {
    Line,
    Scatter,
    Box,
    BoxNoOutline
  }
}
