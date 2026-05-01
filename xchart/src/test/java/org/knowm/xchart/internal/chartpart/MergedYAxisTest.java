package org.knowm.xchart.internal.chartpart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.markers.SeriesMarkers;

/** Unit tests for merged / visually-grouped Y-axes. */
public class MergedYAxisTest {

  // ---------------------------------------------------------------
  // helpers
  // ---------------------------------------------------------------

  private static XYChart buildTwoAxisChart() {
    XYChart chart = new XYChartBuilder().width(800).height(600).build();

    List<Double> x = new ArrayList<>();
    List<Double> y0 = new ArrayList<>();
    List<Double> y1 = new ArrayList<>();
    for (int i = 1; i <= 10; i++) {
      x.add((double) i);
      y0.add((double) i); // scale 1–10
      y1.add(i * 100.0); // scale 100–1000
    }

    XYSeries s0 = chart.addSeries("series0", x, y0);
    s0.setYAxisGroup(0);
    s0.setMarker(SeriesMarkers.NONE);

    XYSeries s1 = chart.addSeries("series1", x, y1);
    s1.setYAxisGroup(1);
    s1.setMarker(SeriesMarkers.NONE);

    return chart;
  }

  // ---------------------------------------------------------------
  // 1. API validation
  // ---------------------------------------------------------------

  @Test
  public void mergeRequiresAtLeastTwoGroups() {
    XYChart chart = buildTwoAxisChart();
    assertThatThrownBy(() -> chart.getStyler().mergeYAxisGroups(0))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("at least 2");
  }

  @Test
  public void visualGroupIdIsLowestLogicalIndex() {
    XYChart chart = buildTwoAxisChart();
    chart.getStyler().mergeYAxisGroups(0, 1);

    // Both logical groups should resolve to visual group 0 (the minimum)
    assertThat(chart.getStyler().getYAxisVisualGroup(0)).isEqualTo(0);
    assertThat(chart.getStyler().getYAxisVisualGroup(1)).isEqualTo(0);
  }

  @Test
  public void unmergedGroupHasIdentityVisualGroup() {
    XYChart chart = buildTwoAxisChart();
    // No merging called — visual group = logical group
    assertThat(chart.getStyler().getYAxisVisualGroup(0)).isEqualTo(0);
    assertThat(chart.getStyler().getYAxisVisualGroup(1)).isEqualTo(1);
  }

  @Test
  public void isSlaveReturnsFalseForMaster() {
    XYChart chart = buildTwoAxisChart();
    chart.getStyler().mergeYAxisGroups(0, 1);

    assertThat(chart.getStyler().isYAxisGroupSlave(0)).isFalse(); // 0 is master
    assertThat(chart.getStyler().isYAxisGroupSlave(1)).isTrue(); // 1 is slave
  }

  // ---------------------------------------------------------------
  // 2. Tick synchronization — after rendering the tick pixel
  //    positions of master and slave must be identical.
  // ---------------------------------------------------------------

  @Test
  public void slaveBorrowsMasterTickLocations() throws Exception {
    XYChart chart = buildTwoAxisChart();
    chart.getStyler().mergeYAxisGroups(0, 1);

    // Render into a bitmap to trigger AxisPair.paint() / preparePaint()
    BitmapEncoder.getBitmapBytes(chart, BitmapEncoder.BitmapFormat.PNG);

    Axis masterAxis = chart.axisPair.getYAxis(0);
    Axis slaveAxis = chart.axisPair.getYAxis(1);

    List<Double> masterLocs = masterAxis.getAxisTickCalculator().getTickLocations();
    List<Double> slaveLocs = slaveAxis.getAxisTickCalculator().getTickLocations();

    assertThat(slaveLocs).isNotEmpty();
    assertThat(slaveLocs).containsExactlyElementsOf(masterLocs);
  }

  @Test
  public void slaveTickLabelsCountMatchesMaster() throws Exception {
    XYChart chart = buildTwoAxisChart();
    chart.getStyler().mergeYAxisGroups(0, 1);

    BitmapEncoder.getBitmapBytes(chart, BitmapEncoder.BitmapFormat.PNG);

    Axis masterAxis = chart.axisPair.getYAxis(0);
    Axis slaveAxis = chart.axisPair.getYAxis(1);

    assertThat(slaveAxis.getAxisTickCalculator().getTickLabels())
        .hasSameSizeAs(masterAxis.getAxisTickCalculator().getTickLabels());
  }

  // ---------------------------------------------------------------
  // 3. Axis-line ownership
  // ---------------------------------------------------------------

