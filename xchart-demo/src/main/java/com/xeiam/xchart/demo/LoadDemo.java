package com.xeiam.xchart.demo;

import java.util.ArrayList;
import java.util.List;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.ChartBuilder;
import com.xeiam.xchart.SwingWrapper;
import com.xeiam.xchart.StyleManager.LegendPosition;
import com.xeiam.xchart.demo.charts.ExampleChart;
import com.xeiam.xchart.demo.charts.line.LineChart01;

public class LoadDemo {
	public static void main(String[] args) {

		LoadDemo loadDemo = new LoadDemo();
		Chart chart = loadDemo.getChart();
		new SwingWrapper(chart).displayChart();
	}

	public Chart getChart() {

		// generates Log data
		List<Integer> xData = new ArrayList<Integer>();
		List<Double> yData = new ArrayList<Double>();
		for (int i = -3; i <= 3; i++) {
			xData.add(i);
			yData.add(Math.pow(10, i));
		}

		// Create Chart
		Chart chart = new ChartBuilder().width(800).height(600).build();
		chart.load("xAxisTitle=loaded x title SIN(width)+height:#SIN($width$)+$height$#;yAxisTitle=loaded y title;");

		// Customize Chart
		//chart.getStyleManager().setChartTitleVisible(false);
		chart.getStyleManager().setLegendPosition(LegendPosition.InsideNW);
		//chart.getStyleManager().setYAxisLogarithmic(true);
		chart.getStyleManager().load("xAxisMin=#-1+$markerSize$/4#;yAxisMax=100");

		// Series
		chart.addSeries("10^x", xData, yData);

		return chart;
	}

}
