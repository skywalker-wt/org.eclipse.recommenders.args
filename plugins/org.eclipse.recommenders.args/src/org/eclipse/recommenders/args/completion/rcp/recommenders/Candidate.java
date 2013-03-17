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

/**
 * Simple representation of the generated parameter recommendations.
 * Each instance corresponds to one recommendation item.
 * 
 * @author ChengZhang
 *
 */
public class Candidate implements Comparable<Candidate> {
	/** string representation of the recommended parameter. */
	private String recommendStr;
	/** confidence score of the recommended parameter. */
	private double recommendConfidence;
	
	public final String getRecommendStr() {
		return recommendStr;
	}
	public final void setRecommendStr(final String recStr) {
		this.recommendStr = recStr;
	}
	public final double getRecommendConfidence() {
		return recommendConfidence;
	}
	public final void setRecommendConfidence(final double recConfidence) {
		this.recommendConfidence = recConfidence;
	}
	
	@Override
	public final int compareTo(final Candidate o) {
		if (this.recommendConfidence > o.recommendConfidence) {
			return 1;
		} else if (this.recommendConfidence < o.recommendConfidence) {
			return -1;
		}
		
		return 0;
	}
}
