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
package com.xeiam.xchart.internal.markers;

import java.awt.BasicStroke;
import java.awt.Graphics2D;

/**
 * @author timmolter
 */
public abstract class Marker implements Cloneable {
  private static final double MIN_SIZE = 3;
  private static final double MAX_SIZE = 25;
  
  public static final double SIZE = 8;
    
  protected BasicStroke stroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
  
  protected double size = SIZE;

  public abstract void paint(Graphics2D g, double xOffset, double yOffset);
  
  public double getSize() {
    return size;
  }
  
  public void setSize(double size) {
    this.size = Math.max(MIN_SIZE, Math.min(MAX_SIZE, size));
  }
 
  @Override
  public Marker clone()  {
     try {
        return (Marker) super.clone();
    } catch (CloneNotSupportedException e) {
        return null;
    }
  }
}
