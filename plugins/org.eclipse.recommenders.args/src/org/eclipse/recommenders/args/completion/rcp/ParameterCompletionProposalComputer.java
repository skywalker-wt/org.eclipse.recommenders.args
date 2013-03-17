/**
 * Copyright (c) 2011, 2012 Shanghai Jiao Tong University.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *    Tong Wu - initial API and implementation.
 */
package org.eclipse.recommenders.args.completion.rcp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.CompletionProposal;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.internal.codeassist.InternalCompletionProposal;
import org.eclipse.jdt.internal.codeassist.complete.CompletionOnQualifiedAllocationExpression;
import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.eclipse.jdt.internal.corext.template.java.AbstractJavaContextType;
import org.eclipse.jdt.internal.corext.template.java.JavaContext;
import org.eclipse.jdt.internal.corext.template.java.JavaContextType;
import org.eclipse.jdt.internal.corext.template.java.SignatureUtil;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.text.template.contentassist.TemplateProposal;
import org.eclipse.jdt.ui.ISharedImages;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jdt.ui.text.java.ContentAssistInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposalComputer;
import org.eclipse.jdt.ui.text.java.JavaContentAssistInvocationContext;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.recommenders.args.completion.rcp.recommenders.Candidate;
import org.eclipse.recommenders.args.completion.rcp.recommenders.PreciseParamRecommender;
import org.eclipse.recommenders.completion.rcp.IRecommendersCompletionContext;
import org.eclipse.recommenders.completion.rcp.IRecommendersCompletionContextFactory;

/**
 * This class implements the extension points of javaCompletionProposalComputer.
 * 
 * It initial the context of the quest. Call the method of PreciseParamRecommender.
 * Then show the result to the user.
 * 
 * @author Tong
 *
 */
public class ParameterCompletionProposalComputer implements IJavaCompletionProposalComputer {
	/**
	 * The maximum number of the proposals limited.
	 */
	public static final int RECOMMENDATION_LIMIT = 10;
	
	/**
	 * the context factory which could create IRecommendersCompletionContext.
	 */
    private IRecommendersCompletionContextFactory ctxFactory;
    
    /**
     * The special assist context for Java. 
     */
    private JavaContentAssistInvocationContext jdtContext;
    
    /**
     * Assist context created from JavaContentAssistInvocationContext.
     * Contains more detail context information. (Visible Fields, Visible Locals) 
     */
    private IRecommendersCompletionContext crContext;

    /**
     * Constructor for this class, created from context factory.
     * 
     * @param cFactory - the context factory from which the context of code completion is created.
     */
    @Inject
    public ParameterCompletionProposalComputer(final IRecommendersCompletionContextFactory cFactory) {
        this.ctxFactory = cFactory;
    }

    @Override
    public final List<ICompletionProposal> computeCompletionProposals(final ContentAssistInvocationContext javaContext,
            final IProgressMonitor monitor) {
    	List<ICompletionProposal> output = new ArrayList<ICompletionProposal>();
    	
        this.jdtContext = (JavaContentAssistInvocationContext) javaContext;
        this.crContext = ctxFactory.create(jdtContext);
        
        //get the type of the node, and the index of parameter
        ASTNode completeNode = crContext.getCompletionNode().orNull();
        String declarationSignature = null;
        int index = 0;
        if (completeNode instanceof MessageSend) {
        	MessageSend mn = (MessageSend) completeNode;
        	if (mn.actualReceiverType == null) {
        		return Collections.emptyList();
        	}
       		declarationSignature = new String(mn.actualReceiverType.closestMatch().signature()).replace('/', '.');
       		
        	if (mn.arguments != null) {
        		index = mn.arguments.length;
        	}
        } else if (completeNode instanceof CompletionOnQualifiedAllocationExpression) {
        	CompletionOnQualifiedAllocationExpression cn = (CompletionOnQualifiedAllocationExpression) completeNode;
        	declarationSignature = new String(cn.resolvedType.signature()).replace('/', '.');
        	if (cn.arguments != null) {
        		index = cn.arguments.length;
        	}
        } else {
        	return Collections.EMPTY_LIST;
        }
        
        //calculate the possible parameters for every proposal 
        Map<IJavaCompletionProposal, CompletionProposal> proposals = crContext.getProposals();
        for (IJavaCompletionProposal k : proposals.keySet()) {
        	try {
        		InternalCompletionProposal proposol = (InternalCompletionProposal) proposals.get(k);
        		int kind = proposol.getKind();
        		
        		//if is an anonymous class declaration then continue
        		if (kind == CompletionProposal.ANONYMOUS_CLASS_DECLARATION) {
        			continue;
        		}
        		
        		if (getParameterTypes(proposol).length <= index) {
        			continue;
        		}
        		
        		char[][] parameterNames =  proposol.findParameterNames(null);
	        	PreciseParamRecommender preciseParamRecommender = new PreciseParamRecommender();
	        	preciseParamRecommender.setIcu(jdtContext.getCompilationUnit());
	    		preciseParamRecommender.setClassName(declarationSignature);
				preciseParamRecommender.setSrcString(jdtContext.getCompilationUnit().getSource());
				String methodName = "";
				
				if (proposol.getName() == null) {
					String s = declarationSignature;
					s = s.substring(1, s.length() - 1);
					Class ownClass = Class.forName(s);
					methodName = ownClass.getSimpleName();
				} else {
					methodName = new String(proposol.getName());
				}
	    		preciseParamRecommender.setMethodName(new StringBuffer().append(methodName));
	    		preciseParamRecommender.setParameterNames(proposol.findParameterNames(null));
	    		preciseParamRecommender.setParameterTypes(getParameterTypes(proposol));
	    		preciseParamRecommender.setInvocationOffset(jdtContext.getInvocationOffset());
	    		preciseParamRecommender.setCurrentPosition(index);
	    		
	    		// TODO: find a better solution to take into account info other than names
	    		List<IField> visibleFields = crContext.getVisibleFields();
	            List<ILocalVariable> visibleLocals = crContext.getVisibleLocals();
	    		List<String> visibleNames = new ArrayList<String>();
	    		for (IField f : visibleFields) {
	    			visibleNames.add(f.getElementName());
	    		}
	    		for (ILocalVariable v : visibleLocals) {
	    			visibleNames.add(v.getElementName());
	    		}
	    		preciseParamRecommender.setVisibleNames(visibleNames);
	    		
	    		preciseParamRecommender.parse();
	    		preciseParamRecommender.recommend();
	    		
	    		ICompletionProposal[] argsProposals = 
	    				integrateProposals(preciseParamRecommender.getRecommendationsList().get(index));
	    		output.addAll(0, Arrays.asList(argsProposals));
        	} catch (JavaModelException e) {
        		e.printStackTrace();
        	} catch (ClassNotFoundException e) { 
        		e.printStackTrace();
        	}
        }
        List<ICompletionProposal> temp = output;
        Set<String> containsProposal = new HashSet<String>();
        output = new ArrayList<ICompletionProposal>();
        
        for (ICompletionProposal proposal : temp) {
        	if (containsProposal.contains(proposal.getDisplayString())) {
        		continue;
        	}
        	
        	containsProposal.add(proposal.getDisplayString());
        	output.add(proposal);
        }
        
        if (output.size() > RECOMMENDATION_LIMIT) {
        	output = output.subList(0, RECOMMENDATION_LIMIT - 1);
        }
        
		if (output.size() == 0) {
			return Collections.emptyList();
		}
		return output;
    }
    
