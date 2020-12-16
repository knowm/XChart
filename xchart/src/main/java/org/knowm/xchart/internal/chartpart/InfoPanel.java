package org.knowm.xchart.internal.chartpart;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.knowm.xchart.style.Styler;

/** An info panel that can be displayed together on top of the chart */
public class InfoPanel implements ChartPart {

  private static final int MULTI_LINE_SPACE = 3;

  private final List<String> lines;
  private final int xPosition;
  private final int yPosition;
  private final double xPositionD;
  private final double yPositionD;

  private final Chart chart;
  private final Styler styler;

  // TODO Need these???
  private double xOffset = 0;
  private double yOffset = 0;
  private Rectangle2D bounds;

  /** Constructor */
  public InfoPanel(
      List<String> lines,
      int xPosition,
      int yPosition,
      double xPositionD,
      double yPositionD,
      Chart chart) {

    this.lines = lines;
    this.xPosition = xPosition;
    this.yPosition = yPosition;
    this.xPositionD = xPositionD; // NaN indicates that the positioning was defined via ints
    this.yPositionD = yPositionD; // NaN indicates that the positioning was defined via ints
    this.chart = chart;
    this.styler = chart.getStyler();
  }

  @Override
  public void paint(Graphics2D g) {

    if (!styler.isInfoPanelVisible()) {
      return;
    }

    bounds = getBoundsHint();
    //    System.out.println("bounds = " + bounds);

    // Info panel draw position
    double height = bounds.getHeight();
    double width = bounds.getWidth();

    // first find out if the positioning was defined as a percatge or an absolute pixel number
    if (!Double.valueOf(xPositionD).isNaN()) { // relative positioning

      xOffset = chart.getWidth() * xPositionD;
      yOffset = chart.getHeight() - height - chart.getHeight() * yPositionD - 1;

    } else { // absolute positioning

      xOffset = xPosition;
      yOffset = chart.getHeight() - height - yPosition - 1;
    }

    xOffset = Math.min(xOffset, (chart.getWidth() - width - 1));
    yOffset = Math.max(yOffset, 0);

    // Draw info panel box background and border
    Shape rect = new Rectangle2D.Double(xOffset, yOffset, bounds.getWidth(), height);
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

    double startx = xOffset + styler.getInfoPanelPadding();
    double starty = yOffset + styler.getInfoPanelPadding();

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

  private Rectangle2D getBoundsHint() {

    if (!styler.isInfoPanelVisible()) {
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
    Map<String, Rectangle2D> textBounds = new LinkedHashMap<String, Rectangle2D>(lines.size());
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

  //  float getEntryHeight(Map<String, Rectangle2D> seriesTextBounds, int markerSize) {
  //
  //    float legendEntryHeight = 0;
  //    for (Map.Entry<String, Rectangle2D> entry : seriesTextBounds.entrySet()) {
  //      legendEntryHeight += entry.getValue().getHeight() + MULTI_LINE_SPACE;
  //    }
  //    legendEntryHeight -= MULTI_LINE_SPACE;
  //
  //    legendEntryHeight = Math.max(legendEntryHeight, markerSize);
  //
  //    return legendEntryHeight;
  //  }
  //
  //  float getLegendEntryWidth(Map<String, Rectangle2D> seriesTextBounds, int markerSize) {
  //
  //    float legendEntryWidth = 0;
  //    for (Map.Entry<String, Rectangle2D> entry : seriesTextBounds.entrySet()) {
  //      legendEntryWidth = Math.max(legendEntryWidth, (float) entry.getValue().getWidth());
  //    }
  //
  //    return legendEntryWidth + markerSize + styler.getInfoPanelPadding();
  //  }
}
