## [![XChart](https://raw.githubusercontent.com/knowm/XChart/develop/etc/XChart_64_64.png)](http://knowm.org/open-source/xchart) XChart

XChart is a light weight Java library for plotting data.

## Description

XChart is a light-weight and convenient library for plotting data designed to go from data to chart in the least amount of time possible and to take the guess-work out of customizing the chart style.

## Simplest Example

Create a `XYChart` instance via `QuickChart`, add a series of data to it, and either display it or save it as a bitmap.  

```java

double[] xData = new double[] { 0.0, 1.0, 2.0 };
double[] yData = new double[] { 2.0, 1.0, 0.0 };

// Create Chart
XYChart chart = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", xData, yData);

// Show it
new SwingWrapper(chart).displayChart();

// Save it
BitmapEncoder.saveBitmap(chart, "./Sample_Chart", BitmapFormat.PNG);

// or save it in high-res
BitmapEncoder.saveBitmapWithDPI(chart, "./Sample_Chart_300_DPI", BitmapFormat.PNG, 300);
```

![](https://raw.githubusercontent.com/knowm/XChart/develop/etc/XChart_Simplest.png)

## Intermediate Example

Create a `XYChart` via a `XYChartBuilder`, style chart, add a series to it, style series, and display chart.

```java

// Create Chart
XYChart chart = new XYChartBuilder().width(600).height(500).title("Gaussian Blobs").xAxisTitle("X").yAxisTitle("Y").build();

// Customize Chart
chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Scatter);
chart.getStyler().setChartTitleVisible(false);
chart.getStyler().setLegendPosition(LegendPosition.InsideSW);
chart.getStyler().setMarkerSize(16);

// Series
chart.addSeries("Gaussian Blob 1", getGaussian(1000, 1, 10), getGaussian(1000, 1, 10));
XYSeries series = chart.addSeries("Gaussian Blob 2", getGaussian(1000, 1, 10), getGaussian(1000, 0, 5));
series.setMarker(SeriesMarkers.DIAMOND);

new SwingWrapper(chart).displayChart();
```

![](https://raw.githubusercontent.com/knowm/XChart/develop/etc/XChart_Intermediate.png)

## Advanced Example

Create a `XYChart` via a `XYChartBuilder`, style chart, add a series to it, add chart to `XChartPanel`, embed in Java Swing App, and display GUI.

```java

// Create Chart
final XYChart chart = new XYChartBuilder().width(600).height(400).title("Area Chart").xAxisTitle("X").yAxisTitle("Y").build();

// Customize Chart
chart.getStyler().setLegendPosition(LegendPosition.InsideNE);
chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Area);

// Series
chart.addSeries("a", new double[] { 0, 3, 5, 7, 9 }, new double[] { -3, 5, 9, 6, 5 });
chart.addSeries("b", new double[] { 0, 2, 4, 6, 9 }, new double[] { -1, 6, 4, 0, 4 });
chart.addSeries("c", new double[] { 0, 1, 3, 8, 9 }, new double[] { -2, -1, 1, 0, 1 });

// Schedule a job for the event-dispatching thread:
// creating and showing this application's GUI.
javax.swing.SwingUtilities.invokeLater(new Runnable() {

  @Override
  public void run() {

    // Create and set up the window.
    JFrame frame = new JFrame("Advanced Example");
    frame.setLayout(new BorderLayout());
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // chart
    JPanel chartPanel = new XChartPanel<XYChart>(chart);
    frame.add(chartPanel, BorderLayout.CENTER);

    // label
    JLabel label = new JLabel("Blah blah blah.", SwingConstants.CENTER);
    frame.add(label, BorderLayout.SOUTH);

    // Display the window.
    frame.pack();
    frame.setVisible(true);
  }
});

```

![](https://raw.githubusercontent.com/knowm/XChart/develop/etc/XChart_Advanced.png)

To make it real-time, simply call `updateXYSeries` on the `XYChart` instance to update the series data, followed by `revalidate()` and `repaint()` on the `XChartPanel` instance to repaint.

## Features

* [x] No *required* additional dependencies
* [x] ~182KB Jar
* [x] Multiple Y-Axis charts
* [x] Line charts
* [x] Step charts
* [x] Scatter charts
* [x] Area charts
* [x] Step Area charts
* [x] Bar charts
* [x] Histogram charts
* [x] Pie charts
* [x] Donut charts
* [x] Bubble charts
* [x] Stick charts
* [x] Dial charts
* [x] Radar charts
* [x] OHLC charts
* [x] Error bars
* [x] Logarithmic axes
* [x] Number, Date, Bubble and Category X-Axis
* [x] Multiple series
* [x] Tool tips
* [x] Extensive customization
* [x] Themes - XChart, GGPlot2, Matlab
* [x] Right-click, Save-As...
* [x] User-defined axes range
* [x] Definable legend placement
* [x] CSV import and export
* [x] High resolution chart export
* [x] Export as PNG, JPG, BMP, GIF with custom DPI setting
* [x] Export SVG, EPS and PDF using optional `de.erichseifert.vectorgraphics2d` library
* [x] Real-time charts
* [x] Java 6 and up

## Chart Types

Currently, there are 5 major chart types. Each type has its corresponding `ChartBuilder`, `Styler` and `Series`.

| Chart Type | Builder | Styler | Series | Allowed Data Types | Default Series Render Style |
|---|---|---|---|---|---|
| XYChart | XYChartBuilder | XYStyler | XYSeries | Number, Date | Line |
| CategoryChart | CategoryChartBuilder | CategoryStyler | CategorySeries | Number, Date, String | Bar |
| PieChart | PieChartBuilder | PieStyler | PieSeries | String | Pie |
| BubbleChart | BubbleChartBuilder | BubbleStyler | BubbleSeries | Number, Date | Round |
| DialChart | DialChartBuilder | DialStyler | DialSeries | double  | Round |
| RadarChart | RadarChartBuilder | RadarStyler | RadarSeries | double[] | Round |
| OHLCChart | OHLCChartBuilder | OHLCStyler | OHLCSeries | OHLC with Date | Candle |

The different Stylers contain chart styling methods specific to the corresponding chart type as well as common styling methods common across all chart types.

### XYChart

![](https://raw.githubusercontent.com/knowm/XChart/develop/etc/XYChart.png)

`XYChart` charts take Date or Number data types for the X-Axis and Number data types for the Y-Axis. For both axes, the tick marks are auto generated to span the range and domain of the data in evenly-spaced intervals. 

Series render styles include: `Line`, `Scatter`, `Area`, `Step` and `StepArea`.

### CategoryChart 

![](https://raw.githubusercontent.com/knowm/XChart/develop/etc/CategoryChart.png)

`CategoryChart` charts take Date, Number or String data types for the X-Axis and Number data types for the Y-Axis. For the X-Axis, each category is given its own tick mark.  

Series render styles include: `Bar`, `Line`, `Scatter`, `Area` and `Stick`.

### PieChart

![](https://raw.githubusercontent.com/knowm/XChart/develop/etc/PieChart.png)

`PieChart` charts take String data types for the pie slice name and Number data types for the pie slice value.  

Series render styles include: `Pie` and `Donut`.

### BubbleChart

![](https://raw.githubusercontent.com/knowm/XChart/develop/etc/XChart_Bubble_Chart.png)

`BubbleChart` charts take Date or Number data types for the X-Axis and Number data types for the Y-Axis and bubble sizes. 

Series render styles include: `Round` and in the near future `Square`.

### DialChart

![](https://raw.githubusercontent.com/knowm/XChart/develop/etc/XChart_Dial_Chart.png)

`DialChart` charts take a `double` to set the position of the dial pointer.

### RadarChart

![](https://raw.githubusercontent.com/knowm/XChart/develop/etc/XChart_Radar_Chart.png)

`RadarChart` charts take a `double[]` of values between `0` and `1` to set the position of radar node.

Series render styles include: `Polygon` and  `Circle`.

### OHLCChart

![](https://raw.githubusercontent.com/knowm/XChart/develop/etc/XChart_Candle.png)

`OHLCChart` charts take Date data types for the X-Axis and 4 Number data types for the Y-Axis. For both axes, the tick marks are auto generated to span the range and domain of the data in evenly-spaced intervals.

Series render styles include: `Candle`, `HiLo`.

## Real-time Java Charts using XChart

![](https://raw.githubusercontent.com/knowm/XChart/develop/etc/XChart_SimpleRealtime.gif)

Creating real-time charts is as simple as calling `updateXYSeries` for one or more series objects through the `XYChart` instance and triggering a redraw of the `JPanel` containing the chart. This works for all chart types including `XYChart`, `CategoryChart`, `BubbleChart` and `PieChart`, for which example source code can be found [here](https://github.com/knowm/XChart/tree/develop/xchart-demo/src/main/java/org/knowm/xchart/demo/charts/realtime). Examples demonstrate using the `SwingWrapper` with `repaintChart()` method as well as `XChartPanel` with `revalidate()` and `repaint()`. 

The following sample code used to generate the above real-time chart can be found [here](https://github.com/knowm/XChart/blob/develop/xchart-demo/src/main/java/org/knowm/xchart/standalone/readme/SimpleRealTime.java).

```java
public class SimpleRealTime {

  public static void main(String[] args) throws Exception {

    double phase = 0;
    double[][] initdata = getSineData(phase);

    // Create Chart
    final XYChart chart = QuickChart.getChart("Simple XChart Real-time Demo", "Radians", "Sine", "sine", initdata[0], initdata[1]);

    // Show it
    final SwingWrapper<XYChart> sw = new SwingWrapper<XYChart>(chart);
    sw.displayChart();

    while (true) {

      phase += 2 * Math.PI * 2 / 20.0;

      Thread.sleep(100);

      final double[][] data = getSineData(phase);

      javax.swing.SwingUtilities.invokeLater(new Runnable() {

        @Override
        public void run() {

          chart.updateXYSeries("sine", data[0], data[1], null);
          sw.repaintChart();
        }
      });
    }

  }

  private static double[][] getSineData(double phase) {

    double[] xData = new double[100];
    double[] yData = new double[100];
    for (int i = 0; i < xData.length; i++) {
      double radians = phase + (2 * Math.PI / xData.length * i);
      xData[i] = radians;
      yData[i] = Math.sin(radians);
    }
    return new double[][] { xData, yData };
  }
}
```

## Chart Customization

All the styling options can be found in one of two possible places: 1) the Chart's `Styler` or 2) the series' `set` methods. With this chart customization design, all customization options can be quickly "discovered" using an IDE's built in "Content Assist". With centralized styling like this, there is no need to hunt around the entire charting API to find that one customization you're looking for - it's all right in one spot!

![](https://raw.githubusercontent.com/knowm/XChart/develop/etc/XChart_Chart_Customization.png)
 
![](https://raw.githubusercontent.com/knowm/XChart/develop/etc/XChart_Series_Customization.png)
 
## Chart Themes

XChart ships with three different themes: Default `XChart`, `GGPlot2` and `Matlab`. Using a different theme is as simple as setting the Chart's theme with the `theme` method of the `ChartBuilder`.

    XYChart chart = new XYChartBuilder().width(800).height(600).theme(ChartTheme.Matlab).build();

![](https://raw.githubusercontent.com/knowm/XChart/develop/etc/XChart_Themes.png)

## What's Next?

Now go ahead and [study some more examples](http://knowm.org/open-source/xchart/xchart-example-code/), [download the thing](http://knowm.org/open-source/xchart/xchart-change-log) and [provide feedback](https://github.com/knowm/XChart/issues).
 
## Getting Started

### Non-Maven

Download Jar: http://knowm.org/open-source/xchart/xchart-change-log

### Maven

The XChart release artifacts are hosted on Maven Central.

Add the XChart library as a dependency to your pom.xml file:

```xml
    <dependency>
        <groupId>org.knowm.xchart</groupId>
        <artifactId>xchart</artifactId>
        <version>3.5.2</version>
    </dependency>
```

For snapshots, add the following to your pom.xml file:

```xml
    <repository>
      <id>sonatype-oss-snapshot</id>
      <snapshots/>
      <url>https://oss.sonatype.org/content/repositories/snapshots</url>
    </repository>

    <dependency>
      <groupId>org.knowm.xchart</groupId>
      <artifactId>xchart</artifactId>
      <version>3.5.3-SNAPSHOT</version>
    </dependency>
```

Snapshots can be manually downloaded from Sonatype: [https://oss.sonatype.org/content/groups/public/org/knowm/xchart/xchart/](https://oss.sonatype.org/content/groups/public/org/knowm/xchart/xchart/)

### SBT

To use XChart with the Scala Build Tool (SBT) add the following to your build.sbt

```scala
libraryDependencies += "org.knowm.xchart" % "xchart" % "3.5.2" exclude("de.erichseifert.vectorgraphics2d", "VectorGraphics2D") withSources()
```

## Building

#### general

    mvn clean package  
    mvn javadoc:aggregate  

#### Formatting

    mvn com.coveo:fmt-maven-plugin:format
    
Formats your code using [google-java-format](https://github.com/google/google-java-format) which follows [Google's code styleguide](https://google.github.io/styleguide/javaguide.html).

If you want your IDE to stick to the same format, check out the available configuration plugins:

#### Eclipse

Download [`google-java-format-eclipse-plugin_*.jar`](https://github.com/google/google-java-format/releases) and place in `/Applications/Eclipse Java.app/Contents/Eclipse/dropins`. Restart Eclipse. Select the plugin in `Preferences > Java > Code Style > Formatter > Formatter Implementation`. 

#### IntelliJ

In the plugins section in IntelliJ search for `google-java-format` and install the plugin. Restart IntelliJ.

## Running Demo

    cd /path/to/xchart-demo/jar/
    java -cp xchart-demo-3.5.2.jar:xchart-3.5.2.jar org.knowm.xchart.demo.XChartDemo

![](https://raw.githubusercontent.com/knowm/XChart/develop/etc/XChart_Demo.png)

## Bugs

Please report any bugs or submit feature requests to [XChart's Github issue tracker](https://github.com/knowm/XChart/issues).  

## Continuous Integration

[![Build Status](https://travis-ci.org/timmolter/XChart.png?branch=develop)](https://travis-ci.org/timmolter/XChart.png)  
[Build History](https://travis-ci.org/timmolter/XChart/builds)  

