package org.knowm.xchart;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.awt.Graphics2D;

import javax.swing.JFrame;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DialChartTest {
	private DialChart testItem;
	private DialSeries testItem2;

	@Before
	public void setUp() throws Exception {
		testItem = new DialChartBuilder().width(800).height(600).title(getClass().getSimpleName()).build();
		testItem2 = new DialSeries("Dial", 0.9381, "93.81%");
	}

	@After
	public void tearDown() throws Exception {
		testItem = null;
	}
	/**
	 * Purpose: Test DialChart addSeries
	 * Input: name, value, label
	 * Expected: return DialSeries
	 */
	@Test
	public void testDialChart() {
		assertNotNull(testItem.addSeries("Rate", 0.9381, "93.81%"));
		assertNotNull(testItem.getStyler().setToolTipsEnabled(true));
		assertNotNull(testItem.getStyler().setLabelVisible(true));
		assertNotNull(testItem.getStyler().setLegendVisible(false));
	}
	/**
	 * Purpose: Test DialChart addSeries's method
	 * Input: 
	 * Expected: Verify that each value, label is returned successfully
	 */
	@Test
	public void testDialSeries() {
		assertThat(testItem2.getValue()).isEqualTo(0.9381);
		assertThat(testItem2.getLabel()).isEqualTo("93.81%");
		testItem2.setValue(0.5);
		assertThat(testItem2.getValue()).isEqualTo(0.5);
		assertNull(testItem2.getLegendRenderType());
	}
	/**
	 * Purpose: Do paint DialChart with Graphics2D object
	 * Input: graphics2d, width, height
	 * Expected: Do paint successfully
	 */
	@Test
	public void testDialPaint() {
		JFrame frame = new JFrame("title");
		XChartPanel<DialChart> chartDial = new XChartPanel<DialChart>(testItem);
		frame.add(chartDial);
		frame.pack();
		Graphics2D graphics2d = (Graphics2D) chartDial.getGraphics();
		testItem.paint(graphics2d, 800, 600);
	}
	/**
	 * Purpose: if Chart's names are duplicated in one series
	 * Input: name, value
	 * Expected:
	 * throw IllegalArgumentException: "'name' has already been used. Use unique names for each series"
	 */
	// SaintyCheck
	@Test
	public void testSameSeriesName() {
		assertNotNull(testItem.addSeries("Rate", 0.5));
		assertNotNull(testItem.addSeries("Rate", 0.6));
	}
	/**
	 * Purpose: if value < 0 or value > 1
	 * Input: name, value
	 * Expected:
	 * throw IllegalArgumentException: "Value must be in [0,1] range"
	 */
	@Test
	public void testValueOutOfRange() {
		assertNotNull(testItem.addSeries("R1", -0.5));
		assertNotNull(testItem.addSeries("R2", 50.0));
	}

}
