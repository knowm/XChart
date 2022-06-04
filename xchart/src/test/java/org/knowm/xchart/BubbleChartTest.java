package org.knowm.xchart;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.knowm.xchart.style.Styler;

public class BubbleChartTest {
	BubbleChart bubbleChart;
	
	@Before
	public void setUp() throws Exception {
	    bubbleChart =
	            new BubbleChartBuilder()
	                .width(800)
	                .height(600)
	                .title(getClass().getSimpleName())
	                .xAxisTitle("X")
	                .yAxisTitle("Y")
	                .build();
	    bubbleChart.getStyler().setLegendPosition(Styler.LegendPosition.InsideN);
	    bubbleChart.getStyler().setLegendLayout(Styler.LegendLayout.Horizontal);
	    bubbleChart.getStyler().setToolTipsEnabled(true);
	}

	@After
	public void tearDown() throws Exception {
		bubbleChart = null;
	}
	
	/**
	* Purpose: Add different length of data for yData and BubbleData
	* Input: sanityCheck xData.length==5 yData.length==5 BubbleData.length==4
	* Expected:
	* throw IllegalArgumentException
	*/ 
	@Test(expected = IllegalArgumentException.class)
	public void sanityCheckDifferentLengthYBubbleTest() {
		assertNotNull(bubbleChart.addSeries("test", Arrays.asList(10, 40, 80, 120, 350), 
				Arrays.asList(10, 40, 80, 120, 350), Arrays.asList(10, 40, 80, 350)));
	}
	
	/**
	* Purpose: Add different length of data for xData and yData
	* Input: sanityCheck xData.length==4 yData.length==5 BubbleData.length==5
	* Expected:
	* throw IllegalArgumentException
	*/ 
	@Test(expected = IllegalArgumentException.class)
	public void sanityCheckDifferentLengthXYTest() {
		assertNotNull(bubbleChart.addSeries("test", new double[] {10, 40, 80, 350}, 
				new double[] {10, 40, 80, 120, 350}, new double[] {10, 40, 80, 120, 350}));
	}
	
	/**
	* Purpose: Add null BubbleData
	* Input: sanityCheck xData.length==5 yData.length==5 BubbleData==null
	* Expected:
	* throw IllegalArgumentException
	*/ 
	@Test(expected = IllegalArgumentException.class)
	public void sanityCheckNullBubbleData() {
		assertNotNull(bubbleChart.addSeries("test", new double[] {10, 40, 80, 120, 350}, 
				new double[] {10, 40, 80, 120, 350}, null));
	}
	
	/**
	* Purpose: Add zero length double array for BubbleData
	* Input: sanityCheck xData.length==5 yData.length==5 BubbleData.length==0
	* Expected:
	* throw IllegalArgumentException
	*/ 
	@Test(expected = IllegalArgumentException.class)
	public void sanityCheckZeroLengthBubbleTest() {
		double[] zeorLengthArr = new double[0];
		assertNotNull(bubbleChart.addSeries("test", new double[] {10, 40, 80, 120, 350}, 
				new double[] {10, 40, 80, 120, 350}, zeorLengthArr));
	}
	
	/**
	* Purpose: Add zero length double array for xData
	* Input: sanityCheck xData.length==0 yData.length==5 BubbleData.length==5
	* Expected:
	* throw IllegalArgumentException
	*/ 
	@Test(expected = IllegalArgumentException.class)
	public void sanityCheckZeroLengthXDataTest() {
		double[] zeorLengthArr = new double[0];
		assertNotNull(bubbleChart.addSeries("test", zeorLengthArr, 
				new double[] {10, 40, 80, 120, 350}, new double[] {10, 40, 80, 120, 350}));
	}
	
	/**
	* Purpose: Add null xData to generate xData.
	* Input: generateBubbleSeries xData==null
	* Expected:
	* return BubbleSeries
	*/ 
	@Test()
	public void generateBubbleSeriesTest() {
		assertNotNull(bubbleChart.addSeries("test", null, 
				Arrays.asList(10, 40, 80, 120, 350), Arrays.asList(10, 40, 80, 120, 350)));
	}
	
	/**
	* Purpose: Use null xData when updating series data.
	* Input: updateBubbleSeries xData==null, List<double>, List<double>
	* Expected:
	* return BubbleSeries
	*/ 
	@Test()
	public void updateBubbleSeriesWithNullXData() {
		assertNotNull(bubbleChart.addSeries("test", new double[] {10, 40, 80, 120, 350}, 
				new double[] {10, 40, 80, 120, 350}, new double[] {10, 40, 80, 120, 350}));
		assertNotNull(bubbleChart.updateBubbleSeries("test", null, 
				Arrays.asList(10, 40, 80, 120, 350), Arrays.asList(10, 40, 80, 120, 350)));
	}
}
