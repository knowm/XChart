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
package org.knowm.xchart.style.markers;

/**
 * @author timmolter
 */
public interface SeriesMarkers {

  public static Marker NONE = new None();
  public static Marker CIRCLE = new Circle();
  public static Marker DIAMOND = new Diamond();
  public static Marker SQUARE = new Square();
  public static Marker TRIANGLE_DOWN = new TriangleDown();
  public static Marker TRIANGLE_UP = new TriangleUp();

  public Marker[] getSeriesMarkers();

}
