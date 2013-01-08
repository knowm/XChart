/**
 * Copyright 2011-2013 Xeiam LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xeiam.xchart;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;

/**
 * A helper class with static methods for saving Charts as bitmaps
 * 
 * @author timmolter
 */
public class BitmapEncoder {

  /**
   * Constructor - Private constructor to prevent instantiation
   */
  private BitmapEncoder() {

  }

  /**
   * Save a Chart as a PNG file
   * 
   * @param chart
   * @param fileName
   * @throws IOException
   */
  public static void savePNG(Chart chart, String fileName) throws IOException {

    BufferedImage bufferedImage = new BufferedImage(chart.width, chart.height, BufferedImage.TYPE_INT_RGB);
    Graphics2D lGraphics2D = bufferedImage.createGraphics();
    chart.paint(lGraphics2D);

    // Save chart as PNG
    OutputStream out = new FileOutputStream(fileName);
    ImageIO.write(bufferedImage, "png", out);
    out.close();
  }

  /**
   * Save a Chart as a JPEG file
   * 
   * @param chart
   * @param fileName
   * @param quality - // a float between 0 and 1 (1 = maximum quality)
   * @throws FileNotFoundException
   * @throws IOException
   */
  public static void saveJPG(Chart chart, String fileName, float quality) throws FileNotFoundException, IOException {

    BufferedImage bufferedImage = new BufferedImage(chart.width, chart.height, BufferedImage.TYPE_INT_RGB);
    Graphics2D lGraphics2D = bufferedImage.createGraphics();
    chart.paint(lGraphics2D);

    Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("jpeg");
    ImageWriter writer = iter.next();
    // instantiate an ImageWriteParam object with default compression options
    ImageWriteParam iwp = writer.getDefaultWriteParam();
    iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
    iwp.setCompressionQuality(quality);
    File file = new File(fileName);
    FileImageOutputStream output = new FileImageOutputStream(file);
    writer.setOutput(output);
    IIOImage image = new IIOImage(bufferedImage, null, null);
    writer.write(null, image, iwp);
    writer.dispose();
  }

}
