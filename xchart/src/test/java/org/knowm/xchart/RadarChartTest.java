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
	
	/**
	* Purpose: test constructor RadarChart(RadarChartBuilder radarChartBuilder)
	* Input: RadarChart(RadarChartBuilder radarChartBuilder) put width = 3, height = 5, charttheme = GGPlot2, title = "test" through RadarChartBuilder
	* * Expected: Width = 3, Height = 5, Theme = GGPlot2Theme, Title = "test"
	  * 
	*/
	@Test
	public void testConstructor4(){
		RadarChart radarChart = new RadarChartBuilder().width(3).height(5).title("test").theme(ChartTheme.GGPlot2).build();
		
		assertThat(radarChart.getWidth()).isEqualTo(3);
		assertThat(radarChart.getHeight()).isEqualTo(5);
		assertThat(radarChart.getTitle()).isEqualTo("test");
		assertEquals((new GGPlot2Theme().getClass()), radarChart.getStyler().getTheme().getClass());
	}
	
	/**
	* Purpose: Check getter/setter of RaddiLabels
	* Input: setRadiiLabels(), getRadiiLabels() {"t", "e", "s", "t"}
	* * Expected: {"t", "e", "s", "t"}
	  * 
	*/
	@Test
	public void testGetAndsetRadiiLabels(){
		RadarChart radarChart = new RadarChart(3, 5, new GGPlot2Theme());
		String[] testString = {"t", "e", "s", "t"};
		radarChart.setRadiiLabels(testString);
		assertThat(radarChart.getRadiiLabels()).isEqualTo(testString);
	}
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	/**
	* Purpose: Check Condition (radiiLabels == null) in SanityCheck
	* Input: SanityCheck() RadarChart with radiiLabels == null
	* * Expected: throw IllegalArgumentException("Variable labels cannot be null!!!")
	  * 
	*/
	@Test
	public void testSanityCheck1() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Variable labels cannot be null!!!");
		
		RadarChart chart =
		        new RadarChartBuilder().width(800).height(600).title(getClass().getSimpleName()).build();
	    chart.getStyler().setToolTipsEnabled(true);
	    chart.getStyler().setLegendPosition(LegendPosition.InsideSW);
	    chart.setRadiiLabels(null);
	    chart.addSeries("New System", new double[] {0.67, 0.73, 0.97, 0.95, 0.93, 0.73}, null);
	}
	
	/**
	* Purpose: Check Condition (seriesMap.containsKey(seriesName)) in SanityCheck
	* Input: SanityCheck() RadarChart with (seriesMap.containsKey(seriesName))
	* * Expected: throw new IllegalArgumentException(
		          "Series name >"
		              + seriesName
		              + "< has already been used. Use unique names for each series!!!")
	  * 
	*/
	@Test
	public void testSanityCheck2() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Series name >"
	              + "New System"
	              + "< has already been used. Use unique names for each series!!!");
		
		RadarChart chart =
		        new RadarChartBuilder().width(800).height(600).title(getClass().getSimpleName()).build();
	    chart.getStyler().setToolTipsEnabled(true);
	    chart.getStyler().setLegendPosition(LegendPosition.InsideSW);
	    chart.setRadiiLabels(
	            new String[] {
	              "Sales",
	              "Marketing",
	              "Development",
	              "Customer Support",
	              "Information Technology",
	              "Administration"
	            });
	    chart.addSeries("New System", new double[] {0.67, 0.73, 0.97, 0.95, 0.93, 0.73}, null);
	    chart.addSeries("New System", new double[] {0.37, 0.93, 0.57, 0.55, 0.33, 0.73});
	}
	
	/**
	* Purpose: Check Condition (values == null) in SanityCheck
	* Input: SanityCheck() RadarChart with (values == null)
	* * Expected: IllegalArgumentException("Values data cannot be null!!!")
	  * 
	*/
	@Test
	public void testSanityCheck3() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Values data cannot be null!!!");
		
		RadarChart chart =
		        new RadarChartBuilder().width(800).height(600).title(getClass().getSimpleName()).build();
	    chart.getStyler().setToolTipsEnabled(true);
	    chart.getStyler().setLegendPosition(LegendPosition.InsideSW);
	    chart.setRadiiLabels(
	            new String[] {
	              "Sales",
	              "Marketing",
	              "Development",
	              "Customer Support",
	              "Information Technology",
	              "Administration"
	            });
	    chart.addSeries("New System", null, null);
	}
	
	/**
	* Purpose: Check Condition (values.length < radiiLabels.length) in SanityCheck
	* Input: SanityCheck() RadarChart with (values.length < radiiLabels.length)
	* * Expected: IllegalArgumentException("Too few values!!!")
	  * 
	*/
	@Test
	public void testSanityCheck4() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Too few values!!!");
		
		RadarChart chart =
		        new RadarChartBuilder().width(800).height(600).title(getClass().getSimpleName()).build();
	    chart.getStyler().setToolTipsEnabled(true);
	    chart.getStyler().setLegendPosition(LegendPosition.InsideSW);
	    chart.setRadiiLabels(
	            new String[] {
	              "Sales",
	              "Marketing",
	              "Development",
	              "Customer Support",
	              "Information Technology",
	              "Administration"
	            });
	    chart.addSeries("New System", new double[] {0.67, 0.73, 0.97, 0.95, 0.93}, null);
	}
	
	/**
	* Purpose: Check Condition (d < 0 || d > 1) in SanityCheck
	* Input: SanityCheck() RadarChart with values in invalid range
	* * Expected: IllegalArgumentException("Values must be in [0, 1] range!!!")
	  * 
	*/
	@Test
	public void testSanityCheck5() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Values must be in [0, 1] range!!!");
		
		RadarChart chart =
		        new RadarChartBuilder().width(800).height(600).title(getClass().getSimpleName()).build();
	    chart.getStyler().setToolTipsEnabled(true);
	    chart.getStyler().setLegendPosition(LegendPosition.InsideSW);
	    chart.setRadiiLabels(
	            new String[] {
	              "Sales",
	              "Marketing",
	              "Development",
	              "Customer Support",
	              "Information Technology",
	              "Administration"
	            });
	    chart.addSeries("New System", new double[] {0.67, 0.73, 0.97, 0.95, 0.93, 1.73}, null);
	}
}
