package org.knowm.xchart.style;

import java.awt.*;
import java.util.HashMap;
import org.knowm.xchart.style.markers.Marker;

/**
 * The styler is used to manage all things related to styling of the vast number of Chart components
 *
 * @author timmolter
 */
public abstract class Styler {

  /** the default Theme */
  Theme theme = new XChartTheme();

  boolean hasAnnotations = false; // set by subclass
  // Chart Style ///////////////////////////////
  private Font baseFont;
  private Color chartBackgroundColor;
  private Color chartFontColor;
  private int chartPadding;
  private Color[] seriesColors;
  private BasicStroke[] seriesLines;
  private Marker[] seriesMarkers;
  // Chart Title ///////////////////////////////
  private Font chartTitleFont;
  private boolean isChartTitleVisible;
  private boolean isChartTitleBoxVisible;
  private Color chartTitleBoxBackgroundColor;
  private Color chartTitleBoxBorderColor;
  private int chartTitlePadding;
  // Chart Legend ///////////////////////////////
  private boolean isLegendVisible;
  private Color legendBackgroundColor;
  private Color legendBorderColor;
  private Font legendFont;
  private int legendPadding;
  private int legendSeriesLineLength;
  private LegendPosition legendPosition;
  private LegendLayout legendLayout = LegendLayout.Vertical;
  // Chart Plot Area ///////////////////////////////
  private Color plotBackgroundColor;
  private Color plotBorderColor;
  private boolean isPlotBorderVisible;
  private double plotContentSize = .92;
  // Tool Tips ///////////////////////////////
  private boolean isToolTipsEnabled;
  private boolean isToolTipsAlwaysVisible;
  private ToolTipType toolTipType;
  private Color toolTipBackgroundColor;
  private Color toolTipBorderColor;
  private Font toolTipFont;
  private Color toolTipHighlightColor;
  // Annotations ///////////////////////////////
  private Font annotationsFont;
  // Misc. ///////////////////////////////
  private boolean antiAlias = true;
  private String decimalPattern;
  private HashMap<Integer, YAxisPosition> yAxisAlignmentMap = new HashMap<Integer, YAxisPosition>();

  void setAllStyles() {

    // Chart Style ///////////////////////////////
    baseFont = theme.getBaseFont();

    chartBackgroundColor = theme.getChartBackgroundColor();
    chartFontColor = theme.getChartFontColor();

    chartPadding = theme.getChartPadding();
    seriesColors = theme.getSeriesColors();
    seriesLines = theme.getSeriesLines();
    seriesMarkers = theme.getSeriesMarkers();

    // Chart Title ///////////////////////////////
    chartTitleFont = theme.getChartTitleFont();
    isChartTitleVisible = theme.isChartTitleVisible();
    isChartTitleBoxVisible = theme.isChartTitleBoxVisible();
    chartTitleBoxBackgroundColor = theme.getChartTitleBoxBackgroundColor();
    chartTitleBoxBorderColor = theme.getChartTitleBoxBorderColor();
    chartTitlePadding = theme.getChartTitlePadding();

    // legend
    isLegendVisible = theme.isLegendVisible();
    legendBackgroundColor = theme.getLegendBackgroundColor();
    legendBorderColor = theme.getLegendBorderColor();
    legendFont = theme.getLegendFont();
    legendPadding = theme.getLegendPadding();
    legendSeriesLineLength = theme.getLegendSeriesLineLength();
    legendPosition = theme.getLegendPosition();

    // Chart Plot Area ///////////////////////////////
    plotBackgroundColor = theme.getPlotBackgroundColor();
    plotBorderColor = theme.getPlotBorderColor();
    isPlotBorderVisible = theme.isPlotBorderVisible();
    plotContentSize = theme.getPlotContentSize();

    // Tool Tips ///////////////////////////////

    isToolTipsEnabled = theme.isToolTipsEnabled();
    toolTipType = theme.getToolTipType();
    toolTipBackgroundColor = theme.getToolTipBackgroundColor();
    toolTipBorderColor = theme.getToolTipBorderColor();
    toolTipFont = theme.getToolTipFont();
    toolTipHighlightColor = theme.getToolTipHighlightColor();

    // Annotations ///////////////////////////////
    annotationsFont = theme.getAnnotationFont();

    // Formatting
    decimalPattern = null;
  }

