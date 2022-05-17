package org.knowm.xchart.internal;

import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.style.Styler.ChartTheme;

/**
 * A "Builder" to make creating charts easier
 *
 * @author timmolter
 */
public abstract class ChartBuilder<T extends ChartBuilder<?, ?>, C extends Chart> {

  private int width = 800;
  private int height = 600;
  private String title = "";

  private ChartTheme chartTheme = ChartTheme.XChart;

  /** Constructor */
  protected ChartBuilder() {}

  public T width(int width) {

    this.width = width;
    return (T) this;
  }

  public T height(int height) {

    this.height = height;
    return (T) this;
  }

  public T title(String title) {

    this.title = title;
    return (T) this;
  }

  public T theme(ChartTheme chartTheme) {

    this.chartTheme = chartTheme;
    return (T) this;
  }

  public abstract C build();

  public ChartTheme getChartTheme() {
    return chartTheme;
  }

  public String getTitle() {
    return title;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }
}
