package org.knowm.xchart;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.*;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EmptyStackException;
import java.util.List;

import javax.swing.JFrame;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.knowm.xchart.internal.Utils;
import org.knowm.xchart.internal.series.Series.DataType;
import org.knowm.xchart.internal.style.SeriesColorMarkerLineStyle;
import org.knowm.xchart.internal.style.SeriesColorMarkerLineStyleCycler;
import org.knowm.xchart.style.RadarStyler;
import org.knowm.xchart.style.XYStyler;
import org.knowm.xchart.style.Styler.ChartTheme;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.knowm.xchart.style.theme.GGPlot2Theme;
import org.knowm.xchart.style.theme.Theme;

public class XYChartTest {
	
	/**
	* Purpose: test constructor XYChart(int width, int height)
	* Input: XYChart(int width, int height) put Width = 3, Height = 5
	* * Expected: Width = 3, Height = 5, Styler = XYStyler
	  * 
	*/
	@Test
	public void testConstructor1(){
		XYChart xyChart = new XYChart(3, 5);
		
		assertThat(xyChart.getWidth()).isEqualTo(3);
		assertThat(xyChart.getHeight()).isEqualTo(5);
		assertEquals((new XYStyler().getClass()), xyChart.getStyler().getClass());
	}
	
	/**
	* Purpose: test constructor XYChart(int width, int height, Theme theme)
	* Input: XYChart(int width, int height, Theme theme) put Width = 3, Height = 5, Theme = GGPlot2Theme
	* * Expected: Width = 3, Height = 5, Theme = GGPlot2Theme
	  * 
	*/
	@Test
	public void testConstructor2(){
		XYChart xyChart = new XYChart(3, 5, new GGPlot2Theme());
		
		assertThat(xyChart.getWidth()).isEqualTo(3);
		assertThat(xyChart.getHeight()).isEqualTo(5);
		assertEquals((new GGPlot2Theme().getClass()), xyChart.getStyler().getTheme().getClass());
	}
	
	/**
	* Purpose: test constructor XYChart(int width, int height, ChartTheme chartTheme)
	* Input: XYChart(int width, int height, ChartTheme chartTheme) put Width = 3, Height = 5, ChartTheme = GGPlot2
	* * Expected: Width = 3, Height = 5, Theme = GGPlot2Theme
	  * 
	*/
	@Test
	public void testConstructor3(){
		XYChart xyChart = new XYChart(3, 5, ChartTheme.GGPlot2);
		
		assertThat(xyChart.getWidth()).isEqualTo(3);
		assertThat(xyChart.getHeight()).isEqualTo(5);
		assertEquals((new GGPlot2Theme().getClass()), xyChart.getStyler().getTheme().getClass());
	}
	
	/**
	* Purpose: test constructor XYChart(XYChartBuilder chartBuilder)
	* Input: XYChart(XYChartBuilder chartBuilder) put width = 3, height = 5, charttheme = GGPlot2, title = "test", XAxisTitle = "x-test", YAxisTitle = "y-test" through XYChartBuilder
	* * Expected: Width = 3, Height = 5, Theme = GGPlot2Theme, Title = "test", XAxisTitle = "x-test", YAxisTitle = "y-test"
	  * 
	*/
	@Test
	public void testConstructor4(){
		XYChart xyChart = new XYChartBuilder().width(3).height(5).title("test").theme(ChartTheme.GGPlot2).xAxisTitle("x-test").yAxisTitle("y-test").build();
		
		assertThat(xyChart.getWidth()).isEqualTo(3);
		assertThat(xyChart.getHeight()).isEqualTo(5);
		assertThat(xyChart.getTitle()).isEqualTo("test");
		assertThat(xyChart.getXAxisTitle()).isEqualTo("x-test");
		assertThat(xyChart.getYAxisTitle()).isEqualTo("y-test");
		assertEquals((new GGPlot2Theme().getClass()), xyChart.getStyler().getTheme().getClass());
	}
	
