/*
 * $Id$
 *
 * SARL is an general-purpose agent programming language.
 * More details on http://www.sarl.io
 *
 * Copyright (C) 2014-2017 the original authors or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.sarl.acl.encoding.bitefficient.constant;

/**
 * This enumeration describes all available constant 
 * for PredefinedMsgParam as defined by FIPA for Bit-Efficient encoding, 
 * and their setter (used for decoding process - java reflection tips)
 * 
 * @see <a href="http://www.fipa.org/specs/fipa00069/SC00069G.html">FIPA ACL Message Representation in Bit-Efficient Specification</a> 
 * 
 * @author $Author: flacreus$
 * @author $Author: sroth$
 * @author $Author: cstentz$
 * @version $FullVersion$
 * @mavengroupid $Groupid$
 * @mavenartifactid $ArtifactId$
 */

public enum PredefinedMsgParam {

	/**
	 * The parameter is the sender.
	 * <p>
	 * Code: {@code 0x02}.
	 */
	PARAM_SENDER ( (byte) 0x02),
	/**
	 * The parameter is the receiver.
	 * <p>
	 * Code: {@code 0x03}.
	 */
	PARAM_RECEIVER ( (byte) 0x03),
	/**
	 * The parameter is the content.
	 * <p>
	 * Code: {@code 0x04}.
	 */
	PARAM_CONTENT ( (byte) 0x04),
	/**
	 * The parameter is the reply-with.
	 * <p>
	 * Code: {@code 0x05}.
	 */
	PARAM_REPLY_WITH ( (byte) 0x05),
	/**
	 * The parameter is the reply-by.
	 * <p>
	 * Code: {@code 0x06}.
	 */
	PARAM_REPLY_BY ( (byte) 0x06),
	/**
	 * The parameter is the in-reply-to.
	 * <p>
	 * Code: {@code 0x07}.
	 */
	PARAM_IN_REPLY_TO ( (byte) 0x07),
	/**
	 * The parameter is the reply-to.
	 * <p>
	 * Code: {@code 0x08}.
	 */
	PARAM_REPLY_TO ( (byte) 0x08),
	/**
	 * The parameter is the language.
	 * <p>
	 * Code: {@code 0x09}.
	 */
	PARAM_LANGUAGE ( (byte) 0x09),
	/**
	 * The parameter is the encoding.
	 * <p>
	 * Code: {@code 0x0A}.
	 */
	PARAM_ENCODING ( (byte) 0x0A),
	/**
	 * The parameter is the ontology id.
	 * <p>
	 * Code: {@code 0x0B}.
	 */
	PARAM_ONTOLOGY ( (byte) 0x0B),
	/**
	 * The parameter is the protocol id.
	 * <p>
	 * Code: {@code 0x0C}.
	 */
	PARAM_PROTOCOL ( (byte) 0x0C),
	/**
	 * The parameter is the conversation id.
	 * <p>
	 * Code: {@code 0x0D}.
	 */
	PARAM_CONVERSATION_ID ( (byte) 0x0D);

	private final byte code;

	private PredefinedMsgParam(byte code){
		this.code = code;
	}

	/** Replies the code from the FIPA specification.
	 * 
	 * @return the code.
	 */
	public byte getCode(){
		return this.code;
	}
}
