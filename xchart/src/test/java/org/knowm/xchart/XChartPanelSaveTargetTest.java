package org.knowm.xchart;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import javax.swing.filechooser.FileFilter;
import org.junit.jupiter.api.Test;

/**
 * Covers the Save As path resolution behind the overwrite confirmation added for issue #583. The
 * dialog itself cannot be exercised here (CI is headless), so the resolution the confirmation
 * depends on is tested directly.
 */
public class XChartPanelSaveTargetTest {

  /** Builds the same filter instances the Save As dialog installs. */
  private static FileFilter filter(String suffix) {

    return new XChartPanel.SuffixSaveFilter(suffix);
  }

  @Test
  public void testExtensionIsAppendedWhenTheUserOmitsIt() {

    // The case that made the bug bite: "chart" is written as "chart.png", so an overwrite check
    // against the raw selection would never fire.
    assertEquals("chart.png", XChartPanel.resolveSaveTarget(new File("chart"), filter("png")).getName());
    assertEquals("chart.jpg", XChartPanel.resolveSaveTarget(new File("chart"), filter("jpg")).getName());
    assertEquals("chart.pdf", XChartPanel.resolveSaveTarget(new File("chart"), filter("pdf")).getName());
  }

  @Test
  public void testExtensionIsNotDuplicatedWhenTheUserTypesIt() {

    assertEquals(
        "chart.png", XChartPanel.resolveSaveTarget(new File("chart.png"), filter("png")).getName());
    assertEquals(
        "chart.svg", XChartPanel.resolveSaveTarget(new File("chart.svg"), filter("svg")).getName());
  }

  @Test
  public void testResolvedTargetMatchesWhatTheEncoderWrites() {

    // The overwrite check is only trustworthy if it names the exact file the encoder opens.
    String selected = "chart";
    assertEquals(
        BitmapEncoder.addFileExtension(selected, BitmapEncoder.BitmapFormat.GIF),
        XChartPanel.resolveSaveTarget(new File(selected), filter("gif")).getPath());
    assertEquals(
        VectorGraphicsEncoder.addFileExtension(
            selected, VectorGraphicsEncoder.VectorGraphicsFormat.EPS),
        XChartPanel.resolveSaveTarget(new File(selected), filter("eps")).getPath());
  }

  @Test
  public void testNullFilterFallsBackToPng() {

    // Mirrors the encoder the dialog used for a null filter before this change.
    assertEquals("png", XChartPanel.suffixOf(null));
    assertEquals("chart.png", XChartPanel.resolveSaveTarget(new File("chart"), null).getName());
  }

  @Test
  public void testSuffixIsReadFromTheFilterRatherThanItsDescription() {

    assertEquals("eps", XChartPanel.suffixOf(filter("eps")));
    assertEquals("bmp", XChartPanel.suffixOf(filter("bmp")));
  }
}
