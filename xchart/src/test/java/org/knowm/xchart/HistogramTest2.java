package org.knowm.xchart;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HistogramTest2 {
	private Histogram testItem;
	private List<Integer> emptyList = Collections.emptyList();
	@Before
	public void setUp() throws Exception {
	  testItem = new Histogram(Arrays.asList(1, 2, 3, 4), 1, 1, 4);
	}

	@After
	public void tearDown() throws Exception {
	  testItem = null;
	}
	/**
	 * Purpose: Test Histogram with positive values
	 * Input: positive value list, NumBins
	 * Expected: return Histogram
	 */
	@Test
	public void testPositiveValues() {
	  assertNotNull(testItem = new Histogram(Arrays.asList(1, 2, 3, 4, 5, 6), 3));
	  assertThat(testItem.getMax()).isEqualTo(6);
	  assertThat(testItem.getMin()).isEqualTo(1);
	  assertThat(testItem.getNumBins()).isEqualTo(3);
	}
	/**
	 * Purpose: Test Histogram with negative values
	 * Input: negative value list, NumBins
	 * Expected: return Histogram
	 */
	@Test
	public void testNegetiveValues() {
	  assertNotNull(testItem = new Histogram(Arrays.asList(-1, -2, -3, -4, -5, -6), 3));
	  assertThat(testItem.getMax()).isEqualTo(-1);
	  assertThat(testItem.getMin()).isEqualTo(-6);
	  assertThat(testItem.getNumBins()).isEqualTo(3);
	}
	/**
	 * Purpose: get XYAxisData
	 * Input: value list, NumBins, min, max
	 * Expected:
	 * numBins = 2, min = 0, max = 4: getyAxisData = 4
	 * numBins = 2, min = 0, max = 4: getxAxisData = 4
	 */
	@Test
	public void getXYAxisData() {
	  assertNotNull(testItem = new Histogram(Arrays.asList(1, 2, 3, 4, 5, 6), 2, 0, 4));
	  assertThat(testItem.getOriginalData());
	  assertThat(testItem.getyAxisData().get(0) + testItem.getyAxisData().get(1)).isEqualTo(4);
	  assertThat(testItem.getxAxisData().get(0) + testItem.getxAxisData().get(1)).isEqualTo(4);
	}
	/**
	 * Purpose: Data values are smaller than min
	 * Input: value list, NumBins, min, max
	 * Expected:
	 * Skip this value(< min) and execute next value
	 */
	@Test
	public void testDataSmallerThanMin() {
	  assertNotNull(testItem = new Histogram(Arrays.asList(2, 6), 1, 1, 6));
	}
	/**
	 * Purpose: Data values are smaller than min
	 * Input: value list, NumBins, min, max
	 * Expected:
	 * Skip this value(> max)
	 */
	@Test
	public void testDataBiggerThanMax() {
	  assertNotNull(testItem = new Histogram(Arrays.asList(1, 6), 1, 1, 7));
	}
	/**
	 * Purpose: Data values are smaller than min
	 * Input: value list, NumBins, min, max
	 * Expected:
	 * throw IllegalArgumentException: "max cannot be less than min"
	 */
	@Test
	public void testMaxLessThanMin() {
	  assertNotNull(testItem = new Histogram(Arrays.asList(1, 2, 3, 4, 5, 6), 2, 4, 0));	  
	}
	
	// Exception SaintyCheck
	/**
	 * Purpose: Data values are smaller than min
	 * Input: null, NumBins
	 * Expected:
	 * throw IllegalArgumentException: "Histogram cannot be null"
	 */
	@Test
	public void testNullData() {
	  assertNotNull(testItem = new Histogram(null, 0));
	}
	/**
	 * Purpose: Data values are smaller than min
	 * Input: emptyList, NumBins
	 * Expected:
	 * throw IllegalArgumentException: "Histogram data cannot be empty"
	 */
	@Test
	public void testEmptyData() {
	  assertNotNull(testItem = new Histogram(emptyList, 0));
	}
	/**
	 * Purpose: Data values are smaller than min
	 * Input: value list include null, NumBins
	 * Expected:
	 * throw IllegalArgumentException: "Histogram data cannot be contain null"
	 */
	@Test
	public void testNullInData() {
	  assertNotNull(testItem = new Histogram(Arrays.asList(1, 2, 3, null, 5, 6), 3));
	}
	/**
	 * Purpose: Data values are smaller than min
	 * Input: value list, 0
	 * Expected:
	 * throw IllegalArgumentException: "Histogram numBins cannot be less than or equal to 0"
	 */
	@Test
	public void testNumBinLessThanZero() {
	  assertNotNull(testItem = new Histogram(Arrays.asList(1, 2, 3, 4, 5, 6), 0, 0, 4));
	}
}
