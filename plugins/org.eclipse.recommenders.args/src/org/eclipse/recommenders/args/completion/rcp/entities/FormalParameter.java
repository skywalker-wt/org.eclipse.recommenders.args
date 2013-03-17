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
package org.eclipse.recommenders.args.completion.rcp.entities;

/**
 * An important entity class in the project.
 * It represents a specific formal parameter (rather than an actual parameter)
 * of a method declaration (rather than a method call).
 * 
 * @author ChengZhang
 *
 */
public class FormalParameter {
	/** name of the enclosing class. */
	private String className;
	
	/** name of the enclosing method. */
	private String methodName;
	
	/** index of the current formal parameter in the parameter list. */
	private int paramIndex;
	
	/** type (not structure type) of the parameter expression. */
	private String paramType;
	
	/** the string composed of the list of types 
	 *  of all the formal parameter of the current method. 
	 **/
	private String paramListStr;
	/** prime integer number used for computing hash code. */
	private static final int HASH_NUM = 31;
	/** length limit for the string representation of formal parameter. */
	private static final int MAX_LEN = 100; // TODO: check whether this limit can be always enforced
	
	public final String getClassName() {
		return className;
	}
	
	public final void setClassName(final String clsName) {
		this.className = clsName;
	}
	
	public final String getMethodName() {
		return methodName;
	}
	
	public final void setMethodName(final String mName) {
		this.methodName = mName;
	}
	
	public final int getParamIndex() {
		return paramIndex;
	}
	
	public final void setParamIndex(final int pIndex) {
		this.paramIndex = pIndex;
	}
	
	public final String getParamType() {
		return paramType;
	}
	
	public final void setParamType(final String pType) {
			this.paramType = pType;
	}
	
	public final String getParamListStr() {
		return paramListStr;
	}
	
	public final void setParamListStr(final String pListStr) {
		this.paramListStr = pListStr;
	}
	
	@Override
	public final boolean equals(final Object o) {
		if (o instanceof FormalParameter) {
			FormalParameter fp = (FormalParameter) o;
			
			if (this.className != null && fp.className != null && this.className.equals(fp.className)) {
				if (this.methodName != null && fp.methodName != null && this.methodName.equals(fp.methodName)) {
					if (this.paramIndex == fp.paramIndex) {
						if (this.paramType != null && fp.paramType != null && this.paramType.equals(fp.paramType)) {
							return this.paramListStr != null 
								&& fp.paramListStr != null 
								&& this.paramListStr.equals(fp.paramListStr);
						} else {
							return false;
						}
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	@Override
	public final int hashCode() {
		int cc = 0, mc = 0, pc = 0, pls = 0;
		if (this.className != null) {
			cc = this.className.hashCode();
		}
		
		if (this.methodName != null) {
			mc = this.methodName.hashCode();
		}
		
		if (this.paramType != null) {
			pc = this.paramType.hashCode();
		}
		
		if (this.paramListStr != null) {
			pls = this.paramListStr.hashCode();
		}
		
		return ((HASH_NUM * (this.paramIndex * HASH_NUM + cc) + mc)
				* HASH_NUM + pc) * HASH_NUM + pls;
	}
	
	@Override
	public final String toString() {
		String parsedName;
		if (className.indexOf('{') > 0) {
			String[] cnSegs = className.split("\\{");
			parsedName = cnSegs[0] + className.hashCode();
		} else {
			parsedName = className;
		}
		String result = parsedName + "_"
				+ methodName + "_" + paramIndex + "_"
				+ paramType + "_" + paramListStr;
		if (result.length() < MAX_LEN) {
			return result;
		}
		String shortResult = lastSeg(parsedName) + "_"
		+ lastSeg(methodName) + "_" + paramIndex + "_"
		+ lastSeg(paramType) + "_" + lastSeg(paramListStr) + result.hashCode();
		return shortResult;
	}
	
	/**
	 * Get the last segment of a given name string.
	 * 
	 * @param name - the name string to be segmented
	 * @return the last segment
	 */
	private String lastSeg(final String name) {
		if (!name.contains(",")) {
			String[] segs = name.split("\\.");
			return segs[segs.length - 1];
		} else {
			String[] segs = name.split(",");
			StringBuffer sb = new StringBuffer();
			for (String s:segs) {
				sb.append(lastSeg(s) + ",");
			}
			return sb.toString();
		}
	}
}
