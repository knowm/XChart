package org.knowm.xchart;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.knowm.xchart.VectorGraphicsEncoder.VectorGraphicsFormat;

/**
 * @author ekleinod
 */
public class VectorGraphicsEncoderTest {

  @Test
  public void testAddFileExtension() {

    for (VectorGraphicsFormat format : VectorGraphicsFormat.values()) {

      // test -> test.svg
      assertThat(VectorGraphicsEncoder.addFileExtension("test", format))
          .isEqualTo(String.format("test.%s", format.toString().toLowerCase()));

      // test.svg -> test.svg
      assertThat(
              VectorGraphicsEncoder.addFileExtension(
                  String.format("test.%s", format.toString().toLowerCase()), format))
          .isEqualTo(String.format("test.%s", format.toString().toLowerCase()));

      // test.svgsvg -> test.svgsvg.svg
      assertThat(
              VectorGraphicsEncoder.addFileExtension(
                  String.format("test.%1$s%1$s", format.toString().toLowerCase()), format))
          .isEqualTo(String.format("test.%1$s%1$s.%1$s", format.toString().toLowerCase()));

      // test.svg.svg -> test.svg.svg
      assertThat(
              VectorGraphicsEncoder.addFileExtension(
                  String.format("test.%1$s.%1$s", format.toString().toLowerCase()), format))
          .isEqualTo(String.format("test.%1$s.%1$s", format.toString().toLowerCase()));

      // test.txt -> test.txt.svg
      assertThat(VectorGraphicsEncoder.addFileExtension("test.txt", format))
          .isEqualTo(String.format("test.txt.%1$s", format.toString().toLowerCase()));
    }
  }
}
