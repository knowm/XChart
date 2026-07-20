package org.knowm.xchart.internal.chartpart;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import org.junit.jupiter.api.Test;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

// https://github.com/knowm/XChart/issues/593
// On a live chart the cursor used to append the current frame's screen-space points to its list on
// every repaint without ever clearing it. Two symptoms followed: unbounded growth (a memory leak
// that showed up as gigabytes of Cursor$DataPoint), and a label showing values from earlier frames
// because stale points still matched the mouse position.
//
// Exercises the cursor directly (no XChartPanel / Swing display) so it runs headless on CI.
class CursorLiveChartTest {

  private static final int POINT_COUNT = 20;

  /** Index of the data point the mouse is parked on. */
  private static final int HOVER_INDEX = 12;

  private static final int REPAINTS = 30;

  // MouseEvent needs a non-null source Component; a JPanel is fine to construct headless.
  private final Component source = new JPanel();

  @Test
  void cursorDataDoesNotAccumulateAcrossLiveUpdates() {

    XYChart chart = buildChart();
    Cursor cursor = new Cursor(chart);
    PlotInteractionData data = ((Chart<?, ?>) chart).getInteractionData();

    render(chart, cursor);
    PlotInteractionData.CursorData target = data.getCursorDataList().get(HOVER_INDEX);
    cursor.mouseMoved(moved((int) target.x, (int) data.getPlotBounds().getCenterY()));

    for (int frame = 0; frame < REPAINTS; frame++) {
      chart.updateXYSeries("s", xData(), yData(frame), null);
      render(chart, cursor);

      assertThat(data.getCursorDataList())
          .as("interaction data must be rebuilt each paint, not appended to")
          .hasSize(POINT_COUNT);
      assertThat(cursor.dataPointList)
          .as("cursor must drop the previous frame's points (issue #593 memory leak)")
          .hasSize(POINT_COUNT);
    }
  }

  @Test
  void cursorLabelReflectsTheLatestDataAfterLiveUpdates() {

    XYChart chart = buildChart();
    Cursor cursor = new Cursor(chart);
    PlotInteractionData data = ((Chart<?, ?>) chart).getInteractionData();

    render(chart, cursor);
    PlotInteractionData.CursorData target = data.getCursorDataList().get(HOVER_INDEX);
    cursor.mouseMoved(moved((int) target.x, (int) data.getPlotBounds().getCenterY()));

    for (int frame = 0; frame < REPAINTS; frame++) {
      chart.updateXYSeries("s", xData(), yData(frame), null);
      render(chart, cursor);

      assertThat(cursor.matchingDataPointList)
          .as("exactly one label line per series, no leftovers from earlier frames")
          .hasSize(1);
      // ground truth: the interaction data is rebuilt from the series on every paint
      String currentYValue = data.getCursorDataList().get(HOVER_INDEX).yValue;
      assertThat(cursor.matchingDataPointList.get(0).yValue)
          .as("cursor label must show the current frame's value, not an earlier frame's")
          .isEqualTo(currentYValue);
    }
  }

  private static XYChart buildChart() {

    XYChart chart = new XYChartBuilder().width(800).height(600).build();
    chart.addSeries("s", xData(), yData(0));
    chart.enableInteractionData();
    return chart;
  }

  private static List<Double> xData() {

    List<Double> xData = new ArrayList<>();
    for (int i = 0; i < POINT_COUNT; i++) {
      xData.add((double) i);
    }
    return xData;
  }

  /** Y values shift by frame so a stale point is distinguishable from a current one. */
  private static List<Double> yData(int frame) {

    List<Double> yData = new ArrayList<>();
    for (int i = 0; i < POINT_COUNT; i++) {
      yData.add(i * 10.0 + frame);
    }
    return yData;
  }

  /** Paints the chart and hands the collected interaction data to the cursor, as the panel does. */
  private static void render(XYChart chart, Cursor cursor) {

    BufferedImage image =
        new BufferedImage(chart.getWidth(), chart.getHeight(), BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = image.createGraphics();
    chart.paint(g, chart.getWidth(), chart.getHeight());
    chart.consumeInteractionData(g, null, cursor, null);
    g.dispose();
  }

  private MouseEvent moved(int x, int y) {

    return new MouseEvent(source, MouseEvent.MOUSE_MOVED, 1L, 0, x, y, 0, false);
  }
}
