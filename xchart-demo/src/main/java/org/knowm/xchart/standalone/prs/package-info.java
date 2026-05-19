/**
 * Standalone runnable demos that correspond to a specific pull request.
 *
 * <p>Naming convention: {@code TestForPRXXX.java} where XXX is the GitHub PR number.
 *
 * <p>Use this package for PRs that introduce new features or behaviour not tied to a specific bug
 * report. PRs that fix a numbered issue should go in the {@code issues} package as {@code
 * TestForIssueXXX.java} named after the issue number.
 *
 * <p>Each class must have:
 *
 * <ul>
 *   <li>A {@code public static void main(String[] args)} that displays the chart via SwingWrapper.
 *   <li>A {@code public static FooChart getChart()} method that constructs and returns the chart
 *       without launching a window (used for headless testing).
 * </ul>
 */
package org.knowm.xchart.standalone.prs;