	/**
	* Purpose: test addSeries addSeries(String seriesName, double[] yData)
	* Input: addSeries(String seriesName, double[] yData) put seriesName = "test", yData = [1.0, 2.0, 3.0]
	* * Expected: seriesName = "test", yData = [1.0, 2.0, 3.0]
	  * 
	*/
	@Test
	public void testAddSeriesDoubleExtension(){
		//List<Integer> xData = new ArrayList<>();
		double[] yData = {1.0, 2.0, 3.0};
		
		// Create Chart
		XYChart chart =
		    new XYChartBuilder()
		        .width(800)
		        .height(600)
		        .title(getClass().getSimpleName())
		        .xAxisTitle("Power")
		        .yAxisTitle("Value")
		        .build();
		
		// Customize Chart
		chart.getStyler().setChartTitleVisible(true);
		chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
		chart.getStyler().setYAxisLogarithmic(true);
		chart.getStyler().setXAxisLabelRotation(45);
		
		// Series
		chart.addSeries("test", yData);
		assertThat(chart.getSeriesMap().get("test").getYData()).isEqualTo(yData);
		assertThat(chart.getSeriesMap().get("test").getXData()).isEqualTo(new double[] {1.0, 2.0, 3.0});// if Xdata == null, Utils.getGeneratedDataAsArray(newYData.length);
		assertThat(chart.getSeriesMap().get("test").getxAxisDataType()).isEqualTo(DataType.Number);
		assertThat(chart.getSeriesMap().get("test").getyAxisDataType()).isEqualTo(DataType.Number);
		assertThat(chart.getSeriesMap().get("test").getExtraValues()).isEqualTo(null);
	}
	
	/**
	* Purpose: test addSeries XYSeries addSeries(String seriesName, double[] xData, double[] yData)
	* Input: XYSeries addSeries(String seriesName, double[] xData, double[] yData) put seriesName = "test", xData = [3.0, 4.0, 5.0], yData = [1.0, 2.0, 3.0]
	* * Expected: seriesName = "test", yData = [1.0, 2.0, 3.0]
	  * 
	*/
	@Test
	public void testAddSeriesDouble(){
		//List<Integer> xData = new ArrayList<>();
		double[] xData = {3.0, 4.0, 5.0};
		double[] yData = {1.0, 2.0, 3.0};
		
		// Create Chart
		XYChart chart =
		    new XYChartBuilder()
		        .width(800)
		        .height(600)
		        .title(getClass().getSimpleName())
		        .xAxisTitle("Power")
		        .yAxisTitle("Value")
		        .build();
		
		// Customize Chart
		chart.getStyler().setChartTitleVisible(true);
		chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
		chart.getStyler().setYAxisLogarithmic(true);
		chart.getStyler().setXAxisLabelRotation(45);
		
		// Series
		chart.addSeries("test", xData ,yData);
		assertThat(chart.getSeriesMap().get("test").getYData()).isEqualTo(yData);
		assertThat(chart.getSeriesMap().get("test").getXData()).isEqualTo(xData);
		assertThat(chart.getSeriesMap().get("test").getxAxisDataType()).isEqualTo(DataType.Number);
		assertThat(chart.getSeriesMap().get("test").getyAxisDataType()).isEqualTo(DataType.Number);
		assertThat(chart.getSeriesMap().get("test").getExtraValues()).isEqualTo(null);
	}
	
	/**
	* Purpose: test addSeries addSeries(String seriesName, float[] yData)
	* Input: addSeries(String seriesName, float[] yData) put seriesName = "test", yData = [1.0f, 2.0f, 3.0f]
	* * Expected: seriesName = "test", yData = [1.0, 2.0, 3.0]
	  * 
	*/
	@Test
	public void testAddSeriesFloatExtension1(){
		//List<Integer> xData = new ArrayList<>();
		float[] yData = {1.0f, 2.0f, 3.0f};
		double[] yDataDouble = {1.0, 2.0, 3.0};
		
		// Create Chart
		XYChart chart =
		    new XYChartBuilder()
		        .width(800)
		        .height(600)
		        .title(getClass().getSimpleName())
		        .xAxisTitle("Power")
		        .yAxisTitle("Value")
		        .build();
		
		// Customize Chart
		chart.getStyler().setChartTitleVisible(true);
		chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
		chart.getStyler().setYAxisLogarithmic(true);
		chart.getStyler().setXAxisLabelRotation(45);
		
		// Series
		chart.addSeries("test", yData);
		assertThat(chart.getSeriesMap().get("test").getYData()).isEqualTo(yDataDouble);//if Data == float, convert from float to double
		assertThat(chart.getSeriesMap().get("test").getXData()).isEqualTo(new double[] {1.0, 2.0, 3.0});// if Xdata == null, Utils.getGeneratedDataAsArray(newYData.length);
		assertThat(chart.getSeriesMap().get("test").getxAxisDataType()).isEqualTo(DataType.Number);
		assertThat(chart.getSeriesMap().get("test").getyAxisDataType()).isEqualTo(DataType.Number);
		assertThat(chart.getSeriesMap().get("test").getExtraValues()).isEqualTo(null);
	}
	
