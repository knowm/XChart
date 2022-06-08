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

}