/**
 * Copyright (c) 2011, 2012 Shanghai Jiao Tong University.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Yi Zhang - initial API and implementation.
 */
package org.eclipse.recommenders.args.completion.rcp.recommenders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

/**
 * This visitor finds out the accessible variables within the current scope.
 * Currently we deal with scopes defined by static initialization blocks and
 * method declarations, recording the local variables defined in the scopes
 * and formal parameters (for method declarations).
 * 
 * However, as the static initialization blocks and (one of) constructors must 
 * be executed before reaching a normal program point, the order of recording
 * such accessible variables may be incorrect, as it depends on the order of
 * the depth-first traversal of the AST. 
 * 
 * TODO: the accessible variables/methods information may be retrieved using JDT's APIs
 * 
 * @author Yi Zhang
 */
public class AccessibleVariableSearcher extends ASTVisitor {
	/** map between the position of each parameter and the accessible variables. */
	private Map<Integer, List<Name>> result = new HashMap<Integer, List<Name>>();
	/** each stack element stores the accessible fields in the current scope. */
	private Stack<List<Name>> currentField = new Stack<List<Name>>(); 
	/** 
	 * each stack element stores the accessible formal parameters and local variables
	 * in the current scope.
	 */
	private Stack<List<Name>> currentAccessible = new Stack<List<Name>>();
	/** map between each position and its corresponding actual parameter (or argument). */
	private Map<Integer, Expression> pos2arg = new HashMap<Integer, Expression>();
	/** track the current enclosing type. */
	private Stack<TypeDeclaration> typeDecl = new Stack<TypeDeclaration>();
	/** map between each position and its corresponding type declaration. */
	private Map<Integer, TypeDeclaration> pos2typeDecl = new HashMap<Integer, TypeDeclaration>();
	
	public final Map<Integer, Expression> getPos2ArgMap() {
		return pos2arg;
	}

	public final Map<Integer, List<Name>> getResult() {
		return result;
	}
	
	public final Map<Integer, TypeDeclaration> getPos2TypeDecl() {
		return pos2typeDecl;
	}
	
	@Override
	public final boolean visit(final TypeDeclaration node) {
		typeDecl.push(node);
		currentField.push(new ArrayList<Name>());
		return super.visit(node);
	}
	
	@Override
	public final void endVisit(final TypeDeclaration node) {
		typeDecl.pop();
		currentField.pop();
		super.endVisit(node);
	}
	
	@Override
	public final boolean visit(final FieldDeclaration node) {
		for (Object o : node.fragments()) {
			VariableDeclarationFragment vdf = (VariableDeclarationFragment) o;
			currentField.peek().add(vdf.getName());
		}
		return super.visit(node);
	}
	
	@Override
	public final boolean visit(final MethodDeclaration node) {
		List<Name> formalParameters = new ArrayList<Name>();
		for (Object o : node.parameters()) {
			SingleVariableDeclaration svd = (SingleVariableDeclaration) o;
			formalParameters.add(svd.getName());
		}
		currentAccessible.push(formalParameters);
		return super.visit(node);
	}
	
	@Override
	public final void endVisit(final MethodDeclaration node) {
		currentAccessible.pop();
		super.endVisit(node);
	}
	
	@Override
	public final boolean visit(final Initializer node) {
		currentAccessible.push(new ArrayList<Name>());
		return super.visit(node);
	}

	@Override
	public final void endVisit(final Initializer node) {
		currentAccessible.pop();
		super.endVisit(node);
	}

	@Override
	public final boolean visit(final VariableDeclarationStatement node) {
		for (Object o : node.fragments()) {
			VariableDeclarationFragment vdf = (VariableDeclarationFragment) o;
			currentAccessible.peek().add(vdf.getName());
		}
		return super.visit(node);
	}
	
	@Override
	public final boolean visit(final ClassInstanceCreation node) {
		for (Object o : node.arguments()) {
			Expression argExpr = (Expression) o;
			pos2arg.put(argExpr.getStartPosition(), argExpr);
			pos2typeDecl.put(argExpr.getStartPosition(), typeDecl.peek());
			
			if (currentAccessible.isEmpty()) {
				result.put(((Expression) o).getStartPosition(), new ArrayList<Name>());
			} else {
				result.put(((Expression) o).getStartPosition(), new ArrayList<Name>(currentAccessible.peek()));
			}
			if (!currentField.isEmpty()) {
				result.get(((Expression) o).getStartPosition()).addAll(currentField.peek());
			}
		}
		
		return super.visit(node);
	}
	
	@Override
	public final boolean visit(final MethodInvocation node) {
		for (Object o : node.arguments()) {
			Expression argExpr = (Expression) o;
			pos2arg.put(argExpr.getStartPosition(), argExpr);
			pos2typeDecl.put(argExpr.getStartPosition(), typeDecl.peek());
			
			if (currentAccessible.isEmpty()) {
				result.put(((Expression) o).getStartPosition(), new ArrayList<Name>());
			} else {
				result.put(((Expression) o).getStartPosition(), new ArrayList<Name>(currentAccessible.peek()));
			}
			if (!currentField.isEmpty()) {
				result.get(((Expression) o).getStartPosition()).addAll(currentField.peek());
			}
		}
		
		return super.visit(node);
	}
}

