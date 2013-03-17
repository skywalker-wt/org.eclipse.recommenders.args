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

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.recommenders.args.completion.rcp.bn.BayesNetSerializer;
import org.eclipse.recommenders.args.completion.rcp.bn.NodeProbPair;
import org.eclipse.recommenders.args.completion.rcp.entities.BaseVariable;
import org.eclipse.recommenders.args.completion.rcp.entities.FormalParameter;
import org.eclipse.recommenders.args.completion.rcp.ml.data.InstanceData;
import org.eclipse.recommenders.args.completion.rcp.usagedb.ParamInfo;
import org.eclipse.recommenders.args.completion.rcp.usagedb.UsageDBJSONReader;
import org.eclipse.recommenders.commons.bayesnet.BayesianNetwork;
import org.eclipse.recommenders.jayes.BayesNet;
import org.eclipse.recommenders.jayes.BayesNode;
import org.eclipse.recommenders.jayes.inference.junctionTree.JunctionTreeAlgorithm;

/**
 * As indicated by its name, this class is the main class which
 * performs parameter recommendation based on Bayesian Network.
 * 
 * @author ChengZhang
 *
 */
public class ParamRecommenderWithBN implements IParamRecommender {
	/** the maximum number of recommendations. */
	public static final int RECOMMENDATION_LIMIT = 10;
	
	/** data for the global instance. */
	private InstanceData globalInsData;
	/** data for instances containing local variables. */
	private List<InstanceData> invocationInsDataList;
	/** the corresponding formal parameter. */
	private FormalParameter formalParam;
	/** the parameter expression. */
	private Expression paramExpr;
	/** list of local variables. */
	private List<Name> locals;
	/** record the field available from the ancestor class. */
	private List<IVariableBinding> hierarchyField;
	/** declaration of the enclosing type. */
	private TypeDeclaration typeDecl;
	
	/** map between parameter indices and detailed parameter information. */
	private Map<Integer, ParamInfo> index2paramInfo;
	/** recommendStrings is used to provide results to jdt.ui  .*/ 
	private List<Candidate> recommendations;
	
	/**
	 * The constructor.
	 */
	public ParamRecommenderWithBN() {
		locals = new ArrayList<Name>();
		hierarchyField = new ArrayList<IVariableBinding>();
		recommendations = new ArrayList<Candidate>();
	}
	
	@Override
	public final void setTypeDecl(final TypeDeclaration td) {
		this.typeDecl = td;
	}

	@Override
	public final void setLocals(final List<Name> list) {
		this.locals = list;
	}

	@Override
	public final void setParamExpr(final Expression tmpExpr) {
		this.paramExpr = tmpExpr;
	}

	@Override
	public final void setFormalParam(final FormalParameter fp) {
		this.formalParam = fp;
	}

	@Override
	public final void setGlobalInsData(final InstanceData gInsData) {
		this.globalInsData = gInsData;
	}

	@Override
	public final void setInvocationInsDataList(
			final List<InstanceData> invInsDataList) {
		this.invocationInsDataList = invInsDataList;
	}

	@Override
	public void setJavaProject(final IJavaProject javaProject) {
		// TODO Auto-generated method stub
		// actually nothing has been done...
	}
	
