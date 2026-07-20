package org.knowm.xchart;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.VectorGraphicsEncoder.VectorGraphicsFormat;
import org.knowm.xchart.internal.chartpart.AxesChart;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.chartpart.ChartZoom;
import org.knowm.xchart.internal.chartpart.Cursor;
import org.knowm.xchart.internal.chartpart.DataPointDispatcher;
import org.knowm.xchart.internal.chartpart.ToolTips;
import org.knowm.xchart.style.Styler;

/**
 * A Swing JPanel that contains a Chart
 *
 * <p>Right-click + Save As... or ctrl+S pops up a Save As dialog box for saving the chart as PNG,
 * JPEG, etc. file.
 */
public class XChartPanel<T extends Chart<?, ?>> extends JPanel {

  private final T chart;
  private final Dimension preferredSize;
  private String saveAsString = "Save As...";
  private String exportAsString = "Export To...";
  private String printString = "Print...";
  private String resetString = "Reset Zoom";
  private ToolTips toolTips = null;
  private Cursor cursor = null;
  private ChartZoom chartZoom = null;
  private DataPointDispatcher dataPointDispatcher = null;
  private final List<DataPointListener> dataPointListeners = new ArrayList<>();
  private boolean toolTipsEnabled = false;
  private boolean zoomEnabled = false;
  private java.awt.Color zoomSelectionColor = new java.awt.Color(0, 0, 0, 40);
  private boolean zoomResetByDoubleClick = true;
  private boolean zoomResetByButton = true;
  private boolean cursorEnabled = false;
  private ChartButtonConfig chartButtonConfig;

  /**
   * Constructor
   *
   * @param chart
   */
  public XChartPanel(final T chart) {

    this.chart = chart;
    preferredSize = new Dimension(chart.getWidth(), chart.getHeight());

    Styler styler = chart.getStyler();
    chartButtonConfig =
        new ChartButtonConfig()
            .setFontColor(styler.getChartFontColor())
            .setFont(styler.getBaseFont().deriveFont(11f));

    // Right-click listener for saving chart
    this.addMouseListener(new PopUpMenuClickListener());

    // Control+S key listener for saving chart
    KeyStroke ctrlS =
        KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx());
    this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(ctrlS, "save");
    this.getActionMap().put("save", new SaveAction());

    // Control+E key listener for saving chart
    KeyStroke ctrlE =
        KeyStroke.getKeyStroke(KeyEvent.VK_E, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx());
    this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(ctrlE, "export");
    this.getActionMap().put("export", new ExportAction());

    // Control+P key listener for printing chart
    KeyStroke ctrlP =
        KeyStroke.getKeyStroke(KeyEvent.VK_P, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx());
    this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(ctrlP, "print");
    this.getActionMap().put("print", new PrintAction());

    rewireInteractions();

    // Recalculate interactions at component resize
    this.addComponentListener(
        new ComponentAdapter() {
          public void componentResized(ComponentEvent ev) {
            rewireInteractions();
          }
        });
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

  /**
   * Set the "Reset" String if you want to localize it. This is on the button which resets the zoom
   * feature.
   *
   * @param resetString
   */
  public void setResetString(String resetString) {

    this.resetString = resetString;
  }

  public XChartPanel<T> setToolTipsEnabled(boolean enabled) {

    this.toolTipsEnabled = enabled;
    rewireInteractions();
    return this;
  }

  public XChartPanel<T> setZoomEnabled(boolean enabled) {

    this.zoomEnabled = enabled;
    rewireInteractions();
    return this;
  }

  public XChartPanel<T> setZoomSelectionColor(java.awt.Color color) {

    this.zoomSelectionColor = color;
    rewireInteractions();
    return this;
  }

  public XChartPanel<T> setZoomResetByDoubleClick(boolean reset) {

    this.zoomResetByDoubleClick = reset;
    rewireInteractions();
    return this;
  }

  public XChartPanel<T> setZoomResetByButton(boolean reset) {

    this.zoomResetByButton = reset;
    rewireInteractions();
    return this;
  }

  public XChartPanel<T> setCursorEnabled(boolean enabled) {

    this.cursorEnabled = enabled;
    rewireInteractions();
    return this;
  }

