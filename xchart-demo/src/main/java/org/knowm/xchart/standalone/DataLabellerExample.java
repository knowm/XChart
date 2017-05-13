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
package org.knowm.xchart.standalone;

import java.util.ArrayList;
import java.util.List;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.demo.charts.area.AreaLineChart03;
import org.knowm.xchart.style.label.DataLabeller;
import org.knowm.xchart.style.label.DataLabeller.DataLabelContent;

/**
 * Create a Chart matrix
 *
 * @author timmolter
 */
public class DataLabellerExample {

  public static void main(String[] args) {

	ExampleChart<XYChart> alc = new AreaLineChart03();
    List<XYChart> charts = new ArrayList<XYChart>();
    {
    	XYChart chart = alc.getChart();
    	chart.setTitle("Default data labels");
    	charts.add(chart);
    }
    {
    	XYChart chart = alc.getChart();
    	DataLabeller dataLabeller = chart.getDataLabeller();
    	dataLabeller.setShouldShowDataLabels(false);
    	chart.setTitle("No data label");
    	charts.add(chart);
    }
    {
    	//current default
    	XYChart chart = alc.getChart();
    	DataLabeller dataLabeller = chart.getDataLabeller();
    	dataLabeller.setShouldShowDataLabels(true);
    	dataLabeller.setDataLabelsAsToolTips(true);
    	chart.setTitle("Pop up data label");
    	charts.add(chart);
    }
    {
    	XYChart chart = alc.getChart();
    	DataLabeller dataLabeller = chart.getDataLabeller();
    	dataLabeller.setShouldShowDataLabels(true);
    	dataLabeller.setDataLabelsAsToolTips(false);
    	chart.setTitle("Static data label");
    	charts.add(chart);
    }
    {
    	XYChart chart = alc.getChart();
    	DataLabeller dataLabeller = chart.getDataLabeller();
    	dataLabeller.setShouldShowDataLabels(true);
    	dataLabeller.setDataLabelsAsToolTips(false);
    	dataLabeller.setPreventOverlap(true);
    	chart.setTitle("Static data label without overlapping");
    	charts.add(chart);
    }
    {
    	XYChart chart = alc.getChart();
    	DataLabeller dataLabeller = chart.getDataLabeller();
    	dataLabeller.setShouldShowDataLabels(true);
    	dataLabeller.setDataLabelsAsToolTips(false);
    	dataLabeller.setPreventOverlap(true);
    	dataLabeller.setDataLabelContent(DataLabelContent.yLabels);
    	chart.setTitle("Static data label without overlapping with y values only");
    	charts.add(chart);
    }
    
    new SwingWrapper<XYChart>(charts).displayChartMatrix();
  }
}