    /**
     * Get the parameter type name as a string array.
     * @param fProposal - the proposal provided by JDT. 
     * @return the type names as an array of all parameters.
     */
    private String[] getParameterTypes(final CompletionProposal fProposal) {
    	//get the type information of fProposal.
		char[] signature = SignatureUtil.fix83600(fProposal.getSignature());
		char[][] types = Signature.getParameterTypes(signature);

		String[] ret = new String[types.length];
		for (int i = 0; i < types.length; i++) {
			ret[i] = new String(Signature.toCharArray(types[i]));
		}
		
		return ret;
	}
    
    /**
     * Create args proposals for each candidate as a list.
     * Node the candidate created by PreciseParamRecommender.
     * 
     * @param preciseProposals - all of the possible proposal by PreciseParamRecommender.
     * @return the proposals would show to the user.
     */
    private  ICompletionProposal[] integrateProposals(final List<Candidate> preciseProposals) {
    	int total = preciseProposals.size();
		ICompletionProposal[] result = new ICompletionProposal[total];
		for (int i = 0; i < preciseProposals.size(); i++) {
			Candidate candidate = preciseProposals.get(i);
			
			String displayString = candidate.getRecommendStr();
			String replaceString = candidate.getRecommendStr();
			
			result[i] = new ParameterCompletionProposal(createProposal(displayString, "", replaceString, (total - i)));
		}
		
		return result;
	}
    
    /**
     * To create one proposal item. 
     * Note that the relevance is used to rank the item in the proposal list.
     * 
     * @param displayString - the parameter string contained in the proposal
     * @param desc - the extra description of the proposal
     * @param replaceString - currently it is set to be the same as displayString
     * @param relevance - the relevance score used to rank the proposal
     * 
     * @return a reference to the created proposal item
     */
    private TemplateProposal createProposal(
    		final String displayString, 
    		final String desc, 
    		final String replaceString, 
    		final int relevance) {
        final JavaPlugin plugin = JavaPlugin.getDefault();
        final AbstractJavaContextType type =
                (AbstractJavaContextType) plugin.getTemplateContextRegistry().getContextType(JavaContextType.ID_ALL);
        JavaContext documentContext =
                new JavaContext(type, jdtContext.getDocument(), jdtContext.getInvocationOffset(), 0,
                        jdtContext.getCompilationUnit());
        documentContext.setForceEvaluation(true);

        Template template =
                new Template(displayString, desc, JavaContextType.ID_ALL, replaceString, false);
        TemplateProposal p =
                new TemplateProposal(template, documentContext, new Region(jdtContext.getInvocationOffset(), 0), JavaUI
                        .getSharedImages().getImage(ISharedImages.IMG_FIELD_PRIVATE));
        p.setRelevance(relevance);
        
        return p;
    }

    @Override
    public void sessionStarted() {
    }

    @Override
    public final List<IContextInformation> computeContextInformation(final ContentAssistInvocationContext context,
            final IProgressMonitor monitor) {
        return null;
    }

    @Override
    public final String getErrorMessage() {
        return null;
    }

    @Override
    public void sessionEnded() {
    }

}
