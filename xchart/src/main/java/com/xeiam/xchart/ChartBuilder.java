/**
 * Copyright 2011 - 2014 Xeiam LLC.
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
package com.xeiam.xchart;

import com.xeiam.xchart.StyleManager.ChartTheme;
import com.xeiam.xchart.StyleManager.ChartType;

/**
 * A "Builder" to make creating charts easier
 * 
 * @author timmolter
 */
public class ChartBuilder {

  ChartType chartType = ChartType.Line;
  int width = 800;
  int height = 600;
  String title = "";
  String xAxisTitle = "";
  String yAxisTitle = "";
  ChartTheme chartTheme = ChartTheme.XChart;

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

  public ChartBuilder theme(ChartTheme chartTheme) {

    this.chartTheme = chartTheme;
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
