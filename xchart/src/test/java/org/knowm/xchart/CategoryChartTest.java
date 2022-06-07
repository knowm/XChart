package org.knowm.xchart;

import junit.framework.TestCase;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CategoryChartTest extends TestCase {
    private static double getHigh(double close, double orig) {
        return close + (orig * Math.random() * 0.02);
    }

    private static double getLow(double close, double orig) {
        return close - (orig * Math.random() * 0.02);
    }

    private static double getNewClose(double close, double orig) {
        return close + (orig * (Math.random() - 0.5) * 0.1);
    }
    public static void populateData(
            Date startDate,
            double startPrice,
            int count,
            List<Date> xData,
            List<Double> openData,
            List<Double> highData,
            List<Double> lowData,
            List<Double> closeData) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        double data = startPrice;
        for (int i = 1; i <= count; i++) {

            // add 1 day
            // startDate = new Date(startDate.getTime() + (1 * 1000 * 60 * 60 * 24));
            // xData.add(startDate);
            cal.add(Calendar.DATE, 1);
            xData.add(cal.getTime());

            double previous = data;

            data = getNewClose(data, startPrice);

            openData.add(previous);

            highData.add(getHigh(Math.max(previous, data), startPrice));
            lowData.add(getLow(Math.min(previous, data), startPrice));

            closeData.add(data);
        }
    }

    /*Purpose: test creat category chart
     *Input : CategoryChart width=10, height=10, categoryChartBuilder
     * Expected : not fail or exception
     */
    @Test
    public void testCreatCategoryChart(){
        int width = 10;
        int height = 10;
        CategoryChart testchart = new CategoryChart(width, height);

        CategoryChartBuilder categoryChartBuilder = new CategoryChartBuilder();
        CategoryChart testChart2 = new CategoryChart(categoryChartBuilder);


    }

    /*Purpose: test addSeries
     *Input : CategoryChart width=10, height=10, categoryChartBuilder
     * Expected : not fail or exception
     */
    @Test
    public void testaddSeries(){
        int width = 10;
        int height = 10;
        CategoryChart testchart = new CategoryChart(width, height);

        CategoryChartBuilder categoryChartBuilder = new CategoryChartBuilder();
        CategoryChart testChart2 = new CategoryChart(categoryChartBuilder);

        double[] xDoubleData = {1.0, 2.0};
        double[] yDoubleData = {1.0, 2.0};

        int[] xIntData = {1, 2};
        int[] yIntData = {1, 2};

        testchart.addSeries("series1", xDoubleData, yDoubleData);
        testChart2.addSeries("series2", xIntData, yIntData);

    }

    /*Purpose: test sanityCheck function
     *Input : addSeries xListData = (1.0), yListData = (1.0, 2.0)
     * Expected : IllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSanityCheck() {

        int width = 10;
        int height = 10;
        CategoryChart testchart = new CategoryChart(width, height);

        CategoryChartBuilder categoryChartBuilder = new CategoryChartBuilder();
        CategoryChart testChart2 = new CategoryChart(categoryChartBuilder);

        List<Double> xListData = new ArrayList<>();
        List<Double> yListData = new ArrayList<>();

        xListData.add(1.0);
        yListData.add(1.0);
        yListData.add(2.0);

        testchart.addSeries("series1", xListData, yListData);

    }
}