	/**
	* Purpose: test addSeries addSeries(String seriesName, float[] xData, float[] yData)
	* Input: addSeries(String seriesName, float[] xData, float[] yData) put seriesName = "test", xData = [3.0f, 4.0f, 5.0f], yData = [1.0f, 2.0f, 3.0f]
	* * Expected: seriesName = "test", xData = [3.0, 4.0, 5.0], yData = [1.0, 2.0, 3.0]
	  * 
	*/
	@Test
	public void testAddSeriesFloatExtension2(){
		//List<Integer> xData = new ArrayList<>();
		float[] xData = {3.0f, 4.0f, 5.0f};
		double[] xDataDouble = {3.0, 4.0, 5.0};
		float[] yData = {1.0f, 2.0f, 3.0f};
		double[] yDataDouble = {1.0, 2.0, 3.0};
		
		// Create Chart
		XYChart chart =
		    new XYChartBuilder()
		        .width(800)
		        .height(600)
		        .title(getClass().getSimpleName())
		        .xAxisTitle("Power")
		        .yAxisTitle("Value")
		        .build();
		
		// Customize Chart
		chart.getStyler().setChartTitleVisible(true);
		chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
		chart.getStyler().setYAxisLogarithmic(true);
		chart.getStyler().setXAxisLabelRotation(45);
		
		// Series
		chart.addSeries("test",xData, yData);
		assertThat(chart.getSeriesMap().get("test").getYData()).isEqualTo(yDataDouble);//if yData == float, convert from float to double
		assertThat(chart.getSeriesMap().get("test").getXData()).isEqualTo(xDataDouble);// if xData == float, convert from float to double
		assertThat(chart.getSeriesMap().get("test").getxAxisDataType()).isEqualTo(DataType.Number);
		assertThat(chart.getSeriesMap().get("test").getyAxisDataType()).isEqualTo(DataType.Number);
		assertThat(chart.getSeriesMap().get("test").getExtraValues()).isEqualTo(null);
	}
	
	/**
	* Purpose: test addSeries addSeries(String seriesName, float[] xData, float[] yData, float[] errorBars)
	* Input: addSeries(String seriesName, float[] xData, float[] yData, float[] errorBars) put seriesName = "test", xData = [3.0f, 4.0f, 5.0f], yData = [1.0f, 2.0f, 3.0f], errorBarsData = [5.0f, 6.0f, 7.0f]
	* * Expected: seriesName = "test", xData = [3.0, 4.0, 5.0], yData = [1.0, 2.0, 3.0], errorBarsData = [5.0, 6.0, 7.0]
	  * 
	*/
	@Test
	public void testAddSeriesFloat(){
		//List<Integer> xData = new ArrayList<>();
		float[] xData = {3.0f, 4.0f, 5.0f};
		double[] xDataDouble = {3.0, 4.0, 5.0};
		float[] yData = {1.0f, 2.0f, 3.0f};
		double[] yDataDouble = {1.0, 2.0, 3.0};
		float[] errorBarsData = {5.0f, 6.0f, 7.0f};
		double[] errorBarsDataDouble = {5.0, 6.0, 7.0};
		
		// Create Chart
		XYChart chart =
		    new XYChartBuilder()
		        .width(800)
		        .height(600)
		        .title(getClass().getSimpleName())
		        .xAxisTitle("Power")
		        .yAxisTitle("Value")
		        .build();
		
		// Customize Chart
		chart.getStyler().setChartTitleVisible(true);
		chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
		chart.getStyler().setYAxisLogarithmic(true);
		chart.getStyler().setXAxisLabelRotation(45);
		
		// Series
		chart.addSeries("test",xData, yData, errorBarsData);
		assertThat(chart.getSeriesMap().get("test").getYData()).isEqualTo(yDataDouble);//if yData == float, convert from float to double
		assertThat(chart.getSeriesMap().get("test").getXData()).isEqualTo(xDataDouble);// if xData == float, convert from float to double
		assertThat(chart.getSeriesMap().get("test").getxAxisDataType()).isEqualTo(DataType.Number);
		assertThat(chart.getSeriesMap().get("test").getyAxisDataType()).isEqualTo(DataType.Number);
		assertThat(chart.getSeriesMap().get("test").getExtraValues()).isEqualTo(errorBarsDataDouble);// if errorBarsData == float, convert from float to double
	}
	
