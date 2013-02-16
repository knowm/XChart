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
package com.xeiam.xchart;

import com.xeiam.xchart.style.StyleManager.ChartType;
import com.xeiam.xchart.style.theme.Theme;
import com.xeiam.xchart.style.theme.XChartTheme;

/**
 * @author timmolter
 */
public class ChartBuilder {

  protected ChartType chartType = ChartType.Line;
  protected int width = 800;
  protected int height = 600;
  protected String title = "";
  protected String xAxisTitle = "";
  protected String yAxisTitle = "";
  protected Theme theme = new XChartTheme();

  public ChartBuilder chartType(ChartType chartType) {

    this.chartType = chartType;
    return this;
  }

  public ChartBuilder width(int width) {

    this.width = width;
    return this;
  }

  public ChartBuilder height(int height) {

    this.height = height;
    return this;
  }

  public ChartBuilder title(String title) {

    this.title = title;
    return this;
  }

  public ChartBuilder xAxisTitle(String xAxisTitle) {

    this.xAxisTitle = xAxisTitle;
    return this;
  }

  public ChartBuilder yAxisTitle(String yAxisTitle) {

    this.yAxisTitle = yAxisTitle;
    return this;
  }

  public ChartBuilder theme(Theme theme) {

    this.theme = theme;
    return this;
  }

  /**
   * return fully built Chart
   * 
   * @return a Chart
   */
  public Chart build() {

    return new Chart(this);

  }

}
