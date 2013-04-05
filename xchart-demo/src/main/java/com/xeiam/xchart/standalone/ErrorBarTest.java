/**
 * Copyright (C) 2013 Xeiam LLC http://xeiam.com
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.xeiam.xchart.standalone;

import java.awt.Color;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.Series;
import com.xeiam.xchart.SeriesColor;
import com.xeiam.xchart.SeriesLineStyle;
import com.xeiam.xchart.SeriesMarker;
import com.xeiam.xchart.SwingWrapper;

/**
 * @author timmolter
 */
public class ErrorBarTest {

  public static void main(String[] args) throws Exception {

    double[] xData = new double[] { 0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0 };
    double[] yData1 = new double[] { 100, 100, 100, 50, 50, 50, 50 };
    double[] errdata = new double[] { 50, 20, 10, 50, 40, 20, 10 };

    double[] yData2 = new double[] { 50, 80, 90, 0, 10, 30, 40 };
    double[] yData3 = new double[] { 150, 120, 110, 100, 90, 70, 60 };

    Chart mychart = new Chart(900, 700);
    mychart.getStyleManager().setYAxisMin(0);
    mychart.getStyleManager().setYAxisMax(150);
    mychart.getStyleManager().setErrorBarsColor(Color.black);
    Series series1 = mychart.addSeries("Error bar test data", xData, yData1, errdata);
    Series series2 = mychart.addSeries("Y+error", xData, yData2);
    Series series3 = mychart.addSeries("Y-error", xData, yData3);
    series1.setLineStyle(SeriesLineStyle.SOLID);
    series1.setMarker(SeriesMarker.DIAMOND);
    series1.setMarkerColor(Color.MAGENTA);
    series2.setLineStyle(SeriesLineStyle.DASH_DASH);
    series2.setMarker(SeriesMarker.NONE);
    series2.setLineColor(SeriesColor.RED);
    series3.setLineStyle(SeriesLineStyle.DASH_DASH);
    series3.setMarker(SeriesMarker.NONE);
    series3.setLineColor(SeriesColor.RED);

    new SwingWrapper(mychart).displayChart();
  }

}
