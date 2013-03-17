/**
 * Copyright (c) 2011, 2012 Shanghai Jiao Tong University.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Yi Zhang - initial API and implementation.
 */
package org.eclipse.recommenders.args.completion.rcp.recommenders;

import org.eclipse.recommenders.args.completion.rcp.entities.BaseVariable;
import org.eclipse.recommenders.args.completion.rcp.usagedb.ParamInfo;

/**
 * This class represents the concept of parameter usage instance.
 * An instance of this class contains information about a specific
 * parameter usage instance along with its frequency in the database.
 * 
 * @author ChengZhang
 *
 */
public class InstanceInfo {
	/** the base variable. */
	private BaseVariable baseVar;
	/** parameter information. */
	private ParamInfo paramInfo;
	/** index in the map between parameters and indices. */
	private int indexInMap;
	/** frequency in the usage database. */
	private Double freq;
	
	public final Double getFreq() {
		return freq;
	}
	public final void setFreq(final Double f) {
		this.freq = f;
	}
	public final BaseVariable getBaseVar() {
		return baseVar;
	}
	public final void setBaseVar(final BaseVariable bVar) {
		this.baseVar = bVar;
	}
	public final ParamInfo getParamInfo() {
		return paramInfo;
	}
	public final void setParamInfo(final ParamInfo pInfo) {
		this.paramInfo = pInfo;
	}
	public final int getIndexInMap() {
		return indexInMap;
	}
	public final void setIndexInMap(final int iInMap) {
		this.indexInMap = iInMap;
	}
	public InstanceInfo() {
		freq = 0.0;
		paramInfo = null;
	}

}

