package org.knowm.xchart.internal.chartpart;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.*;
import java.util.Arrays;
import java.util.Locale;
import java.util.TimeZone;
import org.junit.jupiter.api.Test;
import org.knowm.xchart.style.AxesChartStyler;

public class AxisTickCalculatorDateTest {

  @Test
  public void shouldHonorMaxAxisLabelCount() {
    // given
    AxesChartStyler styler = new AxesChartStyler() {};
    styler.setTimezone(TimeZone.getTimeZone("UTC"));
    styler.setDatePattern("yyyy-MM-dd");
    styler.setLocale(Locale.UK);
    styler.setPlotContentSize(.900d);
    styler.setAxisTickLabelsFont(new Font(Font.SERIF, Font.PLAIN, 11));

    long june1 = 1685577600000L;
    long june2 = 1685664000000L;

    // when
    AxisTickCalculator_Date calculator =
        new AxisTickCalculator_Date(Axis.Direction.X, 900, june1, june2, styler);

    // test
    assertThat(calculator.tickLabels)
        .isEqualTo(
            // NOTE: I don't fully understand why would it take dates before June 1 and after June
            // 2, but hey, this is how it works currently.
            Arrays.asList("2023-05-31", "2023-06-01", "2023-06-02", "2023-06-03", "2023-06-04"));
  }
}
