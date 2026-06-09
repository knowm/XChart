package org.knowm.xchart.standalone.issues;

import java.util.Arrays;
import java.util.List;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.Histogram;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.Styler;

/**
 * Reproducer for issue #481: Incorrect x-axis value for multiple histograms.
 *
 * <p>When two Histogram instances are created with the auto-range constructor and their data ranges
 * do not overlap, each histogram computes its own min/max independently. The resulting bin-center
 * x-axis lists are completely disjoint, so the CategoryChart only shows one histogram's range on
 * the x-axis.
 *
 * <p>Fix: pass an explicit shared global [min, max] to every Histogram constructor so all series
 * share the same bin boundaries.
 */
public class TestForIssue481 {

  public static void main(String[] args) {

    new SwingWrapper<>(getBugChart()).displayChart();
    new SwingWrapper<>(getFixedChart()).displayChart();
  }

  /** Reproduces the bug: each histogram auto-computes its own min/max. */
  public static CategoryChart getBugChart() {

    CategoryChart chart =
        new CategoryChartBuilder()
            .width(800)
            .height(600)
            .title("Issue #481 BUG – x-axis only shows first histogram's range")
            .xAxisTitle("Value")
            .yAxisTitle("Count")
            .build();

    chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
    chart.getStyler().setAvailableSpaceFill(0.96);

    List<Integer> data1 = Arrays.asList(0, 1, 2);
    List<Integer> data2 = Arrays.asList(-2, -1, 0);

    // Each histogram auto-detects its own range → disjoint x-axis categories
    Histogram histogram1 = new Histogram(data1, 5);
    Histogram histogram2 = new Histogram(data2, 5);

    chart.addSeries("series A [0,1,2]", histogram1.getxAxisData(), histogram1.getyAxisData());
    chart.addSeries("series B [-2,-1,0]", histogram2.getxAxisData(), histogram2.getyAxisData());

    return chart;
  }

  /**
   * Shows the fix: compute a shared global [min, max] and pass it to both Histogram constructors
   * so they produce matching bin boundaries.
   */
  public static CategoryChart getFixedChart() {

    CategoryChart chart =
        new CategoryChartBuilder()
            .width(800)
            .height(600)
            .title("Issue #481 FIXED – shared range [-2.5, 2.5] → x-axis shows [-2,-1,0,1,2]")
            .xAxisTitle("Value")
            .yAxisTitle("Count")
            .build();

    chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
    chart.getStyler().setAvailableSpaceFill(0.96);

    List<Integer> data1 = Arrays.asList(0, 1, 2);
    List<Integer> data2 = Arrays.asList(-2, -1, 0);

    // To get bin centers at exactly -2,-1,0,1,2 we need binSize=1.
    // binSize = (max - min) / numBins → 1 = range / 5 → range = 5.
    // Extend by half a bin on each side of the data extent:
    //   data spans [-2, 2], halfBin = 0.5 → globalMin=-2.5, globalMax=2.5
    int numBins = 5;
    double globalMin = -2.5;
    double globalMax = 2.5;

    Histogram histogram1 = new Histogram(data1, numBins, globalMin, globalMax);
    Histogram histogram2 = new Histogram(data2, numBins, globalMin, globalMax);

    chart.addSeries("series A [0,1,2]", histogram1.getxAxisData(), histogram1.getyAxisData());
    chart.addSeries("series B [-2,-1,0]", histogram2.getxAxisData(), histogram2.getyAxisData());

    return chart;
  }
}
