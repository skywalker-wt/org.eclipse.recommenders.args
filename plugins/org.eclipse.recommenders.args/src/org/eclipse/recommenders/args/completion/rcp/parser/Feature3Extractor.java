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
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.recommenders.args.completion.rcp.bn.LocalVar;

/**
 * This visitor finds out, for each actual parameter, the method invocations that
 * have been occurred for the base variable (SimpleName) of this actual parameter.
 * 
 * Note that: <br/>
 * 1) The actual parameter is a MethodInvocation or QualifiedName, otherwise it does
 * not have a base variable.
 * 2) Here we just used the name string (of the SimpleName) as the key, which may be
 * inaccurate due to scope-related issues and aliases.
 * 
 * @author Jing Fan
 *
 */
public class Feature3Extractor extends ASTVisitor {
	/** the Stack top is the current enclosing method. For each element in Stack, the key is the base variable
	 * and the value is the method invocations happened on the base variable.
	 */
	private Stack<Map<String, List<String>>> invocatedMethods = new Stack<Map<String, List<String>>>();
	/** map between the position (of each parameter) and the method invocations on the parameter's base variable. */
	private Map<Integer, List<String>> result = new HashMap<Integer, List<String>>();
	/** map between the position (of each parameter) and the local variable used in the parameter. */
	private Map<Integer, LocalVar> pos2lv = new HashMap<Integer, LocalVar>();
	
	public final Map<Integer, List<String>> getResult() {
		// returns a map that contains all pairs of
		// (paraID, name list of methods that this exact parameter has already
		// invocated)
		return result;
	}
	
	public final Map<Integer, LocalVar> getPos2Lv() {
		return pos2lv;
	}

	@Override
	public final boolean visit(final ClassInstanceCreation node) {
		String invocatorName = "";
		if ((node.getParent() instanceof VariableDeclarationFragment)) {
			VariableDeclarationFragment parent = (VariableDeclarationFragment) node.getParent();
			invocatorName = parent.getName().getFullyQualifiedName();
		} else if (node.getParent() instanceof Assignment) {
			Assignment parent = (Assignment) node.getParent();
			invocatorName = parent.getLeftHandSide().toString();
		}
		
		if (!invocatedMethods.isEmpty()) {
			String methodName = node.getType().toString();

			Map<String, List<String>> currentInvocatedMethods = invocatedMethods
					.peek();
			if (currentInvocatedMethods.containsKey(invocatorName)) {
				currentInvocatedMethods.get(invocatorName).add(methodName);
			} else {
				List<String> tmp = new ArrayList<String>();
				tmp.add(methodName);
				currentInvocatedMethods.put(invocatorName, tmp);
			}
		}
				
		// then we update the feature3 value for each actual parameter
		for (Object o : node.arguments()) {
			LocalVar lv = new LocalVar(); // lv is the "local variable" used in the argument.
			String argumentName = ((Expression) o).toString();
//					String typeStr = ((Expression)o).resolveTypeBinding().getQualifiedName();
			ITypeBinding tb = ((Expression) o).resolveTypeBinding();
			
			if (o instanceof MethodInvocation) { // only deal with the form: a.method(?), considering 'a'
				MethodInvocation mi = (MethodInvocation) o;
				Expression expr = mi.getExpression();
				
				if (expr instanceof SimpleName) {
					argumentName = ((SimpleName) expr).getIdentifier();
//							typeStr = expr.resolveTypeBinding().getQualifiedName();
					tb = expr.resolveTypeBinding();
				}
			} else if (o instanceof QualifiedName) { // only deal with the form: o.field, considering 'o'
				QualifiedName qn = (QualifiedName) o;
				Name prefix = qn.getQualifier();
				
				if (prefix instanceof SimpleName) {
					argumentName = ((SimpleName) prefix).getIdentifier();
					tb = prefix.resolveTypeBinding();
				}
			}
			
			if (tb != null) {
				lv.setType(tb.getQualifiedName());
			} else {
				lv.setType(null);
			}
			lv.setStrRep(argumentName);
			pos2lv.put(((Expression) o).getStartPosition(), lv);
			
			if (invocatedMethods.isEmpty()) { 
				// if the method call is not in any method body, just store an empty List for each actual parameter
				result.put(((Expression) o).getStartPosition(), new ArrayList<String>());
				continue;
			}
			
			Map<String, List<String>> currentInvocatedMethods = invocatedMethods.peek();
			if (currentInvocatedMethods.containsKey(argumentName)) {
				List<String> tmp = new ArrayList<String>();
				for (String s : currentInvocatedMethods.get(argumentName)) {
					tmp.add(s);
				}
				result.put(((Expression) o).getStartPosition(), tmp);
			} else {
				List<String> tmp = new ArrayList<String>();
				result.put(((Expression) o).getStartPosition(), tmp);
			}	
		}
		
		return super.visit(node);
	}
	
