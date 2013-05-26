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
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
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

  /**
   * Save a Chart as a PNG file
   * 
   * @param chart
   * @param fileName
   * @throws IOException
   */
  public static void savePNG(Chart chart, String fileName) throws IOException {

    BufferedImage bufferedImage = new BufferedImage(chart.getWidth(), chart.getHeight(), BufferedImage.TYPE_INT_RGB);
    Graphics2D graphics2D = bufferedImage.createGraphics();
    chart.paint(graphics2D);

    // Save chart as PNG
    OutputStream out = new FileOutputStream(fileName);
    ImageIO.write(bufferedImage, "png", out);
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
  public static void savePNGWithDPI(Chart chart, String fileName, int DPI) throws IOException {

    double scaleFactor = DPI / 72.0;

    BufferedImage bufferedImage = new BufferedImage((int) (chart.getWidth() * scaleFactor), (int) (chart.getHeight() * scaleFactor), BufferedImage.TYPE_INT_RGB);

    Graphics2D graphics2D = bufferedImage.createGraphics();

    AffineTransform at = graphics2D.getTransform();
    at.scale(scaleFactor, scaleFactor);
    graphics2D.setTransform(at);

    chart.paint(graphics2D, chart.getWidth(), chart.getHeight());

    for (Iterator<ImageWriter> iw = ImageIO.getImageWritersByFormatName("png"); iw.hasNext();) {
      ImageWriter writer = iw.next();
      // instantiate an ImageWriteParam object with default compression options
      ImageWriteParam iwp = writer.getDefaultWriteParam();

      ImageTypeSpecifier typeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_RGB);
      IIOMetadata metadata = writer.getDefaultImageMetadata(typeSpecifier, iwp);
      if (metadata.isReadOnly() || !metadata.isStandardMetadataFormatSupported()) {
        continue;
      }

      setDPIforPNG(metadata, DPI);

      File file = new File(fileName);
      FileImageOutputStream output = new FileImageOutputStream(file);
      writer.setOutput(output);
      IIOImage image = new IIOImage(bufferedImage, null, metadata);
      writer.write(null, image, iwp);
      writer.dispose();
      break;
    }
  }

  /**
   * Sets the metadata correctly
   * 
   * @param metadata
   * @param DPI
   * @throws IIOInvalidTreeException
   */
  private static void setDPIforPNG(IIOMetadata metadata, int DPI) throws IIOInvalidTreeException {

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
   * @param quality - // a float between 0 and 1 (1 = maximum quality)
   * @throws FileNotFoundException
   * @throws IOException
   */
  public static void saveJPG(Chart chart, String fileName, float quality) throws FileNotFoundException, IOException {

    BufferedImage bufferedImage = new BufferedImage(chart.getWidth(), chart.getHeight(), BufferedImage.TYPE_INT_RGB);
    Graphics2D graphics2D = bufferedImage.createGraphics();
    chart.paint(graphics2D);

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

  public static void main(String[] args) {

    for (String format : ImageIO.getWriterFormatNames()) {
      System.out.println(format);
      // ImageIO.write(bufferedImage, format, new File("C:\\image_new." + format));
    }
  }

}
