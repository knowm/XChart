/**
 * Copyright 2015 Knowm Inc. (http://knowm.org) and contributors.
 * Copyright 2011-2015 Xeiam LLC (http://xeiam.com) and contributors.
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
package org.knowm.xchart.internal.chartpart;

import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;

import org.knowm.xchart.StyleManager;
import org.knowm.xchart.internal.chartpart.Axis.Direction;

/**
 * @author timmolter
 */
public abstract class AxisTickCalculator {

  /** the List of tick label position in pixels */
  protected List<Double> tickLocations = new LinkedList<Double>();;

  /** the List of tick label values */
  protected List<String> tickLabels = new LinkedList<String>();

  protected final Direction axisDirection;

  protected final double workingSpace;

  protected double minValue;

  protected double maxValue;

  protected final StyleManager styleManager;

  /**
   * Constructor
   *
   * @param axisDirection
   * @param workingSpace
   * @param minValue
   * @param maxValue
   * @param styleManager
   */
  public AxisTickCalculator(Direction axisDirection, double workingSpace, double minValue, double maxValue, StyleManager styleManager) {

    // override min and maxValue if specified
    double overrideMinValue = minValue;
    double overrideMaxValue = maxValue;
    if (axisDirection == Direction.X && styleManager.getXAxisMin() != null) {
      overrideMinValue = styleManager.getXAxisMin();
    }
    if (axisDirection == Direction.Y && styleManager.getYAxisMin() != null) {
      overrideMinValue = styleManager.getYAxisMin();
    }
    if (axisDirection == Direction.X && styleManager.getXAxisMax() != null) {
      overrideMaxValue = styleManager.getXAxisMax();
    }
    if (axisDirection == Direction.Y && styleManager.getYAxisMax() != null) {
      overrideMaxValue = styleManager.getYAxisMax();
    }
    this.axisDirection = axisDirection;
    this.workingSpace = workingSpace;
    this.minValue = overrideMinValue;
    this.maxValue = overrideMaxValue;
    this.styleManager = styleManager;
  }

  /**
   * Gets the first position
   *
   * @param gridStep
   * @return
   */
  double getFirstPosition(double gridStep) {

    // System.out.println("******");

    double firstPosition = minValue - (minValue % gridStep) - gridStep;
    return firstPosition;
  }

  public List<Double> getTickLocations() {

    return tickLocations;
  }

  public List<String> getTickLabels() {

    return tickLabels;
  }

  /**
   * Given the generated tickLabels, will they fit side-by-side without overlapping each other and looking bad? Sometimes the given tickSpacingHint is simply too small.
   *
   * @param tickLabels
   * @param tickSpacingHint
   * @return
   */
  boolean willLabelsFitInTickSpaceHint(List<String> tickLabels, int tickSpacingHint) {

    // Assume that for Y-Axis the ticks will all fit based on their tickSpace hint because the text is usually horizontal and "short". This more applies to the X-Axis.
    if (this.axisDirection == Direction.Y) {
      return true;
    }

    String sampleLabel = " ";
    // find the longest String in all the labels
    for (int i = 0; i < tickLabels.size(); i++) {
      if (tickLabels.get(i) != null && tickLabels.get(i).length() > sampleLabel.length()) {
        sampleLabel = tickLabels.get(i);
      }
    }
    // System.out.println("longestLabel: " + sampleLabel);

    TextLayout textLayout = new TextLayout(sampleLabel, styleManager.getAxisTickLabelsFont(), new FontRenderContext(null, true, false));
    AffineTransform rot = styleManager.getXAxisLabelRotation() == 0 ? null : AffineTransform.getRotateInstance(-1 * Math.toRadians(styleManager.getXAxisLabelRotation()));
    Shape shape = textLayout.getOutline(rot);
    Rectangle2D rectangle = shape.getBounds();
    double largestLabelWidth = rectangle.getWidth();
    // System.out.println("largestLabelWidth: " + largestLabelWidth);
    // System.out.println("tickSpacingHint: " + tickSpacingHint);

    // if (largestLabelWidth * 1.1 >= tickSpacingHint) {
    // System.out.println("WILL NOT FIT!!!");
    // }

    return (largestLabelWidth * 1.1 < tickSpacingHint);

  }
}