  public Font getBaseFont() {

    return baseFont;
  }

  /**
   * Set the base font
   *
   * @param baseFont
   * @return styler
   */
  public Styler setBaseFont(Font baseFont) {

    this.baseFont = baseFont;
    return this;
  }

  public Color getChartBackgroundColor() {

    return chartBackgroundColor;
  }

  /**
   * Set the chart background color - the part around the edge of the chart
   *
   * @param color
   */
  public Styler setChartBackgroundColor(Color color) {

    this.chartBackgroundColor = color;
    return this;
  }

  public Color getChartFontColor() {

    return chartFontColor;
  }

  /**
   * Set the chart font color. includes: Chart title, axes label, legend
   *
   * @param color
   */
  public Styler setChartFontColor(Color color) {

    this.chartFontColor = color;
    return this;
  }

  // Chart Style ///////////////////////////////

  public int getChartPadding() {

    return chartPadding;
  }

  /**
   * Set the chart padding
   *
   * @param chartPadding
   */
  public Styler setChartPadding(int chartPadding) {

    this.chartPadding = chartPadding;
    return this;
  }

  public Color[] getSeriesColors() {

    return seriesColors;
  }

  public Styler setSeriesColors(Color[] seriesColors) {

    this.seriesColors = seriesColors;
    return this;
  }

  public BasicStroke[] getSeriesLines() {

    return seriesLines;
  }

  public Styler setSeriesLines(BasicStroke[] seriesLines) {

    this.seriesLines = seriesLines;
    return this;
  }

  public Marker[] getSeriesMarkers() {

    return seriesMarkers;
  }

  public Styler setSeriesMarkers(Marker[] seriesMarkers) {

    this.seriesMarkers = seriesMarkers;
    return this;
  }

  // Chart Title ///////////////////////////////

  public Font getChartTitleFont() {

    return chartTitleFont;
  }

  /**
   * Set the chart title font
   *
   * @param chartTitleFont
   * @return
   */
  public Styler setChartTitleFont(Font chartTitleFont) {

    this.chartTitleFont = chartTitleFont;
    return this;
  }

  public boolean isChartTitleVisible() {

    return isChartTitleVisible;
  }

  /**
   * Set the chart title visibility
   *
   * @param isChartTitleVisible
   */
  public Styler setChartTitleVisible(boolean isChartTitleVisible) {

    this.isChartTitleVisible = isChartTitleVisible;
    return this;
  }

  public boolean isChartTitleBoxVisible() {

    return isChartTitleBoxVisible;
  }

  /**
   * Set the chart title box visibility
   *
   * @param isChartTitleBoxVisible
   */
  public Styler setChartTitleBoxVisible(boolean isChartTitleBoxVisible) {

    this.isChartTitleBoxVisible = isChartTitleBoxVisible;
    return this;
  }

  public Color getChartTitleBoxBackgroundColor() {

    return chartTitleBoxBackgroundColor;
  }

  /**
   * set the chart title box background color
   *
   * @param chartTitleBoxBackgroundColor
   */
  public Styler setChartTitleBoxBackgroundColor(Color chartTitleBoxBackgroundColor) {

    this.chartTitleBoxBackgroundColor = chartTitleBoxBackgroundColor;
    return this;
  }

  public Color getChartTitleBoxBorderColor() {

    return chartTitleBoxBorderColor;
  }

  /**
   * set the chart title box border color
   *
   * @param chartTitleBoxBorderColor
   */
  public Styler setChartTitleBoxBorderColor(Color chartTitleBoxBorderColor) {

    this.chartTitleBoxBorderColor = chartTitleBoxBorderColor;
    return this;
  }

  public int getChartTitlePadding() {

    return chartTitlePadding;
  }

  /**
   * set the chart title padding; the space between the chart title and the plot area
   *
   * @param chartTitlePadding
   */
  public Styler setChartTitlePadding(int chartTitlePadding) {

    this.chartTitlePadding = chartTitlePadding;
    return this;
  }

