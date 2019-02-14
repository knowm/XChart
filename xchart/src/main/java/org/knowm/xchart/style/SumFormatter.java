package org.knowm.xchart.style;

import java.text.DecimalFormat;

public interface SumFormatter {

    String format(double sum);

    class DefaultSumFormatter implements SumFormatter {

        private final DecimalFormat sumDf;

        public DefaultSumFormatter() {
            this.sumDf = new DecimalFormat("#.0");
        }

        public DefaultSumFormatter(String decimalPattern) {
            this.sumDf = new DecimalFormat(decimalPattern);
        }

        @Override
        public String format(double sum) {
            return sumDf.format(sum);
        }
    }
}
