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

package io.sarl.acl.encoding.bitefficient.constant;

import org.eclipse.xtext.xbase.lib.Pure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.base.Strings;

/**
 * This enumeration describes all available constant for AgentIdentifier as defined by FIPA for Bit-Efficient encoding, 
 * and their setter (used for decoding process - java reflection tips)
 * 
 * @author $Author: flacreus$
 * @author $Author: sroth$
 * @author $Author: cstentz$
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $Groupid$
 * @mavenartifactid $ArtifactId$
 * @since 0.12
 * @see <a href="http://www.fipa.org/specs/fipa00069/SC00069G.html">FIPA ACL Message Representation in Bit-Efficient Specification</a> 
 */
public enum AgentIdentifier {

	/** Begin the name or the identifier of an agent.
	 * 
	 * <p>Code: {@code 0x02}.
	 */
	AGENT_NAME_BEGIN((byte) 0x02),
	
	/** End the name or the identifier of an agent.
	 * 
	 * <p>Code: {@code 0x00}.
	 */
	AGENT_NAME_END((byte) 0x00);

	private final byte code;
	
	private AgentIdentifier(byte code){
		this.code = code;
	}

	/** Replies the code from the FIPA specification.
	 * 
	 * @return the code.
	 */
	@Pure
	public byte getBinaryCode() {
		return this.code;
	}

	/** Replies the Json string representation of this identifier.
	 *
	 * @return the Json string representation.
	 */
	@JsonValue
	@Pure
	public String toJsonString() {
		return name().toLowerCase();
	}

	/** Parse the given case insensitive string for obtaining the identifier.
	 *
	 * @param name the string to parse.
	 * @return the type.
	 * @throws NullPointerException when the specified name is null
	 */
	@JsonCreator
	@Pure
	public static AgentIdentifier valueOfCaseInsensitive(String name) {
		if (Strings.isNullOrEmpty(name)) {
			throw new NullPointerException("name is null"); //$NON-NLS-1$
		}
		final String ucname = name.toUpperCase();
		final String lcname = name.toLowerCase();
		try {
			for (final AgentIdentifier expr : values()) {
				if (ucname.equals(expr.name())) {
					return expr;
				}
				if (lcname.equals(expr.toJsonString())) {
					return expr;
				}
			}
		} catch (Throwable exception) {
			//
		}
		throw new IllegalArgumentException("illegal value for name: " + name); //$NON-NLS-1$
	}	

	/** Replies the Json labels for the identifier.
	 *
	 * @return the labels.
	 */
	@Pure
	public static String getJsonLabels() {
		final StringBuilder buffer = new StringBuilder();
		boolean first = true;
		for (final AgentIdentifier expr : values()) {
			if (first) {
				first = false;
			} else {
				buffer.append(", "); //$NON-NLS-1$
			}
			buffer.append(expr.toJsonString());
		}
		return buffer.toString();
	}

}
