package org.knowm.xchart.internal.chartpart;

import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.style.Styler;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.LinkedHashMap;
import java.util.Map;

public interface OffsetCalculater  {
	public double getXOffset(Chart chart, Rectangle2D bounds, int LEGEND_MARGIN);
	public double getYOffset(Chart chart, Rectangle2D bounds, int LEGEND_MARGIN);
	
}
