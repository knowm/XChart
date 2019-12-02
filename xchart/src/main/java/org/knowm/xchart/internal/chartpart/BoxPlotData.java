package org.knowm.xchart.internal.chartpart;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.knowm.xchart.internal.Utils;
import org.knowm.xchart.internal.series.AxesChartSeries;
import org.knowm.xchart.internal.series.AxesChartSeriesCategory;
import org.knowm.xchart.style.AxesChartStyler;

/**
 * All data used to calculate box plot
 *
 */
public class BoxPlotData<ST extends AxesChartStyler, S extends AxesChartSeries> {

  public enum BoxDatas {
    // Used to store box plot first quartile
    FIRST_PLOT_QUARTILES(0),
    // Used to store box plot second quartile
    SECOND_PLOT_QUARTILES(1),
    // Uesd to store box plot third quartiles
    THIRD_PLOT_QUARTILES(2),
    // Used to store box plot max value
    MAX_BOX_VALUE(3),
    // Uesd to store box plot min value
    MIN_BOX_VALUE(4),
    // Uesd to store box plot average value
    AVERAGE_VALUE(5),
    // Uesd to store the total number of BoxPlots
    ALL_BOX_DATAS(6);

    int index;

    BoxDatas(int index) {
      this.index = index;
    }

    public int getIndex() {
      return index;
    }
  }

  public Double[][] getBoxPlotData(Map<String, S> seriesMap, ST boxPlotStyler) {

    int numBox = Utils.getAxesChartSeriesXDataSize(seriesMap);
    int seriesMapSize = seriesMap.size();
    Double[][] yValue = new Double[seriesMapSize][numBox];
    Double[][] boxPlotYData = new Double[numBox][BoxDatas.ALL_BOX_DATAS.getIndex()];
    int yValueIndex = 0;
    int boxNo = 0;

    for (S series : seriesMap.values()) {
      if (!series.isEnabled()) {
        continue;
      }
      Collection<? extends Number> yData = ((AxesChartSeriesCategory) series).getYData();
      Iterator<? extends Number> yItr = yData.iterator();
      boxNo = 0;

      while (yItr.hasNext()) {
        Number next = yItr.next();

        if (next == null) {
          yValue[yValueIndex][boxNo] = null;
          boxNo++;
          continue;
        }
        Double y = next.doubleValue();

        // When the Y-axis logarithm is less than or equal to 0,and no exception is
        // thrown,skip this point and box
        if (boxPlotStyler.isYAxisLogarithmic() && y > 0) {
          y = Math.log10(y);
        } else if (boxPlotStyler.isYAxisLogarithmic() && y <= 0) {
          y = null;
        }
        yValue[yValueIndex][boxNo] = y;
        boxNo++;
      }

      // May be the x-axis data will be different
      while (boxNo < numBox) {
        yValue[yValueIndex][boxNo] = null;
        boxNo++;
      }
      yValueIndex++;
    }

    // Box plot seriesMapSize masts greater than 3
    // When seriesMapSize is less than 3 ,it cannot form a box plot
    if (seriesMapSize < 3) {
      return null;
    }
    Double[] y = new Double[seriesMapSize];

    // Extract the value of y
    for (boxNo = 0; boxNo < numBox; boxNo++) {
      double yTotleValue = 0.0;

      for (yValueIndex = 0; yValueIndex < seriesMapSize; yValueIndex++) {
        y[yValueIndex] = yValue[yValueIndex][boxNo];

        if (y[yValueIndex] == null) {
          break;
        }
        yTotleValue += y[yValueIndex];
      }

      // Start calculating box related data
      if (yValueIndex < seriesMapSize && yValue[yValueIndex][boxNo] == null) {
        boxPlotYData[boxNo][BoxDatas.FIRST_PLOT_QUARTILES.getIndex()] = null;
        boxPlotYData[boxNo][BoxDatas.SECOND_PLOT_QUARTILES.getIndex()] = null;
        boxPlotYData[boxNo][BoxDatas.THIRD_PLOT_QUARTILES.getIndex()] = null;
        boxPlotYData[boxNo][BoxDatas.MAX_BOX_VALUE.getIndex()] = null;
        boxPlotYData[boxNo][BoxDatas.MIN_BOX_VALUE.getIndex()] = null;
        boxPlotYData[boxNo][BoxDatas.AVERAGE_VALUE.getIndex()] = null;
        continue;
      }
      // Box plot all data must be sorted form small to large
      Arrays.sort(y);
      double firstPlotQuartiles = (seriesMapSize + 1.0) / 4.0;
      double secondPlotQuartiles = (seriesMapSize + 1.0) / 2.0;
      double thirdPlotQuartiles = 3.0 * (seriesMapSize + 1.0) / 4.0;

      // When firstPlotQuartiles,secondPlotQuartiles,thirdPlotQuartiles are
      // integers, the corresponding value is equal to the y value of subscript
      if (firstPlotQuartiles % 1 == 0) {
        firstPlotQuartiles = y[(int) firstPlotQuartiles - 1];
      } else {
        // when When firstPlotQuartiles,secondPlotQuartiles,thirdPlotQuartiles are not
        // integers, the calculation formula is shown below
        firstPlotQuartiles = y[(int) firstPlotQuartiles - 1]
            + (y[(int) firstPlotQuartiles] - y[(int) firstPlotQuartiles - 1]) * (firstPlotQuartiles % 1);
      }
      boxPlotYData[boxNo][BoxDatas.FIRST_PLOT_QUARTILES.getIndex()] = firstPlotQuartiles;

      if (secondPlotQuartiles % 1 == 0) {
        secondPlotQuartiles = y[(int) secondPlotQuartiles - 1];
      } else {
        secondPlotQuartiles = y[(int) secondPlotQuartiles - 1]
            + (y[(int) secondPlotQuartiles] - y[(int) secondPlotQuartiles - 1]) * (secondPlotQuartiles % 1);
      }
      boxPlotYData[boxNo][BoxDatas.SECOND_PLOT_QUARTILES.getIndex()] = secondPlotQuartiles;

      if (thirdPlotQuartiles % 1 == 0) {
        thirdPlotQuartiles = y[(int) thirdPlotQuartiles - 1];
      } else {
        thirdPlotQuartiles = y[(int) thirdPlotQuartiles - 1]
            + (y[(int) thirdPlotQuartiles] - y[(int) thirdPlotQuartiles - 1]) * (thirdPlotQuartiles % 1);
      }
      boxPlotYData[boxNo][BoxDatas.THIRD_PLOT_QUARTILES.getIndex()] = thirdPlotQuartiles;
      // Box plot max value = thirdPlotQuartiles + 1.5 * (thirdPlotQuartiles -
      // firstPlotQuartiles)
      boxPlotYData[boxNo][BoxDatas.MAX_BOX_VALUE.getIndex()] = thirdPlotQuartiles
          + 1.5 * (thirdPlotQuartiles - firstPlotQuartiles);
      // Box plot min value = firstPlotQuartiles - 1.5 * (thirdPlotQuartiles -
      // firstPlotQuartiles)
      boxPlotYData[boxNo][BoxDatas.MIN_BOX_VALUE.getIndex()] = firstPlotQuartiles
          - 1.5 * (thirdPlotQuartiles - firstPlotQuartiles);
      // Box plot average
      boxPlotYData[boxNo][BoxDatas.AVERAGE_VALUE.getIndex()] = yTotleValue / seriesMapSize;
    }
    return boxPlotYData;
  }
}
