package org.knowm.xchart.cursors;

import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.event.*;
import org.knowm.xchart.internal.chartpart.ToolTips.DataPoint;

public abstract class Cursor implements MouseMotionListener, MouseListener {
   DataPoint dataPoint;
   private final List<CursorListener> cursorListeners = new ArrayList<>();

   private final List<DataPoint> dataPointList = new ArrayList<DataPoint>();

   public abstract void paint(Graphics2D g, double xOffset, double yOffset, int cursorSize, double width, double height);

   public void mouseDragged(MouseEvent e) {
      // ignored
   }

   public void addCursorListener(CursorListener cursorListener) {

      cursorListeners.add(cursorListener);
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
      resetDataPoint(newPoint, e.getComponent());
   }

   private void resetDataPoint(DataPoint newPoint, final Component component) {
      if (newPoint != null) {
         // if the existing shown data point is already shown, abort
         if (dataPoint != null) {
            if (dataPoint.equals(newPoint)) {
               return;
            }
         }
         dataPoint = newPoint;
         int indexOfDataPoint = getIndexOfDataPoint();
         if (indexOfDataPoint > -1) {
            for (CursorListener listener : cursorListeners) {
               listener.fireDataPointSelected(indexOfDataPoint);
            }
         }
         component.repaint(); // repaint the entire XChartPanel
      }
      // remove the popup shape
      //else if (dataPoint != null) {
      //  dataPoint = null;
      //  e.getComponent().repaint(); // repaint the entire XChartPanel
      //}
   }

   private int getIndexOfDataPoint() {
      int indexOf = dataPointList.indexOf(dataPoint);
      if(indexOf == -1 && dataPoint != null) {
         for (int i = 0; i < dataPointList.size(); i++) {
            DataPoint point = dataPointList.get(i);
            if (point.getX() == dataPoint.getX()) {
               return i;
            }
         }
      }
      return indexOf;
   }

   public void prepare(Graphics2D g) {

      dataPointList.clear();
   }

   public void addData(double xOffset, double yOffset) {

      dataPointList.add(new DataPoint(xOffset, yOffset));
   }

   public void mouseClicked(MouseEvent e) {
      int indexOfDataPoint = getIndexOfDataPoint();
      if (indexOfDataPoint > -1) {
         for (CursorListener listener : cursorListeners) {
            listener.fireDataPointClicked(indexOfDataPoint);
         }
      }
   }

   public void mousePressed(MouseEvent e) {

   }

   public void mouseReleased(MouseEvent e) {

   }

   public void mouseEntered(MouseEvent e) {

   }

   public void mouseExited(MouseEvent e) {

   }

   /**
    Interface for listener of cursor events
    */
   public interface CursorListener {

      /**
       fired when the cursor is moved to a new datapoint
       @param index the index of the datapoint which the mouse hovered
       */
      void fireDataPointSelected(int index);
      /**
       fired when the cursor is clicked on a currently selected datapoint
       @param index the index of the datapoint which was clicked
       */
      void fireDataPointClicked(int index);
   }
}
