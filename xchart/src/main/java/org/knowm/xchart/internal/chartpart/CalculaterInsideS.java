package org.knowm.xchart.internal.chartpart;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.LinkedHashMap;
import java.util.Map;
import org.knowm.xchart.internal.chartpart.RenderableSeries.LegendRenderType;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.style.Styler;

public class CalculaterInsideS implements OffsetCalculater{
	public double getXOffset(Chart chart, Rectangle2D bounds, int LEGEND_MARGIN) {
        return chart.getPlot().getBounds().getX()
	                + (chart.getPlot().getBounds().getWidth() - bounds.getWidth()) / 2
	                + LEGEND_MARGIN;
	}
	public double getYOffset(Chart chart, Rectangle2D bounds, int LEGEND_MARGIN) {
	        return chart.getPlot().getBounds().getY()
	                + chart.getPlot().getBounds().getHeight()
	                - bounds.getHeight()
	                - LEGEND_MARGIN;
	}
}