	@Override
	public final void recommendParam() {
		recommendations = new ArrayList<Candidate>();
		String path = "platform:/plugin/org.eclipse.recommenders.completion.rcp.args/";
		String fileName = "bn_files/" + formalParam.toString();
		String xmlName =  "bn_files/" + formalParam.toString() + ".json";

		/*
		 * Find the field available from the ancestor classes.
		 * Perhaps this info can be retrieved from the JDT context.
		 * */
		ITypeBinding tpBinding = typeDecl.resolveBinding();
		List<ITypeBinding> ancestorClasses = new ArrayList<ITypeBinding>();
		
		if (tpBinding != null) {
			ITypeBinding tmpTb = tpBinding;
			// TODO: check whether the fields in current type are included.
			while ((tmpTb = tmpTb.getSuperclass()) != null) {
				for (IVariableBinding var : tmpTb.getDeclaredFields()) {
					hierarchyField.add(var);
				}
				
				ancestorClasses.add(tmpTb);
			}
		}
		
		try {
			URL fileURL =  new URL(path + fileName);
			InputStream fileInputStream = fileURL.openConnection().getInputStream();
			
			URL xmlFileURL = new URL(path + xmlName);
			InputStream xmlInputStream = xmlFileURL.openConnection().getInputStream();

			UsageDBJSONReader jsonReader = new UsageDBJSONReader(xmlInputStream);
			if (jsonReader.readMapFromJSON()) {
				index2paramInfo = jsonReader.getIndex2param();
			} else {
				System.err.println("ERROR: Cannot read xml file: " + xmlName);
				return;
			}
			
			BayesianNetwork bNet = BayesNetSerializer.readBayesNet(fileInputStream);
			BayesNet net = BayesNetSerializer.readNetwork(bNet);
			
			List<InstanceInfo> insInfoList = new ArrayList<InstanceInfo>();
	
			List<NodeProbPair> globaPairs = getRankedNodes(net, globalInsData);
			for (NodeProbPair gpp : globaPairs) {
				InstanceInfo insInfo = new InstanceInfo();
				insInfo.setBaseVar(null);
				BayesNode node = gpp.getNode();
				int colonIndex = node.getName().indexOf(":");
				int paramIndex = Integer.valueOf(node.getName().substring(colonIndex + 1));
				insInfo.setIndexInMap(paramIndex);
				ParamInfo newPi = index2paramInfo.get(paramIndex);
				insInfo.setParamInfo(newPi);
				insInfo.setFreq(gpp.getProb());
				
				insInfoList.add(insInfo);
			}
			
			
			for (InstanceData id : invocationInsDataList) {
				List<NodeProbPair> insPairList = getRankedNodes(net, id);
				
				for (NodeProbPair ipp : insPairList) {
					InstanceInfo insInfo = new InstanceInfo();
					insInfo.setBaseVar(id.getBaseVar());
					BayesNode node = ipp.getNode();
					int colonIndex = node.getName().indexOf(":");
					int paramIndex = Integer.valueOf(node.getName().substring(colonIndex + 1));
					insInfo.setIndexInMap(paramIndex);
					ParamInfo newPi = index2paramInfo.get(paramIndex);
					insInfo.setParamInfo(newPi);
					insInfo.setFreq(ipp.getProb());
					
					insInfoList.add(insInfo);
				}
			}
			
			List<InstanceInfo> sortedInfoList = sortAndMergeInstanceInfo(insInfoList);
			
			/*Now we get the structure of the parameters to recommend,
			 * and then we should map them to the actual parameters
			 */
			Map<String, Double> paramStr2Freq = new HashMap<String, Double>();
			Map<String, ParamInfo> paramStr2Info = new HashMap<String, ParamInfo>();
			for (int i = 0; i < sortedInfoList.size(); i++) {
				InstanceInfo instanceInfo = sortedInfoList.get(i);
				concreteAndGenenateParam(instanceInfo, paramStr2Freq, paramStr2Info);
			}
			
//			generateTempRecommendations(fullPairList);
			Set<Entry<String, Double>> entrySet = paramStr2Freq.entrySet();
			for (Entry<String, Double> entry : entrySet) {
				// TODO: to find better solution to resolve the special case of "this".
				ParamInfo pi = paramStr2Info.get(entry.getKey());
				if (pi != null && pi.getParamStr().equals("this")) {
					String expectedType = formalParam.getParamType();
					String thisType = null;
					if (tpBinding != null) {
						tpBinding.getQualifiedName();
					}
					boolean isTypeCompatible = false;
					if (!expectedType.equals(thisType)) { // check the current type;
						for (ITypeBinding superTb : ancestorClasses) {
							String superType = superTb.getQualifiedName();
							if (superType.equals(expectedType)) {
								isTypeCompatible = true;
								break;
							}
						}
						
						if (!isTypeCompatible) {
							ITypeBinding[] interfaces = tpBinding.getInterfaces();
							if (interfaces != null) {
								for (ITypeBinding tpInter : interfaces) {
									String superInter = tpInter.getQualifiedName();
									if (superInter.equals(expectedType)) {
										isTypeCompatible = true;
										break;
									}
								}
							}
						}
					}
					
					if (!isTypeCompatible) {
						continue;
					}
				}
				
				Candidate c = new Candidate();
				c.setRecommendStr(entry.getKey());
				c.setRecommendConfidence(entry.getValue());
				recommendations.add(c);
			}
			
			Collections.sort(recommendations);
			Collections.reverse(recommendations);
		} catch (MalformedURLException e) {
			System.err.println("ERROR: MalformedURLException " + e.getMessage());
		} catch (IOException e) {
			System.err.println("ERROR: Cannot find Bayesian Network model file: " + fileName);
			System.err.println("ERROR: Cannot read xml file: " + xmlName);	
		}
	}
	
