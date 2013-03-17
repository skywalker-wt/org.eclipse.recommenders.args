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
package org.eclipse.recommenders.args.completion.rcp.bn;

import org.eclipse.recommenders.jayes.BayesNode;

/**
 * This class is another from of BayesNode.
 * Note: this class is used for sort by probability.
 * 
 * @author ChengZhang
 *
 */
public class NodeProbPair implements Comparable {
	/**
	 * The reference of BayesNode object.
	 */
	private BayesNode node;
	
	/**
	 * The probability of the BayesNode.
	 * Note: 
	 * This num of probability is not contained in BayesNode.
	 * So need to be set outside of the class manually. 
	 */
	private double prob;
	
	/**
	 * Gets the reference of BayesNode object.
	 * 
	 * @return BayesNode object.
	 */
	public final BayesNode getNode() {
		return node;
	}


	/**
	 * Sets the BayesNode object.
	 * 
	 * @param n - reference of BayesNode object
	 */
	public final void setNode(final BayesNode n) {
		this.node = n;
	}

	/**
	 * Gets the probability of the BayesNode.
	 * Note: 
	 * This num of probability is not contained in BayesNode.
	 * So need to be set outside of the class manually. 
	 * @return the probability of the BayesNode
	 */
	public final double getProb() {
		return prob;
	}


	/**
	 * Sets the probability of the BayesNode.
	 * Note: 
	 * This num of probability is not contained in BayesNode.
	 * So need to be set outside of the class manually. 
	 * 
	 * @param p - the probability of the BayesNode
	 */
	public final void setProb(final double p) {
		this.prob = p;
	}


	@Override
	public final int compareTo(final Object obj) {
		NodeProbPair p2 = (NodeProbPair) obj;
		if (this.prob > p2.prob) {
			return 1;
		} else if (this.prob < p2.prob) {
			return -1;
		}
		
		return 0;
	}
	
}