  @Test
  public void masterIsAxisLineOwner() throws Exception {
    XYChart chart = buildTwoAxisChart();
    chart.getStyler().mergeYAxisGroups(0, 1);

    BitmapEncoder.getBitmapBytes(chart, BitmapEncoder.BitmapFormat.PNG);

    Axis masterAxis = chart.axisPair.getYAxis(0);
    Axis slaveAxis = chart.axisPair.getYAxis(1);

    assertThat(masterAxis.isAxisLineOwner()).isTrue();
    assertThat(slaveAxis.isAxisLineOwner()).isFalse();
  }

  // ---------------------------------------------------------------
  // 4. Gridline master resolution
  // ---------------------------------------------------------------

  @Test
  public void gridlineMasterIsLowestIndexAxisInFirstGroup() throws Exception {
    XYChart chart = buildTwoAxisChart();
    chart.getStyler().mergeYAxisGroups(0, 1);

    BitmapEncoder.getBitmapBytes(chart, BitmapEncoder.BitmapFormat.PNG);

    Axis gridlineMaster = chart.axisPair.getGridlineMasterAxis();
    assertThat(gridlineMaster.getYIndex()).isEqualTo(0);
  }

  @Test
  public void gridlineMasterEqualsLowestIndexAxisWhenNoMerge() throws Exception {
    XYChart chart = buildTwoAxisChart();
    // No merge — gridline master should be the primary axis (index 0)

    BitmapEncoder.getBitmapBytes(chart, BitmapEncoder.BitmapFormat.PNG);

    Axis gridlineMaster = chart.axisPair.getGridlineMasterAxis();
    // The primary Y-axis (group 0) is the innermost on the left, so it drives gridlines.
    assertThat(gridlineMaster.getYIndex()).isEqualTo(0);
  }

  // ---------------------------------------------------------------
  // 5. Backward compatibility — charts without merging must still
  //    render without exception and produce a non-empty PNG.
  // ---------------------------------------------------------------

  @Test
  public void noRegressionUnmergedTwoAxisChart() throws Exception {
    XYChart chart = buildTwoAxisChart();
    // No merge called — must produce valid output
    byte[] png = BitmapEncoder.getBitmapBytes(chart, BitmapEncoder.BitmapFormat.PNG);
    assertThat(png).isNotEmpty();
  }

  @Test
  public void noRegressionSingleAxisChart() throws Exception {
    XYChart chart = new XYChartBuilder().width(600).height(400).build();
    List<Double> x = List.of(1.0, 2.0, 3.0);
    List<Double> y = List.of(10.0, 20.0, 30.0);
    chart.addSeries("s", x, y).setMarker(SeriesMarkers.NONE);

    byte[] png = BitmapEncoder.getBitmapBytes(chart, BitmapEncoder.BitmapFormat.PNG);
    assertThat(png).isNotEmpty();
  }

  // ---------------------------------------------------------------
  // 6. Right-side merged axes
  // ---------------------------------------------------------------

  @Test
  public void rightSideMergeRenders() throws Exception {
    XYChart chart = new XYChartBuilder().width(800).height(600).build();

    List<Double> x = List.of(1.0, 2.0, 3.0, 4.0, 5.0);

    XYSeries s0 = chart.addSeries("left", x, List.of(1.0, 2.0, 3.0, 4.0, 5.0));
    s0.setYAxisGroup(0);
    s0.setMarker(SeriesMarkers.NONE);

    XYSeries s1 = chart.addSeries("right0", x, List.of(100.0, 200.0, 300.0, 400.0, 500.0));
    s1.setYAxisGroup(1);
    s1.setMarker(SeriesMarkers.NONE);

    XYSeries s2 = chart.addSeries("right1", x, List.of(0.1, 0.2, 0.3, 0.4, 0.5));
    s2.setYAxisGroup(2);
    s2.setMarker(SeriesMarkers.NONE);

    chart.getStyler().setYAxisGroupPosition(1, Styler.YAxisPosition.Right);
    chart.getStyler().setYAxisGroupPosition(2, Styler.YAxisPosition.Right);

    chart.getStyler().mergeYAxisGroups(1, 2);

    byte[] png = BitmapEncoder.getBitmapBytes(chart, BitmapEncoder.BitmapFormat.PNG);
    assertThat(png).isNotEmpty();

    // Right-side gridline master should be axis 1 (lowest index in the right merge group)
    Axis rightMaster = chart.axisPair.getRightGridlineMasterAxis();
    assertThat(rightMaster.getYIndex()).isEqualTo(1);
    assertThat(rightMaster.isAxisLineOwner()).isTrue();

    Axis rightSlave = chart.axisPair.getYAxis(2);
    assertThat(rightSlave.isAxisLineOwner()).isFalse();
  }
}
