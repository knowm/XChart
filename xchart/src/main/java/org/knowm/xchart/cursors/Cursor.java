package org.knowm.xchart.cursors;

import java.util.*;
import java.awt.Graphics2D;
import java.awt.event.*;
import org.knowm.xchart.internal.chartpart.ToolTips.DataPoint;

public abstract class Cursor implements MouseMotionListener {
   protected DataPoint dataPoint;

   private final List<DataPoint> dataPointList = new ArrayList<DataPoint>();

   public abstract void paint(Graphics2D g, double xOffset, double yOffset, int cursorSize, double width, double height);

   public void mouseDragged(MouseEvent e) {
      // ignored
   }

   @Override
   public void mouseMoved(MouseEvent e) {
     DataPoint newPoint = null;
     int x = e.getX();
     int y = e.getY();
     for (DataPoint dataPoint : dataPointList) {
       if (dataPoint.shape.contains(x, y) || dataPoint.shape.contains(x, dataPoint.shape.getBounds().getCenterY())) {
         newPoint = dataPoint;
         break;
       }
     }

     if (newPoint != null) {
       // if the existing shown data point is already shown, abort
       if (dataPoint != null) {
         if (dataPoint.equals(newPoint)) {
           return;
         }
       }
       dataPoint = newPoint;
       e.getComponent().repaint(); // repaint the entire XChartPanel
     }
     // remove the popup shape
     //else if (dataPoint != null) {
     //  dataPoint = null;
     //  e.getComponent().repaint(); // repaint the entire XChartPanel
     //}
   }

   public void prepare(Graphics2D g) {
      dataPointList.clear();
   }

   public void addData(double xOffset, double yOffset, double x, double y) {
      dataPointList.add(new DataPoint(xOffset, yOffset, "Real: " + x + "x" + y));
   }
}
