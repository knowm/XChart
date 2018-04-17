package org.knowm.xchart.internal.chartpart;

/** @author timmolter */
public interface RenderableSeries {

  LegendRenderType getLegendRenderType();

  enum LegendRenderType {
    Line,
    Scatter,
    Box,
    BoxNoOutline
  }
}
