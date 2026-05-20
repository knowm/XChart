package org.knowm.xchart.internal.chartpart;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.Format;
import java.util.List;

import org.knowm.xchart.internal.chartpart.Axis.Direction;
import org.knowm.xchart.style.AxesChartStyler;

/**
 * A tick calculator for a "slave" Y-axis in a merged visual group.
 *
 * <p>The slave borrows its master axis's pixel positions ({@code tickLocations}) verbatim, then
 * translates each position back to a data-domain value on the slave's own scale and formats it
 * accordingly. This guarantees that master and slave tick marks land at identical Y-pixel
 * coordinates while showing independent label sets.
 *
 * <p>This class is only instantiated by {@link Axis} when a master axis has been assigned via
 * {@link Axis#setMasterAxis(Axis)}.
 */
class AxisTickCalculator_Synchronized extends AxisTickCalculator_ {

  private final Format slaveFormat;

  /**
   * @param workingSpace the pixel height of the Y-axis (same value used by the master)
   * @param minValue slave axis minimum data value
   * @param maxValue slave axis maximum data value
   * @param masterTickLocations immutable list of pixel positions from the master axis calculator
   * @param styler the chart styler (used for formatting)
   * @param yIndex the logical Y-axis group index of the slave
   */
  AxisTickCalculator_Synchronized(
      double workingSpace,
      double minValue,
      double maxValue,
      List<Double> masterTickLocations,
      AxesChartStyler styler,
      int yIndex) {

    super(Direction.Y, workingSpace, minValue, maxValue, styler);

    // Build label formatter that uses slave's decimal pattern when set
    this.slaveFormat = new Formatter_Number(styler, Direction.Y, minValue, maxValue, yIndex);
    this.axisFormat = slaveFormat;

    populate(masterTickLocations);
  }

  /**
   * Copy the master's pixel positions and compute label values for the slave's data domain.
   *
   * <p>For a Y-axis the pixel space runs bottom-to-top: a pixel location {@code p} from the bottom
   * corresponds to the normalized fraction {@code p / workingSpace}, so the data value is:
   *
   * <pre>  value = minValue + (p / workingSpace) * (maxValue - minValue)</pre>
   *
   * @param masterLocations the master axis tick locations in pixels (measured from bottom of axis)
   */
  private void populate(List<Double> masterLocations) {
    double span = maxValue - minValue;

    for (Double pixelLocation : masterLocations) {
      // Pixel location is measured from bottom; 0 = minValue, workingSpace = maxValue.
      // Use the same formula as AxisTickCalculator_.calculate() to keep transforms consistent.
      double tickSpace = styler.getPlotContentSize() * workingSpace;
      double margin = (workingSpace - tickSpace) / 2.0;

      double value;
      if (tickSpace <= 0 || span == 0) {
        value = minValue;
      } else {
        // invert: tickLocation = margin + ((value - minValue) / span) * tickSpace
        value = minValue + ((pixelLocation - margin) / tickSpace) * span;
        // Round to 10 significant figures to eliminate floating-point round-trip noise
        // (e.g. 399.9999999999994 → 400.0).
        value = BigDecimal.valueOf(value).round(new MathContext(10)).doubleValue();
      }

      tickLocations.add(pixelLocation);
      // Null labels (outside range) match the master's null entries so that the rendering code
      // skips them consistently.
      if (pixelLocation < margin || pixelLocation > margin + tickSpace) {
        tickLabels.add(null);
      } else {
        tickLabels.add(slaveFormat.format(value));
      }
    }
  }

  @Override
  public Format getAxisFormat() {
    return slaveFormat;
  }
}
