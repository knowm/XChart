package org.knowm.xchart.demo.charts;

import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.internal.chartpart.Chart;

public interface ExampleChart<C extends Chart<?, ?>> {

  C getChart();

  String getExampleChartName();

  /**
   * Optional hook for configuring {@link XChartPanel} interaction features (tooltips, zoom,
   * cursor) after the panel is created by the demo app. The default implementation does nothing.
   * Override to enable hover tooltips, zoom, etc. for this example.
   *
   * @param panel the panel that was just created with {@link #getChart()}
   */
  default void customizePanel(XChartPanel<C> panel) {}
}
