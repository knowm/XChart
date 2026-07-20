package org.knowm.xchart.demo;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.style.Styler;

public class DemoChartsUtil {

  private static final String DEMO_CHARTS_PACKAGE = "org.knowm.xchart.demo.charts";

  public static List<ExampleChart<Chart<Styler, Series>>> getAllDemoCharts() {

    String packagePath = DEMO_CHARTS_PACKAGE.replace(".", "/");
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    URL url = loader.getResource(packagePath);

    if (url == null) {
      throw new IllegalStateException(
          "Could not find the demo charts package on the classpath: " + DEMO_CHARTS_PACKAGE);
    }

    List<ExampleChart<Chart<Styler, Series>>> demoCharts;
    try {
      demoCharts = getAllDemoCharts(url);
    } catch (Exception e) {
      throw new IllegalStateException("Could not load the demo charts from: " + url, e);
    }

    if (demoCharts.isEmpty()) {
      throw new IllegalStateException("No demo charts were found at: " + url);
    }

    return demoCharts;
  }

  @SuppressWarnings("unchecked")
  private static List<ExampleChart<Chart<Styler, Series>>> getAllDemoCharts(URL url)
      throws Exception {

    List<ExampleChart<Chart<Styler, Series>>> demoCharts = new ArrayList<>();

    List<Class<?>> classes = getAllAssignedClasses(url);
    // sort
    Collections.sort(
        classes,
        new Comparator<Class<?>>() {

          @Override
          public int compare(Class<?> c1, Class<?> c2) {
            return c1.getName().compareTo(c2.getName());
          }
        });

    for (Class<?> c : classes) {
      demoCharts.add(((ExampleChart<Chart<Styler, Series>>) c.getDeclaredConstructor().newInstance()));
    }
    return demoCharts;
  }

  private static List<Class<?>> getAllAssignedClasses(URL url) throws Exception {

    List<Class<?>> classes = null;

    // Note: go through URI rather than URL.getFile(), which hands back a percent-encoded path and
    // so resolves to a non-existent directory whenever the project path contains a space.
    String type = url.getProtocol();
    if ("file".equals(type)) {
      classes = getClassesByFile(new File(url.toURI()), DEMO_CHARTS_PACKAGE);
    } else if ("jar".equals(type)) {
      classes = getClassesByJar(url);
    }
    List<Class<?>> allAssignedClasses = new ArrayList<>();
    if (classes != null) {
      for (Class<?> c : classes) {
        if (ExampleChart.class.isAssignableFrom(c) && !ExampleChart.class.equals(c)) {
          allAssignedClasses.add(c);
        }
      }
    }
    return allAssignedClasses;
  }

  private static List<Class<?>> getClassesByFile(File dir, String pk)
      throws ClassNotFoundException {

    List<Class<?>> classes = new ArrayList<>();
    if (!dir.exists()) {
      return classes;
    }

    File[] files = dir.listFiles();
    if (files == null) {
      return classes;
    }

    String fileName = "";
    for (File f : files) {
      fileName = f.getName();
      if (f.isDirectory()) {
        classes.addAll(getClassesByFile(f, pk + "." + fileName));
      } else if (fileName.endsWith(".class")) {
        classes.add(
            Class.forName(pk + "." + fileName.substring(0, fileName.length() - ".class".length())));
      }
    }

    return classes;
  }

  private static List<Class<?>> getClassesByJar(URL url) throws Exception {

    List<Class<?>> classes = new ArrayList<>();
    String[] jarInfo = url.getPath().split("!");
    // the jar part is itself a URL, so let File decode it rather than chopping off the scheme by
    // hand - that keeps paths containing spaces (and Windows drive letters) intact
    File jarFilePath = new File(new URL(jarInfo[0]).toURI());
    String packagePath = jarInfo[1].substring(1);
    try (JarFile jarFile = new JarFile(jarFilePath)) {
      Enumeration<JarEntry> entrys = jarFile.entries();
      JarEntry jarEntry = null;
      String entryName = "";
      String className = "";
      while (entrys.hasMoreElements()) {
        jarEntry = entrys.nextElement();
        entryName = jarEntry.getName();
        if (entryName.endsWith(".class") && entryName.startsWith(packagePath)) {
          className = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
          classes.add(Class.forName(className));
        }
      }
    }
    return classes;
  }
}
