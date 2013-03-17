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
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.recommenders.args.completion.rcp.entities.BaseVariable;

/**
 * This visitor is a counterpart of {@link Feature3Extractor}. It is used only
 * in parameter recommendation (testing), instead training. Since during
 * testing, the base variable is unknown, this visitor collects, for each
 * visible local variable, the method calls occurred on it.
 * 
 * @author ChengZhang
 * 
 */
public class Feature3forRecExtractor extends ASTVisitor {
	/** stack used to track the current enclosing method. */
	private Stack<MethodDeclaration> methodDeclStack
								= new Stack<MethodDeclaration>();
	/** 
	 * for each method declaration/body, store the map between each base variable
	 * and the method invocations happened on it within the scope of the current method. 
	 */
	private Map<MethodDeclaration, Map<BaseVariable, List<String>>> 
		decl2vars2invocations = new HashMap<MethodDeclaration, Map<BaseVariable, List<String>>>();
	/** 
	 * for each actual parameter (or argument), store the map between each base variable
	 * and the method invocations happened on it within the scope of the current method. 
	 */
	private Map<Integer, Map<BaseVariable, List<String>>> 
		pos2allInvocations = new HashMap<Integer, Map<BaseVariable, List<String>>>();

	public final Map<Integer, Map<BaseVariable, List<String>>> getPos2AllInvocations() {
		return pos2allInvocations;
	}

	@Override
	public final boolean visit(final MethodDeclaration node) {
		methodDeclStack.push(node);
		return super.visit(node);
	}

	@Override
	public final void endVisit(final MethodDeclaration node) {
		methodDeclStack.pop();
		//System.out.println(pos2allInvocations);
		super.endVisit(node);
	}
	
	@Override
	public final boolean visit(final ClassInstanceCreation node) {
		// we temporarily ignore invocations out of any method declaration.
		if (methodDeclStack.isEmpty()) {
			return super.visit(node);
		}

		/**
		 * In ClassInstanceCreation, node.getExpression() couldn't be SimpleName.
		 * We have to get its parent node, if it is an Assignment Node.we can use
		 * its left hand side as SimpleName.
		 */
		Expression expression = node.getExpression();
		if (expression == null && node.getParent() instanceof Assignment) {
			expression = ((Assignment) node.getParent()).getLeftHandSide();
		}

		if (expression instanceof SimpleName) { // if
															// node.getExpression()
															// returns null, the
															// test will not
															// pass.
			SimpleName baseVarName = (SimpleName) expression;
			BaseVariable baseVar = new BaseVariable();
			baseVar.setExpr(baseVarName);
			baseVar.setName(baseVarName.getIdentifier());
			baseVar.setType(baseVarName.resolveTypeBinding());
			
			node.getType().toString();
			String methodName = node.getType().toString();

			MethodDeclaration currentMethod = methodDeclStack.peek();

			Map<BaseVariable, List<String>> var2invocations = decl2vars2invocations
					.get(currentMethod);
			if (var2invocations == null) {
				var2invocations = new HashMap<BaseVariable, List<String>>();
				decl2vars2invocations.put(currentMethod, var2invocations);
			}

			List<String> invocations = var2invocations.get(baseVar);
			if (invocations == null) {
				invocations = new ArrayList<String>();
				var2invocations.put(baseVar, invocations);
			}

			invocations.add(methodName);
		}

		if (decl2vars2invocations.size() > 0) {
			int i = 0;
		}
		for (Object o : node.arguments()) {
			// just record the enclosing method for each argument (position)
			MethodDeclaration currentMethod = methodDeclStack.peek();

			// get the method invocations that have already encountered in the
			// current method.
			if (decl2vars2invocations.get(currentMethod) != null) {
				Map<BaseVariable, List<String>> currentVar2invo = decl2vars2invocations.get(currentMethod);
				Map<BaseVariable, List<String>> invokedInCurrentMethod = new HashMap<BaseVariable, List<String>>();
				
				// do the deep copy manually
					Set<Entry<BaseVariable, List<String>>> v2iEntrySet = currentVar2invo.entrySet();
					for (Entry<BaseVariable, List<String>> v2iEntry : v2iEntrySet) {
						BaseVariable tmpBaseVar = v2iEntry.getKey();
						List<String> tmpInvokList = v2iEntry.getValue();
						invokedInCurrentMethod.put(tmpBaseVar, new ArrayList<String>(tmpInvokList));
					}
					
					pos2allInvocations.put(((Expression) o).getStartPosition(),
							invokedInCurrentMethod);
				}
			}

			return super.visit(node);
	}

	@Override
	public final boolean visit(final MethodInvocation node) {
		// we temporarily ignore invocations out of any method declaration.
		if (methodDeclStack.isEmpty()) {
			return super.visit(node);
		}

		// record the method invocations on SimpleName
		if (node.getExpression() instanceof SimpleName) { // if
															// node.getExpression()
															// returns null, the
															// test will not
															// pass.
			SimpleName baseVarName = (SimpleName) node.getExpression();
			BaseVariable baseVar = new BaseVariable();
			baseVar.setExpr(baseVarName);
			baseVar.setName(baseVarName.getIdentifier());
			baseVar.setType(baseVarName.resolveTypeBinding());
			
			String methodName = node.getName().toString();

			MethodDeclaration currentMethod = methodDeclStack.peek();

			Map<BaseVariable, List<String>> var2invocations = decl2vars2invocations
					.get(currentMethod);
			if (var2invocations == null) {
				var2invocations = new HashMap<BaseVariable, List<String>>();
				decl2vars2invocations.put(currentMethod, var2invocations);
			}

			List<String> invocations = var2invocations.get(baseVar);
			if (invocations == null) {
				invocations = new ArrayList<String>();
				var2invocations.put(baseVar, invocations);
			}

			invocations.add(methodName);
		}

		for (Object o : node.arguments()) {
			// just record the enclosing method for each argument (position)
			MethodDeclaration currentMethod = methodDeclStack.peek();

			// get the method invocations that have already encountered in the
			// current method.
			
			if (decl2vars2invocations.get(currentMethod) != null) {
//				Map<BaseVariable, List<String>> invokedInCurrentMethod = new HashMap<BaseVariable, List<String>>(
//						decl2vars2invocations.get(currentMethod));
				Map<BaseVariable, List<String>> currentVar2invo = decl2vars2invocations.get(currentMethod);
				Map<BaseVariable, List<String>> invokedInCurrentMethod = new HashMap<BaseVariable, List<String>>();
				
				// do the deep copy manually
				Set<Entry<BaseVariable, List<String>>> v2iEntrySet = currentVar2invo.entrySet();
				for (Entry<BaseVariable, List<String>> v2iEntry : v2iEntrySet) {
					BaseVariable tmpBaseVar = v2iEntry.getKey();
					List<String> tmpInvokList = v2iEntry.getValue();
					invokedInCurrentMethod.put(tmpBaseVar, new ArrayList<String>(tmpInvokList));
				}
				
				pos2allInvocations.put(((Expression) o).getStartPosition(),
						invokedInCurrentMethod);
			}
		}

		return super.visit(node);
	}
}
