package org.knowm.xchart;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.awt.Graphics2D;
import java.util.Arrays;

import javax.swing.JFrame;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.knowm.xchart.BubbleSeries.BubbleSeriesRenderStyle;
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
	* Purpose: Add zero length double array for yData
	* Input: sanityCheck xData.length==5 yData.length==0 BubbleData.length==5
	* Expected:
	* throw IllegalArgumentException
	*/ 
	@Test(expected = IllegalArgumentException.class)
	public void sanityCheckZeroLengthYDataTest() {
		double[] zeorLengthArr = new double[0];
		assertNotNull(bubbleChart.addSeries("test", new double[] {10, 40, 80, 120, 350}, 
				zeorLengthArr, new double[] {10, 40, 80, 120, 350}));
	}
	
	/**
	* Purpose: Add null for yData
	* Input: sanityCheck xData.length==5 yData.length==null BubbleData.length==5
	* Expected:
	* throw IllegalArgumentException
	*/ 
	@Test(expected = IllegalArgumentException.class)
	public void sanityCheckNullYDataTest() {
		assertNotNull(bubbleChart.addSeries("test", new double[] {10, 40, 80, 120, 350}, 
				null, new double[] {10, 40, 80, 120, 350}));
	}
	
	/**
	* Purpose: Test name duplicate sanity checker inside addSeries method.
	* Input: seriesNameDuplicateCheck seriesMap.containsKey("test"), seriesMap.containsKey("test")
	* Expected:
	* throw IllegalArgumentException
	* First add. return BubbleSeries
	* Second add. throw exception
	*/ 
	@Test(expected = IllegalArgumentException.class)
	public void seriesNameDuplicateTest() {
		assertNotNull(bubbleChart.addSeries("test", new double[] {10, 40, 80, 120, 350}, 
				new double[] {10, 40, 80, 120, 350}, new double[] {10, 40, 80, 120, 350}));
		assertNotNull(bubbleChart.addSeries("test", new double[] {10, 40, 80, 120, 350}, 
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
	
	/**
	* Purpose: Use different series name when updating series data.
	* Input: checkSeriesValidity String "test" String "test3"
	* Expected:
	* throw IllegalArgumentException
	*/ 
	@Test(expected = IllegalArgumentException.class)
	public void checkSeriesValidityTest() {
		assertNotNull(bubbleChart.addSeries("test", new double[] {10, 40, 80, 120, 350}, 
				new double[] {10, 40, 80, 120, 350}, new double[] {10, 40, 80, 120, 350}));
		assertNotNull(bubbleChart.updateBubbleSeries("test3", Arrays.asList(10, 40, 80, 120, 350), 
				Arrays.asList(10, 40, 80, 120, 350), Arrays.asList(10, 40, 80, 120, 350)));
	}
	
	/**
	* Purpose: Do paint BoxChart with Graphics2D object
	* Input: paint Graphics2D width height
	* Expected:
	* End without any exceptions.
	*/ 
	@Test
	public void paintTest() {
		assertNotNull(bubbleChart.addSeries("test", new double[] {10, 40, 80, 120, 350}, 
				new double[] {10, 40, 80, 120, 350}, new double[] {10, 40, 80, 120, 350}));
		JFrame frame = new JFrame("title");
		XChartPanel<BubbleChart> chartPanel = new XChartPanel<BubbleChart>(bubbleChart);
		frame.add(chartPanel);
		frame.pack();
		Graphics2D graphics2d = (Graphics2D) chartPanel.getGraphics();
		bubbleChart.paint(graphics2d, 500, 500);
		graphics2d.dispose();
	}
	
	/**
	* Purpose: To test when bubbleSeriesRenderStyle is already set not as by default behavior
	* Input: bubbleSeriesRenderStyle BubbleSeriesRenderStyle.Round
	* Expected:
	* End without any exceptions.
	*/ 
	@Test
	public void bubbleSeriesRenderStyleNotNullTest() {
		bubbleChart.addSeries("test", new double[] {10, 40, 80, 120, 350}, 
				new double[] {10, 40, 80, 120, 350}, new double[] {10, 40, 80, 120, 350}).setBubbleSeriesRenderStyle(BubbleSeriesRenderStyle.Round);
		JFrame frame = new JFrame("title");
		XChartPanel<BubbleChart> chartPanel = new XChartPanel<BubbleChart>(bubbleChart);
		frame.add(chartPanel);
		frame.pack();
		Graphics2D graphics2d = (Graphics2D) chartPanel.getGraphics();
		bubbleChart.paint(graphics2d, 500, 500);
		graphics2d.dispose();
	}
	
	/**
	* Purpose: To test whether calculated min max are accurate.
	* Input: calculateMinMax xMin==1.25 xMax==120.1 yMin==0.8 yMax==50.1
	* Expected:
	* End without any exceptions.
	*/ 
	@Test
	public void minMaxCalcualtorTest() {
		BubbleSeries series = bubbleChart.addSeries("test", new double[] {1.25, 1.5, 2, 120, 120.1}, 
				new double[] {1.1, 0.8, 50, 42, 50.1}, new double[] {10, 40, 80, 120, 350});
		assertEquals(series.getXMin(), 1.25, 0.01);
		assertEquals(series.getXMax(), 120.1, 0.01);
		assertEquals(series.getYMin(), 0.8, 0.01);
		assertEquals(series.getYMax(), 50.1, 0.01);
	}
}
