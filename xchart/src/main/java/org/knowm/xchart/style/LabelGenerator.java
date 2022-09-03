package org.knowm.xchart.style;

import org.knowm.xchart.internal.series.Series;

public interface LabelGenerator {
    String generateSeriesLabel(Series series);
}
