package org.knowm.xchart.site;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

    // Group by category (derived from the simple class name prefix, e.g. "LineChart01" → "Line")
    Map<String, List<ChartEntry>> byCategory = new LinkedHashMap<>();
    for (ExampleChart<Chart<Styler, Series>> example : allCharts) {
      String className = example.getClass().getSimpleName();
      String category = extractCategory(className);
      byCategory.computeIfAbsent(category, k -> new ArrayList<>()).add(new ChartEntry(className, example));
    }

    // Sort categories alphabetically
    List<String> sortedCategories = new ArrayList<>(byCategory.keySet());
    sortedCategories.sort(Comparator.naturalOrder());
    Map<String, List<ChartEntry>> sorted = new LinkedHashMap<>();
    for (String cat : sortedCategories) {
      sorted.put(cat, byCategory.get(cat));
    }

    System.out.println("Rendering " + allCharts.size() + " charts across " + sorted.size() + " categories...");
    renderChartImages(sorted, chartsDir);

    System.out.println("Copying static assets...");
    copyResource("site/css/style.css", cssDir.resolve("style.css"));
    copyResource("site/js/main.js", jsDir.resolve("main.js"));

    System.out.println("Generating HTML...");
    String indexHtml = buildIndexHtml(sorted);
    Files.write(outputDir.resolve("index.html"), indexHtml.getBytes(StandardCharsets.UTF_8));

    System.out.println("Site generated at: " + outputDir.toAbsolutePath());
  }

  private void renderChartImages(Map<String, List<ChartEntry>> byCategory, Path chartsDir)
      throws IOException {
    for (Map.Entry<String, List<ChartEntry>> entry : byCategory.entrySet()) {
      Path catDir = chartsDir.resolve(entry.getKey().toLowerCase());
      Files.createDirectories(catDir);
      for (ChartEntry ce : entry.getValue()) {
        try {
          Chart<?, ?> chart = ce.example.getChart();
          String pngPath = catDir.resolve(ce.className + ".png").toString();
          BitmapEncoder.saveBitmap(chart, pngPath, BitmapFormat.PNG);
        } catch (Exception e) {
          System.err.println("WARNING: Failed to render " + ce.className + ": " + e.getMessage());
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
        .append("  <header class=\"site-header\">\n")
        .append("    <div class=\"header-inner\">\n")
        .append("      <div class=\"header-title\">\n")
        .append("        <span class=\"logo\">\uD83D\uDCCA</span>\n")
        .append("        <h1>XChart</h1>\n")
        .append("        <p class=\"tagline\">A lightweight Java charting library</p>\n")
        .append("      </div>\n")
        .append("      <nav class=\"header-links\">\n")
        .append("        <a href=\"https://github.com/knowm/XChart\" target=\"_blank\">GitHub</a>\n")
        .append("        <a href=\"https://search.maven.org/search?q=g:org.knowm.xchart\" target=\"_blank\">Maven Central</a>\n")
        .append("      </nav>\n")
        .append("    </div>\n")
        .append("  </header>\n\n")
        .append("  <div class=\"layout\">\n")
        .append("    <aside class=\"sidebar\">\n")
        .append("      <div class=\"sidebar-header\">\n")
        .append("        <input type=\"text\" id=\"search\" placeholder=\"Search charts\u2026\" autocomplete=\"off\">\n")
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
        .append("  <footer class=\"site-footer\">\n")
        .append("    <p>XChart is open source under the <a href=\"https://github.com/knowm/XChart/blob/develop/LICENSE\" target=\"_blank\">Apache 2.0 License</a></p>\n")
        .append("  </footer>\n\n")
        .append("  <script src=\"js/main.js\"></script>\n")
        .append("</body>\n")
        .append("</html>\n");

    return sb.toString();
  }

  /** Derives a display category name from a class name like "LineChart01" → "Line". */
  private String extractCategory(String className) {
    // Strip trailing digits
    String noDigits = className.replaceAll("\\d+$", "");
    // Remove "Chart" suffix if present
    if (noDigits.endsWith("Chart")) {
      noDigits = noDigits.substring(0, noDigits.length() - 5);
    }
    return noDigits.isEmpty() ? "Other" : noDigits;
  }

  private String escapeHtml(String s) {
    return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
  }

  private void copyResource(String resourcePath, Path destination) throws IOException {
    try (InputStream in = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
      if (in == null) {
        throw new IllegalStateException("Resource not found: " + resourcePath);
      }
      Files.copy(in, destination, StandardCopyOption.REPLACE_EXISTING);
    }
  }

  private static class ChartEntry {
    final String className;
    final ExampleChart<Chart<Styler, Series>> example;

    ChartEntry(String className, ExampleChart<Chart<Styler, Series>> example) {
      this.className = className;
      this.example = example;
    }
  }
}
