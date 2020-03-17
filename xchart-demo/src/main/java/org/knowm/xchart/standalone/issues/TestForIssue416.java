package org.knowm.xchart.standalone.issues;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.GifEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.demo.charts.realtime.RealtimeChart01;

public class TestForIssue416 {

  public static void main(String[] args) {

    List<BufferedImage> images = new ArrayList<>();
    RealtimeChart01 realtimeChart01 = new RealtimeChart01();
    XYChart xyChart = realtimeChart01.getChart();
    images.add(BitmapEncoder.getBufferedImage(xyChart));
    TimerTask chartUpdaterTask =
        new TimerTask() {

          @Override
          public void run() {

            realtimeChart01.updateData();
            images.add(BitmapEncoder.getBufferedImage(xyChart));
          }
        };

    Timer timer = new Timer();
    timer.scheduleAtFixedRate(chartUpdaterTask, 0, 500);

    try {
      Thread.sleep(10000);
      timer.cancel();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    GifEncoder.saveGif("./RealtimeChart_GIF", images, 0, 300);
  }
}
