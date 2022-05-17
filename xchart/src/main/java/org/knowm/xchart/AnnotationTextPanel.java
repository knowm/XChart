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

public class AnnotationTextPanel extends AnnotationWithXY {

  private static final int MULTI_LINE_SPACE = 3;

  private List<String> lines;

  /**
   * Constructor
   *
   * @param lines
   * @param x
   * @param y
   * @param isValueInScreenSpace
   */
  public AnnotationTextPanel(String lines, double x, double y, boolean isValueInScreenSpace) {
    super(isValueInScreenSpace);
    this.lines = Arrays.asList(lines.split("\\n"));
    this.x = x;
    this.y = y;
  }

  public void init(Chart chart) {

    super.init(chart);
  }

  @Override
  public void paint(Graphics2D graphic) {

    if (!isVisible) {
      return;
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
    contentHeight += entryHeight;

    // determine content width
    double contentWidth = styler.getAnnotationTextPanelPadding() + contentMaxWidth;

    double width = contentWidth + 2 * styler.getAnnotationTextPanelPadding();
    double height = contentHeight + 2 * styler.getAnnotationTextPanelPadding();

    double xOffset;
    double yOffset;

    if (isValueInScreenSpace) {
      xOffset = x;
      yOffset = chart.getHeight() - height - y - 1;
    } else {
      xOffset = getXAxisScreenValue(x);
      yOffset = getYAxisScreenValue(y) - height - 1;
    }

    xOffset = Math.min(xOffset, (chart.getWidth() - width - 1));
    yOffset = Math.max(yOffset, 1);

    bounds = new Rectangle2D.Double(xOffset, yOffset, width, height); // 0 indicates not sure yet.

    // Draw info panel box background and border
    Shape rect = new Rectangle2D.Double(xOffset, yOffset, width, height);
    graphic.setColor(styler.getAnnotationTextPanelBackgroundColor());
    graphic.fill(rect);
    graphic.setStroke(SOLID_STROKE);
    graphic.setColor(styler.getAnnotationTextPanelBorderColor());
    graphic.draw(rect);

    // Draw text onto panel box
    Object oldHint = graphic.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
    graphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    graphic.setColor(styler.getAnnotationTextPanelFontColor());
    graphic.setFont(styler.getAnnotationTextPanelFont());

    xOffset = xOffset + styler.getAnnotationTextPanelPadding();
    yOffset = yOffset + styler.getAnnotationTextPanelPadding();

    double multiLineOffset = 0.0;

    for (Map.Entry<String, Rectangle2D> entry : textBounds.entrySet()) {

      double lineHeight = entry.getValue().getHeight();

      FontRenderContext frc = graphic.getFontRenderContext();
      TextLayout tl = new TextLayout(entry.getKey(), styler.getAnnotationTextPanelFont(), frc);
      Shape shape = tl.getOutline(null);
      AffineTransform orig = graphic.getTransform();
      AffineTransform at = new AffineTransform();
      at.translate(xOffset, yOffset + lineHeight + multiLineOffset);
      graphic.transform(at);
      graphic.fill(shape);
      graphic.setTransform(orig);

      multiLineOffset += lineHeight + MULTI_LINE_SPACE;
    }
    //    System.out.println("bounds = " + bounds);
    graphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldHint);
  }

  private Map<String, Rectangle2D> getTextBounds(List<String> lines) {

    Font infoPanelFont = styler.getAnnotationTextPanelFont();
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

  public void setLines(List<String> lines) {
    this.lines = lines;
  }
}
