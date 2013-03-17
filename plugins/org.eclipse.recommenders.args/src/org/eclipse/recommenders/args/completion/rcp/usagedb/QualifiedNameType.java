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
 * qualified name structure type.
 * 
 * @author ChengZhang
 *
 */
public class QualifiedNameType {
	/** type of the qualifier. */
	private String qualiferType;
	/** declared type of the qualified name. */
	private String declareType;
	/** name of the qualified name (without the qualifier). */
	private String simpleName;
	/** type of the first element of the qualified name. */
	private String firstElementType;
	/** indicate whether the qualified name is static or not. */
	private String modifier;
	
	public final String getQualiferType() {
		return qualiferType;
	}
	public final void setQualiferType(final String qualType) {
		this.qualiferType = qualType;
	}
	public final String getDeclareType() {
		return declareType;
	}
	public final void setDeclareType(final String decType) {
		this.declareType = decType;
	}
	public final String getSimpleName() {
		return simpleName;
	}
	public final void setSimpleName(final String sName) {
		this.simpleName = sName;
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
		if (obj instanceof QualifiedNameType) {
			QualifiedNameType otherQnt = (QualifiedNameType) obj;
			if (this.firstElementType.equals("null") && otherQnt.firstElementType.equals("null")) {
				if (this.simpleName.equals(otherQnt.simpleName)
						&& this.modifier.equals(otherQnt.modifier)) {
					return 1;	// exactly equal
				} else {
					return 0;	// not equal
				}
			}
			if ((this.firstElementType.equals(otherQnt.firstElementType)
					&& this.qualiferType.equals(otherQnt.qualiferType))
					|| (this.declareType != null && this.declareType.equals(otherQnt.declareType))) {
				if (this.simpleName.equals(otherQnt.simpleName) 
						&& this.modifier.equals(otherQnt.modifier)) {
					return 1;	// exactly equal
				} else {
					return 2;	// partially equal
				}
			}
		}
		return 0;	// not equal
	}
}
