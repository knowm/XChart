package org.knowm.xchart.internal.chartpart;

import org.knowm.xchart.PieChart;
import org.knowm.xchart.style.PieStyler;

import java.text.DecimalFormat;

public class PieLabelTypeValue implements IPieLabelType {
    private final DecimalFormat df = new DecimalFormat("#.0");
    private PieChart chart;
    public PieLabelTypeValue(PieChart chart){
        this.chart =chart;
    }
    @Override
    public String getLabel(String name,Number y) {
        if (chart.getStyler().getDecimalPattern() != null) {
            return df.format(y);
        } else {
            return y.toString();
        }
    }

    @Override
    public PieStyler.LabelType getType() {
        return PieStyler.LabelType.Value;
    }
}
