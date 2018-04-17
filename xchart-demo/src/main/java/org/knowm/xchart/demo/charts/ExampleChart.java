package org.knowm.xchart.demo.charts;

import org.knowm.xchart.internal.chartpart.Chart;

public interface ExampleChart<C extends Chart<?, ?>> {

  C getChart();
}
