package org.knowm.xchart.demo.charts.heatmap;

import java.awt.Color;
import java.awt.Font;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.knowm.xchart.HeatMapChart;
import org.knowm.xchart.HeatMapChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler.CardinalPosition;
import org.knowm.xchart.style.Styler.LegendLayout;

/**
 * HeatMap X-Axis Data Date Type
 *
 * <p>Demonstrates the following:
 *
 * <ul>
 *   <li>Set rangeColors 8 color
 *   <li>DatePattern
 *   <li>ShowValue
 *   <li>LegendPosition.OutsideS
 *   <li>LegendLayout.Horizontal
 *   <li>ToolTipsEnabled
 */
public class HeatMapChart04 implements ExampleChart<HeatMapChart> {

  public static void main(String[] args) {

    ExampleChart<HeatMapChart> exampleChart = new HeatMapChart04();
    HeatMapChart chart = exampleChart.getChart();
    new SwingWrapper<>(chart).displayChart();
  }

  @Override
  public HeatMapChart getChart() {

    // Create Chart
    HeatMapChart chart =
        new HeatMapChartBuilder()
            .width(1000)
            .height(600)
            .title("24-hour temperature in four major cities")
            .build();

    chart.getStyler().setPlotContentSize(1);
    chart.getStyler().setLegendFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
    chart.getStyler().setToolTipsEnabled(true);
    chart.getStyler().setDatePattern("HH");
    chart.getStyler().setShowValue(true);
    chart.getStyler().setLegendPosition(CardinalPosition.OutsideS);
    chart.getStyler().setLegendLayout(LegendLayout.Horizontal);

    Color[] rangeColors = {
      new Color(255, 204, 153),
      new Color(255, 204, 102),
      new Color(255, 153, 51),
      new Color(255, 80, 80),
      new Color(255, 31, 0),
      new Color(255, 0, 0),
      new Color(204, 51, 0),
      new Color(153, 51, 0)
    };
    chart.getStyler().setRangeColors(rangeColors);

    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    List<Date> xData = new ArrayList<>();
    Date startDate = null;
    try {
      startDate = df.parse("2020-04-17 00:00:00");
    } catch (ParseException e) {
      e.printStackTrace();
    }
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(startDate);
    for (int i = 0; i < 24; i++) {
      xData.add(calendar.getTime());
      calendar.add(Calendar.HOUR, 1);
    }
    List<String> yData = new ArrayList<>();
    yData.add("New York");
    yData.add("Beijing");
    yData.add("Tokyo");
    yData.add("Paris");

    Number[][] data = {
      {0, 0, 7},
      {1, 0, 8},
      {2, 0, 9},
      {3, 0, 9},
      {4, 0, 9},
      {5, 0, 8},
      {6, 0, 8},
      {7, 0, 8},
      {8, 0, 7},
      {9, 0, 6},
      {10, 0, 4},
      {11, 0, 4},
      {12, 0, 4},
      {13, 0, 4},
      {14, 0, 3},
      {15, 0, 3},
      {16, 0, 3},
      {17, 0, 2},
      {18, 0, 2},
      {19, 0, 2},
      {20, 0, 3},
      {21, 0, 5},
      {22, 0, 6},
      {23, 0, 7},
      {0, 1, 12},
      {1, 1, 11},
      {2, 1, 11},
      {3, 1, 11},
      {4, 1, 11},
      {5, 1, 12},
      {6, 1, 13},
      {7, 1, 15},
      {8, 1, 16},
      {9, 1, 18},
      {10, 1, 18},
      {11, 1, 19},
      {12, 1, 20},
      {13, 1, 21},
      {14, 1, 21},
      {15, 1, 21},
      {16, 1, 21},
      {17, 1, 19},
      {18, 1, 17},
      {19, 1, 13},
      {20, 1, 13},
      {21, 1, 12},
      {22, 1, 12},
      {23, 1, 11},
      {0, 2, 10},
      {1, 2, 10},
      {2, 2, 10},
      {3, 2, 10},
      {4, 2, 10},
      {5, 2, 10},
      {6, 2, 10},
      {7, 2, 11},
      {8, 2, 12},
      {9, 2, 12},
      {10, 2, 13},
      {11, 2, 14},
      {12, 2, 14},
      {13, 2, 14},
      {14, 2, 14},
      {15, 2, 14},
      {16, 2, 14},
      {17, 2, 14},
      {18, 2, 14},
      {19, 2, 14},
      {20, 2, 14},
      {21, 2, 14},
      {22, 2, 14},
      {23, 2, 14},
      {0, 3, 8},
      {1, 3, 7},
      {2, 3, 5},
      {3, 3, 5},
      {4, 3, 4},
      {5, 3, 3},
      {6, 3, 3},
      {7, 3, 3},
      {8, 3, 4},
      {9, 3, 6},
      {10, 3, 8},
      {11, 3, 12},
      {12, 3, 14},
      {13, 3, 14},
      {14, 3, 15},
      {15, 3, 15},
      {16, 3, 15},
      {17, 3, 15},
      {18, 3, 15},
      {19, 3, 14},
      {20, 3, 11},
      {21, 3, 10},
      {22, 3, 8},
      {23, 3, 6}
    };

    chart.addSeries("heatMap", xData, yData, Arrays.asList(data));
    return chart;
  }

  @Override
  public String getExampleChartName() {

    return getClass().getSimpleName() + " - HeatMap X-Axis Data Date Type";
  }
}
