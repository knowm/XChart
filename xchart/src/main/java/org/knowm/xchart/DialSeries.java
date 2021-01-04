package org.knowm.xchart;

import org.knowm.xchart.internal.chartpart.RenderableSeries.LegendRenderType;
import org.knowm.xchart.internal.series.Series;

/**
 * A Series containing Radar data to be plotted on a Chart
 *
 * @author timmolter
 */
public class DialSeries extends Series {

  private double value;
  private String label;

  /**
   * @param label Adds custom label for series. If label is null, it is automatically calculated.
   */
  public DialSeries(String name, double value, String label) {

    super(name);
    this.value = value;
    this.label = label;
  }

  public double getValue() {

    return value;
  }

  public void setValue(double value) {

    this.value = value;
  }

  public String getLabel() {

    return label;
  }

  // TODO solve this with class/interface heirarchy instead
  @Override
  public LegendRenderType getLegendRenderType() {

    // Dial charts don't have a legend
    return null;
  }
}
