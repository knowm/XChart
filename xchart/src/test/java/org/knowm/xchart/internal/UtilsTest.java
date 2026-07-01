package org.knowm.xchart.internal;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import org.junit.jupiter.api.Test;

public class UtilsTest {

  /** 2021-01-01T00:00:00Z */
  private static final long EPOCH_MILLIS = 1_609_459_200_000L;

  private static final ZoneId UTC = ZoneOffset.UTC;

  @Test
  void addFileExtension() {
    assertEquals(Utils.addFileExtension("yourchart.png", ".png"), "yourchart.png");
    assertEquals(Utils.addFileExtension("yourchart.png", ".pn"), "yourchart.png.pn");
    assertEquals(Utils.addFileExtension("a", ".png"), "a.png");
    assertEquals(Utils.addFileExtension("a.PNG", ".png"), "a.PNG");
  }

  @Test
  void nullDateListReturnsNull() {
    assertEquals(null, Utils.getDoubleArrayFromDateList(null, UTC));
  }

  @Test
  void legacyDateConvertsToEpochMillis() {
    double[] result =
        Utils.getDoubleArrayFromDateList(Collections.singletonList(new Date(EPOCH_MILLIS)), UTC);
    assertArrayEquals(new double[] {EPOCH_MILLIS}, result);
  }

  @Test
  void javaTimeTypesAllMapToTheSameInstant() {
    // The same moment expressed with different java.time types must all yield the same epoch millis.
    Date date = new Date(EPOCH_MILLIS);
    Instant instant = Instant.ofEpochMilli(EPOCH_MILLIS);
    ZonedDateTime zdt = ZonedDateTime.ofInstant(instant, UTC);
    OffsetDateTime odt = OffsetDateTime.ofInstant(instant, UTC);
    LocalDateTime ldt = LocalDateTime.ofInstant(instant, UTC);
    LocalDate ld = LocalDate.of(2021, 1, 1);

    double[] result =
        Utils.getDoubleArrayFromDateList(Arrays.asList(date, instant, zdt, odt, ldt, ld), UTC);

    assertArrayEquals(
        new double[] {
          EPOCH_MILLIS, EPOCH_MILLIS, EPOCH_MILLIS, EPOCH_MILLIS, EPOCH_MILLIS, EPOCH_MILLIS
        },
        result);
  }

  @Test
  void zonedTypesIgnoreSuppliedZone() {
    // Instant/ZonedDateTime carry their own offset, so the zoneId argument must not affect them.
    Instant instant = Instant.ofEpochMilli(EPOCH_MILLIS);
    double[] utc = Utils.getDoubleArrayFromDateList(Collections.singletonList(instant), UTC);
    double[] tokyo =
        Utils.getDoubleArrayFromDateList(
            Collections.singletonList(instant), ZoneId.of("Asia/Tokyo"));
    assertArrayEquals(utc, tokyo);
  }

  @Test
  void localTypesRespectSuppliedZone() {
    // A zone-less LocalDateTime resolves to a different instant depending on the zone.
    LocalDateTime ldt = LocalDateTime.of(2021, 1, 1, 0, 0);
    double utc = Utils.getDoubleArrayFromDateList(Collections.singletonList(ldt), UTC)[0];
    double tokyo =
        Utils.getDoubleArrayFromDateList(
            Collections.singletonList(ldt), ZoneId.of("Asia/Tokyo"))[0];
    // Tokyo is UTC+9, so the same wall-clock time is an earlier instant there.
    assertEquals(9 * 60 * 60 * 1000.0, utc - tokyo);
  }

  @Test
  void localTimeAnchorsToEpochDay() {
    double millis =
        Utils.getDoubleArrayFromDateList(Collections.singletonList(LocalTime.of(1, 0)), UTC)[0];
    assertEquals(60 * 60 * 1000.0, millis);
  }

  @Test
  void unsupportedTypeThrows() {
    assertThrows(
        IllegalArgumentException.class,
        () -> Utils.getDoubleArrayFromDateList(Collections.singletonList("not a date"), UTC));
  }
}