  /**
   * Registers a listener that is notified when the mouse hovers over, leaves, or clicks an
   * individual rendered data point (bar, marker, bubble, etc.). Collision detection reuses the
   * per-data-point hit shapes that drive the hover-tooltip feature, so it works for every chart type
   * and does not require tooltips to be enabled.
   *
   * @param listener the listener to add (must not be null)
   */
  public XChartPanel<T> addDataPointListener(DataPointListener listener) {

    Objects.requireNonNull(listener, "listener must not be null");
    dataPointListeners.add(listener);
    rewireInteractions();
    return this;
  }

  /**
   * Removes a previously registered {@link DataPointListener}.
   *
   * @param listener the listener to remove
   */
  public XChartPanel<T> removeDataPointListener(DataPointListener listener) {

    dataPointListeners.remove(listener);
    rewireInteractions();
    return this;
  }

  public java.awt.Color getZoomSelectionColor() {

    return zoomSelectionColor;
  }

  public boolean isZoomResetByButton() {

    return zoomResetByButton;
  }

  public boolean isZoomResetByDoubleClick() {

    return zoomResetByDoubleClick;
  }

  public ChartButtonConfig getChartButtonConfig() {

    return chartButtonConfig;
  }

  /**
   * Replaces the default {@link ChartButtonConfig} for the zoom-reset button. If zoom has already
   * been enabled, interactions are rewired so the new config takes effect immediately.
   *
   * @param chartButtonConfig the new config (must not be null)
   */
  public XChartPanel<T> setChartButtonConfig(ChartButtonConfig chartButtonConfig) {

    Objects.requireNonNull(chartButtonConfig, "chartButtonConfig must not be null");
    this.chartButtonConfig = chartButtonConfig;
    rewireInteractions();
    return this;
  }

  private void rewireInteractions() {

    if (toolTips != null) {
      this.removeMouseMotionListener(toolTips);
      toolTips = null;
    }
    if (cursor != null) {
      this.removeMouseMotionListener(cursor);
      cursor = null;
    }
    if (dataPointDispatcher != null) {
      this.removeMouseMotionListener(dataPointDispatcher);
      this.removeMouseListener(dataPointDispatcher);
      dataPointDispatcher = null;
    }
    if (chartZoom != null) {
      this.removeMouseListener(chartZoom);
      this.removeMouseMotionListener(chartZoom);
      chartZoom = null;
      this.getInputMap(WHEN_IN_FOCUSED_WINDOW).remove(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
      this.getActionMap().remove("resetZoom");
    }

    boolean anyEnabled = false;

    if (zoomEnabled && (chart instanceof XYChart || chart instanceof OHLCChart)) {
      anyEnabled = true;
      @SuppressWarnings("unchecked")
      ChartZoom zoom =
          new ChartZoom((AxesChart<?, ?>) chart, this, resetString);
      this.chartZoom = zoom;
      this.addMouseListener(zoom);
      this.addMouseMotionListener(zoom);
      KeyStroke escape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
      this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(escape, "resetZoom");
      this.getActionMap()
          .put(
              "resetZoom",
              new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                  chartZoom.resetZoom();
                }
              });
    }

    if (cursorEnabled && chart instanceof XYChart) {
      anyEnabled = true;
      cursor = new Cursor(chart);
      this.addMouseMotionListener(cursor);
    }

    if (toolTipsEnabled) {
      anyEnabled = true;
      toolTips = new ToolTips(chart, false, chart.getStyler().getToolTipType());
      this.addMouseMotionListener(toolTips);
    }

    if (!dataPointListeners.isEmpty()) {
      anyEnabled = true;
      dataPointDispatcher = new DataPointDispatcher(dataPointListeners);
      this.addMouseMotionListener(dataPointDispatcher);
      this.addMouseListener(dataPointDispatcher);
    }

    if (toolTipsEnabled || cursorEnabled || !dataPointListeners.isEmpty()) {
      chart.enableInteractionData();
    }

