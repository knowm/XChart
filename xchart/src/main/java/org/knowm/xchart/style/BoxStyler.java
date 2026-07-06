package org.knowm.xchart.style;

import org.knowm.xchart.style.theme.Theme;

public class BoxStyler extends AxesChartStyler {

  private BoxplotCalCulationMethod boxplotCalCulationMethod;
  private double boxWidthFraction = -1;

  public BoxStyler() {

    this.setAllStyles();
    super.setAllStyles();
  }

  @Override
  public void setTheme(Theme theme) {

    this.theme = theme;
    super.setAllStyles();
    boxplotCalCulationMethod = BoxplotCalCulationMethod.N_LESS_1_PLUS_1;
    boxWidthFraction = -1;
  }

  public BoxplotCalCulationMethod getBoxplotCalCulationMethod() {

    return boxplotCalCulationMethod;
  }

  public BoxStyler setBoxplotCalCulationMethod(BoxplotCalCulationMethod boxplotCalCulationMethod) {

    this.boxplotCalCulationMethod = boxplotCalCulationMethod;
    return this;
  }

  public double getBoxWidthFraction() {

    return boxWidthFraction;
  }

  /**
   * Set the width of each box as a fraction of the horizontal space available to it (i.e. the slot
   * for one series). Valid values are in the range (0, 1]; a value of 1 makes adjacent boxes touch.
   * Any value &lt;= 0 (the default of -1) keeps the legacy width, which is derived from {@link
   * #setPlotContentSize(double)} and is independent of the number of series.
   *
   * @param boxWidthFraction fraction of the per-series slot, in (0, 1]; &lt;= 0 for legacy width
   * @return the styler
   */
  public BoxStyler setBoxWidthFraction(double boxWidthFraction) {

    this.boxWidthFraction = boxWidthFraction;
    return this;
  }

  /** Box plot calculation method, method for determining the position of the quartile */
  public enum BoxplotCalCulationMethod {

    /**
     * Determine the position of the quartile, where Qi is = i (n + 1) / 4, where i = 1, 2, and 3. n
     * represents the number of items contained in the sequence. Calculate the corresponding
     * quartile based on location
     */
    N_PLUS_1,

    /**
     * Determine the position of the quartile, where Qi is = i (n-1) / 4, where i = 1, 2, and 3. n
     * represents the number of items contained in the sequence. Calculate the corresponding
     * quartile based on location
     */
    N_LESS_1,

    /**
     * Determine the position of the quartile, where Qi is np = (i * n) / 4, where i = 1, 2, 3 n
     * represents the number of items contained in the sequence. If np is not an integer, Qi = X [np
     * + 1] If np is an integer, Qi = (X [np] + X [np + 1]) / 2
     */
    NP,

    /**
     * Determine the position of the quartile, where Qi is = i (n-1) / 4 + 1, where i = 1, 2, 3 n
     * represents the number of items contained in the sequence. Calculate the corresponding
     * quartile based on location
     */
    N_LESS_1_PLUS_1;
  }
}
