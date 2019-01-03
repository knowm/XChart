package org.knowm.xchart.style;

import java.text.DecimalFormat;

public interface SumFormatter {

  String format(double sum);

  class DefaultSumFormatter implements SumFormatter {

    private final Styler styler;

    public DefaultSumFormatter(Styler styler) {
      this.styler = styler;
    }

    @Override
    public String format(double sum) {
      DecimalFormat totalDf = new DecimalFormat(styler.getDecimalPattern());
      return totalDf.format(sum);
    }
  }
}
