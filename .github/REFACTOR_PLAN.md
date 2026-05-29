# XChart Refactor Plan — Final Status

This document tracks the refactor initiative that ran across PRs #957–#961.
It is kept for historical reference and to explain decisions made along the way.

---

## Completed Work

| PR | Phase | Summary |
|----|-------|---------|
| [#957](https://github.com/knowm/XChart/pull/957) | Phase 1 — Clean-up | Fixed typos in `BoxChart`; removed dead code; renamed `AxesChartSeriesNumericalNoErrorBars` → `AxesChartSeriesNumerical` |
| [#958](https://github.com/knowm/XChart/pull/958) | Phase 2 — Optional render style | Replaced null sentinels with `Optional<RenderStyle>` in all series classes; extracted `SeriesMinMaxCalculator` helper with unit tests |
| [#959](https://github.com/knowm/XChart/pull/959) | Theme API | Added `.theme(Theme)` fluent method to `ChartBuilder`; deprecated `setTheme()` on all stylers; updated `ThemeChart04` demo |
| [#960](https://github.com/knowm/XChart/pull/960) | Phase 4 — AxesChart abstraction | Introduced `AxesChart` abstract subclass; moved all axis state out of the base `Chart` class; removed all `axisPair == null` null guards; eliminated unchecked casts |
| [#961](https://github.com/knowm/XChart/pull/961) | Phase 6 — PlotContent extraction | Renamed `PlotContent_Category_Bar` → `PlotContent_Category`; extracted `computeBarDimensions`, `processSteppedBarDataPoint`, `finalizeSteppedBar` from `doPaint()`; extracted `paintErrorBar()` from `PlotContent_XY` |

---

## Deliberately Skipped

| Phase | Reason |
|-------|--------|
| Phase 3 — Styler decomposition | The monolithic styler is intentional. IDE autocomplete discoverability is a core UX feature of the library; sub-stylers would fragment that experience and make the API harder to discover. |
| Phase 5 — `addSeries` overload reduction | The primitive array overloads (`double[]`, `float[]`, `int[]`) are deliberate convenience API. They lower the barrier to entry for new users and are not a problem to maintain. |

---

## Open Items (future work)

| Item | Notes |
|------|-------|
| Series inheritance chain | The chain is currently 5 levels deep. Could be explored in a future refactor, but requires careful analysis of downstream impact. |
| Fluent setter uniformity | ✅ Done — all series setters now return `this` for chaining (PRs: `BubbleSeries`, `DialSeries`, `PieSeries`, `RadarSeries`, `XYSeries`). |
| Zoom data duplication | `filterXByValue` operates on non-contiguous ranges. A clean `DataRange` abstraction would be the right fix but requires more invasive changes across the rendering pipeline. |

---

_Last updated: 2026-05-29_
