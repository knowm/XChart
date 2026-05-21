package org.knowm.xchart.site;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.demo.DemoChartsUtil;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.style.Styler;

/**
 * Generates the static XChart GitHub Pages website.
 *
 * <p>Run via Maven exec plugin during the package phase. Outputs to target/site-output/.
 */
public class GenerateSite {

  private static final String ISSUES_PACKAGE = "org.knowm.xchart.standalone.issues";
  private static final String PRS_PACKAGE = "org.knowm.xchart.standalone.prs";

  public static void main(String[] args) throws Exception {
    String outputDir = args.length > 0 ? args[0] : "target/site-output";
    new GenerateSite().generate(outputDir);
  }

  public void generate(String outputDirPath) throws Exception {
    Path outputDir = Paths.get(outputDirPath);
    Path chartsDir = outputDir.resolve("charts");
    Path cssDir = outputDir.resolve("css");
    Path jsDir = outputDir.resolve("js");

    Files.createDirectories(chartsDir);
    Files.createDirectories(cssDir);
    Files.createDirectories(jsDir);

    System.out.println("Collecting demo charts...");
    List<ExampleChart<Chart<Styler, Series>>> allCharts = DemoChartsUtil.getAllDemoCharts();
    if (allCharts == null || allCharts.isEmpty()) {
      throw new IllegalStateException("No demo charts found — check classpath.");
    }

    // Group by category (derived from the simple class name prefix, e.g. "LineChart01" -> "Line")
    Map<String, List<ChartEntry>> byCategory = new LinkedHashMap<>();
    for (ExampleChart<Chart<Styler, Series>> example : allCharts) {
      String className = example.getClass().getSimpleName();
      String category = extractCategory(className);
      byCategory
          .computeIfAbsent(category, k -> new ArrayList<>())
          .add(new ChartEntry(className, example));
    }

    // Sort categories alphabetically
    List<String> sortedCategories = new ArrayList<>(byCategory.keySet());
    sortedCategories.sort(Comparator.naturalOrder());
    Map<String, List<ChartEntry>> sorted = new LinkedHashMap<>();
    for (String cat : sortedCategories) {
      sorted.put(cat, byCategory.get(cat));
    }

    System.out.println(
        "Rendering "
            + allCharts.size()
            + " charts across "
            + sorted.size()
            + " categories...");
    renderChartImages(sorted, chartsDir);

    System.out.println("Collecting fixes/PR charts...");
    List<FixEntry> fixEntries = collectFixEntries(chartsDir);
    System.out.println("Found " + fixEntries.size() + " fix/PR chart entries.");

    System.out.println("Copying static assets...");
    copyResource("site/css/style.css", cssDir.resolve("style.css"));
    copyResource("site/js/main.js", jsDir.resolve("main.js"));

    System.out.println("Generating HTML...");
    String indexHtml = buildIndexHtml(sorted);
    Files.write(outputDir.resolve("index.html"), indexHtml.getBytes(StandardCharsets.UTF_8));

    String fixesHtml = buildFixesHtml(fixEntries);
    Files.write(outputDir.resolve("fixes.html"), fixesHtml.getBytes(StandardCharsets.UTF_8));

    System.out.println("Site generated at: " + outputDir.toAbsolutePath());
  }

  // ── Demo chart gallery ───────────────────────────────────────────────────────

  private void renderChartImages(Map<String, List<ChartEntry>> byCategory, Path chartsDir) {
    for (Map.Entry<String, List<ChartEntry>> entry : byCategory.entrySet()) {
      Path catDir = chartsDir.resolve(entry.getKey().toLowerCase());
      try {
        Files.createDirectories(catDir);
      } catch (IOException e) {
        System.err.println("ERROR: Could not create dir " + catDir + ": " + e.getMessage());
        continue;
      }
      for (ChartEntry ce : entry.getValue()) {
        try {
          Chart<?, ?> chart = ce.example.getChart();
          String pngPath = catDir.resolve(ce.className + ".png").toString();
          BitmapEncoder.saveBitmap(chart, pngPath, BitmapFormat.PNG);
        } catch (Exception e) {
          System.err.println("WARNING: Failed to render " + ce.className + ": " + e);
          e.printStackTrace();
        }
      }
    }
  }

