# XChart General Refactor Plan

## Problem Statement

XChart has accumulated ~10 years of structural debt. A high-level code audit identified ~38 issues
across architecture, generics, null handling, duplication, naming, and testability.
This plan captures the major problem areas and proposes a prioritized refactor roadmap.

---

## Findings Summary

### HIGH Severity

**H1 — Axis fields in base `Chart` class (Liskov violation)**

`Chart.java` contains `axisPair`, `xAxisTitle`, `yAxisTitle`, and `yAxisGroupTitleMap` even though
PieChart, RadarChart, and DialChart have no axes. This forces defensive `axisPair != null` checks
throughout the codebase and breaks LSP. A proper `AxesChart<ST, S>` abstract subclass should hold
all axis-related state, and non-axes charts should extend `Chart` directly.

**H2 — God-class Stylers**

`Styler.java` (1013 lines, 100+ fields) and `AxesChartStyler.java` (1056 lines, 50+ more fields)
are monolithic property bags. They handle chart-level, legend, tooltip, annotation, button, and
axis styling all in one class. Adding any new styling property requires touching multiple files.
Decompose into focused sub-stylers (e.g., `LegendStyler`, `TooltipStyler`, `AnnotationStyler`).

**H3 — Massive `addSeries()` overload explosion**

`XYChart` has ~15+ `addSeries()` overloads for every combination of `double[]`, `float[]`, `int[]`,
`List<Number>`, `List<Date>`, etc. `CategoryChart` has ~12+. This is ~30% of chart code. A series
builder / factory pattern would collapse this drastically.

---

### MEDIUM Severity

**M1 — Deep inheritance chains in Series hierarchy**

5-level deep chain:
`Series → AxesChartSeries → MarkerSeries → AxesChartSeriesNumericalNoErrorBars → XYSeries`.
Changes to any intermediate class ripple everywhere. Composition over inheritance should be
explored.

**M2 — Pervasive unchecked casts (`@SuppressWarnings("unchecked")`)**

`Chart.java`, `ChartBuilder.java`, `Cursor.java`, `Axis_X.java`, `XChartPanel.java` all suppress
unchecked cast warnings. This defeats the type system and risks runtime ClassCastExceptions.

**M3 — Misleading class name: `AxesChartSeriesNumericalNoErrorBars`**

Despite the name, this class contains `double[] extraValues` and `findMinMaxWithErrorBars()`.
The class itself has a comment calling this out: "weird name of class."

**M4 — Null used as sentinel for "use styler default"**

`XYSeries.xySeriesRenderStyle = null` is an implicit contract meaning "inherit from styler."
This is an invisible design contract that's easy to break. Use `Optional<>` or explicit delegation.

**M5 — Duplicate data storage for zoom support**

Series classes maintain both full data (`xDataAll`, `yDataAll`) and filtered/zoomed data
(`xData`, `yData`). For large datasets this doubles memory. A view/range abstraction would be
cleaner.

**M6 — High cyclomatic complexity in rendering**

`PlotContent_Category_Bar.java` is 680 lines with deeply nested conditionals for handling
different series types, error bars, and styles. Should be decomposed.

**M7 — Theme is mutable after chart creation**

`setTheme()` can be called on a live Styler, potentially affecting multiple charts. Themes should
be immutable value objects applied at construction time.

**M8 — Calculation mixed with rendering in Series**

Series classes mix data storage, min/max calculation, zoom filtering, and rendering hints. The
calculation logic should be separated so it can be tested independently.

**M9 — Unresolved TODO debt (23+ instances)**

Documented architectural TODOs in `Chart.java` (axis fields), `Series.java`, `Styler.java`,
`AxesChartSeriesNumericalNoErrorBars.java`, `ToolTips.java`, and `BoxChart.java`.

**M10 — `getSeriesMap()` creates new unmodifiable wrapper on every call**

Minor but avoidable per-call allocation; wrap once at mutation time.

---

### LOW Severity

**L1 — Magic numbers scattered in rendering constants**

`Legend_.java` has `BOX_SIZE=20`, `BOX_OUTLINE_WIDTH=5`, `LEGEND_MARGIN=6`, etc. inline.
Should be named constants in a single location.

**L2 — Typos in error messages**

`BoxChart.java`: `"connot be null"`, `"connot be empyt"` → `"cannot be null"`,
`"cannot be empty"`.

**L3 — Commented-out code and orphaned `System.out.println`**

Cleanup pass needed.

**L4 — Inconsistent fluent setter coverage**

Some series setters return `this`, others don't. Should be uniform.

---

## Proposed Refactor Phases

### Phase 1 — Low-hanging fruit (no API breakage)

- Fix typos in error messages (BoxChart and any others)
- Remove commented-out code / dead `System.out.println`
- Rename `AxesChartSeriesNumericalNoErrorBars` to something accurate
- Collect all rendering magic numbers into named constants
- Resolve straightforward TODOs

### Phase 2 — Series & data layer

- Rename series class hierarchy to accurate names
- Introduce `Optional<SeriesRenderStyle>` instead of null sentinel
- Separate min/max calculation from series data storage
- Explore replacing duplicate zoom data with a view/range object

### Phase 3 — Styler decomposition

- Extract `LegendStyler`, `TooltipStyler`, `AnnotationStyler` sub-objects
- Make `Theme` an immutable value type; remove `setTheme()` from Styler

### Phase 4 — Chart class hierarchy (highest risk)

- Introduce `AxesChart<ST extends AxesChartStyler, S extends AxesChartSeries>` between `Chart`
  and the axes-based chart types
- Move `axisPair`, axis titles, `yAxisGroupTitleMap` out of base `Chart`
- Remove all defensive `axisPair != null` checks from base class

### Phase 5 — `addSeries()` overload reduction (public API change)

- Design a `SeriesBuilder` / factory that accepts data generically
- Collapse the 15+ overloads per chart type
- Requires a deprecation cycle before removing old overloads

### Phase 6 — Rendering decomposition

- Break up `PlotContent_Category_Bar` and similar large renderers
- Separate data computation from `Graphics2D` drawing calls (aids testability)

---

## Risk Notes

- **Phase 4** is the highest risk: `Chart` is the base of everything; all chart types must be
  retested
- **Phase 5** is a public API change — requires a deprecation cycle
- **Phases 1–3** can be done incrementally without breaking anything public
