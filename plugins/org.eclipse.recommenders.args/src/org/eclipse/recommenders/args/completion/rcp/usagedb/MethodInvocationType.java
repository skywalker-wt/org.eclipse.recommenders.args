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

/**
 * This class contains the essential information of the 
 * method invocation structure type.
 * 
 * @author ChengZhang
 *
 */
public class MethodInvocationType {
	/** type of the base variable. */
	private String invokeType;
	/** type which declares the called method. */
	private String declareType;
	/** method name. */
	private String methodName;
	/** type of the first element of the invoke expression. */
	private String firstElementType;
	/** indicate whether the method is static or not. */
	private String modifier;

	public final String getInvokeType() {
		return invokeType;
	}

	public final void setInvokeType(final String invType) {
		this.invokeType = invType;
	}
	
	public final String getDeclareType() {
		return declareType;
	}

	public final void setDeclareType(final String declType) {
		this.declareType = declType;
	}

	public final String getMethodName() {
		return methodName;
	}

	public final void setMethodName(final String mName) {
		this.methodName = mName;
	}

	public final String getFirstElementType() {
		return firstElementType;
	}

	public final void setFirstElementType(final String firstElemType) {
		this.firstElementType = firstElemType;
	}

	public final String getModifier() {
		return modifier;
	}

	public final void setModifier(final String mod) {
		this.modifier = mod;
	}
	
	/**
	 * Compute the degree of equality (or similarity) of two objects.
	 * 
	 * @param obj - another obj to compute degree of equality
	 * @return 0 means unequal, 1 means exactly equal, and 2 means partially equal (some parts in common)
	 */
	public final int equalsLevel(final Object obj) {
		if (obj instanceof MethodInvocationType) {
			MethodInvocationType otherMit = (MethodInvocationType) obj;
			// no first element, such as getContext()
			if (this.firstElementType.equals("null") && otherMit.firstElementType.equals("null")) {
				if (this.methodName.equals(otherMit.methodName) 
						&& this.modifier.equals(otherMit.modifier)) {
					return 1;	// exactly equal
				} else {
					return 0;	// not equal
				}
			}
			if ((this.firstElementType.equals(otherMit.firstElementType)
					&& this.invokeType.equals(otherMit.invokeType))
					|| (this.declareType != null && this.declareType.equals(otherMit.declareType))) {
				if (this.methodName.equals(otherMit.methodName)
						&& this.modifier.equals(otherMit.modifier)) {
					return 1;	// exactly equal
				} else {
					return 2;	// partially equal
				}
			}
		}
		return 0;	// not equal
	}
}
