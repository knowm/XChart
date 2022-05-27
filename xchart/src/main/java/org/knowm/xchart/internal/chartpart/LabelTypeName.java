package org.knowm.xchart.internal.chartpart;

import java.text.DecimalFormat;

public class LabelTypeName implements LabelType{
    private final DecimalFormat df = new DecimalFormat("#.0");

    @Override
    public String getLabel(String name, Number y) {
        return name;
    }
}
