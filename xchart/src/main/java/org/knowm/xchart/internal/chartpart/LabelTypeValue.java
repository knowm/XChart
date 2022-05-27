package org.knowm.xchart.internal.chartpart;

public class LabelTypeValue implements LabelType{
    @Override
    public String getLabel(String name,Number y) {
        return name;
    }
}
