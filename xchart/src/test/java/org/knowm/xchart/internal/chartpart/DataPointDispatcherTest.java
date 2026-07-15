package org.knowm.xchart.internal.chartpart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import org.junit.jupiter.api.Test;
import org.knowm.xchart.ChartDataPoint;
import org.knowm.xchart.DataPointListener;

// https://github.com/knowm/XChart/issues/492
// Exercises the dispatcher directly (no XChartPanel / Swing display) so it runs headless on CI.
class DataPointDispatcherTest {

  // MouseEvent needs a non-null source Component; a JPanel is fine to construct headless.
  private final Component source = new JPanel();

  private static PlotInteractionData twoBars() {
    PlotInteractionData data = new PlotInteractionData();
    data.addToolTip(new Rectangle2D.Double(10, 10, 20, 20), 20, 20, 20, "0", "4")
        .withSeries("s", 0);
    data.addToolTip(new Rectangle2D.Double(50, 10, 20, 20), 60, 20, 20, "1", "5")
        .withSeries("s", 1);
    return data;
  }

  private MouseEvent moved(int x, int y) {
    return new MouseEvent(source, MouseEvent.MOUSE_MOVED, 1L, 0, x, y, 0, false);
  }

  private MouseEvent clicked(int x, int y) {
    return new MouseEvent(source, MouseEvent.MOUSE_CLICKED, 1L, 0, x, y, 1, false, MouseEvent.BUTTON1);
  }

  private MouseEvent exited(int x, int y) {
    return new MouseEvent(source, MouseEvent.MOUSE_EXITED, 1L, 0, x, y, 0, false);
  }

  @Test
  void hoverExitClickAcrossBars() {

    List<ChartDataPoint> hovers = new ArrayList<>();
    List<ChartDataPoint> exits = new ArrayList<>();
    List<ChartDataPoint> clicks = new ArrayList<>();

    List<DataPointListener> listeners = new ArrayList<>();
    listeners.add(
        new DataPointListener() {
          @Override
          public void onDataPointHover(ChartDataPoint p, MouseEvent e) {
            hovers.add(p);
          }

          @Override
          public void onDataPointExit(ChartDataPoint p, MouseEvent e) {
            exits.add(p);
          }

          @Override
          public void onDataPointClick(ChartDataPoint p, MouseEvent e) {
            clicks.add(p);
          }
        });

    DataPointDispatcher dispatcher = new DataPointDispatcher(listeners);
    dispatcher.setData(twoBars());

    // enter first bar
    dispatcher.mouseMoved(moved(20, 20));
    // move within the same bar -> no new hover
    dispatcher.mouseMoved(moved(22, 22));
    // move into the gap between bars -> exit
    dispatcher.mouseMoved(moved(40, 20));
    // enter second bar -> hover
    dispatcher.mouseMoved(moved(60, 20));
    // click the second bar
    dispatcher.mouseClicked(clicked(60, 20));

    assertThat(hovers).hasSize(2);
    assertThat(hovers.get(0).getSeriesName()).isEqualTo("s");
    assertThat(hovers.get(0).getDataPointIndex()).isEqualTo(0);
    assertThat(hovers.get(1).getDataPointIndex()).isEqualTo(1);

    assertThat(exits).hasSize(1);
    assertThat(exits.get(0).getDataPointIndex()).isEqualTo(0);

    assertThat(clicks).hasSize(1);
    assertThat(clicks.get(0).getDataPointIndex()).isEqualTo(1);
  }

  @Test
  void mouseExitedClearsHoverAndFiresExit() {

    List<ChartDataPoint> exits = new ArrayList<>();
    List<DataPointListener> listeners = new ArrayList<>();
    listeners.add(
        new DataPointListener() {
          @Override
          public void onDataPointExit(ChartDataPoint p, MouseEvent e) {
            exits.add(p);
          }
        });

    DataPointDispatcher dispatcher = new DataPointDispatcher(listeners);
    dispatcher.setData(twoBars());

    dispatcher.mouseMoved(moved(20, 20)); // hover bar 0
    dispatcher.mouseExited(exited(0, 0)); // leave the panel while hovering

    assertThat(exits).hasSize(1);
    assertThat(exits.get(0).getDataPointIndex()).isEqualTo(0);

    // a second exit with nothing hovered should be a no-op
    dispatcher.mouseExited(exited(0, 0));
    assertThat(exits).hasSize(1);
  }

  @Test
  void listenerMutatingListDuringCallbackDoesNotThrow() {

    List<ChartDataPoint> hovers = new ArrayList<>();
    List<DataPointListener> listeners = new ArrayList<>();
    // one-shot listener that removes itself from inside the callback
    DataPointListener oneShot =
        new DataPointListener() {
          @Override
          public void onDataPointHover(ChartDataPoint p, MouseEvent e) {
            hovers.add(p);
            listeners.remove(this);
          }
        };
    listeners.add(oneShot);

    DataPointDispatcher dispatcher = new DataPointDispatcher(listeners);
    dispatcher.setData(twoBars());

    assertThatCode(() -> dispatcher.mouseMoved(moved(20, 20))).doesNotThrowAnyException();
    assertThat(hovers).hasSize(1);
    assertThat(listeners).isEmpty();
  }

  @Test
  void isOverDataPointMatchesShapes() {

    DataPointDispatcher dispatcher = new DataPointDispatcher(new ArrayList<>());
    dispatcher.setData(twoBars());

    assertThat(dispatcher.isOverDataPoint(20, 20)).isTrue(); // inside bar 0
    assertThat(dispatcher.isOverDataPoint(60, 20)).isTrue(); // inside bar 1
    assertThat(dispatcher.isOverDataPoint(40, 20)).isFalse(); // gap between bars
  }
}
