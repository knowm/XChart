package org.knowm.xchart.style;

import org.knowm.xchart.PieSeries;

public interface LabelGenerator {
    String generateSeriesLabel(PieSeries series);
}
