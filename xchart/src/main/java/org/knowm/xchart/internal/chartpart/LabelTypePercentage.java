package org.knowm.xchart.internal.chartpart;

import java.text.DecimalFormat;

public class LabelTypePercentage implements LabelType{
    private final DecimalFormat df = new DecimalFormat("#.0");
    @Override
    public String getLabel(String name,Number y) {
        double percentage = y.doubleValue() / total * 100;
        return df.format(percentage) + "%";
    }
}
