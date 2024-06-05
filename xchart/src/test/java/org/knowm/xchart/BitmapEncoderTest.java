package org.knowm.xchart;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class BitmapEncoderTest {

    @Test
    public void testAddFileExtension() {
        String fileName1 = "image";
        String fileName2 = "image.png";
        String fileName3 = "image.PNG";

        for (String s : Arrays.asList(fileName1, fileName2, fileName3)) {
            assertEquals("image.png", BitmapEncoder.addFileExtension(s, BitmapEncoder.BitmapFormat.PNG));
        }
        assertEquals("z.bmp", BitmapEncoder.addFileExtension("z", BitmapEncoder.BitmapFormat.BMP));
        assertEquals("asdf.bmp", BitmapEncoder.addFileExtension("asdf", BitmapEncoder.BitmapFormat.BMP));
        assertEquals(".bmp", BitmapEncoder.addFileExtension(".bmp", BitmapEncoder.BitmapFormat.BMP));
        assertEquals(".bmp", BitmapEncoder.addFileExtension(".BmP", BitmapEncoder.BitmapFormat.BMP));
    }
}
