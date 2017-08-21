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
 * This enumeration describes all available constant for ExprStart as 
 * defined by FIPA for Bit-Efficient encoding, 
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

public enum ExprStart {

	/** Level down.
	 * <p>
	 * Code: {@code 0x60}.
	 */
	LEVEL_DOWN((byte) 0x60),
	/** Begin a word.
	 * <p>
	 * Code: {@code 0x70}.
	 */
	WORD_BEGIN((byte) 0x70),
	/** End a word.
	 * <p>
	 * Code: {@code 0x00}.
	 */
	WORD_END((byte) 0x00),
	/** Begin the code of an index word.
	 * <p>
	 * Code: {@code 0x71}.
	 */
	INDEX_WORD_CODE_BEGIN((byte) 0x71),
	/** Begin a number.
	 * <p>
	 * Code: {@code 0x72}.
	 */
	NUMBER_BEGIN((byte) 0x72),
	/** Begin an hexadecimal number.
	 * <p>
	 * Code: {@code 0x73}.
	 */
	HEXA_NUMBER_BEGIN((byte) 0x73),
	/** Begin a string.
	 * <p>
	 * Code: {@code 0x74}.
	 */
	STRING_BEGIN((byte) 0x74),
	/** End a string.
	 * <p>
	 * Code: {@code 0x00}.
	 */
	STRING_END((byte) 0x00),
	/** Begin an index string.
	 * <p>
	 * Code: {@code 0x75}.
	 */
	INDEX_STRING_BEGIN((byte) 0x75),
	/** Begin an len8 string.
	 * <p>
	 * Code: {@code 0x76}.
	 */
	LEN8_STRING_BEGIN((byte) 0x76),
	/** Begin an len16 string.
	 * <p>
	 * Code: {@code 0x77}.
	 */
	LEN16_STRING_BEGIN((byte) 0x77),
	/** Begin a len32 string.
	 * <p>
	 * Code: {@code 0x78}.
	 */
	LEN32_STRING_BEGIN((byte) 0x78),
	/** Begin a string of index byte.
	 * <p>
	 * Code: {@code 0x79}.
	 */
	INDEX_BYTE_STRING_BEGIN((byte) 0x79);
	
	private final byte code;
	
	private ExprStart(byte code) {
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