  private String buildIndexHtml(Map<String, List<ChartEntry>> byCategory) {
    int totalCharts = byCategory.values().stream().mapToInt(List::size).sum();
    StringBuilder sb = new StringBuilder();
    sb.append("<!DOCTYPE html>\n")
        .append("<html lang=\"en\">\n")
        .append("<head>\n")
        .append("  <meta charset=\"UTF-8\">\n")
        .append("  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n")
        .append("  <title>XChart \u2014 Java Chart Library</title>\n")
        .append("  <link rel=\"stylesheet\" href=\"css/style.css\">\n")
        .append("</head>\n")
        .append("<body>\n")
        .append(buildHeader("index.html"))
        .append("  <div class=\"layout\">\n")
        .append("    <aside class=\"sidebar\">\n")
        .append("      <div class=\"sidebar-header\">\n")
        .append(
            "        <input type=\"text\" id=\"search\" placeholder=\"Search charts\u2026\" autocomplete=\"off\">\n")
        .append("      </div>\n")
        .append("      <nav class=\"category-nav\">\n")
        .append("        <a href=\"#all\" class=\"cat-link active\" data-category=\"all\">\n")
        .append("          All <span class=\"badge\">")
        .append(totalCharts)
        .append("</span>\n")
        .append("        </a>\n");

    for (Map.Entry<String, List<ChartEntry>> entry : byCategory.entrySet()) {
      String cat = entry.getKey();
      int count = entry.getValue().size();
      sb.append("        <a href=\"#")
          .append(cat.toLowerCase())
          .append("\" class=\"cat-link\" data-category=\"")
          .append(cat.toLowerCase())
          .append("\">")
          .append(cat)
          .append(" <span class=\"badge\">")
          .append(count)
          .append("</span></a>\n");
    }

    sb.append("      </nav>\n")
        .append("    </aside>\n\n")
        .append("    <main class=\"content\">\n")
        .append("      <div class=\"charts-grid\" id=\"charts-grid\">\n");

    for (Map.Entry<String, List<ChartEntry>> entry : byCategory.entrySet()) {
      String cat = entry.getKey();
      for (ChartEntry ce : entry.getValue()) {
        String chartName = ce.example.getExampleChartName();
        String imgPath = "charts/" + cat.toLowerCase() + "/" + ce.className + ".png";
        sb.append("        <div class=\"chart-card\" data-category=\"")
            .append(cat.toLowerCase())
            .append("\">\n")
            .append("          <div class=\"chart-img-wrap\">\n")
            .append("            <img src=\"")
            .append(imgPath)
            .append("\" alt=\"")
            .append(escapeHtml(chartName))
            .append("\" loading=\"lazy\">\n")
            .append("          </div>\n")
            .append("          <div class=\"chart-info\">\n")
            .append("            <span class=\"chart-category\">")
            .append(cat)
            .append("</span>\n")
            .append("            <p class=\"chart-name\">")
            .append(escapeHtml(chartName))
            .append("</p>\n")
            .append("          </div>\n")
            .append("        </div>\n");
      }
    }

    sb.append("        <div class=\"no-results\" id=\"no-results\" style=\"display:none;\">\n")
        .append("          No charts match your search.\n")
        .append("        </div>\n")
        .append("      </div>\n")
        .append("    </main>\n")
        .append("  </div>\n\n")
        .append(buildFooter())
        .append("  <script src=\"js/main.js\"></script>\n")
        .append("</body>\n")
        .append("</html>\n");

    return sb.toString();
  }

