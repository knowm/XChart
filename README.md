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

Maven
-----
The XChart Java artifacts are currently hosted on the Xeiam Nexus repository here:

    <repositories>
      <repository>
        <id>xchange-release</id>
        <releases/>
        <url>http://nexus.xeiam.com/content/repositories/releases</url>
      </repository>
      <repository>
        <id>xchange-snapshot</id>
        <snapshots/>
        <url>http://nexus.xeiam.com/content/repositories/snapshots/</url>
      </repository>
    </repositories>

Building
===============
mvn clean package
mvn javadoc:javadoc