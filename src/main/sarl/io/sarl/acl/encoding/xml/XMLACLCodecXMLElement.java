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

package io.sarl.acl.encoding.xml;

/**
 * This enumeration describes all available message param (name of the xml tags) as defined by FIPA for XML encoding, 
 * and their setter (used for decoding process - java reflection tips)
 * 
 * @see <a href="http://www.fipa.org/specs/fipa00071/SC00071E.html">FIPA ACL Message Representation in XML Specification</a> 
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $FullVersion$
 * @mavengroupid $Groupid$
 * @mavenartifactid $ArtifactId$
 */
public enum XMLACLCodecXMLElement {
	/**
	 */
	RECEIVER("receiver"), //$NON-NLS-1$
	/**
	 */
	SENDER("sender"), //$NON-NLS-1$
	/**
	 */
	CONTENT("content"), //$NON-NLS-1$
	/**
	 */
	LANGUAGE("language"), //$NON-NLS-1$
	/**
	 */
	ENCODING("encoding"), //$NON-NLS-1$
	/**
	 */
	ONTOLOGY("ontology"), //$NON-NLS-1$
	/**
	 */
	PROTOCOL("protocol"), //$NON-NLS-1$
	/**
	 */
	REPLY_WITH("reply-with"), //$NON-NLS-1$
	/**
	 */
	IN_REPLY_TO("in-reply-to"), //$NON-NLS-1$
	/**
	 */
	REPLY_BY("reply-by"), //$NON-NLS-1$
	/**
	 */
	REPLY_TO("reply-to"), //$NON-NLS-1$
	/**
	 */
	CONVERSATION_ID("conversation-id"), //$NON-NLS-1$
	/**
	 */
	PERFORMATIVE("performative"); //$NON-NLS-1$

	private final String tag;
	
	XMLACLCodecXMLElement(String tag) {
		this.tag = tag;
	}

	/**
	 * @return the name tag of the msg param
	 */
	public String getTag() { 
		return this.tag; 
	} 
	
}
