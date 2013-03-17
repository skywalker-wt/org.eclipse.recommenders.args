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
package org.eclipse.recommenders.args.completion.rcp.usagedb;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.io.CharStreams;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * This class used to get param infomation from json file.
 * 
 * Note: It is different from model file. Parameter information didn't contain 
 * the model of Bayes Network. 
 * 
 * @author Tong
 *
 */
public class UsageDBJSONReader {
	/** The InputStream of the json file.	 */
	private InputStream inputStream;
	
	/** The parameter info from json file.	 */
	private Map<Integer, ParamInfo> index2param;
	
	/** The probability of param from json file.	 */
	private Map<Integer, Integer> index2freq;
	
	public UsageDBJSONReader(final InputStream is) {
		this.inputStream = is;
		index2param = new HashMap<Integer, ParamInfo>();
		index2freq = new HashMap<Integer, Integer>();
	}

	public final Map<Integer, ParamInfo> getIndex2param() {
		return index2param;
	}

	public final void setIndex2param(final Map<Integer, ParamInfo> i2p) {
		this.index2param = i2p;
	}

	public final Map<Integer, Integer> getIndex2freq() {
		return index2freq;
	}

	public final void setIndex2freq(final Map<Integer, Integer> i2f) {
		this.index2freq = i2f;
	}
	
	/**
	 * Read param info from json file.
	 * 
	 * Include the list of ParamInfo , and list of frequency.
	 * 
	 * @return "true" for success
	 */
	public final boolean readMapFromJSON() {
		boolean success = false;
		
			String content;
			try {
				content = CharStreams.toString(new InputStreamReader(inputStream));
				
				//The json file contains only a list of ParamInfoToJson.
				List<ParamInfoToJson> paramInfos = 
						(List<ParamInfoToJson>) new Gson()
							.fromJson(content, new TypeToken<List<ParamInfoToJson>>() { } .getType());
			
				for (ParamInfoToJson p : paramInfos) {
					index2param.put(p.getIndex(), p.getParaminfo());
		            index2freq.put(p.getIndex(), p.getFreq());
		            success = true;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		return success;
	}
}
