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
 * This enumeration describes all available constant for MessageID as
 * defined by FIPA for Bit-Efficient encoding, 
 * and their setter (used for decoding process - java reflection tips).
 * <p>
 * The first byte defines the message identifier. The identifier byte
 * can be used to separate bit-efficient ACL messages
 * from (for example) string-based messages and separate different
 * coding schemes.
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
public enum MessageID {
	
	/** The value 0xFA defines a bit- efficient coding scheme without dynamic code tables.
	 * <p>
	 * Code: {@code 0xFA}.
	 */
	BITEFFICIENT((byte) 0xFA),
	/** The value 0xFB defines a bit-efficient coding scheme with dynamic code tables.
	 * <p>
	 * Code: {@code 0xFB}.
	 */
	BITEFFICIENT_CODETABLE ((byte) 0xFB),
	/** The message identifier 0xFC is used when dynamic code tables are being used, 
	 * but the sender does not want to update code tables (even if message contains
	 * strings that should be added to code table).
	 * <p>
	 * Code: {@code 0xFC}.
	 */ 
	BITEFFICIENT_NO_CODETABLE ((byte) 0xFC);
	
	private final byte code;
	
	private MessageID(byte code){
		this.code = code;
	}
	
	/** Replies the code from the FIPA specification.
	 * 
	 * @return the code.
	 */
	public byte getCode() {
		return this.code;
	}
	

}
