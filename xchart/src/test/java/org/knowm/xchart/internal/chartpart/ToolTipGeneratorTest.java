package org.knowm.xchart.internal.chartpart;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.knowm.xchart.BubbleChart;
import org.knowm.xchart.BubbleChartBuilder;
import org.knowm.xchart.BubbleSeries;
import org.knowm.xchart.ChartDataPoint;
import org.knowm.xchart.HeatMapChart;
import org.knowm.xchart.HeatMapChartBuilder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

// https://github.com/knowm/XChart/issues/679
// A ToolTipGenerator set on any series replaces the default tooltip labels with custom text built
// from the ChartDataPoint (series name + data point index + default labels).
//
// Exercises the interaction data directly (no XChartPanel / Swing display) so it runs headless on
// CI.
class ToolTipGeneratorTest {

  /** Accesses the package-private interaction data through a same-package Chart reference. */
  private static PlotInteractionData interactionData(Chart<?, ?> chart) {

    return chart.getInteractionData();
  }

  /** Paints the chart into an off-screen image so the interaction data gets collected. */
  private static void render(Chart<?, ?> chart) {

    BufferedImage image =
        new BufferedImage(chart.getWidth(), chart.getHeight(), BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = image.createGraphics();
    chart.paint(g, chart.getWidth(), chart.getHeight());
    g.dispose();
  }

  @Test
  void generatorReplacesDefaultLabelsOnXYChart() {

    XYChart chart = new XYChartBuilder().width(800).height(600).build();
    chart
        .addSeries("s", new double[] {1, 2, 3}, new double[] {10, 20, 30})
        .setToolTipGenerator(dataPoint -> "custom #" + dataPoint.getDataPointIndex());
    chart.enableInteractionData();

    render(chart);

    List<PlotInteractionData.ToolTipData> toolTips =
        interactionData(chart).getToolTipDataList();
    assertThat(toolTips).hasSize(3);
    for (int i = 0; i < 3; i++) {
      assertThat(toolTips.get(i).getCustomLabel()).isEqualTo("custom #" + i);
    }
  }

  @Test
  void generatorReceivesSeriesIdentityAndDefaultLabels() {

    List<ChartDataPoint> seen = new ArrayList<>();

    XYChart chart = new XYChartBuilder().width(800).height(600).build();
    chart
        .addSeries("mySeries", new double[] {1, 2}, new double[] {10, 20})
        .setToolTipGenerator(
            dataPoint -> {
              seen.add(dataPoint);
              return null; // fall back to the default label
            });
    chart.enableInteractionData();

    render(chart);

    assertThat(seen).hasSize(2);
    assertThat(seen.get(0).getSeriesName()).isEqualTo("mySeries");
    assertThat(seen.get(0).getDataPointIndex()).isZero();
    assertThat(seen.get(1).getDataPointIndex()).isEqualTo(1);
    // XY tooltips are x/y pairs, so the pair fields carry the default formatted values
    assertThat(seen.get(1).getXValue()).isNotNull();
    assertThat(seen.get(1).getYValue()).isNotNull();

    // generator returned null, so the default labels stay in effect
    for (PlotInteractionData.ToolTipData td : interactionData(chart).getToolTipDataList()) {
      assertThat(td.getCustomLabel()).isNull();
    }
  }

  // the original issue #679 request: extra per-cell information on a heat map
  @Test
  void generatorWorksOnHeatMapCells() {

    HeatMapChart chart = new HeatMapChartBuilder().width(800).height(600).build();
    List<Integer> xData = java.util.Arrays.asList(0, 1);
    List<Integer> yData = java.util.Arrays.asList(0, 1);
    List<Number[]> heatData = new ArrayList<>();
    heatData.add(new Number[] {0, 0, 5});
    heatData.add(new Number[] {0, 1, 6});
    heatData.add(new Number[] {1, 0, 7});
    heatData.add(new Number[] {1, 1, 8});
    chart
        .addSeries("heat", xData, yData, heatData)
        .setToolTipGenerator(
            dataPoint ->
                "cell "
                    + dataPoint.getDataPointIndex()
                    + " extra info"
                    + System.lineSeparator()
                    + "default: "
                    + dataPoint.getLabel());
    chart.enableInteractionData();

    render(chart);

    List<PlotInteractionData.ToolTipData> toolTips =
        interactionData(chart).getToolTipDataList();
    assertThat(toolTips).hasSize(4);
    for (int i = 0; i < 4; i++) {
      assertThat(toolTips.get(i).seriesName).isEqualTo("heat");
      assertThat(toolTips.get(i).getCustomLabel())
          .startsWith("cell " + i + " extra info")
          .contains("default: heat: ");
    }
  }

  @Test
  void generatorTakesPrecedenceOverDeprecatedPerPointStrings() {

    BubbleChart chart = new BubbleChartBuilder().width(800).height(600).build();
    BubbleSeries series =
        chart.addSeries("b", new double[] {1, 2}, new double[] {10, 20}, new double[] {5, 5});
    series.setCustomToolTips(true).setToolTips(new String[] {"old 0", "old 1"});
    series.setToolTipGenerator(dataPoint -> "new " + dataPoint.getDataPointIndex());
    chart.enableInteractionData();

    render(chart);

    List<PlotInteractionData.ToolTipData> toolTips =
        interactionData(chart).getToolTipDataList();
    assertThat(toolTips).hasSize(2);
    for (int i = 0; i < 2; i++) {
      // the deprecated string became the default label, the generator's label wins
      assertThat(toolTips.get(i).label).isEqualTo("old " + i);
      assertThat(toolTips.get(i).getCustomLabel()).isEqualTo("new " + i);
    }
  }

  @Test
  void deprecatedPerPointStringsStillWorkWithoutGenerator() {

    BubbleChart chart = new BubbleChartBuilder().width(800).height(600).build();
    chart
        .addSeries("b", new double[] {1, 2}, new double[] {10, 20}, new double[] {5, 5})
        .setCustomToolTips(true)
        .setToolTips(new String[] {"old 0", "old 1"});
    chart.enableInteractionData();

    render(chart);

    List<PlotInteractionData.ToolTipData> toolTips =
        interactionData(chart).getToolTipDataList();
    assertThat(toolTips).hasSize(2);
    for (int i = 0; i < 2; i++) {
      assertThat(toolTips.get(i).label).isEqualTo("old " + i);
      assertThat(toolTips.get(i).getCustomLabel()).isNull();
    }
  }

  @Test
  void dispatcherReportsTheDisplayedCustomLabel() {

    XYChart chart = new XYChartBuilder().width(800).height(600).build();
    chart
        .addSeries("s", new double[] {1}, new double[] {10})
        .setToolTipGenerator(dataPoint -> "displayed");
    chart.enableInteractionData();

    render(chart);

    DataPointDispatcher dispatcher = new DataPointDispatcher(new ArrayList<>());
    dispatcher.setData(interactionData(chart));

    PlotInteractionData.ToolTipData td = interactionData(chart).getToolTipDataList().get(0);
    assertThat(dispatcher.isOverDataPoint((int) td.x, (int) td.y)).isTrue();
  }
}
