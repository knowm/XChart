package org.knowm.xchart.demo.charts.realtime;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.XcTrans3;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.demo.charts.RealtimeExampleChart;
import org.knowm.xchart.internal.series.Foo;
import org.knowm.xchart.internal.series.Series.DataType;
import org.knowm.xchart.style.Styler.ChartTheme;

/**
 * Real-time XY Chart
 *
 * <p>Demonstrates the following:
 *
 * <ul>
 *   <li>real-time chart updates with SwingWrapper
 *   <li>Matlab Theme
 */
public class RealtimeChart01 implements ExampleChart<XYChart>, RealtimeExampleChart {

  private XYChart xyChart;

  private List<Double> yData = getRandomData(5);

  public static final String SERIES_NAME = "series1";

  public static void main(String[] args) {

    // Setup the panel
    final RealtimeChart01 realtimeChart01 = new RealtimeChart01();
    realtimeChart01.go();
  }

  private void go() {

    final SwingWrapper<XYChart> swingWrapper = new SwingWrapper<XYChart>(getChart());
    swingWrapper.displayChart();

    // Simulate a data feed
    TimerTask chartUpdaterTask =
        new TimerTask() {

          @Override
          public void run() {

            updateData();

            javax.swing.SwingUtilities.invokeLater(
                new Runnable() {

                  @Override
                  public void run() {

                    swingWrapper.repaintChart();
                  }
                });
          }
        };

    Timer timer = new Timer();
    timer.scheduleAtFixedRate(chartUpdaterTask, 0, 500);
  }

  @Override
  public XYChart getChart() {

    //yData = getRandomData(5);

    // Create Chart
    xyChart =
        new XYChartBuilder()
            .width(500)
            .height(400)
            .theme(ChartTheme.Matlab)
            .title("Real-time XY Chart")
            .build();
    
    xyChart.
    		//addSeries(SERIES_NAME, null, yData);
		addSeries(SERIES_NAME, yData
  		  , new XcTrans3<List<? extends Foo>, Integer, Foo, Number>() {
			@Override
			public Number trans(List<? extends Foo> o1, Integer o2, Foo o3) {
				return o2;
			}
		}
  		  , new XcTrans3<List<? extends Foo>, Integer, Foo, Number>() {
  			@Override
  			public Number trans(List<? extends Foo> o1, Integer o2, Foo o3) {
  				return yData.get(o2);
  			}
  		}
  		  , null
  		  , DataType.Number
  		  );

    return xyChart;
  }
  

  public void updateData() {

    // Get some new data
    //List<Double> newData = getRandomData(1);

    //yData.addAll(newData);
    yData.add(nextRandValue());

    // Limit the total number of points
    while (yData.size() > 20) {
      yData.remove(0);
    }

    xyChart.updateXYSeries(SERIES_NAME
    		//, null, yData, null
    		);
  }

  static double nextRandValue() {
	  return Math.random() * 100;
  }
  static List<Double> getRandomData(int numPoints) {

    List<Double> data = new CopyOnWriteArrayList<Double>();
    for (int i = 0; i < numPoints; i++) {
      data.add(nextRandValue());
    }
    return data;
  }
}
