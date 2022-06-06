package org.knowm.xchart;

import junit.framework.TestCase;
import org.junit.Test;
import org.knowm.xchart.internal.series.Series;

import java.util.ArrayList;
import java.util.List;

public class CategorySeriesTest extends TestCase {

    /*Purpose: test get/set ChartCategorySeriesRenderStyle function
     *Input : setChartCategorySeriesRenderStyle renderStyle
     *        getChartCategorySeriesRenderStyle
     * Expected : getChartCategorySeriesRenderStyle->renderStyle
     */
    @Test
    public void testChartCategorySeriesRenderStyle(){
        List<Double> xData = new ArrayList<>();
        List<? extends Number> yData = new ArrayList<>();
        List<? extends Number> errorBars = new ArrayList<>();
        Series.DataType axisType = Series.DataType.Date;


        CategorySeries testSeries = new CategorySeries("series", xData, yData, errorBars, axisType);
        assertNull(testSeries.getChartCategorySeriesRenderStyle());

        CategorySeries.CategorySeriesRenderStyle renderStyle = CategorySeries.CategorySeriesRenderStyle.Line;
        testSeries.setChartCategorySeriesRenderStyle(renderStyle);

        assertEquals(renderStyle, testSeries.getChartCategorySeriesRenderStyle());
    }

}