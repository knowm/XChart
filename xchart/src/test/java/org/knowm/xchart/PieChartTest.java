package org.knowm.xchart;

import org.junit.Before;
import org.junit.Test;

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
}
