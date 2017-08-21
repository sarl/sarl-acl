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
 * This enumeration describes all available constant for BinDateTimeToken as defined by FIPA for Bit-Efficient encoding, 
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

public enum BinDateTimeToken {
	/** Absolute time.
	 * <p>
	 * Code: {@code 0x20}.
	 */
	ABS_TIME((byte) 0x20),
	/** Relative positive time.
	 * <p>
	 * Code: {@code 0x21}.
	 */
	REL_TIME_POS((byte) 0x21),
	/** Relative negative time.
	 * <p>
	 * Code: {@code 0x22}.
	 */
	REL_TIME_NEG((byte) 0x22),
	/** Absolute time with type designator.
	 * <p>
	 * Code: {@code 0x24}.
	 */
	ABS_TIME_TYPE_DESIGNATOR((byte) 0x24),
	/** Relative time positive with type designator.
	 * <p>
	 * Code: {@code 0x25}.
	 */
	REL_TIME_POS_TYPE_DESIGNATOR((byte) 0x25),
	/** Relative time negative with type designator.
	 * <p>
	 * Code: {@code 0x26}.
	 */
	REL_TIME_NEG_TYPE_DESIGNATOR((byte) 0x26);
	
	private final byte code;
	
	private BinDateTimeToken(byte code){
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
