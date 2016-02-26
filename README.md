## [![XChart](https://raw.githubusercontent.com/timmolter/XChart/develop/etc/XChart_64_64.png)](http://knowm.org/open-source/xchart) XChart

XChart is a light weight Java library for plotting data.

## Description

XChart is a light-weight and convenient library for plotting data designed to go from data to chart in the least amount of time possible and to take the guess-work out of customizing the chart style.

## Simplest Example

Create a `Chart` instance via `QuickChart`, add a series of data to it, and either display it or save it as a bitmap.  

```java

    double[] xData = new double[] { 0.0, 1.0, 2.0 };
    double[] yData = new double[] { 2.0, 1.0, 0.0 };

    // Create Chart
    Chart chart = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", xData, yData);

    // Show it
    new SwingWrapper(chart).displayChart();

    // Save it
    BitmapEncoder.saveBitmap(chart, "./Sample_Chart", BitmapFormat.PNG);

    // or save it in high-res
    BitmapEncoder.saveBitmapWithDPI(chart, "./Sample_Chart_300_DPI", BitmapFormat.PNG, 300);
```

![](https://raw.githubusercontent.com/timmolter/XChart/develop/etc/XChart_Simplest.png)

## Intermediate Example

Create a `Chart` via a `ChartBuilder`, style chart, add a series to it, style series, and display chart.

```java

    // Create Chart
    Chart_XY chart = new ChartBuilder_XY().width(600).height(500).title("Gaussian Blobs").xAxisTitle("X").yAxisTitle("Y").build();

    // Customize Chart
    chart.getStyler().setDefaultSeriesRenderStyle(ChartXYSeriesRenderStyle.Scatter);
    chart.getStyler().setChartTitleVisible(false);
    chart.getStyler().setLegendPosition(LegendPosition.InsideSW);
    chart.getStyler().setMarkerSize(16);

    // Series
    chart.addSeries("Gaussian Blob 1", getGaussian(1000, 1, 10), getGaussian(1000, 1, 10));
    Series_XY series = chart.addSeries("Gaussian Blob 2", getGaussian(1000, 1, 10), getGaussian(1000, 0, 5));
    series.setMarker(SeriesMarkers.DIAMOND);

    new SwingWrapper(chart).displayChart();
```

![](https://raw.githubusercontent.com/timmolter/XChart/develop/etc/XChart_Intermediate.png)

## Advanced Example

Create a `Chart` via a `ChartBuilder`, style chart, add a series to it, add chart to `XChartPanel`, embed in Java Swing App, and display GUI.

```java
    // Create Chart
    final Chart_XY chart = new ChartBuilder_XY().width(600).height(400).title("Area Chart").xAxisTitle("X").yAxisTitle("Y").build();

    // Customize Chart
    chart.getStyler().setLegendPosition(LegendPosition.InsideNE);
    chart.getStyler().setDefaultSeriesRenderStyle(ChartXYSeriesRenderStyle.Area);

    // Series
    chart.addSeries("a", new double[] { 0, 3, 5, 7, 9 }, new double[] { -3, 5, 9, 6, 5 });
    chart.addSeries("b", new double[] { 0, 2, 4, 6, 9 }, new double[] { -1, 6, 4, 0, 4 });
    chart.addSeries("c", new double[] { 0, 1, 3, 8, 9 }, new double[] { -2, -1, 1, 0, 1 });

    // Create and set up the window.
    final JFrame frame = new JFrame("Advanced Example");

    // Schedule a job for the event-dispatching thread:
    // creating and showing this application's GUI.
    javax.swing.SwingUtilities.invokeLater(new Runnable() {

      @Override
      public void run() {

        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS)); // <-- you need this for now

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel chartPanel = new XChartPanel(chart);
        frame.add(chartPanel);

        JLabel label = new JLabel("Blah blah blah.", SwingConstants.CENTER);

        frame.add(label);

        // Display the window.
        frame.pack();
        frame.setVisible(true);
      }
    });
```

![](https://raw.githubusercontent.com/timmolter/XChart/develop/etc/XChart_Advanced.png)


To make it real-time, simply call `updateSeries` on the `XChartPanel` instance.

## Features

* [x] No required additional dependencies
* [x] ~164KB Jar
* [x] Line charts
* [x] Scatter charts
* [x] Area charts
* [x] Bar charts
* [x] Histogram charts
* [x] Pie charts
* [x] Error bars
* [x] Logarithmic axes
* [x] Number, Date and Category X-Axis
* [x] Multiple series
* [x] Extensive customization
* [x] Themes - XChart, GGPlot2, Matlab
* [x] Right-click, Save-As...
* [x] User-defined axes range
* [x] Custom legend placement
* [x] CSV import and export
* [x] High resolution chart export
* [x] Export as PNG, JPG, BMP, GIF with custom DPI setting
* [x] Export SVG, EPS and PDF using optional de.erichseifert.vectorgraphics2d library
* [x] Real-time charts
* [x] Java 6 and up

## Chart Types

Currently, there are three major chart types: `Chart_XY`, `Chart_Category` and `Chart_Pie`. Each type has its corresponding `ChartBuilder`, `Styler` and `Series`.

| Chart Type | Builder | Styler | Series | Allowed X-Axis Data Types | Default Series Render Style |
|---|---|---|---|---|---|
| Chart_XY | ChartBuilder_XY | Styler_XY | Series_XY | Number, Date | Line |
| Chart_Category | ChartBuilder_Category | Styler_Category | Series_Category | Number, Date, String | Bar |
| Chart_Pie | ChartBuilder_Pie | Styler_Pie | Series_Pie | String | Pie |

The different Stylers contain chart styling methods specific to the corresponding chart type as well as common styling methods common across all chart types.

### Chart_XY

![](https://raw.githubusercontent.com/timmolter/XChart/develop/etc/Chart_XY.png)

`Chart_XY` charts take Date or Number data types for the X-Axis and Number data types for the Y-Axis. For both axes, the tick marks are auto generated to span the range and domain of the data in evenly-spaced intervals. 

Series render styles include: `Line`, `Scatter` and `Area`.

### Chart_Category 

![](https://raw.githubusercontent.com/timmolter/XChart/develop/etc/Chart_Category.png)

`Chart_Category` charts take Date, Number or String data types for the X-Axis and Number data types for the Y-Axis. For the X-Axis, each category is given its own tick mark.  

Series render styles include: `Bar`, `Line`, `Scatter`, `Area` and `Stick`.

### Chart_Pie

![](https://raw.githubusercontent.com/timmolter/XChart/develop/etc/Chart_Pie.png)

`Chart_Pie` charts take String data types for the pie slice name and Number data types for the pie slice value.  

Series render styles include: `Pie` and in the near future `Donut`.

## Chart Customization

All the styling options can be found in one of two possible places: 1) the Chart's `Styler` or 2) the series' `set` methods. With this chart customization design, all customization options can be quickly "discovered" using an IDE's built in "Content Assist". With centralized styling like this, there is no need to hunt around the entire charting API to find that one customization you're looking for - it's all right in one spot!

![](https://raw.githubusercontent.com/timmolter/XChart/develop/etc/XChart_Chart_Customization.png)
 
![](https://raw.githubusercontent.com/timmolter/XChart/develop/etc/XChart_Series_Customization.png)
 
## Chart Themes

XChart ships with three different themes: Default `XChart`, `GGPlot2` and `Matlab`. Using a different theme is as simple as setting the Chart's theme with the `theme` method of the `ChartBuilder`.

    Chart_XY chart = new ChartBuilder_XY().width(800).height(600).theme(ChartTheme.Matlab).build();

![](https://raw.githubusercontent.com/timmolter/XChart/develop/etc/XChart_Themes.png)

## What's Next?

Now go ahead and [study some more examples](http://knowm.org/open-source/xchart/xchart-example-code/), [download the thing](http://knowm.org/open-source/xchart/xchart-change-log) and [provide feedback](https://github.com/timmolter/XChart/issues).
 
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
        <version>3.0.2</version>
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
      <version>3.0.3-SNAPSHOT</version>
    </dependency>
```

Snapshots can be manually downloaded from Sonatyope: [https://oss.sonatype.org/content/groups/public/org/knowm/xchart/xchart/](https://oss.sonatype.org/content/groups/public/org/knowm/xchart/xchart/)

### SBT

To use XChart with the Scala Build Tool (SBT) add the following to your build.sbt

```scala
libraryDependencies += "org.knowm.xchart" % "xchart" % "3.0.1" exclude("de.erichseifert.vectorgraphics2d", "VectorGraphics2D") withSources()
```
(SBT/Ivy does not seem to respect the optional [VectorGraphics2D](https://github.com/eseifert/vectorgraphics2d) dependency and as it does not exist in Maven Central the build will fail unless it is excluded or available in a local repository.)

## Building

#### general

    mvn clean package  
    mvn javadoc:aggregate  

#### maven-license-plugin

    mvn license:check
    mvn license:format
    mvn license:remove

## Running Demo

    cd /path/to/xchart-demo/jar/
    java -cp xchart-demo-3.0.2.jar:xchart-3.0.2.jar org.knowm.xchart.demo.XChartDemo

![](https://raw.githubusercontent.com/timmolter/XChart/develop/etc/XChart_Demo.png)

## Bugs

Please report any bugs or submit feature requests to [XChart's Github issue tracker](https://github.com/timmolter/XChart/issues).  

## Continuous Integration

[![Build Status](https://travis-ci.org/timmolter/XChart.png?branch=develop)](https://travis-ci.org/timmolter/XChart.png)  
[Build History](https://travis-ci.org/timmolter/XChart/builds)  

## Donations

Donate with Bitcoin: [1JVyTP9v9z54dALuhDTZDQfS6FUjcKjPgZ](https://blockchain.info/address/1JVyTP9v9z54dALuhDTZDQfS6FUjcKjPgZ)

All donations will be used to pay bounties for new features, refactoring, etc. Please consider donating or even posting your own bounties on our [Issues Page](https://github.com/timmolter/xchart/issues?state=open). Open bounties and bounties paid thus far can be found on Knowm's [bounties](http://knowm.org/open-source/) page.

## Release Information

We will announce new releases on our [Twitter page](https://twitter.com/Knowmorg).
