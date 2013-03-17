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

/**
 * The entity object of receiver variable.
 * 
 * @author ChengZhang
 *
 */
public class ReceiverVar {
	/**
	 * the type name of the receiver variable.
	 */
	private String type;
	
	/**
	 * the represent string of the receiver variable.
	 */
	private String strRep;

	public final String getType() {
		return type;
	}
	
	public final void setType(final String t) {
		this.type = t;
	}
	
	public final String getStrRep() {
		return strRep;
	}
	
	public final void setStrRep(final String sRep) {
		this.strRep = sRep;
	}
}