	@Override
	public final boolean visit(final MethodInvocation node) {
		// Since the current node is also a method invocation, we first update the map in the invocatedMethods stack.
		if (node.getExpression() != null && !invocatedMethods.isEmpty()) {
			String invocatorName = node.getExpression().toString();
			String methodName = node.getName().toString();

			Map<String, List<String>> currentInvocatedMethods = invocatedMethods
					.peek();
			if (currentInvocatedMethods.containsKey(invocatorName)) {
				currentInvocatedMethods.get(invocatorName).add(methodName);
			} else {
				List<String> tmp = new ArrayList<String>();
				tmp.add(methodName);
				currentInvocatedMethods.put(invocatorName, tmp);
			}
		}
		
		// then we update the feature3 value for each actual parameter
		for (Object o : node.arguments()) {
			LocalVar lv = new LocalVar(); // lv is the "local variable" used in the argument.
			String argumentName = ((Expression) o).toString();
//			String typeStr = ((Expression)o).resolveTypeBinding().getQualifiedName();
			ITypeBinding tb = ((Expression) o).resolveTypeBinding();
			
			if (o instanceof MethodInvocation) { // only deal with the form: a.method(?), considering 'a'
				MethodInvocation mi = (MethodInvocation) o;
				Expression expr = mi.getExpression();
				int i = 0;
				if (expr instanceof SimpleName) {
					argumentName = ((SimpleName) expr).getIdentifier();
//					typeStr = expr.resolveTypeBinding().getQualifiedName();
					tb = expr.resolveTypeBinding();
				}
			} else if (o instanceof QualifiedName) { // only deal with the form: o.field, considering 'o'
				QualifiedName qn = (QualifiedName) o;
				Name prefix = qn.getQualifier();
				
				if (prefix instanceof SimpleName) {
					argumentName = ((SimpleName) prefix).getIdentifier();
//					typeStr = prefix.resolveTypeBinding().getQualifiedName();
					tb = prefix.resolveTypeBinding();
				}
			}
			
			if (tb != null) {
				lv.setType(tb.getQualifiedName());
			} else {
				lv.setType(null);
			}
			lv.setStrRep(argumentName);
			pos2lv.put(((Expression) o).getStartPosition(), lv);
			
			if (invocatedMethods.isEmpty()) { 
				// if the method call is not in any method body, just store an empty List for each actual parameter
				result.put(((Expression) o).getStartPosition(), new ArrayList<String>());
				continue;
			}
			
			Map<String, List<String>> currentInvocatedMethods = invocatedMethods.peek();
			if (currentInvocatedMethods.containsKey(argumentName)) {
				List<String> tmp = new ArrayList<String>();
				for (String s : currentInvocatedMethods.get(argumentName)) {
					tmp.add(s);
				}
				result.put(((Expression) o).getStartPosition(), tmp);
			} else {
				List<String> tmp = new ArrayList<String>();
				result.put(((Expression) o).getStartPosition(), tmp);
			}	
		}
		return super.visit(node);
	}

	@Override
	public final boolean visit(final MethodDeclaration node) {
		invocatedMethods.push(new HashMap<String, List<String>>());
		return super.visit(node);
	}

	@Override
	public final void endVisit(final MethodDeclaration node) {
		invocatedMethods.pop();
		super.endVisit(node);
	}

}
