package org.knowm.xchart.standalone.issues;

import java.io.IOException;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.knowm.xchart.style.lines.SeriesLines;

/**
 * @author timmolter
 */
public class TestForIssue83 {

  public static void main(String[] args) throws IOException {

    final XYChart chart = new XYChart(500, 580);
    final Styler styleManager = chart.getStyler();
    styleManager.setLegendPosition(LegendPosition.InsideNW);
    styleManager.setLegendVisible(false);

    final double[] keys = {
      101.6829700157669,
      102.4741546172069,
      101.56112372430265,
      102.29668967750219,
      102.1156928915296,
      102.96288807133006,
      102.85820232291313,
      102.70416779932134,
      102.75666703633065,
      102.54695164724063,
      102.64530701963236,
      101.42229521418183,
      102.6239220187132,
      102.65392830689318,
      101.3519528210374,
      102.29890454069181,
      101.45011555581048,
      102.80876656547879,
      102.9487829236201,
      102.65658212119392,
      102.5621808062546,
      102.54679368788584,
      101.44415451644835,
      101.52360532420516,
      102.7494132740427,
      103.03755466140984,
      102.75544822301157,
      102.47525429542132,
      102.63811088590982,
      102.59191775294347,
      101.32048881637581,
      101.44482698818119,
      102.80932781766394,
      101.38219670988731,
      101.46941028338044,
      102.66645765488023,
      101.79878506072832,
      102.12919834900144,
      102.65694786373456,
      101.34087876032368,
      102.35962292551396,
      102.73324077985815,
      101.6331900389947,
      102.68657071464266,
      102.31073017053264,
      102.95034563173265,
      101.56466092390214,
      101.44263290542328,
      102.54872192620866,
      101.51961724673545,
      101.56592215239088,
      102.62299979115573,
      102.16037884019369,
      102.76241468528423,
      103.06247033542299,
      102.50392407673121,
      102.71485878177548,
      102.30595719462644,
      101.83799733593067,
      102.64446820738182,
      102.95845141559543,
      101.44913643540103,
      102.62302475018619,
      101.35064046209624,
      102.49385977096229,
      102.47902987190186,
      102.6192546853896,
      101.31787966105605,
      102.61902499800594,
      102.75304600782704,
      102.66323038080031,
      102.62927538605312,
      101.41262366698777,
      103.06302964768331,
      103.01984694209135,
      101.54079454702787,
      101.7432632007971,
      102.64746484983125,
      102.94083129713017,
      101.38693917529486,
      102.28688939180357,
      101.77714391046378,
      102.61582509980576,
      102.889235861335,
      102.50686276405479,
      103.09822940528373,
      102.58948098022869,
      102.70749156936542,
      102.64387765680111,
      102.75465208779484,
      102.36218073405826
    };
    final double[] values = {
      40.37, 40.59, 40.31, 40.4, 40.39, 40.52, 40.47, 40.56, 40.46, 40.53, 40.58, 40.34, 40.55,
      40.58, 40.33, 40.44, 40.36, 40.57, 40.48, 40.53, 40.55, 40.53, 40.3, 40.31, 40.45, 40.49,
      40.47, 40.59, 40.55, 40.55, 40.35, 40.32, 40.57, 40.33, 40.34, 40.57, 40.38, 40.39, 40.53,
      40.33, 40.41, 40.56, 40.37, 40.46, 40.44, 40.47, 40.31, 40.36, 40.55, 40.36, 40.31, 40.6,
      40.39, 40.46, 40.49, 40.42, 40.58, 40.44, 40.38, 40.53, 40.5, 40.32, 40.6, 40.33, 40.41,
      40.41, 40.53, 40.35, 40.57, 40.46, 40.56, 40.55, 40.34, 40.49, 40.51, 40.32, 40.37, 40.57,
      40.5, 40.35, 40.43, 40.38, 40.58, 40.52, 40.59, 40.49, 40.55, 40.56, 40.53, 40.47, 40.41
    };
    chart.addSeries("Results", keys, values).setLineStyle(SeriesLines.NONE);

    // BitmapEncoder.saveBitmap(chart, "example", BitmapFormat.PNG);
    new SwingWrapper(chart).displayChart();
  }
}
