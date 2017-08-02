/**
 * Copyright 2015-2017 Knowm Inc. (http://knowm.org) and contributors.
 * Copyright 2011-2015 Xeiam LLC (http://xeiam.com) and contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.knowm.xchart.style;

import java.awt.Color;
import java.awt.Font;
import java.awt.Stroke;

public class RadarStyler extends Styler {

  private boolean isCircular;
  private double startAngleInDegrees;
  
  // Chart Plot Area ///////////////////////////////
  // main lines
  private boolean plotGridLinesVisible;
  private Color plotGridLinesColor;
  private Stroke plotGridLinesStroke;

  // helper tick lines
  private boolean axisTicksMarksVisible;
  private Color axisTickMarksColor;
  private Stroke axisTickMarksStroke;
  private int axisTickMarksCount = 5;

  // variable labels
  private boolean axisTitleVisible;
  private Font axisTitleFont;
  private int axisTitlePadding;

  
  private int markerSize;

  public RadarStyler() {

    this.setAllStyles();
    super.setAllStyles();
  }

  @Override
  void setAllStyles() {

    this.isCircular = theme.isCircular();
    this.startAngleInDegrees = theme.getStartAngleInDegrees();

    // Annotations ////////////////////////////////
    this.hasAnnotations = true;

    this.markerSize = theme.getMarkerSize();
    
    // Chart Plot Area ///////////////////////////////
    this.plotGridLinesVisible = theme.isPlotGridLinesVisible();
    this.plotGridLinesColor = theme.getPlotGridLinesColor();
    this.plotGridLinesStroke = theme.getPlotGridLinesStroke();

    
    this.axisTickMarksColor = theme.getAxisTickMarksColor();
    this.axisTickMarksStroke = theme.getAxisTickMarksStroke();
    this.axisTicksMarksVisible = theme.isAxisTicksMarksVisible();
    
    
    this.axisTitleVisible = theme.isXAxisTitleVisible() || theme.isYAxisTitleVisible();
    this.axisTitleFont = theme.getAxisTitleFont();
    this.axisTitlePadding = theme.getAxisTitlePadding();
    
  }

  public boolean isCircular() {

    return isCircular;
  }

  /**
   * Sets whether or not the radar chart is forced to be circular. Otherwise it's shape is oval, matching the containing plot.
   *
   * @param isCircular
   */
  public RadarStyler setCircular(boolean isCircular) {

    this.isCircular = isCircular;
    return this;
  }

  public double getStartAngleInDegrees() {

    return startAngleInDegrees;
  }

  /**
   * Sets the start angle in degrees. Zero degrees is straight up.
   *
   * @param startAngleInDegrees
   */
  public RadarStyler setStartAngleInDegrees(double startAngleInDegrees) {

    this.startAngleInDegrees = startAngleInDegrees;
    return this;
  }

  /**
   * Set the theme the styler should use
   *
   * @param theme
   */
  public RadarStyler setTheme(Theme theme) {

    this.theme = theme;
    super.setAllStyles();
    return this;
  }

  /**
   * Sets the size of the markers (in pixels)
   *
   * @param markerSize
   */
  public RadarStyler setMarkerSize(int markerSize) {

    this.markerSize = markerSize;
    return this;
  }

  public int getMarkerSize() {

    return markerSize;
  }
  
  public boolean isPlotGridLinesVisible() {

    return plotGridLinesVisible;
  }
  
  public void setPlotGridLinesVisible(boolean plotGridLinesVisible) {

    this.plotGridLinesVisible = plotGridLinesVisible;
  }
  
  
  public Color getPlotGridLinesColor() {

    return plotGridLinesColor;
  }
  
  public void setPlotGridLinesColor(Color plotGridLinesColor) {

    this.plotGridLinesColor = plotGridLinesColor;
  }
  
  public Stroke getPlotGridLinesStroke() {

    return plotGridLinesStroke;
  }
  
  public void setPlotGridLinesStroke(Stroke plotGridLinesStroke) {

    this.plotGridLinesStroke = plotGridLinesStroke;
  }

  public boolean isAxisTicksMarksVisible() {

    return axisTicksMarksVisible;
  }
  
  public void setAxisTicksMarksVisible(boolean axisTicksMarksVisible) {

    this.axisTicksMarksVisible = axisTicksMarksVisible;
  }
  
  public Color getAxisTickMarksColor() {

    return axisTickMarksColor;
  }
  
  public void setAxisTickMarksColor(Color axisTickMarksColor) {

    this.axisTickMarksColor = axisTickMarksColor;
  }
  
  public Stroke getAxisTickMarksStroke() {

    return axisTickMarksStroke;
  }
  
  public void setAxisTickMarksStroke(Stroke axisTickMarksStroke) {

    this.axisTickMarksStroke = axisTickMarksStroke;
  }

  public boolean isAxisTitleVisible() {

    return axisTitleVisible;
  }
  
  public void setAxisTitleVisible(boolean axisTitleVisible) {

    this.axisTitleVisible = axisTitleVisible;
  }
  
  public Font getAxisTitleFont() {

    return axisTitleFont;
  }
  
  public void setAxisTitleFont(Font axisTitleFont) {

    this.axisTitleFont = axisTitleFont;
  }
  
  public int getAxisTitlePadding() {

    return axisTitlePadding;
  }
  
  public void setAxisTitlePadding(int axisTitlePadding) {

    this.axisTitlePadding = axisTitlePadding;
  }

  public int getAxisTickMarksCount() {

    return axisTickMarksCount;
  }
  
  public void setAxisTickMarksCount(int axisTickMarksCount) {

    this.axisTickMarksCount = axisTickMarksCount;
  }
}
