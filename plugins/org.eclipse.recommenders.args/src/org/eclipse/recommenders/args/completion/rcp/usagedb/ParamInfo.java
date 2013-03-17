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
package org.eclipse.recommenders.args.completion.rcp.usagedb;

import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeLiteral;

/**
 * This is the abstract info of an actual parameter
 * which is to be stored into XML files.
 * Thus, the attributes of this class are all of simple
 * types, such as String and numbers.
 * 
 * @author ChengZhang
 *
 */
public class ParamInfo {
	/** prime number used to compute hash code. */
	public static final int HASH_NUM = 37;
	/** string representation of the parameter. */
	private String paramStr; // non-null
	/** expression type (seems to be a unused field...). */
	private String exprType; // may be null
	/** structure type of the parameter. */
	private String structureType; // non-null
	/** number of dots in the expression. */
	private int dotLen;
	/** instance containing detailed information of method invocation
	 *  (if the parameter is a method invocation).
	 */
	private MethodInvocationType methodInvType = null;
	/** instance containing detailed information of qualified name
	 *  (if the parameter is a qualified name).
	 */
	private QualifiedNameType qualifiedNaType = null;
	
	public final String getParamStr() {
		return paramStr;
	}
	public final void setParamStr(final String pStr) {
		this.paramStr = pStr;
	}
	public final String getExprType() {
		return exprType;
	}
	public final void setExprType(final String eType) {
		this.exprType = eType;
	}
	public final String getStructureType() {
		return structureType;
	}
	public final void setStructureType(final String sType) {
		this.structureType = sType;
	}
	public final int getDotLen() {
		return dotLen;
	}
	public final void setDotLen(final int dLen) {
		this.dotLen = dLen;
	}
	
	public final MethodInvocationType getMethodInvType() {
		return methodInvType;
	}
	public final void setMethodInvType(final MethodInvocationType mInvType) {
		this.methodInvType = mInvType;
	}
	public final QualifiedNameType getQualifiedNaType() {
		return qualifiedNaType;
	}
	public final void setQualifiedNaType(final QualifiedNameType qualNaType) {
		this.qualifiedNaType = qualNaType;
	}
	
