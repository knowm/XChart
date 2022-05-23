package org.knowm.xchart.internal.chartpart;

import java.awt.*;

public interface OffsetCalculater {
	public double getXOffset(Chart<ST, S> chart, Rectangle2D bounds, int LEGEND_MARGIN);
	public double getYOffset(Chart<ST, S> chart, Rectangle2D bounds, int LEGEND_MARGIN);
	
}
