package org.knowm.xchart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.awt.Color;
import java.awt.Font;
import org.junit.jupiter.api.Test;
import org.knowm.xchart.ChartButtonConfig.ChartButtonPosition;
import org.knowm.xchart.style.colors.ChartColor;

public class ChartButtonConfigTest {

  @Test
  void defaultValues() {

    ChartButtonConfig config = new ChartButtonConfig();
    assertAll(
        () -> assertThat(config.getBackgroundColor()).isEqualTo(ChartColor.LIGHT_GREY.getColor()),
        () -> assertThat(config.getBorderColor()).isEqualTo(ChartColor.DARK_GREY.getColor()),
        () -> assertThat(config.getFontColor()).isEqualTo(ChartColor.BLACK.getColor()),
        () -> assertThat(config.getFont()).isNotNull(),
        () -> assertThat(config.getFont().getStyle()).isEqualTo(Font.PLAIN),
        () -> assertThat(config.getMargin()).isEqualTo(6),
        () -> assertThat(config.getPosition()).isEqualTo(ChartButtonPosition.InsideN));
  }

  @Test
  void fluentSettersReturnThis() {

    ChartButtonConfig config = new ChartButtonConfig();
    assertAll(
        () -> assertThat(config.setBackgroundColor(Color.RED)).isSameAs(config),
        () -> assertThat(config.setBorderColor(Color.BLUE)).isSameAs(config),
        () -> assertThat(config.setFontColor(Color.WHITE)).isSameAs(config),
        () -> assertThat(config.setFont(new Font(Font.MONOSPACED, Font.BOLD, 12))).isSameAs(config),
        () -> assertThat(config.setMargin(10)).isSameAs(config),
        () -> assertThat(config.setPosition(ChartButtonPosition.InsideSE)).isSameAs(config));
  }

  @Test
  void settersApplyValues() {

    Color bg = new Color(10, 20, 30);
    Color border = new Color(40, 50, 60);
    Color font = new Color(70, 80, 90);
    Font f = new Font(Font.MONOSPACED, Font.BOLD, 14);

    ChartButtonConfig config =
        new ChartButtonConfig()
            .setBackgroundColor(bg)
            .setBorderColor(border)
            .setFontColor(font)
            .setFont(f)
            .setMargin(12)
            .setPosition(ChartButtonPosition.InsideSW);

    assertAll(
        () -> assertThat(config.getBackgroundColor()).isEqualTo(bg),
        () -> assertThat(config.getBorderColor()).isEqualTo(border),
        () -> assertThat(config.getFontColor()).isEqualTo(font),
        () -> assertThat(config.getFont()).isEqualTo(f),
        () -> assertThat(config.getMargin()).isEqualTo(12),
        () -> assertThat(config.getPosition()).isEqualTo(ChartButtonPosition.InsideSW));
  }
}
