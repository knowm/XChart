package org.knowm.xchart.internal;

import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.style.Styler.ChartTheme;
import org.knowm.xchart.style.theme.Theme;

/** A "Builder" to make creating charts easier */
public abstract class ChartBuilder<T extends ChartBuilder<?, ?>, C extends Chart<?, ?>> {

  public int width = 800;
  public int height = 600;
  public String title = "";

  public ChartTheme chartTheme = ChartTheme.XChart;

  /** Custom Theme instance; takes precedence over {@link #chartTheme} when non-null. */
  public Theme customTheme = null;

  /** Constructor */
  protected ChartBuilder() {}

  @SuppressWarnings("unchecked")
  public T width(int width) {

    this.width = width;
    return (T) this;
  }

  @SuppressWarnings("unchecked")
  public T height(int height) {

    this.height = height;
    return (T) this;
  }

  @SuppressWarnings("unchecked")
  public T title(String title) {

    this.title = title;
    return (T) this;
  }

  @SuppressWarnings("unchecked")
  public T theme(ChartTheme chartTheme) {

    this.chartTheme = chartTheme;
    return (T) this;
  }

  /**
   * Sets a custom {@link Theme} instance to apply at chart construction time. Takes precedence over
   * {@link #theme(ChartTheme)} when both are set.
   *
   * @param theme a custom Theme instance
   */
  @SuppressWarnings("unchecked")
  public T theme(Theme theme) {

    this.customTheme = theme;
    return (T) this;
  }

  public abstract C build();
}
