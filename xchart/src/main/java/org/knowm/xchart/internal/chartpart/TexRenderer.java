package org.knowm.xchart.internal.chartpart;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Renders text that may contain LaTeX/TeX math markup. A string is treated as TeX if it is wrapped
 * in {@code $...$}, e.g. {@code "$\\frac{x^2}{2}$"}. All other strings are rendered using the
 * standard {@link java.awt.font.TextLayout} pipeline.
 *
 * <p>JLatexMath ({@code org.scilab.forge:jlatexmath}) is an <em>optional</em> dependency. If it is
 * not present on the classpath, TeX strings fall back to plain-text rendering automatically.
 *
 * <h2>Licensing note</h2>
 *
 * JLatexMath is licensed under GPL v2 <strong>with the Classpath Exception</strong> (linking
 * exception), which permits it to be used as a library dependency from software distributed under
 * any license, including Apache 2.0. The exception was added in commit {@code f1733370} on
 * 2016-01-19, referencing the discussion at
 * https://forge.scilab.org/index.php/p/jlatexmath/issues/1566/ — the very issue raised in XChart
 * issue #125. See: https://github.com/opencollab/jlatexmath/commit/f173337015d89e29b8b2fa05de2c1f0e03ff8e3f
 */
public final class TexRenderer {

  private static final boolean JLATEXMATH_AVAILABLE;

  static {
    boolean available;
    try {
      Class.forName("org.scilab.forge.jlatexmath.TeXFormula");
      available = true;
    } catch (ClassNotFoundException e) {
      available = false;
    }
    JLATEXMATH_AVAILABLE = available;
  }

  private TexRenderer() {}

  /**
   * Returns {@code true} if the string is wrapped in {@code $...$} and JLatexMath is on the
   * classpath.
   */
  public static boolean isTeX(String text) {

    return JLATEXMATH_AVAILABLE
        && text != null
        && text.length() >= 2
        && text.startsWith("$")
        && text.endsWith("$");
  }

  /**
   * Returns the bounding box for the rendered string. For TeX strings this is the icon size
   * produced by JLatexMath; for plain strings it is the {@link java.awt.font.TextLayout} bounds.
   */
  public static Rectangle2D getBounds(String text, Font font) {

    if (isTeX(text)) {
      javax.swing.Icon icon = buildIcon(stripDollars(text), font);
      return new Rectangle2D.Double(0, 0, icon.getIconWidth(), icon.getIconHeight());
    }

    java.awt.font.FontRenderContext frc =
        new java.awt.font.FontRenderContext(null, true, false);
    java.awt.font.TextLayout tl = new java.awt.font.TextLayout(text, font, frc);
    return tl.getOutline(null).getBounds2D();
  }