	/**
	 * Sort and list of instance info and remove duplicate items.
	 * 
	 * @param insInfoList - the list of instance info objects to be processed
	 * @return sorted and merged list of instance info
	 */
	private List<InstanceInfo> sortAndMergeInstanceInfo(final List<InstanceInfo> insInfoList) {
		List<InstanceInfo> sortedInfoList = new ArrayList<InstanceInfo>();
		Collections.sort(insInfoList, new InstanceInfoComparator());
		Collections.reverse(insInfoList);
		
		for (InstanceInfo info : insInfoList) {
			boolean isIn = false;
			for (InstanceInfo infoIn : sortedInfoList) {
				// TODO: consider whether to distinguish between global instances and variable instances
				if (infoIn.getIndexInMap() == info.getIndexInMap()) {
					if (infoIn.getBaseVar() == null && info.getBaseVar() == null) {
						isIn = true;
						break;
					} else if (infoIn.getBaseVar() != null && info.getBaseVar() != null) {
						if (infoIn.getBaseVar().equals(info.getBaseVar())) {
							isIn = true;
							break;
						}
					}
				}
			}
			
			if (!isIn) {
				sortedInfoList.add(info);
			}
		}
		
		return sortedInfoList;
	}
	
	/**
	 * Concretize the parameter and update the maps accordingly.
	 * 
	 * @param instanceInfo - information of parameter to be concretized.
	 * @param paramStr2Freq - map between parameter string and its frequency.
	 * @param paramStr2Info - map between parameter string and its detailed information.
	 */
	private void concreteAndGenenateParam(
			final InstanceInfo instanceInfo, 
			final Map<String, Double> paramStr2Freq, 
			final Map<String, ParamInfo> paramStr2Info) {
		Double freq = instanceInfo.getFreq();
		ParamInfo paramInfo = instanceInfo.getParamInfo();
		String paramStr = paramInfo.getParamStr();
		int classDelimiterPos = paramStr.indexOf(":");
		BaseVariable baseVar = instanceInfo.getBaseVar();
		if (paramInfo.getStructureType().equals("MethodInvocation")) {
			concretizeMajorStructures(classDelimiterPos, paramInfo, baseVar, 
					paramStr, freq, "MethodInvocation", paramStr2Freq, paramStr2Info);
		} else if (paramInfo.getStructureType().equals("QualifiedName")) {
			concretizeMajorStructures(classDelimiterPos, paramInfo, baseVar, 
					paramStr, freq, "QualifiedName", paramStr2Freq, paramStr2Info);
		} else if (paramInfo.getStructureType().equals("ArrayAccess")) {
			if (baseVar != null) {
				if (baseVar.getType() != null 
						&& baseVar.getType().getErasure().getQualifiedName().equals(paramStr)) {
					paramStr = baseVar.getName() + "[]";
					
					updateParamMaps(paramStr, freq, paramInfo, paramStr2Freq, paramStr2Info);
				} else {
					return;
				}
			} else {
				for (int j = 0; j < locals.size(); j++) {
					Name local = locals.get(j);
					if (local.resolveTypeBinding() == null) {
						continue;
					}
					
					if (local.resolveTypeBinding().getErasure().getQualifiedName().equals(paramStr)) {
						if (local instanceof SimpleName) {
							SimpleName tmp = (SimpleName) local;
							String newParam;
							newParam = tmp.getIdentifier() + "[]";
							
							updateParamMaps(newParam, freq, paramInfo, paramStr2Freq, paramStr2Info);
						}
					}
				}
				
				for (int k = 0; k < hierarchyField.size(); k++) {
					IVariableBinding var = hierarchyField.get(k);
					if (var.getType().getErasure().getQualifiedName().equals(paramStr)) {
						String newParam;
						newParam = var.getName() + "[]";
						
						updateParamMaps(newParam, freq, paramInfo, paramStr2Freq, paramStr2Info);
					}	
				}
			}	
		} else if (paramInfo.getStructureType().equals("CastExpression")) {
			int namePos = paramStr.indexOf(")");
			if (namePos != -1) {
				String className = paramStr.substring(namePos + 1, paramStr.length());
				if (baseVar != null) {
					if (baseVar.getType() != null 
							&& baseVar.getType().getErasure().getQualifiedName().equals(className)) {
						paramStr = paramStr.substring(0, namePos + 1) + baseVar.getName();
						
						updateParamMaps(paramStr, freq, paramInfo, paramStr2Freq, paramStr2Info);
					} else if (baseVar.getType() != null 
							&& baseVar.getType().getErasure().getQualifiedName().equals(className + "[]")) {
						paramStr = paramStr.substring(0, namePos + 1) + baseVar.getName() + "[]";
						
						updateParamMaps(paramStr, freq, paramInfo, paramStr2Freq, paramStr2Info);
					} else {
						return;
					}
				} else {
					for (int j = 0; j < locals.size(); j++) {
						Name local = locals.get(j);
						if (local.resolveTypeBinding() == null) {
							continue;
						}
						
						if (local.resolveTypeBinding().getErasure().getQualifiedName().equals(className)) {
							if (local instanceof SimpleName) {
								SimpleName tmp = (SimpleName) local;
								String newParam;
								newParam = paramStr.substring(0, namePos + 1) + tmp.getIdentifier();
								updateParamMaps(newParam, freq, paramInfo, paramStr2Freq, paramStr2Info);
							}
						} else if (local.resolveTypeBinding()
									.getErasure().getQualifiedName().equals(className + "[]")) {
							if (local instanceof SimpleName) {
								SimpleName tmp = (SimpleName) local;
								String newParam;
								newParam = paramStr.substring(0, namePos + 1) + tmp.getIdentifier() + "[]";	
								updateParamMaps(newParam, freq, paramInfo, paramStr2Freq, paramStr2Info);
							}
						}
					}
					
					for (int k = 0; k < hierarchyField.size(); k++) {
						IVariableBinding var = hierarchyField.get(k);
						if (var.getType().getErasure().getQualifiedName().equals(className)) {
							String newParam;
							newParam = paramStr.substring(0, namePos + 1) + var.getName();
							updateParamMaps(newParam, freq, paramInfo, paramStr2Freq, paramStr2Info);
						} else if (var.getType().getErasure().getQualifiedName().equals(className + "[]")) {
							String newParam;
							newParam = paramStr.substring(0, namePos + 1) + var.getName() + "[]";
							updateParamMaps(newParam, freq, paramInfo, paramStr2Freq, paramStr2Info);
						}
					}
				}
			}
		} else { // other types of structures
			if (baseVar != null) {
				return;
			}
			updateParamMaps(paramStr, freq, paramInfo, paramStr2Freq, paramStr2Info);
		}
	}
	
