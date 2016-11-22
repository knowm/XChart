package org.knowm.xchart;

public class Font {
  public static final String DIALOG = "Dialog";
  public static final String DIALOG_INPUT = "DialogInput";
  public static final String SANS_SERIF = "SansSerif";
  public static final String SERIF = "Serif";
  public static final String MONOSPACED = "Monospaced";

  public static final int PLAIN = 0;
  public static final int BOLD = 1;
  public static final int ITALIC = 2;

  private final String name;
  private final int style;
  private final int size;

  public Font(String name, int style, int size) {
    this.name = name;
    this.style = style;
    this.size = size;
  }

  public String getName() {
    return name;
  }

  public int getStyle() {
    return style;
  }

  public int getSize() {
    return size;
  }
}
