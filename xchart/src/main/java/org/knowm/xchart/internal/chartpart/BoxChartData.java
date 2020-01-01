package org.knowm.xchart.internal.chartpart;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import org.knowm.xchart.internal.series.AxesChartSeries;
import org.knowm.xchart.internal.series.AxesChartSeriesCategory;
import org.knowm.xchart.style.AxesChartStyler;

/** All data used to calculate box chart */
public class BoxChartData<ST extends AxesChartStyler, S extends AxesChartSeries> {

  // Used to store box plot first quartile
  public static final int FIRST_PLOT_QUARTILES_INDEX = 0;
  // Used to store box plot second quartile
  public static final int SECOND_PLOT_QUARTILES_INDEX = 1;
  // Uesd to store box plot third quartiles
  public static final int THIRD_PLOT_QUARTILES_INDEX = 2;
  // Used to store box plot max value
  public static final int MAX_BOX_VALUE_INDEX = 3;
  // Uesd to store box plot min value
  public static final int MIN_BOX_VALUE_INDEX = 4;
  // Uesd to store box plot average value
  public static final int AVERAGE_VALUE_INDEX = 5;
  // Uesd to store the total number of BoxPlots
  public static final int BOX_DATAS_LENGTH = 6;

  public Double[][] getBoxPlotData(Map<String, S> seriesMap, ST boxPlotStyler) {

    int numBox = seriesMap.size();
    Double[][] boxPlotYData = new Double[numBox][BOX_DATAS_LENGTH];
    int yValueIndex = 0;
    int boxNo = 0;

    for (S series : seriesMap.values()) {
      if (!series.isEnabled()) {
        continue;
      }
      Collection<? extends Number> yData = ((AxesChartSeriesCategory) series).getYData();
      Iterator<? extends Number> yItr = yData.iterator();
      Double[] yValue = new Double[yData.size()];
      double yTotleValue = 0.0;
      boolean isNeedToCount = true;
      yValueIndex = 0;

      while (yItr.hasNext()) {
        Number next = yItr.next();

        if (next == null) {
          yValue[yValueIndex] = null;
          isNeedToCount = false;
          yValueIndex++;
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
        yValue[yValueIndex] = y;
        yValueIndex++;
        yTotleValue += y;
      }

      // Box plot seriesMapSize masts greater than 3
      // When seriesMapSize is less than 3 ,it cannot form a box plot
      if (yData.size() >= 3 && isNeedToCount) {

        // Box plot all data must be sorted form small to large
        Arrays.sort(yValue);
        double firstPlotQuartiles = (yData.size() + 1.0) / 4.0;
        double secondPlotQuartiles = (yData.size() + 1.0) / 2.0;
        double thirdPlotQuartiles = 3.0 * (yData.size() + 1.0) / 4.0;

        // When firstPlotQuartiles,secondPlotQuartiles,thirdPlotQuartiles are
        // integers, the corresponding value is equal to the y value of subscript
        if (firstPlotQuartiles % 1 == 0) {
          firstPlotQuartiles = yValue[(int) firstPlotQuartiles - 1];
        } else {
          // when When firstPlotQuartiles,secondPlotQuartiles,thirdPlotQuartiles are not
          // integers, the calculation formula is shown below
          firstPlotQuartiles =
              yValue[(int) firstPlotQuartiles - 1]
                  + (yValue[(int) firstPlotQuartiles] - yValue[(int) firstPlotQuartiles - 1])
                      * (firstPlotQuartiles % 1);
        }
        boxPlotYData[boxNo][FIRST_PLOT_QUARTILES_INDEX] = firstPlotQuartiles;

        if (secondPlotQuartiles % 1 == 0) {
          secondPlotQuartiles = yValue[(int) secondPlotQuartiles - 1];
        } else {
          secondPlotQuartiles =
              yValue[(int) secondPlotQuartiles - 1]
                  + (yValue[(int) secondPlotQuartiles] - yValue[(int) secondPlotQuartiles - 1])
                      * (secondPlotQuartiles % 1);
        }
        boxPlotYData[boxNo][SECOND_PLOT_QUARTILES_INDEX] = secondPlotQuartiles;

        if (thirdPlotQuartiles % 1 == 0) {
          thirdPlotQuartiles = yValue[(int) thirdPlotQuartiles - 1];
        } else {
          thirdPlotQuartiles =
              yValue[(int) thirdPlotQuartiles - 1]
                  + (yValue[(int) thirdPlotQuartiles] - yValue[(int) thirdPlotQuartiles - 1])
                      * (thirdPlotQuartiles % 1);
        }
        boxPlotYData[boxNo][THIRD_PLOT_QUARTILES_INDEX] = thirdPlotQuartiles;
        // Box plot max value = thirdPlotQuartiles + 1.5 * (thirdPlotQuartiles -
        // firstPlotQuartiles)
        boxPlotYData[boxNo][MAX_BOX_VALUE_INDEX] =
            thirdPlotQuartiles + 1.5 * (thirdPlotQuartiles - firstPlotQuartiles);
        // Box plot min value = firstPlotQuartiles - 1.5 * (thirdPlotQuartiles -
        // firstPlotQuartiles)
        boxPlotYData[boxNo][MIN_BOX_VALUE_INDEX] =
            firstPlotQuartiles - 1.5 * (thirdPlotQuartiles - firstPlotQuartiles);
        // Box plot average
        boxPlotYData[boxNo][AVERAGE_VALUE_INDEX] = yTotleValue / yData.size();
        boxNo++;
      }
    }
    return boxPlotYData;
  }
}