  /**
   * Paints the string at position ({@code x}, {@code y}).
   *
   * <ul>
   *   <li>For TeX strings, {@code (x, y)} is the <em>top-left</em> corner of the rendered icon.
   *   <li>For plain strings, {@code (x, y)} is the <em>baseline</em> origin, matching
   *       {@link java.awt.font.TextLayout} conventions.
   * </ul>
   *
   * <p>TeX strings are rendered by building a JLatexMath icon at <em>2× the requested font
   * size</em>, painting it into a {@link BufferedImage} at that larger size, then drawing it scaled
   * down to the 1× layout dimensions with bicubic interpolation. This produces visibly bolder,
   * crisper strokes compared to direct 1× rendering.
   */
  public static void render(
      Graphics2D g, String text, double x, double y, Font font, Color color) {

    if (isTeX(text)) {
      String latex = stripDollars(text);
      // 1× icon for layout dimensions only
      org.scilab.forge.jlatexmath.TeXIcon icon1x = buildIcon(latex, font.getSize2D());
      int lw = icon1x.getIconWidth();
      int lh = icon1x.getIconHeight();

      // 2× icon — JLatexMath renders at the font size baked in at creation time,
      // so we must create a new icon at 2× font size to get higher-resolution output.
      org.scilab.forge.jlatexmath.TeXIcon icon2x = buildIcon(latex, font.getSize2D() * 2);
      if (color != null) {
        icon2x.setForeground(color);
      }
      BufferedImage img =
          new BufferedImage(icon2x.getIconWidth(), icon2x.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
      Graphics2D ig = img.createGraphics();
      ig.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      ig.setRenderingHint(
          RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
      ig.setColor(color != null ? color : Color.BLACK);
      icon2x.paintIcon(null, ig, 0, 0);
      ig.dispose();

      AffineTransform orig = g.getTransform();
      g.translate(x, y);
      g.setRenderingHint(
          RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
      g.drawImage(img, 0, 0, lw, lh, null); // scale 2× buffer down to 1× layout size
      g.setTransform(orig);
      return;
    }

    java.awt.font.FontRenderContext frc = g.getFontRenderContext();
    java.awt.font.TextLayout tl = new java.awt.font.TextLayout(text, font, frc);
    java.awt.Shape shape = tl.getOutline(null);
    AffineTransform orig = g.getTransform();
    g.translate(x, y);
    g.setColor(color);
    g.fill(shape);
    g.setTransform(orig);
  }

  /**
   * Paints a TeX or plain-text string rotated 90° counter-clockwise (for Y-axis titles).
   *
   * <p>For TeX strings, {@code (x, y)} is the <em>right/bottom</em> corner of the rotated icon in
   * screen space — matching the convention used by {@link java.awt.font.TextLayout} rotated
   * outlines in the calling code. After a −90° rotation the icon is {@code iconHeight}-wide and
   * {@code iconWidth}-tall in screen space.
   *
   * <p>Same 2× font-size supersampling as {@link #render} is applied here.
   */
  public static void renderRotated(
      Graphics2D g, String text, double x, double y, Font font, Color color) {

    if (isTeX(text)) {
      String latex = stripDollars(text);
      // 1× icon for layout dimensions only
      org.scilab.forge.jlatexmath.TeXIcon icon1x = buildIcon(latex, font.getSize2D());
      int lw = icon1x.getIconWidth();
      int lh = icon1x.getIconHeight();

      // 2× icon for high-res render
      org.scilab.forge.jlatexmath.TeXIcon icon2x = buildIcon(latex, font.getSize2D() * 2);
      if (color != null) {
        icon2x.setForeground(color);
      }
      BufferedImage img =
          new BufferedImage(icon2x.getIconWidth(), icon2x.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
      Graphics2D ig = img.createGraphics();
      ig.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      ig.setRenderingHint(
          RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
      ig.setColor(color != null ? color : Color.BLACK);
      icon2x.paintIcon(null, ig, 0, 0);
      ig.dispose();

      AffineTransform orig = g.getTransform();
      // (x, y) = right/bottom corner after rotation.  Screen rect = (x-lh, y-lw) to (x, y).
      g.translate(x - lh, y - lw);
      g.rotate(-Math.PI / 2);
      g.translate(-lw, 0);
      g.setRenderingHint(
          RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
      g.drawImage(img, 0, 0, lw, lh, null); // scale 2× buffer down to 1× layout size
      g.setTransform(orig);
      return;
    }

    java.awt.font.FontRenderContext frc = g.getFontRenderContext();
    java.awt.font.TextLayout tl = new java.awt.font.TextLayout(text, font, frc);
    java.awt.Shape shape =
        tl.getOutline(AffineTransform.getRotateInstance(-Math.PI / 2, 0, 0));
    AffineTransform orig = g.getTransform();
    g.translate(x, y);
    g.setColor(color);
    g.fill(shape);
    g.setTransform(orig);
  }

  // ---- private helpers ----

  private static String stripDollars(String text) {

    return text.substring(1, text.length() - 1);
  }

  private static org.scilab.forge.jlatexmath.TeXIcon buildIcon(String latex, Font font) {

    return buildIcon(latex, font.getSize2D());
  }

  private static org.scilab.forge.jlatexmath.TeXIcon buildIcon(String latex, float fontSize) {

    org.scilab.forge.jlatexmath.TeXFormula formula =
        new org.scilab.forge.jlatexmath.TeXFormula(latex);
    org.scilab.forge.jlatexmath.TeXIcon icon =
        formula.createTeXIcon(
            org.scilab.forge.jlatexmath.TeXConstants.STYLE_DISPLAY, fontSize);
    icon.setInsets(new Insets(2, 2, 2, 2));
    return icon;
  }
}
