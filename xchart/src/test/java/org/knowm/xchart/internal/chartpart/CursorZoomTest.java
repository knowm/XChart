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
import org.knowm.xchart.internal.series.AxesChartSeriesNumerical;

// https://github.com/knowm/XChart/issues/584
// The cursor label used to show a stale Y value after zooming, because Cursor accumulated
// screen-space data points across repaints instead of rebuilding them. Zooming re-maps every point
// to a new screen X, so a pre-zoom point could end up under the mouse and win the match.
//
// Exercises the cursor directly (no XChartPanel / Swing display) so it runs headless on CI:
// painting into a BufferedImage and feeding the collected interaction data to the cursor mirrors
// what the panel does on each repaint.
class CursorZoomTest {

  /** Index of the data point the mouse is parked on. With y == x * 10 its label is "120". */
  private static final int HOVER_INDEX = 12;

  private static final String HOVER_Y_VALUE = "120";

  // MouseEvent needs a non-null source Component; a JPanel is fine to construct headless.
  private final Component source = new JPanel();

  @Test
  void cursorValueSurvivesZoomAndReset() {

    XYChart chart = buildChart();
    Cursor cursor = new Cursor(chart);

    // 1. unzoomed: park the mouse exactly on the data point at HOVER_INDEX
    render(chart, cursor);
    PlotInteractionData data = ((Chart<?, ?>) chart).getInteractionData();
    PlotInteractionData.CursorData target = data.getCursorDataList().get(HOVER_INDEX);
    cursor.mouseMoved(moved((int) target.x, (int) data.getPlotBounds().getCenterY()));
    render(chart, cursor);
    assertThat(matchedYValue(cursor))
        .as("cursor should label the point the mouse sits on")
        .isEqualTo(HOVER_Y_VALUE);

    // 2. zoom into indices 10..15; the mouse stays put, so a different point is now under it
    AxesChartSeriesNumerical series = (AxesChartSeriesNumerical) chart.getSeries("s");
    series.filterXByIndex(10, 15);
    render(chart, cursor);
    assertThat(matchedYValue(cursor))
        .as("cursor should still match a point while zoomed")
        .isNotNull();

    // 3. reset the zoom; the label must return to the unzoomed value, not a stale one
    series.resetFilter();
    render(chart, cursor);
    assertThat(matchedYValue(cursor))
        .as("cursor value must not go stale across a zoom")
        .isEqualTo(HOVER_Y_VALUE);
  }

  private static XYChart buildChart() {

    List<Double> xData = new ArrayList<>();
    List<Double> yData = new ArrayList<>();
    for (int i = 0; i < 20; i++) {
      xData.add((double) i);
      yData.add((double) i * 10);
    }
    XYChart chart = new XYChartBuilder().width(800).height(600).build();
    chart.addSeries("s", xData, yData);
    chart.enableInteractionData();
    return chart;
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

  /** The Y label of the first matched point, or null if the cursor matched nothing. */
  private static String matchedYValue(Cursor cursor) {

    if (cursor.matchingDataPointList.isEmpty()) {
      return null;
    }
    return cursor.matchingDataPointList.get(0).yValue;
  }

  private MouseEvent moved(int x, int y) {

    return new MouseEvent(source, MouseEvent.MOUSE_MOVED, 1L, 0, x, y, 0, false);
  }
}