	/**
	 * Add evidences from the instance data to the Bayesian network, update
	 * the probabilities, and then rank and return all the nodes.
	 * 
	 * @param net - the Bayesian network
	 * @param id - the instance data
	 * @return the list of ranked nodes
	 */
	private List<NodeProbPair> getRankedNodes(
			final BayesNet net, 
			final InstanceData id) {
		// TODO: fow now we do not treat the types of receivers and locals used in the arguments.
		String rawF2 = id.getFeature2();
		List<String> rawLocalCalls = id.getFeature3();
		List<String> rawRevCalls = id.getFeature7();
		
		JunctionTreeAlgorithm jta = new JunctionTreeAlgorithm();
		jta.setNetwork(net);

		BayesNode mctxNode = net.getNode("MethodContextNode");
		int ctxCount = mctxNode.getOutcomeCount();
		for (int i = 0; i < ctxCount; i++) {
			if (mctxNode.getOutcomeName(i).equals(rawF2)) {
				jta.addEvidence(mctxNode, rawF2);
				break;
			}
		}
		
		// add evidence of feature 3
		List<BayesNode> localNodes = net.getNode("LocalVariableNode").getChildren();
		for (BayesNode bn : localNodes) {
			if (!bn.getName().equals("LocalVariableTypeNode")) { // for now we ignore local type node
				String isObserved = "false";
				for (String rawCall : rawLocalCalls) {
					String callStr = "LocalVariable" + ":" + rawCall;
					if (bn.getName().equals(callStr)) {
						isObserved = "true";
						break;
					}
				}
				
				jta.addEvidence(bn, isObserved);
			}
		}
		
		// add evidence of feature 7
		List<BayesNode> revNodes = net.getNode("ReceiverNode").getChildren();
		for (BayesNode bn : revNodes) {
			if (!bn.getName().equals("ReceiverTypeNode")) { // for now we ignore receiver type node
				String isObserved = "false";
				if (rawRevCalls != null) {
					for (String rawCall : rawRevCalls) {
						String callStr = "Receiver" + ":" + rawCall;
						if (bn.getName().equals(callStr)) {
							isObserved = "true";
							break;
						}
					}
				}
				
				jta.addEvidence(bn, isObserved);
			}
		}
		
		List<NodeProbPair> pairList = new ArrayList<NodeProbPair>();
		List<BayesNode> allNodes = net.getNodes();
		String paramPrefix = "Parameter" + ":";
		for (BayesNode bn : allNodes) {
			double[] probs = jta.getBeliefs(bn);
			if (bn.getName().startsWith(paramPrefix)) {
				probs = jta.getBeliefs(bn);
				NodeProbPair p = new NodeProbPair();
				p.setNode(bn);
				p.setProb(probs[0]);
				pairList.add(p);
			}
		}
		
		return pairList;
	}

