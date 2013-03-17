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

import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.SimpleName;

/**
 * This class represents a local (base) variable
 * for a method invocation in a scope of method.
 * 
 * The class is created to fix the bug found by Jing Fan:
 * I formerly used instances of SimpleName as keys in a map,
 * thinking that if their names and types are equal, two SimpleNames
 * should be equal. Unfortunately, SimpleName contains info
 * about its position (I guess), that is, two SimpleNames are not
 * equal if they are at different positions in the compilation unit,
 * even though they actually represent the same local variable.
 * 
 * @author ChengZhang
 *
 */
public class BaseVariable {
	/**
	 * The simple name of literal variable.
	 */
	private SimpleName expr;
	
	/**
	 * The name of variable. 
	 * Note: this field isn't used as a feature in this version.
	 * Just used for calculating the hashcode.
	 */
	private String name;
	
	/**
	 * The exact type information of the variable.
	 */
	private ITypeBinding type;
	
	/**
	 * The constant number for calculating hashCode.
	 */
	private static final int HASH_NUM = 37;
	
	public final SimpleName getExpr() {
		return expr;
	}
	
	public final void setExpr(final SimpleName e) {
		this.expr = e;
	}
	
	/**
	 * Gets the name of variable.
	 * Note: this field isn't used as a feature in this version.
	 * Just used for calculating the hashcode.
	 * 
	 * @return the name of variable.
	 */
	public final String getName() {
		return name;
	}
	
	public final void setName(final String n) {
		this.name = n;
	}
	
	public final ITypeBinding getType() {
		return type;
	}
	
	public final void setType(final ITypeBinding t) {
		this.type = t;
	}
	
	/**
	 * This equals method only takes into account two fields: name and type.
	 * 
	 * @param obj - the other object in the equality check
	 * @return whether the two objects are equal
	 */
	public final boolean equals(final Object obj) {
		if (obj instanceof BaseVariable) {
			BaseVariable otherVar = (BaseVariable) obj;
			
			if (otherVar.name != null && otherVar.type != null) {
				if (this.name != null && this.type != null) {
					return this.name.equals(otherVar.name) 
							&& this.type.equals(otherVar.type);
				} else {
					return false;
				}
			} else if (otherVar.name == null && otherVar.type == null) {
				return this.name == null && this.type == null;
			} else if (otherVar.name != null && otherVar.type == null) {
				if (this.name != null && this.type == null) {
					return this.name.equals(otherVar.name);
				} else {
					return false;
				}
			} else if (otherVar.name == null && otherVar.type != null) {
				if (this.name == null && this.type != null) {
					return this.type.equals(otherVar.type);
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
		int nameCode = 0, typeCode = 0;
		if (this.name != null) {
			nameCode = this.name.hashCode();
		}
		
		if (this.type != null) {
			typeCode = this.type.hashCode();
		}
		
		return nameCode * HASH_NUM + typeCode;
	}
	
	@Override
	public final String toString() {
		String typeStr = "UNKNOWN";
		if (this.type != null) {
			typeStr = this.type.getQualifiedName();
		}
		return this.name + " (" + typeStr + ")";
	}
}
