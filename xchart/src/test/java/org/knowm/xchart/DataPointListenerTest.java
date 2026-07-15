package org.knowm.xchart;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

// https://github.com/knowm/XChart/issues/492
class DataPointListenerTest {

  private static final int WIDTH = 800;
  private static final int HEIGHT = 600;

  @Test
  void chartDataPointEqualityIgnoresShape() {

    ChartDataPoint a =
        new ChartDataPoint("s", 2, null, "x", "y", 10, 20, new Rectangle2D.Double(0, 0, 5, 5));
    ChartDataPoint b =
        new ChartDataPoint("s", 2, null, "x", "y", 10, 20, new Rectangle2D.Double(9, 9, 1, 1));
    ChartDataPoint different =
        new ChartDataPoint("s", 3, null, "x", "y", 10, 20, new Rectangle2D.Double(0, 0, 5, 5));

    assertThat(a).isEqualTo(b);
    assertThat(a.hashCode()).isEqualTo(b.hashCode());
    assertThat(a).isNotEqualTo(different);
  }

  @Test
  void hoverAndClickReportSeriesAndIndexOverBars() {

    CategoryChart chart =
        new CategoryChartBuilder().width(WIDTH).height(HEIGHT).title("test").build();
    chart.addSeries("test 1", Arrays.asList(0, 1, 2, 3, 4), Arrays.asList(4, 5, 9, 6, 5));

    XChartPanel<CategoryChart> panel = new XChartPanel<>(chart);
    panel.setSize(WIDTH, HEIGHT);

    List<ChartDataPoint> hovers = new ArrayList<>();
    List<ChartDataPoint> exits = new ArrayList<>();
    List<ChartDataPoint> clicks = new ArrayList<>();
    panel.addDataPointListener(
        new DataPointListener() {
          @Override
          public void onDataPointHover(ChartDataPoint dataPoint, MouseEvent e) {
            hovers.add(dataPoint);
          }

          @Override
          public void onDataPointExit(ChartDataPoint dataPoint, MouseEvent e) {
            exits.add(dataPoint);
          }

          @Override
          public void onDataPointClick(ChartDataPoint dataPoint, MouseEvent e) {
            clicks.add(dataPoint);
          }
        });

    // render offscreen so the plot collects per-data-point hit shapes
    paintOffscreen(panel);

    // sweep the whole panel with synthetic mouse-moves to land on the bars
    for (int y = 0; y < HEIGHT; y += 4) {
      for (int x = 0; x < WIDTH; x += 4) {
        fireMouseMoved(panel, x, y);
      }
      // step off any hovered bar between rows so re-entry fires a fresh hover
      fireMouseMoved(panel, 0, y);
    }

    assertThat(hovers).isNotEmpty();
    assertThat(exits).isNotEmpty();
    for (ChartDataPoint dataPoint : hovers) {
      assertThat(dataPoint.getSeriesName()).isEqualTo("test 1");
      assertThat(dataPoint.getDataPointIndex()).isBetween(0, 4);
      assertThat(dataPoint.getShape()).isNotNull();
    }
    // all five bars should be discoverable
    assertThat(hovers.stream().map(ChartDataPoint::getDataPointIndex).distinct().count())
        .isEqualTo(5L);

    // click the center of the first hovered bar
    ChartDataPoint firstBar = hovers.get(0);
    fireMouseClicked(
        panel, (int) firstBar.getShape().getBounds().getCenterX(),
        (int) firstBar.getShape().getBounds().getCenterY());
    assertThat(clicks).hasSize(1);
    assertThat(clicks.get(0).getSeriesName()).isEqualTo("test 1");
  }

  @Test
  void noListenersMeansNoInteractionOverhead() {

    CategoryChart chart =
        new CategoryChartBuilder().width(WIDTH).height(HEIGHT).title("test").build();
    chart.addSeries("test 1", Arrays.asList(0, 1), Arrays.asList(4, 5));

    XChartPanel<CategoryChart> panel = new XChartPanel<>(chart);
    // no data-point listener registered -> no dispatcher wired up
    assertThat(panel.getMouseMotionListeners()).isEmpty();

    DataPointListener listener = new DataPointListener() {};
    panel.addDataPointListener(listener);
    assertThat(panel.getMouseMotionListeners()).isNotEmpty();

    panel.removeDataPointListener(listener);
    assertThat(panel.getMouseMotionListeners()).isEmpty();
  }

  private static void paintOffscreen(XChartPanel<?> panel) {
    BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = image.createGraphics();
    panel.paint(g);
    g.dispose();
  }

  private static void fireMouseMoved(XChartPanel<?> panel, int x, int y) {
    MouseEvent e =
        new MouseEvent(panel, MouseEvent.MOUSE_MOVED, 1L, 0, x, y, 0, false);
    for (MouseMotionListener l : panel.getMouseMotionListeners()) {
      l.mouseMoved(e);
    }
  }

  private static void fireMouseClicked(XChartPanel<?> panel, int x, int y) {
    MouseEvent e =
        new MouseEvent(
            panel, MouseEvent.MOUSE_CLICKED, 1L, 0, x, y, 1, false, MouseEvent.BUTTON1);
    for (MouseListener l : panel.getMouseListeners()) {
      l.mouseClicked(e);
    }
  }
}
