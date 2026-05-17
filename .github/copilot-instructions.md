# XChart – Copilot Instructions

Authoritative guide for AI-assisted work on this repository.
Keep this file updated as new decisions are made.

---

## Project Overview

XChart is a lightweight Java charting library (`org.knowm.xchart`).
Current version: **3.8.9-SNAPSHOT** | Java target: **1.8** | Build: **Maven ≥ 3.9.0**

---

## Module Layout

```
xchart/          ← core library (the only deliverable jar)
xchart-demo/     ← standalone demo app; references xchart as a dep
```

---

## Package Structure

| Package | Purpose |
|---------|---------|
| `org.knowm.xchart` | Public API: chart types, series, builders |
| `org.knowm.xchart.internal.chartpart` | Rendering: `Chart`, `Plot_*`, `PlotContent_*`, `Legend_*`, `Axis*` |
| `org.knowm.xchart.internal.series` | Abstract series base classes |
| `org.knowm.xchart.internal.style` | `SeriesColorMarkerLineStyle`, cycler |
| `org.knowm.xchart.style` | Concrete stylers (`PieStyler`, `CategoryStyler`, …) |
| `org.knowm.xchart.style.theme` | Theme implementations |
| `org.knowm.xchart.style.colors` | Color utilities / `FontColorDetector` |
| `org.knowm.xchart.style.lines` | `SeriesLines` |
| `org.knowm.xchart.style.markers` | `SeriesMarkers` |

---

## Naming Conventions (strict — all chart types follow this)

Every chart type `Foo` requires this exact set of classes:

| Role | Class name | Extends |
|------|-----------|---------|
| Chart | `FooChart` | `Chart<FooStyler, FooSeries>` |
| Builder | `FooChartBuilder` | `ChartBuilder<FooChartBuilder, FooChart>` |
| Styler | `FooStyler` | `Styler` or `AxesChartStyler` |
| Series | `FooSeries` | appropriate series base (see below) |
| Plot | `Plot_Foo<ST, S>` | `Plot_<ST, S>` |
| Plot content | `PlotContent_Foo<ST, S>` | `PlotContent_<ST, S>` |
| Legend | `Legend_Foo<ST, S>` | `Legend_<ST, S>` |

Render style enums live **inside** the series class (e.g. `CategorySeries.CategorySeriesRenderStyle`).

---

## Series Hierarchy

```
Series (abstract)
  └─ AxesChartSeries (abstract) – has xMin/xMax/yMin/yMax, stroke, lineColor
       ├─ AxesChartSeriesNumericalNoErrorBars
       └─ AxesChartSeriesCategory – adds xData, yData, errorBars
            └─ CategorySeries, HeatMapSeries, …
  └─ (direct) PieSeries, DialSeries, RadarSeries, …
```

- `Series.DataType` enum: `Number`, `Date`, `String`
- `calculateMinMax()` **must** be called whenever series data changes

---

## Chart Constructor Pattern (all chart types)

```java
// 1. plain size
public FooChart(int width, int height) {
    super(width, height, new FooStyler());
    axisPair = new AxisPair<>(this);   // omit for non-axes charts (Pie, Dial, Radar)
    plot     = new Plot_Foo<>(this);
    legend   = new Legend_Foo<>(this);
}

// 2. custom Theme instance
public FooChart(int width, int height, Theme theme) {
    this(width, height);
    styler.setTheme(theme);
}

// 3. ChartTheme enum
public FooChart(int width, int height, ChartTheme chartTheme) {
    this(width, height, chartTheme.newInstance(chartTheme));
}

// 4. Builder (always delegates to #3)
public FooChart(FooChartBuilder chartBuilder) {
    this(chartBuilder.width, chartBuilder.height, chartBuilder.chartTheme);
    setTitle(chartBuilder.title);
    // set axis titles etc. from builder fields
}
```

---

## Styler Pattern

```java
// Field (private, initialized in setAllStyles())
private SomeType fieldName;

// Getter — blank line before return
public SomeType getFieldName() {

  return fieldName;
}

// Setter — Javadoc with @param, fluent return this
/**
 * One-sentence description.
 *
 * @param fieldName description
 */
public FooStyler setFieldName(SomeType fieldName) {

  this.fieldName = fieldName;
  return this;
}
```

- All fields **must** be initialized in `setAllStyles()`, pulling values from `theme` where applicable.
- `setAllStyles()` must call `super.setAllStyles()` first.
- Never add state to the base `Styler` class unless it truly applies to every chart type.

---

## Code Formatting

- **Formatter**: `fmt-maven-plugin` (google-java-format).
- **Check only**: `mvn fmt:check` (used in CI). Run this to detect violations.
- **NEVER run `mvn fmt:format` project-wide.** It reformats every file, pollutes diffs, and
  causes merge conflicts across all open branches. If a PR has violations, ask the contributor
  to run `mvn fmt:format` on their own changed files only and push a fix commit.
- Indentation: **2 spaces** (google style).
- Imports: sorted by google-java-format; **no wildcard imports** in `src/main`. Tests may use `import static … Assertions.*`.
- One blank line between class members; blank line before `return` in getters.
- **Always** end files with a newline character.

---

## Build & Test

```bash
# Full build + tests
mvn clean verify

# Format source
mvn fmt:format

# Tests only
mvn test -pl xchart

# Skip tests
mvn install -DskipTests
```

Tests use **JUnit Jupiter 5** + **AssertJ**.

### Test patterns

