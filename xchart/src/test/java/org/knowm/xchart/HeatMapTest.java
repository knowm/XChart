package org.knowm.xchart;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

public class HeatMapTest {
    /*
    *Purpose : Check for Width, Height, Title
    * Input: build() with width(1000), height(600), title("Bound Test")
    * Expected: getWidth() -> 1000, getHeight -> 600, getTitle -> "Bound Test"
     */
    @Test
    public void BoundTest() {

        HeatMapChart chart = new HeatMapChartBuilder().width(1000).height(600).title("Bound Test").build();
        assertEquals(1000,chart.getWidth());
        assertEquals(600, chart.getHeight());
        assertEquals("Bound Test",chart.getTitle());
    }
    /*
     *Purpose : Check for AddSeries
     * Input: addSeries(xData,yData,heatData) // these are 12 random heatMap data by x in [1,4] and y in [1,3]
     * Expected:
     * getSeriesMap().size() -> 1
     * getSeriesMap().get("Basic HeatMap").heatData.size() == 12
     */
    @Test
    public void AddSeriesTest() {
        HeatMapChart chart = new HeatMapChartBuilder().width(1000).height(600).title("Bound Test").build();
        int[] xData = {1, 2, 3, 4};
        int[] yData = {1, 2, 3};
        int[][] heatData = new int[xData.length][yData.length];
        Random random = new Random();
        for (int i = 0; i < xData.length; i++) {
            for (int j = 0; j < yData.length; j++) {
                heatData[i][j] = random.nextInt(1000);
            }
        }
        chart.addSeries("Basic HeatMap", xData, yData, heatData);

        assertEquals(1,chart.getSeriesMap().size());
        assertEquals(12, chart.getSeriesMap().get("Basic HeatMap").heatData.size());
    }
    /*
     *Purpose : Check for large heat data
     * Input: addSeries(xData,yData,heatData) // these are 1000000 random heatMap data by x in [1,10000] and y in [1,10000]
     * Expected:
     * getSeriesMap().size() -> 1
     * getSeriesMap().get("Basic HeatMap").heatData.size() == 1000000
     */
    @Test
    public void BigHeatdataTest(){
        HeatMapChart chart = new HeatMapChartBuilder().width(1000).height(600).title("Bound Test").build();
        int[] xData = new int[1000];
        int[] yData = new int[1000];
        int[][] heatData = new int[xData.length][yData.length];
        Random random = new Random();
        for (int i = 0; i < xData.length; i++){
            xData[i] = i+1;
            yData[i] = i+1;
        }
        for (int i = 0; i < xData.length; i++) {
            for (int j = 0; j < yData.length; j++) {
                heatData[i][j] = random.nextInt(1000);
            }
        }
        chart.addSeries("Basic HeatMap", xData, yData, heatData);

        assertEquals(1, chart.getSeriesMap().size());
        assertEquals(1000000,chart.getSeriesMap().get("Basic HeatMap").heatData.size());
    }
    /*
     *Purpose : Check for Default HeatMapChartBuilder build()
     * Input: chart build without call width(),height(),title() method
     * Expected: getWidth() -> 800, getHeight -> 600, getTitle -> ""
     */
    @Test
    public void DefaultBuildTest(){
        HeatMapChart chart = new HeatMapChartBuilder().build();
        assertEquals(800,chart.getWidth());
        assertEquals(600, chart.getHeight());
        assertEquals("",chart.getTitle());
    }
    /*
     *Purpose : Check for sanityCheck in HeatMapChart
     * Input: xData = {1,2,3}, yData = {} heatData = new int[0][3]
     * Expected: IllegalArgumentException
     */
    @Test(expected=IllegalArgumentException.class)
    public void SanityCheckYAxisTest(){
        HeatMapChart chart = new HeatMapChartBuilder().build();
        int[] xData = {1,2,3};
        int[] yData = new int[0];
        int[][] heatData = new int[0][3];
        chart.addSeries("Basic HeatMap", xData, yData, heatData);
    }
    /*
     *Purpose : Check for sanityCheck in HeatMapChart
     * Input: xData = {}, yData = {1,2,3} heatData = new int[0][3]
     * Expected: IllegalArgumentException
     */
    @Test(expected=IllegalArgumentException.class)
    public void SanityCheckXAxisTest(){
        HeatMapChart chart = new HeatMapChartBuilder().build();
        int[] xData = new int[0];
        int[] yData = {1,2,3};
        int[][] heatData = new int[0][3];
        chart.addSeries("Basic HeatMap", xData, yData, heatData);
    }
    /*
     *Purpose : Check for sanityCheck in HeatMapChart
     * Input: xData = {1,2,3}, yData = {1,2,3} heatData = new int[2][0]
     * Expected: IllegalArgumentException
     */
    @Test(expected=IllegalArgumentException.class)
    public void SanityCheckHeatDataTest(){
        HeatMapChart chart = new HeatMapChartBuilder().build();
        int[] xData = {1,2,3};
        int[] yData = {1,2,3};
        int[][] heatData = new int[2][0];
        chart.addSeries("Basic HeatMap", xData, yData, heatData);
    }
    /*
     *Purpose : Check for sanityCheck in HeatMapChart
     * Input: xData = [1,2,3], yData = [1,2,3] heatData = [{1,2},{1,1},{1,3}]
     * Expected: IllegalArgumentException
     */
    @Test(expected=IllegalArgumentException.class)
    public void SanityCheckHeatDataTest2(){
        HeatMapChart chart = new HeatMapChartBuilder().build();
        List<Number> xData = new ArrayList<>();
        xData.add(1);
        xData.add(2);
        xData.add(3);
        List<Number> yData = new ArrayList<>();
        yData.add(1);
        yData.add(2);
        yData.add(3);
        List<Number[]> heatData = new ArrayList<>();
        heatData.add(new Number[]{1,2});
        heatData.add(new Number[]{1,1});
        heatData.add(new Number[]{1,3});
        chart.addSeries("Basic HeatMap", xData, yData, heatData);
    }
    /*
     *Purpose : Check for sanityCheck in HeatMapChart
     * Input: xData = [1,2,3], yData = [1,2,3] heatData = [{1,null,2},{1,3,1},{1,5,3}]
     * Expected: IllegalArgumentException
     */
    @Test(expected=IllegalArgumentException.class)
    public void SanityCheckHeatDataNullColumnTest(){
        HeatMapChart chart = new HeatMapChartBuilder().build();
        List<Number> xData = new ArrayList<>();
        xData.add(1);
        xData.add(2);
        xData.add(3);
        List<Number> yData = new ArrayList<>();
        yData.add(1);
        yData.add(2);
        yData.add(3);
        List<Number[]> heatData = new ArrayList<>();
        heatData.add(new Number[]{1,null,2});
        heatData.add(new Number[]{1,3,1});
        heatData.add(new Number[]{1,5,3});
        chart.addSeries("Basic HeatMap", xData, yData, heatData);
    }
    /*
     *Purpose : Check for sanityCheck in HeatMapChart, Exception for minus entry
     * Input: xData = [1,2,3], yData = [1,2,3] heatData = [{1,1,2},{1,-1,1},{1,5,3}]
     * Expected: IllegalArgumentException
     */
    @Test(expected=IllegalArgumentException.class)
    public void SanityCheckHeatDataMinusTest(){
        HeatMapChart chart = new HeatMapChartBuilder().build();
        List<Number> xData = new ArrayList<>();
        xData.add(1);
        xData.add(2);
        xData.add(3);
        List<Number> yData = new ArrayList<>();
        yData.add(1);
        yData.add(2);
        yData.add(3);
        List<Number[]> heatData = new ArrayList<>();
        heatData.add(new Number[]{1,1,2});
        heatData.add(new Number[]{1,-1,1});
        heatData.add(new Number[]{1,5,3});
        chart.addSeries("Basic HeatMap", xData, yData, heatData);
    }
    /*
     *Purpose : Check for HeatMapSeries min, max
     * Input: xData = [1,2,3,4], yData = [1,2,3] heatData = [[0,0,0,0],[0,1,2,3],[0,2,4,6]]
     * Expected: min = 0, max = 6
     */
    @Test
    public void HeatMapSeriesMinMaxTest(){
        HeatMapChart chart = new HeatMapChartBuilder().width(1000).height(600).title("Bound Test").build();
        int[] xData = {1, 2, 3, 4};
        int[] yData = {1, 2, 3};
        int[][] heatData = new int[xData.length][yData.length];
        for (int i = 0; i < xData.length; i++) {
            for (int j = 0; j < yData.length; j++) {
                heatData[i][j] = i*j;
            }
        }
        chart.addSeries("test",xData,yData,heatData);
        assertEquals(0,chart.getHeatMapSeries().min,0.000001);
        assertEquals(6,chart.getHeatMapSeries().max,0.000001);
    }
    /*
     *Purpose : Check for Exception when you call addSeries twice
     * Input: xData = [1,2,3,4], yData = [1,2,3] heatData = [[0,0,0,0],[0,0,0,0],[0,0,0,0]]
     * then, Call addSeries() twice with these parameters.
     * Expected: RuntimeException
     */
    @Test(expected = RuntimeException.class)
    public void HeatMapSeriesSingletonTest(){
        HeatMapChart chart = new HeatMapChartBuilder().width(1000).height(600).title("Bound Test").build();
        int[] xData = {1, 2, 3, 4};
        int[] yData = {1, 2, 3};
        int[][] heatData = new int[xData.length][yData.length]; // zeros array
        chart.addSeries("test",xData,yData,heatData);
        chart.addSeries("test2",xData,yData,heatData);
    }
}
