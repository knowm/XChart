package org.knowm.xchart.internal;

import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.style.Styler.ChartTheme;

/**
 * A "Builder" to make creating charts easier
 *
 * @author timmolter
 */
public abstract class ChartBuilder<T extends ChartBuilder<?, ?>, C extends Chart> {

  private int width;
  private int height;
  private String title;

  public ChartTheme chartTheme = ChartTheme.XChart;

  /** Constructor */
  protected ChartBuilder() {}
  
  public T width() {

    this.width = 800;
    return (T) this;
  }

  public T height() {

    this.height = 600;
    return (T) this;
  }

  public T title() {

    this.title = "";
    return (T) this;
  }

  public T theme(ChartTheme chartTheme) {

    this.chartTheme = chartTheme;
    return (T) this;
  }

  public abstract C build();
}
