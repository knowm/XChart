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
package org.knowm.xchart.standalone;

import java.io.IOException;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.PieStyler.AnnotationType;
import org.knowm.xchart.style.Styler.ChartTheme;

/**
 * @author timmolter
 */
public class PieChartDemo {

  public static void main(String[] args) throws IOException {

    // Create Chart
    PieChart chart = new PieChartBuilder().width(800).height(600).title("My Pie Chart").theme(ChartTheme.GGPlot2).build();

    // Customize Chart
    chart.getStyler().setLegendVisible(false);
    chart.getStyler().setAnnotationType(AnnotationType.LabelAndPercentage);
    chart.getStyler().setAnnotationDistance(1.15);
    chart.getStyler().setPlotContentSize(.7);
    chart.getStyler().setStartAngleInDegrees(90);

    // Series
    chart.addSeries("Prague", 2);
    chart.addSeries("Dresden", 4);
    chart.addSeries("Munich", 34);
    chart.addSeries("Hamburg", 22);
    chart.addSeries("Berlin", 29);

    // Show it
    new SwingWrapper(chart).displayChart();

    // Save it
    BitmapEncoder.saveBitmap(chart, "./Sample_Chart", BitmapFormat.PNG);

    // or save it in high-res
    BitmapEncoder.saveBitmapWithDPI(chart, "./Sample_Chart_300_DPI", BitmapFormat.PNG, 300);
  }

}
