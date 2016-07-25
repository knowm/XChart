/**
 * Copyright 2015-2016 Knowm Inc. (http://knowm.org) and contributors.
 * Copyright 2011-2015 Xeiam LLC (http://xeiam.com) and contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.knowm.xchart;

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

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;

import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.VectorGraphicsEncoder.VectorGraphicsFormat;
import org.knowm.xchart.internal.chartpart.Chart;

/**
 * A Swing JPanel that contains a Chart
 * <p>
 * Right-click + Save As... or ctrl+S pops up a Save As dialog box for saving the chart as PNG, JPEG, etc. file.
 *
 * @author timmolter
 */
public class XChartPanel<T extends Chart> extends JPanel {

  private final T chart;
  private final Dimension preferredSize;
  private String saveAsString = "Save As...";
  private String exportAsString = "Export As...";

  /**
   * Constructor
   *
   * @param chart
   */
  public XChartPanel(final T chart) {

    this.chart = chart;
    preferredSize = new Dimension(chart.getWidth(), chart.getHeight());

    // Right-click listener for saving chart
    this.addMouseListener(new PopUpMenuClickListener());

    // Control+S key listener for saving chart
    KeyStroke ctrlS = KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
    this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(ctrlS, "save");
    this.getActionMap().put("save", new SaveAction());

    // Control+E key listener for saving chart
    KeyStroke ctrlE = KeyStroke.getKeyStroke(KeyEvent.VK_E, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
    this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(ctrlE, "export");
    this.getActionMap().put("export", new ExportAction());
  }

  /**
   * Set the "Save As..." String if you want to localize it.
   *
   * @param saveAsString
   */
  public void setSaveAsString(String saveAsString) {

    this.saveAsString = saveAsString;
  }

  /**
   * Set the "Export As..." String if you want to localize it.
   *
   * @param exportAsString
   */
  public void setExportAsString(String exportAsString) {

    this.exportAsString = exportAsString;
  }

  @Override
  protected void paintComponent(Graphics g) {

    super.paintComponent(g);

    Graphics2D g2d = (Graphics2D) g.create();
    chart.paint(g2d, getWidth(), getHeight());
    g2d.dispose();
  }

  public T getChart() {

    return this.chart;
  }

  @Override
  public Dimension getPreferredSize() {

    return preferredSize;
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

  private class ExportAction extends AbstractAction {

    public ExportAction() {

      super("export");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

      showExportAsDialog();
    }
  }

  private void showSaveAsDialog() {

    JFileChooser fileChooser = new JFileChooser();
    FileFilter pngFileFilter = new SuffixSaveFilter("png"); // default
    fileChooser.addChoosableFileFilter(pngFileFilter);
    fileChooser.addChoosableFileFilter(new SuffixSaveFilter("jpg"));
    fileChooser.addChoosableFileFilter(new SuffixSaveFilter("bmp"));
    fileChooser.addChoosableFileFilter(new SuffixSaveFilter("gif"));

    // VectorGraphics2D is optional, so if it's on the classpath, allow saving charts as vector graphic
    try {
      Class.forName("de.erichseifert.vectorgraphics2d.VectorGraphics2D");
      // it exists on the classpath
      fileChooser.addChoosableFileFilter(new SuffixSaveFilter("svg"));
      fileChooser.addChoosableFileFilter(new SuffixSaveFilter("eps"));
      fileChooser.addChoosableFileFilter(new SuffixSaveFilter("pdf"));
    } catch (ClassNotFoundException e) {
      // it does not exist on the classpath
    }

    fileChooser.setAcceptAllFileFilterUsed(false);

    fileChooser.setFileFilter(pngFileFilter);

    if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {

      if (fileChooser.getSelectedFile() != null) {
        File theFileToSave = fileChooser.getSelectedFile();
        try {
          if (fileChooser.getFileFilter() == null) {
            BitmapEncoder.saveBitmap(chart, theFileToSave.getCanonicalPath().toString(), BitmapFormat.PNG);
          }
          else if (fileChooser.getFileFilter().getDescription().equals("*.jpg,*.JPG")) {
            BitmapEncoder.saveJPGWithQuality(chart, BitmapEncoder.addFileExtension(theFileToSave.getCanonicalPath().toString(), BitmapFormat.JPG), 1.0f);
          }
          else if (fileChooser.getFileFilter().getDescription().equals("*.png,*.PNG")) {
            BitmapEncoder.saveBitmap(chart, theFileToSave.getCanonicalPath().toString(), BitmapFormat.PNG);
          }
          else if (fileChooser.getFileFilter().getDescription().equals("*.bmp,*.BMP")) {
            BitmapEncoder.saveBitmap(chart, theFileToSave.getCanonicalPath().toString(), BitmapFormat.BMP);
          }
          else if (fileChooser.getFileFilter().getDescription().equals("*.gif,*.GIF")) {
            BitmapEncoder.saveBitmap(chart, theFileToSave.getCanonicalPath().toString(), BitmapFormat.GIF);
          }
          else if (fileChooser.getFileFilter().getDescription().equals("*.svg,*.SVG")) {
            VectorGraphicsEncoder.saveVectorGraphic(chart, theFileToSave.getCanonicalPath().toString(), VectorGraphicsFormat.SVG);
          }
          else if (fileChooser.getFileFilter().getDescription().equals("*.eps,*.EPS")) {
            VectorGraphicsEncoder.saveVectorGraphic(chart, theFileToSave.getCanonicalPath().toString(), VectorGraphicsFormat.EPS);
          }
          else if (fileChooser.getFileFilter().getDescription().equals("*.pdf,*.PDF")) {
            VectorGraphicsEncoder.saveVectorGraphic(chart, theFileToSave.getCanonicalPath().toString(), VectorGraphicsFormat.PDF);
          }
        } catch (IOException e) {
          e.printStackTrace();
        }

      }

    }
  }

  private void showExportAsDialog() {

    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    fileChooser.setAcceptAllFileFilterUsed(false);
    fileChooser.setDialogTitle("Export");

    if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {

      File theFileToSave = fileChooser.getSelectedFile();
      try {
        CSVExporter.writeCSVColumns((XYChart) chart, theFileToSave.getCanonicalPath().toString() + File.separatorChar);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * File filter based on the suffix of a file. This file filter accepts all files that end with .suffix or the capitalized suffix.
   *
   * @author Benedikt Bünz
   */
  private class SuffixSaveFilter extends FileFilter {

    private final String suffix;

    /**
     * @param suffix This file filter accepts all files that end with .suffix or the capitalized suffix.
     */
    public SuffixSaveFilter(String suffix) {

      this.suffix = suffix;
    }

    @Override
    public boolean accept(File f) {

      if (f.isDirectory()) {
        return true;
      }

      String s = f.getName();

      return s.endsWith("." + suffix) || s.endsWith("." + suffix.toUpperCase());
    }

    @Override
    public String getDescription() {

      return "*." + suffix + ",*." + suffix.toUpperCase();
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
    JMenuItem exportAsMenuItem;

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

      if (chart instanceof XYChart) {
        exportAsMenuItem = new JMenuItem(exportAsString);
        exportAsMenuItem.addMouseListener(new MouseListener() {

          @Override
          public void mouseReleased(MouseEvent e) {

            showExportAsDialog();
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
        add(exportAsMenuItem);
      }
    }
  }

}
