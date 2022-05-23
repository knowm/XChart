package org.knowm.xchart.internal.chartpart;

import java.awt.*;

public class CalculaterInsideSE implements OffsetCalculater{
	public double getXOffset(Chart<ST, S> chart, Rectangle2D bounds, int LEGEND_MARGIN) {
        return chart.getPlot().getBounds().getX()
	                + chart.getPlot().getBounds().getWidth()
	                - bounds.getWidth()
	                - LEGEND_MARGIN;
	}
	public double getYOffset(Chart<ST, S> chart, Rectangle2D bounds, int LEGEND_MARGIN) {
	        return chart.getPlot().getBounds().getY()
	                + chart.getPlot().getBounds().getHeight()
	                - bounds.getHeight()
	                - LEGEND_MARGIN;
	}
}
