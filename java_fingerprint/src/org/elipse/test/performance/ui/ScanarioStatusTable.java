package org.eclipse.test.performance.ui;

import java.io.PrintStream;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.test.internal.performance.results.BuildResults;
import org.eclipse.test.internal.performance.results.ConfigResults;
import org.eclipse.test.internal.performance.results.PerformanceResults;
import org.eclipse.test.internal.performance.results.ScenarioResults;

/**
 * Class used to print a scenario status table.
 */
public class ScenarioStatusTable {

	private String component;
	private PrintStream stream;
	private int jsIdCount;

/**
 * Creates an HTML table of red x/green check for a scenario for each
 * configuration.
 */
public ScenarioStatusTable(String  name, PrintStream stream) {
    this.component = name;
    this.stream = stream;
}

/**
 * Prints the HTML representation of scenario status table into the given stream.
 */
public void print(PerformanceResults performanceResults) {
	printTitle();
	List scenarios = performanceResults.getComponentScenarios(this.component);
	String baselineName = performanceResults.getBaselineName();
	int size = scenarios.size();
	printColumnsTitle(size, performanceResults);
	this.jsIdCount = 0;
	for (int i=0; i<size; i++) {
		ScenarioResults scenarioResults = (ScenarioResults) scenarios.get(i);
		this.stream.println("<tr>");
		this.stream.print("<td>");
		boolean hasSummary = scenarioResults.hasSummary();
		if (hasSummary) this.stream.print("<b>");
		String scenarioBaseline = scenarioResults.getBaselineBuildName();
		boolean hasBaseline = baselineName.equals(scenarioBaseline);
		if (!hasBaseline) {
			this.stream.print("*");
			this.stream.print(scenarioResults.getShortName());
			this.stream.print(" <small>(vs. ");
			this.stream.print(scenarioBaseline);
			this.stream.print(")</small>");
		} else {
			this.stream.print(scenarioResults.getShortName());
		}
		if (hasSummary) this.stream.print("</b>");
		this.stream.println();
		String[] configs = performanceResults.getConfigNames(true/*sort*/);
		int length = configs.length;
		for (int j=0; j<length; j++) {
			printConfigStats(scenarioResults, configs[j]);
		}
	}
	this.stream.println("</table>");
}

/*
 * Print the table columns title.
 */
private void printColumnsTitle(int size, PerformanceResults performanceResults) {
	this.stream.println("<table border=\"1\">");
	this.stream.println("<tr>");
	this.stream.print("<td>
All ");
	this.stream.print(size);
	this.stream.println(" scenarios</h4>
