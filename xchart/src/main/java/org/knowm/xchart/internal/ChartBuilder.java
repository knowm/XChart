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
package org.knowm.xchart.internal;

import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.style.Styler.ChartTheme;

/**
 * A "Builder" to make creating charts easier
 *
 * @author timmolter
 */
public abstract class ChartBuilder<T extends ChartBuilder<?, ?>, C extends Chart> {

  // ChartType chartType = ChartType.XY;
  public int width = 800;
  public int height = 600;
  public String title = "";

  public ChartTheme chartTheme = ChartTheme.XChart;

  /**
   * Constructor
   */
  public ChartBuilder() {
  }

  public T width(int width) {

    this.width = width;
    return (T) this;
  }

  public T height(int height) {

    this.height = height;
    return (T) this;
  }

  public T title(String title) {

    this.title = title;
    return (T) this;
  }

  public T theme(ChartTheme chartTheme) {

    this.chartTheme = chartTheme;
    return (T) this;
  }

  public abstract C build();

}
