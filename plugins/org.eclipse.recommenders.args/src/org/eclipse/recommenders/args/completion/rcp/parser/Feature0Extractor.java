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

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.recommenders.args.completion.rcp.entities.FormalParameter;

/**
 * This visitor finds out, for each parameter used in each method call,
 * the parameter expression and the corresponding formal parameter.
 * 
 * However, the currently strategy to find the formal parameter is not
 * so good. Perhaps we have to find a better solution.
 * 
 */
public class Feature0Extractor extends ASTVisitor {
	/** map between each parameter's start position and the parameter expression. */
	private Map<Integer, Expression> result = new HashMap<Integer, Expression>();
	/** map between each parameter's start position and its enclosing class. */
	private Map<Integer, TypeDeclaration> pos2enclosingClass = new HashMap<Integer, TypeDeclaration>();
	/** map between each parameter's start position and its corresponding formal parameter. */
	private Map<Integer, FormalParameter> pos2fp = new HashMap<Integer, FormalParameter>();
	/** a stack to record enclosing classes, with the inner most class at its top. */
	private Stack<TypeDeclaration> classContext = new Stack<TypeDeclaration>();
	
	public final Map<Integer, Expression> getResult() {
		// returns a map that contains all pairs of (paraID, paramExpr)
		return result;
	}
	
	public final Map<Integer, TypeDeclaration> getPos2enclosingClass() {
		return pos2enclosingClass;
	}

	public final Map<Integer, FormalParameter> getPos2Fp() {
		return pos2fp;
	}
	
	@Override
	public final void preVisit(final ASTNode node) {
		if (node instanceof TypeDeclaration) {
			classContext.push(((TypeDeclaration) node));
		}
	}
	
	@Override
	public final void postVisit(final ASTNode node) {
		if (node instanceof TypeDeclaration) {
			classContext.pop();
		}
	}

	@Override
	public final boolean visit(final ConstructorInvocation node) {
//		node.resolveConstructorBinding().getReturnType();
		return super.visit(node);
	}
	
	@Override
	public final boolean visit(final ClassInstanceCreation node) {
		IMethodBinding mBinding = node.resolveConstructorBinding();
		if (mBinding == null) {
			return super.visit(node);
		}
		ITypeBinding[] formalParamTypes = mBinding.getParameterTypes();
		
		// compose the string representation of the type list of the current called method.
		StringBuilder paramListSb = new StringBuilder();
		for (ITypeBinding tmpFpType : formalParamTypes) {
			if (tmpFpType != null) {
				paramListSb.append(tmpFpType.getErasure().getQualifiedName());
				paramListSb.append(",");
			} else {
				paramListSb.append("null,");
			}
		}
		
		int paramIndex = 1;
		for (Object o : node.arguments()) {
			// directly record the parameter expression for each position of argument
			result.put(((Expression) o).getStartPosition(), ((Expression) o));
			if (o instanceof MethodInvocation) {
				MethodInvocation miParam = (MethodInvocation) o;
				Expression baseOfMiParam = miParam.getExpression();
				if (baseOfMiParam == null) { // only when the method invocation is local
					TypeDeclaration enclosingClassName = classContext.peek();
					pos2enclosingClass.put(((Expression) o).getStartPosition(), enclosingClassName);
				}
			}
			
			// try to find out the formal parameter of the current position of argument
			FormalParameter fp = new FormalParameter();
//			Expression expr = node.getExpression();
			Type t = node.getType();
			
			//t.resolveBinding();//node.resolveConstructorBinding().getDeclaringClass();
			ITypeBinding binding = node.resolveTypeBinding(); 
			
			if (binding != null) {
				fp.setClassName(binding.getErasure().getQualifiedName()); 
				// TESTME: test the validity of the call to getErasure()
			} else {
				TypeDeclaration enclosingClass = classContext.peek(); 
				fp.setClassName(enclosingClass.resolveBinding().getQualifiedName()); // TODO: find a better solution.
			}
			
			fp.setMethodName(node.getType().toString());
			//node.getName().getFullyQualifiedName());
			fp.setParamIndex(paramIndex);
			fp.setParamListStr(paramListSb.toString());
			
			
			ITypeBinding fpTypeBinding = null;
			if (mBinding != null) {
				if (formalParamTypes.length >= paramIndex) {
					fpTypeBinding = formalParamTypes[paramIndex - 1]; // the paramIndex starts from 1 ...
				}
			}
			if (fpTypeBinding != null) {
				fp.setParamType(fpTypeBinding.getErasure().getQualifiedName());
			} else {
				fp.setParamType("null");
			}
			
			// record the formal parameter for each position of argument
			pos2fp.put(((Expression) o).getStartPosition(), fp);
			paramIndex++;
		}
		
		return super.visit(node);
	}
	
