/**
 * Copyright (c) 2011, 2012 Shanghai Jiao Tong University.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cheng Zhang - initial API and implementation.
 */
package org.eclipse.recommenders.args.completion.rcp.filter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.eclipse.recommenders.args.completion.rcp.ParametersPlugin;
import org.eclipse.swt.widgets.Composite;

/**
 * This class filters out some uninteresting classes (e.g., java.lang.Object and java.util.Map).
 * As a result, Precise does not mine/recommend from/for the parameters of the
 * methods defined in such uninteresting classes.
 * 
 * @author ChengZhang
 * 
 */
public final class ParameterFilter {
	/** the key string of filter policy. */
	public static final String KEY_FILTER_POLICY = "filter.policy";
	/** the value string of the blacklist filter policy. */
	public static final String VALUE_FILTER_POLICY_BLACKLIST = "filter.policy.blacklist";
	/** the value string of the whitelist filter policy. */
	public static final String VALUE_FILTER_POLICY_WHITELIST = "filter.policy.whitelist";
	
	/** the key string of the blacklist. */
	public static final String KEY_FILTER_BLACKLIST = "filter.blacklist";
	/** the key string of the whitelist. */
	public static final String KEY_FILTER_WHITELIST = "filter.whitelist";
	
	/** whether the configuration has already been loaded. */
	private static boolean isLoaded = false;
	
	/** whether blacklist policy will be used. */
	private static boolean isBlackListPolicy = false;
	/** whether whitelist policy will be used. */
	private static boolean isWhiteListPolicy = false;
	/** the list of blacklist items. */
	private static Set<String> blackList = new HashSet<String>();
	/** the list of whitelist items. */
	private static Set<String> whiteList = new HashSet<String>();
	
	/** the path of the plugin. */
	private static String pluginPath = null;
	/** the path of the property file. */
	private static String propertyFilePath = null;
	
	/** in windows the prefix is "reference:file:/D:/...".  */
	private static final int WINDOWS_PREFIX = 16;

	/** in linux the prefix is "reference:file:/...". */
	private static final int LINUX_PREFIX = 15;
	private static final int OTHER_PREFIX = 16;
	
	/**
	 * This utility method is designed to load 
	 * relevant properties from the configuration file.
	 */
	
	private ParameterFilter() {	}
	
	/**
	 * determine the path of property file which contains configuration items.
	 */
	public static void initializePaths() {
		Composite c;
		if (pluginPath == null && propertyFilePath == null) {
			Properties sysProp = System.getProperties();
			String osName = sysProp.getProperty("os.name");
			if (osName == null) {
				System.err.println("Unable to identify the current OS!");
				return;
			} else {
				if (osName.startsWith("Windows")) {
					pluginPath = ParametersPlugin.getDefault().getBundle().getLocation().substring(WINDOWS_PREFIX);
				} else if (osName.startsWith("Linux")) {
					pluginPath = ParametersPlugin.getDefault().getBundle().getLocation().substring(LINUX_PREFIX);
				} else {
					System.err.println("OS is neither Windows nor Linux. Use default properties path.");
					pluginPath = ParametersPlugin.getDefault().getBundle().getLocation().substring(OTHER_PREFIX);
				}
				propertyFilePath = pluginPath + "/config/precise.properties";
			}
		}
	}
	
	/**
	 * load configuration from the property file.
	 */
	private static void loadConfig() {
		if (!isLoaded) {
			initializePaths();
			Properties prop = new Properties();
			
			try {
				prop.load(new FileInputStream(new File(propertyFilePath)));
				
				String policy = prop.getProperty(KEY_FILTER_POLICY);
				
				if (policy != null) {
					if (policy.equals(VALUE_FILTER_POLICY_BLACKLIST)) {
						isBlackListPolicy = true;
						isWhiteListPolicy = false;
					} else if (policy.equals(VALUE_FILTER_POLICY_WHITELIST)) {
						isWhiteListPolicy = true;
						isBlackListPolicy = false;
					} // else, both of the variables are false
				} // else, both of the variables are false
				
				
				if (isBlackListPolicy) { // if we use the black list policy, we fill the set of black list items.
					String blString = prop.getProperty(KEY_FILTER_BLACKLIST);
					String[] blSegs = blString.split(",");
					for (String blItem : blSegs) {
						blackList.add(blItem);
					}
				} else if (isWhiteListPolicy) { // if we use the white list policy, we fill the set of white list items.
					String wlString = prop.getProperty(KEY_FILTER_WHITELIST);
					String[] wlSegs = wlString.split(",");
					for (String wlItem : wlSegs) {
						whiteList.add(wlItem);
					}
				}
				
				isLoaded = true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
	}
	
	
	/**
	 * Determine whether a parameter will be filtered out during mining and recommendation.
	 * We use the fully qualified type name of the method where the formal parameter resides
	 * to make the judgment.
	 * 
	 * If the method returns true, the parameter will not be handled,
	 * otherwise (the method returns false) the parameter will be handled.
	 * 
	 * @param declTypeName - the fully qualified type name of the method where the formal parameter resides
	 * @return  whether the type will be filtered
	 */
	public static boolean doesFilterOutParam(final String declTypeName) {
		loadConfig(); // make sure that all the variables have already been loaded.
		
		boolean toBeFiltered = false;
		
		// we first check the black list policy
		if (isBlackListPolicy) {
			for (String bItem : blackList) { // each item in the black list must be a fully qualified class name
				// the declTypeName may be unqualified
				if (bItem.equals(declTypeName) || bItem.endsWith(declTypeName)) {
					toBeFiltered = true;
					break;
				}
			}
		} else if (isWhiteListPolicy) {
			toBeFiltered = true;
			for (String wItem : whiteList) { // each item in the white list must be a fully qualified class name
				// the declTypeName may be unqualified
				if (wItem.equals(declTypeName) || wItem.endsWith(declTypeName)) { 
					toBeFiltered = false;
					break;
				}
			}
		} // if no filter policy is specified, the parameter will be handled.
		
		return toBeFiltered;
	}
}
