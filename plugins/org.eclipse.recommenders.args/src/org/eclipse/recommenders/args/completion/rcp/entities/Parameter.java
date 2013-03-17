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

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.Expression;

/**
 * A entity class that was created to represent the formal parameter.
 * Now we use the class FormalParameter, instead of this class.
 * 
 * @author ChengZhang
 * @see FormalParameter
 * @deprecated
 */
public class Parameter {
	private ICompilationUnit compilationUnit;
	private Expression expression;
	private String typeSignature;
	private String name;
	
	private boolean isToRecommend;
	
	public final ICompilationUnit getCompilationUnit() {
		return compilationUnit;
	}
	public final void setCompilationUnit(final ICompilationUnit cUnit) {
		this.compilationUnit = cUnit;
	}
	public final Expression getExpression() {
		return expression;
	}
	public final void setExpression(final Expression expr) {
		this.expression = expr;
	}
	public final String getTypeSignature() {
		return typeSignature;
	}
	public final void setTypeSignature(final String typeSig) {
		this.typeSignature = typeSig;
	}
	public final String getName() {
		return name;
	}
	public final void setName(final String n) {
		this.name = n;
	}
	public final boolean isToRecommend() {
		return isToRecommend;
	}
	public final void setToRecommend(final boolean isToRec) {
		this.isToRecommend = isToRec;
	}
}
