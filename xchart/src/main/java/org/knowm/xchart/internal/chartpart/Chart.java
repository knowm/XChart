package org.knowm.xchart.internal.chartpart;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import org.knowm.xchart.ToolTipType;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.style.Styler;

/** An XChart Chart */
public abstract class Chart<ST extends Styler, S extends Series> implements IChart {

  protected final ST styler;
  protected final ChartTitle<ST, S> chartTitle;
  protected final Map<String, S> seriesMap = new LinkedHashMap<>();

  /** Chart Parts */
  protected Plot_<ST, S> plot;
  protected Legend_<ST, S> legend;

  /** Meta Data */
  private int width;

  private int height;
  private String title = "";

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

  /**
   * Returns an unmodifiable view of the series map. Use {@link #getSeries(String)} to retrieve a
   * specific series by name, or iterate over the values to access all series. To add or remove
   * series use the chart-specific {@code addSeries} / {@link #removeSeries(String)} methods.
   */
  Map<String, S> getSeriesMap() {

    return Collections.unmodifiableMap(seriesMap);
  }

  /**
   * Returns an unmodifiable view of the series values. Use this to iterate over all series or
   * check the series count. To look up a series by name use {@link #getSeries(String)}.
   */
  public Collection<S> getSeriesCollection() {

    return Collections.unmodifiableCollection(seriesMap.values());
  }

  /**
   * Returns the series with the given name, or {@code null} if no such series exists.
   *
   * @param seriesName the series name
   */
  public S getSeries(String seriesName) {

    return seriesMap.get(seriesName);
  }

  public void enableInteractionData() {

    plot.plotContent.interactionData = new PlotInteractionData();
  }

  PlotInteractionData getInteractionData() {

    return plot.plotContent.getInteractionData();
  }

  /**
   * Called by {@link org.knowm.xchart.XChartPanel} after {@code paint()} to feed collected
   * interaction data into the hover-tooltip and cursor overlays and paint them on top of the chart.
   * Keeping this method here avoids exposing the internal {@link PlotInteractionData} type in the
   * public API.
   *
   * @param g the graphics context (same one used for chart painting)
   * @param toolTips hover-tooltip handler, or {@code null} if not enabled
   * @param cursor cursor handler, or {@code null} if not enabled
   */
  public void consumeInteractionData(Graphics2D g, ToolTips toolTips, Cursor cursor) {

    PlotInteractionData data = getInteractionData();
    if (data == null) {
      return;
    }
    if (toolTips != null) {
      toolTips.setData(data);
      toolTips.paint(g);
    }
    if (cursor != null) {
      cursor.setData(data);
      cursor.paint(g);
    }
  }

  /**
   * Renders always-visible data-point labels into the chart image. Called at the end of each
   * chart's {@code paint()} when {@link Styler#isToolTipsAlwaysVisible()} is true. This makes
   * tooltip labels appear in BitmapEncoder output and any other headless rendering context.
   *
   * @param g the graphics context to paint into
   */
  protected void paintAlwaysVisibleToolTips(Graphics2D g) {

    if (!styler.isToolTipsAlwaysVisible()) {
      return;
    }
    PlotInteractionData data = getInteractionData();
    if (data == null) {
      return;
    }
    ToolTipType type = styler.getToolTipType();
    ToolTips toolTips = new ToolTips(this, true, type);
    toolTips.setData(data);
    toolTips.paint(g);
  }
}
