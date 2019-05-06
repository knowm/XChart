package org.knowm.xchart;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import org.knowm.xchart.internal.chartpart.RenderableSeries;
import org.knowm.xchart.internal.chartpart.RenderableSeries.LegendRenderType;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.knowm.xchart.style.markers.SeriesMarkers;

/**
 * A Series containing X, Y and bubble size data to be plotted on a Chart
 *
 * @author timmolter
 */
public class BubbleSeries
//extends NoMarkersSeries // not needed anymore 
extends XYSeries
{
	  public static List<Double> getGaussian(int number, double mean, double std) {
			Random random=new Random();

		    List<Double> seriesData = new LinkedList<Double>();
		    for (int i = 0; i < number; i++) {
		      seriesData.add(mean + std * random.nextGaussian());
		    }

		    return seriesData;
		  }
	public static void main(String[] args) {
		JFrame fr=new JFrame(BubbleSeries.class.getSimpleName());

		XYChart chart;
		{
			chart = new XYChartBuilder()
					//.width(600).height(500)
					.title("Gaussian Blobs").xAxisTitle("X").yAxisTitle("Y").build();

			// Customize Chart
			chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Scatter);
			chart.getStyler().setChartTitleVisible(false);
			chart.getStyler().setLegendPosition(LegendPosition.InsideSW);
			chart.getStyler().setMarkerSize(16);

			// Series
			chart.addSeries("Gaussian Blob 1", getGaussian(1000, 1, 10), getGaussian(1000, 1, 10));
			XYSeries series = chart.addSeries("Gaussian Blob 2", getGaussian(1000, 1, 10), getGaussian(1000, 0, 5));
			series.setMarker(SeriesMarkers.DIAMOND);
		}

		XChartPanel<XYChart> panel = new XChartPanel<XYChart>(chart);
		fr.getContentPane().add(panel);
		fr.setSize(1920>>1,  1080>>1);
		fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fr.setVisible(true);
		
	}

  private BubbleSeriesRenderStyle bubbleSeriesRenderStyle = null;

  /**
   * Constructor
   *
   * @param name
   * @param xData
   * @param yData
   * @param bubbleSizes
   */
  public BubbleSeries(String name
		  , double[] xData, double[] yData, double[] bubbleSizes
		  ) {

    super(name
    		, xData, yData, bubbleSizes
    		, DataType.Number);

    System.out.println("list.sz="+list.size());
    if(bubbleSizes==null)
    	System.out.println("bubbleSizes="+bubbleSizes);
    else
    	System.out.println("bubbleSizes.len="+bubbleSizes.length);
    System.out.println("extra?="+hasExtraValues());
  }
  
  @Override
  protected void calculateMinMax() {
	  calculateMinMax(false);
  }

  public BubbleSeriesRenderStyle getBubbleSeriesRenderStyle() {

    return bubbleSeriesRenderStyle;
  }

  public void setBubbleSeriesRenderStyle(BubbleSeriesRenderStyle bubbleSeriesRenderStyle) {

    this.bubbleSeriesRenderStyle = bubbleSeriesRenderStyle;
  }

  @Override
  public LegendRenderType getLegendRenderType() {

    return bubbleSeriesRenderStyle.getLegendRenderType();
  }

  public enum BubbleSeriesRenderStyle implements RenderableSeries {
    Round(LegendRenderType.Box);

    private final LegendRenderType legendRenderType;

    BubbleSeriesRenderStyle(LegendRenderType legendRenderType) {

      this.legendRenderType = legendRenderType;
    }

    @Override
    public LegendRenderType getLegendRenderType() {

      return legendRenderType;
    }
  }
}
