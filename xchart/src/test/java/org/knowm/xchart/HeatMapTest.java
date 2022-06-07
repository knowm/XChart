package org.knowm.xchart;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

import static org.fest.assertions.api.Assertions.assertThat;

public class HeatMapTest {
    @Test
    public void BoundTest() {

        HeatMapChart chart = new HeatMapChartBuilder().width(1000).height(600).title("Bound Test").build();
        assertThat(chart.getWidth() == 1000);
        assertThat(chart.getWidth() == 600);
        assertThat(chart.getTitle() == "Bound Test");
    }
}
