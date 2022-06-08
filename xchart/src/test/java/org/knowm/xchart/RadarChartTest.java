package org.knowm.xchart;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.EmptyStackException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.knowm.xchart.internal.style.SeriesColorMarkerLineStyle;
import org.knowm.xchart.internal.style.SeriesColorMarkerLineStyleCycler;
import org.knowm.xchart.style.RadarStyler;
import org.knowm.xchart.style.Styler.ChartTheme;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.knowm.xchart.style.markers.Marker;
import org.knowm.xchart.style.theme.GGPlot2Theme;
import org.knowm.xchart.style.theme.Theme;

public class RadarChartTest {

	/**
	* Purpose: test constructor RadarChart(int width, int height)
	* Input: RadarChart(int width, int height) put Width = 3, Height = 5
	* * Expected: Width = 3, Height = 5, Styler = RadarStyler
	  * 
	*/
	@Test
	public void testConstructor1(){
		RadarChart radarChart = new RadarChart(3, 5);
		
		assertThat(radarChart.getWidth()).isEqualTo(3);
		assertThat(radarChart.getHeight()).isEqualTo(5);
		assertEquals((new RadarStyler().getClass()), radarChart.getStyler().getClass());
	}
	
	/**
	* Purpose: test constructor RadarChart(int width, int height, Theme theme)
	* Input: RadarChart(int width, int height, Theme theme) put Width = 3, Height = 5, Theme = GGPlot2Theme
	* * Expected: Width = 3, Height = 5, Theme = GGPlot2Theme
	  * 
	*/
	@Test
	public void testConstructor2(){
		RadarChart radarChart = new RadarChart(3, 5, new GGPlot2Theme());
		
		assertThat(radarChart.getWidth()).isEqualTo(3);
		assertThat(radarChart.getHeight()).isEqualTo(5);
		assertEquals((new GGPlot2Theme().getClass()), radarChart.getStyler().getTheme().getClass());
	}
	
	/**
	* Purpose: test constructor RadarChart(int width, int height, ChartTheme chartTheme)
	* Input: RadarChart(int width, int height, ChartTheme chartTheme) put Width = 3, Height = 5, ChartTheme = GGPlot2
	* * Expected: Width = 3, Height = 5, Theme = GGPlot2Theme
	  * 
	*/
	@Test
	public void testConstructor3(){
		RadarChart radarChart = new RadarChart(3, 5, ChartTheme.GGPlot2);
		
		assertThat(radarChart.getWidth()).isEqualTo(3);
		assertThat(radarChart.getHeight()).isEqualTo(5);
		assertEquals((new GGPlot2Theme().getClass()), radarChart.getStyler().getTheme().getClass());
	}
}
