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

import java.util.Comparator;

/**
 * The comparator implementation used to compare 
 * instances of InstanceInfo based on their frequencies.
 * 
 * @author ChengZhang
 *
 */
public class InstanceInfoComparator implements Comparator<InstanceInfo> {

	@Override
	public final int compare(final InstanceInfo if1, final InstanceInfo if2) {
		if (if1.getFreq() > if2.getFreq()) {
			return 1;
		} else if (if1.getFreq() < if2.getFreq()) {
			return -1;
		}
		
		return 0;
	}

}
