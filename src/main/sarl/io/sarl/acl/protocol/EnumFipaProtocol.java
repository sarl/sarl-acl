/* 
 * $Id$
 * 
 * Janus platform is an open-source multiagent platform.
 * More details on <http://www.janus-project.org>
 * Copyright (C) 2012 Janus Core Developers
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
