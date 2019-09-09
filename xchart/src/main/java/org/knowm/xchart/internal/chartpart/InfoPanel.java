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
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.style.AxesChartStyler;
import org.knowm.xchart.style.Styler;

/** 
 * An info panel that can be displayed together with the chart
 * @author tdiesler@redhat.com 
 */
public class InfoPanel<ST extends Styler, S extends Series> implements ChartPart {

  private static final int INFO_PANEL_MARGIN = 6;
  private static final int MULTI_LINE_SPACE = 3;
  
  private final Chart<ST, S> chart;
  
  private double xOffset = 0;
  private double yOffset = 0;
  private Rectangle2D bounds;

  public InfoPanel(Chart<ST, S> chart) {
    this.chart = chart;
  }

  public void doPaint(Graphics2D g) {

    // Draw legend content inside legend box
    double startx = xOffset + chart.getStyler().getInfoPanelPadding();
    double starty = yOffset + chart.getStyler().getInfoPanelPadding();

    Object oldHint = g.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    List<String> infoContent = chart.getInfoContent();
    Map<String, Rectangle2D> textBounds = getTextBounds(infoContent);

    paintInfoContent(g, textBounds, startx, starty);
    
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldHint);
  }

  @Override
  public void paint(Graphics2D g) {

    if (!chart.getStyler().isInfoPanelVisible()) {
      return;
    }

    // if the area to draw a chart on is so small, don't even bother
    if (chart.getPlot().getBounds().getWidth() < 30) {
      return;
    }

    // Nothing to do when no content is given
    if (chart.getInfoContent().isEmpty()) {
        return;
    }
    
    bounds = getBoundsHintVertical();
    
    // Info panel draw position
    double height = bounds.getHeight();

    switch (chart.getStyler().getInfoPanelPosition()) {
      case OutsideS:
        xOffset = chart.getPlot().getBounds().getX();
        yOffset = chart.getHeight() - bounds.getHeight() - INFO_PANEL_MARGIN;
        break;

      default:
        break;
    }

    // Draw info panel box background and border
    Shape rect = new Rectangle2D.Double(xOffset, yOffset, bounds.getWidth(), height);
    g.setColor(chart.getStyler().getInfoPanelBackgroundColor());
    g.fill(rect);
    g.setStroke(SOLID_STROKE);
    g.setColor(chart.getStyler().getInfoPanelBorderColor());
    g.draw(rect);

    doPaint(g);
  }

  float getEntryHeight(Map<String, Rectangle2D> seriesTextBounds, int markerSize) {

    float legendEntryHeight = 0;
    for (Map.Entry<String, Rectangle2D> entry : seriesTextBounds.entrySet()) {
      legendEntryHeight += entry.getValue().getHeight() + MULTI_LINE_SPACE;
    }
    legendEntryHeight -= MULTI_LINE_SPACE;

    legendEntryHeight = Math.max(legendEntryHeight, markerSize);

    return legendEntryHeight;
  }

  float getLegendEntryWidth(Map<String, Rectangle2D> seriesTextBounds, int markerSize) {

    float legendEntryWidth = 0;
    for (Map.Entry<String, Rectangle2D> entry : seriesTextBounds.entrySet()) {
      legendEntryWidth = Math.max(legendEntryWidth, (float) entry.getValue().getWidth());
    }

    return legendEntryWidth + markerSize + chart.getStyler().getInfoPanelPadding();
  }

  void paintInfoContent(
      Graphics2D g,
      Map<String, Rectangle2D> seriesTextBounds,
      double x,
      double starty) {

    g.setColor(chart.getStyler().getChartFontColor());
    g.setFont(chart.getStyler().getInfoPanelFont());

    double multiLineOffset = 0.0;

    for (Map.Entry<String, Rectangle2D> entry : seriesTextBounds.entrySet()) {

      double height = entry.getValue().getHeight();

      FontRenderContext frc = g.getFontRenderContext();
      TextLayout tl = new TextLayout(entry.getKey(), chart.getStyler().getInfoPanelFont(), frc);
      Shape shape = tl.getOutline(null);
      AffineTransform orig = g.getTransform();
      AffineTransform at = new AffineTransform();
      at.translate(x, starty + height + multiLineOffset);
      g.transform(at);
      g.fill(shape);
      g.setTransform(orig);

      multiLineOffset += height + MULTI_LINE_SPACE;
    }
  }

  private Rectangle2D getBoundsHintVertical() {

    if (!chart.getStyler().isInfoPanelVisible()) {
      return new Rectangle2D.Double();
    }

    // determine text content max width
    double contentMaxWidth = 0;

    // determine total content height
    double contentHeight = 0;

    List<String> infoContent = chart.getInfoContent();
    Map<String, Rectangle2D> textBounds = getTextBounds(infoContent);

    double entryHeight = 0; // could be multi-line
    for (Map.Entry<String, Rectangle2D> entry : textBounds.entrySet()) {
      entryHeight += entry.getValue().getHeight() + MULTI_LINE_SPACE;
      contentMaxWidth = Math.max(contentMaxWidth, entry.getValue().getWidth());
    }

    entryHeight -= MULTI_LINE_SPACE; // subtract away the bottom MULTI_LINE_SPACE
    contentHeight += entryHeight + chart.getStyler().getInfoPanelPadding();

    // determine content width
    double contentWidth = chart.getStyler().getInfoPanelPadding() + contentMaxWidth;

    // Legend Box
    double width = contentWidth + 2 * chart.getStyler().getInfoPanelPadding();
    double height = contentHeight + chart.getStyler().getInfoPanelPadding();

    return new Rectangle2D.Double(0, 0, width, height); // 0 indicates not sure yet.
  }

  Map<String, Rectangle2D> getTextBounds(List<String> lines) {

    Font infoPanelFont = chart.getStyler().getInfoPanelFont();
    Map<String, Rectangle2D> textBounds = new LinkedHashMap<String, Rectangle2D>(lines.size());
    for (String line : lines) {
      TextLayout textLayout = new TextLayout(line, infoPanelFont, new FontRenderContext(null, true, false));
      Shape shape = textLayout.getOutline(null);
      Rectangle2D bounds = shape.getBounds2D();
      textBounds.put(line, bounds);
    }
    return textBounds;
  }

  @Override
  public Rectangle2D getBounds() {
      return getBoundsHintVertical();
  }
}
