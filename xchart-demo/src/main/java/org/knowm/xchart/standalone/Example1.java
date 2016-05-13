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

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.VectorGraphicsEncoder;
import org.knowm.xchart.VectorGraphicsEncoder.VectorGraphicsFormat;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.markers.SeriesMarkers;

/**
 * Creates a simple Chart and saves it as a PNG and JPEG image file.
 */
public class Example1 {

  public static void main(String[] args) throws Exception {

    double[] yData = new double[] { 2.0, 1.0, 0.0 };

    // Create Chart
    XYChart chart = new XYChart(500, 400);
    chart.setTitle("Sample Chart");
    chart.setXAxisTitle("X");
    chart.setXAxisTitle("Y");
    XYSeries series = chart.addSeries("y(x)", null, yData);
    series.setMarker(SeriesMarkers.CIRCLE);

    BitmapEncoder.saveBitmap(chart, "./Sample_Chart", BitmapFormat.PNG);
    BitmapEncoder.saveBitmap(chart, "./Sample_Chart", BitmapFormat.JPG);
    BitmapEncoder.saveJPGWithQuality(chart, "./Sample_Chart_With_Quality.jpg", 0.95f);
    BitmapEncoder.saveBitmap(chart, "./Sample_Chart", BitmapFormat.BMP);
    BitmapEncoder.saveBitmap(chart, "./Sample_Chart", BitmapFormat.GIF);

    BitmapEncoder.saveBitmapWithDPI(chart, "./Sample_Chart_300_DPI", BitmapFormat.PNG, 300);
    BitmapEncoder.saveBitmapWithDPI(chart, "./Sample_Chart_300_DPI", BitmapFormat.JPG, 300);
    BitmapEncoder.saveBitmapWithDPI(chart, "./Sample_Chart_300_DPI", BitmapFormat.GIF, 300);

    VectorGraphicsEncoder.saveVectorGraphic(chart, "./Sample_Chart", VectorGraphicsFormat.EPS);
    VectorGraphicsEncoder.saveVectorGraphic(chart, "./Sample_Chart", VectorGraphicsFormat.PDF);
    VectorGraphicsEncoder.saveVectorGraphic(chart, "./Sample_Chart", VectorGraphicsFormat.SVG);

  }
}
