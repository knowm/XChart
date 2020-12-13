package org.knowm.xchart.internal.chartpart;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.style.Styler;

/**
 * An XChart Chart
 *
 * @author timmolter
 */
public abstract class Chart<ST extends Styler, S extends Series> {

  protected final ST styler;
  protected final ChartTitle<ST, S> chartTitle;
  protected final Map<String, S> seriesMap = new LinkedHashMap<>();
  protected final List<String> infoContent = new ArrayList<>();
  final ToolTips toolTips; // ToolTip is here because AxisPair and Plot need access to it
  final Cursor cursor;
  /** Chart Parts */
  protected AxisPair axisPair;

  protected Plot_<ST, S> plot;
  protected Legend_<ST, S> legend;
  protected InfoPanel infoPanel;

  /** Meta Data */
  private int width;

  private int height;
  private String title = "";
  private String xAxisTitle = "";
  private String yAxisTitle = "";
  private Map<Integer, String> yAxisGroupTitleMap = new HashMap<Integer, String>();
  protected ArrayList<ChartPart> plotParts = new ArrayList<ChartPart>();

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

    this.toolTips = new ToolTips(styler);
    this.cursor = new Cursor(styler);

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

  /** Meta Data Getters and Setters */
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

    return yAxisTitle;
  }

  public void setYAxisTitle(String yAxisTitle) {

    this.yAxisTitle = yAxisTitle;
  }

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

  public Map<String, S> getSeriesMap() {

    return seriesMap;
  }

  public List<String> getInfoContent() {
    return infoContent;
  }

  public void setInfoContent(List<String> content) {
    infoContent.clear();
    infoContent.addAll(content);
  }

  public void addInfoContent(String content) {
    List<String> lines = Arrays.asList(content.split("\\n"));
    infoContent.addAll(lines);
  }

  /** Chart Parts Getters */
  ChartTitle<ST, S> getChartTitle() {

    return chartTitle;
  }

  Legend_<ST, S> getLegend() {

    return legend;
  }

  InfoPanel getInfoPanel() {

    return infoPanel;
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

  Format getYAxisFormat(String yAxisDecimalPattern) {
    Format format = null;
    if (yAxisDecimalPattern != null) {
      format = new DecimalFormat(yAxisDecimalPattern);
    } else {
      format = axisPair.getYAxis().getAxisTickCalculator().getAxisFormat();
    }
    return format;
  }

  public ArrayList<ChartPart> getPlotParts() {

    return plotParts;
  }

  public void addPlotPart(ChartPart chartPart) {

    plotParts.add(chartPart);
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

  public double getYAxisLeftWidth() {

    java.awt.geom.Rectangle2D.Double bounds = getAxisPair().getLeftYAxisBounds();
    return bounds.width + bounds.x;
  }

  /** @Deprecated - use Chart#setCustomXAxisTickLabelsFormatter */
  public void setCustomXAxisTickLabelsMap(Map<Object, Object> overrideMap) {

    axisPair.addCustomTickLabelMap("X0", overrideMap);
  }
  /** @Deprecated - use Chart#setCustomYAxisTickLabelsFormatter */
  public void setCustomYAxisTickLabelsMap(Map<Object, Object> overrideMap) {

    axisPair.addCustomTickLabelMap("Y0", overrideMap);
  }
  /** @Deprecated - use Chart#setCustomYAxisTickLabelsFormatter */
  public void setCustomYAxisTickLabelsMap(Map<Double, Object> overrideMap, int yAxisGroup) {

    axisPair.addCustomTickLabelMap(("Y" + yAxisGroup), overrideMap);
  }

  public void setCustomXAxisTickLabelsFormatter(Function<Double, String> customFormattingFunction) {
    getAxisPair().getXAxis().setCustomFormattingFunction(customFormattingFunction);
  }

  public void setCustomYAxisTickLabelsFormatter(Function<Double, String> customFormattingFunction) {
    getAxisPair().getYAxis().setCustomFormattingFunction(customFormattingFunction);
  }

  public ToolTips getToolTips() {

    return toolTips;
  }

  public Cursor getCursor() {

    return cursor;
  }
}