	/**
	 * Analyze detailed information of the parameter expression
	 * and fill in the corresponding fields.
	 * 
	 * @param param - the parameter expression
	 * @param enclosingType - the enclosing type
	 */
	public final void setParamExpr(
			final Expression param, final TypeDeclaration enclosingType) {
		String sType = resolveStructureType(param);
		this.structureType = sType;
		if (sType.equals("MethodInvocation")) {
			methodInvType = new MethodInvocationType();
			MethodInvocation mi = (MethodInvocation) param;
			String methodName = mi.getName().getIdentifier();
			methodInvType.setMethodName(methodName);
			//Now not consider the modifier of a method
			methodInvType.setModifier("null");
			methodInvType.setDeclareType("null");
			Expression invokeExpr = mi.getExpression();
			
			//local method 
			if (invokeExpr == null) {
				String enclosingTypeName = enclosingType.resolveBinding().getErasure().getQualifiedName();
				methodInvType.setInvokeType(enclosingTypeName);
				methodInvType.setFirstElementType(enclosingTypeName);
				ITypeBinding tpBinding = enclosingType.resolveBinding();
				do {
					for (IMethodBinding method:tpBinding.getDeclaredMethods()) {
						if (method.equals(mi.resolveMethodBinding())) {
							methodInvType.setDeclareType(tpBinding.getErasure().getQualifiedName());
						}
					}
					tpBinding = tpBinding.getSuperclass();
				} while (tpBinding != null);
//				}while((tpBinding = tpBinding.getSuperclass())!= null);
			} else {
				ITypeBinding invokeType = invokeExpr.resolveTypeBinding();
				/*for(IMethodBinding method: invokeType.getDeclaredMethods())
				{
					if (method.equals(mi.resolveMethodBinding()))
					{
						if (Modifier.isStatic(method.getModifiers()))
							methodInvType.setModifier("static");	
					}
				}*/
				if (invokeType != null)	{
					methodInvType.setInvokeType(invokeType.getErasure().getQualifiedName());
					ITypeBinding tpBinding = invokeType;
					do {
						for (IMethodBinding method:tpBinding.getDeclaredMethods()) {
							if (method.equals(mi.resolveMethodBinding())) {
								methodInvType.setDeclareType(tpBinding.getErasure().getQualifiedName());
							}
						}
						tpBinding = tpBinding.getSuperclass();
					} while (tpBinding != null);
				} else {
					methodInvType.setInvokeType("unknown");
				}
				while (!((invokeExpr instanceof SimpleName)
						|| (invokeExpr instanceof ThisExpression)
						|| (invokeExpr instanceof ArrayAccess))) {
					if (invokeExpr instanceof MethodInvocation) {
						MethodInvocation temp = (MethodInvocation) invokeExpr;
						invokeExpr = temp.getExpression();
					} else if (invokeExpr instanceof QualifiedName) {
						QualifiedName temp = (QualifiedName) invokeExpr;
						invokeExpr = temp.getQualifier();
					} else if (invokeExpr instanceof FieldAccess) {
						FieldAccess temp = (FieldAccess) invokeExpr;
						invokeExpr = temp.getExpression();
					} else {
						methodInvType.setFirstElementType("unknown");
						break;
					}
				}
				if (invokeExpr instanceof SimpleName) {
					ITypeBinding firstBinding = invokeExpr.resolveTypeBinding();
					if (firstBinding != null) {	
						/*The strategy to check whether a method is static is quite simple.
						 * We just check whether the variable name invoking the methods equals its type name
						 */
						String firstBindingStr = firstBinding.getErasure().getQualifiedName();
						int pos = firstBindingStr.lastIndexOf(".");
						if (pos != -1) {
							firstBindingStr = firstBindingStr.substring(pos + 1, firstBindingStr.length());
							SimpleName invokeName = (SimpleName) invokeExpr;
							if (invokeName.getIdentifier().equals(firstBindingStr)) {
								methodInvType.setModifier("static");
							}
						}
						methodInvType.setFirstElementType(firstBinding.getErasure().getQualifiedName());
					} else {
						methodInvType.setFirstElementType("unknown");
					}
				} else if (invokeExpr instanceof ThisExpression) {
					ITypeBinding firstBinding = invokeExpr.resolveTypeBinding();
					if (firstBinding != null) {	
						for (IMethodBinding method: invokeType.getDeclaredMethods()) {
							if (method.equals(mi.resolveMethodBinding()))	//may be wrong if the dotlen > 2 
							{
								if (Modifier.isStatic(method.getModifiers())) {
									methodInvType.setModifier("static");	
								}
							}
						}			
						methodInvType.setFirstElementType(firstBinding.getErasure().getQualifiedName());
					} else {
						methodInvType.setFirstElementType("unknown");
					}
					
				} else if (invokeExpr instanceof ArrayAccess) {
					ITypeBinding firstBinding = invokeExpr.resolveTypeBinding();
					if (firstBinding != null) {
						methodInvType.setFirstElementType(firstBinding.getErasure().getQualifiedName());
					} else {
						methodInvType.setFirstElementType("unknown");
					}
				}
			}
		} else if (sType.equals("QualifiedName")) {
			qualifiedNaType = new QualifiedNameType();
			if (param instanceof QualifiedName) {
				QualifiedName qi = (QualifiedName) param;
				String simpleName = qi.getName().getIdentifier();
				qualifiedNaType.setSimpleName(simpleName);
				qualifiedNaType.setModifier("null");
				qualifiedNaType.setDeclareType("null");
				Name qualifier = qi.getQualifier();
				ITypeBinding qType = qualifier.resolveTypeBinding();
				if (qType != null) {
					qualifiedNaType
						.setQualiferType(qType.getErasure().getQualifiedName());
					ITypeBinding tpBinding = qType;
					do {
						for (IVariableBinding var:tpBinding.getDeclaredFields()) {
							if (var.getName().equals(simpleName)) {
								qualifiedNaType
									.setDeclareType(tpBinding.getErasure().getQualifiedName());
							}
						}
						tpBinding = tpBinding.getSuperclass();
					} while (tpBinding != null);
//					}while((tpBinding = tpBinding.getSuperclass())!= null);
				} else {
					qualifiedNaType.setQualiferType("unknown");
				}
				/*for(IVariableBinding var:qType.getDeclaredFields())
				{
					if (var.getName().equals(simpleName))
					{
						if (Modifier.isStatic(var.getModifiers()))
							qualifiedNaType.setModifier("static");	
					}
				} */
				while (qualifier instanceof QualifiedName) {
					QualifiedName tmpQualifiedName = (QualifiedName) qualifier;
					qualifier = tmpQualifiedName.getQualifier();
				}
				
				ITypeBinding firstBinding = qualifier.resolveTypeBinding();
				if (firstBinding != null) {
					String firstBindingStr = firstBinding.getErasure().getQualifiedName();
					int pos = firstBindingStr.lastIndexOf(".");
					if (pos != -1) {
						firstBindingStr = firstBindingStr.substring(pos + 1, firstBindingStr.length());
						SimpleName name = (SimpleName) qualifier;
						if (name.getIdentifier().equals(firstBindingStr)) {
							qualifiedNaType.setModifier("static");
						}
					}
					qualifiedNaType.setFirstElementType(firstBinding.getErasure().getQualifiedName());
				} else {
					qualifiedNaType.setFirstElementType("unknown");
				}
			} else if (param instanceof FieldAccess) {
				FieldAccess fa = (FieldAccess) param;
				String identifier = fa.getName().getIdentifier();
				qualifiedNaType.setSimpleName(identifier);
				qualifiedNaType.setModifier("null");
				Expression fieldExpr = fa.getExpression();
				if (fieldExpr == null) {
					qualifiedNaType.setQualiferType("null");
					qualifiedNaType.setFirstElementType("null");
				} else {
					ITypeBinding fieldType = fieldExpr.resolveTypeBinding();
					if (fieldType != null) {
						qualifiedNaType.setQualiferType(fieldType.getErasure().getQualifiedName());
						ITypeBinding tpBinding = fieldType;
						do {
							for (IVariableBinding var:tpBinding.getDeclaredFields()) {
								if (var.getName().equals(identifier)) {
									qualifiedNaType.setDeclareType(tpBinding.getErasure().getQualifiedName());
								}
							}
							tpBinding = tpBinding.getSuperclass();
						} while (tpBinding != null);
					} else {
						qualifiedNaType.setQualiferType("unknown");
					}
					while (!((fieldExpr instanceof SimpleName)
							|| (fieldExpr instanceof ThisExpression)
							|| (fieldExpr instanceof ArrayAccess))) {
						if (fieldExpr instanceof MethodInvocation) {
							MethodInvocation temp = 
									(MethodInvocation) fieldExpr;
							fieldExpr = temp.getExpression();
						} else if (fieldExpr instanceof QualifiedName) {
							QualifiedName temp = (QualifiedName) fieldExpr;
							fieldExpr = temp.getQualifier();
						} else if (fieldExpr instanceof FieldAccess) {
							FieldAccess temp = (FieldAccess) fieldExpr;
							fieldExpr = temp.getExpression();
						} else {
							qualifiedNaType.setFirstElementType("unknown");
							break;
						}
					}
					if (fieldExpr instanceof SimpleName) {
						ITypeBinding firstBinding = 
								fieldExpr.resolveTypeBinding();
						if (firstBinding != null) {
							String firstBindingStr = firstBinding.getErasure().getQualifiedName();
							int pos = firstBindingStr.lastIndexOf(".");
							if (pos != -1) {
								firstBindingStr = firstBindingStr.substring(pos + 1, firstBindingStr.length());
								SimpleName name = (SimpleName) fieldExpr;
								if (name.getIdentifier().equals(firstBindingStr)) {
									qualifiedNaType.setModifier("static");
								}
							}
							qualifiedNaType.setFirstElementType(firstBinding.getErasure().getQualifiedName());
						} else {
							qualifiedNaType.setFirstElementType("unknown");
						}
					} else if (fieldExpr instanceof ThisExpression) {
						ITypeBinding firstBinding = fieldExpr.resolveTypeBinding();
						if (firstBinding != null) {
							for (IVariableBinding var:firstBinding.getDeclaredFields())	{
								if (var.getName().equals(identifier)) {
									if (Modifier.isStatic(var.getModifiers())) {
										qualifiedNaType.setModifier("static");	
									}
								}
							} 
							qualifiedNaType.setFirstElementType(firstBinding.getErasure().getQualifiedName());
						} else {
							qualifiedNaType.setFirstElementType("unknown");
						}
						
					} else if (fieldExpr instanceof ArrayAccess) {
						ITypeBinding firstBinding = fieldExpr.resolveTypeBinding();
						if (firstBinding != null) {
							qualifiedNaType.setFirstElementType(firstBinding.getErasure().getQualifiedName());
						} else {
							qualifiedNaType.setFirstElementType("unknown");
						}
					}
				
				}
			}
		}
	}
	
