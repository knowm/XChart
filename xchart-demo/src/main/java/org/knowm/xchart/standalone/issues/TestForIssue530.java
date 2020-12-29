package org.knowm.xchart.standalone.issues;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.CategorySeries;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.CategoryStyler;
import org.knowm.xchart.style.Styler.LegendPosition;

public class TestForIssue530 {

  public static final SimpleDateFormat sdfMonthYear = new SimpleDateFormat("MM/yyyy");

  public static void main(String[] args) {
    try {
      new SwingWrapper<CategoryChart>(getVolumesChart()).displayChart();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static CategoryChart getVolumesChart() throws Exception {
    List<Date> dataX;
    List<Double> dataY;
    CategorySeries serie;
    List<Date> xSerie = null;

    // Création du graphe
    CategoryChart chart =
        new CategoryChartBuilder()
            .width(800)
            .height(500)
            .title("Volumes")
            .xAxisTitle("Période")
            .yAxisTitle("Volumes")
            .build();

    // Définition du style du graphe
    CategoryStyler styler = chart.getStyler();
    styler.setLegendPosition(LegendPosition.InsideNW);
    styler.setLegendBackgroundColor(Color.DARK_GRAY);
    styler.setDatePattern("MM/yyyy");
    styler.setXAxisTickMarkSpacingHint(50);
    styler.setAntiAlias(true);
    styler.setChartTitleBoxBorderColor(Color.LIGHT_GRAY);
    styler.setToolTipsEnabled(true);
    styler.setOverlapped(false);
    styler.setStacked(true);
    styler.setChartBackgroundColor(Color.DARK_GRAY);
    styler.setChartFontColor(Color.LIGHT_GRAY);
    styler.setAxisTickLabelsColor(Color.LIGHT_GRAY);
    styler.setYAxisMax(Double.valueOf(100));

    for (int i = 0; i < 3; i++) {
      dataX = getSampleDataX(i);
      dataY = getSampleDataY(i);
      if (xSerie == null) {
        xSerie = dataX;
      }
      serie = chart.addSeries("Serie " + i, xSerie, dataY);
      serie.setChartCategorySeriesRenderStyle(CategorySeries.CategorySeriesRenderStyle.Bar);
      System.out.println("Arrays.toString(dataY.toArray() = " + Arrays.toString(dataY.toArray()));
    }
    return chart;
  }

  public static List<java.util.Date> getSampleDataX(int i) throws Exception {
    List<java.util.Date> xData = new CopyOnWriteArrayList<>();

    xData.add(sdfMonthYear.parse("09/2020"));
    xData.add(sdfMonthYear.parse("10/2020"));
    xData.add(sdfMonthYear.parse("11/2020"));
    return xData;
  }

  public static List<Double> getSampleDataY(int i) throws Exception {
    List<Double> yData = new CopyOnWriteArrayList<Double>();

    yData.add(Double.valueOf(20 + i));
    yData.add(Double.valueOf(10 + i));
    yData.add(Double.valueOf(30 + i));
    return yData;
  }
}
