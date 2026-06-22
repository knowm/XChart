package org.knowm.xchart;

import java.awt.image.BufferedImage;
import java.util.Objects;
import org.knowm.xchart.internal.chartpart.IChart;

/** A helper class with static methods for rendering Charts as console/terminal text */
public final class ConsoleEncoder {

  private static final int DEFAULT_COLUMNS = 120;
  private static final char[] RAMP = {' ', '\u2591', '\u2592', '\u2593', '\u2588'};

  /** Constructor - Private constructor to prevent instantiation */
  private ConsoleEncoder() {}

  /**
   * Generate a console string for a given chart using default dimensions
   *
   * @param chart
   * @return a console string for a given chart
   */
  public static String getConsoleString(IChart chart) {

    BufferedImage bufferedImage =
        BitmapEncoder.getBufferedImage(Objects.requireNonNull(chart, "chart cannot be null"));
    int rows =
        Math.max(
            1,
            (int)
                Math.round(
                    DEFAULT_COLUMNS
                        * bufferedImage.getHeight()
                        / (double) bufferedImage.getWidth()
                        / 2.0));
    return getConsoleString(bufferedImage, DEFAULT_COLUMNS, rows);
  }

  /**
   * Generate a console string for a given chart
   *
   * @param chart
   * @param columns number of character columns
   * @param rows number of character rows
   * @return a console string for a given chart
   */
  public static String getConsoleString(IChart chart, int columns, int rows) {

    Objects.requireNonNull(chart, "chart cannot be null");
    validateDimensions(columns, rows);
    return getConsoleString(BitmapEncoder.getBufferedImage(chart), columns, rows);
  }

  /**
   * Print a console string for a given chart to System.out
   *
   * @param chart
   */
  public static void printConsole(IChart chart) {

    System.out.print(getConsoleString(chart));
  }

  private static void validateDimensions(int columns, int rows) {

    if (columns <= 0) {
      throw new IllegalArgumentException("columns must be greater than 0");
    }
    if (rows <= 0) {
      throw new IllegalArgumentException("rows must be greater than 0");
    }
  }

  private static String getConsoleString(BufferedImage bufferedImage, int columns, int rows) {

    validateDimensions(columns, rows);

    StringBuilder builder = new StringBuilder(rows * (columns + 1));
    int imageWidth = bufferedImage.getWidth();
    int imageHeight = bufferedImage.getHeight();

    for (int row = 0; row < rows; row++) {
      int startY = getBlockStart(row, rows, imageHeight);
      int endY = getBlockEnd(row, rows, imageHeight, startY);

      for (int col = 0; col < columns; col++) {
        int startX = getBlockStart(col, columns, imageWidth);
        int endX = getBlockEnd(col, columns, imageWidth, startX);
        builder.append(getGlyph(bufferedImage, startX, endX, startY, endY));
      }

      if (row < rows - 1) {
        builder.append('\n');
      }
    }

    return builder.toString();
  }

  private static int getBlockStart(int block, int blockCount, int pixelCount) {

    return Math.min((int) Math.floor(block * (double) pixelCount / blockCount), pixelCount - 1);
  }

  private static int getBlockEnd(int block, int blockCount, int pixelCount, int blockStart) {

    int blockEnd = (int) Math.ceil((block + 1) * (double) pixelCount / blockCount);
    return Math.min(Math.max(blockEnd, blockStart + 1), pixelCount);
  }

  private static char getGlyph(
      BufferedImage bufferedImage, int startX, int endX, int startY, int endY) {

    double luminanceSum = 0;
    int pixelCount = 0;

    for (int y = startY; y < endY; y++) {
      for (int x = startX; x < endX; x++) {
        int rgb = bufferedImage.getRGB(x, y);
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;
        luminanceSum += 0.299 * red + 0.587 * green + 0.114 * blue;
        pixelCount++;
      }
    }

    double averageLuminance = luminanceSum / pixelCount;
    double darkness = 1.0 - averageLuminance / 255.0;
    int rampIndex = (int) Math.round(darkness * (RAMP.length - 1));
    return RAMP[Math.max(0, Math.min(rampIndex, RAMP.length - 1))];
  }
}