  // ── Fixes / PR gallery ───────────────────────────────────────────────────────

  /**
   * Scans the standalone.issues and standalone.prs packages, renders any class with a public
   * static getChart() method, and returns a list of FixEntry records sorted by type then number.
   */
  private List<FixEntry> collectFixEntries(Path chartsDir) throws Exception {
    Path fixesDir = chartsDir.resolve("fixes");
    Files.createDirectories(fixesDir);

    List<Class<?>> issueClasses = scanPackage(ISSUES_PACKAGE);
    List<Class<?>> prClasses = scanPackage(PRS_PACKAGE);

    List<FixEntry> entries = new ArrayList<>();
    for (Class<?> cls : issueClasses) {
      FixEntry e = tryBuildFixEntry(cls, "issue", fixesDir);
      if (e != null) entries.add(e);
    }
    for (Class<?> cls : prClasses) {
      FixEntry e = tryBuildFixEntry(cls, "pr", fixesDir);
      if (e != null) entries.add(e);
    }

    entries.sort(
        Comparator.comparing((FixEntry e) -> e.type).thenComparingInt(e -> e.number));
    return entries;
  }

  private FixEntry tryBuildFixEntry(Class<?> cls, String type, Path fixesDir) {
    String simpleName = cls.getSimpleName();
    if (!simpleName.startsWith("TestFor")) return null;

    // Extract number from "TestForIssue593" -> 593, "TestForPR847" -> 847
    // Also handles variants like "TestForIssue54_1"
    String suffix =
        simpleName.replaceFirst("^TestFor(?:Issue|PR)", "").replaceAll("_.*$", "");
    int number;
    try {
      number = Integer.parseInt(suffix);
    } catch (NumberFormatException ex) {
      return null;
    }

    // Find a public static getChart*() method
    Method chartMethod = findGetChartMethod(cls);
    String pngRelPath = "charts/fixes/" + simpleName + ".png";

    if (chartMethod != null) {
      try {
        Chart<?, ?> chart = (Chart<?, ?>) chartMethod.invoke(null);
        BitmapEncoder.saveBitmap(chart, fixesDir.resolve(simpleName + ".png").toString(),
            BitmapFormat.PNG);
      } catch (Exception e) {
        System.err.println("WARNING: Failed to render " + simpleName + ": " + e);
        e.printStackTrace();
        pngRelPath = null;
      }
    } else {
      pngRelPath = null;
    }

    String ghUrl =
        type.equals("issue")
            ? "https://github.com/knowm/XChart/issues/" + number
            : "https://github.com/knowm/XChart/pull/" + number;

    return new FixEntry(simpleName, type, number, pngRelPath, ghUrl);
  }

  private Method findGetChartMethod(Class<?> cls) {
    for (Method m : cls.getMethods()) {
      if (m.getName().startsWith("getChart")
          && m.getParameterCount() == 0
          && java.lang.reflect.Modifier.isStatic(m.getModifiers())
          && Chart.class.isAssignableFrom(m.getReturnType())) {
        return m;
      }
    }
    return null;
  }

