package org.knowm.xchart;

import java.awt.Color;
import java.awt.Font;
import org.knowm.xchart.style.colors.ChartColor;

/**
 * Holds all styling configuration for the chart zoom-reset button rendered inside {@link
 * XChartPanel}. Keeping these Swing-only concerns here prevents them from polluting the
 * renderer-agnostic {@code Styler} / {@code Theme} layer.
 */
public class ChartButtonConfig {

  /** Position of the button within the chart plot area. */
  public enum ChartButtonPosition {
    InsideNW,
    InsideNE,
    InsideSE,
    InsideSW,
    InsideN,
    InsideS
  }

  private Color backgroundColor = ChartColor.LIGHT_GREY.getColor();
  private Color borderColor = ChartColor.DARK_GREY.getColor();
  private Color hoverColor = ChartColor.LIGHT_GREY.getColor().brighter();
  private Color fontColor = ChartColor.BLACK.getColor();
  private Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 11);
  private int margin = 6;
  private ChartButtonPosition position = ChartButtonPosition.InsideN;

  public Color getBackgroundColor() {

    return backgroundColor;
  }

  /**
   * Sets the button background color.
   *
   * @param backgroundColor the background color
   */
  public ChartButtonConfig setBackgroundColor(Color backgroundColor) {

    this.backgroundColor = backgroundColor;
    return this;
  }

  public Color getBorderColor() {

    return borderColor;
  }

  /**
   * Sets the button border color.
   *
   * @param borderColor the border color
   */
  public ChartButtonConfig setBorderColor(Color borderColor) {

    this.borderColor = borderColor;
    return this;
  }

  public Color getHoverColor() {

    return hoverColor;
  }

  /**
   * Sets the button hover color.
   *
   * @param hoverColor the hover color
   */
  public ChartButtonConfig setHoverColor(Color hoverColor) {

    this.hoverColor = hoverColor;
    return this;
  }

  public Color getFontColor() {

    return fontColor;
  }

  /**
   * Sets the button label font color.
   *
   * @param fontColor the font color
   */
  public ChartButtonConfig setFontColor(Color fontColor) {

    this.fontColor = fontColor;
    return this;
  }

  public Font getFont() {

    return font;
  }

  /**
   * Sets the button label font.
   *
   * @param font the font
   */
  public ChartButtonConfig setFont(Font font) {

    this.font = font;
    return this;
  }

  public int getMargin() {

    return margin;
  }

  /**
   * Sets the padding around the button label, in pixels.
   *
   * @param margin the margin
   */
  public ChartButtonConfig setMargin(int margin) {

    this.margin = margin;
    return this;
  }

  public ChartButtonPosition getPosition() {

    return position;
  }

  /**
   * Sets the position of the button within the chart plot area.
   *
   * @param position the position
   */
  public ChartButtonConfig setPosition(ChartButtonPosition position) {

    this.position = position;
    return this;
  }
}