	@Override
	public final List<Candidate> getRecommendations() {
		return recommendations;
	}
	
	/**
	 * This method is used to determine whether a string parameter
	 * is a MethodInvocation. As you can image, the logic is not
	 * strict, since it is difficult to do this check on a string
	 * without parsing context.
	 * 
	 * Here we use a simple rule to do the check.
	 * 
	 * This method has been deprecated because the structural 
	 * information can be directly retrieved from the XML files.
	 * 
	 * @param paramStr string representation of a parameter
	 * @return whether the parameter represents a method invocation
	 * @deprecated
	 */
	private static boolean isMethodInvocation(final String paramStr) {
		return paramStr.endsWith(")") || paramStr.endsWith("}");
	}
	
	/**
	 * Similar to the method above, this method uses simple rules
	 * to determine whether a string parameter is a FieldAccess.
	 * 
	 * This method has been deprecated because the structural 
	 * information can be directly retrieved from the XML files.
	 * 
	 * @param paramStr string representation of a parameter
	 * @return  whether the parameter represents a field access
	 * @deprecated
	 */
	private static boolean isFieldAccess(final String paramStr) {
		boolean isFa = false;
		
		if (!paramStr.endsWith(")") && !paramStr.endsWith("}")) {
			// the string contains a base type (followed by ':')
			// and it is not a string literal.
			if (paramStr.contains(":") && !paramStr.contains("\"") && !paramStr.contains("'")) {
				isFa = true;
			}
		}
		return isFa;
	}

