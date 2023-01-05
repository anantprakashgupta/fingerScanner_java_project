package org.eclipse.test.performance.ui;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.test.internal.performance.data.Dim;
import org.eclipse.test.internal.performance.results.AbstractResults;
import org.eclipse.test.internal.performance.results.BuildResults;
import org.eclipse.test.internal.performance.results.ConfigResults;

/**
 * Class used to fill details file of scenario builds data.
 * @see ScenarioData
 */
public class RawDataTable {

	private ConfigResults configResults;
	private List buildPrefixes;
	private PrintStream stream;
	private Dim[] dimensions = AbstractResults.SUPPORTED_DIMS;
	private boolean debug = false;

private RawDataTable(ConfigResults results, PrintStream ps) {
	this.configResults = results;
	this.stream = ps;
}

public RawDataTable(ConfigResults results, List prefixes, PrintStream ps) {
	this(results, ps);
	this.buildPrefixes = prefixes;
}
public RawDataTable(ConfigResults results, String baselinePrefix, PrintStream ps) {
	this(results, ps);
	this.buildPrefixes = new ArrayList();
	this.buildPrefixes.add(baselinePrefix);
}

/**
 * Print all build data to the current stream.
 */
public void print(){
	stream.print("<table border=\"1\">");
	printSummary();
	printDetails();
	stream.println("</table>");
}

/*
 * Print table columns headers.
 */
private void printColumnHeaders() {
	StringBuffer buffer = new StringBuffer();
	int length = this.dimensions.length;
	for (int i=0; i<length; i++) {
		buffer.append("<td>");
		buffer.append(this.dimensions[i].getName());
		buffer.append("</b>
