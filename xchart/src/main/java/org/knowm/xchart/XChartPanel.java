package org.knowm.xchart;

import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.VectorGraphicsEncoder.VectorGraphicsFormat;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.chartpart.ToolTips;
import org.knowm.xchart.style.AxesChartStyler;
import org.knowm.xchart.style.Styler;

/**
 * A Swing JPanel that contains a Chart
 *
 * <p>Right-click + Save As... or ctrl+S pops up a Save As dialog box for saving the chart as PNG,
 * JPEG, etc. file.
 *
 * @author timmolter
 */
public class XChartPanel<T extends Chart> extends JPanel {

  private final T chart;
  private final Dimension preferredSize;
  private String saveAsString = "Save As...";
  private String exportAsString = "Export To...";
  private String printString = "Print...";

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

    // Mouse motion listener for data label popup
    ToolTips toolTips = chart.getToolTips();
    if (toolTips != null) {
      MouseMotionListener mml = toolTips.getMouseMotionListener();
      if (mml != null) {
        this.addMouseMotionListener(mml);
      }
    }

    // Control+S key listener for saving chart
    KeyStroke ctrlS =
        KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
    this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(ctrlS, "save");
    this.getActionMap().put("save", new SaveAction());

    // Control+E key listener for saving chart
    KeyStroke ctrlE =
        KeyStroke.getKeyStroke(KeyEvent.VK_E, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
    this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(ctrlE, "export");
    this.getActionMap().put("export", new ExportAction());

    // Control+P key listener for printing chart
    KeyStroke ctrlP =
        KeyStroke.getKeyStroke(KeyEvent.VK_P, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
    this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(ctrlP, "print");
    this.getActionMap().put("print", new PrintAction());
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

  /**
   * Set the "Print..." String if you want to localize it.
   *
   * @param printString
   */
  public void setPrintString(String printString) {

    this.printString = printString;
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

  private void showPrintDialog() {
    PrinterJob printJob = PrinterJob.getPrinterJob();
    if (printJob.printDialog()) {
      try {
        // Page format
        PageFormat pageFormat = printJob.defaultPage();
        Paper paper = pageFormat.getPaper();
        JFrame w = (JFrame) SwingUtilities.windowForComponent(this);
        if (w.getWidth() > w.getHeight()) {
          pageFormat.setOrientation(PageFormat.LANDSCAPE);
          paper.setImageableArea(0, 0, pageFormat.getHeight(), pageFormat.getWidth());
        } else {
          paper.setImageableArea(0, 0, pageFormat.getWidth(), pageFormat.getHeight());
        }
        pageFormat.setPaper(paper);
        pageFormat = printJob.validatePage(pageFormat);

        String jobName = "XChart " + chart.getTitle().trim();
        if (!w.getTitle().trim().isEmpty()
            && !w.getTitle().trim().contentEquals(chart.getTitle().trim())) {
          jobName = jobName + " " + w.getTitle().trim();
        }
        printJob.setJobName(jobName);

        printJob.setPrintable(
            new Printer(getParent() /*start with parent to include the caption*/), pageFormat);

        Dimension windowSize = w.getSize();
        Styler styler = getChart().getStyler();
        Color chartBackgroundColor = styler.getChartBackgroundColor();
        Color plotBackgroundColor = styler.getPlotBackgroundColor();
        Color legendBackgroundColor = styler.getLegendBackgroundColor();
        Color toolTipBackgroundColor = styler.getToolTipBackgroundColor();
        Color titleBackgroundColor = styler.getChartTitleBoxBackgroundColor();
        AxesChartStyler cs = (AxesChartStyler) chart.getStyler();
        int markerSize = cs.getMarkerSize();
        try {
          // for printing: white backgrounds
          styler.setLegendBackgroundColor(Color.white);
          styler.setToolTipBackgroundColor(Color.white);
          styler.setChartTitleBoxBackgroundColor(Color.white);
          styler.setChartBackgroundColor(Color.white);
          styler.setPlotBackgroundColor(Color.white);

          // optional for printing: higher resolution, larger markers
          // cs.setMarkerSize(Math.max(markerSize, 5));
          // double widthPx = (int) Math.floor(pageFormat.getImageableWidth()/72*400);
          // double heightPx = (int) Math.floor(pageFormat.getImageableHeight()/72*400);
          // w.setSize((int) Math.floor(widthPx), (int) Math.floor(heightPx));
          // w.validate();

          printJob.print();
        } finally {
          // restore after printing
          styler.setPlotBackgroundColor(plotBackgroundColor);
          styler.setChartBackgroundColor(chartBackgroundColor);
          styler.setChartTitleBoxBackgroundColor(titleBackgroundColor);
          styler.setToolTipBackgroundColor(toolTipBackgroundColor);
          styler.setLegendBackgroundColor(legendBackgroundColor);
          cs.setMarkerSize(markerSize);
          w.setSize(windowSize);
        }
      } catch (PrinterException e) {
        e.printStackTrace();
      }
    }
  }

  private void showSaveAsDialog() {

    UIManager.put("FileChooser.saveButtonText", "Save");
    UIManager.put("FileChooser.fileNameLabelText", "File Name:");
    JFileChooser fileChooser = new JFileChooser();
    FileFilter pngFileFilter = new SuffixSaveFilter("png"); // default
    fileChooser.addChoosableFileFilter(pngFileFilter);
    fileChooser.addChoosableFileFilter(new SuffixSaveFilter("jpg"));
    fileChooser.addChoosableFileFilter(new SuffixSaveFilter("bmp"));
    fileChooser.addChoosableFileFilter(new SuffixSaveFilter("gif"));

    // VectorGraphics2D is optional, so if it's on the classpath, allow saving charts as vector
    // graphic
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
            BitmapEncoder.saveBitmap(chart, theFileToSave.getCanonicalPath(), BitmapFormat.PNG);
          } else if (fileChooser.getFileFilter().getDescription().equals("*.jpg,*.JPG")) {
            BitmapEncoder.saveJPGWithQuality(
                chart,
                BitmapEncoder.addFileExtension(theFileToSave.getCanonicalPath(), BitmapFormat.JPG),
                1.0f);
          } else if (fileChooser.getFileFilter().getDescription().equals("*.png,*.PNG")) {
            BitmapEncoder.saveBitmap(chart, theFileToSave.getCanonicalPath(), BitmapFormat.PNG);
          } else if (fileChooser.getFileFilter().getDescription().equals("*.bmp,*.BMP")) {
            BitmapEncoder.saveBitmap(chart, theFileToSave.getCanonicalPath(), BitmapFormat.BMP);
          } else if (fileChooser.getFileFilter().getDescription().equals("*.gif,*.GIF")) {
            BitmapEncoder.saveBitmap(chart, theFileToSave.getCanonicalPath(), BitmapFormat.GIF);
          } else if (fileChooser.getFileFilter().getDescription().equals("*.svg,*.SVG")) {
            VectorGraphicsEncoder.saveVectorGraphic(
                chart, theFileToSave.getCanonicalPath(), VectorGraphicsFormat.SVG);
          } else if (fileChooser.getFileFilter().getDescription().equals("*.eps,*.EPS")) {
            VectorGraphicsEncoder.saveVectorGraphic(
                chart, theFileToSave.getCanonicalPath(), VectorGraphicsFormat.EPS);
          } else if (fileChooser.getFileFilter().getDescription().equals("*.pdf,*.PDF")) {
            VectorGraphicsEncoder.saveVectorGraphic(
                chart, theFileToSave.getCanonicalPath(), VectorGraphicsFormat.PDF);
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private void showExportAsDialog() {

    UIManager.put("FileChooser.saveButtonText", "Export");
    UIManager.put("FileChooser.fileNameLabelText", "Export To:");
    UIManager.put("FileChooser.saveDialogFileNameLabel.textAndMnemonic", "Export To:");
    // UIDefaults defaults = UIManager.getDefaults();
    // System.out.println(defaults.size()+ " properties");
    // for (Enumeration e = defaults.keys();
    // e.hasMoreElements();) {
    // Object key = e.nextElement();
    // System.out.println(key + " = " + defaults.get(key));
    // }
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    fileChooser.setFileFilter(
        new FileFilter() {

          @Override
          public boolean accept(File f) {

            return f.isDirectory();
          }

          @Override
          public String getDescription() {

            return "Any Directory";
          }
        });
    fileChooser.setAcceptAllFileFilterUsed(false);
    fileChooser.setDialogTitle("Export");

    if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {

      File theFileToSave = fileChooser.getSelectedFile();
      try {
        CSVExporter.writeCSVColumns(
            (XYChart) chart, theFileToSave.getCanonicalPath() + File.separatorChar);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
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

  private class PrintAction extends AbstractAction {

    public PrintAction() {

      super("print");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

      showPrintDialog();
    }
  }

  /**
   * File filter based on the suffix of a file. This file filter accepts all files that end with
   * .suffix or the capitalized suffix.
   *
   * @author Benedikt Bünz
   */
  private class SuffixSaveFilter extends FileFilter {

    private final String suffix;

    /**
     * @param suffix This file filter accepts all files that end with .suffix or the capitalized
     *     suffix.
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
      menu.getGraphics().dispose();
    }
  }

  private class XChartPanelPopupMenu extends JPopupMenu {

    final JMenuItem saveAsMenuItem;
    final JMenuItem printMenuItem;
    JMenuItem exportAsMenuItem;

    public XChartPanelPopupMenu() {

      saveAsMenuItem = new JMenuItem(saveAsString);
      saveAsMenuItem.addMouseListener(
          new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {

              showSaveAsDialog();
            }

            @Override
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseClicked(MouseEvent e) {}
          });
      add(saveAsMenuItem);

      printMenuItem = new JMenuItem(printString);
      printMenuItem.addMouseListener(
          new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {

              showPrintDialog();
            }

            @Override
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseClicked(MouseEvent e) {}
          });
      add(printMenuItem);

      if (chart instanceof XYChart) {
        exportAsMenuItem = new JMenuItem(exportAsString);
        exportAsMenuItem.addMouseListener(
            new MouseListener() {

              @Override
              public void mouseReleased(MouseEvent e) {

                showExportAsDialog();
              }

              @Override
              public void mousePressed(MouseEvent e) {}

              @Override
              public void mouseExited(MouseEvent e) {}

              @Override
              public void mouseEntered(MouseEvent e) {}

              @Override
              public void mouseClicked(MouseEvent e) {}
            });
        add(exportAsMenuItem);
      }
    }
  }

  public static class Printer implements Printable {
    private Component component;

    Printer(Component c) {
      component = c;
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
      if (pageIndex > 0) {
        return NO_SUCH_PAGE;
      }

      Graphics2D g2 = (Graphics2D) graphics;
      g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
      double sx = pageFormat.getImageableWidth() / component.getWidth();
      double sy = pageFormat.getImageableHeight() / component.getHeight();
      g2.scale(sx, sy);

      component.printAll(g2);

      return PAGE_EXISTS;
    }
  }
}
