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
package io.sarl.acl.message;

/**
 * This enumeration describes all available ACL Representations as defined by Fipa.
 * <ul>
 * <li>BIT_EFFICIENT</li>
 * <li>STRING</li>
 * <li>XML</li>
 * </ul>
 * 
 * @see <a href="http://www.fipa.org/repository/aclreps.php3">FIPA ACL Representation Specifications</a>
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $FullVersion$
 * @mavengroupid $Groupid$
 * @mavenartifactid $ArtifactId$
 */
public enum ACLRepresentation {
	/**
	 * 
	 */
	BIT_EFFICIENT("fipa.acl.rep.bitefficient.std"), //$NON-NLS-1$
	/**
	 * 
	 */
	STRING("fipa.acl.rep.string.std"), //$NON-NLS-1$
	/**
	 *
	 */
	JSON("set.acl.rep.json.std"), //$NON-NLS-1$
	/**
	 *
	 */
	BSON("set.acl.rep.bson.std"), //$NON-NLS-1$
	/**
	 *
	 */
	XML("fipa.acl.rep.xml.std"); //$NON-NLS-1$
	  
	private final String value;
	
	ACLRepresentation(String value) {
		this.value = value;
	}

	/**
	 * @return the string value associated to an enum case
	 */
	public String getValue() {
		return this.value;
	}
}