	/**
	 * Here major structures include 1) MethodInvocation and
	 * 2) QualifiedName.
	 * 
	 * @param classDelimiterPos - the position of the class delimiter, ":", in the parameter string.
	 * @param paramInfo - the reference to the ParamInfo object which contains main information of the parameter
	 * @param baseVar - the reference to the base variable (it will be null, if the parameter has no base variable)
	 * @param pStr - the string representation of the parameter
	 * @param freq - the frequency of the parameter
	 * @param structureTypeStr - the string constant which indicates the parameter's structure type
	 * @param paramStr2Freq - the map between parameter strings and their frequencies
	 * @param paramStr2Info - the map between parameter strings and their ParamInfo references
	 */
	private void concretizeMajorStructures(
			final int classDelimiterPos, 
			final ParamInfo paramInfo, 
			final BaseVariable baseVar, 
			final String pStr, 
			final Double freq, 
			final String structureTypeStr, 
			final Map<String, Double> paramStr2Freq, 
			final Map<String, ParamInfo> paramStr2Info) {
		String paramStr = pStr;
		String paramVar = null;
		String declareType = null;
		String oType = null;
		String fType = null;
		if (structureTypeStr.equals("MethodInvocation")) {
			paramVar = paramInfo.getMethodInvType().getFirstElementType();
			declareType = paramInfo.getMethodInvType().getDeclareType();
			oType = paramInfo.getMethodInvType().getInvokeType();
			fType = paramInfo.getMethodInvType().getFirstElementType();
		} else if (structureTypeStr.equals("QualifiedName")) {
			paramVar = paramInfo.getQualifiedNaType().getFirstElementType();
			declareType = paramInfo.getQualifiedNaType().getDeclareType();
			oType = paramInfo.getQualifiedNaType().getQualiferType();
			fType = paramInfo.getQualifiedNaType().getFirstElementType();
		} else {
			System.err.println("ERROR: unknown structure type: " + structureTypeStr);
			return;
		}
		if (classDelimiterPos != -1) {
			/*Find out the class name of the invoking type*/
			String staticClassName = paramVar;
			int pkgDelimeterPos = paramVar.lastIndexOf(".");
			if (pkgDelimeterPos != -1) {
				staticClassName = paramVar.substring(pkgDelimeterPos + 1, paramVar.length());
			}
			
			String modifier = null;
			if (structureTypeStr.equals("MethodInvocation")) {
				modifier = paramInfo.getMethodInvType().getModifier();
			} else if (structureTypeStr.equals("QualifiedName")) {
				modifier = paramInfo.getQualifiedNaType().getModifier();
			}
			if (modifier.equals("static")) {
				/*Only instances in the globalIns and instances in the InvocationDataList with the same
				 * name should be chosen in this case */
				if (baseVar == null || (baseVar.getName().equals(staticClassName))) {
					paramStr = paramStr.substring(classDelimiterPos + 1, paramStr.length());
					paramStr = staticClassName + "." + paramStr;
					updateParamMaps(paramStr, freq, paramInfo, paramStr2Freq, paramStr2Info);
				} else {
					return;
				}
			} else { // basevar == null
				if (baseVar != null) {
					if (baseVar.getType() != null && baseVar.getType().getErasure().getQualifiedName().equals(paramVar)
							&& (!baseVar.getName().equals(staticClassName))) {
						paramStr = paramStr.substring(classDelimiterPos + 1, paramStr.length());
						paramStr = baseVar.getName() + "." + paramStr;
						updateParamMaps(paramStr, freq, paramInfo, paramStr2Freq, paramStr2Info);
					} else if (baseVar.getType() != null 
							&& baseVar.getType().getErasure().getQualifiedName().equals(paramVar + "[]")
							&& (!baseVar.getName().equals(staticClassName))) {
						paramStr = paramStr.substring(classDelimiterPos + 1, paramStr.length());
						paramStr = baseVar.getName() + "[]." + paramStr;
						updateParamMaps(paramStr, freq, paramInfo, paramStr2Freq, paramStr2Info);
					} else {
						return;
					}
				} else {
					/*If the basevar is null, it means that the structure of the parameter is chosen from
					 * the globalIns. We search the variables from the locals and hierarchyField to find 
					 * compatible ones.*/
					/*handle "this" problem*/
					if (oType.equals(fType)) {
						ITypeBinding tdBinding = typeDecl.resolveBinding();
						if (tdBinding.getErasure().getQualifiedName().equals(paramVar))	{
							String newParam;
							newParam = paramStr.substring(classDelimiterPos + 1, paramStr.length());
							newParam = "this" + "." + newParam;
							updateParamMaps(newParam, freq, paramInfo, paramStr2Freq, paramStr2Info);
						} else {
							while (tdBinding != null) {
								if (tdBinding.getErasure().getQualifiedName().equals(declareType)) {
									String newParam;
									newParam = paramStr.substring(classDelimiterPos + 1, paramStr.length());
									newParam = "this" + "." + newParam;
									updateParamMaps(newParam, freq, paramInfo, paramStr2Freq, paramStr2Info);
									break;
								}
								tdBinding = tdBinding.getSuperclass();
							}
						}
					}

					for (int j = 0; j < locals.size(); j++) {
						Name local = locals.get(j);
						if (local.resolveTypeBinding() == null) {
							continue;
						}
						if (local.resolveTypeBinding().getErasure().getQualifiedName().equals(paramVar)) {
							if (local instanceof SimpleName) {
								SimpleName tmp = (SimpleName) local;
								String newParam;
								newParam = paramStr.substring(classDelimiterPos + 1, paramStr.length());
								newParam = tmp.getIdentifier() + "." + newParam;	
								updateParamMaps(newParam, freq, paramInfo, paramStr2Freq, paramStr2Info);
							}
						} else if (local.resolveTypeBinding().getErasure().getQualifiedName().equals(paramVar + "[]")) {
							if (local instanceof SimpleName) {
								SimpleName tmp = (SimpleName) local;
								String newParam;
								newParam = paramStr.substring(classDelimiterPos + 1, paramStr.length());
								newParam = tmp.getIdentifier() + "[]." + newParam;	
								updateParamMaps(newParam, freq, paramInfo, paramStr2Freq, paramStr2Info);
							}
						} else {
							if (local instanceof SimpleName) {
								SimpleName tmp = (SimpleName) local;
								if (oType.equals(fType)) {
									ITypeBinding tmpBinding = local.resolveTypeBinding();
									while (tmpBinding != null) {
										if (tmpBinding.getErasure().getQualifiedName().equals(declareType))	{
											String newParam;
											newParam = paramStr.substring(classDelimiterPos + 1, paramStr.length());
											newParam = tmp.getIdentifier() + "." + newParam;
											updateParamMaps(newParam, freq, paramInfo, paramStr2Freq, paramStr2Info);
											break;
										} else if 
											(tmpBinding.getErasure().getQualifiedName().equals(declareType + "[]")) {
											String newParam;
											newParam = paramStr.substring(classDelimiterPos + 1, paramStr.length());
											newParam = tmp.getIdentifier() + "[]." + newParam;
											updateParamMaps(newParam, freq, paramInfo, paramStr2Freq, paramStr2Info);
											break;
										}
										tmpBinding = tmpBinding.getSuperclass();
									}
								}
							}

						}
					}
					
					for (int k = 0; k < hierarchyField.size(); k++) {
						IVariableBinding var = hierarchyField.get(k);
						if (var.getType().getErasure().getQualifiedName().equals(paramVar)) {
							String newParam;
							newParam = paramStr.substring(classDelimiterPos + 1, paramStr.length());
							newParam = var.getName() + "." + newParam;
							updateParamMaps(newParam, freq, paramInfo, paramStr2Freq, paramStr2Info);
						} else if (var.getType().getErasure().getQualifiedName().equals(paramVar + "[]")) {
							String newParam;
							newParam = paramStr.substring(classDelimiterPos + 1, paramStr.length());
							newParam = var.getName() + "[]." + newParam;
							updateParamMaps(newParam, freq, paramInfo, paramStr2Freq, paramStr2Info);
						} else {
							if (oType.equals(fType)) {
								ITypeBinding tmpBinding = var.getType();
								while (tmpBinding != null) {
									if (tmpBinding.getErasure().getQualifiedName().equals(declareType)) {
										String newParam;
										newParam = paramStr.substring(classDelimiterPos + 1, paramStr.length());
										newParam = var.getName() + "." + newParam;
										updateParamMaps(newParam, freq, paramInfo, paramStr2Freq, paramStr2Info);
										break;
									} else if (tmpBinding.getErasure().getQualifiedName().equals(declareType + "[]")) {
										String newParam;
										newParam = paramStr.substring(classDelimiterPos + 1, paramStr.length());
										newParam = var.getName() + "[]." + newParam;
										updateParamMaps(newParam, freq, paramInfo, paramStr2Freq, paramStr2Info);
										break;
									}
									tmpBinding = tmpBinding.getSuperclass();
								}
							}
							
						}
					}
				}
			}
		} else {
			// this else branch is just designed from local method invocations...
			if (structureTypeStr.equals("QualifiedName")) {
				return;
			}
			
			//mean it is a local method invocation. Now we just add it to the map without checking
			if (baseVar == null) {
				if (oType.equals(fType)) {
					ITypeBinding tdBinding = typeDecl.resolveBinding();
					if (tdBinding.getErasure().getQualifiedName().equals(paramVar))	{
						String newParam;
						newParam = paramStr.substring(classDelimiterPos + 1, paramStr.length());
						newParam = "this" + "." + newParam;
						updateParamMaps(newParam, freq, paramInfo, paramStr2Freq, paramStr2Info);
					} else {
						while (tdBinding != null) {
							if (tdBinding.getErasure().getQualifiedName().equals(declareType)) {
								String newParam;
								newParam = paramStr.substring(classDelimiterPos + 1, paramStr.length());
								newParam = "this" + "." + newParam;
								updateParamMaps(newParam, freq, paramInfo, paramStr2Freq, paramStr2Info);
								break;
							}
							tdBinding = tdBinding.getSuperclass();
						}
					}
				}
			}
		}						
	}
	
	/**
	 * Update the two maps based on parameter info and frequency.
	 * 
	 * @param paramStr - parameter string
	 * @param freq - frequency of parameter instance
	 * @param paramInfo - detailed information of parameter
	 * @param paramStr2Freq - map between parameter string and its frequency
	 * @param paramStr2Info - map between parameter string and its detailed information
	 */
	private void updateParamMaps(
			final String paramStr, 
			final Double freq, 
			final ParamInfo paramInfo,
			final Map<String, Double> paramStr2Freq, 
			final Map<String, ParamInfo> paramStr2Info) {
		if (paramStr2Freq.containsKey(paramStr)) {
			Double newFreq = paramStr2Freq.get(paramStr) + freq;
			paramStr2Freq.put(paramStr, newFreq);
		} else {
			paramStr2Freq.put(paramStr, freq);
			paramStr2Info.put(paramStr, paramInfo);
		}
	}
}