  public Color getLegendBackgroundColor() {

    return legendBackgroundColor;
  }

  /**
   * Set the chart legend background color
   *
   * @param color
   */
  public Styler setLegendBackgroundColor(Color color) {

    this.legendBackgroundColor = color;
    return this;
  }

  /**
   * Set the chart legend border color
   *
   * @return
   */
  public Color getLegendBorderColor() {

    return legendBorderColor;
  }

  public Styler setLegendBorderColor(Color legendBorderColor) {

    this.legendBorderColor = legendBorderColor;
    return this;
  }

  public Font getLegendFont() {

    return legendFont;
  }

  /**
   * Set the chart legend font
   *
   * @param font
   */
  public Styler setLegendFont(Font font) {

    this.legendFont = font;
    return this;
  }

  // Chart Legend ///////////////////////////////

  public boolean isLegendVisible() {

    return isLegendVisible;
  }

  /**
   * Set the chart legend visibility
   *
   * @param isLegendVisible
   */
  public Styler setLegendVisible(boolean isLegendVisible) {

    this.isLegendVisible = isLegendVisible;
    return this;
  }

  public int getLegendPadding() {

    return legendPadding;
  }

  /**
   * Set the chart legend padding
   *
   * @param legendPadding
   */
  public Styler setLegendPadding(int legendPadding) {

    this.legendPadding = legendPadding;
    return this;
  }

  public int getLegendSeriesLineLength() {

    return legendSeriesLineLength;
  }

  /**
   * Set the chart legend series line length
   *
   * @param legendSeriesLineLength
   * @return
   */
  public Styler setLegendSeriesLineLength(int legendSeriesLineLength) {

    if (legendSeriesLineLength < 0) {
      this.legendSeriesLineLength = 0;
    } else {
      this.legendSeriesLineLength = legendSeriesLineLength;
    }
    return this;
  }

  public LegendPosition getLegendPosition() {

    return legendPosition;
  }

  /**
   * sets the legend position
   *
   * @param legendPosition
   */
  public Styler setLegendPosition(LegendPosition legendPosition) {

    this.legendPosition = legendPosition;
    return this;
  }

  /**
   * Set the legend layout
   *
   * @return
   */
  public LegendLayout getLegendLayout() {

    return legendLayout;
  }

  public void setLegendLayout(LegendLayout legendLayout) {

    this.legendLayout = legendLayout;
  }

  public Color getPlotBackgroundColor() {

    return plotBackgroundColor;
  }

  /**
   * set the plot area's background color
   *
   * @param plotBackgroundColor
   */
  public Styler setPlotBackgroundColor(Color plotBackgroundColor) {

    this.plotBackgroundColor = plotBackgroundColor;
    return this;
  }

  public Color getPlotBorderColor() {

    return plotBorderColor;
  }

  /**
   * set the plot area's border color
   *
   * @param plotBorderColor
   */
  public Styler setPlotBorderColor(Color plotBorderColor) {

    this.plotBorderColor = plotBorderColor;
    return this;
  }

  public boolean isPlotBorderVisible() {

    return isPlotBorderVisible;
  }

  /**
   * sets the visibility of the border around the plot area
   *
   * @param isPlotBorderVisible
   */
  public Styler setPlotBorderVisible(boolean isPlotBorderVisible) {

    this.isPlotBorderVisible = isPlotBorderVisible;
    return this;
  }

  // Chart Plot ///////////////////////////////

  public double getPlotContentSize() {

    return plotContentSize;
  }

  /**
   * Sets the content size of the plot inside the plot area of the chart. To fill the area 100%, use
   * a value of 1.0.
   *
   * @param plotContentSize - Valid range is between 0 and 1.
   */
  public Styler setPlotContentSize(double plotContentSize) {

    if (plotContentSize < 0 || plotContentSize > 1) {
      throw new IllegalArgumentException("Plot content size must be tween 0 and 1!!!");
    }

    this.plotContentSize = plotContentSize;
    return this;
  }

  public boolean isToolTipsEnabled() {

    return isToolTipsEnabled;
  }

  public Styler setToolTipsEnabled(boolean toolTipsEnabled) {

    isToolTipsEnabled = toolTipsEnabled;
    return this;
  }

