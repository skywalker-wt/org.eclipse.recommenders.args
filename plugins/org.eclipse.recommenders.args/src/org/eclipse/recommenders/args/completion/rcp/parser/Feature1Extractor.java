/**
 * Copyright (c) 2011, 2012 Shanghai Jiao Tong University.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *    Jing Fan - initial API and implementation.
 */
package org.eclipse.recommenders.args.completion.rcp.parser;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodInvocation;

/**
 * This visitor finds out, for each actual parameter, the name of the called method.
 * 
 * @author Jing Fan
 *
 */
public class Feature1Extractor extends ASTVisitor {
	/** map between each parameter's start position and its enclosing method (i.e., the called method). */
	private Map<Integer, String> result = new HashMap<Integer, String>();
	
	public final Map<Integer, String> getResult() {
		//returns a map that contains all pairs of (paraID, methodName)
		return result;
	}
	
	@Override
	public final boolean visit(final ClassInstanceCreation node) {
		String methodName = node.getType().toString();

		for (Object o: node.arguments()) {
			result.put(((Expression) o).getStartPosition(), methodName);
		}
		
		return super.visit(node);
	}

	@Override
	public final boolean visit(final MethodInvocation node) {
		String methodName = node.getName().toString();

		for (Object o: node.arguments()) {
			result.put(((Expression) o).getStartPosition(), methodName);
		}
		
		return super.visit(node);
	}
}
