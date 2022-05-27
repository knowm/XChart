package org.knowm.xchart.internal.chartpart;

import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieSeries;
import org.knowm.xchart.style.PieStyler;

public interface IPieLabelType {
    String getLabel(String name, Number y);
    static<ST extends PieStyler,S extends PieSeries> IPieLabelType create(PieChart pieChart){
        PieStyler.LabelType type = pieChart.getStyler().getLabelType();
        switch(type){
            case Name:return new PieLabelTypeName();
            case NameAndPercentage:return new PieLabelTypeNameAndPercentage(pieChart);
            case NameAndValue:return new PieLabelTypeNameAndValue(pieChart);
            case Percentage:return new PieLabelTypePercentage(pieChart);
            case Value:return new PieLabelTypeValue(pieChart);
            default:return new PieLabelTypeDefault(pieChart);
        }
    }


    PieStyler.LabelType getType();
}
