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
package com.xeiam.xchart.demo;

import com.xeiam.xchart.Chart;

/**
 * @author timmolter
 */
public final class ChartInfo {

  private final String exampleChartName;
  private final Chart exampleChart;

  /**
   * Constructor
   * 
   * @param exampleChartName
   * @param exampleChart
   */
  public ChartInfo(String exampleChartName, Chart exampleChart) {

    this.exampleChartName = exampleChartName;
    this.exampleChart = exampleChart;
  }

  public String getExampleChartName() {

    return exampleChartName;
  }

  public Chart getExampleChart() {

    return exampleChart;
  }

  @Override
  public String toString() {

    return this.exampleChartName;
  }

}
