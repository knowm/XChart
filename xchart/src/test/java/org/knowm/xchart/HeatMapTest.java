package org.knowm.xchart;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HeatMapTest {
    public static final double EPSILON = 0.00001;
    /*
    *Purpose : Check for Width, Height, Title
    * Input: build() with width(1000), height(600), title("Bound Test")
    * Expected: getWidth() -> 1000, getHeight -> 600, getTitle -> "Bound Test"
     */
    int[] defaultXData;
    int[] defaultYData;
    int[][] defaultHeatData;
    @Before
    public void setUp(){
        int[] xData = {1, 2, 3, 4};
        int[] yData = {1, 2, 3};
        int[][] heatData = new int[xData.length][yData.length];
        Random random = new Random();
        for (int i = 0; i < xData.length; i++) {
            for (int j = 0; j < yData.length; j++) {
                heatData[i][j] = random.nextInt(1000);
            }
        }
        defaultXData = xData;
        defaultYData = yData;
        defaultHeatData = heatData;
    }
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
        int[] xData = defaultXData;
        int[] yData = defaultYData;
        int[][] heatData = defaultHeatData;
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
        assertEquals(0,chart.getHeatMapSeries().getMin(), EPSILON);
        assertEquals(6,chart.getHeatMapSeries().getMax(), EPSILON);
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
    /*
     *Purpose : Check for setting xAxisTitle and yAxisTitle
     * Input: xAxisTitle("test x"), yAxisTitle("test y")
     * Expected: getXAxisTitle() -> "test x" getYAxisTitle() -> "test y"
     */
    @Test
    public void AxisTitleTest(){
        HeatMapChart chart = new HeatMapChartBuilder().xAxisTitle("test x").yAxisTitle("test y").build();
        assertEquals("test x",chart.getXAxisTitle());
        assertEquals("test y",chart.getYAxisTitle());
    }
    /*
     *Purpose : Check for updateSeries without initialization
     * Input: defaultXData, defaultYData, defaultHeatData
     * Expected: IllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void UpdateSeriesTest2(){
        HeatMapChart chart = new HeatMapChartBuilder().build();
        chart.updateSeries("test",defaultXData,defaultYData,defaultHeatData);
    }
    /*
     *Purpose : Check for updateSeries with initialization
     * Input: defaultXData, defaultYData, defaultHeatData -> {1,2} , {1,2}, {{5,6},{7,8}}
     * Expected: size -> 4, min -> 5, max ->8
     */
    @Test
    public void UpdateSeriesTest1(){
        HeatMapChart chart = new HeatMapChartBuilder().build();
        int[] xData = {1, 2};
        int[] yData = {1, 2};
        int[][] heatData = {{5, 6},{7,8}};
        chart.addSeries("test",defaultXData,defaultYData,defaultHeatData);
        chart.updateSeries("test",xData,yData,heatData);
        assertEquals(4,chart.getHeatMapSeries().heatData.size());
        assertEquals(5,chart.getHeatMapSeries().getMin(), EPSILON);
        assertEquals(8,chart.getHeatMapSeries().getMax(), EPSILON);
    }
    /*
     *Purpose : Check for AxisMinMax
     * Input: defaultXData, defaultYData, defaultHeatData
     * Expected: getXMin -> 1,getXMax -> 4,getYMin ->1, getXMax -> 3
     */
    @Test
    public void HeatmapSeriesAxisMinMaxTest(){
        HeatMapChart chart = new HeatMapChartBuilder().build();
        chart.addSeries("test",defaultXData,defaultYData,defaultHeatData);
//        assertEquals(null, chart.getHeatMapSeries().getLegendRenderType());
        assertEquals(1, chart.getHeatMapSeries().getYMin(), EPSILON);
        assertEquals(3, chart.getHeatMapSeries().getYMax(), EPSILON);
        assertEquals(1, chart.getHeatMapSeries().getXMin(), EPSILON);
        assertEquals(4, chart.getHeatMapSeries().getXMax(), EPSILON);
    }
    /*
     *Purpose : Check for Getters in HeatmapSeries
     * Input: defaultXData, defaultYData, defaultHeatData
     * Expected: getXData().size -> 4, getYData().size  -> 3, getHeatData().size  -> 12
     */
    @Test
    public void HeatmapSeriesGetDataTest(){
        HeatMapChart chart = new HeatMapChartBuilder().build();
        HeatMapSeries series = chart.addSeries("test",defaultXData,defaultYData,defaultHeatData);
        assertEquals(4,series.getXData().size());
        assertEquals(3,series.getYData().size());
        assertEquals(12,series.getHeatData().size());
    }
    /*
     *Purpose : Check for Setters in HeatmapSeries
     * Input: defaultXData, defaultYData, defaultHeatData, setMin(-1), setMax(1)
     * Expected: getMin()-> -1 , getMax() -> 1
     */
    @Test
    public void HeatmapSeriesSetMinMax(){
        HeatMapChart chart = new HeatMapChartBuilder().build();
        HeatMapSeries series = chart.addSeries("test",defaultXData,defaultYData,defaultHeatData);
        series.setMin(-1);
        series.setMax(1);
        assertEquals(1,series.getMax(), EPSILON);
        assertEquals(-1,series.getMin(), EPSILON);
    }
    /*
     *Purpose : Check for Setters in HeatmapSeries
     * Input: defaultXData, defaultYData, defaultHeatData, setMin(1), setMax(-1)
     * Expected: getMin()<getMax()
     */
    @Test
    public void WrongMinMaxTest(){
        HeatMapChart chart = new HeatMapChartBuilder().build();
        HeatMapSeries series = chart.addSeries("test",defaultXData,defaultYData,defaultHeatData);
        series.setMin(1);
        series.setMax(-1);
        assertTrue(series.getMax()>series.getMin());
    }
    /*
     *Purpose : Check for HeatData
     * Input: defaultXData, defaultYData, defaultHeatData, setMin(0), setMax(10)
     * Expected: getMin()< (element in heatData) <getMax()
     */
    @Test
    public void HeatDataRangeTest(){
        HeatMapChart chart = new HeatMapChartBuilder().build();
        HeatMapSeries series = chart.addSeries("test",defaultXData,defaultYData,defaultHeatData);
        series.setMin(0);
        series.setMax(10);
        for(Number[] i:series.getHeatData()){
            assertTrue(series.getMin()<=i[2].doubleValue() && i[2].doubleValue()<=series.getMax());
        }

    }
    /*
     *Purpose : Check for getLegendRenderType
     * Input: defaultXData, defaultYData, defaultHeatData
     * Expected: null
     */
    @Test
    public void LegendRenderTypeTest(){
        HeatMapChart chart = new HeatMapChartBuilder().build();
        HeatMapSeries series = chart.addSeries("test",defaultXData,defaultYData,defaultHeatData);
        assertEquals(null,series.getLegendRenderType());
    }
}
