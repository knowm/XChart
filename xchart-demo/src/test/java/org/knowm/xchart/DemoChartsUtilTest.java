package org.knowm.xchart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.knowm.xchart.demo.DemoChartsUtil;

/**
 * Regression test for <a href="https://github.com/knowm/XChart/issues/594">issue #594</a>: the demo
 * chart scan silently found nothing when the project path contained a space, because it resolved
 * the package directory from the percent-encoded {@code URL.getFile()}.
 */
public class DemoChartsUtilTest {

  private static final String DEMO_CHARTS_PACKAGE_PATH = "org/knowm/xchart/demo/charts";

  @Test
  public void shouldFindDemoChartsWhenClasspathContainsASpace() throws Exception {

    Path classesRoot = Files.createTempDirectory("xchart demo classes");
    ClassLoader originalContextClassLoader = Thread.currentThread().getContextClassLoader();
    try {
      assertThat(classesRoot.toString()).contains(" ");
      copyDemoChartClasses(classesRoot.resolve(DEMO_CHARTS_PACKAGE_PATH));

      // a null parent stops the lookup delegating back to the real, space-free classpath
      URLClassLoader classLoader =
          new URLClassLoader(new URL[] {classesRoot.toUri().toURL()}, null);
      Thread.currentThread().setContextClassLoader(classLoader);

      assertThat(DemoChartsUtil.getAllDemoCharts()).isNotEmpty();

      classLoader.close();
    } finally {
      Thread.currentThread().setContextClassLoader(originalContextClassLoader);
      deleteRecursively(classesRoot);
    }
  }

  /**
   * Copies the compiled demo chart classes to {@code target} so they can be discovered through a
   * path containing a space. The classes are only ever <em>found</em> there - loading them still
   * goes through the class loader that owns this test.
   */
  private void copyDemoChartClasses(Path target) throws Exception {

    URL url =
        Thread.currentThread().getContextClassLoader().getResource(DEMO_CHARTS_PACKAGE_PATH);
    assertThat(url).isNotNull();
    // only meaningful when the demo classes sit in a directory, as they do during a build
    assumeTrue("file".equals(url.getProtocol()));

    Path source = new File(url.toURI()).toPath();
    Files.createDirectories(target);
    try (Stream<Path> sources = Files.walk(source)) {
      for (Path path : (Iterable<Path>) sources::iterator) {
        Path destination = target.resolve(source.relativize(path).toString());
        if (Files.isDirectory(path)) {
          Files.createDirectories(destination);
        } else {
          Files.copy(path, destination);
        }
      }
    }
  }

  private void deleteRecursively(Path path) throws IOException {

    try (Stream<Path> paths = Files.walk(path)) {
      for (Path each : (Iterable<Path>) paths.sorted(Comparator.reverseOrder())::iterator) {
        Files.deleteIfExists(each);
      }
    }
  }
}
