package org.knowm.xchart.internal.chartpart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
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

    // Box plot data information for all series
    List<BoxPlotData> boxPlotDataList = new ArrayList<>();
    BoxPlotData boxPlotData = null;
    List<Double> data = null;
    Collection<? extends Number> yData = null;
    Iterator<? extends Number> yDataIterator = null;
    Number next = null;
    for (S series : seriesMap.values()) {
      if (!series.isEnabled()) {
        continue;
      }

      yData = ((AxesChartSeriesCategory) series).getYData();
      yDataIterator = yData.iterator();
      data = new ArrayList<>();
      while (yDataIterator.hasNext()) {
        next = yDataIterator.next();
        if (next != null) {
          data.add(next.doubleValue());
        }
      }

      Collections.sort(data);
      boxPlotData = calculate(data, boxPlotStyler);
      boxPlotDataList.add(boxPlotData);
    }
    return boxPlotDataList;
  }

  private BoxPlotData calculate(List<Double> data, ST boxPlotStyler) {

    BoxPlotData boxPlotData = new BoxPlotData();
    int n = data.size();
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
    if (boxPlotData.lower < data.get(0)) {
      boxPlotData.lower = data.get(0);
    }

    // Upper whisker, upper = Q3 + 1.5 * IQR
    boxPlotData.upper = boxPlotData.q3 + 1.5 * irq;
    if (boxPlotData.upper > data.get(data.size() - 1)) {
      boxPlotData.upper = data.get(data.size() - 1);
    }
    return boxPlotData;
  }

  private static double getQuartile(
      List<Double> data, double qiP, BoxplotCalCulationMethod boxplotCalCulationMethod) {

    int previousItem = (int) Math.floor(qiP);
    int previousItem_index = previousItem == 0 ? 0 : previousItem - 1;
    int nextItem = (int) Math.ceil(qiP);
    int nextItem_index = data.size() == 1 ? 0 : nextItem - 1;
    final double qi;
    if (BoxplotCalCulationMethod.NP == boxplotCalCulationMethod) {
      if (previousItem == nextItem) {
        qi = (data.get(previousItem_index) + data.get(nextItem_index)) / 2;
      } else {
        qi = data.get(nextItem_index);
      }
    } else {
      if (previousItem == nextItem) {
        qi = data.get(previousItem_index);
      } else {
        qi =
            data.get(previousItem_index) * (nextItem - qiP)
                + data.get(nextItem_index) * (qiP - previousItem);
      }
    }
    return qi;
  }
}
