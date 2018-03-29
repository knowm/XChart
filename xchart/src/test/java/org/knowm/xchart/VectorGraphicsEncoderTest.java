package org.knowm.xchart;

import org.junit.Assert;
import org.junit.Test;
import org.knowm.xchart.VectorGraphicsEncoder.VectorGraphicsFormat;

/**
 * @author ekleinod
 */
public class VectorGraphicsEncoderTest {

  @Test
  public void testAddFileExtension() {

    for (VectorGraphicsFormat format : VectorGraphicsFormat.values()) {

      // test -> test.svg
      Assert.assertEquals(String.format("test.%s", format.toString().toLowerCase()), VectorGraphicsEncoder.addFileExtension("test", format));

      // test.svg -> test.svg
      Assert.assertEquals(String.format("test.%s", format.toString().toLowerCase()), VectorGraphicsEncoder.addFileExtension(String.format("test.%s", format.toString().toLowerCase()), format));

      // test.svgsvg -> test.svgsvg.svg
      Assert.assertEquals(String.format("test.%1$s%1$s.%1$s", format.toString().toLowerCase()), VectorGraphicsEncoder.addFileExtension(String.format("test.%1$s%1$s", format.toString().toLowerCase()), format));

      // test.svg.svg -> test.svg.svg
      Assert.assertEquals(String.format("test.%1$s.%1$s", format.toString().toLowerCase()), VectorGraphicsEncoder.addFileExtension(String.format("test.%1$s.%1$s", format.toString().toLowerCase()), format));

      // test.txt -> test.txt.svg
      Assert.assertEquals(String.format("test.txt.%1$s", format.toString().toLowerCase()), VectorGraphicsEncoder.addFileExtension("test.txt", format));

    }

  }

}
