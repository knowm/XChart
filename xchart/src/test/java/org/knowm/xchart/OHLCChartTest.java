package org.knowm.xchart;

import junit.framework.TestCase;
import org.junit.Test;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.style.theme.Theme;
import org.knowm.xchart.style.theme.XChartTheme;

import java.awt.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

public class OHLCChartTest extends TestCase {

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
    
    /*Purpose: create chart as list and addSeries
     *Input : addSeries
     * Expected : not fail
     */
    @Test 
    public void testCreatListChart() {

        OHLCChart testchart = new OHLCChart(800, 600);

        List<Date> xData = new ArrayList<>();
        List<Double> openData = new ArrayList<>();
        List<Double> highData = new ArrayList<>();
        List<Double> lowData = new ArrayList<>();
        List<Double> closeData = new ArrayList<>();

        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse("2017-01-01");
            populateData(date, 5000.0, 20, xData, openData, highData, lowData, closeData);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        xData = null;
        testchart.addSeries("Series1", openData, highData, lowData, closeData);
        testchart.addSeries("Series2", xData, openData, highData, lowData, closeData);
    }





}