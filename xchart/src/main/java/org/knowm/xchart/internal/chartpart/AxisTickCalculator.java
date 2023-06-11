package org.knowm.xchart.internal.chartpart;

import java.text.Format;
import java.util.List;

public interface AxisTickCalculator {
  List<Double> getTickLocations();

  List<String> getTickLabels();

  Format getAxisFormat();
}
