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
package org.knowm.xchart.standalone.issues;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.CategorySeries.CategorySeriesRenderStyle;
import org.knowm.xchart.SwingWrapper;

/**
 * @author timmolter
 */
public class TestForIssue139 {

  public static void main(String[] args) {

    int[] x = new int[] { 0, 1, 2, 3, 4 };
    int[] a = new int[] { 1, 3, 1, 2, 1 };
    int[] b = new int[] { 2, 1, 1, 2, 2 };
    int[] c = new int[] { 1, 1, 2, 3, 3 };

    CategoryChart chart = new CategoryChartBuilder().width(640).height(480).build();

    chart.getStyler().setLegendVisible(false);
    chart.getStyler().setDefaultSeriesRenderStyle(CategorySeriesRenderStyle.Stick);
    // chart.getStyler().setBarsOverlapped(true);
    chart.getStyler().setAvailableSpaceFill(.25);

    chart.addSeries("A", x, a);
    chart.addSeries("B", x, b);
    chart.addSeries("C", x, c);

    new SwingWrapper(chart).displayChart();
  }

}
