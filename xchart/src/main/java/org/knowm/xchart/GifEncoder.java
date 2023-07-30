package org.knowm.xchart;

import com.madgag.gif.fmsware.AnimatedGifEncoder;
import java.awt.image.BufferedImage;
import java.util.List;
import org.knowm.xchart.internal.Utils;

/** A helper class with static methods for saving Charts as a GIF file */
public class GifEncoder {

  private static final String GIF_FILE_EXTENSION = ".gif";

  /**
   * images saved as GIF file, repeated countless times with 100ms delay
   *
   * @param filePath GIF file path
   * @param images Multiple BufferedImages for Chart
   */
  public static void saveGif(String filePath, List<BufferedImage> images) {
    saveGif(filePath, images, 0, 100);
  }

  /**
   * images saved as GIF file, Set repeat times and delay time
   *
   * @param filePath GIF file path
   * @param images Multiple BufferedImages for Chart
   * @param repeat repeat times, less than 0 does not repeat,0 countless times
   * @param delay delay time in milliseconds
   */
  public static void saveGif(String filePath, List<BufferedImage> images, int repeat, int delay) {
    AnimatedGifEncoder gif = new AnimatedGifEncoder();
    gif.setRepeat(repeat);
    gif.start(Utils.addFileExtension(filePath, GIF_FILE_EXTENSION));
    gif.setDelay(delay);
    for (BufferedImage image : images) {
      gif.addFrame(image);
    }
    gif.finish();
  }
}