	/**
	* Purpose: test addSeries addSeries(String seriesName, int[] yData)
	* Input: addSeries(String seriesName, int[] yData) put seriesName = "test", yData = [1, 2, 3]
	* * Expected: seriesName = "test", yData = [1.0, 2.0, 3.0]
	  * 
	*/
	@Test
	public void testAddSeriesIntExtension1(){
		//List<Integer> xData = new ArrayList<>();
		int[] yData = {1, 2, 3};
		double[] yDataDouble = {1.0, 2.0, 3.0};
		
		// Create Chart
		XYChart chart =
		    new XYChartBuilder()
		        .width(800)
		        .height(600)
		        .title(getClass().getSimpleName())
		        .xAxisTitle("Power")
		        .yAxisTitle("Value")
		        .build();
		
		// Customize Chart
		chart.getStyler().setChartTitleVisible(true);
		chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
		chart.getStyler().setYAxisLogarithmic(true);
		chart.getStyler().setXAxisLabelRotation(45);
		
		// Series
		chart.addSeries("test", yData);
		assertThat(chart.getSeriesMap().get("test").getYData()).isEqualTo(yDataDouble);//if Data == int, convert from int to double
		assertThat(chart.getSeriesMap().get("test").getXData()).isEqualTo(new double[] {1.0, 2.0, 3.0});// if Xdata == null, Utils.getGeneratedDataAsArray(newYData.length);
		assertThat(chart.getSeriesMap().get("test").getxAxisDataType()).isEqualTo(DataType.Number);
		assertThat(chart.getSeriesMap().get("test").getyAxisDataType()).isEqualTo(DataType.Number);
		assertThat(chart.getSeriesMap().get("test").getExtraValues()).isEqualTo(null);
	}
	
	/**
	* Purpose: test addSeries addSeries(String seriesName, int[] xData, int[] yData)
	* Input: addSeries(String seriesName, int[] xData, int[] yData) put seriesName = "test", xData = [3, 4, 5], yData = [1, 2, 3]
	* * Expected: seriesName = "test", xData = [3.0, 4.0, 5.0], yData = [1.0, 2.0, 3.0]
	  * 
	*/
	@Test
	public void testAddSeriesIntExtension2(){
		//List<Integer> xData = new ArrayList<>();
		int[] xData = {3, 4, 5};
		double[] xDataDouble = {3.0, 4.0, 5.0};
		int[] yData = {1, 2, 3};
		double[] yDataDouble = {1.0, 2.0, 3.0};
		
		// Create Chart
		XYChart chart =
		    new XYChartBuilder()
		        .width(800)
		        .height(600)
		        .title(getClass().getSimpleName())
		        .xAxisTitle("Power")
		        .yAxisTitle("Value")
		        .build();
		
		// Customize Chart
		chart.getStyler().setChartTitleVisible(true);
		chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
		chart.getStyler().setYAxisLogarithmic(true);
		chart.getStyler().setXAxisLabelRotation(45);
		
		// Series
		chart.addSeries("test",xData, yData);
		assertThat(chart.getSeriesMap().get("test").getYData()).isEqualTo(yDataDouble);//if yData == int, convert from int to double
		assertThat(chart.getSeriesMap().get("test").getXData()).isEqualTo(xDataDouble);// if xData == int, convert from int to double
		assertThat(chart.getSeriesMap().get("test").getxAxisDataType()).isEqualTo(DataType.Number);
		assertThat(chart.getSeriesMap().get("test").getyAxisDataType()).isEqualTo(DataType.Number);
		assertThat(chart.getSeriesMap().get("test").getExtraValues()).isEqualTo(null);
	}
	
