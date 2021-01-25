/* 
 * $Id$
 * 
 * SARL is an general-purpose agent programming language.
 * More details on http://www.sarl.io
 * 
 * Copyright (C) 2014-2021 the original authors or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.sarl.acl.protocol;

/**
 * This enumeration describes all available protocols :
 * <ul>
 * <li>FIPA_REQUEST : Request Protocol</li>
 * <li>FIPA_CONTRACT_NET : CNP Contract Net Protocol</li>
 * </ul>
 * <p>
 * Please refer to the following links to get more information about those protocols :
 * <ul>
 * <li><a href="http://www.fipa.org/specs/fipa00026/SC00026H.html">FIPA Request Interaction Protocol Specification</a></li>
 * <li><a href="http://www.fipa.org/specs/fipa00029/SC00029H.html">FIPA Contract Net Interaction Protocol Specification</a></li>
 * </ul>
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $FullVersion$
 * @mavengroupid $Groupid$
 * @mavenartifactid $ArtifactId$
 */
public enum EnumFipaProtocol {
	/**
	 */
	NONE("none"), //$NON-NLS-1$
	/**
	 */
	FIPA_REQUEST("fipa-request"), //$NON-NLS-1$
	/**
	 */
	FIPA_CONTRACT_NET("fipa-contract-net"), //$NON-NLS-1$
	/**
	 */
	FIPA_PROPOSE("fipa-propose"), //$NON-NLS-1$
	/**
	 */
	FIPA_QUERY("fipa-query"); //$NON-NLS-1$
	
	private final String name;
	
	EnumFipaProtocol(String name) {
		this.name = name;
	}
	
	/**
	 * @return the name of the protocol
	 */
	public String getFipaName() { 
		return this.name; 
	} 

	/**
	 * @param name
	 * @return the field of the protocol enum corresponding to the specified string
	 * @throws IllegalArgumentException
	 */
	public static EnumFipaProtocol valueOfByName(String name) throws IllegalArgumentException { 
		for (EnumFipaProtocol value : values()) { 
			if (value.getFipaName().equalsIgnoreCase(name)) { 
				return value; 
			} 
		} 
		return EnumFipaProtocol.NONE; 
	}

}
