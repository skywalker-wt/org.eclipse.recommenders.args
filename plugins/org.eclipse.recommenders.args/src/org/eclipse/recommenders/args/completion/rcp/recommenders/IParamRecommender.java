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

import java.util.List;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.recommenders.args.completion.rcp.entities.FormalParameter;
import org.eclipse.recommenders.args.completion.rcp.ml.data.InstanceData;

/**
 * Common interface that is implemented by the core recommenders.
 * Different recommenders may use various kinds of underlying models,
 * such as k-NN and Bayesian network.
 * 
 * @author ChengZhang
 *
 */
public interface IParamRecommender {

	void setTypeDecl(TypeDeclaration td);

	void setLocals(List<Name> list);

	void setParamExpr(Expression tmpExpr);

	void setFormalParam(FormalParameter fp);

	void setGlobalInsData(InstanceData gInsData);

	void setInvocationInsDataList(List<InstanceData> invocationInsDataList);

	void setJavaProject(IJavaProject javaProject);
	
	/**
	 * Core method which generates the recommendations.
	 */
	void recommendParam();

	List<Candidate> getRecommendations();
}
