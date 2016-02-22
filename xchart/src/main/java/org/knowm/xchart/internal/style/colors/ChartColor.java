/**
 * Copyright 2015-2016 Knowm Inc. (http://knowm.org) and contributors.
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
package org.knowm.xchart.internal.style.colors;

import java.awt.Color;

/**
 * Pre-defined Colors used for various Chart Elements
 * 
 * @author timmolter
 */
public enum ChartColor {

	/** BLACK */
	BLACK(new Color(0, 0, 0)),
	
	/** DARK_GREY */
	DARK_GREY(new Color(130, 130, 130)),
	
	/** GREY */
	GREY(new Color(210, 210, 210)),
	
	/** LIGHT_GREY */
	LIGHT_GREY(new Color(230, 230, 230)),
	
	/** WHITE */
	WHITE(new Color(255, 255, 255)),
	
	/** HONEY_DEW */
	HONEY_DEW(new Color(240, 255, 240)),
	
	/** IVORY_1 - KREM */
	IVORY_1(new Color(255, 255, 240)),
	
	/** ANTIQUE_WHITE_1 */
	ANTIQUE_WHITE_1(new Color(255, 239, 219)),

  	/** SEE_SHEL_1 */
  	SEE_SHEL_1(new Color(255, 245, 238)),
	
  	/** ROSYBROWN */
  	ROSYBROWN(new Color(188, 143, 143)),
	
  	/** DARK_ORANGE */
  	DARK_ORANGE (new Color(255, 140, 0)),
  
  	/** SAGI_SALMON*/
  	SAGI_SALMON (new Color(198, 113, 113)),

  	/** SGI_SLATE_BLUE */
  	SGI_SLATE_BLUE(new Color(113, 113, 198)),
  	
  	/** SGI_SLATE_BLUE */
  	SGI_CHART_REUSE(new Color(113, 198, 113)),
	
  	/** LIGHT_BLUE */
  	LIGHT_BLUE (new Color(135, 206, 250)),
  	
  	/** LIGHT_BLUE1 */
  	LIGHT_BLUE_1 (new Color(176, 226, 255)),
	
  	/** DODGE_BLUE_4*/
  	DODGE_BLUE_4 (new Color(16, 78, 139));
  	
	Color color;

  /**
   * Get a AWT Color Object
   * 
   * @param chartColor
   * @return a AWT Color Object
   */
  public static Color getAWTColor(ChartColor chartColor) {

    return chartColor.color;
  }

  /**
   * Constructor
   * 
   * @param color
   */
  private ChartColor(Color color) {

    this.color = color;
  }

}
