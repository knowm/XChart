package org.knowm.xchart.internal;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Utils {

  /** Private Constructor */
  private Utils() {}

  /**
   * Gets the offset for the beginning of the tick marks
   *
   * @param workingSpace
   * @param tickSpace
   * @return
   */
  public static double getTickStartOffset(double workingSpace, double tickSpace) {

    double marginSpace = workingSpace - tickSpace;
    return marginSpace / 2.0;
  }

  public static double pow(double base, int exponent) {

    if (exponent > 0) {
      return Math.pow(base, exponent);
    } else {
      return 1.0 / Math.pow(base, -1 * exponent);
    }
  }

  public static List<Double> getNumberListFromDoubleArray(double[] data) {

    if (data == null) {
      return null;
    }

    List<Double> dataNumber;
    dataNumber = new ArrayList<Double>();
    for (double d : data) {
      dataNumber.add(d);
    }
    return dataNumber;
  }

  public static List<Double> getNumberListFromIntArray(int[] data) {

    if (data == null) {
      return null;
    }

    List<Double> dataNumber;
    dataNumber = new ArrayList<Double>();
    for (double d : data) {
      dataNumber.add(d);
    }
    return dataNumber;
  }

  public static List<Double> getGeneratedDataAsList(int length) {

    List<Double> generatedData = new ArrayList<>();
    for (int i = 1; i < length + 1; i++) {
      generatedData.add((double) i);
    }
    return generatedData;
  }

  public static double[] getDoubleArrayFromFloatArray(float[] data) {

    if (data == null) {
      return null;
    }
    double[] doubles = new double[data.length];

    for (int i = 0; i < data.length; i++) {
      doubles[i] = data[i];
    }
    return doubles;
  }

  public static double[] getDoubleArrayFromIntArray(int[] data) {

    if (data == null) {
      return null;
    }
    double[] doubles = new double[data.length];

    for (int i = 0; i < data.length; i++) {
      doubles[i] = data[i];
    }
    return doubles;
  }

  public static double[] getDoubleArrayFromNumberList(List<?> data) {

    if (data == null) {
      return null;
    }
    double[] doubles = new double[data.size()];

    int i = 0;
    for (Object number : data) {
      if (number == null) {
        doubles[i++] = Double.NaN;
      } else {
        doubles[i++] = ((Number) number).doubleValue();
      }
    }
    return doubles;
  }

  public static double[] getDoubleArrayFromDateList(List<?> data) {

    return getDoubleArrayFromDateList(data, ZoneId.systemDefault());
  }

  /**
   * Converts a list of date/time values to an array of epoch milliseconds. Supports {@link Date} as
   * well as the {@code java.time} types {@link Instant}, {@link ZonedDateTime}, {@link
   * OffsetDateTime}, {@link LocalDateTime}, {@link LocalDate} and {@link LocalTime}. The supplied
   * {@code zoneId} is only used for the zone-less types ({@code LocalDateTime}, {@code LocalDate},
   * {@code LocalTime}); the zoned/instant types carry their own offset. Pass the chart's styler
   * timezone so that conversion and axis-label formatting agree.
   */
  public static double[] getDoubleArrayFromDateList(List<?> data, ZoneId zoneId) {

    if (data == null) {
      return null;
    }
    double[] doubles = new double[data.size()];

    int i = 0;
    for (Object date : data) {
      doubles[i++] = toEpochMillis(date, zoneId);
    }
    return doubles;
  }

  private static double toEpochMillis(Object value, ZoneId zoneId) {

    if (value instanceof Date) {
      return ((Date) value).getTime();
    } else if (value instanceof Instant) {
      return ((Instant) value).toEpochMilli();
    } else if (value instanceof ZonedDateTime) {
      return ((ZonedDateTime) value).toInstant().toEpochMilli();
    } else if (value instanceof OffsetDateTime) {
      return ((OffsetDateTime) value).toInstant().toEpochMilli();
    } else if (value instanceof LocalDateTime) {
      return ((LocalDateTime) value).atZone(zoneId).toInstant().toEpochMilli();
    } else if (value instanceof LocalDate) {
      return ((LocalDate) value).atStartOfDay(zoneId).toInstant().toEpochMilli();
    } else if (value instanceof LocalTime) {
      // anchor a time-of-day to the epoch day so it renders correctly with a time-only pattern
      return ((LocalTime) value).atDate(LocalDate.ofEpochDay(0)).atZone(zoneId).toInstant().toEpochMilli();
    }
    throw new IllegalArgumentException(
        "Unsupported date/time type: "
            + (value == null ? "null" : value.getClass().getName())
            + ". Supported types are java.util.Date, Instant, ZonedDateTime, OffsetDateTime, "
            + "LocalDateTime, LocalDate and LocalTime.");
  }

  public static double[] getGeneratedDataAsArray(int length) {

    double[] generatedData = new double[length];
    for (int i = 0; i < length; i++) {
      generatedData[i] = ((double) i + 1);
    }
    return generatedData;
  }

  public static long[] getLongArrayFromIntArray(int[] data) {

    if (data == null) {
      return null;
    }
    long[] longs = new long[data.length];

    for (int i = 0; i < data.length; i++) {
      longs[i] = data[i];
    }
    return longs;
  }

  public static long[] getLongArrayFromFloatArray(float[] data) {

    if (data == null) {
      return null;
    }
    long[] longs = new long[data.length];

    for (int i = 0; i < data.length; i++) {
      longs[i] = (long) data[i];
    }
    return longs;
  }

  public static long[] getLongArrayFromNumberList(List<?> data) {

    if (data == null) {
      return null;
    }
    long[] longs = new long[data.size()];

    int i = 0;
    for (Object number : data) {
      if (number == null) {
        longs[i++] = 0;
      } else {
        longs[i++] = ((Number) number).longValue();
      }
    }
    return longs;
  }

  /**
   * Only adds the extension of the fileExtension to the filename if the filename doesn't already
   * have it.
   *
   * @param fileName File name
   * @param fileExtension File extension
   * @return filename (if extension already exists), otherwise;: filename + fileExtension
   */
  public static String addFileExtension(String fileName, String fileExtension) {
    String fileNameWithFileExtension = fileName;
    if (fileName.length() <= fileExtension.length()
        || !fileName
            .substring(fileName.length() - fileExtension.length())
            .equalsIgnoreCase(fileExtension)) {
      fileNameWithFileExtension = fileName + fileExtension;
    }
    return fileNameWithFileExtension;
  }
}
