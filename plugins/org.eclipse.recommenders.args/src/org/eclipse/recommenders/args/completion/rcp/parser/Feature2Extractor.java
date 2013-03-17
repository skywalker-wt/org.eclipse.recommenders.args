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
import java.util.Stack;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * This visitor finds out, for each actual parameter, the enclosing method where the
 * parameter is used.
 * Note that, there can be no enclosing method for an actual parameter, since a method
 * call may occur at field declaration (out of any method body).
 * 
 * TODO: investigate whether it will be helpful to categorize the field declaration 
 * cases into a kind of special "enclosing method" (i.e. <init> or <clinit>).
 * 
 * @author Jing Fan
 *
 */
public class Feature2Extractor extends ASTVisitor {
	/**
	 * All of the pairs of invoke position and declaration info.
	 * (position, packageName + "." + classDeclareation+ "." + methodDeclaration)
	 */
	private Map<Integer, String> result = new HashMap<Integer, String>();
	
	/**
	 * It could be null, when the method invoked in an anonymous class.
	 */
	private Stack<String> methodDeclaration = new Stack<String>();
	
	/**
	 * The class of the declaration.
	 */
	private Stack<String> classDeclareation = new Stack<String>();
	
	/**
	 * The class of the class.
	 */
	private String packageName = "";

	public final Map<Integer, String> getResult() {
		// returns a map that contains all pairs of
		// (paraID, the name of the method in which the invocation was made)
		return result;
	}

	@Override
	public final boolean visit(final MethodDeclaration node) {
		this.methodDeclaration.push(node.getName().toString());
		return super.visit(node);
	}	

	@Override
	public final void endVisit(final MethodDeclaration node) {
		this.methodDeclaration.pop();
		super.endVisit(node);
	}
	
	@Override
	public final boolean visit(final TypeDeclaration node) {
		this.classDeclareation.push(node.getName().toString());
		return super.visit(node);
	}

	@Override
	public final void endVisit(final TypeDeclaration node) {
		this.classDeclareation.pop();
		super.endVisit(node);
	}

	@Override
	public final boolean visit(final PackageDeclaration node) {
		this.packageName = node.getName().toString();
		return super.visit(node);
	}

	@Override
	public final boolean visit(final ClassInstanceCreation node) {
		if (node.arguments().size() > 0) {
			for (Object o : node.arguments()) {
				if (this.methodDeclaration.isEmpty()) { // when the invocation is not in a method body
					result.put(((Expression) o).getStartPosition(), "");
				} else {
					result.put(((Expression) o).getStartPosition(),
							packageName + "." + classDeclareation.peek() + "." + methodDeclaration.peek());
				}
			}
		}

		return super.visit(node);
	}
	
	@Override
	public final boolean visit(final MethodInvocation node) {
		if (node.arguments().size() > 0) {
			for (Object o : node.arguments()) {
				if (this.methodDeclaration.isEmpty()) { // when the invocation is not in a method body
					result.put(((Expression) o).getStartPosition(), "");
				} else {
					result.put(((Expression) o).getStartPosition(),
							packageName + "." + classDeclareation.peek() + "." + methodDeclaration.peek());
				}
			}
		}

		return super.visit(node);
	}

	
}
