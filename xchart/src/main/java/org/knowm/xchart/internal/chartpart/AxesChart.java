package org.knowm.xchart.internal.chartpart;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Format;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.knowm.xchart.internal.series.AxesChartSeries;
import org.knowm.xchart.style.AxesChartStyler;

/**
 * Abstract base class for all chart types that have X and Y axes.
 *
 * <p>Sits between {@link Chart} and concrete axes-based charts (XYChart, CategoryChart, etc.),
 * holding axis state and methods that are meaningless for non-axes charts (PieChart, DialChart,
 * RadarChart).
 */
public abstract class AxesChart<ST extends AxesChartStyler, S extends AxesChartSeries>
    extends Chart<ST, S> {

  protected AxisPair<ST, S> axisPair;
  protected final ArrayList<ChartPart> annotations = new ArrayList<>();

  private String xAxisTitle = "";
  private String yAxisTitle = "";
  private final Map<Integer, String> yAxisGroupTitleMap = new HashMap<>();

  /**
   * Constructor
   *
   * @param width
   * @param height
   * @param styler
   */
  protected AxesChart(int width, int height, ST styler) {

    super(width, height, styler);
  }

  public String getXAxisTitle() {

    return xAxisTitle;
  }

  public void setXAxisTitle(String xAxisTitle) {

    this.xAxisTitle = xAxisTitle;
  }

  public String getYAxisTitle() {

    return yAxisTitle;
  }

  public void setYAxisTitle(String yAxisTitle) {

    this.yAxisTitle = yAxisTitle;
  }

  public String getYAxisGroupTitle(int yAxisGroup) {

    String title = yAxisGroupTitleMap.get(yAxisGroup);
    if (title == null) {
      return yAxisTitle;
    }
    return title;
  }

  public void setYAxisGroupTitle(int yAxisGroup, String yAxisTitle) {

    yAxisGroupTitleMap.put(yAxisGroup, yAxisTitle);
  }

  public void addAnnotation(Annotation annotation) {

    annotations.add(annotation);
    annotation.init(this);
  }

  /**
   * Sets a custom formatting function for X-axis tick labels. The function receives the raw tick
   * value as a {@code Double} and returns the label string to display. This is a convenience
   * shortcut for {@code chart.getStyler().setXAxisTickLabelsFormattingFunction(fn)}.
   *
   * @param customFormattingFunction the formatting function; pass {@code null} to remove
   */
  public void setCustomXAxisTickLabelsFormatter(Function<Double, String> customFormattingFunction) {
    styler.setXAxisTickLabelsFormattingFunction(customFormattingFunction);
  }

  /**
   * Sets a custom formatting function for Y-axis tick labels. The function receives the raw tick
   * value as a {@code Double} and returns the label string to display. This is a convenience
   * shortcut for {@code chart.getStyler().setYAxisTickLabelsFormattingFunction(fn)}.
   *
   * @param customFormattingFunction the formatting function; pass {@code null} to remove
   */
  public void setCustomYAxisTickLabelsFormatter(Function<Double, String> customFormattingFunction) {
    styler.setYAxisTickLabelsFormattingFunction(customFormattingFunction);
  }

  Axis_X<ST, S> getXAxis() {

    return axisPair.getXAxis();
  }

  Axis_Y<ST, S> getYAxis() {

    return axisPair.getYAxis();
  }

  Axis_Y<ST, S> getYAxis(int yIndex) {

    return axisPair.getYAxis(yIndex);
  }

  AxisPair<ST, S> getAxisPair() {

    return axisPair;
  }

  Format getXAxisFormat() {
    return axisPair.getXAxis().getAxisTickCalculator().getAxisFormat();
  }

  Format getYAxisFormat() {
    return axisPair.getYAxis().getAxisTickCalculator().getAxisFormat();
  }

  Format getYAxisFormat(String yAxisDecimalPattern) {
    final Format format;
    if (yAxisDecimalPattern != null) {
      // Use the styler's locale for the decimal format symbols so the tooltip's decimal separator
      // matches the axis, which formats via the styler locale (see Formatter_Number). Otherwise a
      // JVM default locale like French/German would render the decimal separator as a comma in the
      // tooltip while the axis shows a period.
      format =
          new DecimalFormat(
              yAxisDecimalPattern, DecimalFormatSymbols.getInstance(styler.getLocale()));
    } else {
      format = axisPair.getYAxis().getAxisTickCalculator().getAxisFormat();
    }
    return format;
  }

  public double getChartXFromCoordinate(int screenX) {

    return axisPair.getXAxis().getChartValue(screenX);
  }

  public double getChartYFromCoordinate(int screenY) {

    return axisPair.getYAxis().getChartValue(screenY);
  }

  public double getChartYFromCoordinate(int screenY, int yIndex) {

    return axisPair.getYAxis(yIndex).getChartValue(screenY);
  }

  public double getScreenXFromChart(double xValue) {

    return axisPair.getXAxis().getScreenValue(xValue);
  }

  public double getScreenYFromChart(double yValue) {

    return axisPair.getYAxis().getScreenValue(yValue);
  }

  public double getScreenYFromChart(double yValue, int yIndex) {

    return axisPair.getYAxis(yIndex).getScreenValue(yValue);
  }

  public double getYAxisLeftWidth() {

    java.awt.geom.Rectangle2D.Double bounds = getAxisPair().getLeftYAxisBounds();
    return bounds.width + bounds.x;
  }
}
