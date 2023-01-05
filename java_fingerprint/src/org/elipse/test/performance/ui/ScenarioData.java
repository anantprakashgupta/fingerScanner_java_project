package org.eclipse.test.performance.ui;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.AssertionFailedError;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Display;
import org.eclipse.test.internal.performance.data.Dim;
import org.eclipse.test.internal.performance.data.DimensionMessages;
import org.eclipse.test.internal.performance.results.AbstractResults;
import org.eclipse.test.internal.performance.results.BuildResults;
import org.eclipse.test.internal.performance.results.ComponentResults;
import org.eclipse.test.internal.performance.results.ConfigResults;
import org.eclipse.test.internal.performance.results.PerformanceResults;
import org.eclipse.test.internal.performance.results.ScenarioResults;

/**
 * Class used to print scenario all builds data.
 */
public class ScenarioData {
	private String baselinePrefix = null;
	private List pointsOfInterest;
	private List buildIDStreamPatterns;
	private File rootDir;
	private static final int GRAPH_WIDTH = 600;
	private static final int GRAPH_HEIGHT = 200;

/**
 * Summary of results for a scenario for a given build compared to a
 * reference.
 *
 * @param baselinePrefix The prefix of the baseline build names
 * @param pointsOfInterest A list of buildId's to highlight on line graphs
 * @param buildIDPatterns
 * @param outputDir The directory root where the files are generated
 *
*/
public ScenarioData(String baselinePrefix, List pointsOfInterest, List buildIDPatterns, File outputDir) {
	this.baselinePrefix = baselinePrefix;
	this.pointsOfInterest = pointsOfInterest;
	this.buildIDStreamPatterns = buildIDPatterns;
	this.rootDir = outputDir;
}

/**
 * Print the scenario all builds data from the given performance results.
 * 
 * @param performanceResults The needed information to generate scenario data
 */
public void print(PerformanceResults performanceResults) {
	String[] configNames = performanceResults.getConfigNames(false/*not sorted*/);
	String[] configBoxes = performanceResults.getConfigBoxes(false/*not sorted*/);
	int length = configNames.length;
	for (int i=0; i<length; i++) {
		File outputDir = new File(this.rootDir, configNames[i]);
		outputDir.mkdir();
		Iterator components = performanceResults.getResults();
		while (components.hasNext()) {
			ComponentResults componentResults = (ComponentResults) components.next();
			printSummary(configNames[i], configBoxes[i], componentResults, outputDir);
			printDetails(configNames[i], configBoxes[i], componentResults, outputDir);
		}
	}
}

/*
 * Print the summary file of the builds data.
 */
private void printSummary(String configName, String configBox, ComponentResults componentResults, File outputDir) {
	Iterator scenarios = componentResults.getResults();
	while (scenarios.hasNext()) {
		List highlightedPoints = new ArrayList();
		ScenarioResults scenarioResults = (ScenarioResults) scenarios.next();
		ConfigResults configResults = scenarioResults.getConfigResults(configName);
		if (configResults == null || !configResults.isValid()) continue;

		// get latest points of interest matching
		if (this.pointsOfInterest != null) {
			Iterator buildPrefixes = this.pointsOfInterest.iterator();
			while (buildPrefixes.hasNext()) {
				String buildPrefix = (String) buildPrefixes.next();
				List builds = configResults.getBuilds(buildPrefix);
				if (buildPrefix.indexOf('*') <0 && buildPrefix.indexOf('?') < 0) {
					if (builds.size() > 0) {
						highlightedPoints.add(builds.get(builds.size()-1));
					}
				} else {
					highlightedPoints.addAll(builds);
				}
			}
		}

		String scenarioFileName = scenarioResults.getFileName();
		File outFile = new File(outputDir, scenarioFileName + ".html");
		PrintStream stream = null;
		try {
			stream = new PrintStream(new BufferedOutputStream(new FileOutputStream(outFile)));
		} catch (FileNotFoundException e) {
			System.err.println("can't create output file" + outFile); //$NON-NLS-1$
		}
		if (stream == null) {
			stream = System.out;
		}
		stream.println(Utils.HTML_OPEN);
		stream.println(Utils.HTML_DEFAULT_CSS);

		stream.println("<title>" + scenarioResults.getName() + "(" + configBox + ")" + ""); //$NON-NLS-1$
		stream.println("<h4>Scenario: " + scenarioResults.getName() + " (" + configBox + ")
"); //$NON-NLS-1$ //$NON-NLS-2$

		String failureMessage = Utils.failureMessage(configResults.getCurrentBuildDeviation(), true);
 		if (failureMessage != null){
   			stream.println("<table>
