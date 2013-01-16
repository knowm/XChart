## XChart
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
    BitmapEncoder.savePNG(chart, "./Sample_Chart.png");
    
Now go ahead and [study some more examples](http://xeiam.com/xchart_examplecode.jsp), [download the thing](http://xeiam.com/xchart_changelog.jsp) and [provide feedback](https://github.com/timmolter/XChart/issues).

## Features
* no additional dependencies
* ~50KB Jar
* line charts
* scatter charts
* error bars
* Date x-axis
* multiple series
* extensive customization

## Getting Started
### Non-Maven
Download Jar: http://xeiam.com/xchart.jsp

### Maven
The XChart release artifacts are hosted on Maven Central.

Add the XChart library as a dependency to your pom.xml file:

    <dependency>
      <groupId>com.xeiam</groupId>
      <artifactId>xchart</artifactId>
      <version>1.3.0</version>
    </dependency>

For snapshots, add the following to your pom.xml file:

    <repository>
      <id>xchart-snapshot</id>
      <snapshots/>
      <url>https://oss.sonatype.org/content/repositories/snapshots</url>
    </repository>
    
    <dependency>
      <groupId>com.xeiam</groupId>
      <artifactId>xchart</artifactId>
      <version>1.3.1-SNAPSHOT</version>
    </dependency>

## Building
mvn clean package  
mvn javadoc:aggregate  

## Running Demo
    cd /path/to/xchart-demo/jar/
    java -cp xchart-demo-1.3.0.jar:xchart-1.3.0.jar com.xeiam.xchart.demo.XChartDemo

## Bugs
Please report any bugs or submit feature requests to [XChart's Github issue tracker](https://github.com/timmolter/XChart/issues).  

## More Info
Sonar Code Quality: http://sonar.xeiam.com/  
Jenkins CI: http://ci.xeiam.com/  

## Donations
17dQktcAmU4urXz7tGk2sbuiCqykm3WLs6  