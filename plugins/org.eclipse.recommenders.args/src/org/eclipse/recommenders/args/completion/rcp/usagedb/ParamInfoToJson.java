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
package org.eclipse.recommenders.args.completion.rcp.usagedb;

/**
 * This is the entity class used for JsonReader.
 * It contains the parameter information and its frequency.
 *  
 * @author Tong
 *
 */
public class ParamInfoToJson {
	/**
	 * The reference of ParamInfo.
	 */
	private ParamInfo paraminfo;
	
	/**
	 * The index to identify the parameter.
	 */
	private int index;
	
	/**
	 * The frequency of this parameter appears.
	 */
	private int freq;
	
	public ParamInfoToJson(
			final ParamInfo paramInfo, 
			final int i, 
			final int f) {
		this.paraminfo = paramInfo; 
		this.index = i; 
		this.freq = f; 
	}

	public final ParamInfo getParaminfo() {
		return paraminfo;
	}

	public final void setParaminfo(final ParamInfo paramInfo) {
		this.paraminfo = paramInfo;
	}

	public final int getIndex() {
		return index;
	}

	public final void setIndex(final int i) {
		this.index = i;
	}

	public final int getFreq() {
		return freq;
	}

	public final void setFreq(final int f) {
		this.freq = f;
	}
}
