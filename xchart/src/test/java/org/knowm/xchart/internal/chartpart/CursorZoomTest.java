package org.knowm.xchart.internal.chartpart;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.internal.series.AxesChartSeriesNumerical;

/**
 * Regression test for issue #584: the cursor label showed a stale Y value after zooming, because
 * {@link Cursor} accumulated screen-space data points across repaints instead of rebuilding them.
 * Zooming re-maps every point to a new screen X, so a pre-zoom point could end up under the mouse
 * and win the match.
 *
 * <p>The chart is driven headlessly (no {@code XChartPanel}) by painting into a {@link
 * BufferedImage} and feeding the collected interaction data to the cursor, mirroring what the panel
 * does on each repaint. Cursor internals are read reflectively since they are private.
 */
public class CursorZoomTest {

  /** Index of the data point the mouse is parked on. With y == x * 10 its label is "120". */
  private static final int HOVER_INDEX = 12;

  private static final String HOVER_Y_VALUE = "120";

  @Test
  public void cursorValueIsCorrectAfterZoomAndReset() throws Exception {

    XYChart chart = buildChart();
    Cursor cursor = new Cursor(chart);

    // 1. unzoomed: park the mouse exactly on the data point at HOVER_INDEX
    render(chart, cursor);
    double targetX = pointX(dataPointList(cursor).get(HOVER_INDEX));
    setMouse(cursor, targetX, plotBounds(cursor).getCenterY());
    render(chart, cursor);
    String before = matchedYValue(cursor);
    assertEquals(HOVER_Y_VALUE, before, "cursor should label the point the mouse sits on");

    // 2. zoom into indices 10..15; the mouse stays put, so a different point is now under it
    AxesChartSeriesNumerical series = (AxesChartSeriesNumerical) chart.getSeries("s");
    series.filterXByIndex(10, 15);
    render(chart, cursor);
    assertNotNull(matchedYValue(cursor), "cursor should still match a point while zoomed");

    // 3. reset the zoom; the label must return to the unzoomed value, not a stale one
    series.resetFilter();
    render(chart, cursor);
    assertEquals(before, matchedYValue(cursor), "cursor value must not go stale across a zoom");
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

  private static void setMouse(Cursor cursor, double x, double y) throws Exception {

    field(Cursor.class, "mouseX").setDouble(cursor, x);
    field(Cursor.class, "mouseY").setDouble(cursor, y);
  }

  private static Rectangle2D plotBounds(Cursor cursor) throws Exception {

    return (Rectangle2D) field(Cursor.class, "plotBounds").get(cursor);
  }

  @SuppressWarnings("unchecked")
  private static List<Object> dataPointList(Cursor cursor) throws Exception {

    return (List<Object>) field(Cursor.class, "dataPointList").get(cursor);
  }

  /** The Y label of the first matched point, or null if the cursor matched nothing. */
  @SuppressWarnings("unchecked")
  private static String matchedYValue(Cursor cursor) throws Exception {

    List<Object> matches =
        (List<Object>) field(Cursor.class, "matchingDataPointList").get(cursor);
    if (matches.isEmpty()) {
      return null;
    }
    Object dataPoint = matches.get(0);
    return (String) field(dataPoint.getClass(), "yValue").get(dataPoint);
  }

  private static double pointX(Object dataPoint) throws Exception {

    return field(dataPoint.getClass(), "x").getDouble(dataPoint);
  }

  private static Field field(Class<?> type, String name) throws Exception {

    Field field = type.getDeclaredField(name);
    field.setAccessible(true);
    return field;
  }
}
