Notes:

LinkedBar - a Bar chart with a solid outline connecting data as one shape instead of separate bars.

LinkedBar functionality has been implemented using a LegendRenderStyle LinkedBar (instead of Bar, Line, Scatter)
LinkedBar can be filled-in with color or not filled in (series.setFillColor())

Changes

+ 3 new demo's (LinkedBar, LinkedBar02, LinkedBar03) edited from regular bar demos
+ Added LinkedBar RenderStyle in Series_Category.java
+ Updated Plot_Category.java
+ Added LinkedBar paint function to PlotContent_Category_Bar.java
 + New variables - prevyOffshoot and prevxOffshoot (to know where to draw the outline)


To-do

- Mirror the same changes in Series_XY and
- (Optional) Draw gaussian curve over bar chart

Issues

- LinkedBar doesn't render properly due to the way bar charts are drawn; see pic
- Outline is kinda ugly 

![LinkedBar](https://raw.githubusercontent.com/billyshea/XChart/develop/LinkedBar.png)
![LinkedBar02](https://raw.githubusercontent.com/billyshea/XChart/develop/LinkedBar02.png)
![LinkedBar03](https://raw.githubusercontent.com/billyshea/XChart/develop/LinkedBar03.png)
