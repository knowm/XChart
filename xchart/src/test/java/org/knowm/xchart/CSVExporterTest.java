package org.knowm.xchart;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class CSVExporterTest {

  @TempDir Path tempDir;

  @Test
  public void writeCSVRowsExportsXAndYDataRows() throws Exception {

    XYChart chart = new XYChartBuilder().width(600).height(400).build();
    chart.addSeries("series1", new double[] {1.0, 2.0, 3.0}, new double[] {12.0, 34.0, 56.0});

    CSVExporter.writeCSVRows(chart.getSeries("series1"), tempDir.toString() + File.separator);

    String csv = Files.readString(tempDir.resolve("series1.csv"), StandardCharsets.UTF_8);
    assertThat(csv)
        .isEqualTo(
            "1.0,2.0,3.0"
                + System.lineSeparator()
                + "12.0,34.0,56.0"
                + System.lineSeparator());
  }

  @Test
  public void writeCSVColumnsExportsXAndYDataColumns() throws Exception {

    XYChart chart = new XYChartBuilder().width(600).height(400).build();
    chart.addSeries("series1", new double[] {1.0, 2.0, 3.0}, new double[] {12.0, 34.0, 56.0});

    CSVExporter.writeCSVColumns(chart.getSeries("series1"), tempDir.toString() + File.separator);

    String csv = Files.readString(tempDir.resolve("series1.csv"), StandardCharsets.UTF_8);
    assertThat(csv)
        .isEqualTo(
            "1.0,12.0"
                + System.lineSeparator()
                + "2.0,34.0"
                + System.lineSeparator()
                + "3.0,56.0"
                + System.lineSeparator());
  }
}