	/**
	* Purpose: test addSeries addSeries(String seriesName, int[] xData, int[] yData, int[] errorBars)
	* Input: addSeries(String seriesName, int[] xData, int[] yData, int[] errorBars) put seriesName = "test", xData = [3, 4, 5], yData = [1, 2, 3], errorBarsData = [5, 6, 7]
	* * Expected: seriesName = "test", xData = [3.0, 4.0, 5.0], yData = [1.0, 2.0, 3.0], errorBarsData = [5.0, 6.0, 7.0]
	  * 
	*/
	@Test
	public void testAddSeriesInt(){
		//List<Integer> xData = new ArrayList<>();
		int[] xData = {3, 4, 5};
		double[] xDataDouble = {3.0, 4.0, 5.0};
		int[] yData = {1, 2, 3};
		double[] yDataDouble = {1.0, 2.0, 3.0};
		int[] errorBarsData = {5, 6, 7};
		double[] errorBarsDataDouble = {5.0, 6.0, 7.0};
		
		// Create Chart
		XYChart chart =
		    new XYChartBuilder()
		        .width(800)
		        .height(600)
		        .title(getClass().getSimpleName())
		        .xAxisTitle("Power")
		        .yAxisTitle("Value")
		        .build();
		
		// Customize Chart
		chart.getStyler().setChartTitleVisible(true);
		chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
		chart.getStyler().setYAxisLogarithmic(true);
		chart.getStyler().setXAxisLabelRotation(45);
		
		// Series
		chart.addSeries("test",xData, yData, errorBarsData);
		assertThat(chart.getSeriesMap().get("test").getYData()).isEqualTo(yDataDouble);//if yData == int, convert from int to double
		assertThat(chart.getSeriesMap().get("test").getXData()).isEqualTo(xDataDouble);// if xData == int, convert from int to double
		assertThat(chart.getSeriesMap().get("test").getxAxisDataType()).isEqualTo(DataType.Number);
		assertThat(chart.getSeriesMap().get("test").getyAxisDataType()).isEqualTo(DataType.Number);
		assertThat(chart.getSeriesMap().get("test").getExtraValues()).isEqualTo(errorBarsDataDouble);// if errorBarsData == int, convert from int to double
	}
	
	/**
	* Purpose: test addSeries(String seriesName, List<?> xData, List<? extends Number> yData)
	* Input: addSeries(String seriesName, List<?> xData, List<? extends Number> yData) put seriesName = "test", xData = [-3, -2, -1, 0], yData = [0.001, 0.01, 0.1, 1.0]
	* * Expected: seriesName = "test", xData = [-3.0, -2.0, -1.0, 0.0], yData = [0.001, 0.01, 0.1, 1.0]
	  * 
	*/
	@Test
	public void testAddSeriesListNumberCase(){
		List<Integer> xData = new ArrayList<>();
	    List<Double> yData = new ArrayList<>();
	    for (int i = -3; i <= 0; i++) {
	      xData.add(i);
	      yData.add(Math.pow(10, i));
	    }
	    double[] xDataDouble = {-3.0, -2.0, -1.0, 0.0};
	    double[] yDataDouble = {0.001, 0.01, 0.1, 1.0};
	    
		
		// Create Chart
		XYChart chart =
		    new XYChartBuilder()
		        .width(800)
		        .height(600)
		        .title(getClass().getSimpleName())
		        .xAxisTitle("Power")
		        .yAxisTitle("Value")
		        .build();
		
		// Customize Chart
		chart.getStyler().setChartTitleVisible(true);
		chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
		chart.getStyler().setYAxisLogarithmic(true);
		chart.getStyler().setXAxisLabelRotation(45);
		
		// Series
		chart.addSeries("test",xData, yData);
		assertThat(chart.getSeriesMap().get("test").getYData()).isEqualTo(yDataDouble);//if yData == NumberList, convert from NumberList to double[]
		assertThat(chart.getSeriesMap().get("test").getXData()).isEqualTo(xDataDouble);// if xData == NumberList, convert from int to double[]
		assertThat(chart.getSeriesMap().get("test").getxAxisDataType()).isEqualTo(DataType.Number);
		assertThat(chart.getSeriesMap().get("test").getyAxisDataType()).isEqualTo(DataType.Number);
		assertThat(chart.getSeriesMap().get("test").getExtraValues()).isEqualTo(null);
	}
	
