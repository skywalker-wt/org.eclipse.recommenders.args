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
package org.eclipse.recommenders.args.completion.rcp.recommenders;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.recommenders.args.completion.rcp.entities.BaseVariable;
import org.eclipse.recommenders.args.completion.rcp.entities.FormalParameter;
import org.eclipse.recommenders.args.completion.rcp.ml.data.InstanceData;
import org.eclipse.recommenders.args.completion.rcp.parser.Feature0Extractor;
import org.eclipse.recommenders.args.completion.rcp.parser.Feature1Extractor;
import org.eclipse.recommenders.args.completion.rcp.parser.Feature2Extractor;
import org.eclipse.recommenders.args.completion.rcp.parser.Feature3forRecExtractor;
import org.eclipse.recommenders.args.completion.rcp.parser.Feature7Extractor;

/** Introduction: This class is used in jdt.ui plug-in,
                 to add precise recommendation to the default java recommendation
 * Usage: In jdt.ui, we should first setClassName, setSrcString, setMethodName,
		  setParameterNames, setParameterTypes, setInvocationOffset.
		  Then call Parse() and Recommend()        
 */
public class PreciseParamRecommender {
	/* 
	 * get from the context, anyone should set these values, 
	 * then call Parse() function
	 */
	/** the under-edit compilation unit (in JDT Java Model). */
	private ICompilationUnit icu;
	/** the string of source code being editted. */
	private String srcString = "";
	/** current invoking method. */
	private StringBuffer methodName = null;
	/** array of parameter names. */
	private char[][] parameterNames = null;
	/** arry of parameter types. */
	private String[] parameterTypes = null;
	/** the class name of the receiver. */
	private String className = null;
	/** position of the cursor now. */
	private int currentPosition = -1;
	/** name of the visible variables. */
	private List<String> visibleNames;
	public final List<String> getVisibleNames() {
		return visibleNames;
	}

	public final void setVisibleNames(final List<String> vNames) {
		this.visibleNames = vNames;
	}

	/** the position just after the "." of "a.", "a." is the coder is writing. */
	private int invocationOffset = -1;
	
	/** positions of parameters. */
	private List<Integer> paramPositions = new ArrayList<Integer>();
	/** list of recommendations . */
	private List<List<Candidate>> recommendationsList = new ArrayList<List<Candidate>>();
	
	// for recommending
	/** extractor of feature 0. */
	private Feature0Extractor f0 = null;
	/** extractor of feature 1. */
	private Feature1Extractor f1 = null;
	/** extractor of feature 2. */
	private Feature2Extractor f2 = null;
	/** extractor of feature 3 for recommendation (instead of database building). */
	private Feature3forRecExtractor f3 = null;
	/** extractor of feature 7. */
	private Feature7Extractor f7 = null;
	/** compilation unit in DOM/AST API. */
	private CompilationUnit completeCU = null;
	
	/** the map between position & argument (actual parameter). */
	private AccessibleVariableSearcher identifier = null;

	/**
	 * In order to use recommendation part of precise,
	 * add the methodName & paramName to the src string 
	 * where request the recommendation.
	 */
	private void modifySrcString() {
		StringBuffer tempSrc = new StringBuffer(srcString);
		int dotOffset = 0;
		while (invocationOffset > dotOffset && dotOffset < methodName.length()) {
			if (srcString.charAt(invocationOffset - 1 - dotOffset) == '.') {
				break;
			}
			dotOffset++;
		}
		invocationOffset -= dotOffset;
		
		for (int i = 0; i < methodName.length(); i++) {
			if (srcString.charAt(invocationOffset + i) != methodName.charAt(i)) {
				for (int j = i; j < methodName.length(); j++) {
					tempSrc.insert(invocationOffset + j, methodName.charAt(j));
				}
				break;
			}
		}
		
		int pos = invocationOffset + methodName.length();
		tempSrc.insert(pos, '(');
		pos++;
		for (int i = 0; i < parameterNames.length; i++) {
			paramPositions.add(pos);
			if (i < parameterNames.length - 1) {
				String paramName = new String(parameterNames[i]);
				tempSrc.insert(pos, paramName + ", ");
				pos += paramName.length() + 2;
			} else {  // the last param
				String paramName = new String(parameterNames[i]);
				tempSrc.insert(pos, paramName + ");");
			}
		}
		srcString = tempSrc.toString();
	}
	
