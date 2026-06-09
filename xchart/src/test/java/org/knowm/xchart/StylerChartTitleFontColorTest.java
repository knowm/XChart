package org.knowm.xchart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.knowm.xchart.style.Styler.ChartTheme.GGPlot2;

import java.awt.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class StylerChartTitleFontColorTest {

  private XYChart chart;

  @BeforeEach
  void setUp() {
    chart = new XYChart(800, 600, GGPlot2);
  }

  @Test
  void defaultFallsBackToChartFontColor() {
    Color expected = chart.getStyler().getChartFontColor();
    assertThat(chart.getStyler().getChartTitleFontColor()).isEqualTo(expected);
  }

  @Test
  void setChartTitleFontColorOverridesChartFontColor() {
    Color titleColor = Color.RED;
    chart.getStyler().setChartTitleFontColor(titleColor);
    assertThat(chart.getStyler().getChartTitleFontColor()).isEqualTo(titleColor);
  }

  @Test
  void chartFontColorStillUnaffectedByTitleColorOverride() {
    Color originalFontColor = chart.getStyler().getChartFontColor();
    chart.getStyler().setChartTitleFontColor(Color.BLUE);
    assertThat(chart.getStyler().getChartFontColor()).isEqualTo(originalFontColor);
  }

  @Test
  void setChartTitleFontColorToNullFallsBackToChartFontColor() {
    chart.getStyler().setChartTitleFontColor(Color.GREEN);
    chart.getStyler().setChartTitleFontColor(null);
    assertThat(chart.getStyler().getChartTitleFontColor())
        .isEqualTo(chart.getStyler().getChartFontColor());
  }
}
