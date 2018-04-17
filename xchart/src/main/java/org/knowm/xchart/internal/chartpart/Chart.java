package org.knowm.xchart.internal.chartpart;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.text.Format;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
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
  protected final Map<String, S> seriesMap = new LinkedHashMap<String, S>();
  final ToolTips toolTips; // ToolTip is here because AxisPair and Plot need access to it
  /** Chart Parts */
  protected AxisPair axisPair;

  protected Plot_<ST, S> plot;
  protected Legend_<ST, S> legend;
  /** Meta Data */
  private int width;

  private int height;
  private String title = "";
  private String xAxisTitle = "";
  private String yAxisTitle = "";
  private Map<Integer, String> yAxisGroupTitleMap = new HashMap<Integer, String>();

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

  public ToolTips getToolTips() {

    return toolTips;
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

  public void setXAxisLabelOverrideMap(Map<Double, Object> overrideMap) {

    axisPair.getAxisLabelOverrideMap().put("X0", overrideMap);
  }

  public void setYAxisLabelOverrideMap(Map<Double, Object> overrideMap) {

    axisPair.getAxisLabelOverrideMap().put("Y0", overrideMap);
  }

  public void setYAxisLabelOverrideMap(Map<Double, Object> overrideMap, int yAxisGroup) {

    axisPair.getAxisLabelOverrideMap().put(("Y" + yAxisGroup), overrideMap);
  }

  public Map<Double, Object> getYAxisLabelOverrideMap(Axis.Direction direction, int yIndex) {

    Map<String, Map<Double, Object>> axisLabelOverrideMap = axisPair.getAxisLabelOverrideMap();
    return axisLabelOverrideMap.get((direction.name() + yIndex));
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
}
