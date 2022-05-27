package org.knowm.xchart.internal.chartpart;

import java.text.DecimalFormat;

public class LabelTypeNameAndValue implements LabelType{
    private final DecimalFormat df = new DecimalFormat("#.0");

    @Override
    public String getLabel(String name, Number y) {
        if (pieStyler.getDecimalPattern() != null) {
            return name + " (" + df.format(y) + ")";
        } else {
            return name + " (" + y.toString() + ")";
        }
    }
}
