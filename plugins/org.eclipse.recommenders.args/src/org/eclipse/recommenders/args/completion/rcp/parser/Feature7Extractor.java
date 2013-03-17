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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.recommenders.args.completion.rcp.bn.ReceiverVar;

/**
 * This visitor finds out, for each actual parameter, the method calls on the base variable of
 * the called method.
 * 
 * @author Jing Fan
 *
 */
public class Feature7Extractor extends ASTVisitor {
	/** 
	 * the Stack top is the current enclosing method. For each element in Stack, the key is the base variable
	 * and the value is the method invocations happened on the base variable.
	 */
	private Stack<Map<String, List<String>>> invocatedMethods = new Stack<Map<String, List<String>>>();
	/** map between the position (of each parameter) and the method invocations on the base/receiver 
	 * variable of the corresponding method call. */
	private Map<Integer, List<String>> result = new HashMap<Integer, List<String>>();
	/** map between the position (of each parameter) and the base/receiver variable of 
	 * the corresponding method call. */
	private Map<Integer, ReceiverVar> pos2rev = new HashMap<Integer, ReceiverVar>();
	
	public final Map<Integer, List<String>> getResult() {
		//returns a map that contains all pairs of 
		//(paraID, name list of methods that the invocator variable has already invocated)
		return result;
	}
	
	public final Map<Integer, ReceiverVar> getPos2Rev() {
		return pos2rev;
	}

	@Override
	public final boolean visit(final MethodInvocation node) {
		if (node.getExpression() == null || invocatedMethods.isEmpty()) {
			// if there is no base variable or the method call is not in any method body.
			for (Object o: node.arguments()) {
				result.put(((Expression) o).getStartPosition(), new ArrayList<String>());
			}
			return super.visit(node);
		}
		
		// here we just the string representation of the base variable (expression)
		String invocatorName = node.getExpression().toString();
//		String typeStr = node.getExpression().resolveTypeBinding().getQualifiedName();
		ITypeBinding tb = node.getExpression().resolveTypeBinding();
		
		ReceiverVar rv = new ReceiverVar();
		rv.setStrRep(invocatorName);
		if (tb != null) {
			rv.setType(tb.getQualifiedName());
		} else {
			rv.setType(null);
		}
		
		String methodName = node.getName().toString();
		Map<String, List<String>> currentInvocatedMethods = invocatedMethods.peek();
		
		// first find out the feature7 value for each actual parameter
		for (Object o: node.arguments()) {
			pos2rev.put(((Expression) o).getStartPosition(), rv);
			
			if (currentInvocatedMethods.containsKey(invocatorName)) {
				List<String> tmp = new ArrayList<String>();
				for (String s: currentInvocatedMethods.get(invocatorName)) {
					tmp.add(s);
				}
				result.put(((Expression) o).getStartPosition(), tmp);
			} else {
				List<String> tmp = new ArrayList<String>();
				result.put(((Expression) o).getStartPosition(), tmp);
			}
		}
		
		// then record the current method call
		if (currentInvocatedMethods.containsKey(invocatorName)) {
			currentInvocatedMethods.get(invocatorName).add(methodName);
		} else {
			List<String> tmp = new ArrayList<String>();
			tmp.add(methodName);
			currentInvocatedMethods.put(invocatorName, tmp);
		}

		
		return super.visit(node);
	}
	
	@Override
	public final boolean visit(final MethodDeclaration node) {
		//clear the (var, method)map at the beginning of a method declaration
		invocatedMethods.push(new HashMap<String, List<String>>());
		return super.visit(node);
	}

	@Override
	public final void endVisit(final MethodDeclaration node) {
		//clear the (var, method)map at the end of a method declaration
		invocatedMethods.pop();
		super.endVisit(node);
	}
	
}