	/**
	 * Adjust the source code and extract static features.
	 */
	public final void parse() {
		modifySrcString();
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		//parser.setSource(icu);
		parser.setSource(srcString.toCharArray());
		parser.setProject(icu.getJavaProject());
		IPath path = icu.getPath();
		parser.setUnitName(path.toString());
		
		parser.setResolveBindings(true);
		parser.setStatementsRecovery(true);
		completeCU = (CompilationUnit) parser.createAST(null);

		f0 = new Feature0Extractor();
		completeCU.accept(f0);
		identifier = new AccessibleVariableSearcher();
		completeCU.accept(identifier);
		f1 = new Feature1Extractor();
		completeCU.accept(f1);
		f2 = new Feature2Extractor();
		completeCU.accept(f2);
		f3 = new Feature3forRecExtractor();
		completeCU.accept(f3);
		f7 = new Feature7Extractor();
		completeCU.accept(f7);
	}
	
	/**
	 * The main method which invokes the computation of recommendation items.
	 */
	public final void recommend() {
		// after the feature extraction, it is about to compose the test data
		// instances
		Map<Integer, Expression> pos2arg = identifier.getPos2ArgMap();
		Map<Integer, List<Name>> pos2locals = identifier.getResult();
		Map<Integer, TypeDeclaration> pos2typeDecl = identifier
				.getPos2TypeDecl();
		Set<Entry<Integer, Expression>> entrySet = pos2arg.entrySet();
		
		// iterate all the arguments to get the recommendation
		for (int i = 0; i < paramPositions.size(); i++) {
			if (currentPosition != -1 && i != currentPosition) {
				recommendationsList.add(new ArrayList<Candidate>());
				continue;
			}
			int tmpPos = paramPositions.get(i);
			Expression tmpExpr = null;
			
			for (Entry<Integer, Expression> entry : entrySet) {
				if (entry.getKey() == tmpPos) {
					tmpExpr = entry.getValue();
					break;
				}
			}
			
			if (tmpExpr == null) {
				// impossible here, means can't find the parameter exp in src
				recommendationsList.add(new ArrayList<Candidate>());
				continue;
			}
			
			eraseParameterTypes();
			FormalParameter fp = new FormalParameter();
			fp.setClassName(className);
			fp.setMethodName(methodName.toString());
			fp.setParamIndex(i + 1);
			fp.setParamListStr(getParamListStr());
			fp.setParamType(parameterTypes[i]);
			
			TypeDeclaration td = pos2typeDecl.get(tmpPos);
		
			InstanceData gInsData = new InstanceData();
			if (f1 != null) {
				gInsData.setFeature1(f1.getResult().get(tmpPos));
			}
			if (f2 != null) {
				gInsData.setFeature2(f2.getResult().get(tmpPos));
			}
			gInsData.setFeature3(new ArrayList<String>());

			if (f7 != null) {
				gInsData.setFeature7(f7.getResult().get(tmpPos));
			}

			List<InstanceData> invocationInsDataList = new ArrayList<InstanceData>();
			if (f3 != null) {
				Map<Integer, Map<BaseVariable, List<String>>> pos2allInvocations = f3
						.getPos2AllInvocations();
				Map<BaseVariable, List<String>> occurredInvocations = pos2allInvocations
						.get(tmpPos);

				if (occurredInvocations != null) {
					Set<Entry<BaseVariable, List<String>>> invEntrySet = occurredInvocations
							.entrySet();

					for (Entry<BaseVariable, List<String>> invEntry : invEntrySet) {
						// TODO: here we may have to check the type of the
						// SimpleName to reduce the candidate base variables.
						InstanceData invInsData = new InstanceData();
						invInsData.setBaseVar(invEntry.getKey());

						if (f1 != null) {
							invInsData.setFeature1(f1.getResult().get(tmpPos));
						}
						if (f2 != null) {
							invInsData.setFeature2(f2.getResult().get(tmpPos));
						}

						invInsData.setFeature3(invEntry.getValue());

						if (f7 != null) {
							invInsData.setFeature7(f7.getResult().get(tmpPos));
						}

						invocationInsDataList.add(invInsData);
					}
				}
			}

			IParamRecommender precise = new ParamRecommenderWithBN();
			precise.setFormalParam(fp);
			precise.setGlobalInsData(gInsData);
			precise.setInvocationInsDataList(invocationInsDataList);
			precise.setParamExpr(tmpExpr);
			precise.setLocals(filterVisibleVariables(pos2locals.get(tmpPos)));
			precise.setTypeDecl(td);
			precise.setJavaProject(icu.getJavaProject());
			precise.recommendParam();
			
			List<Candidate> recommendations = precise.getRecommendations();
			recommendationsList.add(recommendations);
		}
	}
	
