package org.knowm.xchart.font;

import org.knowm.xchart.Font;

public interface FontFactory<T> {
  T get(Font font);
}
