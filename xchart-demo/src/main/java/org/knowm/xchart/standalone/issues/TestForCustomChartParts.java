package org.knowm.xchart.standalone.issues;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.internal.chartpart.components.ChartImage;
import org.knowm.xchart.internal.chartpart.components.ChartLine;
import org.knowm.xchart.internal.chartpart.components.ChartText;
import org.knowm.xchart.style.markers.None;

public class TestForCustomChartParts {

  static final int WIDTH = 465;
  static final int HEIGHT = 320;

  public static XYChart getChart() {

    // Create Chart
    XYChart chart = new XYChartBuilder().width(800).height(600).build();

    // Customize Chart
    chart.getStyler().setChartTitleVisible(false);
    chart.getStyler().setLegendVisible(false);

    double start = 0;
    double end = 1;
    double increment = 0.01;

    List<Double> xData = new ArrayList<Double>();
    List<Double> yData = new ArrayList<Double>();

    double x = start;

    while (x <= end) {

      double y = Math.exp(2 * x - (7 * x * x * x));
      xData.add(x);
      yData.add(y);
      x += increment;
    }
    // Series
    XYSeries series = chart.addSeries("series1", xData, yData);
    series.setMarker(new None());

    return chart;
  }

  public static void main(String[] args) {

    final XYChart chart = getChart();

    javax.swing.SwingUtilities.invokeLater(
        new Runnable() {

          @Override
          public void run() {

            JFrame frame = new JFrame("Advanced Example");
            frame.setLayout(new BorderLayout());
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            final XChartPanel chartPanel = new XChartPanel<XYChart>(chart);

            XYSeries series = chart.getSeriesMap().get("series1");

            int width = 2;
            BasicStroke stroke =
                new BasicStroke(
                    width,
                    BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_BEVEL,
                    10.0f,
                    new float[] {3.0f, 0.0f},
                    0.0f);

            // draw a horizontal line at series max point
            ChartLine maxY = new ChartLine(series.getYMax(), false, false);
            maxY.setColor(Color.GREEN);
            maxY.setStroke(stroke);
            maxY.init(chartPanel);

            // draw a horizontal line at series min point
            ChartLine minY = new ChartLine(series.getYMin(), false, false);
            minY.setColor(Color.RED);
            minY.setStroke(stroke);
            minY.init(chartPanel);

            // draw a vertical line at 0.45
            ChartLine xLine = new ChartLine(0.45, true, false);
            xLine.setColor(Color.MAGENTA);
            xLine.setStroke(stroke);
            xLine.init(chartPanel);

            // draw a vertical line at 100 pixels
            ChartLine xLinePixel = new ChartLine(100, true, true);
            xLinePixel.setColor(Color.BLACK);
            xLinePixel.setStroke(stroke);
            xLinePixel.init(chartPanel);

            // add text near to max line
            ChartText maxText = new ChartText("Max", 0.0, series.getYMax(), false);
            maxText.init(chartPanel);

            try {
              URL url =
                  new URL(
                      "https://raw.githubusercontent.com/knowm/XChart/develop/etc/XChart_64_64.png");
              BufferedImage image = ImageIO.read(url);
              ChartImage chartImage = new ChartImage(image, 0, 1, false);
              chartImage.init(chartPanel);
            } catch (IOException e) {
              e.printStackTrace();
            }

            frame.add(chartPanel, BorderLayout.CENTER);

            frame.pack();
            frame.setVisible(true);
          }
        });
  }
}
