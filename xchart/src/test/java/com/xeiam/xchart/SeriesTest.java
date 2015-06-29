package com.xeiam.xchart;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.xeiam.xchart.internal.chartpart.Axis.AxisType;
import com.xeiam.xchart.internal.style.SeriesColorMarkerLineStyleCycler;

public class SeriesTest {
    @Test
    public void testChangeSeriesSize(){
        List<Integer> xData=Arrays.asList(1,2,3);
        List<Integer> yData=Arrays.asList(1,2,3);
        List<Integer> errData=Arrays.asList(1,2,3);
        Series series=new Series("DummySeries", xData, AxisType.Number, yData, AxisType.Number, errData,new SeriesColorMarkerLineStyleCycler().getNextSeriesColorMarkerLineStyle());
        xData=Arrays.asList(1,2,3,4);
        yData=Arrays.asList(1,2,3,4);
        errData=Arrays.asList(1,2,3,4);
        //This would fail:
        // series.replaceYData(yData);
        series.replaceData(xData, yData, errData);
        xData=Arrays.asList(1,2);
        yData=Arrays.asList(1,2);
        errData=Arrays.asList(1,2);
        series.replaceData(xData, yData, errData);
       

    }
}
