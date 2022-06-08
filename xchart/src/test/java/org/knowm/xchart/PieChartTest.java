package org.knowm.xchart;

import org.junit.Before;
import org.junit.Test;
import org.knowm.xchart.style.Styler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PieChartTest {
    public static final double EPSILON = 0.00001;
    /*
     *Purpose : Check for Width and Height and Title
     * Input: width(800), height(600), title("test title")
     * Expected: getWidth-> 1000, getHeight-> 500, getTitle ->"test title"
     */
    @Test
    public void BuildTest(){
        PieChart chart =
                new PieChartBuilder().width(1000).height(500).title("test title").build();
        assertEquals(1000,chart.getWidth());
        assertEquals(500,chart.getHeight());
        assertEquals("test title",chart.getTitle());
    }
    /*
     *Purpose : Check for default build without additional parameters
     * Input: build
     * Expected:  getWidth-> 800, getHeight-> 600, getTitle ->""
     */
    @Test
    public void DefaultBuildTest(){
        PieChart chart =
                new PieChartBuilder().build();
        assertEquals(800,chart.getWidth());
        assertEquals(600,chart.getHeight());
        assertEquals("",chart.getTitle());
    }
    /*
     *Purpose : Check for addSeries of pieseries
     * Input: addSeries ("hello",3), addSeries ("hello2",6),addSeries ("hello2",9)
     * Expected:  size -> 3, getTotal -> 18
     */
    @Test
    public void addSeriesTest(){
        PieChart chart =
                new PieChartBuilder().build();
        chart.addSeries("hello",3);
        chart.addSeries("hello2",6);
        chart.addSeries("hello3",9);
        assertEquals(3,chart.getSeriesMap().size());
        assertEquals(18,chart.getTotal(),EPSILON);
    }
    /*
     *Purpose : Check for removeSeries of pieseries
     * Input: addSeries ("hello",3), addSeries ("hello2",6),removeSeries("hello")
     * Expected:  size -> 1, getTotal -> 6
     */
    @Test
    public void removeSeriesTest(){
        PieChart chart =
                new PieChartBuilder().build();
        chart.addSeries("hello",3);
        chart.addSeries("hello2",6);
        chart.removeSeries("hello");
        assertEquals(1,chart.getSeriesMap().size());
        assertEquals(6,chart.getTotal(),EPSILON);
    }
    /*
     *Purpose : Check for removeSeries of pieseries when the element doesn't exist.
     * Input: removeSeries("hello")
     * Expected:  size->0
     */
    @Test
    public void EmptyRemoveSeriesTest(){
        PieChart chart =
                new PieChartBuilder().build();
        chart.removeSeries("hello");
        assertEquals(0,chart.getSeriesMap().size());
    }
    /*
     *Purpose : Check for getTotal of pieseries when the element doesn't exist.
     * Input: removeSeries("hello")
     * Expected:  size->0
     */
    @Test
    public void EmptyGetTotalTest(){
        PieChart chart =
                new PieChartBuilder().build();
        double total = chart.getTotal();
        assertEquals(0,total,EPSILON);
    }
    /*
     *Purpose : Check for Integer overflow on getTotal implementation
     * Input:  addSeries("test",Integer.MAX_VALUE) addSeries("test2",Integer.MAX_VALUE)
     * Expected:  getTotal -> Integer.MAX_VALUE*2.0
     */
    @Test
    public void IntegerOverflowTest(){
        PieChart chart =
                new PieChartBuilder().build();
        chart.addSeries("test",Integer.MAX_VALUE);
        chart.addSeries("test2",Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE*2.0,chart.getTotal(),EPSILON);
    }
    /*
     *Purpose : Check for addseries when it exist already
     * Input:  addSeries("test",1) addSeries("test",2)
     * Expected:  IllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void ReAddSeriesTest(){
        PieChart chart =
                new PieChartBuilder().build();
        chart.addSeries("test",1);
        chart.addSeries("test",2);
    }
    /*
     *Purpose : Check for addseries when it exist already
     * Input:  addSeries("test"+i,0) * 10000000 times
     * Expected:  size -> 10000000, getTotal -> 0
     */
    @Test
    public void ManyDataTest(){
        PieChart chart =
                new PieChartBuilder().build();
        for(int i=0;i<10000000;i++){
            chart.addSeries("test"+i,0);
        }
        assertEquals(10000000,chart.getSeriesMap().size());
        assertEquals(0,chart.getTotal(),EPSILON);
    }
    /*
     *Purpose : Check for AxisTitle
     * Input:  setXAxisTitle("x") setYAxisTitle("y")
     * Expected:  RuntimeException
     */
    @Test(expected = RuntimeException.class)
    public void AxisTitleTest(){
        PieChart chart =
                new PieChartBuilder().build();
        chart.setXAxisTitle("x");
        chart.setYAxisTitle("y");
        String x = chart.getXAxisTitle();
        String y = chart.getYAxisTitle();
    }
    /*
     *Purpose : Check for updatePieSeries
     * Input:  addSeries("a",1),addSeries("b",2),addSeries("c",3),updatePieSeries("a",4)
     * Expected:  getValue-> 4
     */
    @Test
    public void UpdateTest(){
        PieChart chart =
                new PieChartBuilder().build();
        PieSeries series = chart.addSeries("a",1);
        chart.addSeries("b",2);
        chart.addSeries("c",3);
        chart.updatePieSeries("a",4);
        assertEquals(4,chart.getSeriesMap().get("a").getValue());
        assertEquals(4,series.getValue());
    }
    /*
     *Purpose : Check for updatePieSeries
     * Input:  addSeries("a",1),replaceData(4)
     * Expected:  getValue-> 4
     */
    @Test
    public void ReplaceTest(){
        PieChart chart =
                new PieChartBuilder().build();
        PieSeries series = chart.addSeries("a",1);
        series.replaceData(4);
        assertEquals(4,chart.getSeriesMap().get("a").getValue());
        assertEquals(4,series.getValue());
    }
    /*
     *Purpose : Check for setter and getter of value
     * Input:  addSeries("a",1),setValue(4)
     * Expected:  getValue-> (1 -> 4)
     */
    @Test
    public void ValueGetSetTest(){
        PieChart chart =
                new PieChartBuilder().build();
        PieSeries series = chart.addSeries("a",1);
        assertEquals(1,series.getValue());
        series.setValue(4);
        assertEquals(4,series.getValue());
    }
    /*
     *Purpose : Check for Changing PieSeriesRenderStyle
     * Input:  setChartPieSeriesRenderStyle(->Pie->Donut->Default)
     * Expected:  setChartPieSeriesRenderStyle(null->Pie->Donut->Pie)
     */
    @Test
    public void RenderStyleChangeTest(){
        PieChart chart =
                new PieChartBuilder().build();
        PieSeries series = chart.addSeries("a",1);
        assertEquals(null,series.getChartPieSeriesRenderStyle());
        series.setChartPieSeriesRenderStyle(PieSeries.PieSeriesRenderStyle.Pie);
        assertEquals(PieSeries.PieSeriesRenderStyle.Pie,series.getChartPieSeriesRenderStyle());
        series.setChartPieSeriesRenderStyle(PieSeries.PieSeriesRenderStyle.Donut);
        assertEquals(PieSeries.PieSeriesRenderStyle.Donut,series.getChartPieSeriesRenderStyle());
        series.setChartPieSeriesRenderStyle(chart.getStyler().getDefaultSeriesRenderStyle());
        assertEquals(PieSeries.PieSeriesRenderStyle.Pie,series.getChartPieSeriesRenderStyle());
    }
}
