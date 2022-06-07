package org.knowm.xchart;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.knowm.xchart.style.Styler.ChartTheme;

public class BoxChartTest {
	BoxChart boxChart;
	
	@Before
	public void setUp() throws Exception {
	    boxChart =
	            new BoxChartBuilder()
	                .width(600)
	                .height(450)
	                .title("this is for test")
	                .xAxisTitle("X")
	                .yAxisTitle("Y")
	                .theme(ChartTheme.XChart)
	                .build();
	    boxChart.getStyler().setToolTipsEnabled(true);
	    boxChart.getStyler().setYAxisLogarithmic(true);
	}

	@After
	public void tearDown() throws Exception {
		boxChart = null;
	}
	
	/**
	* Purpose: Test addSeries when it works successfully.
	* Input: addSeries int array, double array, List<? extends Number>
	* Expected:
	* return BoxSeries
	*/ 
	@Test
	public void addSeriesTest() {
		assertNotNull(boxChart.addSeries("testInt", new int[] {1, 2, 3, 4, 5}));
		assertNotNull(boxChart.addSeries("testDouble", new double[] {1.1, 2.2, 3.3
				, 4.4, 5.5}));
		assertNull(boxChart.addSeries("test", Arrays.asList(10, 40, 80, 120, 350)).getLegendRenderType());
	}
	
	/**
	* Purpose: Test name duplicate sanity checker inside addSeries method.
	* Input: seriesNameDuplicateCheck seriesMap.containsKey("test"), seriesMap.containsKey("test")
	* Expected:
	* throw IllegalArgumentException
	* First add. return BoxSeries
	* Second add. throw exception
	*/ 
	@Test(expected = IllegalArgumentException.class)
	public void seriesNameDuplicateTest() {
		assertNotNull(boxChart.addSeries("test", new int[] {1, 2, 3, 4, 5}));
		assertNotNull(boxChart.addSeries("test", new double[] {1.1, 2.2, 3.3
				, 4.4, 5.5}));
	}
	
	/**
	* Purpose: Remove existing series name.
	* Input: removeSeries String "test"
	* Expected:
	* return seriesMap
	* End without exception by seriesNameDuplicateCheck
	*/ 
	@Test
	public void seriesNameRemoveTest() {
		assertNotNull(boxChart.addSeries("test", new int[] {1, 2, 3, 4, 5}));
		assertNotNull(boxChart.removeSeries("test"));
		assertNotNull(boxChart.addSeries("test", new double[] {1.1, 2.2, 3.3
				, 4.4, 5.5}));
	}
	
	/**
	* Purpose: Update with valid series data.
	* Input: updateBoxSeries series.replaceData(int array), series.replaceData(double array)
	* Expected:
	* return BoxSeries
	*/ 
	@Test
	public void updateBoxSeriesTest() {
		assertNotNull(boxChart.addSeries("test", new int[] {1, 2, 3, 4, 5}));
		assertNotNull(boxChart.updateBoxSeries("test", new int[] {5, 5, 5, 5, 5}));
		assertNotNull(boxChart.updateBoxSeries("test", new double[] {1.2, 1.5, 0.7}));
	}
	
	/**
	* Purpose: Update data with invalid series name.
	* Input: checkSeriesValidity getSeriesMap().get(seriesName)
	* Expected:
	* throw IllegalArgumentException
	*/ 
	@Test(expected = IllegalArgumentException.class)
	public void checkSeriesValidityTest() {
		assertNotNull(boxChart.addSeries("test", new int[] {1, 2, 3, 4, 5}));
		assertNotNull(boxChart.updateBoxSeries("test2", new int[] {5, 5, 5, 5, 5}));
	}
	
	/**
	* Purpose: Add invalid null series data.
	* Input: checkSanity null
	* Expected:
	* throw IllegalArgumentException
	*/ 
	@Test(expected = IllegalArgumentException.class)
	public void checkSeriesYDataNullArrayTest() {
		int[] nullArr = null;
		assertNotNull(boxChart.addSeries("test", nullArr));
	}
	
	/**
	* Purpose: Add empty list series data.
	* Input: checkSanity (data.size() == 0)
	* Expected:
	* throw IllegalArgumentException
	*/ 
	@Test(expected = IllegalArgumentException.class)
	public void checkSeriesYDataZeroSizeListTest() {
		List<Integer> data = new ArrayList<>();
		assertNotNull(boxChart.addSeries("test", data));
	}
	
	/**
	* Purpose: Add null containing series data.
	* Input: checkSanity (data.get(0) == null)
	* Expected:
	* throw IllegalArgumentException
	*/ 
	@Test(expected = IllegalArgumentException.class)
	public void checkSeriesYDataNullContainListTest() {
		List<Integer> data = new ArrayList<>();
		data.add(null);
		assertNotNull(boxChart.addSeries("test", data));
	}
	
	/**
	* Purpose: Do paint BoxChart with Graphics2D object
	* Input: paint Graphics2D width height
	* Expected:
	* End without any exceptions.
	*/ 
	@Test
	public void paintTest() {
		assertNotNull(boxChart.addSeries("test", new int[] {1, 2, 3, 4, 5}));
		JFrame frame = new JFrame("title");
		XChartPanel<BoxChart> chartPanel = new XChartPanel<BoxChart>(boxChart);
		frame.add(chartPanel);
		frame.pack();
		Graphics2D graphics2d = (Graphics2D) chartPanel.getGraphics();
	    boxChart.paint(graphics2d, 500, 500);
	    graphics2d.dispose();
	}
	
	/**
	* Purpose: To test whether calculated min max are accurate.
	* Input: calculateMinMax yMin==0.8 yMax==3.6
	* Expected:
	* End without any exceptions.
	*/ 
	@Test
	public void minMaxCalcualtorTest() {
		BoxSeries series = boxChart.addSeries("test", new double[] {1, 0.9, 0.8, 3.5, 3.6});
		assertEquals(series.getYMin(), 0.8, 0.01);
		assertEquals(series.getYMax(), 3.6, 0.01);
	}
}
