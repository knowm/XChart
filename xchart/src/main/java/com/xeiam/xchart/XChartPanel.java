/**
 * Copyright 2011 - 2015 Xeiam LLC.
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
import java.util.Collection;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;

import com.xeiam.xchart.BitmapEncoder.BitmapFormat;
import com.xeiam.xchart.VectorGraphicsEncoder.VectorGraphicsFormat;

/**
 * A Swing JPanel that contains a Chart
 * <p>
 * Right-click + Save As... or ctrl+S pops up a Save As dialog box for saving
 * the chart as a JPeg or PNG file.
 *
 * @author timmolter
 */
public class XChartPanel extends JPanel {

    private final Chart chart;
    private final Dimension preferredSize;
    private String saveAsString = "Save As...";

    /**
     * Constructor
     *
     * @param chart
     */
    public XChartPanel(final Chart chart) {

        this.chart = chart;
        preferredSize = new Dimension(chart.getWidth(), chart.getHeight());

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

        Graphics2D g2d = (Graphics2D) g.create();
        chart.paint(g2d, getWidth(), getHeight());
        g2d.dispose();
    }

    @Override
    public Dimension getPreferredSize() {

        return this.preferredSize;
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
        fileChooser.addChoosableFileFilter(new SuffixSaveFilter("jpg"));
        FileFilter pngFileFilter = new SuffixSaveFilter("png");
        fileChooser.addChoosableFileFilter(pngFileFilter);
        fileChooser.addChoosableFileFilter(new SuffixSaveFilter("bmp"));
        fileChooser.addChoosableFileFilter(new SuffixSaveFilter("gif"));

        // VectorGraphics2D is optional, so if it's on the classpath, allow
        // saving charts as vector graphic
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
                    } else if (fileChooser.getFileFilter().getDescription().equals("*.jpg,*.JPG")) {
                        BitmapEncoder.saveJPGWithQuality(chart, BitmapEncoder.addFileExtension(theFileToSave.getCanonicalPath().toString(), BitmapFormat.JPG), 1.0f);
                    } else if (fileChooser.getFileFilter().getDescription().equals("*.png,*.PNG")) {
                        BitmapEncoder.saveBitmap(chart, theFileToSave.getCanonicalPath().toString(), BitmapFormat.PNG);
                    } else if (fileChooser.getFileFilter().getDescription().equals("*.bmp,*.BMP")) {
                        BitmapEncoder.saveBitmap(chart, theFileToSave.getCanonicalPath().toString(), BitmapFormat.BMP);
                    } else if (fileChooser.getFileFilter().getDescription().equals("*.gif,*.GIF")) {
                        BitmapEncoder.saveBitmap(chart, theFileToSave.getCanonicalPath().toString(), BitmapFormat.GIF);
                    } else if (fileChooser.getFileFilter().getDescription().equals("*.svg,*.SVG")) {
                        VectorGraphicsEncoder.saveVectorGraphic(chart, theFileToSave.getCanonicalPath().toString(), VectorGraphicsFormat.SVG);
                    } else if (fileChooser.getFileFilter().getDescription().equals("*.eps,*.EPS")) {
                        VectorGraphicsEncoder.saveVectorGraphic(chart, theFileToSave.getCanonicalPath().toString(), VectorGraphicsFormat.EPS);
                    } else if (fileChooser.getFileFilter().getDescription().equals("*.pdf,*.PDF")) {
                        VectorGraphicsEncoder.saveVectorGraphic(chart, theFileToSave.getCanonicalPath().toString(), VectorGraphicsFormat.PDF);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * File filter based on the suffix of a file. This file filter accepts all
     * files that end with .suffix or the capitalized suffix.
     *
     * @author Benedikt Bünz
     */
    private class SuffixSaveFilter extends FileFilter {

        private final String suffix;

        /**
         * @param suffix
         *            This file filter accepts all files that end with .suffix
         *            or the capitalized suffix.
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
     * update a series by only updating the Y-Axis data. The X-Axis data will be
     * automatically generated as a list of increasing Integers starting from 1
     * and ending at the size of the new Y-Axis data list.
     *
     * @param seriesName
     * @param newYData
     * @return
     */
    public Series updateSeries(String seriesName, Collection<? extends Number> newYData) {

        Series series = chart.getSeriesMap().get(seriesName);
        if (series == null) {
            throw new IllegalArgumentException("Series name >" + seriesName + "< not found!!!");
        }
        series.replaceYData(newYData);

        // generate X-Data
        List<Integer> generatedXData = new ArrayList<Integer>();
        for (int i = 1; i < newYData.size() + 1; i++) {
            generatedXData.add(i);
        }
        series.replaceXData(generatedXData);

        // Re-display the chart
        revalidate();
        repaint();
        return series;

    }

    /**
     * update a series by updating both the X-Axis and Y-Axis data
     *
     * @param seriesName
     * @param newYData
     * @return
     */
    public Series updateSeries(String seriesName, Collection<?> newXData, List<? extends Number> newYData) {

        Series series = chart.getSeriesMap().get(seriesName);
        if (series == null) {
            throw new IllegalArgumentException("Series name >" + seriesName + "< not found!!!");
        }
        series.replaceData(newXData, newYData);

        // Re-display the chart
        revalidate();
        repaint();

        return series;
    }

    /**
     * update a series by updating the X-Axis, Y-Axis and error bar data
     *
     * @param seriesName
     * @param newXData
     * @param newYData
     * @param newErrorBarData
     * @return
     */
    public Series updateSeries(String seriesName, Collection<?> newXData, List<? extends Number> newYData, List<? extends Number> newErrorBarData) {

        Series series = chart.getSeriesMap().get(seriesName);
        if (series == null) {
            throw new IllegalArgumentException("Series name >" + seriesName + "< not found!!!");
        }
        series.replaceData(newXData, newYData, newErrorBarData);

        // Re-display the chart
        revalidate();
        repaint();

        return series;
    }
}