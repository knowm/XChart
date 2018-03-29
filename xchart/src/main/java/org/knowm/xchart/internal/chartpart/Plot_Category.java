package org.knowm.xchart.internal.chartpart;

import java.awt.*;
import org.knowm.xchart.CategorySeries;
import org.knowm.xchart.CategorySeries.CategorySeriesRenderStyle;
import org.knowm.xchart.style.CategoryStyler;

/** @author timmolter */
public class Plot_Category<ST extends CategoryStyler, S extends CategorySeries>
    extends Plot_AxesChart<ST, S> {

  private final ST stylerCategory;

  /**
   * Constructor
   *
   * @param chart
   */
  public Plot_Category(Chart<ST, S> chart) {

    super(chart);
    stylerCategory = chart.getStyler();
  }

  @Override
  public void paint(Graphics2D g) {

    if (CategorySeriesRenderStyle.Bar.equals(stylerCategory.getDefaultSeriesRenderStyle())
        || CategorySeriesRenderStyle.Stick.equals(stylerCategory.getDefaultSeriesRenderStyle())) {
      this.plotContent = new PlotContent_Category_Bar<ST, S>(chart);
    } else {
      this.plotContent = new PlotContent_Category_Line_Area_Scatter<ST, S>(chart);
    }

    super.paint(g);
  }
}
