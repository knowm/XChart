package org.knowm.xchart.internal.chartpart;

import java.text.DecimalFormat;

public class LabelTypeDefault implements LabelType{
    private final DecimalFormat df = new DecimalFormat("#.0");
    @Override
    public String getLabel(String name,Number y) {
        double percentage = y.doubleValue() / total * 100;
        return name + " (" + df.format(percentage) + "%)";
    }
}
