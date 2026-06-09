package org.knowm.xchart.internal.chartpart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.knowm.xchart.internal.series.AxesChartSeries;
import org.knowm.xchart.internal.series.AxesChartSeriesCategory;
import org.knowm.xchart.style.AxesChartStyler;
import org.knowm.xchart.style.BoxStyler;
import org.knowm.xchart.style.BoxStyler.BoxplotCalCulationMethod;

/**
 * Calculate box plot data information for all series of BoxChart.
 *
 * @param <ST> BoxPlotStyler
 * @param <S> BoxSeries
 */
public class BoxPlotDataCalculator<ST extends AxesChartStyler, S extends AxesChartSeries> {

  public List<BoxPlotData> calculate(Map<String, S> seriesMap, ST boxPlotStyler) {

    List<BoxPlotData> boxPlotDataList = new ArrayList<>();
    for (S series : seriesMap.values()) {
      if (!series.isEnabled()) {
        continue;
      }

      double[] yRaw = ((AxesChartSeriesCategory) series).getYData();

      // filter out NaN values (converted from null), then sort a copy
      int count = 0;
      for (double v : yRaw) {
        if (!Double.isNaN(v)) count++;
      }
      if (count == 0) {
        boxPlotDataList.add(null);
        continue;
      }
      double[] data = new double[count];
      int idx = 0;
      for (double v : yRaw) {
        if (!Double.isNaN(v)) data[idx++] = v;
      }
      Arrays.sort(data);
      boxPlotDataList.add(calculate(data, boxPlotStyler));
    }
    return boxPlotDataList;
  }

  private BoxPlotData calculate(double[] data, ST boxPlotStyler) {

    BoxPlotData boxPlotData = new BoxPlotData();
    int n = data.length;
    BoxplotCalCulationMethod boxplotCalCulationMethod =
        ((BoxStyler) boxPlotStyler).getBoxplotCalCulationMethod();
    double q1P = 0.0;
    double q2P = 0.0;
    double q3P = 0.0;
    double four = 4d;
    if (BoxplotCalCulationMethod.N_PLUS_1.equals(boxplotCalCulationMethod)) {
      q1P = (n + 1) / four;
      q2P = 2 * (n + 1) / four;
      q3P = 3 * (n + 1) / four;
    } else if (BoxplotCalCulationMethod.N_LESS_1.equals(boxplotCalCulationMethod)) {
      q1P = (n - 1) / four;
      q2P = 2 * (n - 1) / four;
      q3P = 3 * (n - 1) / four;
    } else if (BoxplotCalCulationMethod.NP.equals(boxplotCalCulationMethod)) {
      q1P = n / four;
      q2P = 2 * n / four;
      q3P = 3 * n / four;
    } else if (BoxplotCalCulationMethod.N_LESS_1_PLUS_1.equals(boxplotCalCulationMethod)) {
      q1P = (n - 1) / four + 1;
      q2P = 2 * (n - 1) / four + 1;
      q3P = 3 * (n - 1) / four + 1;
    }

    boxPlotData.q1 = getQuartile(data, q1P, boxplotCalCulationMethod);
    boxPlotData.median = getQuartile(data, q2P, boxplotCalCulationMethod);
    boxPlotData.q3 = getQuartile(data, q3P, boxplotCalCulationMethod);

    // Interquartile range, IQR = Q3 - Q1
    double irq = boxPlotData.q3 - boxPlotData.q1;

    // Lower whisker, lower = Q1 - 1.5 * IQR
    boxPlotData.lower = boxPlotData.q1 - 1.5 * irq;
    if (boxPlotData.lower < data[0]) {
      boxPlotData.lower = data[0];
    }

    // Upper whisker, upper = Q3 + 1.5 * IQR
    boxPlotData.upper = boxPlotData.q3 + 1.5 * irq;
    if (boxPlotData.upper > data[data.length - 1]) {
      boxPlotData.upper = data[data.length - 1];
    }
    return boxPlotData;
  }

  private static double getQuartile(
      double[] data, double qiP, BoxplotCalCulationMethod boxplotCalCulationMethod) {

    int previousItem = (int) Math.floor(qiP);
    int previousItem_index = previousItem == 0 ? 0 : previousItem - 1;
    int nextItem = (int) Math.ceil(qiP);
    int nextItem_index = data.length == 1 ? 0 : nextItem - 1;
    final double qi;
    if (BoxplotCalCulationMethod.NP == boxplotCalCulationMethod) {
      if (previousItem == nextItem) {
        qi = (data[previousItem_index] + data[nextItem_index]) / 2;
      } else {
        qi = data[nextItem_index];
      }
    } else {
      if (previousItem == nextItem) {
        qi = data[previousItem_index];
      } else {
        qi =
            data[previousItem_index] * (nextItem - qiP)
                + data[nextItem_index] * (qiP - previousItem);
      }
    }
    return qi;
  }
}
