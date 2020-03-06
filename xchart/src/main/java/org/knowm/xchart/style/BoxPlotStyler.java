package org.knowm.xchart.style;

public class BoxPlotStyler extends AxesChartStyler {

  private BoxplotCalCulationMethod boxplotCalCulationMethod;

  public BoxPlotStyler() {

    this.setAllStyles();
    super.setAllStyles();
  }

  public void setTheme(Theme theme) {

    this.theme = theme;
    super.setAllStyles();
    boxplotCalCulationMethod = BoxplotCalCulationMethod.N_LESS_1_PLUS_1;
  }

  public BoxplotCalCulationMethod getBoxplotCalCulationMethod() {

    return boxplotCalCulationMethod;
  }

  public void setBoxplotCalCulationMethod(BoxplotCalCulationMethod boxplotCalCulationMethod) {

    this.boxplotCalCulationMethod = boxplotCalCulationMethod;
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
