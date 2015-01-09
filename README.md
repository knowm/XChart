## [![XChart](http://xeiam.com/wp-content/uploads/xchartlogo.png)](http://xeiam.com/xchart) XChart
A Simple Charting Library for Java

## Description
XChart is a light-weight and convenient library for plotting data.

Its focus is on simplicity and ease-of-use, requiring only two lines of code to save or display a basic default chart.

Usage is very simple: Create a Chart instance, add a series of data to it, and either save it or display it.

## Example

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
    
Now go ahead and [study some more examples](http://xeiam.com/xchart-example-code/), [download the thing](http://xeiam.com/xchart-change-log) and [provide feedback](https://github.com/timmolter/XChart/issues).

## Features
* No additional dependencies
* ~100KB Jar
* Line charts
* Scatter charts
* Area charts
* Bar charts
* Histogram charts
* Error bars
* Logarithmic axes
* Date and category X-Axis
* Multiple series
* Extensive customization
* Themes - XChart, GGPlot2, MATLAB
* Right-click, Save-As...
* User-defined axes range
* Custom legend placement
* CSV import and export
* High resolution chart export
* Real-time charts

## Getting Started

### Non-Maven

Download Jar: http://xeiam.com/xchart-change-log

### Maven

The XChart release artifacts are hosted on Maven Central.

Add the XChart library as a dependency to your pom.xml file:

    <dependency>
        <groupId>com.xeiam.xchart</groupId>
        <artifactId>xchart</artifactId>
        <version>2.4.3</version>
    </dependency>

For snapshots, add the following to your pom.xml file:

    <repository>
      <id>sonatype-oss-snapshot</id>
      <snapshots/>
      <url>https://oss.sonatype.org/content/repositories/snapshots</url>
    </repository>
    
    <dependency>
      <groupId>com.xeiam</groupId>
      <artifactId>xchart</artifactId>
      <version>2.4.4-SNAPSHOT</version>
    </dependency>
    
Snapshots can be manually downloaded from Sonatyope: [https://oss.sonatype.org/content/groups/public/com/xeiam/xchart/xchart/](https://oss.sonatype.org/content/groups/public/com/xeiam/xchart/xchart/)

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
    java -cp xchart-demo-2.4.2.jar:xchart-2.4.2.jar com.xeiam.xchart.demo.XChartDemo

## Bugs
Please report any bugs or submit feature requests to [XChart's Github issue tracker](https://github.com/timmolter/XChart/issues).  

## Continuous Integration
[![Build Status](https://travis-ci.org/timmolter/XChart.png?branch=develop)](https://travis-ci.org/timmolter/XChart.png)  
[Build History](https://travis-ci.org/timmolter/XChart/builds)  

## Donations
1PrZHiJorAw7RQrjP9CJgtPuqr6fU65PKt

## Release Information

We will announce new releases on our [Twitter page](https://twitter.com/Xeiam).