	/**
	* Purpose: test XYSeries addSeries(String seriesName, List<?> xData, List<? extends Number> yData, List<? extends Number> errorBars) when xData type is Date
	* Input: addSeries(String seriesName, List<?> xData, List<? extends Number> yData, List<? extends Number> errorBars) put seriesName = "test", xData = [1 + ".10.2008", 2 + ".10.2008", 3 + ".10.2008"], yData = [1.0, 2.0, 3.0]
	* * Expected: seriesName = "test", xAxisDataType = DataType.Date, yData = [1.0, 2.0, 3.0]
	  * 
	*/
	@Test
	public void testAddSeriesListDateCase(){
		List<Date> xData = new ArrayList<Date>();
	    List<Double> yData = new ArrayList<Double>();

	    DateFormat simpleDataFormat = new SimpleDateFormat("dd.MM.yyyy");
	    Date date = null;
	    for (int i = 1; i <= 3; i++) {

	      try {
	        date = simpleDataFormat.parse(i + ".10.2008");
	      } catch (ParseException e) {
	        e.printStackTrace();
	      }
	      xData.add(date);
	      yData.add((double)i);
	    }
	    
	    double[] yDataDouble = {1.0, 2.0, 3.0};
	    try {
	    	double[] xDataDouble = {((Date)simpleDataFormat.parse(1 + ".10.2008")).getTime(),((Date)simpleDataFormat.parse(2 + ".10.2008")).getTime(),((Date)simpleDataFormat.parse(3 + ".10.2008")).getTime()};
	     
	    	// Create Chart
			XYChart chart =
			    new XYChartBuilder()
			        .width(800)
			        .height(600)
			        .title(getClass().getSimpleName())
			        .xAxisTitle("Power")
			        .yAxisTitle("Value")
			        .build();
			
			// Customize Chart
			chart.getStyler().setChartTitleVisible(true);
			chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
			chart.getStyler().setYAxisLogarithmic(true);
			chart.getStyler().setXAxisLabelRotation(45);
			
			// Series
			chart.addSeries("test",xData, yData);
			assertThat(chart.getSeriesMap().get("test").getYData()).isEqualTo(yDataDouble);
			assertThat(chart.getSeriesMap().get("test").getXData()).isEqualTo(xDataDouble);
			assertThat(chart.getSeriesMap().get("test").getxAxisDataType()).isEqualTo(DataType.Date);
			assertThat(chart.getSeriesMap().get("test").getyAxisDataType()).isEqualTo(DataType.Number);
			assertThat(chart.getSeriesMap().get("test").getExtraValues()).isEqualTo(null);
	    
	    } catch (ParseException e) {
	        e.printStackTrace();
	      }
	}
	
