package org.knowm.xchart;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.knowm.xchart.internal.chartpart.Annotation;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.style.Styler;

public class AnnotationTextPanel extends Annotation {

  private static final int MULTI_LINE_SPACE = 3;

  private final List<String> lines;

  private Styler styler;

  private Rectangle2D bounds;

  // internal
  private double startx;
  private double starty;

  /**
   * Constructor
   *
   * @param lines
   * @param xPosition
   * @param yPosition
   * @param isValueInScreenSpace
   */
  public AnnotationTextPanel(
      String lines, double xPosition, double yPosition, boolean isValueInScreenSpace) {

    this.lines = Arrays.asList(lines.split("\\n"));
    this.x = xPosition;
    this.y = yPosition;
    this.isValueInScreenSpace = isValueInScreenSpace;
  }

  public void init(Chart chart) {

    super.init(chart);
    this.styler = chart.getStyler();
  }

  @Override
  public void paint(Graphics2D g) {

    if (!isVisible) {
      return;
    }

    bounds = getBoundsHint();
    //    System.out.println("bounds = " + bounds);

    // Info panel draw position
    //    double height = bounds.getHeight();
    //    double width = bounds.getWidth();

    calculatePosition();

    // Draw info panel box background and border
    Shape rect = new Rectangle2D.Double(startx, starty, bounds.getWidth(), bounds.getHeight());
    g.setColor(styler.getInfoPanelBackgroundColor());
    g.fill(rect);
    g.setStroke(SOLID_STROKE);
    g.setColor(styler.getInfoPanelBorderColor());
    g.draw(rect);

    // Draw text onto panel box
    Object oldHint = g.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    Map<String, Rectangle2D> textBounds = getTextBounds(lines);

    g.setColor(styler.getChartFontColor());
    g.setFont(styler.getInfoPanelFont());

    startx = startx + styler.getInfoPanelPadding();
    starty = starty + styler.getInfoPanelPadding();

    double multiLineOffset = 0.0;

    for (Map.Entry<String, Rectangle2D> entry : textBounds.entrySet()) {

      double lineHeight = entry.getValue().getHeight();

      FontRenderContext frc = g.getFontRenderContext();
      TextLayout tl = new TextLayout(entry.getKey(), styler.getInfoPanelFont(), frc);
      Shape shape = tl.getOutline(null);
      AffineTransform orig = g.getTransform();
      AffineTransform at = new AffineTransform();
      at.translate(startx, starty + lineHeight + multiLineOffset);
      g.transform(at);
      g.fill(shape);
      g.setTransform(orig);

      multiLineOffset += lineHeight + MULTI_LINE_SPACE;
    }

    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldHint);
  }

  protected void calculatePosition() {

    if (isValueInScreenSpace) {
      startx = x;
      starty = chart.getHeight() - bounds.getHeight() - y - 1;
    } else {
      startx = getXAxisSreenValue(x);
      starty = getYAxisSreenValue(y) - bounds.getHeight() - 1;
    }

    startx = Math.min(startx, (chart.getWidth() - bounds.getWidth() - 1));
    starty = Math.max(starty, 1);
  }

  private Rectangle2D getBoundsHint() {

    if (!isVisible) {
      return new Rectangle2D.Double();
    }

    // determine text content max width
    double contentMaxWidth = 0;

    // determine total content height
    double contentHeight = 0;

    Map<String, Rectangle2D> textBounds = getTextBounds(lines);

    double entryHeight = 0; // could be multi-line
    for (Map.Entry<String, Rectangle2D> entry : textBounds.entrySet()) {
      entryHeight += entry.getValue().getHeight() + MULTI_LINE_SPACE;
      contentMaxWidth = Math.max(contentMaxWidth, entry.getValue().getWidth());
    }

    entryHeight -= MULTI_LINE_SPACE; // subtract away the bottom MULTI_LINE_SPACE
    contentHeight += entryHeight + styler.getInfoPanelPadding();

    // determine content width
    double contentWidth = styler.getInfoPanelPadding() + contentMaxWidth;

    // Legend Box
    double width = contentWidth + 2 * styler.getInfoPanelPadding();
    double height = contentHeight + styler.getInfoPanelPadding();

    return new Rectangle2D.Double(0, 0, width, height); // 0 indicates not sure yet.
  }

  private Map<String, Rectangle2D> getTextBounds(List<String> lines) {

    Font infoPanelFont = styler.getInfoPanelFont();
    Map<String, Rectangle2D> textBounds = new LinkedHashMap<>(lines.size());
    for (String line : lines) {
      TextLayout textLayout =
          new TextLayout(line, infoPanelFont, new FontRenderContext(null, true, false));
      Shape shape = textLayout.getOutline(null);
      Rectangle2D bounds = shape.getBounds2D();
      textBounds.put(line, bounds);
    }
    return textBounds;
  }

  @Override
  public Rectangle2D getBounds() {
    return getBoundsHint();
  }
}
