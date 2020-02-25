package org.knowm.xchart.style;

public class BoxPlotStyler extends AxesChartStyler {

  public BoxPlotStyler() {

    this.setAllStyles();
    super.setAllStyles();
  }

  public void setTheme(Theme theme) {

    this.theme = theme;
    super.setAllStyles();
  }
}
