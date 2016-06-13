/**
 * Copyright 2015-2016 Knowm Inc. (http://knowm.org) and contributors.
 * Copyright 2011-2015 Xeiam LLC (http://xeiam.com) and contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.knowm.xchart.demo.charts.line;

import java.util.Arrays;
import java.util.List;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.CategorySeries;
import org.knowm.xchart.CategorySeries.CategorySeriesRenderStyle;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler.ChartTheme;
import org.knowm.xchart.style.Styler.LegendPosition;

/**
 * Category Chart with Line Rendering
 * <p>
 * Demonstrates the following:
 * <ul>
 * <li>A Line Chart created from multiple category series types
 * <li>GGPlot2 Theme
 * <li>disabling some series shown in legend
 */
public class LineChart07 implements ExampleChart<CategoryChart> {

  public static void main(String[] args) {

    ExampleChart<CategoryChart> exampleChart = new LineChart07();
    CategoryChart chart = exampleChart.getChart();
    new SwingWrapper<CategoryChart>(chart).displayChart();
  }

  @Override
  public CategoryChart getChart() {

    // Create Chart
    CategoryChart chart = new CategoryChartBuilder().width(800).height(600).theme(ChartTheme.GGPlot2).title("ThreadPool Benchmark").xAxisTitle("Threads").yAxisTitle("Executions").build();

    // Customize Chart
    chart.getStyler().setDefaultSeriesRenderStyle(CategorySeriesRenderStyle.Line);
    chart.getStyler().setXAxisLabelRotation(270);
    chart.getStyler().setLegendPosition(LegendPosition.OutsideE);
    chart.getStyler().setAvailableSpaceFill(0);
    chart.getStyler().setOverlapped(true);

    // Declare data
    List<String> xAxisKeys = Arrays.asList(new String[] { "release-0.5", "release-0.6", "release-0.7", "release-0.8", "release-0.9", "release-1.0.0", "release-1.1.0", "release-1.2.0", "release-1.3.0",
        "release-2.0.0", "release-2.1.0", "release-2.2.0", "release-2.3.0", "release-2.4.0", "release-2.5.0", "release-2.6.0", "release-3.0.0", "release-3.1.0", "release-3.2.0", "release-3.3.0",
        "release-3.4.0", "release-3.5.0", "release-3.6.0", "release-3.7.0", "release-3.8.0", "release-4.0.0", "release-4.1.0", "release-4.2.0", "release-4.3.0", "release-4.4.0", "release-4.4.1",
        "release-4.4.2" });
    String[] seriesNames = new String[] { "Threads:4", "Threads:10", "Threads:20", "Threads:50", "Threads:100", "Threads:150", "Threads:200", "Threads:250", "Threads:500", "Threads:750",
        "Threads:1000", "Threads:1500", "Threads:2000", "Threads:2500" };
    Integer[][] dataPerSeries = new Integer[][] { { 117355, 117594, 117551, 117719, 116553, 117304, 118945, 119067, 117803, 118080, 117676, 118599, 118224, 119263, 119455, 119393, 117961, 119254,
        118447, 119428, 118812, 117947, 119405, 119329, 117749, 119331, 119354, 119519, 118494, 119780, 119766, 119742 }, { 127914, 128835, 128953, 128893, 128830, 129012, 129235, 129424, 129400,
            129477, 129065, 129103, 129150, 129434, 129000, 129467, 128994, 129167, 129849, 128702, 134439, 134221, 134277, 134393, 134390, 134581, 134263, 134641, 134672, 137880, 137675, 137943 }, {
                133396, 133977, 133992, 133656, 134406, 134657, 135194, 135497, 134881, 134873, 135065, 135045, 134480, 135004, 135111, 134720, 134639, 135505, 135831, 135974, 140965, 140759, 140545,
                139959, 141063, 141339, 140967, 140927, 141972, 160884, 163402, 164572 }, { 122376, 122236, 122861, 122806, 122775, 122619, 122505, 122585, 122742, 122847, 122660, 122705, 122852,
                    122847, 122909, 122788, 122861, 123396, 123430, 122847, 121103, 121013, 120936, 120901, 121096, 120931, 121160, 121112, 121145, 175077, 174483, 175787 }, { 120048, 120226, 120745,
                        120669, 120647, 120683, 120499, 120533, 120628, 121059, 120901, 120838, 120845, 120954, 120963, 121055, 120948, 121111, 121239, 121094, 121422, 121249, 120924, 120918, 121061,
                        121063, 121065, 121098, 121011, 173280, 173179, 172193 }, { 119712, 119766, 120053, 120217, 119954, 120080, 120167, 119898, 120065, 120253, 120153, 120103, 120070, 120446,
                            120347, 120223, 120261, 120629, 120576, 120541, 121405, 121481, 121461, 121387, 121295, 121597, 121592, 121593, 121576, 171415, 170628, 169878 }, { 119807, 120232, 119745,
                                119892, 120024, 119854, 119818, 119908, 119685, 119816, 119848, 119919, 119627, 119906, 120242, 119974, 120116, 120472, 120304, 120294, 121308, 121338, 121278, 121292,
                                121418, 121570, 121564, 121541, 121571, 170597, 170346, 170434 }, { 121283, 121580, 120720, 120553, 121146, 120016, 119994, 120194, 120149, 120239, 120238, 120031,
                                    120016, 120314, 120023, 120408, 120315, 120711, 121046, 120850, 121192, 121315, 121198, 121224, 121396, 121398, 121636, 121412, 121252, 168489, 169774, 168750 }, {
                                        121219, 121594, 122576, 122368, 122874, 121831, 121386, 121433, 121722, 121600, 121158, 121653, 121306, 121652, 121982, 121775, 121819, 122243, 122128, 122067,
                                        125185, 124972, 125023, 125004, 125120, 125320, 125395, 125134, 124838, 168492, 167673, 167087 }, { 121576, 122197, 121660, 121673, 122047, 120863, 120715,
                                            120542, 120934, 120936, 120448, 120823, 120546, 121150, 120863, 120946, 120865, 121273, 120848, 121210, 124867, 124927, 124863, 124610, 124633, 124881,
                                            124887, 124626, 124814, 167504, 167717, 165026 }, { 121822, 121540, 121488, 122055, 121253, 120728, 120626, 120474, 119848, 120129, 120082, 120075, 120429,
                                                120859, 121228, 120390, 120161, 121465, 121085, 120682, 124287, 124029, 124162, 124185, 124024, 124416, 124558, 124206, 124109, 166816, 167583,
                                                164828 }, { 121094, 121594, 121273, 121495, 121638, 120419, 119611, 119406, 119381, 120053, 119591, 120080, 120071, 119709, 120008, 120469, 119417,
                                                    120327, 120510, 119873, 123192, 123085, 123388, 123298, 123260, 122982, 123465, 123267, 122856, 164366, 163919, 166612 }, { 120639, 120628, 121443,
                                                        121160, 121245, 119819, 119865, 119300, 119466, 119478, 119870, 119720, 119671, 120333, 119718, 119528, 119581, 120716, 120624, 119585, 121685,
                                                        121978, 123017, 121433, 122190, 122330, 122458, 122090, 122234, 161976, 163628, 158023 }, { 120242, 120674, 120091, 120299, 120662, 119885,
                                                            119480, 119269, 118983, 119290, 119304, 119161, 119875, 118830, 119517, 119980, 119502, 120883, 118953, 119461, 120753, 120526, 120967,
                                                            120244, 122381, 121084, 122404, 121761, 121546, 161230, 160123, 160534 } };

    // Series
    for (int i = 0; i < seriesNames.length; i++) {
      CategorySeries series = chart.addSeries(seriesNames[i], xAxisKeys, Arrays.asList(dataPerSeries[i]));
      series.setShowInLegend(i % 2 == 0);
    }

    return chart;
  }
}
