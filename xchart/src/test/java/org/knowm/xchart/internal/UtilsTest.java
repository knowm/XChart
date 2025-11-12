package org.knowm.xchart.internal;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class UtilsTest {

  @Test
  void addFileExtension() {
    assertEquals(FileUtils.addFileExtension("yourchart.png", ".png"), "yourchart.png");
    assertEquals(FileUtils.addFileExtension("yourchart.png", ".pn"), "yourchart.png.pn");
    assertEquals(FileUtils.addFileExtension("a", ".png"), "a.png");
    assertEquals(FileUtils.addFileExtension("a.PNG", ".png"), "a.PNG");
  }
}
