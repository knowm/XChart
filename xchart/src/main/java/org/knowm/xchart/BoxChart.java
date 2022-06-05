package org.knowm.xchart;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import org.knowm.xchart.internal.Utils;
import org.knowm.xchart.internal.chartpart.AxisPair;
import org.knowm.xchart.internal.chartpart.Legend_Marker;
import org.knowm.xchart.internal.chartpart.Plot_Box;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.internal.series.Series.DataType;
import org.knowm.xchart.internal.style.SeriesColorMarkerLineStyle;
import org.knowm.xchart.style.BoxStyler;
import org.knowm.xchart.style.Styler.ChartTheme;
import org.knowm.xchart.style.theme.Theme;

public class BoxChart extends AbstractChart<BoxStyler, BoxSeries> {

  private final List<String> xData = new ArrayList<>();

  protected BoxChart(int width, int height) {

    super(width, height, new BoxStyler());
    axisPair = new AxisPair<BoxStyler, BoxSeries>(this);
    plot = new Plot_Box<BoxStyler, BoxSeries>(this);
    legend = new Legend_Marker<BoxStyler, BoxSeries>(this);
    paintTarget.addChartPart(axisPair);
    paintTarget.addChartPart(plot);
    paintTarget.addChartPart(chartTitle);
    paintTarget.addChartPart(legend);
  }

  public BoxChart(int width, int height, Theme theme) {

    this(width, height);
    styler.setTheme(theme);
    // Box chart Legend does not show
    styler.setLegendVisible(false);
  }

  public BoxChart(int width, int height, ChartTheme chartTheme) {
    this(width, height, chartTheme.newInstance(chartTheme));
  }

  public BoxChart(BoxChartBuilder chartBuilder) {
    this(chartBuilder.getWidth(), chartBuilder.getHeight(), chartBuilder.getChartTheme());
    setTitle(chartBuilder.getTitle());
    setXAxisTitle(chartBuilder.xAxisTitle);
    setYAxisTitle(chartBuilder.yAxisTitle);
  }

  public BoxSeries addSeries(String seriesName, int[] yData) {

    return addSeries(seriesName, Utils.getNumberListFromIntArray(yData));
  }

  public BoxSeries addSeries(String seriesName, double[] yData) {

    return addSeries(seriesName, Utils.getNumberListFromDoubleArray(yData));
  }

  public BoxSeries addSeries(String seriesName, List<? extends Number> yData) {

    // Sanity checks
    sanityCheck(seriesName, yData);
    xData.add(seriesName);
    BoxSeries series = new BoxSeries(seriesName, xData, yData, null, DataType.String);
    seriesMap.put(seriesName, series);
    return series;
  }

  private void sanityCheck(String seriesName, List<? extends Number> yData) {

    seriesNameDuplicateCheck(seriesName);
    new SanityYChecker(yData).checkSanity();
  }

  public BoxSeries updateBoxSeries(String seriesName, int[] newYData) {

    return updateBoxSeries(seriesName, Utils.getNumberListFromIntArray(newYData));
  }

  public BoxSeries updateBoxSeries(String seriesName, double[] newYData) {

    return updateBoxSeries(seriesName, Utils.getNumberListFromDoubleArray(newYData));
  }

  public BoxSeries updateBoxSeries(String seriesName, List<? extends Number> newYData) {

    BoxSeries series = getSeriesMap().get(seriesName);

    updateSanityCheck(seriesName, newYData, series);
    series.replaceData(newYData);
    return series;
  }

  private void updateSanityCheck(String seriesName, List<? extends Number> newYData, BoxSeries series) {
	checkSeriesValidity(seriesName, series);
	new SanityYChecker(newYData).checkSanity();
  }

  @Override
  public void paint(Graphics2D graphics, int width, int height) {

    settingPaint(width, height);
    doPaint(graphics);
  }

  @Override
  protected void specificSetting() {
	  setSeriesStyles();
  }

  @Override
  protected void setSeriesDefaultForNullPart(Series series, SeriesColorMarkerLineStyle seriesColorMarkerLineStyle) {
	  BoxSeries boxSeries = (BoxSeries) series;
	  if (boxSeries.getLineStyle() == null) { // wasn't set manually
		  boxSeries.setLineStyle(seriesColorMarkerLineStyle.getStroke());
	  }
	  if (boxSeries.getLineColor() == null) { // wasn't set manually
		  boxSeries.setLineColor(seriesColorMarkerLineStyle.getColor());
	  }
	  if (boxSeries.getFillColor() == null) { // wasn't set manually
		  boxSeries.setFillColor(seriesColorMarkerLineStyle.getColor());
	  }
	  if (boxSeries.getMarker() == null) { // wasn't set manually
		  boxSeries.setMarker(seriesColorMarkerLineStyle.getMarker());
	  }
	  if (boxSeries.getMarkerColor() == null) { // wasn't set manually
		  boxSeries.setMarkerColor(seriesColorMarkerLineStyle.getColor());
	  }
  }

  private void checkSeriesValidity(String seriesName, Series series) {
	if (series == null) {
      throw new IllegalArgumentException("Series name >" + seriesName + "< not found!!!");
    }
  }
}
