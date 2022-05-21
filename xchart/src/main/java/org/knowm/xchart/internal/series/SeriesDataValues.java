package org.knowm.xchart.internal.series;

public class SeriesDataValues {
    private final double[] XData;
    private final double[] YData;
    private final double[] ExtraValues;

    /**
     * @param xData
     * @param YData
     * @param ExtraValues
     */
    public SeriesDataValues(double[] xData, double[] YData, double[] ExtraValues) {
        this.XData = xData;
        this.YData = YData;
        this.ExtraValues = ExtraValues;
    }

    public double[] getXData() {
        return XData;
    }

    public double[] getYData() {
        return YData;
    }

    public double[] getExtraValues() {
        return ExtraValues;
    }
}
