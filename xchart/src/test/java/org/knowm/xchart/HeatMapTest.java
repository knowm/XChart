package org.knowm.xchart;

import org.junit.Test;

import java.util.Random;

import static org.fest.assertions.api.Assertions.assertThat;

public class HeatMapTest {
    /*
    *Purpose : Check for Width, Height, Title
    * Input: build() with width(1000), height(600), title("Bound Test")
    * Expected: getWidth() -> 1000, getHeight -> 600, getTitle -> "Bound Test"
     */
    @Test
    public void BoundTest() {

        HeatMapChart chart = new HeatMapChartBuilder().width(1000).height(600).title("Bound Test").build();
        assertThat(chart.getWidth() == 1000);
        assertThat(chart.getWidth() == 600);
        assertThat(chart.getTitle() == "Bound Test");
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

        assertThat(chart.getSeriesMap().size() == 1);
        assertThat(chart.getSeriesMap().get("Basic HeatMap").heatData.size() == 12);
    }
}
