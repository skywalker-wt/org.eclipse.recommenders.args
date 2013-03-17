/**
 * Copyright (c) 2011, 2012 Shanghai Jiao Tong University.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *    Tong Wu - initial API and implementation.
 */
package org.eclipse.recommenders.args.completion.rcp;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle.
 */
public class ParametersPlugin extends AbstractUIPlugin {
	/**
	 * The plug-in Bundle-SymbolicName.
	 */
	public static final String PLUGIN_ID = 
			"org.eclipse.recommenders.completion.rcp.args"; //$NON-NLS-1$

	/**
	 *  The shared instance.
	 */
	private static ParametersPlugin plugin;

	@Override
	public final void start(final BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	
	@Override
	public final void stop(final BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 *
	 * @return the shared instance
	 */
	public static ParametersPlugin getDefault() {
		return plugin;
	}

}
