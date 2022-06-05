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

    /*Purpose: create chart as int and addSeries
     *Input : addSeries xData = {1, 2}, openData = {1, 2}, highData = {1, 2}, lowData = {1, 2}
              closeData = {1, 2}
     * Expected : not fail
     */
    @Test
    public void testCreatIntChart() {

        OHLCChart testchart = new OHLCChart(800, 600);

        int[] xData = {1, 2};
        int[] openData = {1, 2};
        int[] highData = {1, 2};
        int[] lowData = {1, 2};
        int[] closeData = {1, 2};

        testchart.addSeries("Series1", openData, highData, lowData, closeData);
        testchart.addSeries("Series2", xData, openData, highData, lowData, closeData);
    }

    /*Purpose: create chart as Float and addSeries
     *Input : addSeries xData = {1.0f, 2.0f}, openData = {1.0f, 2.0f}, highData = {1.0f, 2.0f}, lowData = {1.0f, 2.0f}
              closeData = {1.0f, 2.0f}
     * Expected : not fail
     */
    @Test
    public void testCreatFloatChart() {

        OHLCChart testchart = new OHLCChart(800, 600);

        float[] xData = {1.0f, 2.0f};
        float[] openData = {1.0f, 2.0f};
        float[] highData = {1.0f, 2.0f};
        float[] lowData = {1.0f, 2.0f};
        float[] closeData = {1.0f, 2.0f};

        testchart.addSeries("Series1", openData, highData, lowData, closeData);
        testchart.addSeries("Series2", xData, openData, highData, lowData, closeData);
    }


    /*Purpose: create chart as Float and addSeries
     *Input : addSeries xData = {1.0, 2.0}, openData = {1.0, 2.0}, highData = {1.0, 2.0}, lowData = {1.0, 2.0}
              closeData = {1.0, 2.0}
     * Expected : not fail
     */
    @Test
    public void testCreatDoubleChart() {

        OHLCChart testchart = new OHLCChart(800, 600);

        double[] xData = {1.0, 2.0};
        double[] openData = {1.0, 2.0};
        double[] highData = {1.0, 2.0};
        double[] lowData = {1.0, 2.0};
        double[] closeData = {1.0, 2.0};

        testchart.addSeries("Series1", openData, highData, lowData, closeData);
        testchart.addSeries("Series2", xData, openData, highData, lowData, closeData);

    }


    /*Purpose: test CheckDataLength function Exception
     *Input : addSeries xData = {1.0, 2.0}, openData = {1.0, 2.0}, highData = {1.0, 2.0}, lowData = {1.0, 2.0}
              closeData = {1.0, 2.0, 3.0}
     * Expected : IllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCheckDataLengthsException(){
        OHLCChart testchart = new OHLCChart(800, 600);

        double[] xData = {1.0, 2.0};
        double[] openData = {1.0, 2.0};
        double[] highData = {1.0, 2.0};
        double[] lowData = {1.0, 2.0};
        double[] closeData = {1.0, 2.0, 3.0};

        testchart.addSeries("Series2", xData, openData, highData, lowData, closeData);


    }

    /*Purpose: test SanityCheck function Exception
     *Input : addSeries xData = {1.0, 2.0}, openData = {1.0, 2.0}, highData = {1.0, 2.0}, lowData = {1.0, 2.0}
              closeData = {1.0, 2.0, 3.0}
     * Expected : IllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSanityCheckException(){
        OHLCChart testchart = new OHLCChart(800, 600);

        double[] xData = {1.0, 2.0};
        double[] openData = {1.0, 2.0};
        double[] highData = {1.0, 2.0};
        double[] lowData = {1.0, 2.0};
        double[] closeData = {1.0, 2.0};
        long[] volumeData = {1L, 2L, 3L};

        testchart.addSeries("Series2", xData, openData, highData, lowData, closeData, volumeData);

    }

    /*Purpose: test updateOHLCSeries function Exception
     *Input : addSeries xData = {1.0, 2.0}, openData = {1.0, 2.0}, highData = {1.0, 2.0}, lowData = {1.0, 2.0}
              closeData = {1.0, 2.0, 3.0}
     * Expected : not fail or exception
     */
    @Test
    public void testUpdateOHLCSeries(){
        OHLCChart testchart = new OHLCChart(800, 600);

        double[] xData = {1.0, 2.0};
        double[] openData = {1.0, 2.0};
        double[] highData = {1.0, 2.0};
        double[] lowData = {1.0, 2.0};
        double[] closeData = {1.0, 2.0};
        long[] volumeData = {1L, 2L};

        testchart.addSeries("Series2", xData, openData, highData, lowData, closeData, volumeData);

        double[] newXData = {1.0, 2.0};
        double[] newYData = {1.0, 2.0};
        testchart.updateOHLCSeries("Series2", newXData, newYData);
    }



}