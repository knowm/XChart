package org.knowm.xchart.internal.chartpart;

import java.awt.*;

public class CalculaterOutsideS implements OffsetCalculater{
	public double getXOffset(Chart<ST, S> chart, Rectangle2D bounds, int LEGEND_MARGIN) {
        return chart.getPlot().getBounds().getX()
	                + (chart.getPlot().getBounds().getWidth() - bounds.getWidth()) / 2.0;
	}
	public double getYOffset(Chart<ST, S> chart, Rectangle2D bounds, int LEGEND_MARGIN) {
	    return chart.getHeight() - bounds.getHeight() - LEGEND_MARGIN;
	}
}
