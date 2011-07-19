/**
 * Copyright 2011 Xeiam LLC.
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
package com.xeiam.xcharts;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;

/**
 * @author timmolter
 */
public class BitmapEncoder {

    /**
     * Saves a chart as a PNG file
     * 
     * @param chart
     * @param pFileName
     */
    public static void savePNG(Chart chart, String pFileName) throws Exception {

        BufferedImage lBufferedImage = new BufferedImage(chart.getWidth(), chart.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D lGraphics2D = lBufferedImage.createGraphics();
        chart.paint(lGraphics2D);

        // Save chart as PNG
        OutputStream out = new FileOutputStream(pFileName);
        ImageIO.write(lBufferedImage, "png", out);
        out.close();
    }

    /**
     * Streams a chart as a PNG file
     * 
     * @param out
     * @param chart
     */
    public static void streamPNG(ServletOutputStream out, Chart chart) throws Exception {

        BufferedImage lBufferedImage = new BufferedImage(chart.getWidth(), chart.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D lGraphics2D = lBufferedImage.createGraphics();
        chart.paint(lGraphics2D);

        ImageIO.write(lBufferedImage, "png", out);
        out.close();
    }
}
