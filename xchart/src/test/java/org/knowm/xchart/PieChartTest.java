package org.knowm.xchart;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PieChartTest {
    /*
     *Purpose : Check for Width and Height and Title
     * Input: width(800), height(600), title("test title")
     * Expected: getWidth-> 800, getHeight-> 600, getTitle ->"test title"
     */
    @Test
    public void BuildTest(){
        PieChart chart =
                new PieChartBuilder().width(800).height(600).title("test title").build();
        assertEquals(800,chart.getWidth());
        assertEquals(600,chart.getHeight());
        assertEquals("test title",chart.getTitle());
    }
}
