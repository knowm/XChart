package org.knowm.xchart.style;

import org.knowm.xchart.CategorySeries.CategorySeriesRenderStyle;

/** @author timmolter */
public class CategoryStyler extends AxesChartStyler {

  private CategorySeriesRenderStyle chartCategorySeriesRenderStyle;

  private double availableSpaceFill;
  private boolean isOverlapped;
  private boolean isStacked;

  /** Constructor */
  public CategoryStyler() {

    this.setAllStyles();
    super.setAllStyles();
  }

  @Override
  protected void setAllStyles() {

    this.chartCategorySeriesRenderStyle = CategorySeriesRenderStyle.Bar; // set default to bar

    availableSpaceFill = theme.getAvailableSpaceFill();
    isOverlapped = theme.isOverlapped();
  }

  public CategorySeriesRenderStyle getDefaultSeriesRenderStyle() {

    return chartCategorySeriesRenderStyle;
  }

  /**
   * Sets the default series render style for the chart (bar, stick, line, scatter, area, etc.) You
   * can override the series render style individually on each Series object.
   *
   * @param chartCategorySeriesRenderStyle
   */
  public CategoryStyler setDefaultSeriesRenderStyle(
      CategorySeriesRenderStyle chartCategorySeriesRenderStyle) {

    this.chartCategorySeriesRenderStyle = chartCategorySeriesRenderStyle;
    return this;
  }

  public double getAvailableSpaceFill() {

    return availableSpaceFill;
  }

  /**
   * Sets the available space for rendering each category as a percentage. For a bar chart with one
   * series, it will be the width of the bar as a percentage of the maximum space alloted for the
   * bar. If there are three series and three bars, the three bars will share the available space.
   * This affects all category series render types, not only bar charts. Full width is 100%, i.e.
   * 1.0
   *
   * @param availableSpaceFill
   */
  public CategoryStyler setAvailableSpaceFill(double availableSpaceFill) {

    this.availableSpaceFill = availableSpaceFill;
    return this;
  }

  public boolean isOverlapped() {

    return isOverlapped;
  }

  /**
   * set whether or not series renderings (i.e. bars, stick, etc.) are overlapped. Otherwise they
   * are placed side-by-side.
   *
   * @param isOverlapped
   */
  public CategoryStyler setOverlapped(boolean isOverlapped) {

    this.isOverlapped = isOverlapped;
    return this;
  }

  public boolean isStacked() {

    return isStacked;
  }

  /**
   * Set whether or not series renderings (i.e. bars, stick, etc.) are stacked.
   *
   * @param isStacked
   */
  public void setStacked(boolean isStacked) {

    this.isStacked = isStacked;
  }

  /**
   * Set the theme the styler should use
   *
   * @param theme
   */
  public void setTheme(Theme theme) {

    this.theme = theme;
    super.setAllStyles();
  }
}
