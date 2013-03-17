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
package org.eclipse.recommenders.args.completion.rcp.ml.data;

import java.util.List;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.recommenders.args.completion.rcp.bn.LocalVar;
import org.eclipse.recommenders.args.completion.rcp.bn.ReceiverVar;
import org.eclipse.recommenders.args.completion.rcp.entities.BaseVariable;

/**
 * This class represent the data instance in the .arff file
 * as well as the .xml file.
 * 
 * @author ChengZhang
 *
 */
public class InstanceData {
	/** method name. */
	private String feature1;
	/** the signature of the enclosing method in which the actual parameter is used.  */
	private String feature2;
	/** methods invoked on the parameter variable. */
	private List<String> feature3; 
	/** methods invoked on the receiver variable. */
	private List<String> feature7; 
	
	/** parameter expression. */
	private Expression param;
	/** parameter string. */
	private String paramStr;
	/** receiver variable used to call the enclosing method. */
	private ReceiverVar rv;
	/** local variable used in the parameter. */
	private LocalVar lv;
	
	public final ReceiverVar getRv() {
		return rv;
	}
	public final void setRv(final ReceiverVar rVar) {
		this.rv = rVar;
	}
	public final LocalVar getLv() {
		return lv;
	}
	public final void setLv(final LocalVar lVar) {
		this.lv = lVar;
	}
	
	/** parameter index. */
	private int paramIndex;
	public final int getParamIndex() {
		return paramIndex;
	}
	public final void setParamIndex(final int pi) {
		this.paramIndex = pi;
	}
	
	/** this field is not essential, but to attach some extra info. */
	private BaseVariable baseVar; 
	public final BaseVariable getBaseVar() {
		return baseVar;
	}

	public final void setBaseVar(final BaseVariable bVar) {
		this.baseVar = bVar;
	}
	
	/**
	 * this field is not null only if param is a local method invocation.
	 * it is used to record (and find) the declaring type of the method invoked.
	 */
	private TypeDeclaration enclosingType; 
	public final TypeDeclaration getEnclosingType() {
		return enclosingType;
	}
	public final void setEnclosingType(final TypeDeclaration enclType) {
		this.enclosingType = enclType;
	}
	
	
	public final String getFeature1() {
		return feature1;
	}

	public final void setFeature1(final String f1) {
		this.feature1 = f1;
	}

	public final String getFeature2() {
		return feature2;
	}

	public final void setFeature2(final String f2) {
		this.feature2 = f2;
	}

	public final List<String> getFeature3() {
		return feature3;
	}

	public final void setFeature3(final List<String> f3) {
		this.feature3 = f3;
	}

	public final List<String> getFeature7() {
		return feature7;
	}

	public final void setFeature7(final List<String> f7) {
		this.feature7 = f7;
	}

	public final Expression getParam() {
		return param;
	}

	public final void setParam(final Expression p) {
		this.param = p;
	}

	public final String getParamStr() {
		return paramStr;
	}

	public final void setParamStr(final String pStr) {
		this.paramStr = pStr;
	}
}