	/**
	* Purpose: test addSeries(String seriesName, List<?> xData, List<? extends Number> yData)
	* Input: addSeries(String seriesName, List<?> xData, List<? extends Number> yData) put seriesName = "test", xData = [-3, -2, -1, 0], yData = [0.001, 0.01, 0.1, 1.0]
	* * Expected: seriesName = "test", xData = [-3.0, -2.0, -1.0, 0.0], yData = [0.001, 0.01, 0.1, 1.0]
	  * 
	*/
	@Test
	public void testAddSeriesListWithoutXData(){
	    List<Double> yData = new ArrayList<>();
	    for (int i = -3; i <= 0; i++) {
	      yData.add(Math.pow(10, i));
	    }
	    double[] xDataDouble = {1.0, 2.0, 3.0, 4.0};
	    double[] yDataDouble = {0.001, 0.01, 0.1, 1.0};
	    
		
		// Create Chart
		XYChart chart =
		    new XYChartBuilder()
		        .width(800)
		        .height(600)
		        .title(getClass().getSimpleName())
		        .xAxisTitle("Power")
		        .yAxisTitle("Value")
		        .build();
		
		// Customize Chart
		chart.getStyler().setChartTitleVisible(true);
		chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
		chart.getStyler().setYAxisLogarithmic(true);
		chart.getStyler().setXAxisLabelRotation(45);
		
		// Series
		chart.addSeries("test", yData);
		assertThat(chart.getSeriesMap().get("test").getYData()).isEqualTo(yDataDouble);//if yData == NumberList, convert from NumberList to double[]
		assertThat(chart.getSeriesMap().get("test").getXData()).isEqualTo(xDataDouble);// if xData == null, execute Utils.getGeneratedDataAsArray(yData.length)
		assertThat(chart.getSeriesMap().get("test").getxAxisDataType()).isEqualTo(DataType.Number);
		assertThat(chart.getSeriesMap().get("test").getyAxisDataType()).isEqualTo(DataType.Number);
		assertThat(chart.getSeriesMap().get("test").getExtraValues()).isEqualTo(null);
	}
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	/**
	* Purpose: test getDataType(List<?> data)
	* Input: getDataType(List<?> data) when neither dataType is DataType.Date nor DataType.Number
	* * Expected: throw new IllegalArgumentException("Series data must be either Number or Date type!!!");
	  * 
	*/
	@Test
	public void testGetDataType(){
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Series data must be either Number or Date type!!!");
		
		List<String> xData = new ArrayList<>();
	    List<Double> yData = new ArrayList<>();
	    for (int i = -3; i <= 0; i++) {
	      xData.add(Integer.toString(i));
	      yData.add(Math.pow(10, i));
	    }
	    double[] yDataDouble = {0.001, 0.01, 0.1, 1.0};
	    
		// Create Chart
		XYChart chart =
		    new XYChartBuilder()
		        .width(800)
		        .height(600)
		        .title(getClass().getSimpleName())
		        .xAxisTitle("Power")
		        .yAxisTitle("Value")
		        .build();
		
		// Customize Chart
		chart.getStyler().setChartTitleVisible(true);
		chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
		chart.getStyler().setYAxisLogarithmic(true);
		chart.getStyler().setXAxisLabelRotation(45);
		
		// Series
		chart.addSeries("test",xData, yData);	
	}
	
	/**
	* Purpose: test addSeries addSeries(String seriesName, double[] xData, double[] yData, double[] errorBars)
	* Input: addSeries(String seriesName, double[] xData, double[] yData, double[] errorBars) put seriesName = "test", xData = [3.0, 4.0, 5.0], yData = [1.0, 2.0, 3.0], errorBars = [5.0, 6.0, 7.0]
	* * Expected: seriesName = "test", xData = [3.0, 4.0, 5.0], yData = [1.0, 2.0, 3.0], errorBars = [5.0, 6.0, 7.0]
	  * 
	*/
	@Test
	public void testAddSeriesDoubleExtension2(){
		//List<Integer> xData = new ArrayList<>();
		double[] xData = {3.0, 4.0, 5.0};
		double[] yData = {1.0, 2.0, 3.0};
		double[] errorBarsData = {5.0, 6.0, 7.0};
		
		// Create Chart
		XYChart chart =
		    new XYChartBuilder()
		        .width(800)
		        .height(600)
		        .title(getClass().getSimpleName())
		        .xAxisTitle("Power")
		        .yAxisTitle("Value")
		        .build();
		
		// Customize Chart
		chart.getStyler().setChartTitleVisible(true);
		chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
		chart.getStyler().setYAxisLogarithmic(true);
		chart.getStyler().setXAxisLabelRotation(45);
		
		// Series
		chart.addSeries("test", xData, yData, errorBarsData);
		assertThat(chart.getSeriesMap().get("test").getYData()).isEqualTo(yData);
		assertThat(chart.getSeriesMap().get("test").getXData()).isEqualTo(xData);
		assertThat(chart.getSeriesMap().get("test").getxAxisDataType()).isEqualTo(DataType.Number);
		assertThat(chart.getSeriesMap().get("test").getyAxisDataType()).isEqualTo(DataType.Number);
		assertThat(chart.getSeriesMap().get("test").getExtraValues()).isEqualTo(errorBarsData);
	}

}