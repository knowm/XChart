package org.knowm.xchart;

import java.awt.Graphics2D;

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
	    paintTarget.paint(graphics);
		annotations.paint(graphics);
	}
	
	protected void settingPaint(int width, int height) {
		setWidth(width);
		setHeight(height);
		specificSetting();
	}
	
	abstract protected void specificSetting();
	
	
	protected void setSeriesStyles() {

		 for (Series series : getSeriesMap().values()) {

		   setSeriesDefaultForNullPart(series, getSeriesColorMarkerLineStyle());
		 }
	}
	
	protected void seriesNameDuplicateCheck(String seriesName) {
		if (seriesMap.containsKey(seriesName)) {
	      throw new IllegalArgumentException(
	          "Series name > "
	              + seriesName
	              + " < has already been used. Use unique names for each series!!!");
	    }
	}

	
	abstract protected void setSeriesDefaultForNullPart(Series series, SeriesColorMarkerLineStyle seriesColorMarkerLineStyle);
}
