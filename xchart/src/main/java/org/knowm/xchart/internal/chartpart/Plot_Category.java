/**
 * Copyright 2015-2016 Knowm Inc. (http://knowm.org) and contributors.
 * Copyright 2011-2015 Xeiam LLC (http://xeiam.com) and contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.knowm.xchart.internal.chartpart;

import java.awt.Graphics2D;

import org.knowm.xchart.CategorySeries;
import org.knowm.xchart.CategorySeries.CategorySeriesRenderStyle;
import org.knowm.xchart.internal.Series;
import org.knowm.xchart.style.AxesChartStyler;
import org.knowm.xchart.style.CategoryStyler;

/**
 * @author timmolter
 */
public class Plot_Category<ST extends AxesChartStyler, S extends Series> extends Plot_AxesChart {

  CategoryStyler stylerCategory;

  /**
   * Constructor
   *
   * @param chart
   */
  public Plot_Category(Chart<CategoryStyler, CategorySeries> chart) {

    super(chart);
    stylerCategory = chart.getStyler();
  }

  @Override
  public void paint(Graphics2D g) {

    if (CategorySeriesRenderStyle.Bar.equals(stylerCategory.getDefaultSeriesRenderStyle()) || CategorySeriesRenderStyle.Stick.equals(stylerCategory.getDefaultSeriesRenderStyle())) {

      this.plotContent = new PlotContent_Category_Bar<CategoryStyler, CategorySeries>(chart);
    }
    else {
      this.plotContent = new PlotContent_Category_Line_Area_Scatter<CategoryStyler, CategorySeries>(chart);
    }

    super.paint(g);
  }

}
