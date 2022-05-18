package org.knowm.xchart.internal.chartpart;

import org.knowm.xchart.CategorySeries;
import org.knowm.xchart.CategorySeries.CategorySeriesRenderStyle;
import org.knowm.xchart.style.CategoryStyler;

/** @author timmolter */
public class Plot_Category<ST extends CategoryStyler, S extends CategorySeries>
    extends Plot_AxesChart<ST, S> {

  /**
   * Constructor
   *
   * @param chart
   */
  public Plot_Category(Chart<ST, S> chart) {

    super(chart);
    
    ST stylerCategory = chart.getStyler();
    
    final boolean isBar = CategorySeriesRenderStyle.Bar.equals(stylerCategory.getDefaultSeriesRenderStyle());
    final boolean isStick = CategorySeriesRenderStyle.Stick.equals(stylerCategory.getDefaultSeriesRenderStyle());
    final boolean isSteppedBar = CategorySeriesRenderStyle.SteppedBar.equals(stylerCategory.getDefaultSeriesRenderStyle());
    
    if (isBar || isStick || isSteppedBar) {
      this.plotContent = new PlotContent_Category_Bar<ST, S>(chart);
    } else {
      this.plotContent = new PlotContent_Category_Line_Area_Scatter<ST, S>(chart);
    }
  }
}