  private String buildFixesHtml(List<FixEntry> entries) {
    long issueCount = entries.stream().filter(e -> e.type.equals("issue")).count();
    long prCount = entries.stream().filter(e -> e.type.equals("pr")).count();

    StringBuilder sb = new StringBuilder();
    sb.append("<!DOCTYPE html>\n")
        .append("<html lang=\"en\">\n")
        .append("<head>\n")
        .append("  <meta charset=\"UTF-8\">\n")
        .append("  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n")
        .append("  <title>XChart \u2014 Fixes &amp; PRs Gallery</title>\n")
        .append("  <link rel=\"stylesheet\" href=\"css/style.css\">\n")
        .append("</head>\n")
        .append("<body>\n")
        .append(buildHeader("fixes.html"))
        .append("  <div class=\"layout\">\n")
        .append("    <aside class=\"sidebar\">\n")
        .append("      <div class=\"sidebar-header\">\n")
        .append(
            "        <input type=\"text\" id=\"search\" placeholder=\"Search fixes\u2026\" autocomplete=\"off\">\n")
        .append("      </div>\n")
        .append("      <nav class=\"category-nav\">\n")
        .append("        <a href=\"#all\" class=\"cat-link active\" data-category=\"all\">\n")
        .append("          All <span class=\"badge\">")
        .append(entries.size())
        .append("</span>\n")
        .append("        </a>\n")
        .append("        <a href=\"#issue\" class=\"cat-link\" data-category=\"issue\">\n")
        .append("          Issues <span class=\"badge\">")
        .append(issueCount)
        .append("</span>\n")
        .append("        </a>\n")
        .append("        <a href=\"#pr\" class=\"cat-link\" data-category=\"pr\">\n")
        .append("          Pull Requests <span class=\"badge\">")
        .append(prCount)
        .append("</span>\n")
        .append("        </a>\n")
        .append("      </nav>\n")
        .append("    </aside>\n\n")
        .append("    <main class=\"content\">\n")
        .append("      <div class=\"charts-grid\" id=\"charts-grid\">\n");

    for (FixEntry fe : entries) {
      String label = fe.type.equals("issue") ? "Issue #" + fe.number : "PR #" + fe.number;
      sb.append("        <div class=\"chart-card\" data-category=\"")
          .append(fe.type)
          .append("\">\n");

      if (fe.pngRelPath != null) {
        sb.append("          <div class=\"chart-img-wrap\">\n")
            .append("            <img src=\"")
            .append(fe.pngRelPath)
            .append("\" alt=\"")
            .append(escapeHtml(fe.className))
            .append("\" loading=\"lazy\">\n")
            .append("          </div>\n");
      } else {
        sb.append("          <div class=\"chart-img-wrap chart-no-image\">\n")
            .append("            <span class=\"no-image-label\">No preview</span>\n")
            .append("          </div>\n");
      }

      sb.append("          <div class=\"chart-info\">\n")
          .append("            <span class=\"chart-category\">")
          .append(label)
          .append("</span>\n")
          .append("            <p class=\"chart-name\">")
          .append(escapeHtml(fe.className))
          .append("</p>\n")
          .append("            <a class=\"gh-link\" href=\"")
          .append(fe.ghUrl)
          .append("\" target=\"_blank\">View on GitHub \u2197</a>\n")
          .append("          </div>\n")
          .append("        </div>\n");
    }

    sb.append("        <div class=\"no-results\" id=\"no-results\" style=\"display:none;\">\n")
        .append("          No entries match your search.\n")
        .append("        </div>\n")
        .append("      </div>\n")
        .append("    </main>\n")
        .append("  </div>\n\n")
        .append(buildFooter())
        .append("  <script src=\"js/main.js\"></script>\n")
        .append("</body>\n")
        .append("</html>\n");

    return sb.toString();
  }

  // ── Shared HTML fragments ────────────────────────────────────────────────────

  private String buildHeader(String activePage) {
    return "  <header class=\"site-header\">\n"
        + "    <div class=\"header-inner\">\n"
        + "      <div class=\"header-title\">\n"
        + "        <span class=\"logo\">\uD83D\uDCCA</span>\n"
        + "        <h1>XChart</h1>\n"
        + "        <p class=\"tagline\">A lightweight Java charting library</p>\n"
        + "      </div>\n"
        + "      <nav class=\"header-links\">\n"
        + "        <a href=\"index.html\""
        + ("index.html".equals(activePage) ? " class=\"active\"" : "")
        + ">Gallery</a>\n"
        + "        <a href=\"fixes.html\""
        + ("fixes.html".equals(activePage) ? " class=\"active\"" : "")
        + ">Fixes &amp; PRs</a>\n"
        + "        <a href=\"https://github.com/knowm/XChart\" target=\"_blank\">GitHub</a>\n"
        + "        <a href=\"https://search.maven.org/search?q=g:org.knowm.xchart\""
        + " target=\"_blank\">Maven Central</a>\n"
        + "      </nav>\n"
        + "    </div>\n"
        + "  </header>\n\n";
  }

