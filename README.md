Description
===============

XChart is a charting API for Java. While several charting APIs already
exist, most of them require a steep learning curve and the code base
is bloated, full of too many "features" and/or very outdated. 
Inspired by SWTCharts, simplicity is prioritized over everything else 
so you can quickly make a plot and move on. XCharts can be used in
your Swing, Java EE, and Java SE applications. If you're looking for
a Java charting API that is easy to use and does not need every feature
under the sun, XChart is for you.

Getting Started
===============

Non-Maven
---------
Download Jar: http://xeiam.com/xchart.jsp

The XChart release artifacts are hosted on Maven Central. Otherwise if you need the latest snapshots, they are hosted at oss.sonatype.org. Add the following repository to your pom.xml file.

    <repository>
      <id>xchart-snapshot</id>
      <snapshots/>
      <url>https://oss.sonatype.org/content/repositories/snapshots</url>
    </repository>
  
Add this dependency to your pom.xml file:

    <dependency>
      <groupId>com.xeiam</groupId>
      <artifactId>xchart</artifactId>
      <version>1.2.1-SNAPSHOT</version>
    </dependency>

Building
===============
mvn clean package  
mvn javadoc:javadoc  

Donations
===============
17dQktcAmU4urXz7tGk2sbuiCqykm3WLs6