	@Override
	public final boolean visit(final MethodInvocation node) {
		// these lines code are very likely to be a relic of debugging...
		/*if (node.toString().contains("tipLabelImage.setForeground(display")) {
			int aaa = 0;
			aaa = 1;
		}*/
		IMethodBinding mBinding = node.resolveMethodBinding();
		if (mBinding == null) {
			return super.visit(node);
		}
		ITypeBinding[] formalParamTypes = mBinding.getParameterTypes();
		
		// compose the string representation of the type list of the current called method.
		StringBuilder paramListSb = new StringBuilder();
		for (ITypeBinding tmpFpType : formalParamTypes) {
			if (tmpFpType != null) {
				paramListSb.append(tmpFpType.getErasure().getQualifiedName());
				paramListSb.append(",");
			} else {
				paramListSb.append("null,");
			}
		}
		
		int paramIndex = 1;
		for (Object o : node.arguments()) {
			// directly record the parameter expression for each position of argument
			result.put(((Expression) o).getStartPosition(), ((Expression) o));
			if (o instanceof MethodInvocation) {
				MethodInvocation miParam = (MethodInvocation) o;
				Expression baseOfMiParam = miParam.getExpression();
				if (baseOfMiParam == null) { // only when the method invocation is local
					TypeDeclaration enclosingClassName = classContext.peek();
					pos2enclosingClass.put(((Expression) o).getStartPosition(), enclosingClassName);
				}
			}
			
			// try to find out the formal parameter of the current position of argument
			FormalParameter fp = new FormalParameter();
			Expression expr = node.getExpression();
			
			// NOTE: here we store the explicit type of the base variable, 
			// instead of the declaring type of the called method.
			if (expr != null) { 				
				ITypeBinding binding = expr.resolveTypeBinding();
				if (binding != null) {
					fp.setClassName(binding.getErasure().getQualifiedName()); 
					// TESTME: test the validity of the call to getErasure()
				} else {
					fp.setClassName(expr.toString());  // TODO: find a better solution.
				}
			} else {
				TypeDeclaration enclosingClass = classContext.peek(); // may be inaccurate due to inheritance
				fp.setClassName(enclosingClass.resolveBinding().getQualifiedName());
			}
			
			fp.setMethodName(node.getName().getIdentifier());
			fp.setParamIndex(paramIndex);
			fp.setParamListStr(paramListSb.toString());
			
			
			ITypeBinding fpTypeBinding = null;
			if (mBinding != null) {
				if (formalParamTypes.length >= paramIndex) {
					fpTypeBinding = formalParamTypes[paramIndex - 1]; // the paramIndex starts from 1 ...
				}
			}
			if (fpTypeBinding != null) {
				fp.setParamType(fpTypeBinding.getErasure().getQualifiedName());
			} else {
				fp.setParamType("null");
			}
			
			// record the formal parameter for each position of argument
			pos2fp.put(((Expression) o).getStartPosition(), fp);
			paramIndex++;
		}
		
		return super.visit(node);
	}

}
