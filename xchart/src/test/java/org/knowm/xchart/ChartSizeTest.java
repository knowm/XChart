package org.knowm.xchart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.jupiter.api.Test;

/** https://github.com/knowm/XChart/issues/807 */
public class ChartSizeTest {

  private static XYChart sampleChart() {
    XYChart chart = new XYChartBuilder().width(500).height(400).title("Sample").build();
    chart.addSeries("y(x)", new double[] {0, 1, 2}, new double[] {2, 1, 0});
    return chart;
  }

  @Test
  public void setSizeUpdatesDimensionsAndChains() {

    XYChart chart = sampleChart();
    assertThat(chart.setSize(1920, 1080)).isSameAs(chart);
    assertThat(chart.getWidth()).isEqualTo(1920);
    assertThat(chart.getHeight()).isEqualTo(1080);
  }

  @Test
  public void setSizeRejectsNonPositiveDimensions() {

    XYChart chart = sampleChart();
    for (int[] size : new int[][] {{0, 100}, {100, 0}, {-1, 100}, {100, -1}}) {
      assertThatThrownBy(() -> chart.setSize(size[0], size[1]))
          .isInstanceOf(IllegalArgumentException.class);
    }
    assertThat(chart.getWidth()).isEqualTo(500);
    assertThat(chart.getHeight()).isEqualTo(400);
  }

  @Test
  public void rasterExportUsesNewSize() throws IOException {

    XYChart chart = sampleChart();
    chart.setSize(800, 300);
    BufferedImage image =
        ImageIO.read(new ByteArrayInputStream(ChartEncoder.getBytes(chart, "png")));
    assertThat(image.getWidth()).isEqualTo(800);
    assertThat(image.getHeight()).isEqualTo(300);
  }

  @Test
  public void vectorExportUsesNewSize() throws IOException {

    XYChart chart = sampleChart();
    chart.setSize(720, 360);

    String svg = new String(ChartEncoder.getBytes(chart, "svg"), StandardCharsets.UTF_8);
    Matcher viewBox = Pattern.compile("viewBox=\"([^\"]+)\"").matcher(svg);
    assertThat(viewBox.find()).isTrue();
    String[] bounds = viewBox.group(1).trim().split("\\s+");
    assertThat(Double.parseDouble(bounds[2])).isEqualTo(720.0);
    assertThat(Double.parseDouble(bounds[3])).isEqualTo(360.0);

    try (PDDocument pdf = Loader.loadPDF(ChartEncoder.getBytes(chart, "pdf"))) {
      assertThat(pdf.getPage(0).getMediaBox().getWidth()).isEqualTo(720.0f);
      assertThat(pdf.getPage(0).getMediaBox().getHeight()).isEqualTo(360.0f);
    }
  }

  @Test
  public void setSizeWorksForOtherChartTypes() throws IOException {

    PieChart pie = new PieChartBuilder().width(500).height(400).build();
    pie.addSeries("A", 3);
    pie.setSize(300, 600);
    BufferedImage image = ImageIO.read(new ByteArrayInputStream(ChartEncoder.getBytes(pie, "png")));
    assertThat(image.getWidth()).isEqualTo(300);
    assertThat(image.getHeight()).isEqualTo(600);

    CategoryChart bars = new CategoryChartBuilder().width(500).height(400).build();
    bars.addSeries("Sample", new double[] {1, 2, 3}, new double[] {2, 4, 3});
    bars.setSize(640, 200);
    image = ImageIO.read(new ByteArrayInputStream(ChartEncoder.getBytes(bars, "png")));
    assertThat(image.getWidth()).isEqualTo(640);
    assertThat(image.getHeight()).isEqualTo(200);
  }
}