	/**
	 * We only check the three essential fields : 
	 * paramStr, exprType, and structureType.
	 * 
	 * @param obj another object for equality checking
	 * @return whether the two objects are equal
	 */
	public final boolean equals(final Object obj) {
		if (obj instanceof ParamInfo) {
			ParamInfo otherPi = (ParamInfo) obj;
			
			if (this.exprType == null && otherPi.exprType == null) {
				return this.paramStr.equals(otherPi.paramStr) 
						&& this.structureType.equals(otherPi.structureType);
			} else if (this.exprType != null && otherPi.exprType != null) {
				if (this.exprType.equals(otherPi.exprType)) {
					return this.paramStr.equals(otherPi.paramStr) 
							&& this.structureType.equals(otherPi.structureType);
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
		int exprTypeCode = 0;
		if (this.exprType != null) {
			exprTypeCode =  this.exprType.hashCode();
		}
		int paramStrCode = this.paramStr.hashCode();
		int structureTypeCode = this.structureType.hashCode();
		
		return (paramStrCode * HASH_NUM + structureTypeCode) * HASH_NUM + exprTypeCode;
	}
	
	/**
	 * Returns a string literal to indicate the parameter's structure type.
	 * 
	 * @param expr - the parameter expression
	 * @return - string literal to indicate the parameter's structure type
	 */
	public static String resolveStructureType(final Expression expr) {
		if (expr instanceof SimpleName) {
			return "SimpleName";
		} else if (expr instanceof NullLiteral) {
			return "NullLiteral";
		} else if (expr instanceof MethodInvocation) {
			// currently we do not set the length limit (2 or 3),
			// however, we will probably set it if it is necessary.
			return "MethodInvocation";
		} else if (expr instanceof QualifiedName) {
			// currently we do not set the length limit (2 or 3),
			// however, we will probably set it if it is necessary.
			return "QualifiedName";
		} else if (expr instanceof StringLiteral) {
			return "StringLiteral";
		} else if (expr instanceof BooleanLiteral) {
			return "BooleanLiteral";
		} else if (expr instanceof NumberLiteral) {
			return "NumberLiteral";
		} else if (expr instanceof ArrayAccess) {
			return "ArrayAccess";
		} else if (expr instanceof ThisExpression) {
			return "ThisExpression";
		} else if (expr instanceof CastExpression) {
			return "CastExpression";
		} else if (expr instanceof FieldAccess) {
			return "QualifiedName";
		} else if (expr instanceof CharacterLiteral) {
			return "CharacterLiteral";
		} else if (expr instanceof TypeLiteral) {
			return "TypeLiteral";
		} else {
			return "Uninteresting";
		}
	}
}
