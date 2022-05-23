package org.knowm.xchart.internal.chartpart;

import java.awt.*;
import java.util.ArrayList;

public class ChartPartComposite {
    private ArrayList<ChartPart> chartParts;
    public ChartPartComposite(){
        chartParts = new ArrayList<>();
    }
    public void addChartPart(ChartPart chartPart){
        if(!chartParts.contains(chartPart)  && chartPart != null)
            chartParts.add(chartPart);
    }
    public void removeChartPart(ChartPart chartPart){
        if(chartParts.contains(chartPart) && chartPart != null)
            chartParts.remove(chartPart);
    }
    public void clear(){
        chartParts.clear();
    }
    public void paint(Graphics2D g){
        for(ChartPart chartPart: chartParts){
            chartPart.paint(g);
        }
    }
}
