package org.knowm.xchart;

import java.awt.Graphics2D;
import java.util.List;

import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.internal.style.SeriesColorMarkerLineStyle;
import org.knowm.xchart.internal.style.SeriesColorMarkerLineStyleCycler;
import org.knowm.xchart.style.Styler;

public abstract class AbstractChart<ST extends Styler, S extends Series> extends Chart<ST, S> {


	protected AbstractChart(int width, int height, ST styler) {
		super(width, height, styler);
	}

	protected SeriesColorMarkerLineStyle getSeriesColorMarkerLineStyle() {
		SeriesColorMarkerLineStyleCycler seriesColorMarkerLineStyleCycler =
	        new SeriesColorMarkerLineStyleCycler(
	            getStyler().getSeriesColors(),
	            getStyler().getSeriesMarkers(),
	            getStyler().getSeriesLines());
		return seriesColorMarkerLineStyleCycler.getNextSeriesColorMarkerLineStyle();
	}
	
	protected void doPaint(Graphics2D graphics) {
	    paintBackground(graphics);

		axisPair.paint(graphics);
		plot.paint(graphics);
		chartTitle.paint(graphics);
		legend.paint(graphics);
		annotations.paint(graphics);
	}
	
	protected void settingPaint(int width, int height) {
		setWidth(width);
		setHeight(height);
		specificSetting();
	}
	
	abstract protected void specificSetting();
	
	
	protected void checkSeriesValidity(String seriesName, Series series) {
		if (series == null) {
	      throw new IllegalArgumentException("Series name >" + seriesName + "< not found!!!");
	    }
	}
	
	protected void sanityCheckYData(List<? extends Number> yData) {

		 if (yData == null) {
		   throw new IllegalArgumentException("Y-Axis data connot be null !!!");
		 }
		 if (yData.size() == 0) {
		   throw new IllegalArgumentException("Y-Axis data connot be empyt !!!");
		 }
		 if (yData.contains(null)) {
		   throw new IllegalArgumentException("Y-Axis data cannot contain null !!!");
		 }
	}
	
	protected void sanityCheckYData(double[] yData) {
		 if (yData == null) {
		   throw new IllegalArgumentException("Y-Axis data connot be null !!!");
		 }
		 if (yData.length == 0) {
		   throw new IllegalArgumentException("Y-Axis data connot be empyt !!!");
		 }
	}
	
	protected void setSeriesStyles() {

		 for (Series series : getSeriesMap().values()) {

		   setSeriesDefaultForNullPart(series, getSeriesColorMarkerLineStyle());
		 }
	}
	
	abstract protected void setSeriesDefaultForNullPart(Series series, SeriesColorMarkerLineStyle seriesColorMarkerLineStyle);
}
