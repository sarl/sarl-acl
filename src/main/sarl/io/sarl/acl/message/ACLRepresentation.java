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
