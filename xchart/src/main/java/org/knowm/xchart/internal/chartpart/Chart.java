package org.knowm.xchart.internal.chartpart;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.style.AxesChartStyler;
import org.knowm.xchart.style.Styler;

/** An XChart Chart */
public abstract class Chart<ST extends Styler, S extends Series> {

  protected final ST styler;
  protected final ChartTitle<ST, S> chartTitle;
  protected final Map<String, S> seriesMap = new LinkedHashMap<>();
  protected final ArrayList<ChartPart> annotations = new ArrayList<>();

  /** Chart Parts */
  // TODO maybe move this to a secondary abstract class for inheritors with axes. Pie charts don't
  // have an axis for example
  protected AxisPair axisPair;

  protected Plot_<ST, S> plot;
  protected Legend_<ST, S> legend;

  /** Meta Data */
  private int width;

  private int height;
  private String title = "";
  // TODO maybe move these to a secondary abstract class for inheritors with axes. Pie charts don't
  // have an axis for example
  private String xAxisTitle = "";
  private String yAxisTitle = "";

  // TODO Does this belong here for all chart types?
  private final Map<Integer, String> yAxisGroupTitleMap = new HashMap<>();

  /**
   * Constructor
   *
   * @param width
   * @param height
   * @param styler
   */
  protected Chart(int width, int height, ST styler) {

    this.width = width;
    this.height = height;
    this.styler = styler;

    // TODO move this out??
    this.chartTitle = new ChartTitle<ST, S>(this);
  }

  public abstract void paint(Graphics2D g, int width, int height);

  protected void paintBackground(Graphics2D g) {

    // paint chart main background
    g.setRenderingHint(
        RenderingHints.KEY_ANTIALIASING,
        styler.getAntiAlias()
            ? RenderingHints.VALUE_ANTIALIAS_ON
            : RenderingHints.VALUE_ANTIALIAS_OFF); // global rendering hint
    g.setColor(styler.getChartBackgroundColor());
    Shape rect = new Rectangle2D.Double(0, 0, getWidth(), getHeight());
    g.fill(rect);
  }

  /**
   * Gets the Chart's styler, which can be used to customize the Chart's appearance
   *
   * @return the styler
   */
  public ST getStyler() {

    return styler;
  }

  public S removeSeries(String seriesName) {

    return seriesMap.remove(seriesName);
  }

  /** Getters and Setters */
  public int getWidth() {

    return width;
  }

  protected void setWidth(int width) {

    this.width = width;
  }

  public int getHeight() {

    return height;
  }

  protected void setHeight(int height) {

    this.height = height;
  }

  // TODO remove public
  public String getTitle() {

    return title;
  }

  public void setTitle(String title) {

    this.title = title;
  }

  public String getXAxisTitle() {

    return xAxisTitle;
  }

  public void setXAxisTitle(String xAxisTitle) {

    this.xAxisTitle = xAxisTitle;
  }

  public String getYAxisTitle() {

    // TODO just call the getYAxisGroupTitle method passing in the 0th index
    return yAxisTitle;
  }

  public void setYAxisTitle(String yAxisTitle) {

    this.yAxisTitle = yAxisTitle;
  }

  // TODO these related methods don't make sense for all chart types
  public String getYAxisGroupTitle(int yAxisGroup) {

    String title = yAxisGroupTitleMap.get(yAxisGroup);
    if (title == null) {
      return yAxisTitle;
    }
    return title;
  }

  public void setYAxisGroupTitle(int yAxisGroup, String yAxisTitle) {

    yAxisGroupTitleMap.put(yAxisGroup, yAxisTitle);
  }

  public void addAnnotation(Annotation annotation) {

    annotations.add(annotation);
    annotation.init(this);
  }

  /**
   * @Deprecated - use styler instead
   *
   * @param customFormattingFunction
   */
  public void setCustomXAxisTickLabelsFormatter(Function<Double, String> customFormattingFunction) {
    AxesChartStyler axesChartStyler = (AxesChartStyler) (styler);
    axesChartStyler.setxAxisTickLabelsFormattingFunction(customFormattingFunction);
  }

  /**
   * @Deprecated - use styler instead
   *
   * @param customFormattingFunction
   */
  public void setCustomYAxisTickLabelsFormatter(Function<Double, String> customFormattingFunction) {
    AxesChartStyler axesChartStyler = (AxesChartStyler) (styler);
    axesChartStyler.setyAxisTickLabelsFormattingFunction(customFormattingFunction);
  }

  /** Chart Parts Getters */
  ChartTitle<ST, S> getChartTitle() {

    return chartTitle;
  }

  Legend_<ST, S> getLegend() {

    return legend;
  }

  Plot_<ST, S> getPlot() {

    return plot;
  }

  Axis getXAxis() {

    return axisPair.getXAxis();
  }

  Axis getYAxis() {

    return axisPair.getYAxis();
  }

  Axis getYAxis(int yIndex) {

    return axisPair.getYAxis(yIndex);
  }

  AxisPair getAxisPair() {

    return axisPair;
  }

  Format getXAxisFormat() {
    return axisPair.getXAxis().getAxisTickCalculator().getAxisFormat();
  }

  Format getYAxisFormat() {
    return axisPair.getYAxis().getAxisTickCalculator().getAxisFormat();
  }

  // TODO investigate this
  Format getYAxisFormat(String yAxisDecimalPattern) {
    final Format format;
    if (yAxisDecimalPattern != null) {
      format = new DecimalFormat(yAxisDecimalPattern);
    } else {
      format = axisPair.getYAxis().getAxisTickCalculator().getAxisFormat();
    }
    return format;
  }

  public double getChartXFromCoordinate(int screenX) {

    if (axisPair == null) {
      return Double.NaN;
    }
    return axisPair.getXAxis().getChartValue(screenX);
  }

  public double getChartYFromCoordinate(int screenY) {

    if (axisPair == null) {
      return Double.NaN;
    }
    return axisPair.getYAxis().getChartValue(screenY);
  }

  public double getChartYFromCoordinate(int screenY, int yIndex) {

    if (axisPair == null) {
      return Double.NaN;
    }
    return axisPair.getYAxis(yIndex).getChartValue(screenY);
  }

  public double getScreenXFromChart(double xValue) {

    if (axisPair == null) {
      return Double.NaN;
    }
    return axisPair.getXAxis().getScreenValue(xValue);
  }

  public double getScreenYFromChart(double yValue) {

    if (axisPair == null) {
      return Double.NaN;
    }
    return axisPair.getYAxis().getScreenValue(yValue);
  }

  public double getScreenYFromChart(double yValue, int yIndex) {

    if (axisPair == null) {
      return Double.NaN;
    }
    return axisPair.getYAxis(yIndex).getScreenValue(yValue);
  }

  //  ArrayList<ChartPart> getAnnotations() {
  //
  //    return annotations;
  //  }

  // TODO remove public
  public double getYAxisLeftWidth() {

    java.awt.geom.Rectangle2D.Double bounds = getAxisPair().getLeftYAxisBounds();
    return bounds.width + bounds.x;
  }

  // TODO remove this?
  public Map<String, S> getSeriesMap() {

    return seriesMap;
  }
}
