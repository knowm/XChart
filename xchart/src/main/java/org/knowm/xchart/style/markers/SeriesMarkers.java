package org.knowm.xchart.style.markers;

/** @author timmolter */
public interface SeriesMarkers {

  Marker NONE = new None();
  Marker CIRCLE = new Circle();
  Marker DIAMOND = new Diamond();
  Marker SQUARE = new Square();
  Marker TRIANGLE_DOWN = new TriangleDown();
  Marker TRIANGLE_UP = new TriangleUp();
  Marker CROSS = new Cross();
  Marker PLUS = new Plus();
  Marker TRAPEZOID = new Trapezoid();
  Marker OVAL = new Oval();
  Marker RECTANGLE = new Rectangle();

  Marker[] getSeriesMarkers();
}
