package org.knowm.xchart.internal.chartpart;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class ChartPartComposite implements ChartPart{
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
    public Rectangle2D getBounds(){
        Rectangle2D temp = new Rectangle2D.Double();
        for(ChartPart chartPart: chartParts){
            temp = temp.createUnion(chartPart.getBounds());
        }
        return temp;
    }
}
