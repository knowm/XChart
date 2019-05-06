package org.knowm.xchart;

import java.util.ArrayList;
import java.util.List;

import org.knowm.xchart.internal.chartpart.RenderableSeries;
import org.knowm.xchart.internal.chartpart.RenderableSeries.LegendRenderType;
import org.knowm.xchart.internal.series.AxesChartSeriesNumericalNoErrorBars;
import org.knowm.xchart.internal.series.Foo;

/**
 * A Series containing X and Y data to be plotted on a Chart
 *
 * @author timmolter
 */
public class XYSeries extends AxesChartSeriesNumericalNoErrorBars {

	private XYSeriesRenderStyle xySeriesRenderStyle = null;

	protected List<?> list;
	protected boolean hasExtraValues;
	
	protected XcTrans3<List<?>, Integer, Object, Number> transX;
	protected XcTrans3<List<?>, Integer, Object, Number> transY;
	protected XcTrans3<List<?>, Integer, Object, Number> transExtra;

	public XYSeries(String name, List<? extends Foo> list, DataType axisType
  		  , XcTrans3<List<?>, Integer, Foo, Number> transX
  		  , XcTrans3<List<? extends Foo>, Integer, Foo, Number> transY
  		  , XcTrans3<List<? extends Foo>, Integer, Foo, Number> transExtra
			) {

		super(name, axisType);
		
  		this.list=list;
  		this.transX=transX;
  		this.transY=transY;
  		this.transExtra=transExtra;
  		
		hasExtraValues = transExtra!=null;
		
	    calculateMinMax();
	}

	public XYSeries(String name, double[] xData, double[] yData, double[] errorBars, DataType axisType) {

		super(name, axisType);
		
		list=createFrom(xData, yData, errorBars);
		hasExtraValues = errorBars!=null;
		
		transX=new XcTrans3<List<? extends Foo>, Integer, Foo, Number>() {
			@Override
			public Number trans(List<? extends Foo> o1, Integer o2, Foo o3) {
				return ((FooDouble)o3).x;
			}
		};
		transY=new XcTrans3<List<? extends Foo>, Integer, Foo, Number>() {
			@Override
			public Number trans(List<? extends Foo> o1, Integer o2, Foo o3) {
				return ((FooDouble)o3).y;
			}
		};
		transExtra=new XcTrans3<List<? extends Foo>, Integer, Foo, Number>() {
			@Override
			public Number trans(List<? extends Foo> o1, Integer o2, Foo o3) {
				return ((FooDouble)o3).extra;
			}
		};

	    calculateMinMax();
//	    System.out.println("name="+name);
//	    System.out.println("  x min="+xMin+", max="+xMax);
//	    System.out.println("  y min="+yMin+", max="+yMax);
	}

	public static List<? extends Foo> createFrom(double[] xData, double[] yData, double[] extraData) {
		
		if(xData==null) {
			xData=new double[yData.length];
			for(int i=0; i<xData.length; i++) {
				xData[i]=i;
			}
		} else {
			if(yData.length!=xData.length)
				throw new IllegalArgumentException();
		}
		if(extraData!=null) {
			if(yData.length!=extraData.length)
				throw new IllegalArgumentException();
		}
		
		List<FooDouble> listNew=new ArrayList<FooDouble>();
		for(int i=0; i<xData.length; i++) {
			Double extrai=null;
			if(extraData!=null)
				extrai=extraData[i];
			FooDouble foo=new FooDouble(xData[i], yData[i], extrai);
			listNew.add(foo);
		}
		return listNew;
	}

	public XYSeriesRenderStyle getXYSeriesRenderStyle() {

		return xySeriesRenderStyle;
	}

	public XYSeries setXYSeriesRenderStyle(XYSeriesRenderStyle chartXYSeriesRenderStyle) {

		this.xySeriesRenderStyle = chartXYSeriesRenderStyle;
		return this;
	}

	@Override
	public LegendRenderType getLegendRenderType() {

		return xySeriesRenderStyle.getLegendRenderType();
	}

	public enum XYSeriesRenderStyle implements RenderableSeries {
		Line(LegendRenderType.Line),

		Area(LegendRenderType.Line),

		Step(LegendRenderType.Line),

		StepArea(LegendRenderType.Line),

		Scatter(LegendRenderType.Scatter);

		private final LegendRenderType legendRenderType;

		XYSeriesRenderStyle(LegendRenderType legendRenderType) {

			this.legendRenderType = legendRenderType;
		}

		@Override
		public LegendRenderType getLegendRenderType() {

			return legendRenderType;
		}
	}

	@Override
	public List<? extends Foo> getData() {
		return list;
	}

	@Override
	public Number getX(int observationi, Foo obj) {
		return transX.trans(list, observationi, obj);
	}

	@Override
	public Number getY(int observationi, Foo obj) {
		return transY.trans(list, observationi, obj);
	}

	@Override
	public Number getExtraValue(int observationi, Foo obj) {
		return transExtra.trans(list, observationi, obj);
	}

	@Override
	public boolean hasExtraValues() {
		return hasExtraValues;
	}

}

class FooDouble extends Foo {
	public FooDouble(double x, double y, Double extrai) {
		this.x=x;
		this.y=y;
		this.extra=extrai;
	}
	double x;
	double y;
	Double extra;
}
