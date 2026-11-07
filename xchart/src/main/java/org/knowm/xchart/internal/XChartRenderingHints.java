package org.knowm.xchart.internal;

import java.awt.Graphics2D;
import java.awt.RenderingHints;

/** XChart-internal rendering hints that encoders set to tell chart parts about the backend. */
public final class XChartRenderingHints {

  /**
   * Set to {@link Boolean#TRUE} on a {@link Graphics2D} whose backend drops gradient paints, so
   * chart parts paint flat fills instead. {@code VectorGraphicsEncoder} sets it for SVG and EPS.
   */
  public static final RenderingHints.Key KEY_FLAT_FILLS_ONLY = new BooleanKey(1);

  private XChartRenderingHints() {}

  /** True when {@code g} carries {@link #KEY_FLAT_FILLS_ONLY} set to {@link Boolean#TRUE}. */
  public static boolean isFlatFillsOnly(Graphics2D g) {

    return Boolean.TRUE.equals(g.getRenderingHint(KEY_FLAT_FILLS_ONLY));
  }

  private static final class BooleanKey extends RenderingHints.Key {

    private BooleanKey(int privateKey) {

      super(privateKey);
    }

    @Override
    public boolean isCompatibleValue(Object value) {

      return value instanceof Boolean;
    }

    @Override
    public String toString() {

      return "XChart flat fills only";
    }
  }
}
