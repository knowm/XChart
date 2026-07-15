package org.knowm.xchart;

import java.awt.event.MouseEvent;

/**
 * Listener for mouse interactions with individual rendered data points (bars, markers, pie slices,
 * bubbles, etc.). Register an implementation with {@link
 * XChartPanel#addDataPointListener(DataPointListener)} to be notified when the mouse hovers over,
 * leaves, or clicks a data point.
 *
 * <p>Collision detection reuses the same per-data-point hit shapes that drive the hover-tooltip
 * feature, so it works for every chart type without enabling tooltips. All methods are {@code
 * default} no-ops, so implementations only override the events they care about.
 *
 * <pre>{@code
 * XChartPanel<CategoryChart> panel = new XChartPanel<>(chart);
 * panel.addDataPointListener(
 *     new DataPointListener() {
 *       @Override
 *       public void onDataPointClick(ChartDataPoint dataPoint) {
 *         System.out.println("Clicked " + dataPoint.getSeriesName()
 *             + " point #" + dataPoint.getDataPointIndex());
 *       }
 *     });
 * }</pre>
 */
public interface DataPointListener {

  /**
   * Called once when the mouse moves onto a data point's shape. Not called again while the mouse
   * stays on the same data point; {@link #onDataPointExit} fires when it leaves.
   *
   * @param dataPoint the data point now under the mouse
   * @param e the originating mouse-move event (for screen coordinates, modifiers, etc.)
   */
  default void onDataPointHover(ChartDataPoint dataPoint, MouseEvent e) {}

  /**
   * Called once when the mouse leaves the data point it was hovering over (without immediately
   * entering another).
   *
   * @param dataPoint the data point the mouse just left
   * @param e the mouse-move event during which the point was left
   */
  default void onDataPointExit(ChartDataPoint dataPoint, MouseEvent e) {}

  /**
   * Called when a data point's shape is clicked. Use {@link MouseEvent#getButton()} /{@link
   * MouseEvent#isPopupTrigger()} to distinguish left-clicks from right-clicks (e.g. to show a
   * context menu).
   *
   * @param dataPoint the clicked data point
   * @param e the originating mouse-click event
   */
  default void onDataPointClick(ChartDataPoint dataPoint, MouseEvent e) {}
}
