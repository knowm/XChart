## [![XChart](https://raw.githubusercontent.com/timmolter/XChart/develop/etc/XChart_64_64.png)](http://knowm.org/open-source/xchart) XChart

A Simple Charting Library for Java

## Description

XChart is a light-weight and convenient library for plotting data.

Its focus is on simplicity and ease-of-use, requiring only two lines of code to save or display a basic default chart.

Usage is very simple: Create a Chart instance, add a series of data to it, and either save it or display it.

## Example

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

Now go ahead and [study some more examples](http://knowm.org/open-source/xchart/xchart-example-code/), [download the thing](http://knowm.org/open-source/xchart/xchart-change-log) and [provide feedback](https://github.com/timmolter/XChart/issues).

## Features

* [x] No required additional dependencies
* [x] ~100KB Jar
* [x] Line charts
* [x] Scatter charts
* [x] Area charts
* [x] Bar charts
* [x] Histogram charts
* [x] Error bars
* [x] Logarithmic axes
* [x] Date and category X-Axis
* [x] Multiple series
* [x] Extensive customization
* [x] Themes - XChart, GGPlot2, MATLAB
* [x] Right-click, Save-As...
* [x] User-defined axes range
* [x] Custom legend placement
* [x] CSV import and export
* [x] High resolution chart export
* [x] Export as PNG, JPG, BMP, GIF and EPS
* [x] Export SVG and PDF using optional de.erichseifert.vectorgraphics2d library
* [x] Real-time charts
* [x] Java 6 and up
 
## Getting Started

### Non-Maven

Download Jar: http://knowm.org/open-source/xchart/xchart-change-log

### Maven

The XChart release artifacts are hosted on Maven Central.

Add the XChart library as a dependency to your pom.xml file:

```xml
    <dependency>
        <groupId>com.xeiam.xchart</groupId>
        <artifactId>xchart</artifactId>
        <version>2.5.0</version>
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
      <groupId>com.xeiam</groupId>
      <artifactId>xchart</artifactId>
      <version>2.5.1-SNAPSHOT</version>
    </dependency>
```

Snapshots can be manually downloaded from Sonatyope: [https://oss.sonatype.org/content/groups/public/com/xeiam/xchart/xchart/](https://oss.sonatype.org/content/groups/public/com/xeiam/xchart/xchart/)

### SBT

To use XChart with the Scala Build Tool (SBT) add the following to your build.sbt

```scala
libraryDependencies += "com.xeiam.xchart" % "xchart" % "2.5.0" exclude("de.erichseifert.vectorgraphics2d", "VectorGraphics2D") withSources()
```
(SBT/Ivy does not seem to respect the optional [VectorGraphics2D](https://github.com/eseifert/vectorgraphics2d) dependency and as it does not exist in Maven Central the build will fail unless it is excluded or available in a local repository.)

## Building

#### general

    mvn clean package  
    mvn javadoc:aggregate  

Note: in order to add support for SVG and PDF exports the optional [VectorGraphics2D](https://github.com/eseifert/vectorgraphics2d) has to be downloaded installed locally as it is not available in Maven Central. Make sure to checkout and build the right version! 

#### maven-license-plugin

    mvn license:check
    mvn license:format
    mvn license:remove

## Running Demo

    cd /path/to/xchart-demo/jar/
    java -cp xchart-demo-2.5.0.jar:xchart-2.5.0.jar com.xeiam.xchart.demo.XChartDemo

## Bugs
Please report any bugs or submit feature requests to [XChart's Github issue tracker](https://github.com/timmolter/XChart/issues).  

## Continuous Integration
[![Build Status](https://travis-ci.org/timmolter/XChart.png?branch=develop)](https://travis-ci.org/timmolter/XChart.png)  
[Build History](https://travis-ci.org/timmolter/XChart/builds)  

## Donations
1PrZHiJorAw7RQrjP9CJgtPuqr6fU65PKt

## Release Information

We will announce new releases on our [Twitter page](https://twitter.com/Knowmorg).