```java
@BeforeEach
void setUp() { chart = new FooChart(800, 600, GGPlot2); }

@Test
void descriptiveCamelCaseNameNoTestPrefix() {
    assertAll(
        () -> assertDoesNotThrow(...),
        () -> assertThat(series.getName()).isEqualTo("expected")
    );
}

// Exception testing
assertThatThrownBy(() -> { ... })
    .isInstanceOf(IllegalArgumentException.class)
    .hasMessageMatching("Series name >x< ...");
```

- Tests go in `xchart/src/test/java/org/knowm/xchart/` mirroring the main package.
- One test class per chart type or internal component.
- **New public API must include tests.**

---

## High-Risk Areas (Ripple Effect Zones)

Changes in these files affect **every** chart type — review with extra care:

| File | Impact |
|------|--------|
| `Chart.java` | Base of all charts |
| `Styler.java` | Base of all stylers |
| `Series.java` | Base of all series |
| `AxisPair.java` | All axes-based charts |
| `Axis.java` | All axes-based charts |
| `PlotContent_.java` | All PlotContent renderers |
| `Plot_.java` | All Plot wrappers |
| `Legend_.java` | All legends |
| `Utils.java` | Used across many renderers |
| `CSVImporter.java` | Public import API |

Any PR touching these files should be tested by painting **multiple** chart types, not just the one the PR targets.

---

## Demo / Standalone Test Convention

Every code PR should be accompanied by a standalone runnable demo that exercises the fix or feature.

| PR type | Package | Naming |
|---------|---------|--------|
| Fixes a numbered issue | `xchart-demo/…/standalone/issues/` | `TestForIssueXXX.java` (XXX = issue number) |
| New feature, no specific issue | `xchart-demo/…/standalone/prs/` | `TestForPRXXX.java` (XXX = PR number) |

### Required structure for every demo class

```java
package org.knowm.xchart.standalone.prs; // or .issues

public class TestForPRXXX {

  public static void main(String[] args) {
    new SwingWrapper<>(getChart()).displayChart();
  }

  /** Constructs and returns the chart without launching a window (headless-safe). */
  public static FooChart getChart() {
    // build and return chart
  }
}
```

- `getChart()` must be `public static` so it can be called from unit tests without a display.
- No Swing/AWT calls outside of `main()`.
- Separate `getChartXxx()` methods when demonstrating multiple variants of the same feature.

---

## PR Review Criteria

When evaluating a PR, rank it higher if it:

1. Passes `mvn fmt:check` (no formatting violations).
2. Follows the exact naming conventions above.
3. Adds or updates tests for new/changed behaviour.
4. Targets only the chart type it claims to change (no incidental changes to base classes).
5. Is free of wildcard imports in `src/main`.
6. Has a `\n` at the end of every changed file.
7. Does not set sibling state as a side-effect of a setter (e.g. a setter for field A silently nulling field B is fragile).
8. Uses the existing `seriesMap` (LinkedHashMap — insertion order is meaningful) iteration patterns.

Lower-risk PRs: single-field styler additions, dependency bumps with passing CI.
Higher-risk PRs: new chart types, changes to rendering base classes, refactors of `Utils`/`CSVImporter`.

---

## Open PR Decisions Log

_Last updated: 2026-05-17_

### Dependabot — safe to merge (CI green, CLEAN)
| PR | Bump | Decision |
|----|------|----------|
| #911 | javafx-controls 11 → 26-ea+13 | ⏳ pending |
| #901 | maven-javadoc-plugin 3.10 → 3.12 | ⏳ pending |
| #899 | maven-surefire-plugin 3.5.0 → 3.5.4 | ⏳ pending |
| #897 | pdfbox-graphics2d 3.0.2 → 3.0.5 | ⏳ pending |
| #894 | assertj-core 3.26 → 3.27.6 | ⏳ pending |
| #871 | maven-release-plugin 3.0.1 → 3.1.1 | ⏳ pending |
| #863 | maven-jar-plugin 3.4.1 → 3.4.2 | ⏳ pending |

### Dependabot — failing CI (do not merge yet)
| PR | Bump | Reason |
|----|------|--------|
| #896 | JUnit Jupiter 5.10 → **6.0.0** | ❌ Closed — breaking major version; staying on JUnit 5.x |
| #895 | fmt-maven-plugin 2.24 → 2.29 | Formatter upgrade may reformat all source — skip until a dedicated cleanup PR is planned |
| #912 | javafx-swing 11 → 26-**ea**+13 | Early-access build; CI failure |

### Code PRs — review queue (priority order)
| PR | Title | State | Analysis status |
|----|-------|-------|----------------|
| #847 | Custom pie chart label generator | MERGEABLE / CLEAN / APPROVED | ⏳ in review — demo: `TestForPR847.java` |
| #864 | Fix #593 | MERGEABLE / CLEAN | ✅ merged — demo: `TestForIssue593.java` |
| #866 | Horizontal bar chart (new type) | MERGEABLE / CLEAN | ✅ merged — demo: `TestForPR866.java` |
| #888 | Fix chart-update freeze (#886) | **CONFLICTING** — needs rebase | ✅ merged — cherry-picked 2 Java files, excluded pom noise — demo: `TestForIssue886.java` |
| #885 | Refactor code smells | MERGEABLE / CLEAN | ⏳ not started |
| #913 | Decompose Utils + CSVImporter (SRP) | MERGEABLE / CLEAN | ⏳ not started |
| #917 | Grouped/merged Y-axes | UNSTABLE | ⏳ not started |

### Stale / draft — likely close
| PR | Notes |
|----|-------|
| #641 | Draft for 3 years; may be superseded |
| #520 | 5+ years old; mergeable state unknown |
| #314 | 6+ years old; large XYSeries refactor; likely needs full rewrite |