  private String buildFooter() {
    return "  <footer class=\"site-footer\">\n"
        + "    <p>XChart is open source under the"
        + " <a href=\"https://github.com/knowm/XChart/blob/develop/LICENSE\""
        + " target=\"_blank\">Apache 2.0 License</a></p>\n"
        + "  </footer>\n\n";
  }

  // ── Package scanning ─────────────────────────────────────────────────────────

  @SuppressWarnings("unchecked")
  private List<Class<?>> scanPackage(String packageName) throws Exception {
    List<Class<?>> classes = new ArrayList<>();
    String packagePath = packageName.replace('.', '/');
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    URL url = loader.getResource(packagePath);
    if (url == null) return classes;

    if (url.getProtocol().equals("jar")) {
      String jarPath = url.getPath().substring(5, url.getPath().indexOf('!'));
      try (JarFile jar = new JarFile(jarPath)) {
        Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
          JarEntry entry = entries.nextElement();
          String name = entry.getName();
          if (name.startsWith(packagePath + "/") && name.endsWith(".class")) {
            String className =
                name.replace('/', '.').substring(0, name.length() - 6);
            try {
              classes.add(Class.forName(className, false, loader));
            } catch (Throwable t) {
              System.err.println("WARNING: Could not load " + className + ": " + t);
            }
          }
        }
      }
    } else {
      java.io.File dir = new java.io.File(url.toURI());
      if (dir.isDirectory()) {
        for (java.io.File f : dir.listFiles()) {
          if (f.getName().endsWith(".class")) {
            String className =
                packageName + "." + f.getName().substring(0, f.getName().length() - 6);
            try {
              classes.add(Class.forName(className, false, loader));
            } catch (Throwable t) {
              System.err.println("WARNING: Could not load " + className + ": " + t);
            }
          }
        }
      }
    }

    classes.sort(Comparator.comparing(Class::getSimpleName));
    return classes;
  }

  // ── Utilities ────────────────────────────────────────────────────────────────

  /** Derives a display category name from a class name like "LineChart01" -> "Line". */
  private String extractCategory(String className) {
    String noDigits = className.replaceAll("\\d+$", "");
    if (noDigits.endsWith("Chart")) {
      noDigits = noDigits.substring(0, noDigits.length() - 5);
    }
    return noDigits.isEmpty() ? "Other" : noDigits;
  }

  private String escapeHtml(String s) {
    return s.replace("&", "&amp;")
        .replace("<", "&lt;")
        .replace(">", "&gt;")
        .replace("\"", "&quot;");
  }

  private void copyResource(String resourcePath, Path destination) throws IOException {
    try (InputStream in = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
      if (in == null) {
        throw new IllegalStateException("Resource not found: " + resourcePath);
      }
      Files.copy(in, destination, StandardCopyOption.REPLACE_EXISTING);
    }
  }

  // ── Data classes ─────────────────────────────────────────────────────────────

  private static class ChartEntry {
    final String className;
    final ExampleChart<Chart<Styler, Series>> example;

    ChartEntry(String className, ExampleChart<Chart<Styler, Series>> example) {
      this.className = className;
      this.example = example;
    }
  }

  private static class FixEntry {
    final String className;
    final String type; // "issue" or "pr"
    final int number;
    final String pngRelPath; // null if no image
    final String ghUrl;

    FixEntry(String className, String type, int number, String pngRelPath, String ghUrl) {
      this.className = className;
      this.type = type;
      this.number = number;
      this.pngRelPath = pngRelPath;
      this.ghUrl = ghUrl;
    }
  }
}
