package org.eclipse.test.performance.ui;

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.test.internal.performance.PerformanceTestPlugin;
import org.eclipse.test.internal.performance.results.AbstractResults;
import org.eclipse.test.internal.performance.results.ConfigResults;
import org.eclipse.test.internal.performance.results.DB_Results;
import org.eclipse.test.internal.performance.results.PerformanceResults;
import org.eclipse.test.internal.performance.results.ScenarioResults;
import org.osgi.framework.Bundle;

/**
 * Main class to generate performance results of all scenarios matching a given pattern
 * in one HTML page per component.
 * 
 * @see #printUsage() method to see a detailed parameters usage
 */
public class Main implements IApplication {

/**
 * Prefix of baseline builds displayed in data graphs.
 * This field is set using <b>-baselinePrefix argument.
 * <p>
 * Example:
 *		<pre>-baseline.prefix 3.2_200606291905
