/**
 * Copyright 2012 - 2013 Xeiam LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xeiam.xchart;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;

/**
 * A Swing JPanel that contains a Chart
 * <p>
 * Right-click + Save As... or ctrl+S pops up a Save As dialog box for saving the chart as a JPeg or PNG file.
 * 
 * @author timmolter
 */
public class XChartPanel extends JPanel {

  private final Chart chart;

  private String saveAsString = "Save As...";

  /**
   * Constructor
   * 
   * @param chart
   */
  public XChartPanel(final Chart chart) {

    this.chart = chart;

    // Right-click listener for saving chart
    this.addMouseListener(new PopUpMenuClickListener());

    // Control+S key listener for saving chart
    KeyStroke ctrlS = KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
    this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(ctrlS, "save");
    this.getActionMap().put("save", new SaveAction());
  }

  /**
   * Set the "Save As..." String if you want to localize it.
   * 
   * @param saveAsString
   */
  public void setSaveAsString(String saveAsString) {

    this.saveAsString = saveAsString;
  }

  @Override
  protected void paintComponent(Graphics g) {

    super.paintComponent(g);

    chart.paint((Graphics2D) g, getWidth(), getHeight());
  }

  @Override
  public Dimension getPreferredSize() {

    return new Dimension(chart.getWidth(), chart.getHeight());
  }

  private class SaveAction extends AbstractAction {

    public SaveAction() {

      super("save");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

      showSaveAsDialog();
    }
  }

  private void showSaveAsDialog() {

    JFileChooser fileChooser = new JFileChooser();
    fileChooser.addChoosableFileFilter(new JPGSaveFilter());
    fileChooser.addChoosableFileFilter(new PNGSaveFilter());
    fileChooser.setAcceptAllFileFilterUsed(false);

    if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {

      if (fileChooser.getSelectedFile() != null) {
        File theFileToSave = fileChooser.getSelectedFile();
        try {
          if (fileChooser.getFileFilter() == null) {
            BitmapEncoder.savePNG(chart, theFileToSave.getCanonicalPath().toString());
          }
          else if (fileChooser.getFileFilter().getDescription().equals("*.jpg,*.JPG")) {
            BitmapEncoder.saveJPG(chart, theFileToSave.getCanonicalPath().toString() + ".jpg", 1.0f);
          }
          else if (fileChooser.getFileFilter().getDescription().equals("*.png,*.PNG")) {
            BitmapEncoder.savePNG(chart, theFileToSave.getCanonicalPath().toString() + ".png");
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

    }
  }

  private class JPGSaveFilter extends FileFilter {

    @Override
    public boolean accept(File f) {

      if (f.isDirectory()) {
        return false;
      }

      String s = f.getName();

      return s.endsWith(".jpg") || s.endsWith(".JPG");
    }

    @Override
    public String getDescription() {

      return "*.jpg,*.JPG";
    }

  }

  private class PNGSaveFilter extends FileFilter {

    @Override
    public boolean accept(File f) {

      if (f.isDirectory()) {
        return false;
      }

      String s = f.getName();

      return s.endsWith(".png") || s.endsWith(".PNG");
    }

    @Override
    public String getDescription() {

      return "*.png,*.PNG";
    }

  }

  private class PopUpMenuClickListener extends MouseAdapter {

    @Override
    public void mousePressed(MouseEvent e) {

      if (e.isPopupTrigger()) {
        doPop(e);
      }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

      if (e.isPopupTrigger()) {
        doPop(e);
      }
    }

    private void doPop(MouseEvent e) {

      XChartPanelPopupMenu menu = new XChartPanelPopupMenu();
      menu.show(e.getComponent(), e.getX(), e.getY());
    }
  }

  private class XChartPanelPopupMenu extends JPopupMenu {

    JMenuItem saveAsMenuItem;

    public XChartPanelPopupMenu() {

      saveAsMenuItem = new JMenuItem(saveAsString);
      saveAsMenuItem.addMouseListener(new MouseListener() {

        @Override
        public void mouseReleased(MouseEvent e) {

          showSaveAsDialog();
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseClicked(MouseEvent e) {

        }
      });
      add(saveAsMenuItem);
    }
  }

  /**
   * update a series by only updating the Y-Axis data
   * 
   * @param seriesName
   * @param newYData
   */
  public void updateSeries(String seriesName, List<Number> newYData) {

    Series series = chart.getSeriesMap().get(seriesName);
    if (series == null) {
      throw new IllegalArgumentException("Series name >" + seriesName + "< not found!!!");
    }
    series.replaceYData(newYData);

    // generate X-Data
    List<Number> generatedXData = new ArrayList<Number>();
    for (int i = 1; i < newYData.size() + 1; i++) {
      generatedXData.add(i);
    }
    series.replaceXData(generatedXData);

    // Re-display the chart
    revalidate();
    repaint();
  }
}
