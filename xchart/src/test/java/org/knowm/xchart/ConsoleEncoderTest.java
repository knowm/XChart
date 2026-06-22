package org.knowm.xchart;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ConsoleEncoderTest {

  @Test
  public void getConsoleStringRendersRequestedGrid() {

    XYChart chart = getChart();

    String consoleString = ConsoleEncoder.getConsoleString(chart, 80, 24);

    assertFalse(consoleString.isEmpty());
    assertTrue(consoleString.contains("\n"));
    String[] lines = consoleString.split("\n", -1);
    assertEquals(24, lines.length);
    for (String line : lines) {
      assertEquals(80, line.length());
    }
  }

  @Test
  public void getConsoleStringRendersSmallGrid() {

    XYChart chart = getChart();

    String consoleString = ConsoleEncoder.getConsoleString(chart, 10, 5);

    String[] lines = consoleString.split("\n", -1);
    assertEquals(5, lines.length);
    for (String line : lines) {
      assertEquals(10, line.length());
    }
  }

  @Test
  public void getConsoleStringMapsDarkPixelsToVisibleGlyphs() {

    XYChart chart = getChart();

    String consoleString = ConsoleEncoder.getConsoleString(chart, 80, 24);

    assertTrue(consoleString.chars().anyMatch(ch -> ch != ' ' && ch != '\n'));
    assertTrue(consoleString.chars().anyMatch(ch -> ch == ' '));
  }

  @Test
  public void getConsoleStringDefaultDimensionsDoesNotThrow() {

    XYChart chart = getChart();

    String consoleString = assertDoesNotThrow(() -> ConsoleEncoder.getConsoleString(chart));

    assertTrue(consoleString.contains("\n"));
  }

  @Test
  public void getConsoleStringRejectsInvalidArguments() {

    XYChart chart = getChart();

    assertThrows(NullPointerException.class, () -> ConsoleEncoder.getConsoleString(null));
    assertThrows(IllegalArgumentException.class, () -> ConsoleEncoder.getConsoleString(chart, 0, 24));
    assertThrows(IllegalArgumentException.class, () -> ConsoleEncoder.getConsoleString(chart, 80, 0));
    assertThrows(IllegalArgumentException.class, () -> ConsoleEncoder.getConsoleString(chart, -1, 24));
    assertThrows(IllegalArgumentException.class, () -> ConsoleEncoder.getConsoleString(chart, 80, -1));
  }

  private XYChart getChart() {

    XYChart chart = new XYChartBuilder().width(400).height(300).build();
    chart.addSeries("series", new double[] {1, 2, 3}, new double[] {4, 5, 6});
    return chart;
  }
}
