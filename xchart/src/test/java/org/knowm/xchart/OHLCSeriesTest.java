package org.knowm.xchart;

import junit.framework.TestCase;
import org.junit.Test;
import org.knowm.xchart.internal.series.Series;

import static org.fest.assertions.api.Assertions.assertThat;

public class OHLCSeriesTest extends TestCase {
    /*Purpose: creat OHLCSeries and test getData, replaceData
    *Input : replaceData xData = {1.0, 2.0}->{3.0, 4.0}, openData = {1.0, 2.0}->{3.0, 4.0},
    *        highData = {5.0, 6.0}->{7.0, 8.0}, lowData = {2.0, 3.0}->{3.0, 4.0},
    *        closeData = {5.0, 5.0}->{6.0, 6.0}, volumeData = {1L, 2L}->{2L, 3L}
    * Expected : each get data is equal to input data
     */
    @Test
    public void testSeries() {
        double[] xData = {1.0, 2.0};
        double[] openData = {1.0, 2.0};
        double[] highData = {5.0, 6.0};
        double[]  lowData = {2.0, 3.0};
        double[] closeData = {5.0, 5.0};
        long[] volumeData = {1L, 2L};
        Series.DataType xAxisDataType = Series.DataType.Number;

        OHLCSeries testSeries = new OHLCSeries("ser1", xData, openData, highData, lowData, closeData, volumeData, xAxisDataType);

        assertThat(testSeries.getXData()).isEqualTo(xData);
        assertThat(testSeries.getOpenData()).isEqualTo(openData);
        assertThat(testSeries.getHighData()).isEqualTo(highData);
        assertThat(testSeries.getLowData()).isEqualTo(lowData);
        assertThat(testSeries.getCloseData()).isEqualTo(closeData);
        assertThat(testSeries.getVolumeData()).isEqualTo(volumeData);
        assertThat(testSeries.getxAxisDataType()).isEqualTo(xAxisDataType);

        xData = new double[] {3.0, 4.0};
        openData = new double[] {3.0, 4.0};
        highData = new double[] {7.0, 8.0};
        lowData = new double[]{3.0, 4.0};
        closeData = new double[]{6.0, 6.0};
        volumeData = new long[]{2L, 3L};

        testSeries.replaceData(xData, openData, highData, lowData, closeData, volumeData);


        assertThat(testSeries.getXData()).isEqualTo(xData);
        assertThat(testSeries.getOpenData()).isEqualTo(openData);
        assertThat(testSeries.getHighData()).isEqualTo(highData);
        assertThat(testSeries.getLowData()).isEqualTo(lowData);
        assertThat(testSeries.getCloseData()).isEqualTo(closeData);
        assertThat(testSeries.getVolumeData()).isEqualTo(volumeData);

    }

    /*Purpose: test about up/down color set/get
     *Input : setColor color
     * Expected : color==color
     */

    @Test
    public void testColor(){
        double[] xData = {1.0, 2.0};
        double[] openData = {1.0, 2.0};
        double[] highData = {5.0, 6.0};
        double[]  lowData = {2.0, 3.0};
        double[] closeData = {5.0, 5.0};
        long[] volumeData = {1L, 2L};
        Series.DataType xAxisDataType = Series.DataType.Number;

        OHLCSeries testSeries = new OHLCSeries("ser1", xData, openData, highData, lowData, closeData, volumeData, xAxisDataType);

        java.awt.Color color = java.awt.Color.BLACK;
        testSeries.setUpColor(color);
        assertThat(testSeries.getUpColor()).isEqualTo(color);

        testSeries.setDownColor(color);
        assertThat(testSeries.getDownColor()).isEqualTo(color);
    }

}