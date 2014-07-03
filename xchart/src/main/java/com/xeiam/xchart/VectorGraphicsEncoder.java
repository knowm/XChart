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

import java.io.FileOutputStream;
import java.io.IOException;

import de.erichseifert.vectorgraphics2d.EPSGraphics2D;
import de.erichseifert.vectorgraphics2d.PDFGraphics2D;
import de.erichseifert.vectorgraphics2d.SVGGraphics2D;
import de.erichseifert.vectorgraphics2d.VectorGraphics2D;

/**
 * A helper class with static methods for saving Charts as bitmaps
 * 
 * @author timmolter
 */
public final class VectorGraphicsEncoder {

  /**
   * Constructor - Private constructor to prevent instantiation
   */
  private VectorGraphicsEncoder() {

  }

  public enum VectorGraphicsFormat {
    EPS, PDF, SVG;
  }

  public static void saveVectorGraphic(Chart chart, String fileName, VectorGraphicsFormat vectorGraphicsFormat) throws IOException {

    VectorGraphics2D g = null;

    switch (vectorGraphicsFormat) {
    case EPS:
      g = new EPSGraphics2D(0.0, 0.0, chart.getWidth(), chart.getHeight());
      break;
    case PDF:
      g = new PDFGraphics2D(0.0, 0.0, chart.getWidth(), chart.getHeight());
      break;
    case SVG:
      g = new SVGGraphics2D(0.0, 0.0, chart.getWidth(), chart.getHeight());
      break;

    default:
      break;
    }

    chart.paint(g, chart.getWidth(), chart.getHeight());

    // Write the vector graphic output to a file
    FileOutputStream file = new FileOutputStream(fileName + "." + vectorGraphicsFormat.toString().toLowerCase());

    try {
      file.write(g.getBytes());
    } finally {
      file.close();
    }
  }

}
