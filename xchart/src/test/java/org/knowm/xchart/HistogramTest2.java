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
	@Test
	public void testPositiveValues() {
	  assertNotNull(testItem = new Histogram(Arrays.asList(1, 2, 3, 4, 5, 6), 3));
	  assertThat(testItem.getMax()).isEqualTo(6);
	  assertThat(testItem.getMin()).isEqualTo(1);
	  assertThat(testItem.getNumBins()).isEqualTo(3);
	}
	@Test
	public void testNegetiveValues() {
	  assertNotNull(testItem = new Histogram(Arrays.asList(-1, -2, -3, -4, -5, -6), 3));
	  assertThat(testItem.getMax()).isEqualTo(-1);
	  assertThat(testItem.getMin()).isEqualTo(-6);
	  assertThat(testItem.getNumBins()).isEqualTo(3);
	}
	@Test
	public void getXYAxisData() {
	  assertNotNull(testItem = new Histogram(Arrays.asList(1, 2, 3, 4, 5, 6), 2, 0, 4));
	  assertThat(testItem.getOriginalData());
	  assertThat(testItem.getyAxisData().get(0) + testItem.getyAxisData().get(1)).isEqualTo(4);
	  assertThat(testItem.getxAxisData().get(0) + testItem.getxAxisData().get(1)).isEqualTo(4);
	}
	@Test
	public void testDataSmallerThanMin() {
	  assertNotNull(testItem = new Histogram(Arrays.asList(1, 6), 1, 2, 6));
	}
	@Test
	public void testDataBiggerThanMax() {
	  assertNotNull(testItem = new Histogram(Arrays.asList(1, 6), 1, 1, 7));
	}
	@Test
	public void testMaxLessThanMin() {
	  assertNotNull(testItem = new Histogram(Arrays.asList(1, 2, 3, 4, 5, 6), 2, 4, 0));	  
	}
	
	// Exception SaintyCheck
	@Test
	public void testNullData() {
	  assertNotNull(testItem = new Histogram(null, 0));
	}
	@Test
	public void testEmptyData() {
	  assertNotNull(testItem = new Histogram(emptyList, 0));
	}
	@Test
	public void testNullInData() {
	  assertNotNull(testItem = new Histogram(Arrays.asList(1, 2, 3, null, 5, 6), 3));
	}
	@Test
	public void testNumBinLessThanZero() {
	  assertNotNull(testItem = new Histogram(Arrays.asList(1, 2, 3, 4, 5, 6), 0, 4, 0));
	}
}
