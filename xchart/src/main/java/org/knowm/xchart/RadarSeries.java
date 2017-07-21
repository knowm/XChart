/**
 * Copyright 2015-2017 Knowm Inc. (http://knowm.org) and contributors.
 * Copyright 2011-2015 Xeiam LLC (http://xeiam.com) and contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.knowm.xchart;

import java.util.List;

import org.knowm.xchart.internal.chartpart.Axis.AxisDataType;
import org.knowm.xchart.internal.chartpart.RenderableSeries.LegendRenderType;
import org.knowm.xchart.internal.series.MarkersSeries;

/**
 * A Series containing Radar data to be plotted on a Chart
 *
 * @author timmolter
 */
public class RadarSeries extends MarkersSeries {

  LegendRenderType legendRenderType;

  protected double[] values;
  protected String[] toolTips;

  /**
   * 
   * @param toolTips Adds custom tool tips for series. If tool tip is null, it is automatically calculated.
   */

  public RadarSeries(String name, double[] values, String[] toolTips) {

    super(name, null, null, null);
    this.values = values;
    this.toolTips = toolTips;
  }
  
  @Override
  protected void calculateMinMax() {
  }
  
  public double[] getValues() {

    return values;
  }

  public void setValues(double[] values) {

    this.values = values;
  }

  @Override
  public AxisDataType getAxesType(List<?> data) {

    return AxisDataType.Number;
  }
  
  public void setLegendRenderType(LegendRenderType legendRenderType) {

    this.legendRenderType = legendRenderType;
  }
  
  @Override
  public LegendRenderType getLegendRenderType() {

    return legendRenderType;
  }

  public String[] getToolTips() {

    return toolTips;
  }
  
}
