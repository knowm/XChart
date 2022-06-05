package org.knowm.xchart.internal.chartpart;

import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieSeries;
import org.knowm.xchart.style.PieStyler;

public interface PieLabelFormat {
    String format(String name, Number y);
    static<ST extends PieStyler,S extends PieSeries> PieLabelFormat create(PieChart pieChart){
        PieStyler.LabelType type = pieChart.getStyler().getLabelType();
        switch(type){
            case Name:return new PieLabelFormatName();
            case NameAndPercentage:return new PieLabelFormatNameAndPercentage(pieChart);
            case NameAndValue:return new PieLabelFormatNameAndValue(pieChart);
            case Percentage:return new PieLabelFormatPercentage(pieChart);
            case Value:return new PieLabelFormatValue(pieChart);
            default:return new PieLabelFormatDefault(pieChart);
        }
    }


    PieStyler.LabelType getType();
}