  public boolean isToolTipsAlwaysVisible() {

    return isToolTipsAlwaysVisible;
  }

  public Styler setToolTipsAlwaysVisible(boolean toolTipsAlwaysVisible) {

    isToolTipsAlwaysVisible = toolTipsAlwaysVisible;
    return this;
  }

  public ToolTipType getToolTipType() {

    return toolTipType;
  }

  public Styler setToolTipType(ToolTipType toolTipType) {

    this.toolTipType = toolTipType;
    return this;
  }

  public Color getToolTipBackgroundColor() {

    return toolTipBackgroundColor;
  }

  public Styler setToolTipBackgroundColor(Color toolTipBackgroundColor) {

    this.toolTipBackgroundColor = toolTipBackgroundColor;
    return this;
  }

  // Tool Tips ///////////////////////////////

  public Color getToolTipBorderColor() {

    return toolTipBorderColor;
  }

  public Styler setToolTipBorderColor(Color toolTipBorderColor) {

    this.toolTipBorderColor = toolTipBorderColor;
    return this;
  }

  public Font getToolTipFont() {

    return toolTipFont;
  }

  public Styler setToolTipFont(Font toolTipFont) {

    this.toolTipFont = toolTipFont;
    return this;
  }

  public Color getToolTipHighlightColor() {

    return toolTipHighlightColor;
  }

  public Styler setToolTipHighlightColor(Color toolTipHighlightColor) {

    this.toolTipHighlightColor = toolTipHighlightColor;
    return this;
  }

  public Boolean hasAnnotations() {

    return hasAnnotations;
  }

  /**
   * Sets if annotations should be added to charts. Each chart type has a different annotation type
   *
   * @param hasAnnotations
   */
  public Styler setHasAnnotations(boolean hasAnnotations) {

    this.hasAnnotations = hasAnnotations;
    return this;
  }

  public Font getAnnotationsFont() {

    return annotationsFont;
  }

  /**
   * Sets the Font used for chart annotations
   *
   * @param annotationsFont
   */
  public Styler setAnnotationsFont(Font annotationsFont) {

    this.annotationsFont = annotationsFont;
    return this;
  }

  public String getDecimalPattern() {

    return decimalPattern;
  }

  /**
   * Set the decimal formatter for all numbers on the chart rendered as Strings
   *
   * @param decimalPattern - the pattern describing the decimal format
   */
  public Styler setDecimalPattern(String decimalPattern) {

    this.decimalPattern = decimalPattern;
    return this;
  }

  // Annotations ///////////////////////////////

  public YAxisPosition getYAxisGroupPosistion(int yAxisGroup) {

    return yAxisAlignmentMap.get(yAxisGroup);
  }

  /**
   * Set the YAxis group position.
   *
   * @param yAxisGroup
   * @param yAxisPosition
   */
  public void setYAxisGroupPosition(int yAxisGroup, YAxisPosition yAxisPosition) {

    yAxisAlignmentMap.put(yAxisGroup, yAxisPosition);
  }

  public Theme getTheme() {

    return theme;
  }

  public boolean getAntiAlias() {

    return antiAlias;
  }

  public void setAntiAlias(boolean newVal) {

    antiAlias = newVal;
  }

  public enum LegendPosition {
    OutsideE,
    InsideNW,
    InsideNE,
    InsideSE,
    InsideSW,
    InsideN,
    InsideS,
    OutsideS
  }

  public enum LegendLayout {
    Vertical,
    Horizontal
  }

  public enum ChartTheme {
    XChart,
    GGPlot2,
    Matlab;

    public Theme newInstance(ChartTheme chartTheme) {

      switch (chartTheme) {
        case GGPlot2:
          return new GGPlot2Theme();

        case Matlab:
          return new MatlabTheme();

        case XChart:
        default:
          return new XChartTheme();
      }
    }
  }

  public enum TextAlignment {
    Left,
    Centre,
    Right
  }

  public enum ToolTipType {
    xLabels,
    yLabels,
    xAndYLabels
  }

  public enum YAxisPosition {
    Left,
    Right
  }
}
