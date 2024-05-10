---
name: Bug report
about: Create a report to help us improve
title: ''
labels: ''
assignees: ''

---

**Describe the bug**
A clear and concise description of what the bug is.

**To Reproduce**
Please make a code snippet demonstrating the bug using the following sample chart code:
```
public class TestForIssue834 {

  public static void main(String[] args) throws ParseException {

    XYChart chart = getXYChart();
    new SwingWrapper(chart).displayChart();
  }

  public static XYChart getXYChart() {
    XYChart chart =
        new XYChartBuilder()
            .width(720)
            .height(480)
            .title("Buggy Example")
            .xAxisTitle("Count")
            .yAxisTitle("Value")
            .build();


    double[] xValues = new double[] {1, 2, 3};
    double[] yValues = new double[] {1, 2, 3};
    chart.addSeries("main", xValues, yValues);

    return chart;
  }
}
```

**Screenshots**
Add screenshots to help explain your problem.


**Expected behavior**
A clear and concise description of what you expected to happen.
