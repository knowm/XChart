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

    public void dataSanityCheck() {

        boolean notSameErrorBarsAndY_Axis = getExtraValues() != null && getExtraValues().length != getYData().length;
        boolean notSameXAndY_Axis = getXData().length != getYData().length;
        // Sanity check
        if (notSameErrorBarsAndY_Axis) {
            throw new IllegalArgumentException("error bars and Y-Axis sizes are not the same!!!");
        }
        if (notSameXAndY_Axis) {
            throw new IllegalArgumentException("X and Y-Axis sizes are not the same!!!");
        }
    }
}
