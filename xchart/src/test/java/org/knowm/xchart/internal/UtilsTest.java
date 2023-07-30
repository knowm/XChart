package org.knowm.xchart.internal;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class UtilsTest {

  @Test
  void addFileExtension() {
    assertEquals(Utils.addFileExtension("yourchart.png", ".png"), "yourchart.png");
    assertEquals(Utils.addFileExtension("yourchart.png", ".pn"), "yourchart.png.pn");
    assertEquals(Utils.addFileExtension("a", ".png"), "a.png");
    assertEquals(Utils.addFileExtension("a.PNG", ".png"), "a.PNG");
  }
}
