package org.knowm.xchart;

import java.util.Arrays;
import java.util.List;

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
		assertNotNull(boxChart.addSeries("test", Arrays.asList(10, 40, 80, 120, 350)));
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
}