    if (anyEnabled) {
      repaint();
    }
  }

  @Override
  protected void paintComponent(Graphics g) {

    super.paintComponent(g);

    Graphics2D g2d = (Graphics2D) g.create();
    chart.paint(g2d, getWidth(), getHeight());

    chart.consumeInteractionData(g2d, toolTips, cursor, dataPointDispatcher);
    if (chartZoom != null) {
      chartZoom.paint(g2d);
    }

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
        if (this.getWidth() > this.getHeight()) {
          pageFormat.setOrientation(PageFormat.LANDSCAPE);
          paper.setImageableArea(0, 0, pageFormat.getHeight(), pageFormat.getWidth());
        } else {
          paper.setImageableArea(0, 0, pageFormat.getWidth(), pageFormat.getHeight());
        }
        pageFormat.setPaper(paper);
        pageFormat = printJob.validatePage(pageFormat);

        String jobName = "XChart " + chart.getTitle().trim();
        printJob.setJobName(jobName);

        printJob.setPrintable(new Printer(this), pageFormat);
        printJob.print();
      } catch (PrinterException e) {
        e.printStackTrace();
      }
    }
  }

  private void showSaveAsDialog() {

    UIManager.put("FileChooser.saveButtonText", "Save");
    UIManager.put("FileChooser.fileNameLabelText", "File Name:");
    JFileChooser fileChooser = new OverwriteConfirmingFileChooser();
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
    } catch (ClassNotFoundException e) {
      // it does not exist on the classpath
    }
    try {
      Class.forName("de.rototor.pdfbox.graphics2d.PdfBoxGraphics2D");
      // it exists on the classpath
      fileChooser.addChoosableFileFilter(new SuffixSaveFilter("pdf"));
    } catch (ClassNotFoundException e) {
      // it does not exist on the classpath
    }

    fileChooser.setAcceptAllFileFilterUsed(false);

    fileChooser.setFileFilter(pngFileFilter);

    if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {

      if (fileChooser.getSelectedFile() != null) {
        try {
          // The extension is already applied here, matching the path the overwrite check used.
          String path =
              resolveSaveTarget(fileChooser.getSelectedFile(), fileChooser.getFileFilter())
                  .getCanonicalPath();
          switch (suffixOf(fileChooser.getFileFilter())) {
            case "jpg":
              BitmapEncoder.saveJPGWithQuality(chart, path, 1.0f);
              break;
            case "bmp":
              BitmapEncoder.saveBitmap(chart, path, BitmapFormat.BMP);
              break;
            case "gif":
              BitmapEncoder.saveBitmap(chart, path, BitmapFormat.GIF);
              break;
            case "svg":
              VectorGraphicsEncoder.saveVectorGraphic(chart, path, VectorGraphicsFormat.SVG);
              break;
            case "eps":
              VectorGraphicsEncoder.saveVectorGraphic(chart, path, VectorGraphicsFormat.EPS);
              break;
            case "pdf":
              VectorGraphicsEncoder.saveVectorGraphic(chart, path, VectorGraphicsFormat.PDF);
              break;
            case "png":
            default:
              BitmapEncoder.saveBitmap(chart, path, BitmapFormat.PNG);
              break;
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
    UIManager.put("FileChooser.fileNameLabelMnemonic", "Export To:");
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
    disableLabel(fileChooser.getComponents());
    disableTextField(fileChooser.getComponents());
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

    if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {

      File theFileToSave = null;
      if (fileChooser.getSelectedFile() != null) {
        if (fileChooser.getSelectedFile().exists()) {
          theFileToSave = fileChooser.getSelectedFile();
        } else {
          theFileToSave = new File(fileChooser.getSelectedFile().getParent());
        }
      }

      try {
        CSVExporter.writeCSVColumns(
            (XYChart) chart, theFileToSave.getCanonicalPath() + File.separatorChar);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private void disableTextField(Component[] comp) {
    for (Component component : comp) {
      //            System.out.println(component.toString());
      if (component instanceof JPanel) {
        disableTextField(((JPanel) component).getComponents());
      } else if (component instanceof JTextField) {
        component.setVisible(false);
        return;
      }
    }
  }

  private void disableLabel(Component[] comp) {
    for (Component component : comp) {
      //      System.out.println(comp[x].toString());
      if (component instanceof JPanel) {
        disableLabel(((JPanel) component).getComponents());
      } else if (component instanceof JLabel) {
        //        System.out.println(comp[x].toString());
        component.setVisible(false);
        return;
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
   */
  /**
   * Returns the file extension implied by the given save-dialog file filter, without the leading
   * dot. Falls back to {@code "png"} for a null or unrecognized filter, matching both the dialog's
   * default filter and the encoder used when no filter is set.
   */
  static String suffixOf(FileFilter fileFilter) {

    if (fileFilter instanceof SuffixSaveFilter) {
      return ((SuffixSaveFilter) fileFilter).getSuffix().toLowerCase(Locale.ROOT);
    }
    return "png";
  }

  /**
   * Resolves the file that the Save As dialog will actually write, applying the extension implied
   * by the selected filter exactly as the encoder would.
   *
   * <p>The selected file is frequently extension-less, since the user just types "chart" and picks
   * a format from the filter drop-down. An overwrite check therefore has to be made against this
   * resolved path rather than against the raw selection, or the most common case would still
   * clobber silently. The extension is applied by the very same {@code addFileExtension} method the
   * matching encoder calls, so the checked path and the written path cannot drift apart.
   */
  static File resolveSaveTarget(File selectedFile, FileFilter fileFilter) {

    String path = selectedFile.getPath();
    switch (suffixOf(fileFilter)) {
      case "jpg":
        return new File(BitmapEncoder.addFileExtension(path, BitmapFormat.JPG));
      case "bmp":
        return new File(BitmapEncoder.addFileExtension(path, BitmapFormat.BMP));
      case "gif":
        return new File(BitmapEncoder.addFileExtension(path, BitmapFormat.GIF));
      case "svg":
        return new File(VectorGraphicsEncoder.addFileExtension(path, VectorGraphicsFormat.SVG));
      case "eps":
        return new File(VectorGraphicsEncoder.addFileExtension(path, VectorGraphicsFormat.EPS));
      case "pdf":
        return new File(VectorGraphicsEncoder.addFileExtension(path, VectorGraphicsFormat.PDF));
      case "png":
      default:
        return new File(BitmapEncoder.addFileExtension(path, BitmapFormat.PNG));
    }
  }

  /**
   * A JFileChooser that asks before replacing an existing file. Swing has no built-in overwrite
   * confirmation, so overriding {@code approveSelection} is the standard way to add one; declining
   * leaves the dialog open so the user can pick a different name.
   */
  private static class OverwriteConfirmingFileChooser extends JFileChooser {

    @Override
    public void approveSelection() {

      File selectedFile = getSelectedFile();
      if (getDialogType() == SAVE_DIALOG && selectedFile != null) {
        File target = resolveSaveTarget(selectedFile, getFileFilter());
        if (target.exists()) {
          int answer =
              JOptionPane.showConfirmDialog(
                  this,
                  target.getName() + " already exists.\nDo you want to replace it?",
                  "Confirm Save As",
                  JOptionPane.YES_NO_OPTION,
                  JOptionPane.WARNING_MESSAGE);
          if (answer != JOptionPane.YES_OPTION) {
            return;
          }
        }
      }
      super.approveSelection();
    }
  }

  private static class SuffixSaveFilter extends FileFilter {

    private final String suffix;

    /**
     * @param suffix This file filter accepts all files that end with .suffix or the capitalized
     *     suffix.
     */
    public SuffixSaveFilter(String suffix) {

      this.suffix = suffix;
    }

    public String getSuffix() {

      return suffix;
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

      if (e.isPopupTrigger() && !isOverListenedDataPoint(e)) {
        doPop(e);
      }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

      if (e.isPopupTrigger() && !isOverListenedDataPoint(e)) {
        doPop(e);
      }
    }

    /**
     * When a DataPointListener is registered and the right-click lands on a data point, the default
     * Save As / Print menu is suppressed so the listener can show its own context menu instead.
     * Right-clicks elsewhere still show the default menu.
     */
    private boolean isOverListenedDataPoint(MouseEvent e) {

      return dataPointDispatcher != null
          && !dataPointListeners.isEmpty()
          && dataPointDispatcher.isOverDataPoint(e.getX(), e.getY());
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
    private final Component component;

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
