package org.knowm.xchart;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.Font;
import java.awt.geom.Rectangle2D;
import org.junit.jupiter.api.Test;
import org.knowm.xchart.internal.chartpart.TexRenderer;

/** Tests for {@link TexRenderer}. */
class TexRendererTest {

  private static final Font FONT = new Font("SansSerif", Font.PLAIN, 12);

  // ---- isTeX detection ----

  @Test
  void isTexReturnsTrueForDollarWrappedString() {

    assertThat(TexRenderer.isTeX("$\\alpha$")).isTrue();
  }

  @Test
  void isTexReturnsTrueForSingleDollarPair() {

    assertThat(TexRenderer.isTeX("$$")).isTrue();
  }

  @Test
  void isTexReturnsFalseForPlainText() {

    assertThat(TexRenderer.isTeX("hello")).isFalse();
  }

  @Test
  void isTexReturnsFalseForNullString() {

    assertThat(TexRenderer.isTeX(null)).isFalse();
  }

  @Test
  void isTexReturnsFalseForEmptyString() {

    assertThat(TexRenderer.isTeX("")).isFalse();
  }

  @Test
  void isTexReturnsFalseForOnlyLeadingDollar() {

    assertThat(TexRenderer.isTeX("$alpha")).isFalse();
  }

  @Test
  void isTexReturnsFalseForOnlyTrailingDollar() {

    assertThat(TexRenderer.isTeX("alpha$")).isFalse();
  }

  // ---- getBounds ----

  @Test
  void getBoundsReturnsPositiveDimensionsForPlainText() {

    Rectangle2D bounds = TexRenderer.getBounds("Hello", FONT);
    assertThat(bounds.getWidth()).isPositive();
    assertThat(bounds.getHeight()).isPositive();
  }

  @Test
  void getBoundsForTexReturnsSomethingWhenJLatexMathPresent() {

    // If JLatexMath is not on the classpath isTeX returns false and this just tests plain text.
    // If it IS present we verify we get non-trivial dimensions.
    Rectangle2D bounds = TexRenderer.getBounds("$\\frac{x^2}{2}$", FONT);
    assertThat(bounds.getWidth()).isPositive();
    assertThat(bounds.getHeight()).isPositive();
  }

  @Test
  void getBoundsPlainTextWidthGrowsWithStringLength() {

    Rectangle2D shortBounds = TexRenderer.getBounds("Hi", FONT);
    Rectangle2D longBounds = TexRenderer.getBounds("Hello World this is a longer string", FONT);
    assertThat(longBounds.getWidth()).isGreaterThan(shortBounds.getWidth());
  }
}
