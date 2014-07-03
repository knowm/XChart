/**
 * Copyright 2011 - 2014 Xeiam LLC.
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
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.FileImageOutputStream;

/**
 * A helper class with static methods for saving Charts as bitmaps
 * 
 * @author timmolter
 */
public final class BitmapEncoder {

  /**
   * Constructor - Private constructor to prevent instantiation
   */
  private BitmapEncoder() {

  }

  public enum BitmapFormat {
    PNG, JPG, BMP, GIF;
  }

  /**
   * Save a Chart as an image file
   * 
   * @param chart
   * @param fileName
   * @param bitmapFormat
   * @throws IOException
   */
  public static void saveBitmap(Chart chart, String fileName, BitmapFormat bitmapFormat) throws IOException {

    BufferedImage bufferedImage = getBufferedImage(chart);

    OutputStream out = new FileOutputStream(fileName + "." + bitmapFormat.toString().toLowerCase());
    ImageIO.write(bufferedImage, bitmapFormat.toString().toLowerCase(), out);
    out.close();
  }

  /**
   * Save a chart as a PNG with a custom DPI. The default DPI is 72, which is fine for displaying charts on a computer monitor, but for printing charts, a DPI of around 300 is much better.
   * 
   * @param chart
   * @param fileName
   * @param DPI
   * @throws IOException
   */
  public static void saveBitmapWithDPI(Chart chart, String fileName, BitmapFormat bitmapFormat, int DPI) throws IOException {

    double scaleFactor = DPI / 72.0;

    BufferedImage bufferedImage = new BufferedImage((int) (chart.getWidth() * scaleFactor), (int) (chart.getHeight() * scaleFactor), BufferedImage.TYPE_INT_RGB);

    Graphics2D graphics2D = bufferedImage.createGraphics();

    AffineTransform at = graphics2D.getTransform();
    at.scale(scaleFactor, scaleFactor);
    graphics2D.setTransform(at);

    chart.paint(graphics2D, chart.getWidth(), chart.getHeight());
    Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(bitmapFormat.toString().toLowerCase());
    if (writers.hasNext()) {
      ImageWriter writer = writers.next();
      // instantiate an ImageWriteParam object with default compression options
      ImageWriteParam iwp = writer.getDefaultWriteParam();

      ImageTypeSpecifier typeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_RGB);
      IIOMetadata metadata = writer.getDefaultImageMetadata(typeSpecifier, iwp);
      if (metadata.isReadOnly() || !metadata.isStandardMetadataFormatSupported()) {
        throw new IllegalArgumentException("It is not possible to set the DPI on a bitmap with " + bitmapFormat + " format!! Try another format.");
      }

      setDPI(metadata, DPI);

      File file = new File(fileName + "." + bitmapFormat.toString().toLowerCase());
      FileImageOutputStream output = new FileImageOutputStream(file);
      writer.setOutput(output);
      IIOImage image = new IIOImage(bufferedImage, null, metadata);
      writer.write(null, image, iwp);
      writer.dispose();
    }
  }

  /**
   * Sets the metadata correctly
   * 
   * @param metadata
   * @param DPI
   * @throws IIOInvalidTreeException
   */
  private static void setDPI(IIOMetadata metadata, int DPI) throws IIOInvalidTreeException {

    // for PNG, it's dots per millimeter
    double dotsPerMilli = 1.0 * DPI / 10 / 2.54;

    IIOMetadataNode horiz = new IIOMetadataNode("HorizontalPixelSize");
    horiz.setAttribute("value", Double.toString(dotsPerMilli));

    IIOMetadataNode vert = new IIOMetadataNode("VerticalPixelSize");
    vert.setAttribute("value", Double.toString(dotsPerMilli));

    IIOMetadataNode dim = new IIOMetadataNode("Dimension");
    dim.appendChild(horiz);
    dim.appendChild(vert);

    IIOMetadataNode root = new IIOMetadataNode("javax_imageio_1.0");
    root.appendChild(dim);

    metadata.mergeTree("javax_imageio_1.0", root);
  }

  /**
   * Save a Chart as a JPEG file
   * 
   * @param chart
   * @param fileName
   * @param quality - a float between 0 and 1 (1 = maximum quality)
   * @throws FileNotFoundException
   * @throws IOException
   */
  public static void saveJPGWithQuality(Chart chart, String fileName, float quality) throws FileNotFoundException, IOException {

    BufferedImage bufferedImage = getBufferedImage(chart);

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

  /**
   * Generates a byte[] for a given chart, PNG compressed
   * 
   * @param chart
   * @return a byte[] for a given chart, PNG compressed
   * @throws IOException
   */
  public static byte[] getBitmapBytes(Chart chart, BitmapFormat bitmapFormat) throws IOException {

    BufferedImage bufferedImage = getBufferedImage(chart);

    byte[] imageInBytes = null;

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ImageIO.write(bufferedImage, bitmapFormat.toString().toLowerCase(), baos);
    baos.flush();
    imageInBytes = baos.toByteArray();
    baos.close();

    return imageInBytes;
  }

  public static BufferedImage getBufferedImage(Chart chart) {

    BufferedImage bufferedImage = new BufferedImage(chart.getWidth(), chart.getHeight(), BufferedImage.TYPE_INT_RGB);
    Graphics2D graphics2D = bufferedImage.createGraphics();
    chart.paint(graphics2D);
    return bufferedImage;
  }

}