	/**
	 * Filter out local variables, retaining some special visible variables.
	 * 
	 * @param before - the list of names before being filtered
	 * @return the list of names after being filtered
	 */
	private List<Name> filterVisibleVariables(final List<Name> before) {
		List<Name> after = new ArrayList<Name>();
		
		for (Name n : before) {
			if (n.isSimpleName()) { // TODO: test this branch.
				SimpleName sn = (SimpleName) n;
				if (visibleNames.contains(sn.getIdentifier())) {
					after.add(n);
				}
			} else {
				after.add(n);
			}
		}
		
		return after;
	}
	
	/**
	 * Produce a string to represent the list of parameter types.
	 * 
	 * @return string representation of the list of parameter types
	 */
	private String getParamListStr() {
		StringBuffer sBuffer = new StringBuffer();
		for (int i = 0; i < parameterTypes.length; i++) {
			sBuffer.append(parameterTypes[i] + ",");
		}
		return sBuffer.toString();
	}
	
	public final String getClassName() {
		return className;
	}
	
	/**
	 * Process the raw class name string and extract the normal class name.
	 * For example, "Lorg.eclipse.swt.browser.Browser;"
	 * 
	 * @param clsName - the original class name
	 */
	public final void setClassName(final String clsName) {
		String tmp = clsName;
		int pos = tmp.indexOf(";");
		if (pos != -1) {
			tmp = tmp.substring(0, pos);
		}
		pos = tmp.indexOf("L");
		if (pos != -1) {
			tmp = tmp.substring(pos + 1);
		}
		this.className = tmp;
	}
	
	public final ICompilationUnit getIcu() {
		return icu;
	}

	public final void setIcu(final ICompilationUnit cu) {
		this.icu = cu;
	}

	public final String getSrcString() {
		return srcString;
	}

	public final void setSrcString(final String srcStr) {
		this.srcString = srcStr;
	}

	public final StringBuffer getMethodName() {
		return methodName;
	}

	public final void setMethodName(final StringBuffer mName) {
		this.methodName = mName;
	}

	public final char[][] getParameterNames() {
		return parameterNames;
	}

	public final void setParameterNames(final char[][] paramNames) {
		this.parameterNames = paramNames;
	}

	public final String[] getParameterTypes() {
		return parameterTypes;
	}

	public final void setParameterTypes(final String[] paramTypes) {
		this.parameterTypes = paramTypes;
	}

	public final int getInvocationOffsets() {
		return invocationOffset;
	}

	public final void setInvocationOffset(final int invOffset) {
		this.invocationOffset = invOffset;
	}
	
	public final List<List<Candidate>> getRecommendationsList() {
		return recommendationsList;
	}

	public final void setRecommendationsList(final List<List<Candidate>> recList) {
		this.recommendationsList = recList;
	}
	
	public final int getCurrentPosition() {
		return currentPosition;
	}

	public final void setCurrentPosition(final int curPosition) {
		this.currentPosition = curPosition;
	}

	/**
	 * Manually erase the information of generic type parameters.
	 */
	public final void eraseParameterTypes() {
		if (parameterTypes != null) {
			for (int i = 0; i < parameterTypes.length; i++) {
				if (parameterTypes[i] != null && parameterTypes[i].contains("<") && parameterTypes[i].contains(">")) {
					parameterTypes[i] = parameterTypes[i].substring(0, parameterTypes[i].indexOf("<"));
				}
			}
		}
	}
}
