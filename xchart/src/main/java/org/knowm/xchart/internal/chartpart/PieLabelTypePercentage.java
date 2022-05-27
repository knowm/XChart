package org.knowm.xchart.internal.chartpart;

import org.knowm.xchart.PieChart;
import org.knowm.xchart.style.PieStyler;

import java.text.DecimalFormat;

public class PieLabelTypePercentage implements IPieLabelType {
    private final DecimalFormat df = new DecimalFormat("#.0");
    PieChart chart;
    public PieLabelTypePercentage(PieChart chart){
        this.chart = chart;
    }
    @Override
    public String getLabel(String name,Number y) {
        double percentage = y.doubleValue() / chart.getTotal() * 100;
        return df.format(percentage) + "%";
    }

    @Override
    public PieStyler.LabelType getType() {
        return PieStyler.LabelType.Percentage;
    }
}
